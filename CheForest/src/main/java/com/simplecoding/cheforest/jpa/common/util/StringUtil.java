package com.simplecoding.cheforest.jpa.common.util;
/*
    재료 처리목적으로 만든 클래스
 */
import java.util.List;

public class StringUtil {
    public static String joinList(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }

    public static List<String> splitToList(String str) {
        if (str == null || str.isBlank()) return List.of();
        return List.of(str.split(","));
    }
}
