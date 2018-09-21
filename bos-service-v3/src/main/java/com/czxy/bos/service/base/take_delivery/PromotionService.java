package com.czxy.bos.service.base.take_delivery;

import com.czxy.bos.dao.base.take_delivery.PromotionMapper;
import com.czxy.bos.domain.take_delivery.Promotion;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class PromotionService {
    @Resource
    private PromotionMapper promotionMapper;

    public void save(Promotion promotion) {
        promotionMapper.insert(promotion);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Promotion> queryPromotionListByPage(Integer page, Integer rows) {
        // 使用分页助手开始分页,指定两个参数：当前页码，每页条数
        PageHelper.startPage(page, rows);
        // 然后分页拦截器会自动对接下来的查询进行分页
        List<Promotion> promotions = promotionMapper.selectAll();
        // 封装分页信息对象
        return new PageInfo<>(promotions);


    }

    /**
     * 查询所有没有过期的数据
     * @return
     */
    public List<Promotion> findAll(){
        //查询没有过期的
        Example example = new Example(Promotion.class);
        example.createCriteria().andEqualTo("status","1");

        return  promotionMapper.selectByExample(example);
    }

    /**
     * 更新与结束日期
     */
    public void updateWithEndDate() {
        promotionMapper.updateWithEndDate();
    }

}