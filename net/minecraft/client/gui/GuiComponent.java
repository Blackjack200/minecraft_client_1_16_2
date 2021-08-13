package net.minecraft.client.gui;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.BiConsumer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public abstract class GuiComponent {
    public static final ResourceLocation BACKGROUND_LOCATION;
    public static final ResourceLocation STATS_ICON_LOCATION;
    public static final ResourceLocation GUI_ICONS_LOCATION;
    private int blitOffset;
    
    protected void hLine(final PoseStack dfj, int integer2, int integer3, final int integer4, final int integer5) {
        if (integer3 < integer2) {
            final int integer6 = integer2;
            integer2 = integer3;
            integer3 = integer6;
        }
        fill(dfj, integer2, integer4, integer3 + 1, integer4 + 1, integer5);
    }
    
    protected void vLine(final PoseStack dfj, final int integer2, int integer3, int integer4, final int integer5) {
        if (integer4 < integer3) {
            final int integer6 = integer3;
            integer3 = integer4;
            integer4 = integer6;
        }
        fill(dfj, integer2, integer3 + 1, integer2 + 1, integer4, integer5);
    }
    
    public static void fill(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        innerFill(dfj.last().pose(), integer2, integer3, integer4, integer5, integer6);
    }
    
    private static void innerFill(final Matrix4f b, int integer2, int integer3, int integer4, int integer5, final int integer6) {
        if (integer2 < integer4) {
            final int integer7 = integer2;
            integer2 = integer4;
            integer4 = integer7;
        }
        if (integer3 < integer5) {
            final int integer7 = integer3;
            integer3 = integer5;
            integer5 = integer7;
        }
        final float float7 = (integer6 >> 24 & 0xFF) / 255.0f;
        final float float8 = (integer6 >> 16 & 0xFF) / 255.0f;
        final float float9 = (integer6 >> 8 & 0xFF) / 255.0f;
        final float float10 = (integer6 & 0xFF) / 255.0f;
        final BufferBuilder dfe11 = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        dfe11.begin(7, DefaultVertexFormat.POSITION_COLOR);
        dfe11.vertex(b, (float)integer2, (float)integer5, 0.0f).color(float8, float9, float10, float7).endVertex();
        dfe11.vertex(b, (float)integer4, (float)integer5, 0.0f).color(float8, float9, float10, float7).endVertex();
        dfe11.vertex(b, (float)integer4, (float)integer3, 0.0f).color(float8, float9, float10, float7).endVertex();
        dfe11.vertex(b, (float)integer2, (float)integer3, 0.0f).color(float8, float9, float10, float7).endVertex();
        dfe11.end();
        BufferUploader.end(dfe11);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    
    protected void fillGradient(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        final Tesselator dfl9 = Tesselator.getInstance();
        final BufferBuilder dfe10 = dfl9.getBuilder();
        dfe10.begin(7, DefaultVertexFormat.POSITION_COLOR);
        fillGradient(dfj.last().pose(), dfe10, integer2, integer3, integer4, integer5, this.blitOffset, integer6, integer7);
        dfl9.end();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }
    
    protected static void fillGradient(final Matrix4f b, final BufferBuilder dfe, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9) {
        final float float10 = (integer8 >> 24 & 0xFF) / 255.0f;
        final float float11 = (integer8 >> 16 & 0xFF) / 255.0f;
        final float float12 = (integer8 >> 8 & 0xFF) / 255.0f;
        final float float13 = (integer8 & 0xFF) / 255.0f;
        final float float14 = (integer9 >> 24 & 0xFF) / 255.0f;
        final float float15 = (integer9 >> 16 & 0xFF) / 255.0f;
        final float float16 = (integer9 >> 8 & 0xFF) / 255.0f;
        final float float17 = (integer9 & 0xFF) / 255.0f;
        dfe.vertex(b, (float)integer5, (float)integer4, (float)integer7).color(float11, float12, float13, float10).endVertex();
        dfe.vertex(b, (float)integer3, (float)integer4, (float)integer7).color(float11, float12, float13, float10).endVertex();
        dfe.vertex(b, (float)integer3, (float)integer6, (float)integer7).color(float15, float16, float17, float14).endVertex();
        dfe.vertex(b, (float)integer5, (float)integer6, (float)integer7).color(float15, float16, float17, float14).endVertex();
    }
    
    public static void drawCenteredString(final PoseStack dfj, final Font dkr, final String string, final int integer4, final int integer5, final int integer6) {
        dkr.drawShadow(dfj, string, (float)(integer4 - dkr.width(string) / 2), (float)integer5, integer6);
    }
    
    public static void drawCenteredString(final PoseStack dfj, final Font dkr, final Component nr, final int integer4, final int integer5, final int integer6) {
        final FormattedCharSequence aex7 = nr.getVisualOrderText();
        dkr.drawShadow(dfj, aex7, (float)(integer4 - dkr.width(aex7) / 2), (float)integer5, integer6);
    }
    
    public static void drawString(final PoseStack dfj, final Font dkr, final String string, final int integer4, final int integer5, final int integer6) {
        dkr.drawShadow(dfj, string, (float)integer4, (float)integer5, integer6);
    }
    
    public static void drawString(final PoseStack dfj, final Font dkr, final Component nr, final int integer4, final int integer5, final int integer6) {
        dkr.drawShadow(dfj, nr, (float)integer4, (float)integer5, integer6);
    }
    
    public void blitOutlineBlack(final int integer1, final int integer2, final BiConsumer<Integer, Integer> biConsumer) {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        biConsumer.accept((integer1 + 1), integer2);
        biConsumer.accept((integer1 - 1), integer2);
        biConsumer.accept(integer1, (integer2 + 1));
        biConsumer.accept(integer1, (integer2 - 1));
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        biConsumer.accept(integer1, integer2);
    }
    
    public static void blit(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final TextureAtlasSprite eju) {
        innerBlit(dfj.last().pose(), integer2, integer2 + integer5, integer3, integer3 + integer6, integer4, eju.getU0(), eju.getU1(), eju.getV0(), eju.getV1());
    }
    
    public void blit(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        blit(dfj, integer2, integer3, this.blitOffset, (float)integer4, (float)integer5, integer6, integer7, 256, 256);
    }
    
    public static void blit(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final float float5, final float float6, final int integer7, final int integer8, final int integer9, final int integer10) {
        innerBlit(dfj, integer2, integer2 + integer7, integer3, integer3 + integer8, integer4, integer7, integer8, float5, float6, integer10, integer9);
    }
    
    public static void blit(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final float float6, final float float7, final int integer8, final int integer9, final int integer10, final int integer11) {
        innerBlit(dfj, integer2, integer2 + integer4, integer3, integer3 + integer5, 0, integer8, integer9, float6, float7, integer10, integer11);
    }
    
    public static void blit(final PoseStack dfj, final int integer2, final int integer3, final float float4, final float float5, final int integer6, final int integer7, final int integer8, final int integer9) {
        blit(dfj, integer2, integer3, integer6, integer7, float4, float5, integer6, integer7, integer8, integer9);
    }
    
    private static void innerBlit(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final float float9, final float float10, final int integer11, final int integer12) {
        innerBlit(dfj.last().pose(), integer2, integer3, integer4, integer5, integer6, (float9 + 0.0f) / integer11, (float9 + integer7) / integer11, (float10 + 0.0f) / integer12, (float10 + integer8) / integer12);
    }
    
    private static void innerBlit(final Matrix4f b, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final float float7, final float float8, final float float9, final float float10) {
        final BufferBuilder dfe11 = Tesselator.getInstance().getBuilder();
        dfe11.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe11.vertex(b, (float)integer2, (float)integer5, (float)integer6).uv(float7, float10).endVertex();
        dfe11.vertex(b, (float)integer3, (float)integer5, (float)integer6).uv(float8, float10).endVertex();
        dfe11.vertex(b, (float)integer3, (float)integer4, (float)integer6).uv(float8, float9).endVertex();
        dfe11.vertex(b, (float)integer2, (float)integer4, (float)integer6).uv(float7, float9).endVertex();
        dfe11.end();
        RenderSystem.enableAlphaTest();
        BufferUploader.end(dfe11);
    }
    
    public int getBlitOffset() {
        return this.blitOffset;
    }
    
    public void setBlitOffset(final int integer) {
        this.blitOffset = integer;
    }
    
    static {
        BACKGROUND_LOCATION = new ResourceLocation("textures/gui/options_background.png");
        STATS_ICON_LOCATION = new ResourceLocation("textures/gui/container/stats_icons.png");
        GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    }
}
