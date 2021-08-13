package net.minecraft.client.gui.screens.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ItemCombinerMenu;

public class ItemCombinerScreen<T extends ItemCombinerMenu> extends AbstractContainerScreen<T> implements ContainerListener {
    private ResourceLocation menuResource;
    
    public ItemCombinerScreen(final T bix, final Inventory bfs, final Component nr, final ResourceLocation vk) {
        super(bix, bfs, nr);
        this.menuResource = vk;
    }
    
    protected void subInit() {
    }
    
    @Override
    protected void init() {
        super.init();
        this.subInit();
        this.menu.addSlotListener(this);
    }
    
    @Override
    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        RenderSystem.disableBlend();
        this.renderFg(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    protected void renderFg(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(this.menuResource);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(dfj, integer5 + 59, integer6 + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem()) {
            this.blit(dfj, integer5 + 99, integer6 + 45, this.imageWidth, 0, 28, 21);
        }
    }
    
    @Override
    public void refreshContainer(final AbstractContainerMenu bhz, final NonNullList<ItemStack> gj) {
        this.slotChanged(bhz, 0, bhz.getSlot(0).getItem());
    }
    
    @Override
    public void setContainerData(final AbstractContainerMenu bhz, final int integer2, final int integer3) {
    }
    
    @Override
    public void slotChanged(final AbstractContainerMenu bhz, final int integer, final ItemStack bly) {
    }
}
