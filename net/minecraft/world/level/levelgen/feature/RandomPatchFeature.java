package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class RandomPatchFeature extends Feature<RandomPatchConfiguration> {
    public RandomPatchFeature(final Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final RandomPatchConfiguration cmk) {
        final BlockState cee7 = cmk.stateProvider.getState(random, fx);
        BlockPos fx2;
        if (cmk.project) {
            fx2 = bso.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, fx);
        }
        else {
            fx2 = fx;
        }
        int integer9 = 0;
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        for (int integer10 = 0; integer10 < cmk.tries; ++integer10) {
            a10.setWithOffset(fx2, random.nextInt(cmk.xspread + 1) - random.nextInt(cmk.xspread + 1), random.nextInt(cmk.yspread + 1) - random.nextInt(cmk.yspread + 1), random.nextInt(cmk.zspread + 1) - random.nextInt(cmk.zspread + 1));
            final BlockPos fx3 = a10.below();
            final BlockState cee8 = bso.getBlockState(fx3);
            if ((bso.isEmptyBlock(a10) || (cmk.canReplace && bso.getBlockState(a10).getMaterial().isReplaceable())) && cee7.canSurvive(bso, a10) && (cmk.whitelist.isEmpty() || cmk.whitelist.contains(cee8.getBlock())) && !cmk.blacklist.contains(cee8) && (!cmk.needWater || bso.getFluidState(fx3.west()).is(FluidTags.WATER) || bso.getFluidState(fx3.east()).is(FluidTags.WATER) || bso.getFluidState(fx3.north()).is(FluidTags.WATER) || bso.getFluidState(fx3.south()).is(FluidTags.WATER))) {
                cmk.blockPlacer.place(bso, a10, cee7, random);
                ++integer9;
            }
        }
        return integer9 > 0;
    }
}
