package com.itheima.rabbitmq.consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

@Component
//Rabbit的消息接收,使用监听器的方式,需要添加以下注解,声明监听器
//注解里的参数,是队列名称
@RabbitListener(queues = "itheima")
public class MyListener {

    @RabbitHandler()
    public void handlerMessage(String message) {
        System.out.println("itheima消费者2:" + message);
    }

    //当一个生产者发消息到队列中,由多个消费者监听队列,成为工作模式
    //默认情况下,消费者消费消息是平均分配(轮询)模式
    //另一种模式叫做按劳分配模式,可以根据每个消费者消费消息的状况,进行消息分配
    //简单来说,消费消息越多的消费者,消费消息的数量越多
}
