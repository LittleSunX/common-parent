package com.czxy.bos.controller.base.take_delivery;

import com.czxy.bos.domain.base.Area;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.SubArea;
import com.czxy.bos.domain.take_delivery.Order;
import com.czxy.bos.service.base.AreaService;
import com.czxy.bos.service.base.CourierService;
import com.czxy.bos.service.base.SubAreaService;
import com.czxy.bos.service.base.take_delivery.BosOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/bosOrder")
public class BosOrderController {
    @Resource
    private BosOrderService bosOrderService;
    @Resource
    private AreaService areaService;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CourierService courierService;
    @Resource
    private SubAreaService subAreaService;


    @PostMapping
    public ResponseEntity<String> add(@RequestBody Order order){
        System.out.println(order);
      //  System.out.println(order);
        //1 处理数据(订单号、时间、状态、地址id)
        order.setOrderNum(UUID.randomUUID().toString());
        order.setOrderTime(new Date());
        order.setOrderType("1");    //订单状态 1 待取件 2 运输中 3 已签收 4 异常
        //寄件人地址
        Area sendArea = areaService.selectByArea(order.getSendArea());
        order.setSendAreaId(sendArea.getId());
        //收件人地址
        Area recArea = areaService.selectByArea(order.getRecArea());
        order.setRecAreaId(recArea.getId());
        //2 自动分单：CRM地址完全匹配
        String url = "http://localhost:8090/crmCustomer/findFixdAreaIdByAddressAndID?address="+order.getSendAddress()+"&customerId=" + order.getCustomer_id();
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        String fixedAreaId = entity.getBody();
        System.out.println(fixedAreaId);
        //2.1  2) 查询定区管理的所有的快递员
        List<Courier> courierList = courierService.findAssociationCourier(fixedAreaId);
        //2.1  3) 分单--将订单关联一下快递员
        ResponseEntity<String> temp= autoSaveOrder(order , courierList );
        if(temp != null){
            return temp;
        }
        //2.2 地址关键字匹配：通过发货地址，确定区域area，获得区域下所有分区（子区域 sub_area），通过分区关键字和辅助关键字匹配，获得匹配到分区，从而获得定区，再获得快递员
        //  area --> sub_area --> fixedArea --> courier
        //2.2 1) 通过发货地址区域，获得所有的子区域
        List<SubArea> subAreaList = subAreaService.findAllByAreaId(sendArea.getId());
        //2.2 2) 子区域【关键字匹配】，获得对应的定区id
        for (SubArea subArea : subAreaList) {
            //  包含信息建材城西路
            if(order.getSendAddress().contains( subArea.getKeyWords() )){
                courierList=courierService.findAssociationCourier(subArea.getFixedAreaId());
                //2.2 4) 给订单绑定快递员
                temp = autoSaveOrder(order , courierList );
                if(temp != null){
                    return temp;
                }

            }

        }
        //2.3 地址【辅助关键字】匹配 (以“关键字”匹配为主，如果没有匹配上，再通过“辅助关键字”进行匹配)
        for(SubArea subArea : subAreaList){
            // 北京市海淀区建材城西路金燕龙办公楼一层  包含 建材城西路
            if(order.getSendAddress().contains( subArea.getAssistKeyWords() )){
                //2.2 3) 根据定区查询关联的快递员
                List<Courier> couriersList = courierService.findAssociationCourier( subArea.getFixedAreaId() );     //注意：提供SubArea没有生产getter和setter
                //2.2 4) 给订单绑定快递员
                temp = autoSaveOrder(order , courierList );
                if(temp != null){
                    return temp;
                }
            }
        }

        //3人工分单
        order.setOrderType("2");
        bosOrderService.addOrder(order);
        //生成工单
        return new ResponseEntity<>("添加订单成功", HttpStatus.OK);
    }

    private ResponseEntity<String> autoSaveOrder(Order order, List<Courier> courierList) {
        for (Courier courier : courierList) {
            if(courier != null){
                //将订单和快递员关联
                order.setCourierId(courier.getId());
                //自动分单成功
                order.setOrderType("1");
                //保存
                bosOrderService.addOrder(order);
                return new ResponseEntity<>("创建订单成功，已经成功自动分单！",HttpStatus.OK);
            }

        }
        return null;
    }
}
