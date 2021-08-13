package net.minecraft.realms;

import java.time.Duration;
import java.util.Arrays;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.ChatType;
import net.minecraft.client.gui.chat.NarratorChatListener;

public class NarrationHelper {
    private static final RepeatedNarrator REPEATED_NARRATOR;
    
    public static void now(final String string) {
        final NarratorChatListener dkw2 = NarratorChatListener.INSTANCE;
        dkw2.clear();
        dkw2.handle(ChatType.SYSTEM, new TextComponent(fixNarrationNewlines(string)), Util.NIL_UUID);
    }
    
    private static String fixNarrationNewlines(final String string) {
        return string.replace("\\n", (CharSequence)System.lineSeparator());
    }
    
    public static void now(final String... arr) {
        now((Iterable<String>)Arrays.asList((Object[])arr));
    }
    
    public static void now(final Iterable<String> iterable) {
        now(join(iterable));
    }
    
    public static String join(final Iterable<String> iterable) {
        return String.join((CharSequence)System.lineSeparator(), (Iterable)iterable);
    }
    
    public static void repeatedly(final String string) {
        NarrationHelper.REPEATED_NARRATOR.narrate(fixNarrationNewlines(string));
    }
    
    static {
        REPEATED_NARRATOR = new RepeatedNarrator(Duration.ofSeconds(5L));
    }
}
