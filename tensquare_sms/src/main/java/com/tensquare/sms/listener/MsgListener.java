package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class MsgListener {

    @Autowired
    private SmsUtil smsUtil;

    @RabbitHandler
    public void handlerMsg(Map<String, String> map) {

        String mobile = map.get("mobile");
        String code = map.get("code");

        System.out.println("验证码:" + code);
        System.out.println("电话号码:" + mobile);

        try {
            //发送短信验证码
            smsUtil.sendSms(mobile, code);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
