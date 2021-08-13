package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VoidStartPlatformFeature extends Feature<NoneFeatureConfiguration> {
    private static final BlockPos PLATFORM_ORIGIN;
    private static final ChunkPos PLATFORM_ORIGIN_CHUNK;
    
    public VoidStartPlatformFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    private static int checkerboardDistance(final int integer1, final int integer2, final int integer3, final int integer4) {
        return Math.max(Math.abs(integer1 - integer3), Math.abs(integer2 - integer4));
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final ChunkPos bra7 = new ChunkPos(fx);
        if (checkerboardDistance(bra7.x, bra7.z, VoidStartPlatformFeature.PLATFORM_ORIGIN_CHUNK.x, VoidStartPlatformFeature.PLATFORM_ORIGIN_CHUNK.z) > 1) {
            return true;
        }
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        for (int integer9 = bra7.getMinBlockZ(); integer9 <= bra7.getMaxBlockZ(); ++integer9) {
            for (int integer10 = bra7.getMinBlockX(); integer10 <= bra7.getMaxBlockX(); ++integer10) {
                if (checkerboardDistance(VoidStartPlatformFeature.PLATFORM_ORIGIN.getX(), VoidStartPlatformFeature.PLATFORM_ORIGIN.getZ(), integer10, integer9) <= 16) {
                    a8.set(integer10, VoidStartPlatformFeature.PLATFORM_ORIGIN.getY(), integer9);
                    if (a8.equals(VoidStartPlatformFeature.PLATFORM_ORIGIN)) {
                        bso.setBlock(a8, Blocks.COBBLESTONE.defaultBlockState(), 2);
                    }
                    else {
                        bso.setBlock(a8, Blocks.STONE.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
    
    static {
        PLATFORM_ORIGIN = new BlockPos(8, 3, 8);
        PLATFORM_ORIGIN_CHUNK = new ChunkPos(VoidStartPlatformFeature.PLATFORM_ORIGIN);
    }
}
