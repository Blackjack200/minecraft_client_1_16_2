package net.minecraft.client.gui.screens.inventory;

import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.client.gui.GuiComponent;
import java.util.Iterator;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.Collection;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class EffectRenderingInventoryScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected boolean doRenderEffects;
    
    public EffectRenderingInventoryScreen(final T bhz, final Inventory bfs, final Component nr) {
        super(bhz, bfs, nr);
    }
    
    @Override
    protected void init() {
        super.init();
        this.checkEffectRendering();
    }
    
    protected void checkEffectRendering() {
        if (this.minecraft.player.getActiveEffects().isEmpty()) {
            this.leftPos = (this.width - this.imageWidth) / 2;
            this.doRenderEffects = false;
        }
        else {
            this.leftPos = 160 + (this.width - this.imageWidth - 200) / 2;
            this.doRenderEffects = true;
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        super.render(dfj, integer2, integer3, float4);
        if (this.doRenderEffects) {
            this.renderEffects(dfj);
        }
    }
    
    private void renderEffects(final PoseStack dfj) {
        final int integer3 = this.leftPos - 124;
        final Collection<MobEffectInstance> collection4 = this.minecraft.player.getActiveEffects();
        if (collection4.isEmpty()) {
            return;
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        int integer4 = 33;
        if (collection4.size() > 5) {
            integer4 = 132 / (collection4.size() - 1);
        }
        final Iterable<MobEffectInstance> iterable6 = (Iterable<MobEffectInstance>)Ordering.natural().sortedCopy((Iterable)collection4);
        this.renderBackgrounds(dfj, integer3, integer4, iterable6);
        this.renderIcons(dfj, integer3, integer4, iterable6);
        this.renderLabels(dfj, integer3, integer4, iterable6);
    }
    
    private void renderBackgrounds(final PoseStack dfj, final int integer2, final int integer3, final Iterable<MobEffectInstance> iterable) {
        this.minecraft.getTextureManager().bind(EffectRenderingInventoryScreen.INVENTORY_LOCATION);
        int integer4 = this.topPos;
        for (final MobEffectInstance apr8 : iterable) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.blit(dfj, integer2, integer4, 0, 166, 140, 32);
            integer4 += integer3;
        }
    }
    
    private void renderIcons(final PoseStack dfj, final int integer2, final int integer3, final Iterable<MobEffectInstance> iterable) {
        final MobEffectTextureManager ekh6 = this.minecraft.getMobEffectTextures();
        int integer4 = this.topPos;
        for (final MobEffectInstance apr9 : iterable) {
            final MobEffect app10 = apr9.getEffect();
            final TextureAtlasSprite eju11 = ekh6.get(app10);
            this.minecraft.getTextureManager().bind(eju11.atlas().location());
            GuiComponent.blit(dfj, integer2 + 6, integer4 + 7, this.getBlitOffset(), 18, 18, eju11);
            integer4 += integer3;
        }
    }
    
    private void renderLabels(final PoseStack dfj, final int integer2, final int integer3, final Iterable<MobEffectInstance> iterable) {
        int integer4 = this.topPos;
        for (final MobEffectInstance apr8 : iterable) {
            String string9 = I18n.get(apr8.getEffect().getDescriptionId());
            if (apr8.getAmplifier() >= 1 && apr8.getAmplifier() <= 9) {
                string9 = string9 + ' ' + I18n.get(new StringBuilder().append("enchantment.level.").append(apr8.getAmplifier() + 1).toString());
            }
            this.font.drawShadow(dfj, string9, (float)(integer2 + 10 + 18), (float)(integer4 + 6), 16777215);
            final String string10 = MobEffectUtil.formatDuration(apr8, 1.0f);
            this.font.drawShadow(dfj, string10, (float)(integer2 + 10 + 18), (float)(integer4 + 6 + 10), 8355711);
            integer4 += integer3;
        }
    }
}
