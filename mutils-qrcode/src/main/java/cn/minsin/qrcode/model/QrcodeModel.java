package cn.minsin.qrcode.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class QrcodeModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3139191851191875798L;

	/**
	 * 二维码内容
	 */
	private String content;

	/**
	 * 宽度
	 */
	private Integer width = 500;

	/**
	 * 高度
	 */
	private Integer height = 500;

	/**
	 * 图片类型
	 */
	private String format = "png";

	/**
	 * 二维码中logo,文件路径不能包含中文
	 */
	private LogoModel logoImageModel;

	/**
	 * 二维码白边等级  -1表示没有白边  0 1 2 3 4  白边要依次增多 默认1
	 */
	private int level = -1;
}
