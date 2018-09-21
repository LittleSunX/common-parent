package com.czxy.bos.service.base;

import com.czxy.bos.dao.base.AreaMapper;
import com.czxy.bos.domain.base.Area;
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
public class AreaService {
    @Resource
    private AreaMapper areaMapper;


    public Integer saveAreas(List<Area> list){
        int count=0;
        for (Area area : list) {
            Area temp = areaMapper.selectByPrimaryKey(area.getId());
            if (temp!=null){
                continue;

            }
            count++;
            areaMapper.insert(area);
        }
        return  count;
    }

    /**
     * 查询区域列表页面
     * @param area
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Area> queryAreaListByPage(Area area, Integer page, Integer rows){
        Example example = new Example(Area.class);
        Example.Criteria criteria = example.createCriteria();
        if (area!=null){
            if (StringUtils.isNotBlank(area.getProvince())){
                criteria.andLike("province", "%"+area.getProvince()+"%");

            }
            if (StringUtils.isNotBlank(area.getCity())){
                criteria.andLike("city", "%"+area.getCity()+"%");
            }
            if(StringUtils.isNotBlank(area.getDistrict())){
                criteria.andLike("district", "%"+area.getDistrict()+"%");
            }
        }
        // 使用分页助手开始分页,指定两个参数：当前页码，每页条数
        PageHelper.startPage(page,rows);
        // 然后分页拦截器会自动对接下来的查询进行分页
        List<Area> list = areaMapper.selectByExample(example);// 传查询条件
        // 封装分页信息对象
        return  new PageInfo<>(list);
    }
    public  Integer save(Area area){
        area.setId(UUID.randomUUID().toString());

       return  areaMapper.insert(area);

    }
    /**
     * 修改
     */
    public Integer updata(Area area){
        return  areaMapper.updateByPrimaryKey(area);
    }
    /**
     * 删除
     */
    public void delete(String [] ids){
        for (String id : ids) {
            areaMapper.deleteByPrimaryKey(id);
            
        }
    }


}
