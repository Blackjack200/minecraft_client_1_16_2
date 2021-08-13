package net.minecraft.client.gui.screens.advancements;

import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.advancements.Advancement;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.gui.screens.Screen;

public class AdvancementsScreen extends Screen implements ClientAdvancements.Listener {
    private static final ResourceLocation WINDOW_LOCATION;
    private static final ResourceLocation TABS_LOCATION;
    private static final Component VERY_SAD_LABEL;
    private static final Component NO_ADVANCEMENTS_LABEL;
    private static final Component TITLE;
    private final ClientAdvancements advancements;
    private final Map<Advancement, AdvancementTab> tabs;
    private AdvancementTab selectedTab;
    private boolean isScrolling;
    
    public AdvancementsScreen(final ClientAdvancements dwi) {
        super(NarratorChatListener.NO_TITLE);
        this.tabs = (Map<Advancement, AdvancementTab>)Maps.newLinkedHashMap();
        this.advancements = dwi;
    }
    
    @Override
    protected void init() {
        this.tabs.clear();
        this.selectedTab = null;
        this.advancements.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.advancements.setSelectedTab(((AdvancementTab)this.tabs.values().iterator().next()).getAdvancement(), true);
        }
        else {
            this.advancements.setSelectedTab((this.selectedTab == null) ? null : this.selectedTab.getAdvancement(), true);
        }
    }
    
    @Override
    public void removed() {
        this.advancements.setListener(null);
        final ClientPacketListener dwm2 = this.minecraft.getConnection();
        if (dwm2 != null) {
            dwm2.send(ServerboundSeenAdvancementsPacket.closedScreen());
        }
    }
    
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (integer == 0) {
            final int integer2 = (this.width - 252) / 2;
            final int integer3 = (this.height - 140) / 2;
            for (final AdvancementTab dpb10 : this.tabs.values()) {
                if (dpb10.isMouseOver(integer2, integer3, double1, double2)) {
                    this.advancements.setSelectedTab(dpb10.getAdvancement(), true);
                    break;
                }
            }
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (this.minecraft.options.keyAdvancements.matches(integer1, integer2)) {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final int integer4 = (this.width - 252) / 2;
        final int integer5 = (this.height - 140) / 2;
        this.renderBackground(dfj);
        this.renderInside(dfj, integer2, integer3, integer4, integer5);
        this.renderWindow(dfj, integer4, integer5);
        this.renderTooltips(dfj, integer2, integer3, integer4, integer5);
    }
    
    public boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        if (integer != 0) {
            return this.isScrolling = false;
        }
        if (!this.isScrolling) {
            this.isScrolling = true;
        }
        else if (this.selectedTab != null) {
            this.selectedTab.scroll(double4, double5);
        }
        return true;
    }
    
    private void renderInside(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        final AdvancementTab dpb7 = this.selectedTab;
        if (dpb7 == null) {
            GuiComponent.fill(dfj, integer4 + 9, integer5 + 18, integer4 + 9 + 234, integer5 + 18 + 113, -16777216);
            final int integer6 = integer4 + 9 + 117;
            final Font font = this.font;
            final Component no_ADVANCEMENTS_LABEL = AdvancementsScreen.NO_ADVANCEMENTS_LABEL;
            final int integer7 = integer6;
            final int n = integer5 + 18 + 56;
            this.font.getClass();
            GuiComponent.drawCenteredString(dfj, font, no_ADVANCEMENTS_LABEL, integer7, n - 9 / 2, -1);
            final Font font2 = this.font;
            final Component very_SAD_LABEL = AdvancementsScreen.VERY_SAD_LABEL;
            final int integer8 = integer6;
            final int n2 = integer5 + 18 + 113;
            this.font.getClass();
            GuiComponent.drawCenteredString(dfj, font2, very_SAD_LABEL, integer8, n2 - 9, -1);
            return;
        }
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)(integer4 + 9), (float)(integer5 + 18), 0.0f);
        dpb7.drawContents(dfj);
        RenderSystem.popMatrix();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
    }
    
    public void renderWindow(final PoseStack dfj, final int integer2, final int integer3) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(AdvancementsScreen.WINDOW_LOCATION);
        this.blit(dfj, integer2, integer3, 0, 0, 252, 140);
        if (this.tabs.size() > 1) {
            this.minecraft.getTextureManager().bind(AdvancementsScreen.TABS_LOCATION);
            for (final AdvancementTab dpb6 : this.tabs.values()) {
                dpb6.drawTab(dfj, integer2, integer3, dpb6 == this.selectedTab);
            }
            RenderSystem.enableRescaleNormal();
            RenderSystem.defaultBlendFunc();
            for (final AdvancementTab dpb6 : this.tabs.values()) {
                dpb6.drawIcon(integer2, integer3, this.itemRenderer);
            }
            RenderSystem.disableBlend();
        }
        this.font.draw(dfj, AdvancementsScreen.TITLE, (float)(integer2 + 8), (float)(integer3 + 6), 4210752);
    }
    
    private void renderTooltips(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.selectedTab != null) {
            RenderSystem.pushMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.translatef((float)(integer4 + 9), (float)(integer5 + 18), 400.0f);
            this.selectedTab.drawTooltips(dfj, integer2 - integer4 - 9, integer3 - integer5 - 18, integer4, integer5);
            RenderSystem.disableDepthTest();
            RenderSystem.popMatrix();
        }
        if (this.tabs.size() > 1) {
            for (final AdvancementTab dpb8 : this.tabs.values()) {
                if (dpb8.isMouseOver(integer4, integer5, integer2, integer3)) {
                    this.renderTooltip(dfj, dpb8.getTitle(), integer2, integer3);
                }
            }
        }
    }
    
    public void onAddAdvancementRoot(final Advancement y) {
        final AdvancementTab dpb3 = AdvancementTab.create(this.minecraft, this, this.tabs.size(), y);
        if (dpb3 == null) {
            return;
        }
        this.tabs.put(y, dpb3);
    }
    
    public void onRemoveAdvancementRoot(final Advancement y) {
    }
    
    public void onAddAdvancementTask(final Advancement y) {
        final AdvancementTab dpb3 = this.getTab(y);
        if (dpb3 != null) {
            dpb3.addAdvancement(y);
        }
    }
    
    public void onRemoveAdvancementTask(final Advancement y) {
    }
    
    @Override
    public void onUpdateAdvancementProgress(final Advancement y, final AdvancementProgress aa) {
        final AdvancementWidget dpd4 = this.getAdvancementWidget(y);
        if (dpd4 != null) {
            dpd4.setProgress(aa);
        }
    }
    
    @Override
    public void onSelectedTabChanged(@Nullable final Advancement y) {
        this.selectedTab = (AdvancementTab)this.tabs.get(y);
    }
    
    public void onAdvancementsCleared() {
        this.tabs.clear();
        this.selectedTab = null;
    }
    
    @Nullable
    public AdvancementWidget getAdvancementWidget(final Advancement y) {
        final AdvancementTab dpb3 = this.getTab(y);
        return (dpb3 == null) ? null : dpb3.getWidget(y);
    }
    
    @Nullable
    private AdvancementTab getTab(Advancement y) {
        while (y.getParent() != null) {
            y = y.getParent();
        }
        return (AdvancementTab)this.tabs.get(y);
    }
    
    static {
        WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
        TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");
        VERY_SAD_LABEL = new TranslatableComponent("advancements.sad_label");
        NO_ADVANCEMENTS_LABEL = new TranslatableComponent("advancements.empty");
        TITLE = new TranslatableComponent("gui.advancements");
    }
}
