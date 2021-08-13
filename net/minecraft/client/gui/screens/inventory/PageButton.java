package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.Button;

public class PageButton extends Button {
    private final boolean isForward;
    private final boolean playTurnSound;
    
    public PageButton(final int integer1, final int integer2, final boolean boolean3, final OnPress a, final boolean boolean5) {
        super(integer1, integer2, 23, 13, TextComponent.EMPTY, a);
        this.isForward = boolean3;
        this.playTurnSound = boolean5;
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getInstance().getTextureManager().bind(BookViewScreen.BOOK_LOCATION);
        int integer4 = 0;
        int integer5 = 192;
        if (this.isHovered()) {
            integer4 += 23;
        }
        if (!this.isForward) {
            integer5 += 13;
        }
        this.blit(dfj, this.x, this.y, integer4, integer5, 23, 13);
    }
    
    @Override
    public void playDownSound(final SoundManager enm) {
        if (this.playTurnSound) {
            enm.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0f));
        }
    }
}
