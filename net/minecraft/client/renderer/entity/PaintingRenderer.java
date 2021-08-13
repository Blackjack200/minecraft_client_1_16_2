package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.PaintingTextureManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.decoration.Painting;

public class PaintingRenderer extends EntityRenderer<Painting> {
    public PaintingRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public void render(final Painting bcp, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float2));
        final Motive bco8 = bcp.motive;
        final float float4 = 0.0625f;
        dfj.scale(0.0625f, 0.0625f, 0.0625f);
        final VertexConsumer dfn10 = dzy.getBuffer(RenderType.entitySolid(this.getTextureLocation(bcp)));
        final PaintingTextureManager ekj11 = Minecraft.getInstance().getPaintingTextures();
        this.renderPainting(dfj, dfn10, bcp, bco8.getWidth(), bco8.getHeight(), ekj11.get(bco8), ekj11.getBackSprite());
        dfj.popPose();
        super.render(bcp, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Painting bcp) {
        return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlas().location();
    }
    
    private void renderPainting(final PoseStack dfj, final VertexConsumer dfn, final Painting bcp, final int integer4, final int integer5, final TextureAtlasSprite eju6, final TextureAtlasSprite eju7) {
        final PoseStack.Pose a9 = dfj.last();
        final Matrix4f b10 = a9.pose();
        final Matrix3f a10 = a9.normal();
        final float float12 = -integer4 / 2.0f;
        final float float13 = -integer5 / 2.0f;
        final float float14 = 0.5f;
        final float float15 = eju7.getU0();
        final float float16 = eju7.getU1();
        final float float17 = eju7.getV0();
        final float float18 = eju7.getV1();
        final float float19 = eju7.getU0();
        final float float20 = eju7.getU1();
        final float float21 = eju7.getV0();
        final float float22 = eju7.getV(1.0);
        final float float23 = eju7.getU0();
        final float float24 = eju7.getU(1.0);
        final float float25 = eju7.getV0();
        final float float26 = eju7.getV1();
        final int integer6 = integer4 / 16;
        final int integer7 = integer5 / 16;
        final double double29 = 16.0 / integer6;
        final double double30 = 16.0 / integer7;
        for (int integer8 = 0; integer8 < integer6; ++integer8) {
            for (int integer9 = 0; integer9 < integer7; ++integer9) {
                final float float27 = float12 + (integer8 + 1) * 16;
                final float float28 = float12 + integer8 * 16;
                final float float29 = float13 + (integer9 + 1) * 16;
                final float float30 = float13 + integer9 * 16;
                int integer10 = Mth.floor(bcp.getX());
                final int integer11 = Mth.floor(bcp.getY() + (float29 + float30) / 2.0f / 16.0f);
                int integer12 = Mth.floor(bcp.getZ());
                final Direction gc42 = bcp.getDirection();
                if (gc42 == Direction.NORTH) {
                    integer10 = Mth.floor(bcp.getX() + (float27 + float28) / 2.0f / 16.0f);
                }
                if (gc42 == Direction.WEST) {
                    integer12 = Mth.floor(bcp.getZ() - (float27 + float28) / 2.0f / 16.0f);
                }
                if (gc42 == Direction.SOUTH) {
                    integer10 = Mth.floor(bcp.getX() - (float27 + float28) / 2.0f / 16.0f);
                }
                if (gc42 == Direction.EAST) {
                    integer12 = Mth.floor(bcp.getZ() + (float27 + float28) / 2.0f / 16.0f);
                }
                final int integer13 = LevelRenderer.getLightColor(bcp.level, new BlockPos(integer10, integer11, integer12));
                final float float31 = eju6.getU(double29 * (integer6 - integer8));
                final float float32 = eju6.getU(double29 * (integer6 - (integer8 + 1)));
                final float float33 = eju6.getV(double30 * (integer7 - integer9));
                final float float34 = eju6.getV(double30 * (integer7 - (integer9 + 1)));
                this.vertex(b10, a10, dfn, float27, float30, float32, float33, -0.5f, 0, 0, -1, integer13);
                this.vertex(b10, a10, dfn, float28, float30, float31, float33, -0.5f, 0, 0, -1, integer13);
                this.vertex(b10, a10, dfn, float28, float29, float31, float34, -0.5f, 0, 0, -1, integer13);
                this.vertex(b10, a10, dfn, float27, float29, float32, float34, -0.5f, 0, 0, -1, integer13);
                this.vertex(b10, a10, dfn, float27, float29, float15, float17, 0.5f, 0, 0, 1, integer13);
                this.vertex(b10, a10, dfn, float28, float29, float16, float17, 0.5f, 0, 0, 1, integer13);
                this.vertex(b10, a10, dfn, float28, float30, float16, float18, 0.5f, 0, 0, 1, integer13);
                this.vertex(b10, a10, dfn, float27, float30, float15, float18, 0.5f, 0, 0, 1, integer13);
                this.vertex(b10, a10, dfn, float27, float29, float19, float21, -0.5f, 0, 1, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float29, float20, float21, -0.5f, 0, 1, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float29, float20, float22, 0.5f, 0, 1, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float29, float19, float22, 0.5f, 0, 1, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float30, float19, float21, 0.5f, 0, -1, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float30, float20, float21, 0.5f, 0, -1, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float30, float20, float22, -0.5f, 0, -1, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float30, float19, float22, -0.5f, 0, -1, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float29, float24, float25, 0.5f, -1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float30, float24, float26, 0.5f, -1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float30, float23, float26, -0.5f, -1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float27, float29, float23, float25, -0.5f, -1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float29, float24, float25, -0.5f, 1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float30, float24, float26, -0.5f, 1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float30, float23, float26, 0.5f, 1, 0, 0, integer13);
                this.vertex(b10, a10, dfn, float28, float29, float23, float25, 0.5f, 1, 0, 0, integer13);
            }
        }
    }
    
    private void vertex(final Matrix4f b, final Matrix3f a, final VertexConsumer dfn, final float float4, final float float5, final float float6, final float float7, final float float8, final int integer9, final int integer10, final int integer11, final int integer12) {
        dfn.vertex(b, float4, float5, float8).color(255, 255, 255, 255).uv(float6, float7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer12).normal(a, (float)integer9, (float)integer10, (float)integer11).endVertex();
    }
}
