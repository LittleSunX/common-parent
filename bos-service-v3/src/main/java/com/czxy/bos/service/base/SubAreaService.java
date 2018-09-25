package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.SubAreaMapper;
import com.czxy.bos.domain.base.SubArea;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class SubAreaService {
    @Resource
    private SubAreaMapper subAreaMapper;

    /**
     * 关联分区
     * @param subAreaId
     * @return
     */
    public List<SubArea> findPartition(String subAreaId){
        List<SubArea> list = subAreaMapper.findAll(subAreaId);
        return  list;
    }
    /**
     * 查找指定区域所有的分区（子区域）
     * @param areaId
     * @return
     */
    public  List<SubArea> findAllByAreaId(String areaId){
        Example example = new Example(SubArea.class);
        example.createCriteria().andEqualTo("areaId",areaId);
        return  subAreaMapper.selectByExample(example);
    }

}
