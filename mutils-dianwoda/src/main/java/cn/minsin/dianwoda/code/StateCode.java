package cn.minsin.dianwoda.code;

/**
 * 点我达接口返回状态码
 *
 * @author minsin
 */
public enum StateCode {

    C0(0, "接口调用成功"),
    C1000(1000, "未知错误"),
    C1001(1001, "服务不可用"),
    C1002(1002, "无效PK{0}"),
    C1003(1003, "缺少签名参数"),
    C1004(1004, "签名无效{0}"),
    C1005(1005, "HTTP方法{0}被禁止"),
    C1006(1006, "编码错误"),
    C1007(1007, "请求被禁止"),
    C1008(1008, "调用服务的次数超限"),
    C1009(1009, "调用频率超限"),
    C1010(1010, "PK{0}被禁用"),
    C2000(2001, "参数错误 Fields:“问题参数名”"),
    C3000(3001, "业务逻辑出错,“具体错误信息”"),
    C3002(3002, "正在派发的订单对应的订单已存在"),
    C3003(3003, "错误的城市编码或点我达并不能提供服务"),
    C3004(3004, "当前订单状态下不允许修改订单状态"),
    C3005(3005, "当前订单状态下不允许修改订单金额"),
    C3006(3006, "订单不存在"),
    C3007(3007, "当前订单状态下不允许修改订单信息"),
    C4001(4001, "订单距离超出"),
    C4002(4002, "商家不存在"),
    C4003(4003, "账户余额不足");

    private int code;

    private String msg;

    private StateCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(int code) {
        for (StateCode c : StateCode.values()) {
            if (c.getCode() == code) {
                return c.getMsg();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
