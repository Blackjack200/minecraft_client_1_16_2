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

public class SpruceFoliagePlacer extends FoliagePlacer {
    public static final Codec<SpruceFoliagePlacer> CODEC;
    private final UniformInt trunkHeight;
    
    public SpruceFoliagePlacer(final UniformInt aft1, final UniformInt aft2, final UniformInt aft3) {
        super(aft1, aft2);
        this.trunkHeight = aft3;
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.SPRUCE_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        final BlockPos fx12 = b.foliagePos();
        int integer10 = random.nextInt(2);
        int integer11 = 1;
        int integer12 = 0;
        for (int integer13 = integer9; integer13 >= -integer6; --integer13) {
            this.placeLeavesRow(bry, random, cmw, fx12, integer10, set, integer13, b.doubleTrunk(), cqx);
            if (integer10 >= integer11) {
                integer10 = integer12;
                integer12 = 1;
                integer11 = Math.min(integer11 + 1, integer7 + b.radiusOffset());
            }
            else {
                ++integer10;
            }
        }
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return Math.max(4, integer - this.trunkHeight.sample(random));
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return integer2 == integer5 && integer4 == integer5 && integer5 > 0;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> FoliagePlacer.<FoliagePlacer>foliagePlacerParts((RecordCodecBuilder.Instance<FoliagePlacer>)instance).and((App)UniformInt.codec(0, 16, 8).fieldOf("trunk_height").forGetter(cnn -> cnn.trunkHeight)).apply((Applicative)instance, SpruceFoliagePlacer::new));
    }
}
