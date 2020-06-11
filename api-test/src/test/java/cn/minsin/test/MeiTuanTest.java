package cn.minsin.test;

import cn.minsin.meituan.peisong.function.MeituanPeisongFunctions;
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
 * @since: 2020/4/1 19:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MeiTuanTest {

    @Autowired
    private MeituanPeisongFunctions meituanPeisongFunctions;


    @Test
    public void parse() throws IOException, NoSuchAlgorithmException {
        OrderByCoordinatesModel orderByCoordinatesModel = new OrderByCoordinatesModel();
        meituanPeisongFunctions.createByCoordinates(orderByCoordinatesModel);
    }
}
