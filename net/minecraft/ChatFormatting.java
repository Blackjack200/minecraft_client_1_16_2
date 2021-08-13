package net.minecraft;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Locale;
import javax.annotation.Nullable;
import java.util.regex.Pattern;
import java.util.Map;

public enum ChatFormatting {
    BLACK("BLACK", '0', 0, Integer.valueOf(0)), 
    DARK_BLUE("DARK_BLUE", '1', 1, Integer.valueOf(170)), 
    DARK_GREEN("DARK_GREEN", '2', 2, Integer.valueOf(43520)), 
    DARK_AQUA("DARK_AQUA", '3', 3, Integer.valueOf(43690)), 
    DARK_RED("DARK_RED", '4', 4, Integer.valueOf(11141120)), 
    DARK_PURPLE("DARK_PURPLE", '5', 5, Integer.valueOf(11141290)), 
    GOLD("GOLD", '6', 6, Integer.valueOf(16755200)), 
    GRAY("GRAY", '7', 7, Integer.valueOf(11184810)), 
    DARK_GRAY("DARK_GRAY", '8', 8, Integer.valueOf(5592405)), 
    BLUE("BLUE", '9', 9, Integer.valueOf(5592575)), 
    GREEN("GREEN", 'a', 10, Integer.valueOf(5635925)), 
    AQUA("AQUA", 'b', 11, Integer.valueOf(5636095)), 
    RED("RED", 'c', 12, Integer.valueOf(16733525)), 
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, Integer.valueOf(16733695)), 
    YELLOW("YELLOW", 'e', 14, Integer.valueOf(16777045)), 
    WHITE("WHITE", 'f', 15, Integer.valueOf(16777215)), 
    OBFUSCATED("OBFUSCATED", 'k', true), 
    BOLD("BOLD", 'l', true), 
    STRIKETHROUGH("STRIKETHROUGH", 'm', true), 
    UNDERLINE("UNDERLINE", 'n', true), 
    ITALIC("ITALIC", 'o', true), 
    RESET("RESET", 'r', -1, (Integer)null);
    
    private static final Map<String, ChatFormatting> FORMATTING_BY_NAME;
    private static final Pattern STRIP_FORMATTING_PATTERN;
    private final String name;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private final int id;
    @Nullable
    private final Integer color;
    
    private static String cleanName(final String string) {
        return string.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }
    
    private ChatFormatting(final String string3, final char character, final int integer5, @Nullable final Integer integer) {
        this(string3, character, false, integer5, integer);
    }
    
    private ChatFormatting(final String string3, final char character, final boolean boolean5) {
        this(string3, character, boolean5, -1, null);
    }
    
    private ChatFormatting(final String string3, final char character, final boolean boolean5, final int integer6, @Nullable final Integer integer) {
        this.name = string3;
        this.code = character;
        this.isFormat = boolean5;
        this.id = integer6;
        this.color = integer;
        this.toString = new StringBuilder().append("§").append(character).toString();
    }
    
    public int getId() {
        return this.id;
    }
    
    public boolean isFormat() {
        return this.isFormat;
    }
    
    public boolean isColor() {
        return !this.isFormat && this != ChatFormatting.RESET;
    }
    
    @Nullable
    public Integer getColor() {
        return this.color;
    }
    
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
    
    public String toString() {
        return this.toString;
    }
    
    @Nullable
    public static String stripFormatting(@Nullable final String string) {
        return (string == null) ? null : ChatFormatting.STRIP_FORMATTING_PATTERN.matcher((CharSequence)string).replaceAll("");
    }
    
    @Nullable
    public static ChatFormatting getByName(@Nullable final String string) {
        if (string == null) {
            return null;
        }
        return (ChatFormatting)ChatFormatting.FORMATTING_BY_NAME.get(cleanName(string));
    }
    
    @Nullable
    public static ChatFormatting getById(final int integer) {
        if (integer < 0) {
            return ChatFormatting.RESET;
        }
        for (final ChatFormatting k5 : values()) {
            if (k5.getId() == integer) {
                return k5;
            }
        }
        return null;
    }
    
    @Nullable
    public static ChatFormatting getByCode(final char character) {
        final char character2 = Character.toString(character).toLowerCase(Locale.ROOT).charAt(0);
        for (final ChatFormatting k6 : values()) {
            if (k6.code == character2) {
                return k6;
            }
        }
        return null;
    }
    
    public static Collection<String> getNames(final boolean boolean1, final boolean boolean2) {
        final List<String> list3 = (List<String>)Lists.newArrayList();
        for (final ChatFormatting k7 : values()) {
            if (!k7.isColor() || boolean1) {
                if (!k7.isFormat() || boolean2) {
                    list3.add(k7.getName());
                }
            }
        }
        return (Collection<String>)list3;
    }
    
    static {
        FORMATTING_BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(k -> cleanName(k.name), k -> k));
        STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    }
}
