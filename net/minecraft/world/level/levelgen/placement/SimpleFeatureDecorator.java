package net.minecraft.world.level.levelgen.placement;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public abstract class SimpleFeatureDecorator<DC extends DecoratorConfiguration> extends FeatureDecorator<DC> {
    public SimpleFeatureDecorator(final Codec<DC> codec) {
        super(codec);
    }
    
    @Override
    public final Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final DC clt, final BlockPos fx) {
        return this.place(random, clt, fx);
    }
    
    protected abstract Stream<BlockPos> place(final Random random, final DC clt, final BlockPos fx);
}
