package com.czxy.bos.controller.base;


import com.czxy.bos.domain.base.Standard;
import com.czxy.bos.service.base.StandardService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("standard")
public class StandardController {
    @Resource
    private StandardService standardService;

    @PostMapping
    public ResponseEntity<String>  save(Standard standard){
        try {
          Integer count = standardService.save(standard);
            if (count==1){
                return  new ResponseEntity<String>("创建成功",HttpStatus.OK);
            }
            return  new ResponseEntity<String>("创建出现异常",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<String>("创建失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
   /* @RequestMapping("/queryStandardByPage")
    public ResponseEntity<EasyUIResult<Standard>> queryStandardByPage(@RequestParam("page") Integer page , @RequestParam("rows") Integer rows) {
        try {
            EasyUIResult<Standard> result = standardService.queryStandardByPage(page, rows);
            if (result!=null){
                return  new ResponseEntity<>(result,HttpStatus.OK);
            }
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }*/

    @GetMapping
    public ResponseEntity<EasyUIResult<Standard>> queryStandardByPage(@RequestParam("page") Integer page , @RequestParam("rows") Integer rows){
        //查询分页结果
        PageInfo<Standard> pageInfo = this.standardService.queryStandardByPage(page, rows);
        //按照datagrid格式封装数据
        EasyUIResult result = new EasyUIResult(pageInfo.getTotal() , pageInfo.getList() );
        //按照RESTFul格式返回
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> update(Standard standard){
        try {
            Integer u = standardService.update(standard);
            if (u==1){
                return  new ResponseEntity<String>("修改成功",HttpStatus.OK);
            }
            return  new ResponseEntity<String>("修改出现异常",HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<String>("修改失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @DeleteMapping
    public  ResponseEntity<String> delete(String ids){

        try {
            String[] idArray = ids.split(",");
            standardService.delete(idArray);
            return  new ResponseEntity<String>("删除成功",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return  new ResponseEntity<String>("删除失败",HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping ("/all")
    public ResponseEntity<List<Standard>> findAll(){

        return  new ResponseEntity<>(standardService.findAll(),HttpStatus.OK);
    }

}
