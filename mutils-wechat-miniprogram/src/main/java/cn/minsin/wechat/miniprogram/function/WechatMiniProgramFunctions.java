package cn.minsin.wechat.miniprogram.function;

import cn.minsin.core.constant.CharSetConstant;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.tools.HttpClientUtil;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.core.tools.ModelUtil;
import cn.minsin.wechat.miniprogram.config.MutilsWechatMiniProgramProperties;
import cn.minsin.wechat.miniprogram.model.Code2SessionReturnModel;
import cn.minsin.wechat.miniprogram.model.MiniProgramCodeModel;
import cn.minsin.wechat.miniprogram.model.MiniProgramOrderPayModel;
import cn.minsin.wechat.miniprogram.model.MiniProgramRefundModel;
import cn.minsin.wechat.miniprogram.model.UserInfoModel;
import cn.minsin.wechat.wechatpay.core.config.WechatPayCoreProperties;
import cn.minsin.wechat.wechatpay.core.function.WeChatPayFunctions;
import cn.minsin.wechat.wechatpay.core.model.AccessTokenModel;
import cn.minsin.wechat.wechatpay.core.model.RefundReturnModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 小程序相关功能
 *
 * @author mintonzhang
 * @date 2019年1月10日
 */
@Slf4j
@Component
public class WechatMiniProgramFunctions extends WeChatPayFunctions {

    @Autowired
    private MutilsWechatMiniProgramProperties properties;

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
     * 获取sessionkey和openid,一般用于小程序授权登录. 官方文档
     * https://developers.weixin.qq.com/miniprogram/dev/api/code2Session.html
     *
     * @param code 小程序获取的code
     * @throws IOException
     * @throws ClientProtocolException
     */
    public Code2SessionReturnModel jscode2session(String code) throws ClientProtocolException, IOException {
        CloseableHttpClient build = HttpClientUtil.getInstance();
        CloseableHttpResponse response = null;
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";

            url = url.replace("APPID", properties.getAppid()).replace("SECRET", properties.getAppSecret()).replace("JSCODE",
                    code);
            HttpGet get = HttpClientUtil.getGetMethod(url);

            response = build.execute(get);
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            return JSON.parseObject(string, Code2SessionReturnModel.class);
        } finally {
            IOUtil.close(build, response);
        }
    }

    /**
     * 解密用户敏感数据获取用户信息 注意wx.login() 必须要在wx.getUserinfo()前调用
     *
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @throws IOException
     * @throws ClientProtocolException
     * @throws NoSuchPaddingException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws InvalidParameterSpecException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public UserInfoModel getUserInfo(String encryptedData, String code, String iv)
            throws ClientProtocolException, IOException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException {
        Code2SessionReturnModel jscode2session = jscode2session(code);
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(jscode2session.getSession_key());
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + 1;
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
        byte[] resultByte = cipher.doFinal(dataByte);
        String result = new String(resultByte, StandardCharsets.UTF_8);
        return JSONObject.parseObject(result, UserInfoModel.class);
    }

    /**
     * 创建小程序支付的请求参数 小程序将用其发起微信支付 注意：小程序必须要要使用填写openid 参考
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     *
     * @param model 下单时的包装对象
     *              小程序能发起的请求的包装内容
     * @throws IOException
     * @throws ParseException
     */
    public Map<String, String> createMiniProgramPayParamter(MiniProgramOrderPayModel model)
            throws ParseException, IOException, MutilsErrorException {
        model.setAppid(properties.getAppid());
        Map<String, String> doXMLParse = super.createUnifiedOrder(model);
        checkMap(doXMLParse);
        SortedMap<String, String> sortMap = new TreeMap<>();
        String appId = doXMLParse.get("appid");
        String nonceStr = doXMLParse.get("nonce_str");
        String package_str = "prepay_id=" + doXMLParse.get("prepay_id");
        String signType = "MD5";
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        sortMap.put("appId", appId);
        sortMap.put("nonceStr", nonceStr);
        sortMap.put("package", package_str);
        sortMap.put("signType", signType);
        sortMap.put("timeStamp", timeStamp);
        sortMap.put("paySign", super.createSign(sortMap));
        sortMap.remove("appId");
        return sortMap;
    }

    /**
     * 发起退款申请 参考 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     *
     * @param model
     * @throws MutilsErrorException
     * @throws IOException
     * @throws JDOMException
     * @throws ClientProtocolException
     */
    public RefundReturnModel createMiniProgramRefundParamter(MiniProgramRefundModel model)
            throws ClientProtocolException, IOException, MutilsErrorException {
        model.setAppid(properties.getAppid());
        return super.createRefundRequest(model);
    }

    /**
     * 小程序获取accessToken 详情参考
     * https://developers.weixin.qq.com/miniprogram/dev/api/getAccessToken.html
     *
     * @throws IOException
     * @throws ClientProtocolException
     */
    public AccessTokenModel getAccessToken() throws ClientProtocolException, IOException {
        CloseableHttpClient instance = HttpClientUtil.getInstance();
        CloseableHttpResponse response = null;
        try {
            String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
            String requestUrl = accessTokenUrl.replace("APPID", properties.getAppid()).replace("APPSECRET",
                    properties.getAppSecret());

            HttpGet getMethod = HttpClientUtil.getGetMethod(requestUrl);
            response = instance.execute(getMethod);
            String string = EntityUtils.toString(response.getEntity(), CharSetConstant.UTF_8);
            getMethod.releaseConnection();
            return JSON.parseObject(string, AccessTokenModel.class);
        } finally {
            IOUtil.close(instance, response);
        }
    }

    /**
     * 获取小程序码 参考
     * https://developers.weixin.qq.com/miniprogram/dev/api/getWXACodeUnlimit.html
     *
     * @param model
     * @throws IOException
     * @throws ClientProtocolException
     * @throws MutilsErrorException
     */
    public InputStream getMiniProgramQrCode(MiniProgramCodeModel model)
            throws ClientProtocolException, IOException, MutilsErrorException {
        ModelUtil.verificationField(model);
        CloseableHttpClient httpClient = HttpClientUtil.getInstance();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(
                    "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + model.getAccess_token());
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            StringEntity entity = new StringEntity(model.toString());
            entity.setContentType("image/png");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, CharSetConstant.UTF_8));
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            InputStream content = response.getEntity().getContent();
            byte[] copyInputStream = IOUtil.copyInputStream(content);
            return new ByteArrayInputStream(copyInputStream);
        } finally {
            IOUtil.close(httpClient, response);
        }

    }
}
