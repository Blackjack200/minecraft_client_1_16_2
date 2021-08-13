package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.core.Direction;

public interface BlockAndTintGetter extends BlockGetter {
    float getShade(final Direction gc, final boolean boolean2);
    
    LevelLightEngine getLightEngine();
    
    int getBlockTint(final BlockPos fx, final ColorResolver colorResolver);
    
    default int getBrightness(final LightLayer bsc, final BlockPos fx) {
        return this.getLightEngine().getLayerListener(bsc).getLightValue(fx);
    }
    
    default int getRawBrightness(final BlockPos fx, final int integer) {
        return this.getLightEngine().getRawBrightness(fx, integer);
    }
    
    default boolean canSeeSky(final BlockPos fx) {
        return this.getBrightness(LightLayer.SKY, fx) >= this.getMaxLightLevel();
    }
}
