package cn.minsin.wechat.miniprogram.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: minton.zhang
 * @since: 2020/11/5 21:02
 */
@Getter
@Setter
public class WxPhoneNumberModel {

	private String phoneNumber;
	private String purePhoneNumber;
	private String countryCode;

}
