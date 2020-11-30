package com.abchina.autotest.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 参数生成Service
 * by llh
 */
@Component
public class ParaGenerate {
    /**
     * 密码类型枚举
     */
    public static enum TYPE {
        /**
         ** 字符型
         */
        LETTER,
        /**
         ** 大写字符型 
         */
        CAPITAL,
        /**
         ** 数字型 
         */
        NUMBER,
        /**
         ** 符号型 
         */
        SIGN,
        /**
         ** 大+小字符 型 
         */
        LETTER_CAPITAL,
        /**
         ** 小字符+数字 型  
         */
        LETTER_NUMBER,
        /**
         ** 大+小字符+数字 型 
         */
        LETTER_CAPITAL_NUMBER,
        /**
         ** 大+小字符+数字+符号 型
         */
        LETTER_CAPITAL_NUMBER_SIGN,
        /**
         * 汉字
         */
        CHINESE
    }

    private static String[] lowercase = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private static String[] capital = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private static String[] number = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    private static String[] numberNoZero = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private static String[] sign = {
            "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "`", "-", "=",
            "{", "}", "|", ":", "\"", "<", ">", "?",
            "[", "]", "\\", ";", "'", ",", ".", "/"};

    /**
     ** 静态随机数
     */
    private static Random random = new Random();

    /** 获取随机字符串
  * @param num 位数
  * @param type 类型
  * @type 
     * 字符型 LETTER,
  * 大写字符型 CAPITAL,
  * 数字型 NUMBER,
  * 符号型 SIGN,
  * 大+小字符型 LETTER_CAPITAL,
  * 小字符+数字 型 LETTER_NUMBER,
     * 大+小字符+数字 型 LETTER_CAPITAL_NUMBER,
     * 大+小字符+数字+符号 型 LETTER_CAPITAL_NUMBER_SIGN
     * 汉字 型 CHINESE
  */
    public String getRandomString(int num, TYPE type) {
        ArrayList<String> temp = new ArrayList<String>();
        StringBuffer code = new StringBuffer();
        if (type == TYPE.LETTER) {
            temp.addAll(Arrays.asList(lowercase));
        } else if (type == TYPE.CAPITAL) {
            temp.addAll(Arrays.asList(capital));
        } else if (type == TYPE.NUMBER) {
            temp.addAll(Arrays.asList(number));
        } else if (type == TYPE.SIGN) {
            temp.addAll(Arrays.asList(sign));
        } else if (type == TYPE.LETTER_CAPITAL) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(capital));
        } else if (type == TYPE.LETTER_NUMBER) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(number));
        } else if (type == TYPE.LETTER_CAPITAL_NUMBER) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(capital));
            temp.addAll(Arrays.asList(number));
        } else if (type == TYPE.LETTER_CAPITAL_NUMBER_SIGN) {
            temp.addAll(Arrays.asList(lowercase));
            temp.addAll(Arrays.asList(capital));
            temp.addAll(Arrays.asList(number));
            temp.addAll(Arrays.asList(sign));
        } else if (TYPE.CHINESE.equals(type)){
            for (int i = 0; i < num; i++) {
                code.append(new String(new char[]{(char) (new Random().nextInt(20902) + 19968)}));
            }
            return code.toString();
        }
        for (int i = 0; i < num; i++) {
            code.append(temp.get(random.nextInt(temp.size())));
        }
        return code.toString();
    }

    /**
     * 获取数字样例
     * inte 整数位位数
     * deci 小数位位数
     * plusOrMinus 正负标识 1正数 0负数
     * @return
     */
    public BigDecimal getRandomNumber(int inte, int deci, int plusOrMinus) {
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> tempNoZero = new ArrayList<String>();
        temp.addAll(Arrays.asList(number));
        tempNoZero.addAll(Arrays.asList(numberNoZero));
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 1; i < inte; i++) {
            result = result.add(BigDecimal.valueOf(Integer.valueOf(temp.get(random.nextInt(temp.size()))) * Math.pow(10,
                    i - 1)));
        }
        result = result.add(BigDecimal.valueOf(Integer.valueOf(tempNoZero.get(random.nextInt(tempNoZero.size()))) * Math.pow(10,
                inte - 1)));
        for (int i = 1; i < deci; i++) {
            result = result.add(BigDecimal.valueOf(Integer.valueOf(temp.get(random.nextInt(temp.size()))) * Math.pow(10,
                    -i)));
        }
        result = result.add(BigDecimal.valueOf(Integer.valueOf(tempNoZero.get(random.nextInt(tempNoZero.size()))) * Math.pow(10,
                -inte)));
        if (plusOrMinus == 1) {
            return result;
        } else {
            return result.negate();
        }
    }
}
