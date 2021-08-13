package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public class FancyFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<FancyFoliagePlacer> CODEC;
    
    public FancyFoliagePlacer(final UniformInt aft1, final UniformInt aft2, final int integer) {
        super(aft1, aft2, integer);
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.FANCY_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        for (int integer10 = integer9; integer10 >= integer9 - integer6; --integer10) {
            final int integer11 = integer7 + ((integer10 != integer9 && integer10 != integer9 - integer6) ? 1 : 0);
            this.placeLeavesRow(bry, random, cmw, b.foliagePos(), integer11, set, integer10, b.doubleTrunk(), cqx);
        }
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return Mth.square(integer2 + 0.5f) + Mth.square(integer4 + 0.5f) > integer5 * integer5;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> BlobFoliagePlacer.<BlobFoliagePlacer>blobParts((RecordCodecBuilder.Instance<BlobFoliagePlacer>)instance).apply((Applicative)instance, FancyFoliagePlacer::new));
    }
}
