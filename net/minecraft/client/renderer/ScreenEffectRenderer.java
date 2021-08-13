package net.minecraft.client.renderer;

import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.resources.model.ModelBakery;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ScreenEffectRenderer {
    private static final ResourceLocation UNDERWATER_LOCATION;
    
    public static void renderScreenEffect(final Minecraft djw, final PoseStack dfj) {
        RenderSystem.disableAlphaTest();
        final Player bft3 = djw.player;
        if (!bft3.noPhysics) {
            final BlockState cee4 = getViewBlockingState(bft3);
            if (cee4 != null) {
                renderTex(djw, djw.getBlockRenderer().getBlockModelShaper().getParticleIcon(cee4), dfj);
            }
        }
        if (!djw.player.isSpectator()) {
            if (djw.player.isEyeInFluid(FluidTags.WATER)) {
                renderWater(djw, dfj);
            }
            if (djw.player.isOnFire()) {
                renderFire(djw, dfj);
            }
        }
        RenderSystem.enableAlphaTest();
    }
    
    @Nullable
    private static BlockState getViewBlockingState(final Player bft) {
        final BlockPos.MutableBlockPos a2 = new BlockPos.MutableBlockPos();
        for (int integer3 = 0; integer3 < 8; ++integer3) {
            final double double4 = bft.getX() + ((integer3 >> 0) % 2 - 0.5f) * bft.getBbWidth() * 0.8f;
            final double double5 = bft.getEyeY() + ((integer3 >> 1) % 2 - 0.5f) * 0.1f;
            final double double6 = bft.getZ() + ((integer3 >> 2) % 2 - 0.5f) * bft.getBbWidth() * 0.8f;
            a2.set(double4, double5, double6);
            final BlockState cee10 = bft.level.getBlockState(a2);
            if (cee10.getRenderShape() != RenderShape.INVISIBLE && cee10.isViewBlocking(bft.level, a2)) {
                return cee10;
            }
        }
        return null;
    }
    
    private static void renderTex(final Minecraft djw, final TextureAtlasSprite eju, final PoseStack dfj) {
        djw.getTextureManager().bind(eju.atlas().location());
        final BufferBuilder dfe4 = Tesselator.getInstance().getBuilder();
        final float float5 = 0.1f;
        final float float6 = -1.0f;
        final float float7 = 1.0f;
        final float float8 = -1.0f;
        final float float9 = 1.0f;
        final float float10 = -0.5f;
        final float float11 = eju.getU0();
        final float float12 = eju.getU1();
        final float float13 = eju.getV0();
        final float float14 = eju.getV1();
        final Matrix4f b15 = dfj.last().pose();
        dfe4.begin(7, DefaultVertexFormat.POSITION_COLOR_TEX);
        dfe4.vertex(b15, -1.0f, -1.0f, -0.5f).color(0.1f, 0.1f, 0.1f, 1.0f).uv(float12, float14).endVertex();
        dfe4.vertex(b15, 1.0f, -1.0f, -0.5f).color(0.1f, 0.1f, 0.1f, 1.0f).uv(float11, float14).endVertex();
        dfe4.vertex(b15, 1.0f, 1.0f, -0.5f).color(0.1f, 0.1f, 0.1f, 1.0f).uv(float11, float13).endVertex();
        dfe4.vertex(b15, -1.0f, 1.0f, -0.5f).color(0.1f, 0.1f, 0.1f, 1.0f).uv(float12, float13).endVertex();
        dfe4.end();
        BufferUploader.end(dfe4);
    }
    
    private static void renderWater(final Minecraft djw, final PoseStack dfj) {
        RenderSystem.enableTexture();
        djw.getTextureManager().bind(ScreenEffectRenderer.UNDERWATER_LOCATION);
        final BufferBuilder dfe3 = Tesselator.getInstance().getBuilder();
        final float float4 = djw.player.getBrightness();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final float float5 = 4.0f;
        final float float6 = -1.0f;
        final float float7 = 1.0f;
        final float float8 = -1.0f;
        final float float9 = 1.0f;
        final float float10 = -0.5f;
        final float float11 = -djw.player.yRot / 64.0f;
        final float float12 = djw.player.xRot / 64.0f;
        final Matrix4f b13 = dfj.last().pose();
        dfe3.begin(7, DefaultVertexFormat.POSITION_COLOR_TEX);
        dfe3.vertex(b13, -1.0f, -1.0f, -0.5f).color(float4, float4, float4, 0.1f).uv(4.0f + float11, 4.0f + float12).endVertex();
        dfe3.vertex(b13, 1.0f, -1.0f, -0.5f).color(float4, float4, float4, 0.1f).uv(0.0f + float11, 4.0f + float12).endVertex();
        dfe3.vertex(b13, 1.0f, 1.0f, -0.5f).color(float4, float4, float4, 0.1f).uv(0.0f + float11, 0.0f + float12).endVertex();
        dfe3.vertex(b13, -1.0f, 1.0f, -0.5f).color(float4, float4, float4, 0.1f).uv(4.0f + float11, 0.0f + float12).endVertex();
        dfe3.end();
        BufferUploader.end(dfe3);
        RenderSystem.disableBlend();
    }
    
    private static void renderFire(final Minecraft djw, final PoseStack dfj) {
        final BufferBuilder dfe3 = Tesselator.getInstance().getBuilder();
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();
        final TextureAtlasSprite eju4 = ModelBakery.FIRE_1.sprite();
        djw.getTextureManager().bind(eju4.atlas().location());
        final float float5 = eju4.getU0();
        final float float6 = eju4.getU1();
        final float float7 = (float5 + float6) / 2.0f;
        final float float8 = eju4.getV0();
        final float float9 = eju4.getV1();
        final float float10 = (float8 + float9) / 2.0f;
        final float float11 = eju4.uvShrinkRatio();
        final float float12 = Mth.lerp(float11, float5, float7);
        final float float13 = Mth.lerp(float11, float6, float7);
        final float float14 = Mth.lerp(float11, float8, float10);
        final float float15 = Mth.lerp(float11, float9, float10);
        final float float16 = 1.0f;
        for (int integer17 = 0; integer17 < 2; ++integer17) {
            dfj.pushPose();
            final float float17 = -0.5f;
            final float float18 = 0.5f;
            final float float19 = -0.5f;
            final float float20 = 0.5f;
            final float float21 = -0.5f;
            dfj.translate(-(integer17 * 2 - 1) * 0.24f, -0.30000001192092896, 0.0);
            dfj.mulPose(Vector3f.YP.rotationDegrees((integer17 * 2 - 1) * 10.0f));
            final Matrix4f b23 = dfj.last().pose();
            dfe3.begin(7, DefaultVertexFormat.POSITION_COLOR_TEX);
            dfe3.vertex(b23, -0.5f, -0.5f, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).uv(float13, float15).endVertex();
            dfe3.vertex(b23, 0.5f, -0.5f, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).uv(float12, float15).endVertex();
            dfe3.vertex(b23, 0.5f, 0.5f, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).uv(float12, float14).endVertex();
            dfe3.vertex(b23, -0.5f, 0.5f, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).uv(float13, float14).endVertex();
            dfe3.end();
            BufferUploader.end(dfe3);
            dfj.popPose();
        }
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }
    
    static {
        UNDERWATER_LOCATION = new ResourceLocation("textures/misc/underwater.png");
    }
}
