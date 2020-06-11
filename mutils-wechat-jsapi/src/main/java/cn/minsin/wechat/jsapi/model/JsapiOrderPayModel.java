package cn.minsin.wechat.jsapi.model;

import cn.minsin.core.annotation.NotNull;
import cn.minsin.wechat.wechatpay.core.model.PayModel;
import lombok.Getter;
import lombok.Setter;

public class JsapiOrderPayModel extends PayModel {

    /**
     *
     */
    private static final long serialVersionUID = -2340902354991037737L;

    @Getter
    @Setter
    @NotNull("用户的openid")
    private String openid;

    public JsapiOrderPayModel() {
        super();
        this.setTrade_type("JSAPI");
    }
}
