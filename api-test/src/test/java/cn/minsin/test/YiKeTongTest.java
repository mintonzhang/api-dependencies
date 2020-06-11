package cn.minsin.test;

import cn.minsin.yiketong.function.YiKeTongFunctions;
import cn.minsin.yiketong.model.ResultModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 17:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class YiKeTongTest {

    @Autowired
    private YiKeTongFunctions yiKeTongFunctions;


    @Test
    public void parse() throws IOException {
        ResultModel binding = yiKeTongFunctions.binding("123123", "123123", "123123", 3000);

        System.out.println(binding);
    }
}
