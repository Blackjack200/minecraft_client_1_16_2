package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

public class TopSolidHeightMapDecorator extends BaseHeightmapDecorator<NoneDecoratorConfiguration> {
    public TopSolidHeightMapDecorator(final Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected Heightmap.Types type(final NoneDecoratorConfiguration cmd) {
        return Heightmap.Types.OCEAN_FLOOR_WG;
    }
}
