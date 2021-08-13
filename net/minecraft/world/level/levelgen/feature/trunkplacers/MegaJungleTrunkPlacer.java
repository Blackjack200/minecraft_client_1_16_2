package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import java.util.Collection;
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

public class MegaJungleTrunkPlacer extends GiantTrunkPlacer {
    public static final Codec<MegaJungleTrunkPlacer> CODEC;
    
    public MegaJungleTrunkPlacer(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.MEGA_JUNGLE_TRUNK_PLACER;
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        final List<FoliagePlacer.FoliageAttachment> list9 = (List<FoliagePlacer.FoliageAttachment>)Lists.newArrayList();
        list9.addAll((Collection)super.placeTrunk(bry, random, integer, fx, set, cqx, cmw));
        for (int integer2 = integer - 2 - random.nextInt(4); integer2 > integer / 2; integer2 -= 2 + random.nextInt(4)) {
            final float float11 = random.nextFloat() * 6.2831855f;
            int integer3 = 0;
            int integer4 = 0;
            for (int integer5 = 0; integer5 < 5; ++integer5) {
                integer3 = (int)(1.5f + Mth.cos(float11) * integer5);
                integer4 = (int)(1.5f + Mth.sin(float11) * integer5);
                final BlockPos fx2 = fx.offset(integer3, integer2 - 3 + integer5 / 2, integer4);
                TrunkPlacer.placeLog(bry, random, fx2, set, cqx, cmw);
            }
            list9.add(new FoliagePlacer.FoliageAttachment(fx.offset(integer3, integer2, integer4), -2, false));
        }
        return list9;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> TrunkPlacer.<TrunkPlacer>trunkPlacerParts((RecordCodecBuilder.Instance<TrunkPlacer>)instance).apply((Applicative)instance, MegaJungleTrunkPlacer::new));
    }
}
