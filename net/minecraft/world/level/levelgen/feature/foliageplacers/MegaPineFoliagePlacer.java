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

public class MegaPineFoliagePlacer extends FoliagePlacer {
    public static final Codec<MegaPineFoliagePlacer> CODEC;
    private final UniformInt crownHeight;
    
    public MegaPineFoliagePlacer(final UniformInt aft1, final UniformInt aft2, final UniformInt aft3) {
        super(aft1, aft2);
        this.crownHeight = aft3;
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.MEGA_PINE_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        final BlockPos fx12 = b.foliagePos();
        int integer10 = 0;
        for (int integer11 = fx12.getY() - integer6 + integer9; integer11 <= fx12.getY() + integer9; ++integer11) {
            final int integer12 = fx12.getY() - integer11;
            final int integer13 = integer7 + b.radiusOffset() + Mth.floor(integer12 / (float)integer6 * 3.5f);
            int integer14;
            if (integer12 > 0 && integer13 == integer10 && (integer11 & 0x1) == 0x0) {
                integer14 = integer13 + 1;
            }
            else {
                integer14 = integer13;
            }
            this.placeLeavesRow(bry, random, cmw, new BlockPos(fx12.getX(), integer11, fx12.getZ()), integer14, set, 0, b.doubleTrunk(), cqx);
            integer10 = integer13;
        }
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return this.crownHeight.sample(random);
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return integer2 + integer4 >= 7 || integer2 * integer2 + integer4 * integer4 > integer5 * integer5;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> FoliagePlacer.<FoliagePlacer>foliagePlacerParts((RecordCodecBuilder.Instance<FoliagePlacer>)instance).and((App)UniformInt.codec(0, 16, 8).fieldOf("crown_height").forGetter(cnl -> cnl.crownHeight)).apply((Applicative)instance, MegaPineFoliagePlacer::new));
    }
}
