package net.minecraft.client.renderer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class CubeMap {
    private final ResourceLocation[] images;
    
    public CubeMap(final ResourceLocation vk) {
        this.images = new ResourceLocation[6];
        for (int integer3 = 0; integer3 < 6; ++integer3) {
            this.images[integer3] = new ResourceLocation(vk.getNamespace(), vk.getPath() + '_' + integer3 + ".png");
        }
    }
    
    public void render(final Minecraft djw, final float float2, final float float3, final float float4) {
        final Tesselator dfl6 = Tesselator.getInstance();
        final BufferBuilder dfe7 = dfl6.getBuilder();
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(Matrix4f.perspective(85.0, djw.getWindow().getWidth() / (float)djw.getWindow().getHeight(), 0.05f, 10.0f));
        RenderSystem.matrixMode(5888);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.rotatef(180.0f, 1.0f, 0.0f, 0.0f);
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        final int integer8 = 2;
        for (int integer9 = 0; integer9 < 4; ++integer9) {
            RenderSystem.pushMatrix();
            final float float5 = (integer9 % 2 / 2.0f - 0.5f) / 256.0f;
            final float float6 = (integer9 / 2 / 2.0f - 0.5f) / 256.0f;
            final float float7 = 0.0f;
            RenderSystem.translatef(float5, float6, 0.0f);
            RenderSystem.rotatef(float2, 1.0f, 0.0f, 0.0f);
            RenderSystem.rotatef(float3, 0.0f, 1.0f, 0.0f);
            for (int integer10 = 0; integer10 < 6; ++integer10) {
                djw.getTextureManager().bind(this.images[integer10]);
                dfe7.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
                final int integer11 = Math.round(255.0f * float4) / (integer9 + 1);
                if (integer10 == 0) {
                    dfe7.vertex(-1.0, -1.0, 1.0).uv(0.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, 1.0, 1.0).uv(0.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, 1.0, 1.0).uv(1.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, -1.0, 1.0).uv(1.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                }
                if (integer10 == 1) {
                    dfe7.vertex(1.0, -1.0, 1.0).uv(0.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, 1.0, 1.0).uv(0.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, 1.0, -1.0).uv(1.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, -1.0, -1.0).uv(1.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                }
                if (integer10 == 2) {
                    dfe7.vertex(1.0, -1.0, -1.0).uv(0.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, 1.0, -1.0).uv(0.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, 1.0, -1.0).uv(1.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, -1.0, -1.0).uv(1.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                }
                if (integer10 == 3) {
                    dfe7.vertex(-1.0, -1.0, -1.0).uv(0.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, 1.0, -1.0).uv(0.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, 1.0, 1.0).uv(1.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, -1.0, 1.0).uv(1.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                }
                if (integer10 == 4) {
                    dfe7.vertex(-1.0, -1.0, -1.0).uv(0.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, -1.0, 1.0).uv(0.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, -1.0, 1.0).uv(1.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, -1.0, -1.0).uv(1.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                }
                if (integer10 == 5) {
                    dfe7.vertex(-1.0, 1.0, 1.0).uv(0.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(-1.0, 1.0, -1.0).uv(0.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, 1.0, -1.0).uv(1.0f, 1.0f).color(255, 255, 255, integer11).endVertex();
                    dfe7.vertex(1.0, 1.0, 1.0).uv(1.0f, 0.0f).color(255, 255, 255, integer11).endVertex();
                }
                dfl6.end();
            }
            RenderSystem.popMatrix();
            RenderSystem.colorMask(true, true, true, false);
        }
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.matrixMode(5889);
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(5888);
        RenderSystem.popMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }
    
    public CompletableFuture<Void> preload(final TextureManager ejv, final Executor executor) {
        final CompletableFuture<?>[] arr4 = new CompletableFuture[6];
        for (int integer5 = 0; integer5 < arr4.length; ++integer5) {
            arr4[integer5] = ejv.preload(this.images[integer5], executor);
        }
        return (CompletableFuture<Void>)CompletableFuture.allOf((CompletableFuture[])arr4);
    }
}
