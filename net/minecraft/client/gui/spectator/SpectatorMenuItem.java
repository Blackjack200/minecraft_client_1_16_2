package net.minecraft.client.gui.spectator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuItem {
    void selectItem(final SpectatorMenu dsi);
    
    Component getName();
    
    void renderIcon(final PoseStack dfj, final float float2, final int integer);
    
    boolean isEnabled();
}
