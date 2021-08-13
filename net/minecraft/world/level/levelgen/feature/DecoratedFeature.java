package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import org.apache.commons.lang3.mutable.MutableBoolean;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;

public class DecoratedFeature extends Feature<DecoratedFeatureConfiguration> {
    public DecoratedFeature(final Codec<DecoratedFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final DecoratedFeatureConfiguration cls) {
        final MutableBoolean mutableBoolean7 = new MutableBoolean();
        cls.decorator.getPositions(new DecorationContext(bso, cfv), random, fx).forEach(fx -> {
            if (((ConfiguredFeature)cls.feature.get()).place(bso, cfv, random, fx)) {
                mutableBoolean7.setTrue();
            }
        });
        return mutableBoolean7.isTrue();
    }
    
    public String toString() {
        return String.format("< %s [%s] >", new Object[] { this.getClass().getSimpleName(), Registry.FEATURE.getKey(this) });
    }
}
