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

public class MegaJungleFoliagePlacer extends FoliagePlacer {
    public static final Codec<MegaJungleFoliagePlacer> CODEC;
    protected final int height;
    
    public MegaJungleFoliagePlacer(final UniformInt aft1, final UniformInt aft2, final int integer) {
        super(aft1, aft2);
        this.height = integer;
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.MEGA_JUNGLE_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        for (int integer10 = b.doubleTrunk() ? integer6 : (1 + random.nextInt(2)), integer11 = integer9; integer11 >= integer9 - integer10; --integer11) {
            final int integer12 = integer7 + b.radiusOffset() + 1 - integer11;
            this.placeLeavesRow(bry, random, cmw, b.foliagePos(), integer12, set, integer11, b.doubleTrunk(), cqx);
        }
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return this.height;
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return integer2 + integer4 >= 7 || integer2 * integer2 + integer4 * integer4 > integer5 * integer5;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> FoliagePlacer.<FoliagePlacer>foliagePlacerParts((RecordCodecBuilder.Instance<FoliagePlacer>)instance).and((App)Codec.intRange(0, 16).fieldOf("height").forGetter(cnk -> cnk.height)).apply((Applicative)instance, MegaJungleFoliagePlacer::new));
    }
}
