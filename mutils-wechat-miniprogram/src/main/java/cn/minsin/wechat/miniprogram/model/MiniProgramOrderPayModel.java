package cn.minsin.wechat.miniprogram.model;

import cn.minsin.wechat.wechatpay.core.model.PayModel;
import lombok.Getter;
import lombok.Setter;

public class MiniProgramOrderPayModel extends PayModel {

	@Getter
	@Setter
	private String openid;

	public MiniProgramOrderPayModel() {
		super();
		this.setTrade_type("JSAPI");
	}
}
