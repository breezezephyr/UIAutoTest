package com.auto.test.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实现描述：文本处理工具
 */
public abstract class Texts {

    public static String extract(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);
        if (matcher.find())
            return matcher.groupCount() > 0 ? matcher.group(1) : matcher.group();
        return null;
    }

}
