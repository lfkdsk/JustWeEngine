package com.lfk.justweengine.Utils.tools;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 正则表达式处理类
 *
 * @author liufengkai
 */
public class ValidatorsUtils {

    /**
     * 简体中文的正则表达式。
     */
    private static final String REGEX_SIMPLE_CHINESE = "^[\u4E00-\u9FA5]+$";

    /**
     * 字母数字的正则表达式。
     */
    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]+";

    /**
     * 移动手机号码的正则表达式。
     */
    private static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";

    /**
     * 联通手机号码的正则表达式。
     */
    private static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|8[56])\\d{8}";

    /**
     * 电信手机号码的正则表达式。
     */
    private static final String REGEX_CHINA_TELECOM = "(?!00|015|013)(0\\d{9,11})|(1(33|53|80|89)\\d{8})";

    /**
     * 整数或浮点数的正则表达式。
     */
    private static final String REGEX_NUMERIC = "(\\+|-){0,1}(\\d+)([.]?)(\\d*)";

    /**
     * 身份证号码的正则表达式。
     */
    private static final String REGEX_ID_CARD = "(\\d{14}|\\d{17})(\\d|x|X)";

    /**
     * 电子邮箱的正则表达式。
     */
    private static final String REGEX_EMAIL = ".+@.+\\.[a-z]+";

    /**
     * 电话号码的正则表达式。
     */
    private static final String REGEX_PHONE_NUMBER = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";

    /**
     * 判断字符串是否只包含字母和数字.
     *
     * @param str 字符串
     * @return 如果字符串只包含字母和数字, 则返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isAlphanumeric(String str) {
        return isRegexMatch(str, REGEX_ALPHANUMERIC);
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * <p>
     * <pre>
     *   StringUtils.isBlank(null)                = true
     *   StringUtils.isBlank(&quot;&quot;)        = true
     *   StringUtils.isBlank(&quot; &quot;)       = true
     *   StringUtils.isBlank(&quot;bob&quot;)     = false
     *   StringUtils.isBlank(&quot;  bob  &quot;) = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为中国移动手机号码。
     *
     * @param str 字符串
     * @return 如果是移动号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaMobile(String str) {
        return isRegexMatch(str, REGEX_CHINA_MOBILE);
    }

    /**
     * 是否为中国联通手机号码。
     *
     * @param str 字符串
     * @return 如果是联通号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaUnicom(String str) {
        return isRegexMatch(str, REGEX_CHINA_UNICOM);
    }

    /**
     * 判断是否为电信手机。
     *
     * @param str 字符串
     * @return 如果是电信号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaTelecom(String str) {
        return isRegexMatch(str, REGEX_CHINA_TELECOM);
    }

    /**
     * 判断是否为小灵通手机(Personal Access Phone System, PAS)。
     *
     * @param str 字符串
     * @return 如果是小灵通号码，返回 <code>true</code>，否则返回 <code>false</code>。
     * @deprecated 已经被 {@link #isChinaTelecom(String)} 取代
     */
    @Deprecated
    public static boolean isChinaPAS(String str) {
        return isChinaTelecom(str);
    }


    /**
     * 判断字符串是否是合法的电子邮箱地址.
     *
     * @param str 字符串
     * @return 是true，否则false
     */
    public static boolean isEmail(String str) {
        return isRegexMatch(str, REGEX_EMAIL);
    }

    /**
     * 当数组为<code>null</code>, 或者长度为0, 或者长度为1且元素的值为<code>null</code>时返回 <code>true</code>.
     *
     * @param args
     * @return true/false
     */
    public static boolean isEmpty(Object[] args) {
        return args == null || args.length == 0 || (args.length == 1 && args[0] == null);
    }

    /**
     * 字符串是否为Empty，null和空格都算是Empty
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断集合是否为空。
     *
     * @param <T>        集合泛型
     * @param collection 集合对象
     * @return 当集合对象为 <code>null</code> 或者长度为零时返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * <p>
     * Validating for ID card number.
     * </p>
     *
     * @param str string to be validated
     * @return If the str is valid ID card number return <code>true</code>, otherwise return <code>false</code>.
     */
    public static boolean isIdCardNumber(String str) {
        // 15位或18数字, 14数字加x(X)字符或17数字加x(X)字符才是合法的
        return isRegexMatch(str, REGEX_ID_CARD);
    }

    /**
     * 是否为手机号码, 包括移动, 联通, 小灵通等手机号码.
     *
     * @param str 字符串
     * @return 若是合法的手机号码返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isMobile(String str) {
        return isChinaMobile(str) || isChinaUnicom(str) || isChinaPAS(str);
    }

    /**
     * 是否为数字的字符串。
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > '9' || str.charAt(i) < '0') {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是固定范围内的数字的字符串
     *
     * @param str
     * @param min
     * @param max
     * @return true/false
     */
    public static boolean isNumber(String str, int min, int max) {
        if (!isNumber(str)) {
            return false;
        }

        int number = Integer.parseInt(str);
        return number >= min && number <= max;
    }

    /**
     * 判断字符是否为整数或浮点数. <br>
     *
     * @param str 字符串
     * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
     */
    public static boolean isNumeric(String str) {
        return isRegexMatch(str, REGEX_NUMERIC);
    }

    /**
     * 判断字符是否为符合精度要求的整数或浮点数。
     *
     * @param str         字符串
     * @param fractionNum 小数部分的最多允许的位数
     * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
     */
    public static boolean isNumeric(String str, int fractionNum) {
        if (isEmpty(str)) {
            return false;
        }

        // 整数或浮点数
        String regex = "(\\+|-)?(\\d+)([.]?)(\\d{0," + fractionNum + "})";
        return Pattern.matches(regex, str);
    }

    /**
     * <p>
     * Validating for phone number.
     * </p>
     * <p>
     * e.g. <li>78674585 --> valid</li> <li>6872-4585 --> valid</li> <li>(6872)4585 --> valid</li> <li>0086-10-6872-4585
     * --> valid</li> <li>0086-(10)-6872-4585 --> invalid</li> <li>0086(10)68724585 --> invalid</li>
     *
     * @param str string to be validated
     * @return If the str is valid phone number return <code>true</code>, otherwise return <code>false</code>.
     */
    public static boolean isPhoneNumber(String str) {
        // Regex for checking phone number
        return isRegexMatch(str, REGEX_PHONE_NUMBER);
    }

    /**
     * 判断是否是合法的邮编
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isPostcode(String str) {
        if (isEmpty(str)) {
            return false;
        }

        if (str.length() != 6 || !isNumber(str)) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否是固定长度范围内的字符串
     *
     * @param str
     * @param minLength
     * @param maxLength
     * @return true/false
     */
    public static boolean isString(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }

        if (minLength < 0) {
            return str.length() <= maxLength;
        } else if (maxLength < 0) {
            return str.length() >= minLength;
        } else {
            return str.length() >= minLength && str.length() <= maxLength;
        }
    }

    /**
     * 判断是否是合法的时间字符串。
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isTime(String str) {
        if (isEmpty(str) || str.length() > 8) {
            return false;
        }

        String[] items = str.split(":");

        if (items.length != 2 && items.length != 3) {
            return false;
        }

        for (int i = 0; i < items.length; i++) {
            if (items[i].length() != 2 && items[i].length() != 1) {
                return false;
            }
        }

        return !(!isNumber(items[0], 0, 23) || !isNumber(items[1], 0, 59) || (items.length == 3 && !isNumber(items[2],
                0, 59)));
    }

    /**
     * 是否是简体中文字符串。
     *
     * @param str 字符串
     * @return true/false
     */
    public static boolean isSimpleChinese(String str) {
        return isRegexMatch(str, REGEX_SIMPLE_CHINESE);
    }

    /**
     * 判断字符串是否匹配了正则表达式。
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return true/false
     */
    public static boolean isRegexMatch(String str, String regex) {
        return str != null && str.matches(regex);
    }

}
