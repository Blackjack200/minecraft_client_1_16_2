package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import com.mojang.serialization.Codec;

public class DiskReplaceFeature extends BaseDiskFeature {
    public DiskReplaceFeature(final Codec<DiskConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final DiskConfiguration clv) {
        return bso.getFluidState(fx).is(FluidTags.WATER) && super.place(bso, cfv, random, fx, clv);
    }
}
