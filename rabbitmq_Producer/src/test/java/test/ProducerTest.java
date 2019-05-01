package test;

import com.itheima.rabbitmq.producer.ProducerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class)
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //相当于ActiveMQ的点对点消息发送
    @Test
    public void test() {
        //第一个参数是把消息发到哪个队列中
        //第二个参数是消息的内容
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("sms", "测试发送简单类型的消息" + i);
        }
    }


    //分列模式,相当于ActiveMQ的发布订阅(一对多)
    //Publish/Subscribe
    @Test
    public void test2() {
        //第一个参数是交换机
        //第二个参数是路由键,本例没有涉及,写空串
        rabbitTemplate.convertAndSend("chuanzhi", "", "测试分列模式的消息发送");
    }

    //Routing路由模式
    @Test
    public void test3() {
        //第一个参数是交换机,本例使用chuanzhi2,
        //需要创建direct类型的交换机,并且指定路由键绑定队列
        //第二个参数是路由键
        rabbitTemplate.convertAndSend("chuanzhi2", "abc", "测试Routing模式的消息发送abc");
    }
    //主题模式
    //Topics
    @Test
    public void test4() {
        //第一个参数是交换机,本例使用chuanzhi3,
        //需要创建topic类型的交换机,并且指定路由键绑定队列,路由键使用*和#
        //第二个参数是路由键
        rabbitTemplate.convertAndSend("chuanzhi3", "itheima.itcast.abc", "测试topic模式abc.itcast");
    }
}
