package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.SubAreaMaapper;
import com.czxy.bos.domain.base.SubArea;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class SubAreaService {
    @Resource
    private SubAreaMaapper subAreaMaapper;

    /**
     * 关联分区
     * @param subAreaId
     * @return
     */
    public List<SubArea> findPartition(String subAreaId){
        List<SubArea> list = subAreaMaapper.findAll(subAreaId);
        return  list;
    }

}
