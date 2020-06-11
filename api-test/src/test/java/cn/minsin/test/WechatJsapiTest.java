package cn.minsin.test;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.wechat.jsapi.function.WechatJsapiFunctions;
import cn.minsin.wechat.jsapi.model.JsapiOrderPayModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 19:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WechatJsapiTest {

    @Autowired
    WechatJsapiFunctions wechatJsapiFunctions;

    @Test
    public void test() throws IOException, MutilsErrorException {
        JsapiOrderPayModel miniProgramOrderPayModel = new JsapiOrderPayModel();
        miniProgramOrderPayModel.setOpenid("123123123");
        Map<String, String> miniProgramPayParamter = wechatJsapiFunctions.createJsapiPayParamter(miniProgramOrderPayModel);
        System.out.println(miniProgramPayParamter);
    }
}
