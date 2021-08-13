package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ChestMenu;

public class ContainerScreen extends AbstractContainerScreen<ChestMenu> implements MenuAccess<ChestMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND;
    private final int containerRows;
    
    public ContainerScreen(final ChestMenu big, final Inventory bfs, final Component nr) {
        super(big, bfs, nr);
        this.passEvents = false;
        final int integer5 = 222;
        final int integer6 = 114;
        this.containerRows = big.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
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
        this.minecraft.getTextureManager().bind(ContainerScreen.CONTAINER_BACKGROUND);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        this.blit(dfj, integer5, integer6 + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
    
    static {
        CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");
    }
}
