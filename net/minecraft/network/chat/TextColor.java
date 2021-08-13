package net.minecraft.network.chat;

import com.google.common.collect.ImmutableMap;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import java.util.Map;

public final class TextColor {
    private static final Map<ChatFormatting, TextColor> LEGACY_FORMAT_TO_COLOR;
    private static final Map<String, TextColor> NAMED_COLORS;
    private final int value;
    @Nullable
    private final String name;
    
    private TextColor(final int integer, final String string) {
        this.value = integer;
        this.name = string;
    }
    
    private TextColor(final int integer) {
        this.value = integer;
        this.name = null;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public String serialize() {
        if (this.name != null) {
            return this.name;
        }
        return this.formatValue();
    }
    
    private String formatValue() {
        return String.format("#%06X", new Object[] { this.value });
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final TextColor od3 = (TextColor)object;
        return this.value == od3.value;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.value, this.name });
    }
    
    public String toString() {
        return (this.name != null) ? this.name : this.formatValue();
    }
    
    @Nullable
    public static TextColor fromLegacyFormat(final ChatFormatting k) {
        return (TextColor)TextColor.LEGACY_FORMAT_TO_COLOR.get(k);
    }
    
    public static TextColor fromRgb(final int integer) {
        return new TextColor(integer);
    }
    
    @Nullable
    public static TextColor parseColor(final String string) {
        if (string.startsWith("#")) {
            try {
                final int integer2 = Integer.parseInt(string.substring(1), 16);
                return fromRgb(integer2);
            }
            catch (NumberFormatException numberFormatException2) {
                return null;
            }
        }
        return (TextColor)TextColor.NAMED_COLORS.get(string);
    }
    
    static {
        LEGACY_FORMAT_TO_COLOR = (Map)Stream.of((Object[])ChatFormatting.values()).filter(ChatFormatting::isColor).collect(ImmutableMap.toImmutableMap(Function.identity(), k -> new TextColor(k.getColor(), k.getName())));
        NAMED_COLORS = (Map)TextColor.LEGACY_FORMAT_TO_COLOR.values().stream().collect(ImmutableMap.toImmutableMap(od -> od.name, Function.identity()));
    }
}
