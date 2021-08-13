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

public class PineFoliagePlacer extends FoliagePlacer {
    public static final Codec<PineFoliagePlacer> CODEC;
    private final UniformInt height;
    
    public PineFoliagePlacer(final UniformInt aft1, final UniformInt aft2, final UniformInt aft3) {
        super(aft1, aft2);
        this.height = aft3;
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.PINE_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        int integer10 = 0;
        for (int integer11 = integer9; integer11 >= integer9 - integer6; --integer11) {
            this.placeLeavesRow(bry, random, cmw, b.foliagePos(), integer10, set, integer11, b.doubleTrunk(), cqx);
            if (integer10 >= 1 && integer11 == integer9 - integer6 + 1) {
                --integer10;
            }
            else if (integer10 < integer7 + b.radiusOffset()) {
                ++integer10;
            }
        }
    }
    
    @Override
    public int foliageRadius(final Random random, final int integer) {
        return super.foliageRadius(random, integer) + random.nextInt(integer + 1);
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return this.height.sample(random);
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return integer2 == integer5 && integer4 == integer5 && integer5 > 0;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> FoliagePlacer.<FoliagePlacer>foliagePlacerParts((RecordCodecBuilder.Instance<FoliagePlacer>)instance).and((App)UniformInt.codec(0, 16, 8).fieldOf("height").forGetter(cnm -> cnm.height)).apply((Applicative)instance, PineFoliagePlacer::new));
    }
}
