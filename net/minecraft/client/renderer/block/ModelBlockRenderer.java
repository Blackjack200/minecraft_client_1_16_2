package net.minecraft.client.renderer.block;

import net.minecraft.core.Vec3i;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.world.level.block.Block;
import java.util.BitSet;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReport;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.client.Minecraft;
import java.util.Random;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.color.block.BlockColors;

public class ModelBlockRenderer {
    private final BlockColors blockColors;
    private static final ThreadLocal<Cache> CACHE;
    
    public ModelBlockRenderer(final BlockColors dkl) {
        this.blockColors = dkl;
    }
    
    public boolean tesselateBlock(final BlockAndTintGetter bqx, final BakedModel elg, final BlockState cee, final BlockPos fx, final PoseStack dfj, final VertexConsumer dfn, final boolean boolean7, final Random random, final long long9, final int integer) {
        final boolean boolean8 = Minecraft.useAmbientOcclusion() && cee.getLightEmission() == 0 && elg.useAmbientOcclusion();
        final Vec3 dck14 = cee.getOffset(bqx, fx);
        dfj.translate(dck14.x, dck14.y, dck14.z);
        try {
            if (boolean8) {
                return this.tesselateWithAO(bqx, elg, cee, fx, dfj, dfn, boolean7, random, long9, integer);
            }
            return this.tesselateWithoutAO(bqx, elg, cee, fx, dfj, dfn, boolean7, random, long9, integer);
        }
        catch (Throwable throwable15) {
            final CrashReport l16 = CrashReport.forThrowable(throwable15, "Tesselating block model");
            final CrashReportCategory m17 = l16.addCategory("Block model being tesselated");
            CrashReportCategory.populateBlockDetails(m17, fx, cee);
            m17.setDetail("Using AO", boolean8);
            throw new ReportedException(l16);
        }
    }
    
    public boolean tesselateWithAO(final BlockAndTintGetter bqx, final BakedModel elg, final BlockState cee, final BlockPos fx, final PoseStack dfj, final VertexConsumer dfn, final boolean boolean7, final Random random, final long long9, final int integer) {
        boolean boolean8 = false;
        final float[] arr14 = new float[Direction.values().length * 2];
        final BitSet bitSet15 = new BitSet(3);
        final AmbientOcclusionFace b16 = new AmbientOcclusionFace();
        for (final Direction gc20 : Direction.values()) {
            random.setSeed(long9);
            final List<BakedQuad> list21 = elg.getQuads(cee, gc20, random);
            if (!list21.isEmpty()) {
                if (!boolean7 || Block.shouldRenderFace(cee, bqx, fx, gc20)) {
                    this.renderModelFaceAO(bqx, cee, fx, dfj, dfn, list21, arr14, bitSet15, b16, integer);
                    boolean8 = true;
                }
            }
        }
        random.setSeed(long9);
        final List<BakedQuad> list22 = elg.getQuads(cee, null, random);
        if (!list22.isEmpty()) {
            this.renderModelFaceAO(bqx, cee, fx, dfj, dfn, list22, arr14, bitSet15, b16, integer);
            boolean8 = true;
        }
        return boolean8;
    }
    
    public boolean tesselateWithoutAO(final BlockAndTintGetter bqx, final BakedModel elg, final BlockState cee, final BlockPos fx, final PoseStack dfj, final VertexConsumer dfn, final boolean boolean7, final Random random, final long long9, final int integer) {
        boolean boolean8 = false;
        final BitSet bitSet14 = new BitSet(3);
        for (final Direction gc18 : Direction.values()) {
            random.setSeed(long9);
            final List<BakedQuad> list19 = elg.getQuads(cee, gc18, random);
            if (!list19.isEmpty()) {
                if (!boolean7 || Block.shouldRenderFace(cee, bqx, fx, gc18)) {
                    final int integer2 = LevelRenderer.getLightColor(bqx, cee, fx.relative(gc18));
                    this.renderModelFaceFlat(bqx, cee, fx, integer2, integer, false, dfj, dfn, list19, bitSet14);
                    boolean8 = true;
                }
            }
        }
        random.setSeed(long9);
        final List<BakedQuad> list20 = elg.getQuads(cee, null, random);
        if (!list20.isEmpty()) {
            this.renderModelFaceFlat(bqx, cee, fx, -1, integer, true, dfj, dfn, list20, bitSet14);
            boolean8 = true;
        }
        return boolean8;
    }
    
