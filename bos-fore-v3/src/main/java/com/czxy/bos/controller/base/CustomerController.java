package com.czxy.bos.controller.base;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.czxy.bos.constant.Constants;
import com.czxy.bos.util.MailUtil;
import com.czxy.bos.util.SmsUtil;
import com.czxy.crm.domain.Customer;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private HttpSession session;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private StringRedisTemplate redisTemplate;
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private Queue queue;
    @Resource
    private HttpServletRequest request;



    @GetMapping("/sendSms")
    public ResponseEntity<Void> sendSms(String telephone){
        String s = RandomStringUtils.randomNumeric(4);
        System.out.println("生成手机验证码为：" + s);
        // 将短信验证码 保存到session
        session.setAttribute(telephone, s);
      //  request.getSession().setAttribute(telephone,s);
        try {
          /*  SmsUtil.sendSms(telephone, null, s, null, null);17805202466*/
            SendSmsResponse sms = SmsUtil.sendSms(telephone, "呵呵", s, "沭阳西北角", "119");
            System.out.println(sms.getMessage()+"==============");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //采用active mq发送消息
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("telephone",telephone);
            mapMessage.setString("code" , s);

            this.jmsMessagingTemplate.convertAndSend(this.queue , mapMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/regist")
    public  ResponseEntity<String> regist(String checkcode ,Customer customer){
        //判断验证码是否正确，从session获取验证码
        String sessionCheckcode = (String) session.getAttribute(customer.getTelephone());
        if (sessionCheckcode == null ||!sessionCheckcode.equals(checkcode)){
            return new ResponseEntity<>("验证码错误",HttpStatus.INTERNAL_SERVER_ERROR);//500

        }
        //给一条提示
        System.out.println("发送成功");
        //2 通过crm系统进行用户注册，第二个为请求参数，发送过去的数据为json
        String url = Constants.CRM_MANAGEMENT_HOST + "/crmCustomer/saveCustomer";
        ResponseEntity<String> entity = restTemplate.postForEntity(url,customer,String.class);
        //crm系统返回201表示注册成功
        if (entity.getStatusCodeValue()==201){
            try {
                //生成激活码
                String activecode = RandomStringUtils.randomNumeric(32);
                //激活链接
                String activeUrl = "http://localhost:8092/customer/activeMail?telephone="+customer.getTelephone()+"&activecode=" + activecode;
                System.out.println(activeUrl);
                // 将激活码保存到redis，设置24小时失效
                redisTemplate.opsForValue().set(customer.getTelephone() , activecode , 24, TimeUnit.HOURS);

                //发送激活邮件
                String text = "手机用户["+customer.getTelephone()+"]您好：<br/>你正在“速运快递”注册用户，请<a href='"+activeUrl+"'>点击</a>进行激活，激活码有效时间24小时。<br/>若不是本人操作，请忽略。";
                MailUtil.sendMail(customer.getEmail() , "【速运快递】账号激活邮件", text);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return entity;
    }
    @GetMapping("/validatecode")
    public ResponseEntity<Void> validatecode(HttpServletResponse response) throws IOException {

        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String capstr = hash1.substring(0, 4);
        session.setAttribute("validateCode", capstr);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(capstr, 8, 24);
        g.dispose();
        response.setContentType("image/jpeg");

        OutputStream strm = response.getOutputStream();
        ImageIO.write(image, "jpeg", strm);
        strm.close();

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @GetMapping("/login")
    public  ResponseEntity<String> login(String telephone,String password,String checkcode){
        //验证码是否正确
        //获得session验证码
        String validateCode = (String) request.getSession().getAttribute("validateCode");
        if (validateCode==null){
            return  new ResponseEntity<>("验证码失效",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!validateCode.equalsIgnoreCase(checkcode)){
            return  new ResponseEntity<>("验证码错误",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //将session验证码移除
        request.getSession().removeAttribute("validateCode");
        //通过crm系统查询用户
        String url="http://localhost:8090/crmCustomer/findCustomerByTelephoneAndPassword?telephone="+telephone+"&password="+password;
        ResponseEntity<Customer> entity = restTemplate.getForEntity(url, Customer.class);
        Customer customer = entity.getBody();
        //判断用户为null则没有查询结果
        if (customer==null){
            return  new ResponseEntity<>("用户名或密码不匹配",HttpStatus.INTERNAL_SERVER_ERROR);

        }
        //有结果状态不是1未激活用户
        if (customer.getType()!=1){
         return  new ResponseEntity<>("当前用户未激活不能登录",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //登录正常将用户存到session
        request.getSession().setAttribute("customer",customer);
        System.out.println(customer);
        //提示登录成功
        return new ResponseEntity<>("登陆成功",HttpStatus.OK);

    }


}
