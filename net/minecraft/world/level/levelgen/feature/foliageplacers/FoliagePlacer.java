package net.minecraft.world.level.levelgen.feature.foliageplacers;

import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public abstract class FoliagePlacer {
    public static final Codec<FoliagePlacer> CODEC;
    protected final UniformInt radius;
    protected final UniformInt offset;
    
    protected static <P extends FoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, UniformInt, UniformInt> foliagePlacerParts(final RecordCodecBuilder.Instance<P> instance) {
        return (Products.P2<RecordCodecBuilder.Mu<P>, UniformInt, UniformInt>)instance.group((App)UniformInt.codec(0, 8, 8).fieldOf("radius").forGetter(cni -> cni.radius), (App)UniformInt.codec(0, 8, 8).fieldOf("offset").forGetter(cni -> cni.offset));
    }
    
    public FoliagePlacer(final UniformInt aft1, final UniformInt aft2) {
        this.radius = aft1;
        this.offset = aft2;
    }
    
    protected abstract FoliagePlacerType<?> type();
    
    public void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final BoundingBox cqx) {
        this.createFoliage(bry, random, cmw, integer4, b, integer6, integer7, set, this.offset(random), cqx);
    }
    
    protected abstract void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx);
    
    public abstract int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw);
    
    public int foliageRadius(final Random random, final int integer) {
        return this.radius.sample(random);
    }
    
    private int offset(final Random random) {
        return this.offset.sample(random);
    }
    
    protected abstract boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6);
    
    protected boolean shouldSkipLocationSigned(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        int integer6;
        int integer7;
        if (boolean6) {
            integer6 = Math.min(Math.abs(integer2), Math.abs(integer2 - 1));
            integer7 = Math.min(Math.abs(integer4), Math.abs(integer4 - 1));
        }
        else {
            integer6 = Math.abs(integer2);
            integer7 = Math.abs(integer4);
        }
        return this.shouldSkipLocation(random, integer6, integer3, integer7, integer5, boolean6);
    }
    
    protected void placeLeavesRow(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final BlockPos fx, final int integer5, final Set<BlockPos> set, final int integer7, final boolean boolean8, final BoundingBox cqx) {
        final int integer8 = boolean8 ? 1 : 0;
        final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
        for (int integer9 = -integer5; integer9 <= integer5 + integer8; ++integer9) {
            for (int integer10 = -integer5; integer10 <= integer5 + integer8; ++integer10) {
                if (!this.shouldSkipLocationSigned(random, integer9, integer7, integer10, integer5, boolean8)) {
                    a12.setWithOffset(fx, integer9, integer7, integer10);
                    if (TreeFeature.validTreePos(bry, a12)) {
                        bry.setBlock(a12, cmw.leavesProvider.getState(random, a12), 19);
                        cqx.expand(new BoundingBox(a12, a12));
                        set.add(a12.immutable());
                    }
                }
            }
        }
    }
    
    static {
        CODEC = Registry.FOLIAGE_PLACER_TYPES.dispatch(FoliagePlacer::type, FoliagePlacerType::codec);
    }
    
    public static final class FoliageAttachment {
        private final BlockPos foliagePos;
        private final int radiusOffset;
        private final boolean doubleTrunk;
        
        public FoliageAttachment(final BlockPos fx, final int integer, final boolean boolean3) {
            this.foliagePos = fx;
            this.radiusOffset = integer;
            this.doubleTrunk = boolean3;
        }
        
        public BlockPos foliagePos() {
            return this.foliagePos;
        }
        
        public int radiusOffset() {
            return this.radiusOffset;
        }
        
        public boolean doubleTrunk() {
            return this.doubleTrunk;
        }
    }
}
