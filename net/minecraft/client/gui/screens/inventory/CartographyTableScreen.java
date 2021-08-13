package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.Tesselator;
import javax.annotation.Nullable;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.Items;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CartographyTableMenu;

public class CartographyTableScreen extends AbstractContainerScreen<CartographyTableMenu> {
    private static final ResourceLocation BG_LOCATION;
    
    public CartographyTableScreen(final CartographyTableMenu bif, final Inventory bfs, final Component nr) {
        super(bif, bfs, nr);
        this.titleLabelY -= 2;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        this.renderBackground(dfj);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(CartographyTableScreen.BG_LOCATION);
        final int integer5 = this.leftPos;
        final int integer6 = this.topPos;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        final Item blu8 = ((CartographyTableMenu)this.menu).getSlot(1).getItem().getItem();
        final boolean boolean9 = blu8 == Items.MAP;
        final boolean boolean10 = blu8 == Items.PAPER;
        final boolean boolean11 = blu8 == Items.GLASS_PANE;
        final ItemStack bly12 = ((CartographyTableMenu)this.menu).getSlot(0).getItem();
        boolean boolean12 = false;
        MapItemSavedData cxu13;
        if (bly12.getItem() == Items.FILLED_MAP) {
            cxu13 = MapItem.getSavedData(bly12, this.minecraft.level);
            if (cxu13 != null) {
                if (cxu13.locked) {
                    boolean12 = true;
                    if (boolean10 || boolean11) {
                        this.blit(dfj, integer5 + 35, integer6 + 31, this.imageWidth + 50, 132, 28, 21);
                    }
                }
                if (boolean10 && cxu13.scale >= 4) {
                    boolean12 = true;
                    this.blit(dfj, integer5 + 35, integer6 + 31, this.imageWidth + 50, 132, 28, 21);
                }
            }
        }
        else {
            cxu13 = null;
        }
        this.renderResultingMap(dfj, cxu13, boolean9, boolean10, boolean11, boolean12);
    }
    
    private void renderResultingMap(final PoseStack dfj, @Nullable final MapItemSavedData cxu, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
        final int integer8 = this.leftPos;
        final int integer9 = this.topPos;
        if (boolean4 && !boolean6) {
            this.blit(dfj, integer8 + 67, integer9 + 13, this.imageWidth, 66, 66, 66);
            this.renderMap(cxu, integer8 + 85, integer9 + 31, 0.226f);
        }
        else if (boolean3) {
            this.blit(dfj, integer8 + 67 + 16, integer9 + 13, this.imageWidth, 132, 50, 66);
            this.renderMap(cxu, integer8 + 86, integer9 + 16, 0.34f);
            this.minecraft.getTextureManager().bind(CartographyTableScreen.BG_LOCATION);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0f, 0.0f, 1.0f);
            this.blit(dfj, integer8 + 67, integer9 + 13 + 16, this.imageWidth, 132, 50, 66);
            this.renderMap(cxu, integer8 + 70, integer9 + 32, 0.34f);
            RenderSystem.popMatrix();
        }
        else if (boolean5) {
            this.blit(dfj, integer8 + 67, integer9 + 13, this.imageWidth, 0, 66, 66);
            this.renderMap(cxu, integer8 + 71, integer9 + 17, 0.45f);
            this.minecraft.getTextureManager().bind(CartographyTableScreen.BG_LOCATION);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0f, 0.0f, 1.0f);
            this.blit(dfj, integer8 + 66, integer9 + 12, 0, this.imageHeight, 66, 66);
            RenderSystem.popMatrix();
        }
        else {
            this.blit(dfj, integer8 + 67, integer9 + 13, this.imageWidth, 0, 66, 66);
            this.renderMap(cxu, integer8 + 71, integer9 + 17, 0.45f);
        }
    }
    
    private void renderMap(@Nullable final MapItemSavedData cxu, final int integer2, final int integer3, final float float4) {
        if (cxu != null) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)integer2, (float)integer3, 1.0f);
            RenderSystem.scalef(float4, float4, 1.0f);
            final MultiBufferSource.BufferSource a6 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            this.minecraft.gameRenderer.getMapRenderer().render(new PoseStack(), a6, cxu, true, 15728880);
            a6.endBatch();
            RenderSystem.popMatrix();
        }
    }
    
    static {
        BG_LOCATION = new ResourceLocation("textures/gui/container/cartography_table.png");
    }
}
