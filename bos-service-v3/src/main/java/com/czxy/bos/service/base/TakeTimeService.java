package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.TakeTimeMapper;
import com.czxy.bos.domain.base.TakeTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class TakeTimeService {
    @Resource
    private TakeTimeMapper takeTimeMapper;
    public List<TakeTime> findAll() {
        List<TakeTime> list = takeTimeMapper.selectAll();
        return list;
    }

}
