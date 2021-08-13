package net.minecraft.world.level.block.grower;

import net.minecraft.data.worldgen.Features;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.Random;

public class DarkOakTreeGrower extends AbstractMegaTreeGrower {
    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(final Random random, final boolean boolean2) {
        return null;
    }
    
    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredMegaFeature(final Random random) {
        return Features.DARK_OAK;
    }
}
