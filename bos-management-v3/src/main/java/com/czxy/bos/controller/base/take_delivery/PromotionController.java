package com.czxy.bos.controller.base.take_delivery;

import com.czxy.bos.domain.take_delivery.Promotion;
import com.czxy.bos.service.base.take_delivery.PromotionService;
import com.czxy.bos.vo.EasyUIResult;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Resource
    private PromotionService promotionService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;
    @PostMapping
    public ResponseEntity<Void> save(Promotion promotion, @RequestParam("titleImgFile") MultipartFile multipartFile) throws IOException {

        // 宣传图 上传、在数据表保存 宣传图路径
        // 绝对路径（用于上传）
        String savePath = request.getServletContext().getRealPath("/upload/");
        // 相对路径（用于保存数据库）
        String saveUrl = request.getContextPath()+ "/upload/";

        // 生成随机图片名
        UUID uuid = UUID.randomUUID();
        String name = multipartFile.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));

        String randomFileName = uuid + ext;

        // 保存图片 (绝对路径)
        File destFile = new File(savePath + "/" + randomFileName);
        if(!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }
        System.out.println(destFile.getAbsolutePath());
        multipartFile.transferTo(destFile);


        // 将保存路径 相对工程web访问路径，保存model中
        //TODO 是否需要删除http前缀
        promotion.setTitleImg(saveUrl + randomFileName);

        // 调用业务层，完成活动任务数据保存
        promotionService.save(promotion);

        response.sendRedirect("/pages/take_delivery/promotion");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @return
     */
    @GetMapping
    public Map<String,Object> queryPromotionListByPage (@RequestParam("page")Integer page,
                                         @RequestParam("rows")Integer rows){
        // 查询用户
        PageInfo<Promotion> pageInfo = this.promotionService.queryPromotionListByPage(page, rows);
        // 封装datagrid需要的格式
        Map<String,Object> map=new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        // 封装
        return  map;
    }

    /**
     * http://localhost:8088/promotion/findAll
     * @return
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<Promotion>> findAll(){
        // 查询用户
        List<Promotion> list = promotionService.findAll();
        return  new ResponseEntity<>(list, HttpStatus.OK);
    }





}
