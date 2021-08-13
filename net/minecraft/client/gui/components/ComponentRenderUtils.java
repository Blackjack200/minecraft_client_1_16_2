package net.minecraft.client.gui.components;

import net.minecraft.locale.Language;
import java.util.function.BiConsumer;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Style;
import java.util.Optional;
import net.minecraft.client.ComponentCollector;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FormattedCharSequence;

public class ComponentRenderUtils {
    private static final FormattedCharSequence INDENT;
    
    private static String stripColor(final String string) {
        return Minecraft.getInstance().options.chatColors ? string : ChatFormatting.stripFormatting(string);
    }
    
    public static List<FormattedCharSequence> wrapComponents(final FormattedText nu, final int integer, final Font dkr) {
        final ComponentCollector djl4 = new ComponentCollector();
        nu.visit((ob, string) -> {
            djl4.append(FormattedText.of(stripColor(string), ob));
            return (java.util.Optional<Object>)Optional.empty();
        }, Style.EMPTY);
        final List<FormattedCharSequence> list5 = (List<FormattedCharSequence>)Lists.newArrayList();
        dkr.getSplitter().splitLines(djl4.getResultOrEmpty(), integer, Style.EMPTY, (BiConsumer<FormattedText, Boolean>)((nu, boolean3) -> {
            final FormattedCharSequence aex4 = Language.getInstance().getVisualOrder(nu);
            list5.add((boolean3 ? FormattedCharSequence.composite(ComponentRenderUtils.INDENT, aex4) : aex4));
        }));
        if (list5.isEmpty()) {
            return (List<FormattedCharSequence>)Lists.newArrayList((Object[])new FormattedCharSequence[] { FormattedCharSequence.EMPTY });
        }
        return list5;
    }
    
    static {
        INDENT = FormattedCharSequence.codepoint(32, Style.EMPTY);
    }
}
