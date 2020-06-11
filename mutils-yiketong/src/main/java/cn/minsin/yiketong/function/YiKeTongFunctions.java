package cn.minsin.yiketong.function;

import cn.minsin.core.constant.CharSetConstant;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.yiketong.config.YiKeTongProperties;
import cn.minsin.yiketong.model.ResultModel;
import cn.minsin.yiketong.util.ParamUtil;
import cn.minsin.yiketong.util.SignUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 15:54
 */
@Component
public class YiKeTongFunctions {

    @Autowired
    private YiKeTongProperties properties;

    /**
     * 手机号进行虚拟号绑定
     *
     * @param area_code
     * @param tel_a
     * @param tel_b
     * @throws IOException
     * @throws ClientProtocolException
     */
    public ResultModel binding(String area_code, String tel_a, String tel_b, int timeout)
            throws ClientProtocolException, IOException {

        String url = properties.getApiUrl() + "number/axb/binding";
        CloseableHttpClient build = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            String ts = String.valueOf(System.currentTimeMillis() / 1000);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("appkey", properties.getCorpKey());
            params.put("ts", ts);
            params.put("request_id", System.currentTimeMillis());
            params.put("tel_a", tel_a);
            params.put("tel_b", tel_b);
            params.put("area_code", area_code);
            params.put("expiration", timeout);

            String orderStr = ParamUtil.createLinkString(params);
            orderStr = orderStr + "&secret=" + properties.getCorpSecret();
            String sign = SignUtil.encode(orderStr);
            params.put("sign", sign);

            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity(JSONObject.toJSONString(params), CharSetConstant.UTF_8);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, Consts.UTF_8.toString()));
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.setEntity(se);
            response = build.execute(post);

            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            return JSON.parseObject(string, ResultModel.class);
        } finally {
            IOUtil.close(build, response);
        }
    }

    /**
     * 查询号码池使用率 由于服务商规定 只查询60%的数据 以下的data都会为空
     *
     * @throws IOException
     * @throws ClientProtocolException
     */
    public ResultModel utilization() throws ClientProtocolException, IOException {

        CloseableHttpClient build = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            String url = properties.getApiUrl() + "monitor/axb/utilization";
            String ts = String.valueOf(System.currentTimeMillis() / 1000);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("appkey", properties.getCorpKey());
            params.put("ts", ts);
            String orderStr = ParamUtil.createLinkString(params);
            orderStr = orderStr + "&secret=" + properties.getCorpSecret();
            String sign = SignUtil.encode(orderStr);
            params.put("sign", sign);

            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity(JSONObject.toJSONString(params), CharSetConstant.UTF_8);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, Consts.UTF_8.toString()));
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.setEntity(se);
            response = build.execute(post);

            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            return JSON.parseObject(string, ResultModel.class);
        } finally {
            IOUtil.close(build, response);
        }
    }
}
