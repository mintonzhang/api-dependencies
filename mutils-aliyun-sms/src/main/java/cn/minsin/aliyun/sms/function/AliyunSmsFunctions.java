package cn.minsin.aliyun.sms.function;

import cn.minsin.aliyun.sms.config.MutilsAliyunSmsProperties;
import cn.minsin.aliyun.sms.model.AliyunQueryModel;
import cn.minsin.aliyun.sms.model.AliyunSendSmsModel;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.rule.AbstractFunctionRule;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 阿里云短信
 *
 * @author mintonzhang
 * @date 2019年1月17日
 * @since 0.2.5
 */
@Component
public class AliyunSmsFunctions extends AbstractFunctionRule {


    @Autowired
    private MutilsAliyunSmsProperties properties;

    private static IAcsClient client;

    @PostConstruct
    public void init() {
        if (properties != null) {
            client = initClient();
        }
    }

    /**
     * 发送短信给单个用户
     *
     * @param model
     * @throws ServerException
     * @throws ClientException
     * @throws MutilsErrorException
     */
    public SendSmsResponse sendSingleSms(AliyunSendSmsModel model)
            throws ServerException, ClientException, MutilsErrorException {

        return client.getAcsResponse(model.toSendSmsRequest());
    }

    /**
     * 发送短信给多个用户
     *
     * @param model 发送短信的model
     * @throws ServerException
     * @throws ClientException
     * @throws MutilsErrorException mutils框架内自定义异常
     */
    public SendBatchSmsResponse sendBatchSms(AliyunSendSmsModel model)
            throws ServerException, ClientException, MutilsErrorException {

        return client.getAcsResponse(model.toSendBatchSmsRequest());
    }

    /**
     * 查询某个号码的最近30天的使用情况
     *
     * @param model
     * @throws ServerException
     * @throws ClientException
     */
    public QuerySendDetailsResponse querySendDetails(AliyunQueryModel model)
            throws ServerException, ClientException {
        return client.getAcsResponse(model.toQuerySendDetailsRequest());
    }

    /**
     * 初始化短信客户端
     */
    protected IAcsClient initClient() {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(properties.getConnectTimeout()));
        System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(properties.getReadTimeout()));

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", properties.getAccessKeyId(),
                properties.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou", properties.getProduct(), properties.getDomain());
        return new DefaultAcsClient(profile);
    }
}
