package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Checkbox extends AbstractButton {
    private static final ResourceLocation TEXTURE;
    private boolean selected;
    private final boolean showLabel;
    
    public Checkbox(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr, final boolean boolean6) {
        this(integer1, integer2, integer3, integer4, nr, boolean6, true);
    }
    
    public Checkbox(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr, final boolean boolean6, final boolean boolean7) {
        super(integer1, integer2, integer3, integer4, nr);
        this.selected = boolean6;
        this.showLabel = boolean7;
    }
    
    @Override
    public void onPress() {
        this.selected = !this.selected;
    }
    
    public boolean selected() {
        return this.selected;
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final Minecraft djw6 = Minecraft.getInstance();
        djw6.getTextureManager().bind(Checkbox.TEXTURE);
        RenderSystem.enableDepthTest();
        final Font dkr7 = djw6.font;
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GuiComponent.blit(dfj, this.x, this.y, this.isFocused() ? 20.0f : 0.0f, this.selected ? 20.0f : 0.0f, 20, this.height, 64, 64);
        this.renderBg(dfj, djw6, integer2, integer3);
        if (this.showLabel) {
            GuiComponent.drawString(dfj, dkr7, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 0xE0E0E0 | Mth.ceil(this.alpha * 255.0f) << 24);
        }
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/gui/checkbox.png");
    }
}
