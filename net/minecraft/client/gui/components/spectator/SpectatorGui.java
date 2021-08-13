package net.minecraft.client.gui.components.spectator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.spectator.categories.SpectatorPage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.spectator.SpectatorMenuListener;
import net.minecraft.client.gui.GuiComponent;

public class SpectatorGui extends GuiComponent implements SpectatorMenuListener {
    private static final ResourceLocation WIDGETS_LOCATION;
    public static final ResourceLocation SPECTATOR_LOCATION;
    private final Minecraft minecraft;
    private long lastSelectionTime;
    private SpectatorMenu menu;
    
    public SpectatorGui(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void onHotbarSelected(final int integer) {
        this.lastSelectionTime = Util.getMillis();
        if (this.menu != null) {
            this.menu.selectSlot(integer);
        }
        else {
            this.menu = new SpectatorMenu(this);
        }
    }
    
    private float getHotbarAlpha() {
        final long long2 = this.lastSelectionTime - Util.getMillis() + 5000L;
        return Mth.clamp(long2 / 2000.0f, 0.0f, 1.0f);
    }
    
    public void renderHotbar(final PoseStack dfj, final float float2) {
        if (this.menu == null) {
            return;
        }
        final float float3 = this.getHotbarAlpha();
        if (float3 <= 0.0f) {
            this.menu.exit();
            return;
        }
        final int integer5 = this.minecraft.getWindow().getGuiScaledWidth() / 2;
        final int integer6 = this.getBlitOffset();
        this.setBlitOffset(-90);
        final int integer7 = Mth.floor(this.minecraft.getWindow().getGuiScaledHeight() - 22.0f * float3);
        final SpectatorPage dsm8 = this.menu.getCurrentPage();
        this.renderPage(dfj, float3, integer5, integer7, dsm8);
        this.setBlitOffset(integer6);
    }
    
    protected void renderPage(final PoseStack dfj, final float float2, final int integer3, final int integer4, final SpectatorPage dsm) {
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, float2);
        this.minecraft.getTextureManager().bind(SpectatorGui.WIDGETS_LOCATION);
        this.blit(dfj, integer3 - 91, integer4, 0, 0, 182, 22);
        if (dsm.getSelectedSlot() >= 0) {
            this.blit(dfj, integer3 - 91 - 1 + dsm.getSelectedSlot() * 20, integer4 - 1, 0, 22, 24, 22);
        }
        for (int integer5 = 0; integer5 < 9; ++integer5) {
            this.renderSlot(dfj, integer5, this.minecraft.getWindow().getGuiScaledWidth() / 2 - 90 + integer5 * 20 + 2, (float)(integer4 + 3), float2, dsm.getItem(integer5));
        }
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();
    }
    
    private void renderSlot(final PoseStack dfj, final int integer2, final int integer3, final float float4, final float float5, final SpectatorMenuItem dsk) {
        this.minecraft.getTextureManager().bind(SpectatorGui.SPECTATOR_LOCATION);
        if (dsk != SpectatorMenu.EMPTY_SLOT) {
            final int integer4 = (int)(float5 * 255.0f);
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)integer3, float4, 0.0f);
            final float float6 = dsk.isEnabled() ? 1.0f : 0.25f;
            RenderSystem.color4f(float6, float6, float6, float5);
            dsk.renderIcon(dfj, float6, integer4);
            RenderSystem.popMatrix();
            if (integer4 > 3 && dsk.isEnabled()) {
                final Component nr10 = this.minecraft.options.keyHotbarSlots[integer2].getTranslatedKeyMessage();
                this.minecraft.font.drawShadow(dfj, nr10, (float)(integer3 + 19 - 2 - this.minecraft.font.width(nr10)), float4 + 6.0f + 3.0f, 16777215 + (integer4 << 24));
            }
        }
    }
    
    public void renderTooltip(final PoseStack dfj) {
        final int integer3 = (int)(this.getHotbarAlpha() * 255.0f);
        if (integer3 > 3 && this.menu != null) {
            final SpectatorMenuItem dsk4 = this.menu.getSelectedItem();
            final Component nr5 = (dsk4 == SpectatorMenu.EMPTY_SLOT) ? this.menu.getSelectedCategory().getPrompt() : dsk4.getName();
            if (nr5 != null) {
                final int integer4 = (this.minecraft.getWindow().getGuiScaledWidth() - this.minecraft.font.width(nr5)) / 2;
                final int integer5 = this.minecraft.getWindow().getGuiScaledHeight() - 35;
                RenderSystem.pushMatrix();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                this.minecraft.font.drawShadow(dfj, nr5, (float)integer4, (float)integer5, 16777215 + (integer3 << 24));
                RenderSystem.disableBlend();
                RenderSystem.popMatrix();
            }
        }
    }
    
    @Override
    public void onSpectatorMenuClosed(final SpectatorMenu dsi) {
        this.menu = null;
        this.lastSelectionTime = 0L;
    }
    
    public boolean isMenuActive() {
        return this.menu != null;
    }
    
    public void onMouseScrolled(final double double1) {
        int integer4;
        for (integer4 = this.menu.getSelectedSlot() + (int)double1; integer4 >= 0 && integer4 <= 8 && (this.menu.getItem(integer4) == SpectatorMenu.EMPTY_SLOT || !this.menu.getItem(integer4).isEnabled()); integer4 += (int)double1) {}
        if (integer4 >= 0 && integer4 <= 8) {
            this.menu.selectSlot(integer4);
            this.lastSelectionTime = Util.getMillis();
        }
    }
    
    public void onMouseMiddleClick() {
        this.lastSelectionTime = Util.getMillis();
        if (this.isMenuActive()) {
            final int integer2 = this.menu.getSelectedSlot();
            if (integer2 != -1) {
                this.menu.selectSlot(integer2);
            }
        }
        else {
            this.menu = new SpectatorMenu(this);
        }
    }
    
    static {
        WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
        SPECTATOR_LOCATION = new ResourceLocation("textures/gui/spectator_widgets.png");
    }
}
