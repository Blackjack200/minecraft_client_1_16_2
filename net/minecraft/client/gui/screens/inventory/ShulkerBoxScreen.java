package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ShulkerBoxMenu;

public class ShulkerBoxScreen extends AbstractContainerScreen<ShulkerBoxMenu> {
    private static final ResourceLocation CONTAINER_TEXTURE;
    
    public ShulkerBoxScreen(final ShulkerBoxMenu bjl, final Inventory bfs, final Component nr) {
        super(bjl, bfs, nr);
        ++this.imageHeight;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(ShulkerBoxScreen.CONTAINER_TEXTURE);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    static {
        CONTAINER_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
    }
}
