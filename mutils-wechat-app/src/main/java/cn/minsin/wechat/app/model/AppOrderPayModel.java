package cn.minsin.wechat.app.model;

import cn.minsin.wechat.wechatpay.core.model.PayModel;

public class AppOrderPayModel extends PayModel {

    /**
     *
     */
    private static final long serialVersionUID = -4389845287484100322L;


    public AppOrderPayModel() {
        this.setTrade_type("APP");
    }

}
