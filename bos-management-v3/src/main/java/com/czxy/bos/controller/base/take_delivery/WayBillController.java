package com.czxy.bos.controller.base.take_delivery;

import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.es.domain.ESWayBill;
import com.czxy.bos.service.base.take_delivery.WayBillService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wayBill")
public class WayBillController {
    @Resource
    private WayBillService wayBillService;



    /**
     * 分页查询
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/pageQuickQuery")
    public ResponseEntity<EasyUIResult<WayBill>> pageQuickQuery(Integer page, Integer rows){
        PageInfo<WayBill> pageInfo = wayBillService.pageQuickQuery(page, rows);
        EasyUIResult<WayBill> result = new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList());
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 添加或更新操作
     * @param wayBill
     * @return
     */
    @PostMapping("/saveOrUpdateWayBill")
     public ResponseEntity<String> saveOrUpdateWayBill (WayBill wayBill){
        //处理数据  待发货状态
        wayBill.setSignStatus(1);
        //保存
        wayBillService.saveOrUpdateWayBill(wayBill);
        //提示
        return new ResponseEntity<>("运单录入成功",HttpStatus.OK);
     }

    /**
     *根据运单号查询
     * @param wayBillNum
     * @return
     */
    @GetMapping("/findByWayBillNum")
    public ResponseEntity<WayBill> findByWayBillNum(String wayBillNum) {
        // 调用业务层 查询
        WayBill wayBill = wayBillService.findByWayBillNum(wayBillNum);

        return new ResponseEntity<>(wayBill , HttpStatus.OK);
    }
    @GetMapping("/pageQuery")
    public ResponseEntity<EasyUIResult<ESWayBill>> pageQuery(ESWayBill wayBill , Integer page , Integer rows){
        Page<ESWayBill> pageBean = this.wayBillService.pageQuery(wayBill , page, rows);
        EasyUIResult<ESWayBill> result = new EasyUIResult<>(pageBean.getTotalElements() ,pageBean.getContent());
        return new ResponseEntity<>(result , HttpStatus.OK);
    }



}
