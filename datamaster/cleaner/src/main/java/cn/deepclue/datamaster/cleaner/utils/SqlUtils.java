package cn.deepclue.datamaster.cleaner.utils;

import org.apache.commons.lang3.StringUtils;

public class SqlUtils {
    /**
     * like转义
     *
     * @param str   传入字符串
     * @param start 是否左模糊
     * @param end   是否右模糊
     * @return 转义结果
     */
    public static String likeEscape(String str, boolean start, boolean end) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 拼接顺序不能改变
        if (start) {
            sb.append("%");
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '_':
                case '%':
                    sb.append("\\");
                    break;
                case '\\':
                    sb.append("\\\\\\");
                    break;
            }
            sb.append(c);
        }

        if (end) {
            sb.append("%");
        }
        return sb.toString();
    }
}
