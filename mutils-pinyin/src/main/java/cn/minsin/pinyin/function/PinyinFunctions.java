package cn.minsin.pinyin.function;

import cn.minsin.core.exception.MutilsException;
import cn.minsin.core.tools.StringUtil;
import cn.minsin.pinyin.model.PinyinResult;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音帮助类
 *
 * @author minton.zhang
 * @date 2019年5月10日
 */
public class PinyinFunctions {

    /**
     * 去汉字第一个字符 但是会有多音字取错的情况
     *
     * @param chinese
     * @param type
     */
    public static String getFirstLetter(String chinese, HanyuPinyinCaseType type, String defaultValue) {
        MutilsException.throwException(StringUtil.isBlank(chinese), "汉字不能为空");
        char[] cl_chars = chinese.trim().toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setCaseType(type == null ? HanyuPinyinCaseType.LOWERCASE : type);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches("[\u4e00-\u9fa5]+")) {
                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                return PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
            } else if (str.matches("[a-zA-Z]+")) {
                // 如果字符是数字,取数字
                return String.valueOf(cl_chars[0]);
                // 如果字符是字母,取字母
            } else if (str.matches("[0-9]+")) {
                return defaultValue;
            } else {
                return defaultValue;
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new MutilsException(e);
        }

    }

    /**
     * 解析汉字成拼音
     *
     * @param chinese
     */
    public static PinyinResult parseChineseStr(String chinese, HanyuPinyinOutputFormat format) {
        chinese = StringUtil.pickUpChinese(chinese);
        MutilsException.throwException(StringUtil.isBlank(chinese), "汉字不能为空");
        char[] nameChar = chinese.toCharArray();
        PinyinResult pinyinResult = new PinyinResult(nameChar.length, chinese);
        if (format == null) {
            format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        }

        // 取得当前汉字的所有全拼
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], format);
                    if (strs != null) {
                        pinyinResult.setMultiLetter(i, strs);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }
        }
        pinyinResult.resolve();
        return pinyinResult;
    }
}
