package net.minecraft.client.gui.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;
import net.minecraft.client.Option;

public class AccessibilityOptionsScreen extends SimpleOptionsSubScreen {
    private static final Option[] OPTIONS;
    
    public AccessibilityOptionsScreen(final Screen doq, final Options dka) {
        super(doq, dka, new TranslatableComponent("options.accessibility.title"), AccessibilityOptionsScreen.OPTIONS);
    }
    
    static {
        OPTIONS = new Option[] { Option.NARRATOR, Option.SHOW_SUBTITLES, Option.TEXT_BACKGROUND_OPACITY, Option.TEXT_BACKGROUND, Option.CHAT_OPACITY, Option.CHAT_LINE_SPACING, Option.CHAT_DELAY, Option.AUTO_JUMP, Option.TOGGLE_CROUCH, Option.TOGGLE_SPRINT, Option.SCREEN_EFFECTS_SCALE, Option.FOV_EFFECTS_SCALE };
    }
}
