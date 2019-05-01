package test;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.SmsApplication;
import com.tensquare.sms.util.SmsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SmsApplication.class)
public class SmsTest {

    @Autowired
    private SmsUtil smsUtil;

    //@Test
    public void test() throws Exception {
        smsUtil.sendSms("13297180930", "123456");
    }
}
