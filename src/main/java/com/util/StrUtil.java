package com.util;

public class StrUtil {
    public static boolean IsNullOrEmpty(String s) {
        if(s==null || "".equals(s))
            return true;
        return false;
    }

    public static String getSizeStr(float size) {
        if (size >= 1099511627776L) {
            return String.format("%.2f", size / 1024 / 1024 / 1024 / 1024) + " P";
        } else if (size >= 1073741824) {
            return String.format("%.2f", size / 1024 / 1024 / 1024) + " G";
        } else if (size >= 1048576) {
            return String.format("%.2f", size / 1024 / 1024) + " M";
        } else if (size > 1024) {
            return String.format("%.2f", size / 1024) + " K";
        } else {
            return size + " Byte";
        }
    }
}
