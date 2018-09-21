package com.czxy.bos.controller.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/forePromotion")
public class ForePromotionController {
    @Resource
    private RestTemplate restTemplate;
    /**
     * 查询促销页面
     */
    @GetMapping
    public ResponseEntity<String> queryPromotionByPage(){
        String url = "http://localhost:8088/promotion/findAll";
        return restTemplate.getForEntity(url, String.class);

    }



}
