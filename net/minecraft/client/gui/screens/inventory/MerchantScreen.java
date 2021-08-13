package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.item.ItemStack;
import java.util.Iterator;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MerchantMenu;

public class MerchantScreen extends AbstractContainerScreen<MerchantMenu> {
    private static final ResourceLocation VILLAGER_LOCATION;
    private static final Component TRADES_LABEL;
    private static final Component LEVEL_SEPARATOR;
    private static final Component DEPRECATED_TOOLTIP;
    private int shopItem;
    private final TradeOfferButton[] tradeOfferButtons;
    private int scrollOff;
    private boolean isDragging;
    
    public MerchantScreen(final MerchantMenu bjd, final Inventory bfs, final Component nr) {
        super(bjd, bfs, nr);
        this.tradeOfferButtons = new TradeOfferButton[7];
        this.imageWidth = 276;
        this.inventoryLabelX = 107;
    }
    
    private void postButtonClick() {
        ((MerchantMenu)this.menu).setSelectionHint(this.shopItem);
        ((MerchantMenu)this.menu).tryMoveItems(this.shopItem);
        this.minecraft.getConnection().send(new ServerboundSelectTradePacket(this.shopItem));
    }
    
    @Override
    protected void init() {
        super.init();
        final int integer2 = (this.width - this.imageWidth) / 2;
        final int integer3 = (this.height - this.imageHeight) / 2;
        int integer4 = integer3 + 16 + 2;
        for (int integer5 = 0; integer5 < 7; ++integer5) {
            this.tradeOfferButtons[integer5] = this.<TradeOfferButton>addButton(new TradeOfferButton(integer2 + 5, integer4, integer5, dlg -> {
                if (dlg instanceof TradeOfferButton) {
                    this.shopItem = dlg.getIndex() + this.scrollOff;
                    this.postButtonClick();
                }
                return;
            }));
            integer4 += 20;
        }
    }
    
