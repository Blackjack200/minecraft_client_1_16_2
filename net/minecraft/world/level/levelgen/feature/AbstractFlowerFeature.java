package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class AbstractFlowerFeature<U extends FeatureConfiguration> extends Feature<U> {
    public AbstractFlowerFeature(final Codec<U> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final U clx) {
        final BlockState cee7 = this.getRandomFlower(random, fx, clx);
        int integer8 = 0;
        for (int integer9 = 0; integer9 < this.getCount(clx); ++integer9) {
            final BlockPos fx2 = this.getPos(random, fx, clx);
            if (bso.isEmptyBlock(fx2) && fx2.getY() < 255 && cee7.canSurvive(bso, fx2) && this.isValid(bso, fx2, clx)) {
                bso.setBlock(fx2, cee7, 2);
                ++integer8;
            }
        }
        return integer8 > 0;
    }
    
    public abstract boolean isValid(final LevelAccessor brv, final BlockPos fx, final U clx);
    
    public abstract int getCount(final U clx);
    
    public abstract BlockPos getPos(final Random random, final BlockPos fx, final U clx);
    
    public abstract BlockState getRandomFlower(final Random random, final BlockPos fx, final U clx);
}
