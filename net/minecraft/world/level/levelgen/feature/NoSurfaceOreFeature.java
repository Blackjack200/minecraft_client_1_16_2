package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class NoSurfaceOreFeature extends Feature<OreConfiguration> {
    NoSurfaceOreFeature(final Codec<OreConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final OreConfiguration cmg) {
        final int integer7 = random.nextInt(cmg.size + 1);
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        for (int integer8 = 0; integer8 < integer7; ++integer8) {
            this.offsetTargetPos(a8, random, fx, Math.min(integer8, 7));
            if (cmg.target.test(bso.getBlockState(a8), random) && !this.isFacingAir(bso, a8)) {
                bso.setBlock(a8, cmg.state, 2);
            }
        }
        return true;
    }
    
    private void offsetTargetPos(final BlockPos.MutableBlockPos a, final Random random, final BlockPos fx, final int integer) {
        final int integer2 = this.getRandomPlacementInOneAxisRelativeToOrigin(random, integer);
        final int integer3 = this.getRandomPlacementInOneAxisRelativeToOrigin(random, integer);
        final int integer4 = this.getRandomPlacementInOneAxisRelativeToOrigin(random, integer);
        a.setWithOffset(fx, integer2, integer3, integer4);
    }
    
    private int getRandomPlacementInOneAxisRelativeToOrigin(final Random random, final int integer) {
        return Math.round((random.nextFloat() - random.nextFloat()) * integer);
    }
    
    private boolean isFacingAir(final LevelAccessor brv, final BlockPos fx) {
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        for (final Direction gc8 : Direction.values()) {
            a4.setWithOffset(fx, gc8);
            if (brv.getBlockState(a4).isAir()) {
                return true;
            }
        }
        return false;
    }
}
