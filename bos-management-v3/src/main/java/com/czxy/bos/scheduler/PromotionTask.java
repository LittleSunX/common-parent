package com.czxy.bos.scheduler;

import com.czxy.bos.service.base.take_delivery.PromotionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PromotionTask {
    @Resource
    private PromotionService promotionService;
    //每分钟执行一次，从0分钟开始（整分钟）
    @Scheduled(cron="0 0/1 * * * ?")
    public void process(){
        promotionService.updateWithEndDate();
    }



}