    private void renderModelFaceAO(final BlockAndTintGetter bqx, final BlockState cee, final BlockPos fx, final PoseStack dfj, final VertexConsumer dfn, final List<BakedQuad> list, final float[] arr, final BitSet bitSet, final AmbientOcclusionFace b, final int integer) {
        for (final BakedQuad eas13 : list) {
            this.calculateShape(bqx, cee, fx, eas13.getVertices(), eas13.getDirection(), arr, bitSet);
            b.calculate(bqx, cee, fx, eas13.getDirection(), arr, bitSet, eas13.isShade());
            this.putQuadData(bqx, cee, fx, dfn, dfj.last(), eas13, b.brightness[0], b.brightness[1], b.brightness[2], b.brightness[3], b.lightmap[0], b.lightmap[1], b.lightmap[2], b.lightmap[3], integer);
        }
    }
    
    private void putQuadData(final BlockAndTintGetter bqx, final BlockState cee, final BlockPos fx, final VertexConsumer dfn, final PoseStack.Pose a, final BakedQuad eas, final float float7, final float float8, final float float9, final float float10, final int integer11, final int integer12, final int integer13, final int integer14, final int integer15) {
        float float11;
        float float12;
        float float13;
        if (eas.isTinted()) {
            final int integer16 = this.blockColors.getColor(cee, bqx, fx, eas.getTintIndex());
            float11 = (integer16 >> 16 & 0xFF) / 255.0f;
            float12 = (integer16 >> 8 & 0xFF) / 255.0f;
            float13 = (integer16 & 0xFF) / 255.0f;
        }
        else {
            float11 = 1.0f;
            float12 = 1.0f;
            float13 = 1.0f;
        }
        dfn.putBulkData(a, eas, new float[] { float7, float8, float9, float10 }, float11, float12, float13, new int[] { integer11, integer12, integer13, integer14 }, integer15, true);
    }
    
    private void calculateShape(final BlockAndTintGetter bqx, final BlockState cee, final BlockPos fx, final int[] arr, final Direction gc, @Nullable final float[] arr, final BitSet bitSet) {
        float float9 = 32.0f;
        float float10 = 32.0f;
        float float11 = 32.0f;
        float float12 = -32.0f;
        float float13 = -32.0f;
        float float14 = -32.0f;
        for (int integer15 = 0; integer15 < 4; ++integer15) {
            final float float15 = Float.intBitsToFloat(arr[integer15 * 8]);
            final float float16 = Float.intBitsToFloat(arr[integer15 * 8 + 1]);
            final float float17 = Float.intBitsToFloat(arr[integer15 * 8 + 2]);
            float9 = Math.min(float9, float15);
            float10 = Math.min(float10, float16);
            float11 = Math.min(float11, float17);
            float12 = Math.max(float12, float15);
            float13 = Math.max(float13, float16);
            float14 = Math.max(float14, float17);
        }
        if (arr != null) {
            arr[Direction.WEST.get3DDataValue()] = float9;
            arr[Direction.EAST.get3DDataValue()] = float12;
            arr[Direction.DOWN.get3DDataValue()] = float10;
            arr[Direction.UP.get3DDataValue()] = float13;
            arr[Direction.NORTH.get3DDataValue()] = float11;
            arr[Direction.SOUTH.get3DDataValue()] = float14;
            final int integer15 = Direction.values().length;
            arr[Direction.WEST.get3DDataValue() + integer15] = 1.0f - float9;
            arr[Direction.EAST.get3DDataValue() + integer15] = 1.0f - float12;
            arr[Direction.DOWN.get3DDataValue() + integer15] = 1.0f - float10;
            arr[Direction.UP.get3DDataValue() + integer15] = 1.0f - float13;
            arr[Direction.NORTH.get3DDataValue() + integer15] = 1.0f - float11;
            arr[Direction.SOUTH.get3DDataValue() + integer15] = 1.0f - float14;
        }
        final float float18 = 1.0E-4f;
        final float float15 = 0.9999f;
        switch (gc) {
            case DOWN: {
                bitSet.set(1, float9 >= 1.0E-4f || float11 >= 1.0E-4f || float12 <= 0.9999f || float14 <= 0.9999f);
                bitSet.set(0, float10 == float13 && (float10 < 1.0E-4f || cee.isCollisionShapeFullBlock(bqx, fx)));
                break;
            }
            case UP: {
                bitSet.set(1, float9 >= 1.0E-4f || float11 >= 1.0E-4f || float12 <= 0.9999f || float14 <= 0.9999f);
                bitSet.set(0, float10 == float13 && (float13 > 0.9999f || cee.isCollisionShapeFullBlock(bqx, fx)));
                break;
            }
            case NORTH: {
                bitSet.set(1, float9 >= 1.0E-4f || float10 >= 1.0E-4f || float12 <= 0.9999f || float13 <= 0.9999f);
                bitSet.set(0, float11 == float14 && (float11 < 1.0E-4f || cee.isCollisionShapeFullBlock(bqx, fx)));
                break;
            }
            case SOUTH: {
                bitSet.set(1, float9 >= 1.0E-4f || float10 >= 1.0E-4f || float12 <= 0.9999f || float13 <= 0.9999f);
                bitSet.set(0, float11 == float14 && (float14 > 0.9999f || cee.isCollisionShapeFullBlock(bqx, fx)));
                break;
            }
            case WEST: {
                bitSet.set(1, float10 >= 1.0E-4f || float11 >= 1.0E-4f || float13 <= 0.9999f || float14 <= 0.9999f);
                bitSet.set(0, float9 == float12 && (float9 < 1.0E-4f || cee.isCollisionShapeFullBlock(bqx, fx)));
                break;
            }
            case EAST: {
                bitSet.set(1, float10 >= 1.0E-4f || float11 >= 1.0E-4f || float13 <= 0.9999f || float14 <= 0.9999f);
                bitSet.set(0, float9 == float12 && (float12 > 0.9999f || cee.isCollisionShapeFullBlock(bqx, fx)));
                break;
            }
        }
    }
    