    @Override
    protected void renderLabels(final PoseStack dfj, final int integer2, final int integer3) {
        final int integer4 = ((MerchantMenu)this.menu).getTraderLevel();
        if (integer4 > 0 && integer4 <= 5 && ((MerchantMenu)this.menu).showProgressBar()) {
            final Component nr6 = this.title.copy().append(MerchantScreen.LEVEL_SEPARATOR).append(new TranslatableComponent(new StringBuilder().append("merchant.level.").append(integer4).toString()));
            final int integer5 = this.font.width(nr6);
            final int integer6 = 49 + this.imageWidth / 2 - integer5 / 2;
            this.font.draw(dfj, nr6, (float)integer6, 6.0f, 4210752);
        }
        else {
            this.font.draw(dfj, this.title, (float)(49 + this.imageWidth / 2 - this.font.width(this.title) / 2), 6.0f, 4210752);
        }
        this.font.draw(dfj, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
        final int integer7 = this.font.width(MerchantScreen.TRADES_LABEL);
        this.font.draw(dfj, MerchantScreen.TRADES_LABEL, (float)(5 - integer7 / 2 + 48), 6.0f, 4210752);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(MerchantScreen.VILLAGER_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        GuiComponent.blit(dfj, integer5, integer6, this.getBlitOffset(), 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 512);
        final MerchantOffers bqt8 = ((MerchantMenu)this.menu).getOffers();
        if (!bqt8.isEmpty()) {
            final int integer7 = this.shopItem;
            if (integer7 < 0 || integer7 >= bqt8.size()) {
                return;
            }
            final MerchantOffer bqs10 = (MerchantOffer)bqt8.get(integer7);
            if (bqs10.isOutOfStock()) {
                this.minecraft.getTextureManager().bind(MerchantScreen.VILLAGER_LOCATION);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                GuiComponent.blit(dfj, this.leftPos + 83 + 99, this.topPos + 35, this.getBlitOffset(), 311.0f, 0.0f, 28, 21, 256, 512);
            }
        }
    }
    
    private void renderProgressBar(final PoseStack dfj, final int integer2, final int integer3, final MerchantOffer bqs) {
        this.minecraft.getTextureManager().bind(MerchantScreen.VILLAGER_LOCATION);
        final int integer4 = ((MerchantMenu)this.menu).getTraderLevel();
        final int integer5 = ((MerchantMenu)this.menu).getTraderXp();
        if (integer4 >= 5) {
            return;
        }
        GuiComponent.blit(dfj, integer2 + 136, integer3 + 16, this.getBlitOffset(), 0.0f, 186.0f, 102, 5, 256, 512);
        final int integer6 = VillagerData.getMinXpPerLevel(integer4);
        if (integer5 < integer6 || !VillagerData.canLevelUp(integer4)) {
            return;
        }
        final int integer7 = 100;
        final float float10 = 100.0f / (VillagerData.getMaxXpPerLevel(integer4) - integer6);
        final int integer8 = Math.min(Mth.floor(float10 * (integer5 - integer6)), 100);
        GuiComponent.blit(dfj, integer2 + 136, integer3 + 16, this.getBlitOffset(), 0.0f, 191.0f, integer8 + 1, 5, 256, 512);
        final int integer9 = ((MerchantMenu)this.menu).getFutureTraderXp();
        if (integer9 > 0) {
            final int integer10 = Math.min(Mth.floor(integer9 * float10), 100 - integer8);
            GuiComponent.blit(dfj, integer2 + 136 + integer8 + 1, integer3 + 16 + 1, this.getBlitOffset(), 2.0f, 182.0f, integer10, 3, 256, 512);
        }
    }
    
    private void renderScroller(final PoseStack dfj, final int integer2, final int integer3, final MerchantOffers bqt) {
        final int integer4 = bqt.size() + 1 - 7;
        if (integer4 > 1) {
            final int integer5 = 139 - (27 + (integer4 - 1) * 139 / integer4);
            final int integer6 = 1 + integer5 / integer4 + 139 / integer4;
            final int integer7 = 113;
            int integer8 = Math.min(113, this.scrollOff * integer6);
            if (this.scrollOff == integer4 - 1) {
                integer8 = 113;
            }
            GuiComponent.blit(dfj, integer2 + 94, integer3 + 18 + integer8, this.getBlitOffset(), 0.0f, 199.0f, 6, 27, 256, 512);
        }
        else {
            GuiComponent.blit(dfj, integer2 + 94, integer3 + 18, this.getBlitOffset(), 6.0f, 199.0f, 6, 27, 256, 512);
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        final MerchantOffers bqt6 = ((MerchantMenu)this.menu).getOffers();
        if (!bqt6.isEmpty()) {
            final int integer4 = (this.width - this.imageWidth) / 2;
            final int integer5 = (this.height - this.imageHeight) / 2;
            int integer6 = integer5 + 16 + 1;
            final int integer7 = integer4 + 5 + 5;
            RenderSystem.pushMatrix();
            RenderSystem.enableRescaleNormal();
            this.minecraft.getTextureManager().bind(MerchantScreen.VILLAGER_LOCATION);
            this.renderScroller(dfj, integer4, integer5, bqt6);
            int integer8 = 0;
            for (final MerchantOffer bqs13 : bqt6) {
                if (this.canScroll(bqt6.size()) && (integer8 < this.scrollOff || integer8 >= 7 + this.scrollOff)) {
                    ++integer8;
                }
                else {
                    final ItemStack bly14 = bqs13.getBaseCostA();
                    final ItemStack bly15 = bqs13.getCostA();
                    final ItemStack bly16 = bqs13.getCostB();
                    final ItemStack bly17 = bqs13.getResult();
                    this.itemRenderer.blitOffset = 100.0f;
                    final int integer9 = integer6 + 2;
                    this.renderAndDecorateCostA(dfj, bly15, bly14, integer7, integer9);
                    if (!bly16.isEmpty()) {
                        this.itemRenderer.renderAndDecorateFakeItem(bly16, integer4 + 5 + 35, integer9);
                        this.itemRenderer.renderGuiItemDecorations(this.font, bly16, integer4 + 5 + 35, integer9);
                    }
                    this.renderButtonArrows(dfj, bqs13, integer4, integer9);
                    this.itemRenderer.renderAndDecorateFakeItem(bly17, integer4 + 5 + 68, integer9);
                    this.itemRenderer.renderGuiItemDecorations(this.font, bly17, integer4 + 5 + 68, integer9);
                    this.itemRenderer.blitOffset = 0.0f;
                    integer6 += 20;
                    ++integer8;
                }
            }
            final int integer10 = this.shopItem;
            MerchantOffer bqs13 = (MerchantOffer)bqt6.get(integer10);
            if (((MerchantMenu)this.menu).showProgressBar()) {
                this.renderProgressBar(dfj, integer4, integer5, bqs13);
            }
            if (bqs13.isOutOfStock() && this.isHovering(186, 35, 22, 21, integer2, integer3) && ((MerchantMenu)this.menu).canRestock()) {
                this.renderTooltip(dfj, MerchantScreen.DEPRECATED_TOOLTIP, integer2, integer3);
            }
            for (final TradeOfferButton a17 : this.tradeOfferButtons) {
                if (a17.isHovered()) {
                    a17.renderToolTip(dfj, integer2, integer3);
                }
                a17.visible = (a17.index < ((MerchantMenu)this.menu).getOffers().size());
            }
            RenderSystem.popMatrix();
            RenderSystem.enableDepthTest();
        }
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    private void renderButtonArrows(final PoseStack dfj, final MerchantOffer bqs, final int integer3, final int integer4) {
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(MerchantScreen.VILLAGER_LOCATION);
        if (bqs.isOutOfStock()) {
            GuiComponent.blit(dfj, integer3 + 5 + 35 + 20, integer4 + 3, this.getBlitOffset(), 25.0f, 171.0f, 10, 9, 256, 512);
        }
        else {
            GuiComponent.blit(dfj, integer3 + 5 + 35 + 20, integer4 + 3, this.getBlitOffset(), 15.0f, 171.0f, 10, 9, 256, 512);
        }
    }
    
    private void renderAndDecorateCostA(final PoseStack dfj, final ItemStack bly2, final ItemStack bly3, final int integer4, final int integer5) {
        this.itemRenderer.renderAndDecorateFakeItem(bly2, integer4, integer5);
        if (bly3.getCount() == bly2.getCount()) {
            this.itemRenderer.renderGuiItemDecorations(this.font, bly2, integer4, integer5);
        }
        else {
            this.itemRenderer.renderGuiItemDecorations(this.font, bly3, integer4, integer5, (bly3.getCount() == 1) ? "1" : null);
            this.itemRenderer.renderGuiItemDecorations(this.font, bly2, integer4 + 14, integer5, (bly2.getCount() == 1) ? "1" : null);
            this.minecraft.getTextureManager().bind(MerchantScreen.VILLAGER_LOCATION);
            this.setBlitOffset(this.getBlitOffset() + 300);
            GuiComponent.blit(dfj, integer4 + 7, integer5 + 12, this.getBlitOffset(), 0.0f, 176.0f, 9, 2, 256, 512);
            this.setBlitOffset(this.getBlitOffset() - 300);
        }
    }
    
    private boolean canScroll(final int integer) {
        return integer > 7;
    }
    
    public boolean mouseScrolled(final double double1, final double double2, final double double3) {
        final int integer8 = ((MerchantMenu)this.menu).getOffers().size();
        if (this.canScroll(integer8)) {
            final int integer9 = integer8 - 7;
            this.scrollOff -= (int)double3;
            this.scrollOff = Mth.clamp(this.scrollOff, 0, integer9);
        }
        return true;
    }
    
    @Override
    public boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        final int integer2 = ((MerchantMenu)this.menu).getOffers().size();
        if (this.isDragging) {
            final int integer3 = this.topPos + 18;
            final int integer4 = integer3 + 139;
            final int integer5 = integer2 - 7;
            float float15 = ((float)double2 - integer3 - 13.5f) / (integer4 - integer3 - 27.0f);
            float15 = float15 * integer5 + 0.5f;
            this.scrollOff = Mth.clamp((int)float15, 0, integer5);
            return true;
        }
        return super.mouseDragged(double1, double2, integer, double4, double5);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        this.isDragging = false;
        final int integer2 = (this.width - this.imageWidth) / 2;
        final int integer3 = (this.height - this.imageHeight) / 2;
        if (this.canScroll(((MerchantMenu)this.menu).getOffers().size()) && double1 > integer2 + 94 && double1 < integer2 + 94 + 6 && double2 > integer3 + 18 && double2 <= integer3 + 18 + 139 + 1) {
            this.isDragging = true;
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    static {
        VILLAGER_LOCATION = new ResourceLocation("textures/gui/container/villager2.png");
        TRADES_LABEL = new TranslatableComponent("merchant.trades");
        LEVEL_SEPARATOR = new TextComponent(" - ");
        DEPRECATED_TOOLTIP = new TranslatableComponent("merchant.deprecated");
    }
    
    class TradeOfferButton extends Button {
        final int index;
        
        public TradeOfferButton(final int integer2, final int integer3, final int integer4, final OnPress a) {
            super(integer2, integer3, 89, 20, TextComponent.EMPTY, a);
            this.index = integer4;
            this.visible = false;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        @Override
        public void renderToolTip(final PoseStack dfj, final int integer2, final int integer3) {
            if (this.isHovered && ((MerchantMenu)MerchantScreen.this.menu).getOffers().size() > this.index + MerchantScreen.this.scrollOff) {
                if (integer2 < this.x + 20) {
                    final ItemStack bly5 = ((MerchantOffer)((MerchantMenu)MerchantScreen.this.menu).getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostA();
                    Screen.this.renderTooltip(dfj, bly5, integer2, integer3);
                }
                else if (integer2 < this.x + 50 && integer2 > this.x + 30) {
                    final ItemStack bly5 = ((MerchantOffer)((MerchantMenu)MerchantScreen.this.menu).getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostB();
                    if (!bly5.isEmpty()) {
                        Screen.this.renderTooltip(dfj, bly5, integer2, integer3);
                    }
                }
                else if (integer2 > this.x + 65) {
                    final ItemStack bly5 = ((MerchantOffer)((MerchantMenu)MerchantScreen.this.menu).getOffers().get(this.index + MerchantScreen.this.scrollOff)).getResult();
                    Screen.this.renderTooltip(dfj, bly5, integer2, integer3);
                }
            }
        }
    }
}
