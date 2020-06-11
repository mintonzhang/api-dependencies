package cn.minsin.test;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.gexin.push.function.GexinPushFunctions;
import cn.minsin.gexin.push.model.PushModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 20:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GeXinPushTest {


    @Autowired
    private GexinPushFunctions gexinPushFunctions;

    @Test
    public void parse() throws MutilsErrorException {
        PushModel pushModel = new PushModel();
        gexinPushFunctions.push(pushModel);
    }
}
