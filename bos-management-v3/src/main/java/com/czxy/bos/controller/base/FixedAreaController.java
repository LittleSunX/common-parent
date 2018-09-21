package com.czxy.bos.controller.base;

import com.czxy.bos.constant.Constants;
import com.czxy.bos.domain.base.FixedArea;
import com.czxy.bos.service.base.FixedAreaService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/fixedArea")
public class FixedAreaController {
    @Resource
    private FixedAreaService fixedAreaService;
    @Resource
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<String> save(FixedArea fixedArea){
        Integer count = fixedAreaService.saveFixedArea(fixedArea);
        if (count==1){
        return new ResponseEntity<>("新增定区成功!",HttpStatus.CREATED);//201
        }
        return new ResponseEntity<>("新增定区失败!",HttpStatus.INTERNAL_SERVER_ERROR);

    }
    /**
     * 分页条件查询    /fixedArea?page=3&rows=10
     *                /fixedArea 如果参数中没有page和rows，将使用默认值 @RequestParam(defaultValue="1")
     * @param fixedArea
     * @param page
     * @param rows
     * @return
     */
   @GetMapping
    public ResponseEntity<EasyUIResult<FixedArea>> findAll(FixedArea fixedArea,
                                                           @RequestParam(name="page",defaultValue="1") Integer page ,
                                                           @RequestParam(name="rows",defaultValue = "5")Integer rows){
        //1 查询PageInfo
        PageInfo<FixedArea> pageInfo = fixedAreaService.findAllByPage( fixedArea , page , rows );
        //2 result
        EasyUIResult result = new EasyUIResult( pageInfo.getTotal() , pageInfo.getList() );
        //3 status
        return new ResponseEntity<>( result ,HttpStatus.OK);
    }
    @PutMapping
    public  ResponseEntity<String> update(FixedArea fixedArea){
        Integer u = fixedAreaService.update(fixedArea);
        if (u==1){
            return  new ResponseEntity<>("修改成功",HttpStatus.OK);

        }
        return  new ResponseEntity<>("修改失败",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * 查询未关联的客户
     * @return
     */
    @GetMapping( "/findNoAssociationCustomers")
    public ResponseEntity<String> findNoAssociationCustomers() {
        // 使用HttpClient调用 远程接口
        String url = Constants.CRM_MANAGEMENT_HOST + "/crmCustomer/findNoAssociationCustomers";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        return result;
    }
    @GetMapping("/findHasAssociationFixedAreaCustomers")
    public  ResponseEntity<String> findHasAssociationFixedAreaCustomers(@RequestParam("id")String fixedAreaId){
        // 使用HttpClient调用 接口
        String url = Constants.CRM_MANAGEMENT_HOST + "/crmCustomer/findHasAssociationFixedAreaCustomers?fixedAreaId="+fixedAreaId;

        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        return result;



    }
    @GetMapping("/associationCustomersToFixedArea")
    public ResponseEntity<String> associationCustomersToFixedArea(@RequestParam("customerIds[]")String[] customerIdStr,
                                                                  @RequestParam("fixedAreaId")String fixedAreaId) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < customerIdStr.length; i++) {
            if (i==customerIdStr.length-1){
                sbf.append(customerIdStr[i]);

            }else{
                sbf.append(customerIdStr[i]+",");
            }

        }

        System.out.println(sbf.toString());



        String url = Constants.CRM_MANAGEMENT_HOST + "/crmCustomer/associationCustomersToFixedArea?customerIds="+sbf.toString()+"&fixedAreaId="+fixedAreaId;
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        return result;



    }
  /*  /** http://localhost:8090/crmCustomer/associationCustomersToFixedArea?fixedAreaId=dq001&customerIds=1,2
     * 定区关联一组客户
     * @param fixedAreaId
     * @param customerIds
     * @return
     *//*
    @PostMapping("/associationCustomersToFixedArea")
    public ResponseEntity<String> associationCustomersToFixedArea(String fixedAreaId, String customerIds){
        //向crm系统发送post请求
        return restTemplate.postForEntity("http://localhost:8090/crmCustomer/associationCustomersToFixedArea?fixedAreaId="+fixedAreaId+"&customerIds=" + customerIds ,null , String.class);
    }*/

    @PostMapping("/associationCourierToFixedArea")
    public ResponseEntity<Void> associationCourierToFixedArea(String fixedAreaId , Integer courierId, Integer takeTimeId){
       // System.out.println("********"+fixedAreaId+"============"+takeTimeId+"========="+courierId);
        //1 关联操作
        fixedAreaService.associationCourierToFixedArea(fixedAreaId, courierId , takeTimeId);
        //2 提示
        return new ResponseEntity<>(HttpStatus.CREATED);
    }





}
