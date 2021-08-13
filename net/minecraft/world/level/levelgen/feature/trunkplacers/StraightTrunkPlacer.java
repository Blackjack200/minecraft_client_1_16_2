package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import com.mojang.serialization.Codec;

public class StraightTrunkPlacer extends TrunkPlacer {
    public static final Codec<StraightTrunkPlacer> CODEC;
    
    public StraightTrunkPlacer(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.STRAIGHT_TRUNK_PLACER;
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        TrunkPlacer.setDirtAt(bry, fx.below());
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            TrunkPlacer.placeLog(bry, random, fx.above(integer2), set, cqx, cmw);
        }
        return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(fx.above(integer), 0, false));
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> TrunkPlacer.<TrunkPlacer>trunkPlacerParts((RecordCodecBuilder.Instance<TrunkPlacer>)instance).apply((Applicative)instance, StraightTrunkPlacer::new));
    }
}
