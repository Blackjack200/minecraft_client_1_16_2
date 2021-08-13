package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class LiquidBlockRenderer {
    private final TextureAtlasSprite[] lavaIcons;
    private final TextureAtlasSprite[] waterIcons;
    private TextureAtlasSprite waterOverlay;
    
    public LiquidBlockRenderer() {
        this.lavaIcons = new TextureAtlasSprite[2];
        this.waterIcons = new TextureAtlasSprite[2];
    }
    
    protected void setupSprites() {
        this.lavaIcons[0] = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.LAVA.defaultBlockState()).getParticleIcon();
        this.lavaIcons[1] = ModelBakery.LAVA_FLOW.sprite();
        this.waterIcons[0] = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.WATER.defaultBlockState()).getParticleIcon();
        this.waterIcons[1] = ModelBakery.WATER_FLOW.sprite();
        this.waterOverlay = ModelBakery.WATER_OVERLAY.sprite();
    }
    
    private static boolean isNeighborSameFluid(final BlockGetter bqz, final BlockPos fx, final Direction gc, final FluidState cuu) {
        final BlockPos fx2 = fx.relative(gc);
        final FluidState cuu2 = bqz.getFluidState(fx2);
        return cuu2.getType().isSame(cuu.getType());
    }
    
    private static boolean isFaceOccludedByState(final BlockGetter bqz, final Direction gc, final float float3, final BlockPos fx, final BlockState cee) {
        if (cee.canOcclude()) {
            final VoxelShape dde6 = Shapes.box(0.0, 0.0, 0.0, 1.0, float3, 1.0);
            final VoxelShape dde7 = cee.getOcclusionShape(bqz, fx);
            return Shapes.blockOccudes(dde6, dde7, gc);
        }
        return false;
    }
    
    private static boolean isFaceOccludedByNeighbor(final BlockGetter bqz, final BlockPos fx, final Direction gc, final float float4) {
        final BlockPos fx2 = fx.relative(gc);
        final BlockState cee6 = bqz.getBlockState(fx2);
        return isFaceOccludedByState(bqz, gc, float4, fx2, cee6);
    }
    
    private static boolean isFaceOccludedBySelf(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Direction gc) {
        return isFaceOccludedByState(bqz, gc.getOpposite(), 1.0f, fx, cee);
    }
    
    public static boolean shouldRenderFace(final BlockAndTintGetter bqx, final BlockPos fx, final FluidState cuu, final BlockState cee, final Direction gc) {
        return !isFaceOccludedBySelf(bqx, fx, cee, gc) && !isNeighborSameFluid(bqx, fx, gc, cuu);
    }
    
    public boolean tesselate(final BlockAndTintGetter bqx, final BlockPos fx, final VertexConsumer dfn, final FluidState cuu) {
        final boolean boolean6 = cuu.is(FluidTags.LAVA);
        final TextureAtlasSprite[] arr7 = boolean6 ? this.lavaIcons : this.waterIcons;
        final BlockState cee8 = bqx.getBlockState(fx);
        final int integer9 = boolean6 ? 16777215 : BiomeColors.getAverageWaterColor(bqx, fx);
        final float float10 = (integer9 >> 16 & 0xFF) / 255.0f;
        final float float11 = (integer9 >> 8 & 0xFF) / 255.0f;
        final float float12 = (integer9 & 0xFF) / 255.0f;
        final boolean boolean7 = !isNeighborSameFluid(bqx, fx, Direction.UP, cuu);
        final boolean boolean8 = shouldRenderFace(bqx, fx, cuu, cee8, Direction.DOWN) && !isFaceOccludedByNeighbor(bqx, fx, Direction.DOWN, 0.8888889f);
        final boolean boolean9 = shouldRenderFace(bqx, fx, cuu, cee8, Direction.NORTH);
        final boolean boolean10 = shouldRenderFace(bqx, fx, cuu, cee8, Direction.SOUTH);
        final boolean boolean11 = shouldRenderFace(bqx, fx, cuu, cee8, Direction.WEST);
        final boolean boolean12 = shouldRenderFace(bqx, fx, cuu, cee8, Direction.EAST);
        if (!boolean7 && !boolean8 && !boolean12 && !boolean11 && !boolean9 && !boolean10) {
            return false;
        }
        boolean boolean13 = false;
        final float float13 = bqx.getShade(Direction.DOWN, true);
        final float float14 = bqx.getShade(Direction.UP, true);
        final float float15 = bqx.getShade(Direction.NORTH, true);
        final float float16 = bqx.getShade(Direction.WEST, true);
        float float17 = this.getWaterHeight(bqx, fx, cuu.getType());
        float float18 = this.getWaterHeight(bqx, fx.south(), cuu.getType());
        float float19 = this.getWaterHeight(bqx, fx.east().south(), cuu.getType());
        float float20 = this.getWaterHeight(bqx, fx.east(), cuu.getType());
        final double double28 = fx.getX() & 0xF;
        final double double29 = fx.getY() & 0xF;
        final double double30 = fx.getZ() & 0xF;
        final float float21 = 0.001f;
        final float float22 = boolean8 ? 0.001f : 0.0f;
        if (boolean7 && !isFaceOccludedByNeighbor(bqx, fx, Direction.UP, Math.min(Math.min(float17, float18), Math.min(float19, float20)))) {
            boolean13 = true;
            float17 -= 0.001f;
            float18 -= 0.001f;
            float19 -= 0.001f;
            float20 -= 0.001f;
            final Vec3 dck44 = cuu.getFlow(bqx, fx);
            float float23;
            float float24;
            float float25;
            float float26;
            float float27;
            float float28;
            float float29;
            float float30;
            if (dck44.x == 0.0 && dck44.z == 0.0) {
                final TextureAtlasSprite eju45 = arr7[0];
                float23 = eju45.getU(0.0);
                float24 = eju45.getV(0.0);
                float25 = float23;
                float26 = eju45.getV(16.0);
                float27 = eju45.getU(16.0);
                float28 = float26;
                float29 = float27;
                float30 = float24;
            }
            else {
                final TextureAtlasSprite eju45 = arr7[1];
                final float float31 = (float)Mth.atan2(dck44.z, dck44.x) - 1.5707964f;
                final float float32 = Mth.sin(float31) * 0.25f;
                final float float33 = Mth.cos(float31) * 0.25f;
                final float float34 = 8.0f;
                float23 = eju45.getU(8.0f + (-float33 - float32) * 16.0f);
                float24 = eju45.getV(8.0f + (-float33 + float32) * 16.0f);
                float25 = eju45.getU(8.0f + (-float33 + float32) * 16.0f);
                float26 = eju45.getV(8.0f + (float33 + float32) * 16.0f);
                float27 = eju45.getU(8.0f + (float33 + float32) * 16.0f);
                float28 = eju45.getV(8.0f + (float33 - float32) * 16.0f);
                float29 = eju45.getU(8.0f + (float33 - float32) * 16.0f);
                float30 = eju45.getV(8.0f + (-float33 - float32) * 16.0f);
            }
            final float float35 = (float23 + float25 + float27 + float29) / 4.0f;
            final float float31 = (float24 + float26 + float28 + float30) / 4.0f;
            final float float32 = arr7[0].getWidth() / (arr7[0].getU1() - arr7[0].getU0());
            final float float33 = arr7[0].getHeight() / (arr7[0].getV1() - arr7[0].getV0());
            final float float34 = 4.0f / Math.max(float33, float32);
            float23 = Mth.lerp(float34, float23, float35);
            float25 = Mth.lerp(float34, float25, float35);
            float27 = Mth.lerp(float34, float27, float35);
            float29 = Mth.lerp(float34, float29, float35);
            float24 = Mth.lerp(float34, float24, float31);
            float26 = Mth.lerp(float34, float26, float31);
            float28 = Mth.lerp(float34, float28, float31);
            float30 = Mth.lerp(float34, float30, float31);
            final int integer10 = this.getLightColor(bqx, fx);
            final float float36 = float14 * float10;
            final float float37 = float14 * float11;
            final float float38 = float14 * float12;
            this.vertex(dfn, double28 + 0.0, double29 + float17, double30 + 0.0, float36, float37, float38, float23, float24, integer10);
            this.vertex(dfn, double28 + 0.0, double29 + float18, double30 + 1.0, float36, float37, float38, float25, float26, integer10);
            this.vertex(dfn, double28 + 1.0, double29 + float19, double30 + 1.0, float36, float37, float38, float27, float28, integer10);
            this.vertex(dfn, double28 + 1.0, double29 + float20, double30 + 0.0, float36, float37, float38, float29, float30, integer10);
            if (cuu.shouldRenderBackwardUpFace(bqx, fx.above())) {
                this.vertex(dfn, double28 + 0.0, double29 + float17, double30 + 0.0, float36, float37, float38, float23, float24, integer10);
                this.vertex(dfn, double28 + 1.0, double29 + float20, double30 + 0.0, float36, float37, float38, float29, float30, integer10);
                this.vertex(dfn, double28 + 1.0, double29 + float19, double30 + 1.0, float36, float37, float38, float27, float28, integer10);
                this.vertex(dfn, double28 + 0.0, double29 + float18, double30 + 1.0, float36, float37, float38, float25, float26, integer10);
            }
        }
        if (boolean8) {
            final float float23 = arr7[0].getU0();
            final float float25 = arr7[0].getU1();
            final float float27 = arr7[0].getV0();
            final float float29 = arr7[0].getV1();
            final int integer11 = this.getLightColor(bqx, fx.below());
            final float float26 = float13 * float10;
            final float float28 = float13 * float11;
            final float float30 = float13 * float12;
            this.vertex(dfn, double28, double29 + float22, double30 + 1.0, float26, float28, float30, float23, float29, integer11);
            this.vertex(dfn, double28, double29 + float22, double30, float26, float28, float30, float23, float27, integer11);
            this.vertex(dfn, double28 + 1.0, double29 + float22, double30, float26, float28, float30, float25, float27, integer11);
            this.vertex(dfn, double28 + 1.0, double29 + float22, double30 + 1.0, float26, float28, float30, float25, float29, integer11);
            boolean13 = true;
        }
        for (int integer12 = 0; integer12 < 4; ++integer12) {
            float float25;
            float float27;
            double double31;
            double double32;
            double double33;
            double double34;
            Direction gc47;
            boolean boolean14;
            if (integer12 == 0) {
                float25 = float17;
                float27 = float20;
                double31 = double28;
                double32 = double28 + 1.0;
                double33 = double30 + 0.0010000000474974513;
                double34 = double30 + 0.0010000000474974513;
                gc47 = Direction.NORTH;
                boolean14 = boolean9;
            }
            else if (integer12 == 1) {
                float25 = float19;
                float27 = float18;
                double31 = double28 + 1.0;
                double32 = double28;
                double33 = double30 + 1.0 - 0.0010000000474974513;
                double34 = double30 + 1.0 - 0.0010000000474974513;
                gc47 = Direction.SOUTH;
                boolean14 = boolean10;
            }
            else if (integer12 == 2) {
                float25 = float18;
                float27 = float17;
                double31 = double28 + 0.0010000000474974513;
                double32 = double28 + 0.0010000000474974513;
                double33 = double30 + 1.0;
                double34 = double30;
                gc47 = Direction.WEST;
                boolean14 = boolean11;
            }
            else {
                float25 = float20;
                float27 = float19;
                double31 = double28 + 1.0 - 0.0010000000474974513;
                double32 = double28 + 1.0 - 0.0010000000474974513;
                double33 = double30;
                double34 = double30 + 1.0;
                gc47 = Direction.EAST;
                boolean14 = boolean12;
            }
            if (boolean14 && !isFaceOccludedByNeighbor(bqx, fx, gc47, Math.max(float25, float27))) {
                boolean13 = true;
                final BlockPos fx2 = fx.relative(gc47);
                TextureAtlasSprite eju46 = arr7[1];
                if (!boolean6) {
                    final Block bul51 = bqx.getBlockState(fx2).getBlock();
                    if (bul51 instanceof HalfTransparentBlock || bul51 instanceof LeavesBlock) {
                        eju46 = this.waterOverlay;
                    }
                }
                final float float36 = eju46.getU(0.0);
                final float float37 = eju46.getU(8.0);
                final float float38 = eju46.getV((1.0f - float25) * 16.0f * 0.5f);
                final float float39 = eju46.getV((1.0f - float27) * 16.0f * 0.5f);
                final float float40 = eju46.getV(8.0);
                final int integer13 = this.getLightColor(bqx, fx2);
                final float float41 = (integer12 < 2) ? float15 : float16;
                final float float42 = float14 * float41 * float10;
                final float float43 = float14 * float41 * float11;
                final float float44 = float14 * float41 * float12;
                this.vertex(dfn, double31, double29 + float25, double33, float42, float43, float44, float36, float38, integer13);
                this.vertex(dfn, double32, double29 + float27, double34, float42, float43, float44, float37, float39, integer13);
                this.vertex(dfn, double32, double29 + float22, double34, float42, float43, float44, float37, float40, integer13);
                this.vertex(dfn, double31, double29 + float22, double33, float42, float43, float44, float36, float40, integer13);
                if (eju46 != this.waterOverlay) {
                    this.vertex(dfn, double31, double29 + float22, double33, float42, float43, float44, float36, float40, integer13);
                    this.vertex(dfn, double32, double29 + float22, double34, float42, float43, float44, float37, float40, integer13);
                    this.vertex(dfn, double32, double29 + float27, double34, float42, float43, float44, float37, float39, integer13);
                    this.vertex(dfn, double31, double29 + float25, double33, float42, float43, float44, float36, float38, integer13);
                }
            }
        }
        return boolean13;
    }
    
    private void vertex(final VertexConsumer dfn, final double double2, final double double3, final double double4, final float float5, final float float6, final float float7, final float float8, final float float9, final int integer) {
        dfn.vertex(double2, double3, double4).color(float5, float6, float7, 1.0f).uv(float8, float9).uv2(integer).normal(0.0f, 1.0f, 0.0f).endVertex();
    }
    
    private int getLightColor(final BlockAndTintGetter bqx, final BlockPos fx) {
        final int integer4 = LevelRenderer.getLightColor(bqx, fx);
        final int integer5 = LevelRenderer.getLightColor(bqx, fx.above());
        final int integer6 = integer4 & 0xFF;
        final int integer7 = integer5 & 0xFF;
        final int integer8 = integer4 >> 16 & 0xFF;
        final int integer9 = integer5 >> 16 & 0xFF;
        return ((integer6 > integer7) ? integer6 : integer7) | ((integer8 > integer9) ? integer8 : integer9) << 16;
    }
    
    private float getWaterHeight(final BlockGetter bqz, final BlockPos fx, final Fluid cut) {
        int integer5 = 0;
        float float6 = 0.0f;
        for (int integer6 = 0; integer6 < 4; ++integer6) {
            final BlockPos fx2 = fx.offset(-(integer6 & 0x1), 0, -(integer6 >> 1 & 0x1));
            if (bqz.getFluidState(fx2.above()).getType().isSame(cut)) {
                return 1.0f;
            }
            final FluidState cuu9 = bqz.getFluidState(fx2);
            if (cuu9.getType().isSame(cut)) {
                final float float7 = cuu9.getHeight(bqz, fx2);
                if (float7 >= 0.8f) {
                    float6 += float7 * 10.0f;
                    integer5 += 10;
                }
                else {
                    float6 += float7;
                    ++integer5;
                }
            }
            else if (!bqz.getBlockState(fx2).getMaterial().isSolid()) {
                ++integer5;
            }
        }
        return float6 / integer5;
    }
}
