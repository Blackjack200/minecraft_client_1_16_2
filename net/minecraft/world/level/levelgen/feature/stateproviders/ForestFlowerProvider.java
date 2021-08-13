package net.minecraft.world.level.levelgen.feature.stateproviders;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class ForestFlowerProvider extends BlockStateProvider {
    public static final Codec<ForestFlowerProvider> CODEC;
    private static final BlockState[] FLOWERS;
    public static final ForestFlowerProvider INSTANCE;
    
    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.FOREST_FLOWER_PROVIDER;
    }
    
    @Override
    public BlockState getState(final Random random, final BlockPos fx) {
        final double double4 = Mth.clamp((1.0 + Biome.BIOME_INFO_NOISE.getValue(fx.getX() / 48.0, fx.getZ() / 48.0, false)) / 2.0, 0.0, 0.9999);
        return ForestFlowerProvider.FLOWERS[(int)(double4 * ForestFlowerProvider.FLOWERS.length)];
    }
    
    static {
        CODEC = Codec.unit(() -> ForestFlowerProvider.INSTANCE);
        FLOWERS = new BlockState[] { Blocks.DANDELION.defaultBlockState(), Blocks.POPPY.defaultBlockState(), Blocks.ALLIUM.defaultBlockState(), Blocks.AZURE_BLUET.defaultBlockState(), Blocks.RED_TULIP.defaultBlockState(), Blocks.ORANGE_TULIP.defaultBlockState(), Blocks.WHITE_TULIP.defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState(), Blocks.OXEYE_DAISY.defaultBlockState(), Blocks.CORNFLOWER.defaultBlockState(), Blocks.LILY_OF_THE_VALLEY.defaultBlockState() };
        INSTANCE = new ForestFlowerProvider();
    }
}
