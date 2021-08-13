package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class StateSwitchingButton extends AbstractWidget {
    protected ResourceLocation resourceLocation;
    protected boolean isStateTriggered;
    protected int xTexStart;
    protected int yTexStart;
    protected int xDiffTex;
    protected int yDiffTex;
    
    public StateSwitchingButton(final int integer1, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
        super(integer1, integer2, integer3, integer4, TextComponent.EMPTY);
        this.isStateTriggered = boolean5;
    }
    
    public void initTextureValues(final int integer1, final int integer2, final int integer3, final int integer4, final ResourceLocation vk) {
        this.xTexStart = integer1;
        this.yTexStart = integer2;
        this.xDiffTex = integer3;
        this.yDiffTex = integer4;
        this.resourceLocation = vk;
    }
    
    public void setStateTriggered(final boolean boolean1) {
        this.isStateTriggered = boolean1;
    }
    
    public boolean isStateTriggered() {
        return this.isStateTriggered;
    }
    
    public void setPosition(final int integer1, final int integer2) {
        this.x = integer1;
        this.y = integer2;
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final Minecraft djw6 = Minecraft.getInstance();
        djw6.getTextureManager().bind(this.resourceLocation);
        RenderSystem.disableDepthTest();
        int integer4 = this.xTexStart;
        int integer5 = this.yTexStart;
        if (this.isStateTriggered) {
            integer4 += this.xDiffTex;
        }
        if (this.isHovered()) {
            integer5 += this.yDiffTex;
        }
        this.blit(dfj, this.x, this.y, integer4, integer5, this.width, this.height);
        RenderSystem.enableDepthTest();
    }
}
