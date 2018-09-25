package com.czxy.bos.service.base.take_delivery;

import com.czxy.bos.dao.base.take_delivery.BosOrderMapper;
import com.czxy.bos.dao.base.take_delivery.WorkBillMapper;
import com.czxy.bos.domain.take_delivery.Order;
import com.czxy.bos.domain.take_delivery.WorkBill;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional
public class BosOrderService {
    @Resource
    private BosOrderMapper bosOrderMapper;
    @Resource
    private WorkBillMapper workBillMapper;

    /**
     * 保存订单
     * @param order
     */
    public  void  addOrder(Order order){
        //保存订单
        bosOrderMapper.insert(order);
        //保存工单
        WorkBill workBill = new WorkBill();
        //类型 ， 工单类型 新,追,销
        workBill.setType("新");
        //状态  取件状态
        workBill.setPickstate("新单");
        //时间 工单生成时间
        workBill.setBuildtime( new Date() );
        //备注
        workBill.setRemark( order.getRemark() );
        //短信序号 -- 发短信省略
        workBill.setSmsNumber(RandomStringUtils.randomNumeric(4) );
        //订单号
        workBill.setOrderId( order.getId() );           //注意WorkBill没有提供setter方法
        //快递员号
        workBill.setCourierId( order.getCourierId() );  //注意WorkBill没有提供setter方法


        workBillMapper.insert( workBill );

    }

}
