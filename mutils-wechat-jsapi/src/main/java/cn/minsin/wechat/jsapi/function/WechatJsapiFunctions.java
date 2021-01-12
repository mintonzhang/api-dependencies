package cn.minsin.wechat.jsapi.function;

import cn.minsin.core.constant.CharSetConstant;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.tools.HttpClientUtil;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.wechat.jsapi.config.MutilsWechatJsapiProperties;
import cn.minsin.wechat.jsapi.model.JsapiOrderPayModel;
import cn.minsin.wechat.jsapi.model.JsapiRefundModel;
import cn.minsin.wechat.wechatpay.core.config.WechatPayCoreProperties;
import cn.minsin.wechat.wechatpay.core.function.WeChatPayFunctions;
import cn.minsin.wechat.wechatpay.core.model.AccessTokenModel;
import cn.minsin.wechat.wechatpay.core.model.RefundReturnModel;
import cn.minsin.wechat.wechatpay.core.util.Sha1Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * jsapi相关功能
 *
 * @author mintonzhang
 * @date 2019年1月10日
 */
@Slf4j
@Component
public class WechatJsapiFunctions extends WeChatPayFunctions {

    @Autowired
    private MutilsWechatJsapiProperties properties;

    @Autowired(required = false)
    private WechatPayCoreProperties coreProperties;

    @PostConstruct
    private void init() {
        if (coreProperties == null) {
            log.info("微信支付相关功能无法使用,如需使用请在properties中配置WeChat配置mutils.wechat.pay相关属性");
        }
        super.setProperties(coreProperties);
    }

    /**
     * 发起退款申请
     *
     * @param model
     * @throws MutilsErrorException
     * @throws IOException
     * @throws JDOMException
     * @throws ClientProtocolException
     */
    public RefundReturnModel createJsapiRefundParamter(JsapiRefundModel model)
            throws MutilsErrorException, ClientProtocolException, IOException {
        model.setAppid(properties.getAppid());
        return super.createRefundRequest(model);
    }

    /**
     * 生成微信JS相关初始化config配置
     *
     * @param url       当前网页地址
     * @param debug     是否开启调试
     * @param functions 默认 "openLocation", "getLocation", "chooseWXPay"
     * @throws IOException
     * @throws ClientProtocolException
     */
    public Map<String, Object> createInitJSConfig(String url, boolean debug, String... functions) throws Exception {

	    if (functions == null || functions.length == 0) {
		    functions = new String[]{"openLocation", "getLocation", "chooseWXPay"};
	    }
	    String jsapi_ticket = getAccessToken().getJsapi_ticket();
	    // 10位序列号,可以自行调整。
	    String nonce_str = String.valueOf(System.currentTimeMillis());

	    Long timestamp = System.currentTimeMillis() / 1000;

        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("noncestr", nonce_str);
        packageParams.put("timestamp", timestamp.toString());
        packageParams.put("jsapi_ticket", jsapi_ticket);
        packageParams.put("url", url);
        String sign = Sha1Util.createSHA1Sign(packageParams);
        SortedMap<String, Object> returnMap = new TreeMap<>();
        returnMap.put("appId", properties.getAppid());
        returnMap.put("nonceStr", nonce_str);
        returnMap.put("timestamp", timestamp);
        returnMap.put("signature", sign);
        returnMap.put("jsApiList", functions);
        returnMap.put("debug", debug);
        return returnMap;
    }

	/**
	 * jsapi获取AccessToken用于实现登录
	 */
	public AccessTokenModel getAccessToken() throws Exception {

		CloseableHttpClient instance = HttpClientUtil.getInstance();
		CloseableHttpResponse response = null;
		try {
			String accessTokenUrl = properties.getAccessTokenUrl();

			String jsapiTicketUrl = properties.getJsapiTicketUrl();

			String appid = properties.getAppid();

            String appSecret = properties.getAppSecret();

            String requestUrl = accessTokenUrl.replace("APPID", appid).replace("APPSECRET", appSecret);

            HttpGet getMethod = HttpClientUtil.getGetMethod(requestUrl);
            response = instance.execute(getMethod);
            String string = EntityUtils.toString(response.getEntity(), CharSetConstant.UTF_8);
            getMethod.releaseConnection();
            JSONObject jsonObject = JSON.parseObject(string);
            String access_token = jsonObject.get("access_token").toString();
            String jsapi_ticketurl = jsapiTicketUrl.replace("ACCESS_TOKEN", access_token);
            getMethod = HttpClientUtil.getGetMethod(jsapi_ticketurl);
            string = EntityUtils.toString(response.getEntity(), CharSetConstant.UTF_8);
            getMethod.releaseConnection();
            jsonObject = JSON.parseObject(string);
            String jsapi_ticke = jsonObject.get("ticket").toString();
            AccessTokenModel accessToken = new AccessTokenModel();
            accessToken.setAccess_token(access_token);
            accessToken.setExpires_in(7200);
            accessToken.setExpires_time(System.currentTimeMillis() / 1000);
            accessToken.setJsapi_ticket(jsapi_ticke);
            return accessToken;
        } finally {
            IOUtil.close(instance, response);
        }
    }

    /**
     * 小程序将发起微信支付
     *
     * @param model 下单时的包装对象
     *              公众号能发起的请求的包装内容
     */
    public Map<String, String> createJsapiPayParamter(JsapiOrderPayModel model) throws Exception {
	    model.setAppid(properties.getAppid());
	    Map<String, String> doXMLParse = super.createUnifiedOrder(model);
	    checkMap(doXMLParse);
	    SortedMap<String, String> sortMap = new TreeMap<>();
	    String appId = doXMLParse.get("appid");
	    sortMap.put("appId", appId);
	    sortMap.put("nonceStr", doXMLParse.get("nonce_str"));
	    sortMap.put("package", "prepay_id=" + doXMLParse.get("prepay_id"));
	    sortMap.put("signType", "MD5");
	    sortMap.put("timeStamp", System.currentTimeMillis() / 1000 + "");
	    sortMap.put("paySign", super.createSign(sortMap));
        sortMap.put("prepay_id", doXMLParse.get("prepay_id"));
        return sortMap;

    }
}
