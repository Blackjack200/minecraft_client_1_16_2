package net.minecraft.client.renderer;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;

public class BiomeColors {
    public static final ColorResolver GRASS_COLOR_RESOLVER;
    public static final ColorResolver FOLIAGE_COLOR_RESOLVER;
    public static final ColorResolver WATER_COLOR_RESOLVER;
    
    private static int getAverageColor(final BlockAndTintGetter bqx, final BlockPos fx, final ColorResolver colorResolver) {
        return bqx.getBlockTint(fx, colorResolver);
    }
    
    public static int getAverageGrassColor(final BlockAndTintGetter bqx, final BlockPos fx) {
        return getAverageColor(bqx, fx, BiomeColors.GRASS_COLOR_RESOLVER);
    }
    
    public static int getAverageFoliageColor(final BlockAndTintGetter bqx, final BlockPos fx) {
        return getAverageColor(bqx, fx, BiomeColors.FOLIAGE_COLOR_RESOLVER);
    }
    
    public static int getAverageWaterColor(final BlockAndTintGetter bqx, final BlockPos fx) {
        return getAverageColor(bqx, fx, BiomeColors.WATER_COLOR_RESOLVER);
    }
    
    static {
        GRASS_COLOR_RESOLVER = Biome::getGrassColor;
        FOLIAGE_COLOR_RESOLVER = ((bss, double2, double3) -> bss.getFoliageColor());
        WATER_COLOR_RESOLVER = ((bss, double2, double3) -> bss.getWaterColor());
    }
}