    private void renderModelFaceFlat(final BlockAndTintGetter bqx, final BlockState cee, final BlockPos fx, int integer4, final int integer5, final boolean boolean6, final PoseStack dfj, final VertexConsumer dfn, final List<BakedQuad> list, final BitSet bitSet) {
        for (final BakedQuad eas13 : list) {
            if (boolean6) {
                this.calculateShape(bqx, cee, fx, eas13.getVertices(), eas13.getDirection(), null, bitSet);
                final BlockPos fx2 = bitSet.get(0) ? fx.relative(eas13.getDirection()) : fx;
                integer4 = LevelRenderer.getLightColor(bqx, cee, fx2);
            }
            final float float14 = bqx.getShade(eas13.getDirection(), eas13.isShade());
            this.putQuadData(bqx, cee, fx, dfn, dfj.last(), eas13, float14, float14, float14, float14, integer4, integer4, integer4, integer4, integer5);
        }
    }
    
    public void renderModel(final PoseStack.Pose a, final VertexConsumer dfn, @Nullable final BlockState cee, final BakedModel elg, final float float5, final float float6, final float float7, final int integer8, final int integer9) {
        final Random random11 = new Random();
        final long long12 = 42L;
        for (final Direction gc17 : Direction.values()) {
            random11.setSeed(42L);
            renderQuadList(a, dfn, float5, float6, float7, elg.getQuads(cee, gc17, random11), integer8, integer9);
        }
        random11.setSeed(42L);
        renderQuadList(a, dfn, float5, float6, float7, elg.getQuads(cee, null, random11), integer8, integer9);
    }
    
    private static void renderQuadList(final PoseStack.Pose a, final VertexConsumer dfn, final float float3, final float float4, final float float5, final List<BakedQuad> list, final int integer7, final int integer8) {
        for (final BakedQuad eas10 : list) {
            float float6;
            float float7;
            float float8;
            if (eas10.isTinted()) {
                float6 = Mth.clamp(float3, 0.0f, 1.0f);
                float7 = Mth.clamp(float4, 0.0f, 1.0f);
                float8 = Mth.clamp(float5, 0.0f, 1.0f);
            }
            else {
                float6 = 1.0f;
                float7 = 1.0f;
                float8 = 1.0f;
            }
            dfn.putBulkData(a, eas10, float6, float7, float8, integer7, integer8);
        }
    }
    
    public static void enableCaching() {
        ((Cache)ModelBlockRenderer.CACHE.get()).enable();
    }
    
    public static void clearCache() {
        ((Cache)ModelBlockRenderer.CACHE.get()).disable();
    }
    
    static {
        CACHE = ThreadLocal.withInitial(() -> new Cache());
    }
    
    enum AmbientVertexRemap {
        DOWN(0, 1, 2, 3), 
        UP(2, 3, 0, 1), 
        NORTH(3, 0, 1, 2), 
        SOUTH(0, 1, 2, 3), 
        WEST(3, 0, 1, 2), 
        EAST(1, 2, 3, 0);
        
        private final int vert0;
        private final int vert1;
        private final int vert2;
        private final int vert3;
        private static final AmbientVertexRemap[] BY_FACING;
        
