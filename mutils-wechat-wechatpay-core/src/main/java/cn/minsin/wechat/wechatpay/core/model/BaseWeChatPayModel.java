package cn.minsin.wechat.wechatpay.core.model;

import cn.minsin.core.annotation.NotNull;
import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.rule.AbstractModelRule;
import cn.minsin.core.tools.ModelUtil;
import cn.minsin.core.tools.StringUtil;
import cn.minsin.wechat.wechatpay.core.util.SignUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.SortedMap;


public abstract class BaseWeChatPayModel extends AbstractModelRule {

    /**
     *
     */
    private static final long serialVersionUID = -6916140873435262221L;

    @NotNull(value = "签名  自动生成无须填写", notNull = false)
    @Getter
    private String sign;

    /**
     * 生成xml
     *
     * @throws MutilsErrorException
     */
    public String toXml(String partnerKey) {
        SortedMap<String, String> treeMap = ModelUtil.toTreeMap(this);
        this.sign = SignUtil.createSign(treeMap, partnerKey);
        StringBuilder sb = new StringBuilder("<xml>");
        for (Field field : ModelUtil.getAllFields(this)) {
            try {
                if (ModelUtil.verificationField(field)) {
                    continue;
                }
                field.setAccessible(true);
                Object object = field.get(this);
                if (!StringUtil.isBlank(object)) {
                    sb.append("<").append(field.getName()).append(">");
                    sb.append(object).append("</").append(field.getName()).append(">");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.append("</xml>").toString();
    }
}
