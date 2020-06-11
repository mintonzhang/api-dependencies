/**
 *
 */
package cn.minsin.kuaidi100.function;

import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.tools.HttpClientUtil;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.core.vo.VO;
import cn.minsin.kuaidi100.config.MutilsKuaiDi100Properties;
import cn.minsin.kuaidi100.util.MD5Util;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 快递100物流查询
 *
 * @author mintonzhang
 */
@Component
public class KuaiDi100Functions extends AbstractFunctionRule {

    @Autowired
    private MutilsKuaiDi100Properties properties;

    /**
     * 查询物流单号
     *
     * @param logisticsCode   物流公司code
     * @param logisticsNumber 物流单号
     *  2018年7月20日
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String getLogistics(String logisticsCode, String logisticsNumber) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            String param = VO.init("com", logisticsNumber).put("num", logisticsCode).toString();
            String sign = MD5Util.encode(param + properties.getKey() + properties.getCustomer());
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("param", param));
            params.add(new BasicNameValuePair("sign", sign));
            params.add(new BasicNameValuePair("customer", properties.getCustomer()));
            httpclient = HttpClientUtil.getInstance();
            HttpPost post = HttpClientUtil.getPostMethod(properties.getUrl());
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            response = httpclient.execute(post);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            IOUtil.close(response, httpclient);
        }
    }
}
