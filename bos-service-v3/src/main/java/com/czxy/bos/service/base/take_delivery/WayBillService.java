package com.czxy.bos.service.base.take_delivery;

import com.czxy.bos.dao.base.take_delivery.WayBillMapper;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.es.domain.ESWayBill;
import com.czxy.bos.es.repository.WayBillRepository;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class WayBillService {
    @Resource
    private WayBillMapper wayBillMapper;
    @Resource
    private WayBillRepository wayBillRepository;


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

    /**
     * 运单id如果存在将完成更新操作，如果不存在将完成添加操作
     * @param wayBill
     */
    public void saveOrUpdateWayBill (WayBill wayBill){

        if (wayBill.getId()!=null){
            //更新
            wayBillMapper.updateByPrimaryKey(wayBill);
        }else {
            //添加
            //校验：运单号必须唯一
            Example example = new Example(WayBill.class);
            example.createCriteria().andEqualTo("wayBillNum",wayBill.getWayBillNum());
            WayBill temp = wayBillMapper.selectOneByExample(example);
            if (temp!=null){
                throw new BosException("运单号【"+wayBill.getWayBillNum()+"】已存在");
            }
            //运单的状态需要初始化  //待发货
            ESWayBill esWayBill = new ESWayBill();
            BeanUtils.copyProperties(wayBill,esWayBill);

            wayBillRepository.save(esWayBill);
            wayBill.setSignStatus(1);
            //插入
            wayBillMapper.insert(wayBill);

        }

    }

    /**
     * 根据运单号查询
     * @param wayBillNum
     * @return
     */
    public WayBill findByWayBillNum(String wayBillNum) {
        Example example = new Example(WayBill.class);

        example.createCriteria().andEqualTo("wayBillNum",wayBillNum);

        return wayBillMapper.selectOneByExample(example);
    }

    /**
     * 条件查询搜索
     * @param wayBill
     * @param page
     * @param rows
     * @return
     */
    public Page<ESWayBill> pageQuery(ESWayBill wayBill , Integer page, Integer rows) {
        //条件封装对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //运单号查询 等值查询
        if (StringUtils.isNotBlank(wayBill.getWayBillNum())){
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("wayBillNum",wayBill.getWayBillNum()));
        }
        //发货地，模糊查询
        if (StringUtils.isNotBlank(wayBill.getSendAddress())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("sendAddress",wayBill.getSendAddress()));
        }
        //收货地 ，模糊查询
        if(StringUtils.isNotBlank(wayBill.getRecAddress())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("recAddress",wayBill.getSendAddress()));
        }
        //速运类型  等值查询
        if(StringUtils.isNotBlank(wayBill.getSendProNum())){
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("sendProNum",wayBill.getSendProNum()));
        }

        //签到状态 等值查询
        if(wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0){
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("signStatus",wayBill.getSignStatus()));
        }


        //2 搜索封装对象
        NativeSearchQuery searchQuery = new NativeSearchQuery(boolQueryBuilder);
        //2.1 设置分页
        searchQuery.setPageable(PageRequest.of(page-1,rows ));

        //3 查询
        return this.wayBillRepository.search(searchQuery);
    }


}
