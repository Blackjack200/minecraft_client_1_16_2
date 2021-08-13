package net.minecraft.util;

import net.minecraft.network.chat.Style;

@FunctionalInterface
public interface FormattedCharSink {
    boolean accept(final int integer1, final Style ob, final int integer3);
}
