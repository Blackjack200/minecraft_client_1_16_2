package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.HopperMenu;

public class HopperScreen extends AbstractContainerScreen<HopperMenu> {
    private static final ResourceLocation HOPPER_LOCATION;
    
    public HopperScreen(final HopperMenu biu, final Inventory bfs, final Component nr) {
        super(biu, bfs, nr);
        this.passEvents = false;
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
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
        this.minecraft.getTextureManager().bind(HopperScreen.HOPPER_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    static {
        HOPPER_LOCATION = new ResourceLocation("textures/gui/container/hopper.png");
    }
}
