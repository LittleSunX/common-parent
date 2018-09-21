package com.czxy.bos.service.base;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.czxy.bos.dao.base.CourierMapper;
import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.exception.BosException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CourierService {
    @Resource
    private CourierMapper courierMapper;

    public Integer saveCourier(Courier courier){
        if (StringUtils.isBlank(courier.getCourierNum())){
        throw  new BosException("操作对象不存在");
        }
        Example example = new Example(Courier.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("courierNum",courier.getCourierNum());
        Courier byExample = courierMapper.selectOneByExample(example);
        if (byExample!=null){
         throw  new BosException("快递员号已存在");
        }
        return  courierMapper.insert(courier);

    }
  public PageInfo<Courier> queryCourierListByPage(Integer page,Integer rows){
      PageHelper.startPage(page,rows);
      List<Courier> list = courierMapper.findCourierByPage();
      return  new PageInfo<>(list);
  }
  public Integer delete(Integer[] ids ){
      for (Integer id : ids) {
          Courier courier = courierMapper.selectByPrimaryKey(id);
          courier.setDeltag('1');
          int i = courierMapper.updateByPrimaryKey(courier);

      }
      return  ids.length;

  }
    /**
     * 快递员作废（将标记设置成null）
     * @param ids
     * @return
     */
    public int restore(Integer[] ids) {
        for (Integer id : ids) {
            Courier courier = courierMapper.selectByPrimaryKey(id);
            courier.setDeltag(null);
            courierMapper.updateByPrimaryKey(courier);
        }
        return ids.length;
    }

    public List<Courier> findNoAssociation() {
        List<Courier> list = courierMapper.findNoAssociation();
        for(Courier c:list){
            c.setInfo(c.getName()+"("+c.getCompany()+")");
        }
        return list;

    }

    public List<Courier> findAssociationCourier(String fixedAreaId) {
        List<Courier> list = courierMapper.findAssociationCourier(fixedAreaId);
        return list;

    }
}
