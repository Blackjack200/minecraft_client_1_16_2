package com.mojang.realmsclient.gui;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;

public interface ErrorCallback {
    void error(final Component nr);
    
    default void error(final String string) {
        this.error(new TextComponent(string));
    }
}
