package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;

public class ReplaceBlobsFeature extends Feature<ReplaceSphereConfiguration> {
    public ReplaceBlobsFeature(final Codec<ReplaceSphereConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final ReplaceSphereConfiguration cmn) {
        final Block bul7 = cmn.targetState.getBlock();
        final BlockPos fx2 = findTarget(bso, fx.mutable().clamp(Direction.Axis.Y, 1, bso.getMaxBuildHeight() - 1), bul7);
        if (fx2 == null) {
            return false;
        }
        final int integer9 = cmn.radius().sample(random);
        boolean boolean10 = false;
        for (final BlockPos fx3 : BlockPos.withinManhattan(fx2, integer9, integer9, integer9)) {
            if (fx3.distManhattan(fx2) > integer9) {
                break;
            }
            final BlockState cee13 = bso.getBlockState(fx3);
            if (!cee13.is(bul7)) {
                continue;
            }
            this.setBlock(bso, fx3, cmn.replaceState);
            boolean10 = true;
        }
        return boolean10;
    }
    
    @Nullable
    private static BlockPos findTarget(final LevelAccessor brv, final BlockPos.MutableBlockPos a, final Block bul) {
        while (a.getY() > 1) {
            final BlockState cee4 = brv.getBlockState(a);
            if (cee4.is(bul)) {
                return a;
            }
            a.move(Direction.DOWN);
        }
        return null;
    }
}