        private AmbientVertexRemap(final int integer3, final int integer4, final int integer5, final int integer6) {
            this.vert0 = integer3;
            this.vert1 = integer4;
            this.vert2 = integer5;
            this.vert3 = integer6;
        }
        
        public static AmbientVertexRemap fromFacing(final Direction gc) {
            return AmbientVertexRemap.BY_FACING[gc.get3DDataValue()];
        }
        
        static {
            BY_FACING = Util.<AmbientVertexRemap[]>make(new AmbientVertexRemap[6], (java.util.function.Consumer<AmbientVertexRemap[]>)(arr -> {
                arr[Direction.DOWN.get3DDataValue()] = AmbientVertexRemap.DOWN;
                arr[Direction.UP.get3DDataValue()] = AmbientVertexRemap.UP;
                arr[Direction.NORTH.get3DDataValue()] = AmbientVertexRemap.NORTH;
                arr[Direction.SOUTH.get3DDataValue()] = AmbientVertexRemap.SOUTH;
                arr[Direction.WEST.get3DDataValue()] = AmbientVertexRemap.WEST;
                arr[Direction.EAST.get3DDataValue()] = AmbientVertexRemap.EAST;
            }));
        }
    }
    
    static class Cache {
        private boolean enabled;
        private final Long2IntLinkedOpenHashMap colorCache;
        private final Long2FloatLinkedOpenHashMap brightnessCache;
        
        private Cache() {
            this.colorCache = Util.<Long2IntLinkedOpenHashMap>make((java.util.function.Supplier<Long2IntLinkedOpenHashMap>)(() -> {
                final Long2IntLinkedOpenHashMap long2IntLinkedOpenHashMap2 = new Long2IntLinkedOpenHashMap(100, 0.25f) {
                    protected void rehash(final int integer) {
                    }
                };
                long2IntLinkedOpenHashMap2.defaultReturnValue(Integer.MAX_VALUE);
                return long2IntLinkedOpenHashMap2;
            }));
            this.brightnessCache = Util.<Long2FloatLinkedOpenHashMap>make((java.util.function.Supplier<Long2FloatLinkedOpenHashMap>)(() -> {
                final Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap2 = new Long2FloatLinkedOpenHashMap(100, 0.25f) {
                    protected void rehash(final int integer) {
                    }
                };
                long2FloatLinkedOpenHashMap2.defaultReturnValue(Float.NaN);
                return long2FloatLinkedOpenHashMap2;
            }));
        }
        
        public void enable() {
            this.enabled = true;
        }
        
        public void disable() {
            this.enabled = false;
            this.colorCache.clear();
            this.brightnessCache.clear();
        }
        
        public int getLightColor(final BlockState cee, final BlockAndTintGetter bqx, final BlockPos fx) {
            final long long5 = fx.asLong();
            if (this.enabled) {
                final int integer7 = this.colorCache.get(long5);
                if (integer7 != Integer.MAX_VALUE) {
                    return integer7;
                }
            }
            final int integer7 = LevelRenderer.getLightColor(bqx, cee, fx);
            if (this.enabled) {
                if (this.colorCache.size() == 100) {
                    this.colorCache.removeFirstInt();
                }
                this.colorCache.put(long5, integer7);
            }
            return integer7;
        }
        
        public float getShadeBrightness(final BlockState cee, final BlockAndTintGetter bqx, final BlockPos fx) {
            final long long5 = fx.asLong();
            if (this.enabled) {
                final float float7 = this.brightnessCache.get(long5);
                if (!Float.isNaN(float7)) {
                    return float7;
                }
            }
            final float float7 = cee.getShadeBrightness(bqx, fx);
            if (this.enabled) {
                if (this.brightnessCache.size() == 100) {
                    this.brightnessCache.removeFirstFloat();
                }
                this.brightnessCache.put(long5, float7);
            }
            return float7;
        }
    }
    
    class AmbientOcclusionFace {
        private final float[] brightness;
        private final int[] lightmap;
        
        public AmbientOcclusionFace() {
            this.brightness = new float[4];
            this.lightmap = new int[4];
        }
        
