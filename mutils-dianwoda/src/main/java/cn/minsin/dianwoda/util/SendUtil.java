package cn.minsin.dianwoda.util;

import cn.minsin.core.tools.HttpClientUtil;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.dianwoda.config.DianWoDaProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 18:04
 */
public class SendUtil {

    /**
     * 模拟发送http请求
     *
     * @param url            业务api接口
     * @param businessParams 业务参数
     *                       响应结果
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static JSONObject doSend(String url, Map<String, Object> businessParams, DianWoDaProperties properties)
            throws ClientProtocolException, IOException {
        CloseableHttpClient build = HttpClientUtil.getInstance();
        CloseableHttpResponse response = null;
        try {
            /* 生成签名 */
            String sign = SignUtil.sign(businessParams, properties.getSercret());
            businessParams.put("pk", properties.getPk());
            businessParams.put("v", properties.getVersion());
            businessParams.put("format", properties.getFormat());
            businessParams.put("sig", sign);
            businessParams.put("timestamp", properties.getTimestamp());
            HttpPost post = new HttpPost(properties.getUrl() + url);
            List<NameValuePair> list = new LinkedList<>();
            businessParams.keySet().forEach(k -> list.add(new BasicNameValuePair(k, businessParams.get(k).toString())));
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
            post.setEntity(uefEntity);
            response = build.execute(post);
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            return JSON.parseObject(string);
        } finally {
            IOUtil.close(build, response);
        }
    }
}
