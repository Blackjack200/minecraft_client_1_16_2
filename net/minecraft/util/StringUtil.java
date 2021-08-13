package net.minecraft.util;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class StringUtil {
    private static final Pattern STRIP_COLOR_PATTERN;
    
    public static String formatTickDuration(final int integer) {
        int integer2 = integer / 20;
        final int integer3 = integer2 / 60;
        integer2 %= 60;
        if (integer2 < 10) {
            return new StringBuilder().append(integer3).append(":0").append(integer2).toString();
        }
        return new StringBuilder().append(integer3).append(":").append(integer2).toString();
    }
    
    public static String stripColor(final String string) {
        return StringUtil.STRIP_COLOR_PATTERN.matcher((CharSequence)string).replaceAll("");
    }
    
    public static boolean isNullOrEmpty(@Nullable final String string) {
        return StringUtils.isEmpty((CharSequence)string);
    }
    
    static {
        STRIP_COLOR_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    }
}