        public void calculate(final BlockAndTintGetter bqx, final BlockState cee, final BlockPos fx, final Direction gc, final float[] arr, final BitSet bitSet, final boolean boolean7) {
            final BlockPos fx2 = bitSet.get(0) ? fx.relative(gc) : fx;
            final AdjacencyInfo a10 = AdjacencyInfo.fromFacing(gc);
            final BlockPos.MutableBlockPos a11 = new BlockPos.MutableBlockPos();
            final Cache d12 = (Cache)ModelBlockRenderer.CACHE.get();
            a11.setWithOffset(fx2, a10.corners[0]);
            final BlockState cee2 = bqx.getBlockState(a11);
            final int integer14 = d12.getLightColor(cee2, bqx, a11);
            final float float15 = d12.getShadeBrightness(cee2, bqx, a11);
            a11.setWithOffset(fx2, a10.corners[1]);
            final BlockState cee3 = bqx.getBlockState(a11);
            final int integer15 = d12.getLightColor(cee3, bqx, a11);
            final float float16 = d12.getShadeBrightness(cee3, bqx, a11);
            a11.setWithOffset(fx2, a10.corners[2]);
            final BlockState cee4 = bqx.getBlockState(a11);
            final int integer16 = d12.getLightColor(cee4, bqx, a11);
            final float float17 = d12.getShadeBrightness(cee4, bqx, a11);
            a11.setWithOffset(fx2, a10.corners[3]);
            final BlockState cee5 = bqx.getBlockState(a11);
            final int integer17 = d12.getLightColor(cee5, bqx, a11);
            final float float18 = d12.getShadeBrightness(cee5, bqx, a11);
            a11.setWithOffset(fx2, a10.corners[0]).move(gc);
            final boolean boolean8 = bqx.getBlockState(a11).getLightBlock(bqx, a11) == 0;
            a11.setWithOffset(fx2, a10.corners[1]).move(gc);
            final boolean boolean9 = bqx.getBlockState(a11).getLightBlock(bqx, a11) == 0;
            a11.setWithOffset(fx2, a10.corners[2]).move(gc);
            final boolean boolean10 = bqx.getBlockState(a11).getLightBlock(bqx, a11) == 0;
            a11.setWithOffset(fx2, a10.corners[3]).move(gc);
            final boolean boolean11 = bqx.getBlockState(a11).getLightBlock(bqx, a11) == 0;
            float float19;
            int integer18;
            if (boolean10 || boolean8) {
                a11.setWithOffset(fx2, a10.corners[0]).move(a10.corners[2]);
                final BlockState cee6 = bqx.getBlockState(a11);
                float19 = d12.getShadeBrightness(cee6, bqx, a11);
                integer18 = d12.getLightColor(cee6, bqx, a11);
            }
            else {
                float19 = float15;
                integer18 = integer14;
            }
            float float20;
            int integer19;
            if (boolean11 || boolean8) {
                a11.setWithOffset(fx2, a10.corners[0]).move(a10.corners[3]);
                final BlockState cee6 = bqx.getBlockState(a11);
                float20 = d12.getShadeBrightness(cee6, bqx, a11);
                integer19 = d12.getLightColor(cee6, bqx, a11);
            }
            else {
                float20 = float15;
                integer19 = integer14;
            }
            float float21;
            int integer20;
            if (boolean10 || boolean9) {
                a11.setWithOffset(fx2, a10.corners[1]).move(a10.corners[2]);
                final BlockState cee6 = bqx.getBlockState(a11);
                float21 = d12.getShadeBrightness(cee6, bqx, a11);
                integer20 = d12.getLightColor(cee6, bqx, a11);
            }
            else {
                float21 = float15;
                integer20 = integer14;
            }
            float float22;
            int integer21;
            if (boolean11 || boolean9) {
                a11.setWithOffset(fx2, a10.corners[1]).move(a10.corners[3]);
                final BlockState cee6 = bqx.getBlockState(a11);
                float22 = d12.getShadeBrightness(cee6, bqx, a11);
                integer21 = d12.getLightColor(cee6, bqx, a11);
            }
            else {
                float22 = float15;
                integer21 = integer14;
            }
            int integer22 = d12.getLightColor(cee, bqx, fx);
            a11.setWithOffset(fx, gc);
            final BlockState cee7 = bqx.getBlockState(a11);
            if (bitSet.get(0) || !cee7.isSolidRender(bqx, a11)) {
                integer22 = d12.getLightColor(cee7, bqx, a11);
            }
            final float float23 = bitSet.get(0) ? d12.getShadeBrightness(bqx.getBlockState(fx2), bqx, fx2) : d12.getShadeBrightness(bqx.getBlockState(fx), bqx, fx);
            final AmbientVertexRemap c40 = AmbientVertexRemap.fromFacing(gc);
            if (!bitSet.get(1) || !a10.doNonCubicWeight) {
                final float float24 = (float18 + float15 + float20 + float23) * 0.25f;
                final float float25 = (float17 + float15 + float19 + float23) * 0.25f;
                final float float26 = (float17 + float16 + float21 + float23) * 0.25f;
                final float float27 = (float18 + float16 + float22 + float23) * 0.25f;
                this.lightmap[c40.vert0] = this.blend(integer17, integer14, integer19, integer22);
                this.lightmap[c40.vert1] = this.blend(integer16, integer14, integer18, integer22);
                this.lightmap[c40.vert2] = this.blend(integer16, integer15, integer20, integer22);
                this.lightmap[c40.vert3] = this.blend(integer17, integer15, integer21, integer22);
                this.brightness[c40.vert0] = float24;
                this.brightness[c40.vert1] = float25;
                this.brightness[c40.vert2] = float26;
                this.brightness[c40.vert3] = float27;
            }
            else {
                final float float24 = (float18 + float15 + float20 + float23) * 0.25f;
                final float float25 = (float17 + float15 + float19 + float23) * 0.25f;
                final float float26 = (float17 + float16 + float21 + float23) * 0.25f;
                final float float27 = (float18 + float16 + float22 + float23) * 0.25f;
                final float float28 = arr[a10.vert0Weights[0].shape] * arr[a10.vert0Weights[1].shape];
                final float float29 = arr[a10.vert0Weights[2].shape] * arr[a10.vert0Weights[3].shape];
                final float float30 = arr[a10.vert0Weights[4].shape] * arr[a10.vert0Weights[5].shape];
                final float float31 = arr[a10.vert0Weights[6].shape] * arr[a10.vert0Weights[7].shape];
                final float float32 = arr[a10.vert1Weights[0].shape] * arr[a10.vert1Weights[1].shape];
                final float float33 = arr[a10.vert1Weights[2].shape] * arr[a10.vert1Weights[3].shape];
                final float float34 = arr[a10.vert1Weights[4].shape] * arr[a10.vert1Weights[5].shape];
                final float float35 = arr[a10.vert1Weights[6].shape] * arr[a10.vert1Weights[7].shape];
                final float float36 = arr[a10.vert2Weights[0].shape] * arr[a10.vert2Weights[1].shape];
                final float float37 = arr[a10.vert2Weights[2].shape] * arr[a10.vert2Weights[3].shape];
                final float float38 = arr[a10.vert2Weights[4].shape] * arr[a10.vert2Weights[5].shape];
                final float float39 = arr[a10.vert2Weights[6].shape] * arr[a10.vert2Weights[7].shape];
                final float float40 = arr[a10.vert3Weights[0].shape] * arr[a10.vert3Weights[1].shape];
                final float float41 = arr[a10.vert3Weights[2].shape] * arr[a10.vert3Weights[3].shape];
                final float float42 = arr[a10.vert3Weights[4].shape] * arr[a10.vert3Weights[5].shape];
                final float float43 = arr[a10.vert3Weights[6].shape] * arr[a10.vert3Weights[7].shape];
                this.brightness[c40.vert0] = float24 * float28 + float25 * float29 + float26 * float30 + float27 * float31;
                this.brightness[c40.vert1] = float24 * float32 + float25 * float33 + float26 * float34 + float27 * float35;
                this.brightness[c40.vert2] = float24 * float36 + float25 * float37 + float26 * float38 + float27 * float39;
                this.brightness[c40.vert3] = float24 * float40 + float25 * float41 + float26 * float42 + float27 * float43;
                final int integer23 = this.blend(integer17, integer14, integer19, integer22);
                final int integer24 = this.blend(integer16, integer14, integer18, integer22);
                final int integer25 = this.blend(integer16, integer15, integer20, integer22);
                final int integer26 = this.blend(integer17, integer15, integer21, integer22);
                this.lightmap[c40.vert0] = this.blend(integer23, integer24, integer25, integer26, float28, float29, float30, float31);
                this.lightmap[c40.vert1] = this.blend(integer23, integer24, integer25, integer26, float32, float33, float34, float35);
                this.lightmap[c40.vert2] = this.blend(integer23, integer24, integer25, integer26, float36, float37, float38, float39);
                this.lightmap[c40.vert3] = this.blend(integer23, integer24, integer25, integer26, float40, float41, float42, float43);
            }
            final float float24 = bqx.getShade(gc, boolean7);
            for (int integer27 = 0; integer27 < this.brightness.length; ++integer27) {
                final float[] brightness = this.brightness;
                final int n = integer27;
                brightness[n] *= float24;
            }
        }
        
