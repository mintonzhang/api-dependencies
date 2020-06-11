package cn.minsin.test;

import cn.minsin.qrcode.function.QRcodeFunctions;
import cn.minsin.qrcode.model.QrcodeModel;
import com.google.zxing.WriterException;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 19:40
 */
public class QrCodeTest {

    public static void main(String[] args) throws IOException, WriterException {

         QRcodeFunctions.createQRCodeToOutputStream(new QrcodeModel().setContent("!@3123"),new FileOutputStream("D://qrcode.png"));
    }
}
