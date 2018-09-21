package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.StandardMapper;
import com.czxy.bos.domain.base.Standard;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class StandardService {
    @Resource
    private StandardMapper standardMapper;

    /**
     * 添加
     * @param standard
     * @return
     */
    public  Integer save(Standard standard){

        return  standardMapper.insert(standard);

    }


    /*public EasyUIResult<Standard> queryStandardByPage(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
       // List<Standard> list = standardMapper.select(null);
        List<Standard> list = standardMapper.selectAll();
        PageInfo<Standard> pageInfo = new PageInfo<>(list);


        return  new EasyUIResult<>(pageInfo.getTotal(),pageInfo.getList());
    }*/
    /**
     * 分页
     * @param page 第几页
     * @param rows 第几行
     * @return
     */
    public PageInfo<Standard> queryStandardByPage(int page , int rows){
        //1 分页
        PageHelper.startPage(page ,rows);
        //2 查询所有--内含分页
        List<Standard> list = standardMapper.selectAll();
        //3 封装数据
        return new PageInfo<>( list );
    }

    /**
     * 修改方法
     * @param standard
     * @return
     */
    public  Integer update(Standard standard){
        return standardMapper.updateByPrimaryKey(standard);
    }

    /**
     * 删除方法
     * @param ids
     */
    public  void delete(String [] ids){
        for (String id : ids) {
            standardMapper.deleteByPrimaryKey(id);
        }



    }
    public  List<Standard> findAll(){
        return  standardMapper.selectAll();
    }


}
