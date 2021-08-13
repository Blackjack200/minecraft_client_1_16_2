package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public abstract class EdgeDecorator<DC extends DecoratorConfiguration> extends FeatureDecorator<DC> {
    public EdgeDecorator(final Codec<DC> codec) {
        super(codec);
    }
    
    protected abstract Heightmap.Types type(final DC clt);
}
