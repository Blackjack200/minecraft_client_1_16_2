package net.minecraft.world.level.chunk;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;

public interface LightChunkGetter {
    @Nullable
    BlockGetter getChunkForLighting(final int integer1, final int integer2);
    
    default void onLightUpdate(final LightLayer bsc, final SectionPos gp) {
    }
    
    BlockGetter getLevel();
}
