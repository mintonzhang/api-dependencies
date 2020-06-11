package cn.minsin.test;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.wechat.miniprogram.function.WechatMiniProgramFunctions;
import cn.minsin.wechat.miniprogram.model.MiniProgramOrderPayModel;
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
public class WechatMiniProgramTest {

    @Autowired
    WechatMiniProgramFunctions wechatMiniProgramFunctions;

    @Test
    public void test() throws IOException, MutilsErrorException {
        MiniProgramOrderPayModel miniProgramOrderPayModel = new MiniProgramOrderPayModel();
        miniProgramOrderPayModel.setOpenid("123123123");
        Map<String, String> miniProgramPayParamter = wechatMiniProgramFunctions.createMiniProgramPayParamter(miniProgramOrderPayModel);
        System.out.println(miniProgramPayParamter);
    }
}
