package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class ChunkBorderRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    
    public ChunkBorderRenderer(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        RenderSystem.enableDepthTest();
        RenderSystem.shadeModel(7425);
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        final Entity apx10 = this.minecraft.gameRenderer.getMainCamera().getEntity();
        final Tesselator dfl11 = Tesselator.getInstance();
        final BufferBuilder dfe12 = dfl11.getBuilder();
        final double double6 = 0.0 - double4;
        final double double7 = 256.0 - double4;
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        final double double8 = (apx10.xChunk << 4) - double3;
        final double double9 = (apx10.zChunk << 4) - double5;
        RenderSystem.lineWidth(1.0f);
        dfe12.begin(3, DefaultVertexFormat.POSITION_COLOR);
        for (int integer21 = -16; integer21 <= 32; integer21 += 16) {
            for (int integer22 = -16; integer22 <= 32; integer22 += 16) {
                dfe12.vertex(double8 + integer21, double6, double9 + integer22).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex();
                dfe12.vertex(double8 + integer21, double6, double9 + integer22).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                dfe12.vertex(double8 + integer21, double7, double9 + integer22).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                dfe12.vertex(double8 + integer21, double7, double9 + integer22).color(1.0f, 0.0f, 0.0f, 0.0f).endVertex();
            }
        }
        for (int integer21 = 2; integer21 < 16; integer21 += 2) {
            dfe12.vertex(double8 + integer21, double6, double9).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8 + integer21, double6, double9).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + integer21, double7, double9).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + integer21, double7, double9).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8 + integer21, double6, double9 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8 + integer21, double6, double9 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + integer21, double7, double9 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + integer21, double7, double9 + 16.0).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
        }
        for (int integer21 = 2; integer21 < 16; integer21 += 2) {
            dfe12.vertex(double8, double6, double9 + integer21).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8, double6, double9 + integer21).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double7, double9 + integer21).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double7, double9 + integer21).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double6, double9 + integer21).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double6, double9 + integer21).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double7, double9 + integer21).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double7, double9 + integer21).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
        }
        for (int integer21 = 0; integer21 <= 256; integer21 += 2) {
            final double double10 = integer21 - double4;
            dfe12.vertex(double8, double10, double9).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
            dfe12.vertex(double8, double10, double9).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double10, double9 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double10, double9 + 16.0).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double10, double9).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double10, double9).color(1.0f, 1.0f, 0.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double10, double9).color(1.0f, 1.0f, 0.0f, 0.0f).endVertex();
        }
        dfl11.end();
        RenderSystem.lineWidth(2.0f);
        dfe12.begin(3, DefaultVertexFormat.POSITION_COLOR);
        for (int integer21 = 0; integer21 <= 16; integer21 += 16) {
            for (int integer22 = 0; integer22 <= 16; integer22 += 16) {
                dfe12.vertex(double8 + integer21, double6, double9 + integer22).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
                dfe12.vertex(double8 + integer21, double6, double9 + integer22).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                dfe12.vertex(double8 + integer21, double7, double9 + integer22).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
                dfe12.vertex(double8 + integer21, double7, double9 + integer22).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
            }
        }
        for (int integer21 = 0; integer21 <= 256; integer21 += 16) {
            final double double10 = integer21 - double4;
            dfe12.vertex(double8, double10, double9).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
            dfe12.vertex(double8, double10, double9).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double10, double9 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double10, double9 + 16.0).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            dfe12.vertex(double8 + 16.0, double10, double9).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double10, double9).color(0.25f, 0.25f, 1.0f, 1.0f).endVertex();
            dfe12.vertex(double8, double10, double9).color(0.25f, 0.25f, 1.0f, 0.0f).endVertex();
        }
        dfl11.end();
        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
    }
}
