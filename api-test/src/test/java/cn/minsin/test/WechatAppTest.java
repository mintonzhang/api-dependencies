package cn.minsin.test;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.wechat.app.function.WechatAppFunctions;
import cn.minsin.wechat.app.model.AppOrderPayModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 19:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WechatAppTest {

    @Autowired
    WechatAppFunctions wechatAppFunctions;

    @Test
    public void test() throws IOException, MutilsErrorException {
        AppOrderPayModel miniProgramOrderPayModel = new AppOrderPayModel();
        Map<String, String> miniProgramPayParamter = wechatAppFunctions.createAppPayParamter(miniProgramOrderPayModel);
        System.out.println(miniProgramPayParamter);
    }
}
