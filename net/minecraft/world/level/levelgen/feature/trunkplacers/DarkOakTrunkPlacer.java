package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
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

public class DarkOakTrunkPlacer extends TrunkPlacer {
    public static final Codec<DarkOakTrunkPlacer> CODEC;
    
    public DarkOakTrunkPlacer(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        final List<FoliagePlacer.FoliageAttachment> list9 = (List<FoliagePlacer.FoliageAttachment>)Lists.newArrayList();
        final BlockPos fx2 = fx.below();
        TrunkPlacer.setDirtAt(bry, fx2);
        TrunkPlacer.setDirtAt(bry, fx2.east());
        TrunkPlacer.setDirtAt(bry, fx2.south());
        TrunkPlacer.setDirtAt(bry, fx2.south().east());
        final Direction gc11 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        final int integer2 = integer - random.nextInt(4);
        int integer3 = 2 - random.nextInt(3);
        final int integer4 = fx.getX();
        final int integer5 = fx.getY();
        final int integer6 = fx.getZ();
        int integer7 = integer4;
        int integer8 = integer6;
        final int integer9 = integer5 + integer - 1;
        for (int integer10 = 0; integer10 < integer; ++integer10) {
            if (integer10 >= integer2 && integer3 > 0) {
                integer7 += gc11.getStepX();
                integer8 += gc11.getStepZ();
                --integer3;
            }
            final int integer11 = integer5 + integer10;
            final BlockPos fx3 = new BlockPos(integer7, integer11, integer8);
            if (TreeFeature.isAirOrLeaves(bry, fx3)) {
                TrunkPlacer.placeLog(bry, random, fx3, set, cqx, cmw);
                TrunkPlacer.placeLog(bry, random, fx3.east(), set, cqx, cmw);
                TrunkPlacer.placeLog(bry, random, fx3.south(), set, cqx, cmw);
                TrunkPlacer.placeLog(bry, random, fx3.east().south(), set, cqx, cmw);
            }
        }
        list9.add(new FoliagePlacer.FoliageAttachment(new BlockPos(integer7, integer9, integer8), 0, true));
        for (int integer10 = -1; integer10 <= 2; ++integer10) {
            for (int integer11 = -1; integer11 <= 2; ++integer11) {
                if (integer10 < 0 || integer10 > 1 || integer11 < 0 || integer11 > 1) {
                    if (random.nextInt(3) <= 0) {
                        for (int integer12 = random.nextInt(3) + 2, integer13 = 0; integer13 < integer12; ++integer13) {
                            TrunkPlacer.placeLog(bry, random, new BlockPos(integer4 + integer10, integer9 - integer13 - 1, integer6 + integer11), set, cqx, cmw);
                        }
                        list9.add(new FoliagePlacer.FoliageAttachment(new BlockPos(integer7 + integer10, integer9, integer8 + integer11), 0, false));
                    }
                }
            }
        }
        return list9;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> TrunkPlacer.<TrunkPlacer>trunkPlacerParts((RecordCodecBuilder.Instance<TrunkPlacer>)instance).apply((Applicative)instance, DarkOakTrunkPlacer::new));
    }
}
