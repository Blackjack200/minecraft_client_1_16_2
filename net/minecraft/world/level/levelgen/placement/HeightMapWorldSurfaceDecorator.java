package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class HeightMapWorldSurfaceDecorator extends BaseHeightmapDecorator<NoneDecoratorConfiguration> {
    public HeightMapWorldSurfaceDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected Heightmap.Types type(final NoneDecoratorConfiguration cmd) {
        return Heightmap.Types.WORLD_SURFACE_WG;
    }
}
