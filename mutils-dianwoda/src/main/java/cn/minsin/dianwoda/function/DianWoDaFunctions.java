package cn.minsin.dianwoda.function;

import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.tools.MapUtil;
import cn.minsin.dianwoda.config.DianWoDaProperties;
import cn.minsin.dianwoda.model.OrderModel;
import cn.minsin.dianwoda.util.SendUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DianWoDaFunctions extends AbstractFunctionRule {

    @Autowired
    private DianWoDaProperties properties;

    /**
     * 派发订单 /api/v3/order-send.json
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @throws Exception
     */
    public JSONObject order_send(OrderModel ot) throws ClientProtocolException, IOException {
        return SendUtil.doSend("/api/v3/order-send.json", MapUtil.toMap(ot), properties);
    }

}