        private int blend(int integer1, int integer2, int integer3, final int integer4) {
            if (integer1 == 0) {
                integer1 = integer4;
            }
            if (integer2 == 0) {
                integer2 = integer4;
            }
            if (integer3 == 0) {
                integer3 = integer4;
            }
            return integer1 + integer2 + integer3 + integer4 >> 2 & 0xFF00FF;
        }
        
        private int blend(final int integer1, final int integer2, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
            final int integer5 = (int)((integer1 >> 16 & 0xFF) * float5 + (integer2 >> 16 & 0xFF) * float6 + (integer3 >> 16 & 0xFF) * float7 + (integer4 >> 16 & 0xFF) * float8) & 0xFF;
            final int integer6 = (int)((integer1 & 0xFF) * float5 + (integer2 & 0xFF) * float6 + (integer3 & 0xFF) * float7 + (integer4 & 0xFF) * float8) & 0xFF;
            return integer5 << 16 | integer6;
        }
    }
    
    public enum SizeInfo {
        DOWN(Direction.DOWN, false), 
        UP(Direction.UP, false), 
        NORTH(Direction.NORTH, false), 
        SOUTH(Direction.SOUTH, false), 
        WEST(Direction.WEST, false), 
        EAST(Direction.EAST, false), 
        FLIP_DOWN(Direction.DOWN, true), 
        FLIP_UP(Direction.UP, true), 
        FLIP_NORTH(Direction.NORTH, true), 
        FLIP_SOUTH(Direction.SOUTH, true), 
        FLIP_WEST(Direction.WEST, true), 
        FLIP_EAST(Direction.EAST, true);
        
