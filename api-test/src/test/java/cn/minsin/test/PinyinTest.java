package cn.minsin.test;

import cn.minsin.pinyin.function.PinyinFunctions;
import cn.minsin.pinyin.model.PinyinResult;

/**
 * @author: minton.zhang
 * @since: 2020/4/1 19:42
 */
public class PinyinTest {

    public static void main(String[] args) {
        PinyinResult test = PinyinFunctions.parseChineseStr("账号", null);
        System.out.println(test.getFirstLetter());
    }
}
