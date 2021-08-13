package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.GrindstoneMenu;

public class GrindstoneScreen extends AbstractContainerScreen<GrindstoneMenu> {
    private static final ResourceLocation GRINDSTONE_LOCATION;
    
    public GrindstoneScreen(final GrindstoneMenu bit, final Inventory bfs, final Component nr) {
        super(bit, bfs, nr);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.renderBg(dfj, float4, integer2, integer3);
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(GrindstoneScreen.GRINDSTONE_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        if ((((GrindstoneMenu)this.menu).getSlot(0).hasItem() || ((GrindstoneMenu)this.menu).getSlot(1).hasItem()) && !((GrindstoneMenu)this.menu).getSlot(2).hasItem()) {
            this.blit(dfj, integer5 + 92, integer6 + 31, this.imageWidth, 0, 28, 21);
        }
    }
    
    static {
        GRINDSTONE_LOCATION = new ResourceLocation("textures/gui/container/grindstone.png");
    }
}
