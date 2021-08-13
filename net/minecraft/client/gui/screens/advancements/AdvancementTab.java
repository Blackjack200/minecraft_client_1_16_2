package net.minecraft.client.gui.screens.advancements;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public class AdvancementTab extends GuiComponent {
    private final Minecraft minecraft;
    private final AdvancementsScreen screen;
    private final AdvancementTabType type;
    private final int index;
    private final Advancement advancement;
    private final DisplayInfo display;
    private final ItemStack icon;
    private final Component title;
    private final AdvancementWidget root;
    private final Map<Advancement, AdvancementWidget> widgets;
    private double scrollX;
    private double scrollY;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private float fade;
    private boolean centered;
    
    public AdvancementTab(final Minecraft djw, final AdvancementsScreen dpf, final AdvancementTabType dpc, final int integer, final Advancement y, final DisplayInfo ah) {
        this.widgets = (Map<Advancement, AdvancementWidget>)Maps.newLinkedHashMap();
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.minecraft = djw;
        this.screen = dpf;
        this.type = dpc;
        this.index = integer;
        this.advancement = y;
        this.display = ah;
        this.icon = ah.getIcon();
        this.title = ah.getTitle();
        this.addWidget(this.root = new AdvancementWidget(this, djw, y, ah), y);
    }
    
    public Advancement getAdvancement() {
        return this.advancement;
    }
    
    public Component getTitle() {
        return this.title;
    }
    
    public void drawTab(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4) {
        this.type.draw(dfj, this, integer2, integer3, boolean4, this.index);
    }
    
    public void drawIcon(final int integer1, final int integer2, final ItemRenderer efg) {
        this.type.drawIcon(integer1, integer2, this.index, efg, this.icon);
    }
    
    public void drawContents(final PoseStack dfj) {
        if (!this.centered) {
            this.scrollX = 117 - (this.maxX + this.minX) / 2;
            this.scrollY = 56 - (this.maxY + this.minY) / 2;
            this.centered = true;
        }
        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0f, 0.0f, 950.0f);
        RenderSystem.colorMask(false, false, false, false);
        GuiComponent.fill(dfj, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0f, 0.0f, -950.0f);
        RenderSystem.depthFunc(518);
        GuiComponent.fill(dfj, 234, 113, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        final ResourceLocation vk3 = this.display.getBackground();
        if (vk3 != null) {
            this.minecraft.getTextureManager().bind(vk3);
        }
        else {
            this.minecraft.getTextureManager().bind(TextureManager.INTENTIONAL_MISSING_TEXTURE);
        }
        final int integer4 = Mth.floor(this.scrollX);
        final int integer5 = Mth.floor(this.scrollY);
        final int integer6 = integer4 % 16;
        final int integer7 = integer5 % 16;
        for (int integer8 = -1; integer8 <= 15; ++integer8) {
            for (int integer9 = -1; integer9 <= 8; ++integer9) {
                GuiComponent.blit(dfj, integer6 + 16 * integer8, integer7 + 16 * integer9, 0.0f, 0.0f, 16, 16, 16, 16);
            }
        }
        this.root.drawConnectivity(dfj, integer4, integer5, true);
        this.root.drawConnectivity(dfj, integer4, integer5, false);
        this.root.draw(dfj, integer4, integer5);
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0f, 0.0f, -950.0f);
        RenderSystem.colorMask(false, false, false, false);
        GuiComponent.fill(dfj, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0f, 0.0f, 950.0f);
        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
    }
    
    public void drawTooltips(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0f, 0.0f, 200.0f);
        GuiComponent.fill(dfj, 0, 0, 234, 113, Mth.floor(this.fade * 255.0f) << 24);
        boolean boolean7 = false;
        final int integer6 = Mth.floor(this.scrollX);
        final int integer7 = Mth.floor(this.scrollY);
        if (integer2 > 0 && integer2 < 234 && integer3 > 0 && integer3 < 113) {
            for (final AdvancementWidget dpd11 : this.widgets.values()) {
                if (dpd11.isMouseOver(integer6, integer7, integer2, integer3)) {
                    boolean7 = true;
                    dpd11.drawHover(dfj, integer6, integer7, this.fade, integer4, integer5);
                    break;
                }
            }
        }
        RenderSystem.popMatrix();
        if (boolean7) {
            this.fade = Mth.clamp(this.fade + 0.02f, 0.0f, 0.3f);
        }
        else {
            this.fade = Mth.clamp(this.fade - 0.04f, 0.0f, 1.0f);
        }
    }
    
    public boolean isMouseOver(final int integer1, final int integer2, final double double3, final double double4) {
        return this.type.isMouseOver(integer1, integer2, this.index, double3, double4);
    }
    
    @Nullable
    public static AdvancementTab create(final Minecraft djw, final AdvancementsScreen dpf, int integer, final Advancement y) {
        if (y.getDisplay() == null) {
            return null;
        }
        for (final AdvancementTabType dpc8 : AdvancementTabType.values()) {
            if (integer < dpc8.getMax()) {
                return new AdvancementTab(djw, dpf, dpc8, integer, y, y.getDisplay());
            }
            integer -= dpc8.getMax();
        }
        return null;
    }
    
    public void scroll(final double double1, final double double2) {
        if (this.maxX - this.minX > 234) {
            this.scrollX = Mth.clamp(this.scrollX + double1, -(this.maxX - 234), 0.0);
        }
        if (this.maxY - this.minY > 113) {
            this.scrollY = Mth.clamp(this.scrollY + double2, -(this.maxY - 113), 0.0);
        }
    }
    
    public void addAdvancement(final Advancement y) {
        if (y.getDisplay() == null) {
            return;
        }
        final AdvancementWidget dpd3 = new AdvancementWidget(this, this.minecraft, y, y.getDisplay());
        this.addWidget(dpd3, y);
    }
    
    private void addWidget(final AdvancementWidget dpd, final Advancement y) {
        this.widgets.put(y, dpd);
        final int integer4 = dpd.getX();
        final int integer5 = integer4 + 28;
        final int integer6 = dpd.getY();
        final int integer7 = integer6 + 27;
        this.minX = Math.min(this.minX, integer4);
        this.maxX = Math.max(this.maxX, integer5);
        this.minY = Math.min(this.minY, integer6);
        this.maxY = Math.max(this.maxY, integer7);
        for (final AdvancementWidget dpd2 : this.widgets.values()) {
            dpd2.attachToParent();
        }
    }
    
    @Nullable
    public AdvancementWidget getWidget(final Advancement y) {
        return (AdvancementWidget)this.widgets.get(y);
    }
    
    public AdvancementsScreen getScreen() {
        return this.screen;
    }
}
