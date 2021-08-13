package net.minecraft.util;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import java.util.Optional;

public class StringDecomposer {
    private static final Optional<Object> STOP_ITERATION;
    
    private static boolean feedChar(final Style ob, final FormattedCharSink aey, final int integer, final char character) {
        if (Character.isSurrogate(character)) {
            return aey.accept(integer, ob, 65533);
        }
        return aey.accept(integer, ob, character);
    }
    
    public static boolean iterate(final String string, final Style ob, final FormattedCharSink aey) {
        for (int integer4 = string.length(), integer5 = 0; integer5 < integer4; ++integer5) {
            final char character6 = string.charAt(integer5);
            if (Character.isHighSurrogate(character6)) {
                if (integer5 + 1 >= integer4) {
                    if (!aey.accept(integer5, ob, 65533)) {
                        return false;
                    }
                    break;
                }
                else {
                    final char character7 = string.charAt(integer5 + 1);
                    if (Character.isLowSurrogate(character7)) {
                        if (!aey.accept(integer5, ob, Character.toCodePoint(character6, character7))) {
                            return false;
                        }
                        ++integer5;
                    }
                    else if (!aey.accept(integer5, ob, 65533)) {
                        return false;
                    }
                }
            }
            else if (!feedChar(ob, aey, integer5, character6)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean iterateBackwards(final String string, final Style ob, final FormattedCharSink aey) {
        final int integer4 = string.length();
        for (int integer5 = integer4 - 1; integer5 >= 0; --integer5) {
            final char character6 = string.charAt(integer5);
            if (Character.isLowSurrogate(character6)) {
                if (integer5 - 1 < 0) {
                    if (!aey.accept(0, ob, 65533)) {
                        return false;
                    }
                    break;
                }
                else {
                    final char character7 = string.charAt(integer5 - 1);
                    if (Character.isHighSurrogate(character7)) {
                        --integer5;
                        if (!aey.accept(integer5, ob, Character.toCodePoint(character7, character6))) {
                            return false;
                        }
                    }
                    else if (!aey.accept(integer5, ob, 65533)) {
                        return false;
                    }
                }
            }
            else if (!feedChar(ob, aey, integer5, character6)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean iterateFormatted(final String string, final Style ob, final FormattedCharSink aey) {
        return iterateFormatted(string, 0, ob, aey);
    }
    
    public static boolean iterateFormatted(final String string, final int integer, final Style ob, final FormattedCharSink aey) {
        return iterateFormatted(string, integer, ob, ob, aey);
    }
    
    public static boolean iterateFormatted(final String string, final int integer, final Style ob3, final Style ob4, final FormattedCharSink aey) {
        final int integer2 = string.length();
        Style ob5 = ob3;
        for (int integer3 = integer; integer3 < integer2; ++integer3) {
            final char character9 = string.charAt(integer3);
            if (character9 == '§') {
                if (integer3 + 1 >= integer2) {
                    break;
                }
                final char character10 = string.charAt(integer3 + 1);
                final ChatFormatting k11 = ChatFormatting.getByCode(character10);
                if (k11 != null) {
                    ob5 = ((k11 == ChatFormatting.RESET) ? ob4 : ob5.applyLegacyFormat(k11));
                }
                ++integer3;
            }
            else if (Character.isHighSurrogate(character9)) {
                if (integer3 + 1 >= integer2) {
                    if (!aey.accept(integer3, ob5, 65533)) {
                        return false;
                    }
                    break;
                }
                else {
                    final char character10 = string.charAt(integer3 + 1);
                    if (Character.isLowSurrogate(character10)) {
                        if (!aey.accept(integer3, ob5, Character.toCodePoint(character9, character10))) {
                            return false;
                        }
                        ++integer3;
                    }
                    else if (!aey.accept(integer3, ob5, 65533)) {
                        return false;
                    }
                }
            }
            else if (!feedChar(ob5, aey, integer3, character9)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean iterateFormatted(final FormattedText nu, final Style ob, final FormattedCharSink aey) {
        return !nu.visit((ob, string) -> iterateFormatted(string, 0, ob, aey) ? Optional.empty() : StringDecomposer.STOP_ITERATION, ob).isPresent();
    }
    
    public static String filterBrokenSurrogates(final String string) {
        final StringBuilder stringBuilder2 = new StringBuilder();
        iterate(string, Style.EMPTY, (integer2, ob, integer4) -> {
            stringBuilder2.appendCodePoint(integer4);
            return true;
        });
        return stringBuilder2.toString();
    }
    
    static {
        STOP_ITERATION = Optional.of(Unit.INSTANCE);
    }
}
