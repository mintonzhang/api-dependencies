package cn.minsin.gexin.push.function;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.exception.MutilsException;
import cn.minsin.core.rule.AbstractFunctionRule;
import cn.minsin.core.tools.ModelUtil;
import cn.minsin.gexin.push.config.GexinPushMultiProperties;
import cn.minsin.gexin.push.config.GexinPushProperties;
import cn.minsin.gexin.push.model.PushModel;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 个性推送功能列表
 *
 * @author mintonzhang
 * @date 2019年1月16日
 * @since 0.2.3
 */
@Component
public class GexinPushFunctions extends AbstractFunctionRule {

    @Autowired
    private GexinPushProperties properties;

    private GexinPushMultiProperties childConfig;

    public GexinPushFunctions init(String key) {
        GexinPushMultiProperties gexinPushMultiProperties = properties.getConfig().get(key);
        MutilsException.throwException(gexinPushMultiProperties == null, "未找到名为'" + key + "'对应的记录");
        childConfig = gexinPushMultiProperties;
        return this;
    }

    protected IGtPush initPush() {
        return new IGtPush(properties.getUrl(), childConfig.getAppkey(), childConfig.getMasterSecret());
    }

    /**
     * 推送单个
     *
     * @param model 推送的model
     * @throws MutilsErrorException
     */
    public IPushResult pushSingle(PushModel model) throws MutilsErrorException {
        model.setPushMany(false);
        return push(model);
    }

    /**
     * 推送单个
     *
     * @param model 推送的model
     * @throws MutilsErrorException
     */
    public IPushResult pushMany(PushModel model) throws MutilsErrorException {
        model.setPushMany(true);
        return push(model);
    }

    /**
     * 推送一个或多个由pushMany 控制
     * 1.修复推送消息无法显示标题头 since 0.3.2
     *
     * @param model 推送的对象
     * @throws MutilsErrorException
     */
    public IPushResult push(PushModel model) throws MutilsErrorException {
        ModelUtil.verificationField(model);
        IGtPush push = initPush();
        String content = model.getContent();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(childConfig.getAppid());
        template.setAppkey(childConfig.getAppkey());
        template.setTransmissionType(model.getTransmissionType());
        template.setTransmissionContent(content);

        APNPayload payload = new APNPayload();
        payload.setContentAvailable(1);
        payload.setSound(model.getSound());
        payload.setCategory(content);

        APNPayload.DictionaryAlertMsg msg = new APNPayload.DictionaryAlertMsg();
        msg.setSubtitle(model.getSubTitle());
        msg.setTitle(model.getTitle());
        payload.setAlertMsg(msg);
        template.setAPNInfo(payload);

        List<String> clientids = model.getClientids();
        if (clientids.isEmpty()) {
            throw new MutilsErrorException("clientid不能为空");
        }
        boolean pushMany = model.isPushMany();
        if (!pushMany) {
            SingleMessage message = new SingleMessage();
            message.setOffline(true);
            message.setOfflineExpireTime(model.getTimeout());
            message.setData(template);
            message.setPushNetWorkType(model.getPushNetWorkType());
            Target target = new Target();
            target.setAppId(childConfig.getAppid());
            target.setClientId(clientids.get(0));
            return push.pushMessageToSingle(message, target);
        }
        ListMessage message = new ListMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(model.getTimeout());
        message.setData(template);
        message.setPushNetWorkType(model.getPushNetWorkType());

        List<Target> targets = new ArrayList<Target>();
        for (String clientid : clientids) {
            Target target = new Target();
            target.setAppId(childConfig.getAppid());
            target.setClientId(clientid);
            targets.add(target);
        }
        String contentId = push.getContentId(message);
        return push.pushMessageToList(contentId, targets);
    }

}
