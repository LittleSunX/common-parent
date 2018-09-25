package com.czxy.crm.controller;

import com.czxy.crm.domain.Customer;
import com.czxy.crm.service.CrmCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/crmCustomer")
public class CrmCustomerController {
    @Resource
    private CrmCustomerService crmCustomerService;

    /**
     * http://localhost:8090/crmCustomer/findNoAssociationCustomers
     * 查询没有关联定区的客户
     *
     * @return
     */
    @GetMapping("/findNoAssociationCustomers")
    public ResponseEntity<List<Customer>> findNoAssociationCustomers() {
        //1.查询
        List<Customer> list = crmCustomerService.findNoAssociationCustomers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 已经关联到指定定区的客户列表
     * http://localhost:8090/crmCustomer/findHasAssociationFixedAreaCustomers?fixedAreaId=dq001
     *
     * @param fixedAreaId
     * @return
     */
    @GetMapping("/findHasAssociationFixedAreaCustomers")
    public ResponseEntity<List<Customer>> findHasAssociationFixedAreaCustomers(
            @RequestParam("fixedAreaId") String fixedAreaId) {
        List<Customer> list = crmCustomerService.findHasAssociationFixedAreaCustomers(fixedAreaId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * http://localhost:8090/crmCustomer/associationCustomersToFixedArea?fixedAreaId=dq001&customerIds=1,2
     * 定区关联一组客户
     *
     * @param fixedAreaId
     * @param customerIds
     * @return
     */
    @GetMapping("/associationCustomersToFixedArea")
    public ResponseEntity<String> associationCustomersToFixedArea(String fixedAreaId, String customerIds) {
        //操作
        crmCustomerService.associationCustomersToFixedArea(fixedAreaId, customerIds);

        //提示
        return new ResponseEntity<String>("关联成功", HttpStatus.OK);
    }
    /**
     *
     * @param customer  远程前端系统访问，发送的为json数据，需通过@RequestBody确定请求数据为json
     * @return
     */
    @PostMapping("/saveCustomer")
    public ResponseEntity<String> saveCustomer(@RequestBody  Customer customer){
        try {
            crmCustomerService.saveCustomer(customer);
            return  new ResponseEntity<>("注册成功",HttpStatus.CREATED);//201
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);//500
        }


    }
    /**
     * http://localhost:8090/customer/telephone?telephone=136...
     * @param telephone
     * @return
     */
    @GetMapping("/findByTelephone")
    public ResponseEntity<Customer> findByTelephone(String telephone){
        //1查询
        Customer customer = crmCustomerService.findByTelephone(telephone);
        //2 封装
        return new ResponseEntity<>(customer ,HttpStatus.OK);
    }

    /**
     * 激活
     * @param telephone
     * @return
     */
    @GetMapping("/updateType")
    public ResponseEntity<String> updateType(String telephone){
        try {
            //1 更新
            crmCustomerService.updateType(telephone);
            //2 提示
            return new ResponseEntity<String>("账号激活成功",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("账号激活失败",HttpStatus.ALREADY_REPORTED);
        }
    }
    /** http://localhost:8090/crmCustomer/findCustomerByTelephoneAndPassword?telephone=136&password=1234
     * 通过手机号和密码查询
     * @param telephone
     * @param password
     * @return
     */
    @GetMapping("/findCustomerByTelephoneAndPassword")
    public ResponseEntity<Customer> findCustomerByTelephoneAndPassword(String telephone,String password){
        Customer customer = crmCustomerService.findCustomerByTelephoneAndPassword(telephone, password);

        return  new ResponseEntity<>(customer,HttpStatus.OK);

    }
    /**
     * 通过地址和客户id查询定区id
     * @param address
     * @param customerId
     * @return
     */
    @GetMapping("/findFixdAreaIdByAddressAndID")
    public ResponseEntity<String> findFixdAreaIdByAddressAndID(String address , String customerId){
        String fixedAreaId = crmCustomerService.findFixdAreaIdByAddressAndID(address, customerId);

        return  new ResponseEntity<>(fixedAreaId,HttpStatus.OK);

    }





}