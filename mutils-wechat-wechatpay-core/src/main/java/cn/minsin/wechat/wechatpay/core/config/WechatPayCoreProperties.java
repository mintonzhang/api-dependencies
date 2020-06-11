package cn.minsin.wechat.wechatpay.core.config;

import cn.minsin.core.constant.MutilsModelConstant;
import cn.minsin.core.exception.MutilsException;
import cn.minsin.core.init.AbstractConfig;
import cn.minsin.core.tools.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信支付核心配置文件
 *
 * @author mintonzhang
 * @date 2019年2月14日
 * @since 0.1.0
 */
@Getter
@Setter
@ConfigurationProperties(MutilsModelConstant.MUTILS_PREFIX + ".wechat.pay")
public class WechatPayCoreProperties extends AbstractConfig {

    /**
     * 是否启用支付功能
     */
    private boolean enable = false;
    /**
     * 商户id
     */
    private String partnerId;

    /**
     * 商户秘钥
     */
    private String partnerKey;

    /**
     * 退款请求地址
     */
    private String refundUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 付款通知地址
     */
    private String notifyUrl;

    /**
     * 退款异步通知地址
     */
    private String refundNotifyUrl;

    /**
     * 退款证书地址
     */
    private String certificatePath;

    /**
     * 统一下单地址
     */
    private String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 退款证书格式
     */
    private String certificateFormat = "PKCS12";

    /**
     * 是否包含退款 如果为true 如果为true certificatePath、refundUrl、certificateFormat必填
     */
    private boolean withRefund = false;

    /**
     * 提现地址
     */
    private String withdrawUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 是否同步 如果为false 那么 refundNotifyUrl不能为空
     */
    private boolean isSynchronizeRefund = true;

    @Override
    protected void checkConfig() {
        if (StringUtil.isBlank(partnerId, partnerKey, notifyUrl, unifiedOrderUrl)) {
            throw new MutilsException("请检查微信支付的必填配置项. partnerId, partnerKey, notifyUrl, unifiedOrderUrl");
        }
        if (withRefund) {
            if (StringUtil.isBlank(certificatePath, refundUrl, certificateFormat)) {
                throw new MutilsException("请检查微信退款相关配置项.certificatePath, refundUrl, certificateFormat");
            }
            if (!isSynchronizeRefund) {
                if (StringUtil.isBlank(refundNotifyUrl)) {
                    throw new MutilsException("请配置微信异步退款回调url");
                }
            }
        }
    }
}
