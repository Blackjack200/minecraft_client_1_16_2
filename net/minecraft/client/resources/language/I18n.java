package net.minecraft.client.resources.language;

import java.util.IllegalFormatException;
import net.minecraft.locale.Language;

public class I18n {
    private static volatile Language language;
    
    static void setLanguage(final Language ly) {
        I18n.language = ly;
    }
    
    public static String get(final String string, final Object... arr) {
        final String string2 = I18n.language.getOrDefault(string);
        try {
            return String.format(string2, arr);
        }
        catch (IllegalFormatException illegalFormatException4) {
            return "Format error: " + string2;
        }
    }
    
    public static boolean exists(final String string) {
        return I18n.language.has(string);
    }
    
    static {
        I18n.language = Language.getInstance();
    }
}
