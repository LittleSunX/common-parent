package com.czxy.bos.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;

@Component
public class SmsConsumer {
    /**
     * 消费者实时监控“czxy.queue”队列中的消息
     * @param message
     */
    @JmsListener(destination="czxy.queue")
    public void receiveQueue(Message message){
        try {
            MapMessage mapMessage = (MapMessage) message;
            String telephone = mapMessage.getString("telephone");
            String code = mapMessage.getString("code");

            //SmsUtil.sendSms(phone, null, code, null, null);

            System.out.println("消费者consumer：" + telephone + " : " + code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


