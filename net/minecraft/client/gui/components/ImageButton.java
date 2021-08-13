package net.minecraft.client.gui.components;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class ImageButton extends Button {
    private final ResourceLocation resourceLocation;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;
    
    public ImageButton(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final ResourceLocation vk, final OnPress a) {
        this(integer1, integer2, integer3, integer4, integer5, integer6, integer7, vk, 256, 256, a);
    }
    
    public ImageButton(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final ResourceLocation vk, final int integer9, final int integer10, final OnPress a) {
        this(integer1, integer2, integer3, integer4, integer5, integer6, integer7, vk, integer9, integer10, a, TextComponent.EMPTY);
    }
    
    public ImageButton(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final ResourceLocation vk, final int integer9, final int integer10, final OnPress a, final Component nr) {
        super(integer1, integer2, integer3, integer4, nr, a);
        this.textureWidth = integer9;
        this.textureHeight = integer10;
        this.xTexStart = integer5;
        this.yTexStart = integer6;
        this.yDiffTex = integer7;
        this.resourceLocation = vk;
    }
    
    public void setPosition(final int integer1, final int integer2) {
        this.x = integer1;
        this.y = integer2;
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final Minecraft djw6 = Minecraft.getInstance();
        djw6.getTextureManager().bind(this.resourceLocation);
        int integer4 = this.yTexStart;
        if (this.isHovered()) {
            integer4 += this.yDiffTex;
        }
        RenderSystem.enableDepthTest();
        GuiComponent.blit(dfj, this.x, this.y, (float)this.xTexStart, (float)integer4, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
