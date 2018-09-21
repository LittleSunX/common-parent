package com.czxy.bos.dao.base;

import com.czxy.bos.domain.base.Courier;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface CourierMapper extends Mapper<Courier> {
    @Select("select * from t_courier")
    @Results(
            {
                    @Result(property = "id", column = "ID"),
                    @Result(property="courierNum",column="COURIER_NUM"),
                    @Result(property="checkPwd",column="CHECK_PWD"),
                    @Result(property="vehicleType",column="VEHICLE_TYPE"),
                    @Result(property="vehicleNum",column="VEHICLE_NUM"),
                    @Result(property = "standard", column = "STANDARD_ID", one = @One(select = "com.czxy.bos.dao.base.StandardMapper.selectByPrimaryKey"))
            }
    )
    List<Courier> findCourierByPage();

    /**
     * 查找未关联的快递员
     * @return
     */
    @Select("select * from t_courier where id not in (select DISTINCT(courier_id) from t_fixedarea_courier)")
    List<Courier> findNoAssociation();
    @Select("SELECT * FROM t_courier c , t_fixedarea_courier fc " +
            "WHERE c.id = fc.courier_id AND fc.fixed_area_id = #{fixedAreaId}")
    @Results({
            @Result(property="courierNum",column="COURIER_NUM"),
            @Result(property="standard",column="standard_id",one=@One(select="com.czxy.bos.dao.base.StandardMapper.selectByPrimaryKey")),
            @Result(property="takeTime",column="taketime_id",one=@One(select="com.czxy.bos.dao.base.TakeTimeMapper.selectByPrimaryKey"))
    }
    )

    List<Courier> findAssociationCourier(@Param("fixedAreaId") String fixedAreaId);
}
