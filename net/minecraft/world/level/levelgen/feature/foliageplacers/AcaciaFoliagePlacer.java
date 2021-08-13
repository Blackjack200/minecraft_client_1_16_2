package net.minecraft.world.level.levelgen.feature.foliageplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public class AcaciaFoliagePlacer extends FoliagePlacer {
    public static final Codec<AcaciaFoliagePlacer> CODEC;
    
    public AcaciaFoliagePlacer(final UniformInt aft1, final UniformInt aft2) {
        super(aft1, aft2);
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        final boolean boolean12 = b.doubleTrunk();
        final BlockPos fx13 = b.foliagePos().above(integer9);
        this.placeLeavesRow(bry, random, cmw, fx13, integer7 + b.radiusOffset(), set, -1 - integer6, boolean12, cqx);
        this.placeLeavesRow(bry, random, cmw, fx13, integer7 - 1, set, -integer6, boolean12, cqx);
        this.placeLeavesRow(bry, random, cmw, fx13, integer7 + b.radiusOffset() - 1, set, 0, boolean12, cqx);
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return 0;
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        if (integer3 == 0) {
            return (integer2 > 1 || integer4 > 1) && integer2 != 0 && integer4 != 0;
        }
        return integer2 == integer5 && integer4 == integer5 && integer5 > 0;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> FoliagePlacer.<FoliagePlacer>foliagePlacerParts((RecordCodecBuilder.Instance<FoliagePlacer>)instance).apply((Applicative)instance, AcaciaFoliagePlacer::new));
    }
}
