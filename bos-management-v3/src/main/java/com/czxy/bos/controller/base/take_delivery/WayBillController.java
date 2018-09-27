package com.czxy.bos.controller.base.take_delivery;

import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.service.base.take_delivery.WayBillService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wayBill")
public class WayBillController {
    @Resource
    private WayBillService wayBillService;
    @GetMapping("/pageQuickQuery")
    public ResponseEntity<EasyUIResult<WayBill>> pageQuickQuery(Integer page, Integer rows){
        PageInfo<WayBill> pageInfo = wayBillService.pageQuickQuery(page, rows);
        EasyUIResult<WayBill> result = new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList());
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }
}
