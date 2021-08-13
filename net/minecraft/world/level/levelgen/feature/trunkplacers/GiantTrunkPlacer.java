package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
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

public class GiantTrunkPlacer extends TrunkPlacer {
    public static final Codec<GiantTrunkPlacer> CODEC;
    
    public GiantTrunkPlacer(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.GIANT_TRUNK_PLACER;
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        final BlockPos fx2 = fx.below();
        TrunkPlacer.setDirtAt(bry, fx2);
        TrunkPlacer.setDirtAt(bry, fx2.east());
        TrunkPlacer.setDirtAt(bry, fx2.south());
        TrunkPlacer.setDirtAt(bry, fx2.south().east());
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            placeLogIfFreeWithOffset(bry, random, a10, set, cqx, cmw, fx, 0, integer2, 0);
            if (integer2 < integer - 1) {
                placeLogIfFreeWithOffset(bry, random, a10, set, cqx, cmw, fx, 1, integer2, 0);
                placeLogIfFreeWithOffset(bry, random, a10, set, cqx, cmw, fx, 1, integer2, 1);
                placeLogIfFreeWithOffset(bry, random, a10, set, cqx, cmw, fx, 0, integer2, 1);
            }
        }
        return (List<FoliagePlacer.FoliageAttachment>)ImmutableList.of(new FoliagePlacer.FoliageAttachment(fx.above(integer), 0, true));
    }
    
    private static void placeLogIfFreeWithOffset(final LevelSimulatedRW bry, final Random random, final BlockPos.MutableBlockPos a, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw, final BlockPos fx, final int integer8, final int integer9, final int integer10) {
        a.setWithOffset(fx, integer8, integer9, integer10);
        TrunkPlacer.placeLogIfFree(bry, random, a, set, cqx, cmw);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> TrunkPlacer.<TrunkPlacer>trunkPlacerParts((RecordCodecBuilder.Instance<TrunkPlacer>)instance).apply((Applicative)instance, GiantTrunkPlacer::new));
    }
}
