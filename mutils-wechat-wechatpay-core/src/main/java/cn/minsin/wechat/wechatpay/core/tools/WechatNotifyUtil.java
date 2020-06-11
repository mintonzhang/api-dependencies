package cn.minsin.wechat.wechatpay.core.tools;

import cn.minsin.core.exception.MutilsErrorException;
import cn.minsin.core.tools.IOUtil;
import cn.minsin.core.tools.MapUtil;
import cn.minsin.wechat.wechatpay.core.util.ParseXmlUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 16:28
 */
public class WechatNotifyUtil {

    /**
     * 微信支付回调解析
     * <xml><return_code><![CDATA[STATE]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>
     * 如果成功 将STATE替换为SUCCESS 如果失败替换为FAIL 反馈给微信服务器不用再重复请求。 使用PrintWriter.println直接输出
     *
     * @param req
     * @throws IOException
     * @throws MutilsErrorException
     */
    public static <T> T parseNotify(Class<T> clazz, HttpServletRequest req) throws IOException, MutilsErrorException {
        BufferedReader br = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(req.getInputStream());
            br = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            Map<String, String> map = ParseXmlUtil.doXMLParse(sb.toString());
            return MapUtil.mapToObject(map, clazz);
        } finally {
            IOUtil.close(br, inputStreamReader);
        }

    }

    /**
     * 微信返回值
     *
     * @param isSuccess 是否成功
     */
    public static String notifyReturnValue(boolean isSuccess) {
        return String.format("<xml><return_code><![CDATA[%s]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>", isSuccess ? "SUCCESS" : "FAIL");
    }
}
