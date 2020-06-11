package cn.minsin.alipay.function;

import cn.minsin.alipay.config.MutilsAlipayProperties;
import cn.minsin.alipay.model.NotifyModel;
import cn.minsin.alipay.model.PayModel;
import cn.minsin.alipay.model.RefundModel;
import cn.minsin.alipay.model.TransferModel;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.tools.MapUtil;
import cn.minsin.core.tools.ModelUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝功能列表
 *
 * @author mintonzhang
 * @date 2019年1月17日
 * @since 0.1.0
 */

@Component
public class AlipayFunctions extends AbstractFunctionRule {

    @Autowired
    protected MutilsAlipayProperties properties;

    /**
     * 发起支付宝网站支付
     *
     * @throws MutilsErrorException
     * @throws AlipayApiException
     */
    public AlipayTradePrecreateResponse createWebAlipayParams(PayModel payModel) throws AlipayApiException {
        ModelUtil.verificationField(payModel);
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
        alipayRequest.setBizContent(payModel.toString());
        alipayRequest.setNotifyUrl(properties.getNotifyUrl());
        return initAlipayClient().execute(alipayRequest);
    }

    /**
     * 发起支付宝订单生成
     *
     * @throws AlipayApiException
     */
    public AlipayTradeAppPayResponse createAlipayParams(PayModel payModel) throws AlipayApiException {
        ModelUtil.verificationField(payModel);
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        alipayRequest.setBizContent(payModel.toString());
        alipayRequest.setNotifyUrl(properties.getNotifyUrl());
        return initAlipayClient().sdkExecute(alipayRequest);

    }

    /**
     * 支付宝转账
     *
     * @param model 2018年12月6日
     * @throws AlipayApiException
     */
    public AlipayFundTransToaccountTransferResponse transfer(TransferModel model) throws AlipayApiException {
        ModelUtil.verificationField(model);
        AlipayFundTransToaccountTransferRequest alipayRequest = new AlipayFundTransToaccountTransferRequest();
        alipayRequest.setBizContent(model.toString());
        return initAlipayClient().execute(alipayRequest);
    }

    /**
     * 支付宝退款 退款是需要验证public_key 请确认是否和sign_type匹配
     *
     * @param model
     * @throws AlipayApiException
     */
    public AlipayTradeRefundResponse refund(RefundModel model) throws AlipayApiException {
        ModelUtil.verificationField(model);
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizContent(model.toString());
        return initAlipayClient().execute(alipayRequest);
    }

    /**
     * 解析支付宝回调并验证签名
     * <p>
     * 如果成功 使用PrintWriter.println输出 success 如果失败输出failed 反馈给支付宝服务器不用再重复请求。
     *
     * @param req
     * @throws UnsupportedEncodingException
     */
    public NotifyModel parseNotify(HttpServletRequest req, boolean checkSign) throws UnsupportedEncodingException {
        req.setCharacterEncoding("utf-8");
        Map<String, String> retMap = new HashMap<String, String>();
        Map<String, String[]> requestParams = req.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            retMap.put(name, valueStr);
        }
        if (checkSign) {
            if (!checkSign(retMap)) {
                return null;
            }
        }
        return MapUtil.mapToObject(retMap, NotifyModel.class);
    }

    /**
     * 验证签名
     *
     * @param params
     */
    public boolean checkSign(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, properties.getPublicKey(), properties.getCharset(), properties.getSignType());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected AlipayClient initAlipayClient() {
        return new DefaultAlipayClient(properties.getServerUrl(), properties.getAppid(), properties.getPrivateKey(),
                properties.getFormat(), properties.getCharset(), properties.getPublicKey(), properties.getSignType());
    }
}
