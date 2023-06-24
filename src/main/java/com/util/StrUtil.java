package com.util;

public class StrUtil {
    public static boolean IsNullOrEmpty(String s) {
        if(s==null || "".equals(s))
            return true;
        return false;
    }
}
