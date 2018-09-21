package com.czxy.bos.controller.base;

import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.service.base.CourierService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {
    @Resource
    private CourierService courierService;

    @PostMapping
    public ResponseEntity<String> saveCourier(Courier courier) {
        Integer r = courierService.saveCourier(courier);
        if (r == 1) {
            return new ResponseEntity<>("添加成功", HttpStatus.CREATED);//201状态码
        }
        return new ResponseEntity<>("添加失败", HttpStatus.BAD_REQUEST);//400状态码


    }

    @GetMapping
    public ResponseEntity<EasyUIResult<Courier>> findCourierByPage(@RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
        //查询分页结果
        PageInfo<Courier> pageInfo = courierService.queryCourierListByPage(page, rows);
        //按照datagrid格式封装数据
        EasyUIResult<Courier> result = new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList());
        //按照RESTFul格式返回
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("ids[]") Integer[] ids) {

        courierService.delete(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);//204状态码


    }

    /**
     * 快递员还原（将标记设置成null）
     *
     * @param ids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> restore(@RequestParam("ids[]") Integer[] ids) {
        courierService.restore(ids);

        // 成功，返回204 ,操作成功，但是不返回数据
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);//204状态码

    }

    /**
     * 查询没有被关联定区的快递员
     *
     * @return
     */
    @GetMapping("/findNoAssociation")
    public ResponseEntity<List<Courier>> findNoAssociation() {
        // 调用业务层，查询未关联定区的快递员
        List<Courier> result = courierService.findNoAssociation();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 查询关联的快递员
     *
     * @param fixedAreaId
     * @return
     */
    @GetMapping("/findAssociationCourier")
    public ResponseEntity<List<Courier>> findAssociationCourier(@RequestParam("fixedAreaId") String fixedAreaId) {
        // 调用业务层，查询关联快递员 列表
        List<Courier> result = courierService.findAssociationCourier(fixedAreaId);
        // 将查询快递员列表 压入值栈
        return new ResponseEntity<List<Courier>>(result, HttpStatus.OK);


    }
}