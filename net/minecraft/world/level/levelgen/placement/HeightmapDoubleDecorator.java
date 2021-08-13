package net.minecraft.world.level.levelgen.placement;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class HeightmapDoubleDecorator<DC extends DecoratorConfiguration> extends EdgeDecorator<DC> {
    public HeightmapDoubleDecorator(final Codec<DC> codec) {
        super(codec);
    }
    
    @Override
    protected Heightmap.Types type(final DC clt) {
        return Heightmap.Types.MOTION_BLOCKING;
    }
    
    @Override
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final DC clt, final BlockPos fx) {
        final int integer6 = fx.getX();
        final int integer7 = fx.getZ();
        final int integer8 = cps.getHeight(this.type(clt), integer6, integer7);
        if (integer8 == 0) {
            return (Stream<BlockPos>)Stream.of((Object[])new BlockPos[0]);
        }
        return (Stream<BlockPos>)Stream.of(new BlockPos(integer6, random.nextInt(integer8 * 2), integer7));
    }
}
