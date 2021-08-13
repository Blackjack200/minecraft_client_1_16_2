package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BasaltPillarFeature extends Feature<NoneFeatureConfiguration> {
    public BasaltPillarFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        if (!bso.isEmptyBlock(fx) || bso.isEmptyBlock(fx.above())) {
            return false;
        }
        final BlockPos.MutableBlockPos a7 = fx.mutable();
        final BlockPos.MutableBlockPos a8 = fx.mutable();
        boolean boolean9 = true;
        boolean boolean10 = true;
        boolean boolean11 = true;
        boolean boolean12 = true;
        while (bso.isEmptyBlock(a7)) {
            if (Level.isOutsideBuildHeight(a7)) {
                return true;
            }
            bso.setBlock(a7, Blocks.BASALT.defaultBlockState(), 2);
            boolean9 = (boolean9 && this.placeHangOff(bso, random, a8.setWithOffset(a7, Direction.NORTH)));
            boolean10 = (boolean10 && this.placeHangOff(bso, random, a8.setWithOffset(a7, Direction.SOUTH)));
            boolean11 = (boolean11 && this.placeHangOff(bso, random, a8.setWithOffset(a7, Direction.WEST)));
            boolean12 = (boolean12 && this.placeHangOff(bso, random, a8.setWithOffset(a7, Direction.EAST)));
            a7.move(Direction.DOWN);
        }
        a7.move(Direction.UP);
        this.placeBaseHangOff(bso, random, a8.setWithOffset(a7, Direction.NORTH));
        this.placeBaseHangOff(bso, random, a8.setWithOffset(a7, Direction.SOUTH));
        this.placeBaseHangOff(bso, random, a8.setWithOffset(a7, Direction.WEST));
        this.placeBaseHangOff(bso, random, a8.setWithOffset(a7, Direction.EAST));
        a7.move(Direction.DOWN);
        final BlockPos.MutableBlockPos a9 = new BlockPos.MutableBlockPos();
        for (int integer14 = -3; integer14 < 4; ++integer14) {
            for (int integer15 = -3; integer15 < 4; ++integer15) {
                final int integer16 = Mth.abs(integer14) * Mth.abs(integer15);
                if (random.nextInt(10) < 10 - integer16) {
                    a9.set(a7.offset(integer14, 0, integer15));
                    int integer17 = 3;
                    while (bso.isEmptyBlock(a8.setWithOffset(a9, Direction.DOWN))) {
                        a9.move(Direction.DOWN);
                        if (--integer17 <= 0) {
                            break;
                        }
                    }
                    if (!bso.isEmptyBlock(a8.setWithOffset(a9, Direction.DOWN))) {
                        bso.setBlock(a9, Blocks.BASALT.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
    
    private void placeBaseHangOff(final LevelAccessor brv, final Random random, final BlockPos fx) {
        if (random.nextBoolean()) {
            brv.setBlock(fx, Blocks.BASALT.defaultBlockState(), 2);
        }
    }
    
    private boolean placeHangOff(final LevelAccessor brv, final Random random, final BlockPos fx) {
        if (random.nextInt(10) != 0) {
            brv.setBlock(fx, Blocks.BASALT.defaultBlockState(), 2);
            return true;
        }
        return false;
    }
}
