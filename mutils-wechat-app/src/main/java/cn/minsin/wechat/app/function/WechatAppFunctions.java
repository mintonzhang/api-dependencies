package cn.minsin.wechat.app.function;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.wechat.app.config.MutilsWechatAppProperties;
import cn.minsin.wechat.app.model.AppOrderPayModel;
import cn.minsin.wechat.app.model.AppRefundModel;
import cn.minsin.wechat.wechatpay.core.config.WechatPayCoreProperties;
import cn.minsin.wechat.wechatpay.core.function.WeChatPayFunctions;
import cn.minsin.wechat.wechatpay.core.model.RefundReturnModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * * APP 移动端的API
 *
 * @author mintonzhang
 * @date 2019年2月12日
 * @since 0.2.0
 */
@Slf4j
@Component
public class WechatAppFunctions extends WeChatPayFunctions {

    @Autowired
    private MutilsWechatAppProperties properties;

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
     * 创建APP支付的请求参数 APP将用其发起微信支付
     *
     * @param model 下单时的包装对象
     *              APP能发起的请求的包装内容
     * @throws JDOMException
     * @throws MutilsErrorException
     * @throws IOException
     * @throws ParseException
     * @throws Exception
     */
    public Map<String, String> createAppPayParamter(AppOrderPayModel model) throws ParseException, IOException, MutilsErrorException {
        model.setAppid(properties.getAppid());
        Map<String, String> doXMLParse = super.createUnifiedOrder(model);
        this.checkMap(doXMLParse);
        SortedMap<String, String> sortMap = new TreeMap<>();
        String appId = doXMLParse.get("appid");
        String nonceStr = doXMLParse.get("nonce_str");
        String prepayid = doXMLParse.get("prepay_id");
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        sortMap.put("appid", appId);
        sortMap.put("partnerid", coreProperties.getPartnerId());
        sortMap.put("noncestr", nonceStr);
        sortMap.put("package", "Sign=WXPay");
        sortMap.put("timestamp", timeStamp);
        sortMap.put("prepayid", prepayid);
        sortMap.put("sign", super.createSign(sortMap));
        return sortMap;
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
    public RefundReturnModel createAppRefundParamter(AppRefundModel model) throws MutilsErrorException, ClientProtocolException, IOException {
        model.setAppid(properties.getAppid());
        return super.createRefundRequest(model);
    }
}
