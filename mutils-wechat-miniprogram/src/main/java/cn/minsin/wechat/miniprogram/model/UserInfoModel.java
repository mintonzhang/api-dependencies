package cn.minsin.wechat.miniprogram.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4390557940217451678L;

	private String openId;
	private String nickName;
	private String gender;
	private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private Object watermark;

}

