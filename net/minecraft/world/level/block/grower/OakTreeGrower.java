package net.minecraft.world.level.block.grower;

import javax.annotation.Nullable;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.Random;

public class OakTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(final Random random, final boolean boolean2) {
        if (random.nextInt(10) == 0) {
            return boolean2 ? Features.FANCY_OAK_BEES_005 : Features.FANCY_OAK;
        }
        return boolean2 ? Features.OAK_BEES_005 : Features.OAK;
    }
}
