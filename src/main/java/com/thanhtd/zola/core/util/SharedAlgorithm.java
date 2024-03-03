package com.thanhtd.zola.core.util;

import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharedAlgorithm {
    public static boolean emailValidate(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 2);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (ObjectUtils.isEmpty(str)) return false;
        return pattern.matcher(str).matches();
    }

    public static String buildIdsStr(List<Long> ids) {
        StringJoiner str = new StringJoiner("-");
        for (Long id: ids) {
            str.add(String.valueOf(id));
        }
        return str.toString();
    }

    public static List<Long> parseIdFromStr(String str) {
        List<Long> ids = new ArrayList<>();
        for (String id: str.split("-")) {
            if (!isNumeric(id)) continue;
            ids.add(Long.parseLong(id));
        }
        return ids;
    }
}
