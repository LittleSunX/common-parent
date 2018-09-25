package com.czxy.crm.service;

import com.czxy.bos.exception.BosException;
import com.czxy.crm.dao.CrmCustomerMapper;
import com.czxy.crm.domain.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CrmCustomerService {
    @Resource
    private CrmCustomerMapper crmCustomerMapper;
    /**
     * 查询没有关联定区的客户 （客户中定区外键为null）
     * @return
     */
    public List<Customer>findNoAssociationCustomers(){
        //拼凑条件，fixedAreaId需要为null
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("fixedAreaId");

        return  crmCustomerMapper.selectByExample(example);

    }
    /**
     * 已经关联到指定定区的客户列表，即字段fixedAreaId = ?
     * @param fixedAreaId
     * @return
     */
      public  List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId){
          //创建Example
          Example example = new Example(Customer.class);
          //查询条件
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("fixedAreaId",fixedAreaId);

          return crmCustomerMapper.selectByExample(example);
      }
    public void associationCustomersToFixedArea(String fixedAreaId, String customerIdStr) {
        // 将指定定区的所有关联字段设置成null （删除之前的关联）
        List<Customer> list = findHasAssociationFixedAreaCustomers(fixedAreaId);
        for (Customer customer : list) {
            customer.setFixedAreaId(null);
            crmCustomerMapper.updateByPrimaryKey(customer);
        }
        // 切割字符串 1,2,3，组织新的关联关系
        if (StringUtils.isBlank(customerIdStr)) {
            return;
        }
        // 更新本次选中的所有
        String[] customerIdArray = customerIdStr.split(",");
        for (String idStr : customerIdArray) {
            Integer id = Integer.parseInt(idStr);
            //查询
            Customer customer = crmCustomerMapper.selectByPrimaryKey(id);
            customer.setFixedAreaId(fixedAreaId);
            crmCustomerMapper.updateByPrimaryKey(customer);
        }


    }

    public void saveCustomer(Customer customer) {
        // 1.1 非空判断
        if(StringUtils.isBlank(customer.getTelephone())){
            throw new BosException("手机不能为空！");
        }
        // 1.2 手机号不能重复
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("telephone",customer.getTelephone());
        Customer findCustomer = crmCustomerMapper.selectOneByExample(example);
        if(findCustomer != null){
            throw new BosException("手机已被占用！");
        }

        //2 添加
        crmCustomerMapper.insert(customer);
    }
    /**
     * 根据电话号码查询客户
     * @param telephone
     * @return
     */
    public Customer findByTelephone(String telephone) {
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("telephone", telephone);

        return crmCustomerMapper.selectOneByExample(example);
    }



    /**
     * 激活操作 (根据电话号码修改用户的激活状态)
     * @param telephone
     */
    public void updateType(String telephone){
        //1 查询
        Customer customer = findByTelephone(telephone);
        if(customer == null){
            throw new BosException("操作对象不存在");
        }
        //2 在更新
        customer.setType(1);    //激活
        crmCustomerMapper.updateByPrimaryKey(customer);
    }

    /**
     * 通过电话和密码找到客户
     * @param telephone
     * @param password
     * @return
     */
   public  Customer   findCustomerByTelephoneAndPassword(String telephone,String password){
       Example example = new Example(Customer.class);
       example.createCriteria().andEqualTo("telephone",telephone).andEqualTo("password",password);

       return crmCustomerMapper.selectOneByExample(example);

   }
    /**
     * 通过地址和客户id查询定区id
     * @param address
     * @param customerId
     * @return
     */
    public String findFixdAreaIdByAddressAndID(String address, String customerId) {
        Example example = new Example(Customer.class);
        example.createCriteria().andEqualTo("address",address)
                .orEqualTo("id",customerId);
        Customer customer = crmCustomerMapper.selectOneByExample(example);
      //  System.out.println(customer);
        if(customer != null){
            return customer.getFixedAreaId();
        }
        return null;

    }

}
