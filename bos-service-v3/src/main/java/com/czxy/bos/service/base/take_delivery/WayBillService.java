package com.czxy.bos.service.base.take_delivery;

import com.czxy.bos.dao.base.take_delivery.WayBillMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class WayBillService {
    @Resource
    private WayBillMapper wayBillMapper;

    /**
     * 分页查询
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<WayBill> pageQuickQuery(Integer page, Integer rows){
        PageHelper.startPage(page,rows);
        List<WayBill> wayBills = wayBillMapper.selectAll();
        return  new PageInfo<>(wayBills);
    }
}
