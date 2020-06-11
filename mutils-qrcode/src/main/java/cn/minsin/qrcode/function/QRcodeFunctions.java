package cn.minsin.qrcode.function;

import cn.minsin.core.constant.CharSetConstant;
import cn.minsin.core.tools.ModelUtil;
import cn.minsin.qrcode.model.LogoModel;
import cn.minsin.qrcode.model.QrcodeModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码相关功能
 *
 * @author mintonzhang
 * @date 2019年1月22日
 * @since 0.2.8
 */
public class QRcodeFunctions {


    /**
     * 生成二维码图片
     *
     * @param model 创建实体类
     * @throws WriterException
     * @throws IOException
     */
    public static InputStream createQRCodeToInputStream(QrcodeModel model) throws WriterException, IOException {
        BufferedImage createImage = createImage(model);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(createImage, model.getFormat(), os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * 获取二维码的输出流
     *
     * @param model 生成对象
     * @param os    输出对象
     * @throws WriterException
     * @throws IOException
     */
    public static boolean createQRCodeToOutputStream(QrcodeModel model, OutputStream os) throws WriterException, IOException {
        BufferedImage createImage = createImage(model);
        ImageIO.write(createImage, model.getFormat(), os);
        return true;
    }

    protected static BufferedImage createImage(QrcodeModel model) throws WriterException, IOException {
        ModelUtil.verificationField(model);
        int width = model.getWidth(), height = model.getHeight();
        int level = model.getLevel();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, CharSetConstant.UTF_8);
        if (level >= 0 && level <= 5) {
            // 设置白边
            hints.put(EncodeHintType.MARGIN, level);
        }
        // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
        BitMatrix bitMatrix = multiFormatWriter.encode(model.getContent(), BarcodeFormat.QR_CODE, width, height, hints);
        if (level == -1) {
            bitMatrix = deleteWhite(bitMatrix);
        }
        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? MatrixToImageConfig.BLACK : MatrixToImageConfig.WHITE);
            }
        }
        return encode(image, model);
    }

    /**
     * 创建logo
     *
     * @param image
     * @param model
     * @throws IOException
     */
    protected static BufferedImage encode(BufferedImage image, QrcodeModel model) throws IOException {
        LogoModel logoImageModel = model.getLogoImageModel();
        // 不需要添加logo
        if (logoImageModel != null) {
            ModelUtil.verificationField(logoImageModel);

            int height = logoImageModel.getHeight();
            int width = logoImageModel.getWidth();
            // logo所在位置
            int x = (image.getWidth() - height) / 2;
            int y = (image.getHeight() - width) / 2;
            Graphics2D g2 = image.createGraphics();
            BufferedImage logo = ImageIO.read(logoImageModel.getLogo());

            // 开始绘制图片
            g2.drawImage(logo, x, y, width, height, null);
            if (logoImageModel.getIsArc()) {
                BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                // 设置笔画对象
                g2.setStroke(stroke);
                // 指定弧度的圆角矩形
                RoundRectangle2D.Float round = new RoundRectangle2D.Float(x, y, width, height, 20, 20);
                g2.setColor(logoImageModel.getBorderColor());
                // 绘制圆弧矩形
                g2.draw(round);
                // 设置logo 有一道灰色边框
                BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                // 设置笔画对象
                g2.setStroke(stroke2);
                RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(x + 2, y + 2, width - 4, height - 4, 20, 20);
                g2.setColor(new Color(128, 128, 128));
                // 绘制圆弧矩形
                g2.draw(round2);
            }
            g2.dispose();
            logo.flush();
        }
        image.flush();
        return image;
    }

    /**
     * 二维码解码
     *
     * @param input 二维码文件流
     * @throws NotFoundException
     * @throws IOException
     */
    public static String decode(InputStream input) throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(input);
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CharSetConstant.UTF_8);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 移除白边
     *
     * @param matrix
     */
    protected static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2];
        int resHeight = rec[3];

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }
}
