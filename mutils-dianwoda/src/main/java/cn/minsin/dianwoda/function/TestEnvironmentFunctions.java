package cn.minsin.dianwoda.function;

import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.vo.VO;
import cn.minsin.dianwoda.config.DianWoDaProperties;
import cn.minsin.dianwoda.util.SendUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 点我达测试环境的专用接口
 *
 * @author mintonzhang
 * @date 2019年2月14日
 * @since 0.1.0
 */
@Component
public class TestEnvironmentFunctions extends AbstractFunctionRule {

    @Autowired
    private DianWoDaProperties properties;

    /**
     * 接受订单（测试接口仅测试环境有效）
     *
     * @param order_original_id
     * @throws IOException
     * @throws ClientProtocolException
     */
    public JSONObject orderAcceptedTest(String order_original_id) throws ClientProtocolException, IOException {
        return SendUtil.doSend("/api/v3/order-accepted-test.json",
                VO.init().put("order_original_id", order_original_id), properties);
    }

    /**
     * 完成到店（测试接口仅测试环境有效）
     *
     * @param order_original_id
     * @throws IOException
     * @throws ClientProtocolException
     */
    public JSONObject orderArriveTest(String order_original_id) throws ClientProtocolException, IOException {
        return SendUtil.doSend("/api/v3/order-arrive-test.json",
                VO.init().put("order_original_id", order_original_id), properties);
    }


    /**
     * 完成取货（测试接口仅测试环境有效）
     *
     * @param order_original_id
     * @throws IOException
     * @throws ClientProtocolException
     */
    public JSONObject orderFetchTest(String order_original_id) throws ClientProtocolException, IOException {
        return SendUtil.doSend("/api/v3/order-fetch-test.json",
                VO.init().put("order_original_id", order_original_id), properties);
    }

    /**
     * 完成配送（测试接口仅测试环境有效）
     *
     * @param order_original_id
     * @throws IOException
     * @throws ClientProtocolException
     */
    public JSONObject orderFinishTest(String order_original_id) throws ClientProtocolException, IOException {
        return SendUtil.doSend("/api/v3/order-finish-test.json",
                VO.init().put("order_original_id", order_original_id), properties);
    }
}
