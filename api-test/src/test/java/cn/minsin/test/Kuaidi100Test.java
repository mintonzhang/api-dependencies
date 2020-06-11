package cn.minsin.test;

import cn.minsin.kuaidi100.function.KuaiDi100Functions;
import cn.minsin.meituan.peisong.model.send.OrderByCoordinatesModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 19:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Kuaidi100Test {
    @Autowired
    private KuaiDi100Functions kuaiDi100Functions;


    @Test
    public void parse() throws IOException, NoSuchAlgorithmException {
        OrderByCoordinatesModel orderByCoordinatesModel = new OrderByCoordinatesModel();
        kuaiDi100Functions.getLogistics("!23123","123123");
    }
}
