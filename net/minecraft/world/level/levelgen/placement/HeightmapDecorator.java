package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class HeightmapDecorator<DC extends DecoratorConfiguration> extends BaseHeightmapDecorator<DC> {
    public HeightmapDecorator(final Codec<DC> codec) {
        super(codec);
    }
    
    @Override
    protected Heightmap.Types type(final DC clt) {
        return Heightmap.Types.MOTION_BLOCKING;
    }
}