        private final int shape;
        
        private SizeInfo(final Direction gc, final boolean boolean4) {
            this.shape = gc.get3DDataValue() + (boolean4 ? Direction.values().length : 0);
        }
    }
    
    public enum AdjacencyInfo {
        DOWN(new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }, 0.5f, true, new SizeInfo[] { SizeInfo.FLIP_WEST, SizeInfo.SOUTH, SizeInfo.FLIP_WEST, SizeInfo.FLIP_SOUTH, SizeInfo.WEST, SizeInfo.FLIP_SOUTH, SizeInfo.WEST, SizeInfo.SOUTH }, new SizeInfo[] { SizeInfo.FLIP_WEST, SizeInfo.NORTH, SizeInfo.FLIP_WEST, SizeInfo.FLIP_NORTH, SizeInfo.WEST, SizeInfo.FLIP_NORTH, SizeInfo.WEST, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.FLIP_EAST, SizeInfo.NORTH, SizeInfo.FLIP_EAST, SizeInfo.FLIP_NORTH, SizeInfo.EAST, SizeInfo.FLIP_NORTH, SizeInfo.EAST, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.FLIP_EAST, SizeInfo.SOUTH, SizeInfo.FLIP_EAST, SizeInfo.FLIP_SOUTH, SizeInfo.EAST, SizeInfo.FLIP_SOUTH, SizeInfo.EAST, SizeInfo.SOUTH }), 
        UP(new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH }, 1.0f, true, new SizeInfo[] { SizeInfo.EAST, SizeInfo.SOUTH, SizeInfo.EAST, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_EAST, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_EAST, SizeInfo.SOUTH }, new SizeInfo[] { SizeInfo.EAST, SizeInfo.NORTH, SizeInfo.EAST, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_EAST, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_EAST, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.WEST, SizeInfo.NORTH, SizeInfo.WEST, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_WEST, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_WEST, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.WEST, SizeInfo.SOUTH, SizeInfo.WEST, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_WEST, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_WEST, SizeInfo.SOUTH }), 
        NORTH(new Direction[] { Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST }, 0.8f, true, new SizeInfo[] { SizeInfo.UP, SizeInfo.FLIP_WEST, SizeInfo.UP, SizeInfo.WEST, SizeInfo.FLIP_UP, SizeInfo.WEST, SizeInfo.FLIP_UP, SizeInfo.FLIP_WEST }, new SizeInfo[] { SizeInfo.UP, SizeInfo.FLIP_EAST, SizeInfo.UP, SizeInfo.EAST, SizeInfo.FLIP_UP, SizeInfo.EAST, SizeInfo.FLIP_UP, SizeInfo.FLIP_EAST }, new SizeInfo[] { SizeInfo.DOWN, SizeInfo.FLIP_EAST, SizeInfo.DOWN, SizeInfo.EAST, SizeInfo.FLIP_DOWN, SizeInfo.EAST, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_EAST }, new SizeInfo[] { SizeInfo.DOWN, SizeInfo.FLIP_WEST, SizeInfo.DOWN, SizeInfo.WEST, SizeInfo.FLIP_DOWN, SizeInfo.WEST, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_WEST }), 
        SOUTH(new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP }, 0.8f, true, new SizeInfo[] { SizeInfo.UP, SizeInfo.FLIP_WEST, SizeInfo.FLIP_UP, SizeInfo.FLIP_WEST, SizeInfo.FLIP_UP, SizeInfo.WEST, SizeInfo.UP, SizeInfo.WEST }, new SizeInfo[] { SizeInfo.DOWN, SizeInfo.FLIP_WEST, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_WEST, SizeInfo.FLIP_DOWN, SizeInfo.WEST, SizeInfo.DOWN, SizeInfo.WEST }, new SizeInfo[] { SizeInfo.DOWN, SizeInfo.FLIP_EAST, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_EAST, SizeInfo.FLIP_DOWN, SizeInfo.EAST, SizeInfo.DOWN, SizeInfo.EAST }, new SizeInfo[] { SizeInfo.UP, SizeInfo.FLIP_EAST, SizeInfo.FLIP_UP, SizeInfo.FLIP_EAST, SizeInfo.FLIP_UP, SizeInfo.EAST, SizeInfo.UP, SizeInfo.EAST }), 
        WEST(new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH }, 0.6f, true, new SizeInfo[] { SizeInfo.UP, SizeInfo.SOUTH, SizeInfo.UP, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_UP, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_UP, SizeInfo.SOUTH }, new SizeInfo[] { SizeInfo.UP, SizeInfo.NORTH, SizeInfo.UP, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_UP, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_UP, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.DOWN, SizeInfo.NORTH, SizeInfo.DOWN, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_NORTH, SizeInfo.FLIP_DOWN, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.DOWN, SizeInfo.SOUTH, SizeInfo.DOWN, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_SOUTH, SizeInfo.FLIP_DOWN, SizeInfo.SOUTH }), 
        EAST(new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH }, 0.6f, true, new SizeInfo[] { SizeInfo.FLIP_DOWN, SizeInfo.SOUTH, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_SOUTH, SizeInfo.DOWN, SizeInfo.FLIP_SOUTH, SizeInfo.DOWN, SizeInfo.SOUTH }, new SizeInfo[] { SizeInfo.FLIP_DOWN, SizeInfo.NORTH, SizeInfo.FLIP_DOWN, SizeInfo.FLIP_NORTH, SizeInfo.DOWN, SizeInfo.FLIP_NORTH, SizeInfo.DOWN, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.FLIP_UP, SizeInfo.NORTH, SizeInfo.FLIP_UP, SizeInfo.FLIP_NORTH, SizeInfo.UP, SizeInfo.FLIP_NORTH, SizeInfo.UP, SizeInfo.NORTH }, new SizeInfo[] { SizeInfo.FLIP_UP, SizeInfo.SOUTH, SizeInfo.FLIP_UP, SizeInfo.FLIP_SOUTH, SizeInfo.UP, SizeInfo.FLIP_SOUTH, SizeInfo.UP, SizeInfo.SOUTH });
        
        private final Direction[] corners;
        private final boolean doNonCubicWeight;
        private final SizeInfo[] vert0Weights;
        private final SizeInfo[] vert1Weights;
        private final SizeInfo[] vert2Weights;
        private final SizeInfo[] vert3Weights;
        private static final AdjacencyInfo[] BY_FACING;
        
        private AdjacencyInfo(final Direction[] arr, final float float4, final boolean boolean5, final SizeInfo[] arr6, final SizeInfo[] arr7, final SizeInfo[] arr8, final SizeInfo[] arr9) {
            this.corners = arr;
            this.doNonCubicWeight = boolean5;
            this.vert0Weights = arr6;
            this.vert1Weights = arr7;
            this.vert2Weights = arr8;
            this.vert3Weights = arr9;
        }
        
        public static AdjacencyInfo fromFacing(final Direction gc) {
            return AdjacencyInfo.BY_FACING[gc.get3DDataValue()];
        }
        
        static {
            BY_FACING = Util.<AdjacencyInfo[]>make(new AdjacencyInfo[6], (java.util.function.Consumer<AdjacencyInfo[]>)(arr -> {
                arr[Direction.DOWN.get3DDataValue()] = AdjacencyInfo.DOWN;
                arr[Direction.UP.get3DDataValue()] = AdjacencyInfo.UP;
                arr[Direction.NORTH.get3DDataValue()] = AdjacencyInfo.NORTH;
                arr[Direction.SOUTH.get3DDataValue()] = AdjacencyInfo.SOUTH;
                arr[Direction.WEST.get3DDataValue()] = AdjacencyInfo.WEST;
                arr[Direction.EAST.get3DDataValue()] = AdjacencyInfo.EAST;
            }));
        }
    }
}
