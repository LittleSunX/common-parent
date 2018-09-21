package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.CourierMapper;
import com.czxy.bos.dao.base.FixedAreaCourierMapper;
import com.czxy.bos.dao.base.FixedAreaMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.base.FixedArea;
import com.czxy.bos.domain.base.FixedAreaCourier;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FixedAreaService {
    @Resource
    private FixedAreaMapper fixedAreaMapper;
    @Resource
    private CourierMapper courierMapper;

    @Resource
    private FixedAreaCourierMapper fixedAreaCourierMapper;

    public  Integer saveFixedArea(FixedArea fixedarea){

        //校验
        //名称必须唯一
        Example nameExample = new Example(FixedArea.class);
        Example.Criteria nameCriteria = nameExample.createCriteria();
        nameCriteria.andEqualTo("fixedAreaName",fixedarea.getFixedAreaName());
        FixedArea nameFixedArea = fixedAreaMapper.selectOneByExample(nameExample);
        if (nameFixedArea!=null){
            throw new BosException("定区名称已存在!");
        }
        //负责人必须唯一
        Example leaderExample = new Example(FixedArea.class);
        Example.Criteria leaderCriteria = leaderExample.createCriteria();
        leaderCriteria.andEqualTo("fixedAreaLeader",fixedarea.getFixedAreaLeader());
        FixedArea leaderFixedArea = fixedAreaMapper.selectOneByExample(leaderExample);
        if (leaderFixedArea!=null){
            throw new BosException("定区负责人已存在!");
        }
        // 1.3 数据完整性
        fixedarea.setId( UUID.randomUUID().toString() );

        return fixedAreaMapper.insert(fixedarea);

    }
    public PageInfo<FixedArea> findAllByPage(FixedArea fixedArea , Integer page, Integer rows){

        Example example = new Example(FixedArea.class);
        Example.Criteria criteria = example.createCriteria();
        if (fixedArea!=null){
            if (StringUtils.isNotBlank(fixedArea.getCompany())){
                criteria.andLike("company","%"+fixedArea.getCompany()+"%");

            }
            if (StringUtils.isNotBlank(fixedArea.getId())){
                criteria.andLike("id","%"+fixedArea.getId()+"%");

            }

        }
        //1 分页
        PageHelper.startPage( page ,rows );
        //2 查询
        List<FixedArea> list = fixedAreaMapper.selectByExample( example );
        //3 封装
        return new PageInfo<>( list );

    }
    /**
     * 修改
     */
    public Integer update(FixedArea fixedArea){
        return  fixedAreaMapper.updateByPrimaryKey(fixedArea);
    }


    public void associationCourierToFixedArea(String fixedAreaId, Integer courierId, Integer takeTimeId) {
        //1 更新快递员的收派时间
        // 1.1 快递员查询
        Courier courier = courierMapper.selectByPrimaryKey(courierId);
        //1.2 设置收派时间
        courier.setTakeTimeId(takeTimeId);
        //1.3 更新快要
        courierMapper.updateByPrimaryKey(courier);

        //2 快递员定区表中添加一条关联数据
        FixedAreaCourier fixedAreaCourier = new FixedAreaCourier();
        fixedAreaCourier.setCourierId(courierId);
        fixedAreaCourier.setFixedAreaId(fixedAreaId);
        fixedAreaCourierMapper.insert(fixedAreaCourier);
    }
}
