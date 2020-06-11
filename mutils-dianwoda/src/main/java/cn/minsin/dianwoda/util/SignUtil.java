package cn.minsin.dianwoda.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 点我达签名生成
 *
 * @author minsin
 */
public class SignUtil {

    /**
     * 生成签名
     *
     * @param paramValues 签名参数
     * @param secret      秘钥
     *                    签名
     */
    public static String sign(Map<String, Object> paramValues, String secret) {
        try {
            StringBuilder sb = new StringBuilder();
            List<String> paramNames = new ArrayList<String>(paramValues.size());
            paramNames.addAll(paramValues.keySet());
            Collections.sort(paramNames);
            sb.append(secret);
            paramNames.forEach(paramName -> sb.append(paramName).append(paramValues.get(paramName)));
            sb.append(secret);
            System.out.println("得到的加密前文段：" + sb);
            byte[] sha1Digest = getSHA1Digest(sb.toString());
            System.out.println("得到的签名：" + byte2hex(sha1Digest));
            return byte2hex(sha1Digest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取SHA1
     *
     * @param data 数据
     *             byte数组
     * @throws IOException IO异常
     */
    private static byte[] getSHA1Digest(String data) throws IOException {
        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            bytes = md.digest(data.getBytes("utf-8"));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return bytes;
    }

    /**
     * 二进制转十六进制字符串
     *
     * @param bytes 字节数组
     *              十六进制字符串
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
