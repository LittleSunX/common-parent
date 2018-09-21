package com.czxy.bos.dao.base;

import com.czxy.bos.domain.base.SubArea;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SubAreaMaapper extends Mapper<SubArea> {

    @Select("SELECT * FROM t_sub_area WHERE FIXEDAREA_ID=(SELECT id FROM t_fixed_area WHERE id=#{subAreaId})")
    @Results({
            @Result(id=true,property="id",column="ID"),
            @Result(property = "area",one = @One(select = "com.czxy.bos.dao.base.AreaMapper.selectByPrimaryKey"), column = "AREA_ID"),
            @Result(property = "fixedArea",one = @One(select = "com.czxy.bos.dao.base.FixedAreaMapper.selectByPrimaryKey"), column = "FIXEDAREA_ID")
    })

    List<SubArea> findAll(String subAreaId);



}
