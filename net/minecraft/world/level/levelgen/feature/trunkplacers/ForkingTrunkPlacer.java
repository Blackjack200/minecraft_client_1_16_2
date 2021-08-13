package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import com.google.common.collect.Lists;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import com.mojang.serialization.Codec;

public class ForkingTrunkPlacer extends TrunkPlacer {
    public static final Codec<ForkingTrunkPlacer> CODEC;
    
    public ForkingTrunkPlacer(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.FORKING_TRUNK_PLACER;
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        TrunkPlacer.setDirtAt(bry, fx.below());
        final List<FoliagePlacer.FoliageAttachment> list9 = (List<FoliagePlacer.FoliageAttachment>)Lists.newArrayList();
        final Direction gc10 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        final int integer2 = integer - random.nextInt(4) - 1;
        int integer3 = 3 - random.nextInt(3);
        final BlockPos.MutableBlockPos a13 = new BlockPos.MutableBlockPos();
        int integer4 = fx.getX();
        int integer5 = fx.getZ();
        int integer6 = 0;
        for (int integer7 = 0; integer7 < integer; ++integer7) {
            final int integer8 = fx.getY() + integer7;
            if (integer7 >= integer2 && integer3 > 0) {
                integer4 += gc10.getStepX();
                integer5 += gc10.getStepZ();
                --integer3;
            }
            if (TrunkPlacer.placeLog(bry, random, a13.set(integer4, integer8, integer5), set, cqx, cmw)) {
                integer6 = integer8 + 1;
            }
        }
        list9.add(new FoliagePlacer.FoliageAttachment(new BlockPos(integer4, integer6, integer5), 1, false));
        integer4 = fx.getX();
        integer5 = fx.getZ();
        final Direction gc11 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        if (gc11 != gc10) {
            final int integer8 = integer2 - random.nextInt(2) - 1;
            int integer9 = 1 + random.nextInt(3);
            integer6 = 0;
            for (int integer10 = integer8; integer10 < integer && integer9 > 0; ++integer10, --integer9) {
                if (integer10 >= 1) {
                    final int integer11 = fx.getY() + integer10;
                    integer4 += gc11.getStepX();
                    integer5 += gc11.getStepZ();
                    if (TrunkPlacer.placeLog(bry, random, a13.set(integer4, integer11, integer5), set, cqx, cmw)) {
                        integer6 = integer11 + 1;
                    }
                }
            }
            if (integer6 > 1) {
                list9.add(new FoliagePlacer.FoliageAttachment(new BlockPos(integer4, integer6, integer5), 0, false));
            }
        }
        return list9;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> TrunkPlacer.<TrunkPlacer>trunkPlacerParts((RecordCodecBuilder.Instance<TrunkPlacer>)instance).apply((Applicative)instance, ForkingTrunkPlacer::new));
    }
}
