package net.minecraft.advancements;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public enum FrameType {
    TASK("task", 0, ChatFormatting.GREEN), 
    CHALLENGE("challenge", 26, ChatFormatting.DARK_PURPLE), 
    GOAL("goal", 52, ChatFormatting.GREEN);
    
    private final String name;
    private final int texture;
    private final ChatFormatting chatColor;
    private final Component displayName;
    
    private FrameType(final String string3, final int integer4, final ChatFormatting k) {
        this.name = string3;
        this.texture = integer4;
        this.chatColor = k;
        this.displayName = new TranslatableComponent("advancements.toast." + string3);
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getTexture() {
        return this.texture;
    }
    
    public static FrameType byName(final String string) {
        for (final FrameType ai5 : values()) {
            if (ai5.name.equals(string)) {
                return ai5;
            }
        }
        throw new IllegalArgumentException("Unknown frame type '" + string + "'");
    }
    
    public ChatFormatting getChatColor() {
        return this.chatColor;
    }
    
    public Component getDisplayName() {
        return this.displayName;
    }
}
