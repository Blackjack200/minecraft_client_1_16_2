package net.minecraft.client.gui.screens.packs;

import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.locale.Language;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class TransferableSelectionList extends ObjectSelectionList<PackEntry> {
    private static final ResourceLocation ICON_OVERLAY_LOCATION;
    private static final Component INCOMPATIBLE_TITLE;
    private static final Component INCOMPATIBLE_CONFIRM_TITLE;
    private final Component title;
    
    public TransferableSelectionList(final Minecraft djw, final int integer2, final int integer3, final Component nr) {
        super(djw, integer2, integer3, 32, integer3 - 55 + 4, 36);
        this.title = nr;
        this.centerListVertically = false;
        final boolean boolean1 = true;
        djw.font.getClass();
        this.setRenderHeader(boolean1, (int)(9.0f * 1.5f));
    }
    
    @Override
    protected void renderHeader(final PoseStack dfj, final int integer2, final int integer3, final Tesselator dfl) {
        final Component nr6 = new TextComponent("").append(this.title).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD);
        this.minecraft.font.draw(dfj, nr6, (float)(integer2 + this.width / 2 - this.minecraft.font.width(nr6) / 2), (float)Math.min(this.y0 + 3, integer3), 16777215);
    }
    
    @Override
    public int getRowWidth() {
        return this.width;
    }
    
    @Override
    protected int getScrollbarPosition() {
        return this.x1 - 6;
    }
    
    static {
        ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/resource_packs.png");
        INCOMPATIBLE_TITLE = new TranslatableComponent("pack.incompatible");
        INCOMPATIBLE_CONFIRM_TITLE = new TranslatableComponent("pack.incompatible.confirm.title");
    }
    
    public static class PackEntry extends Entry<PackEntry> {
        private TransferableSelectionList parent;
        protected final Minecraft minecraft;
        protected final Screen screen;
        private final PackSelectionModel.Entry pack;
        private final FormattedCharSequence nameDisplayCache;
        private final MultiLineLabel descriptionDisplayCache;
        private final FormattedCharSequence incompatibleNameDisplayCache;
        private final MultiLineLabel incompatibleDescriptionDisplayCache;
        
        public PackEntry(final Minecraft djw, final TransferableSelectionList drg, final Screen doq, final PackSelectionModel.Entry a) {
            this.minecraft = djw;
            this.screen = doq;
            this.pack = a;
            this.parent = drg;
            this.nameDisplayCache = cacheName(djw, a.getTitle());
            this.descriptionDisplayCache = cacheDescription(djw, a.getExtendedDescription());
            this.incompatibleNameDisplayCache = cacheName(djw, TransferableSelectionList.INCOMPATIBLE_TITLE);
            this.incompatibleDescriptionDisplayCache = cacheDescription(djw, a.getCompatibility().getDescription());
        }
        
        private static FormattedCharSequence cacheName(final Minecraft djw, final Component nr) {
            final int integer3 = djw.font.width(nr);
            if (integer3 > 157) {
                final FormattedText nu4 = FormattedText.composite(djw.font.substrByWidth(nr, 157 - djw.font.width("...")), FormattedText.of("..."));
                return Language.getInstance().getVisualOrder(nu4);
            }
            return nr.getVisualOrderText();
        }
        
        private static MultiLineLabel cacheDescription(final Minecraft djw, final Component nr) {
            return MultiLineLabel.create(djw.font, nr, 157, 2);
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            final PackCompatibility abt12 = this.pack.getCompatibility();
            if (!abt12.isCompatible()) {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                GuiComponent.fill(dfj, integer4 - 1, integer3 - 1, integer4 + integer5 - 9, integer3 + integer6 + 1, -8978432);
            }
            this.minecraft.getTextureManager().bind(this.pack.getIconTexture());
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            GuiComponent.blit(dfj, integer4, integer3, 0.0f, 0.0f, 32, 32, 32, 32);
            FormattedCharSequence aex13 = this.nameDisplayCache;
            MultiLineLabel dlr14 = this.descriptionDisplayCache;
            if (this.showHoverOverlay() && (this.minecraft.options.touchscreen || boolean9)) {
                this.minecraft.getTextureManager().bind(TransferableSelectionList.ICON_OVERLAY_LOCATION);
                GuiComponent.fill(dfj, integer4, integer3, integer4 + 32, integer3 + 32, -1601138544);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                final int integer9 = integer7 - integer4;
                final int integer10 = integer8 - integer3;
                if (!this.pack.getCompatibility().isCompatible()) {
                    aex13 = this.incompatibleNameDisplayCache;
                    dlr14 = this.incompatibleDescriptionDisplayCache;
                }
                if (this.pack.canSelect()) {
                    if (integer9 < 32) {
                        GuiComponent.blit(dfj, integer4, integer3, 0.0f, 32.0f, 32, 32, 256, 256);
                    }
                    else {
                        GuiComponent.blit(dfj, integer4, integer3, 0.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
                else {
                    if (this.pack.canUnselect()) {
                        if (integer9 < 16) {
                            GuiComponent.blit(dfj, integer4, integer3, 32.0f, 32.0f, 32, 32, 256, 256);
                        }
                        else {
                            GuiComponent.blit(dfj, integer4, integer3, 32.0f, 0.0f, 32, 32, 256, 256);
                        }
                    }
                    if (this.pack.canMoveUp()) {
                        if (integer9 < 32 && integer9 > 16 && integer10 < 16) {
                            GuiComponent.blit(dfj, integer4, integer3, 96.0f, 32.0f, 32, 32, 256, 256);
                        }
                        else {
                            GuiComponent.blit(dfj, integer4, integer3, 96.0f, 0.0f, 32, 32, 256, 256);
                        }
                    }
                    if (this.pack.canMoveDown()) {
                        if (integer9 < 32 && integer9 > 16 && integer10 > 16) {
                            GuiComponent.blit(dfj, integer4, integer3, 64.0f, 32.0f, 32, 32, 256, 256);
                        }
                        else {
                            GuiComponent.blit(dfj, integer4, integer3, 64.0f, 0.0f, 32, 32, 256, 256);
                        }
                    }
                }
            }
            this.minecraft.font.drawShadow(dfj, aex13, (float)(integer4 + 32 + 2), (float)(integer3 + 1), 16777215);
            dlr14.renderLeftAligned(dfj, integer4 + 32 + 2, integer3 + 12, 10, 8421504);
        }
        
        private boolean showHoverOverlay() {
            return !this.pack.isFixedPosition() || !this.pack.isRequired();
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            final double double3 = double1 - this.parent.getRowLeft();
            final double double4 = double2 - this.parent.getRowTop(this.parent.children().indexOf(this));
            if (this.showHoverOverlay() && double3 <= 32.0) {
                if (this.pack.canSelect()) {
                    final PackCompatibility abt11 = this.pack.getCompatibility();
                    if (abt11.isCompatible()) {
                        this.pack.select();
                    }
                    else {
                        final Component nr12 = abt11.getConfirmation();
                        this.minecraft.setScreen(new ConfirmScreen(boolean1 -> {
                            this.minecraft.setScreen(this.screen);
                            if (boolean1) {
                                this.pack.select();
                            }
                        }, TransferableSelectionList.INCOMPATIBLE_CONFIRM_TITLE, nr12));
                    }
                    return true;
                }
                if (double3 < 16.0 && this.pack.canUnselect()) {
                    this.pack.unselect();
                    return true;
                }
                if (double3 > 16.0 && double4 < 16.0 && this.pack.canMoveUp()) {
                    this.pack.moveUp();
                    return true;
                }
                if (double3 > 16.0 && double4 > 16.0 && this.pack.canMoveDown()) {
                    this.pack.moveDown();
                    return true;
                }
            }
            return false;
        }
    }
}
