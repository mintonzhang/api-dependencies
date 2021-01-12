package cn.minsin.wechat.wechatpay.core.function;

import cn.minsin.core.constant.CharSetConstant;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.exception.MutilsException;
import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.tools.HttpClientUtil;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.core.tools.MapUtil;
import cn.minsin.wechat.wechatpay.core.config.WechatPayCoreProperties;
import cn.minsin.wechat.wechatpay.core.model.PayModel;
import cn.minsin.wechat.wechatpay.core.model.RefundModel;
import cn.minsin.wechat.wechatpay.core.model.RefundReturnModel;
import cn.minsin.wechat.wechatpay.core.model.WithdrawModel;
import cn.minsin.wechat.wechatpay.core.util.ParseXmlUtil;
import cn.minsin.wechat.wechatpay.core.util.SignUtil;
import lombok.Setter;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

/**
 * 微信配置文件(微信支付，微信公众号)
 *
 * @author mintonzhang
 * @date 2018年6月22日
 */
public class WeChatPayFunctions extends AbstractFunctionRule {

    @Setter
    protected WechatPayCoreProperties properties;


	/**
	 * 发起微信转账(提现)
	 *
	 * @param model 发起提现的包装类
	 * @throws MutilsErrorException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JDOMException
	 */
	public Map<String, String> createWithdrawXml(WithdrawModel model) throws Exception {
		model.setMchid(properties.getPartnerId());
		String xml = model.toXml(properties.getPartnerKey());
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			httpclient = HttpClientUtil.getSSLInstance(properties.getPartnerId(), properties.getCertificatePath(),
					properties.getCertificateFormat());
			HttpPost httpost = HttpClientUtil.getPostMethod(properties.getWithdrawUrl());

			httpost.setEntity(new StringEntity(xml, CharSetConstant.UTF_8));
            response = httpclient.execute(httpost);
            String jsonStr = EntityUtils.toString(response.getEntity(), CharSetConstant.UTF_8);
            return ParseXmlUtil.doXMLParse(jsonStr);
        } finally {
            IOUtil.close(httpclient, response);
        }
    }

	/**
	 * 发起退款申请
	 */
	protected RefundReturnModel createRefundRequest(RefundModel model) throws Exception {
		//设置商户id
		model.setMch_id(properties.getPartnerId());
		//设置回调地址
		model.setNotify_url(properties.isSynchronizeRefund() ? null : properties.getRefundNotifyUrl());
		String xmlParam = model.toXml(properties.getPartnerKey());
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try {
			httpclient = HttpClientUtil.getSSLInstance(properties.getPartnerId(), properties.getCertificatePath(),
					properties.getCertificateFormat());
            HttpPost httpost = HttpClientUtil.getPostMethod(properties.getRefundUrl());
            httpost.setEntity(new StringEntity(xmlParam, CharSetConstant.UTF_8));
            response = httpclient.execute(httpost);
            String jsonStr = EntityUtils.toString(response.getEntity(), CharSetConstant.UTF_8);
            return MapUtil.mapToObject(ParseXmlUtil.doXMLParse(jsonStr), RefundReturnModel.class);
        } finally {
            IOUtil.close(httpclient, response);
        }
    }

	/**
	 * 统一下单接口 用于生成 预支付id 及二维码id
	 *
	 * @param model 预下单的对象
	 * @thro
	 */
	protected Map<String, String> createUnifiedOrder(PayModel model) throws Exception {
		model.setMch_id(properties.getPartnerId());
		model.setNotify_url(properties.getNotifyUrl());
		// 先初始化;
		CloseableHttpClient httpclient = HttpClientUtil.getInstance();
		CloseableHttpResponse response = null;
		try {
			HttpPost httpost = HttpClientUtil.getPostMethod(properties.getUnifiedOrderUrl());
			String xmlParam = model.toXml(properties.getPartnerKey());
			httpost.setEntity(new StringEntity(xmlParam, CharSetConstant.UTF_8));
			response = httpclient.execute(httpost);
            String jsonStr = EntityUtils.toString(response.getEntity(), CharSetConstant.UTF_8);
            if (jsonStr.contains("FAIL")) {
                throw new MutilsErrorException(jsonStr);
            }
            return ParseXmlUtil.doXMLParse(jsonStr);
        } finally {
            IOUtil.close(httpclient, response);
        }

    }

	protected void checkMap(Map<String, String> doXMLParse) {
		if (doXMLParse == null || doXMLParse.isEmpty()) {
			throw new MutilsException(
					"统一支付XML生成失败,无法进行下一步操作. The value from createUnifiedOrder method is null,please check the parameters.");
		}
	}

    /**
     * 生成签名
     *
     * @param sortMap
     */
    protected String createSign(SortedMap<String, String> sortMap) {
        return SignUtil.createSign(sortMap, properties.getPartnerKey());
    }


}
