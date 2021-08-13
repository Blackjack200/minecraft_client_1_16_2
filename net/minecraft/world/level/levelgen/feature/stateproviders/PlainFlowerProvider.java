package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.Util;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class PlainFlowerProvider extends BlockStateProvider {
    public static final Codec<PlainFlowerProvider> CODEC;
    public static final PlainFlowerProvider INSTANCE;
    private static final BlockState[] LOW_NOISE_FLOWERS;
    private static final BlockState[] HIGH_NOISE_FLOWERS;
    
    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.PLAIN_FLOWER_PROVIDER;
    }
    
    @Override
    public BlockState getState(final Random random, final BlockPos fx) {
        final double double4 = Biome.BIOME_INFO_NOISE.getValue(fx.getX() / 200.0, fx.getZ() / 200.0, false);
        if (double4 < -0.8) {
            return Util.<BlockState>getRandom(PlainFlowerProvider.LOW_NOISE_FLOWERS, random);
        }
        if (random.nextInt(3) > 0) {
            return Util.<BlockState>getRandom(PlainFlowerProvider.HIGH_NOISE_FLOWERS, random);
        }
        return Blocks.DANDELION.defaultBlockState();
    }
    
    static {
        CODEC = Codec.unit(() -> PlainFlowerProvider.INSTANCE);
        INSTANCE = new PlainFlowerProvider();
        LOW_NOISE_FLOWERS = new BlockState[] { Blocks.ORANGE_TULIP.defaultBlockState(), Blocks.RED_TULIP.defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState(), Blocks.WHITE_TULIP.defaultBlockState() };
        HIGH_NOISE_FLOWERS = new BlockState[] { Blocks.POPPY.defaultBlockState(), Blocks.AZURE_BLUET.defaultBlockState(), Blocks.OXEYE_DAISY.defaultBlockState(), Blocks.CORNFLOWER.defaultBlockState() };
    }
}
