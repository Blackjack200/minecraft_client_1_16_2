package net.minecraft.world.level.lighting;

import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LightChunkGetter;
import javax.annotation.Nullable;

public class LevelLightEngine implements LightEventListener {
    @Nullable
    private final LayerLightEngine<?, ?> blockEngine;
    @Nullable
    private final LayerLightEngine<?, ?> skyEngine;
    
    public LevelLightEngine(final LightChunkGetter cgg, final boolean boolean2, final boolean boolean3) {
        this.blockEngine = (boolean2 ? new BlockLightEngine(cgg) : null);
        this.skyEngine = (boolean3 ? new SkyLightEngine(cgg) : null);
    }
    
    public void checkBlock(final BlockPos fx) {
        if (this.blockEngine != null) {
            this.blockEngine.checkBlock(fx);
        }
        if (this.skyEngine != null) {
            this.skyEngine.checkBlock(fx);
        }
    }
    
    public void onBlockEmissionIncrease(final BlockPos fx, final int integer) {
        if (this.blockEngine != null) {
            this.blockEngine.onBlockEmissionIncrease(fx, integer);
        }
    }
    
    public boolean hasLightWork() {
        return (this.skyEngine != null && this.skyEngine.hasLightWork()) || (this.blockEngine != null && this.blockEngine.hasLightWork());
    }
    
    public int runUpdates(final int integer, final boolean boolean2, final boolean boolean3) {
        if (this.blockEngine != null && this.skyEngine != null) {
            final int integer2 = integer / 2;
            final int integer3 = this.blockEngine.runUpdates(integer2, boolean2, boolean3);
            final int integer4 = integer - integer2 + integer3;
            final int integer5 = this.skyEngine.runUpdates(integer4, boolean2, boolean3);
            if (integer3 == 0 && integer5 > 0) {
                return this.blockEngine.runUpdates(integer5, boolean2, boolean3);
            }
            return integer5;
        }
        else {
            if (this.blockEngine != null) {
                return this.blockEngine.runUpdates(integer, boolean2, boolean3);
            }
            if (this.skyEngine != null) {
                return this.skyEngine.runUpdates(integer, boolean2, boolean3);
            }
            return integer;
        }
    }
    
    public void updateSectionStatus(final SectionPos gp, final boolean boolean2) {
        if (this.blockEngine != null) {
            this.blockEngine.updateSectionStatus(gp, boolean2);
        }
        if (this.skyEngine != null) {
            this.skyEngine.updateSectionStatus(gp, boolean2);
        }
    }
    
    public void enableLightSources(final ChunkPos bra, final boolean boolean2) {
        if (this.blockEngine != null) {
            this.blockEngine.enableLightSources(bra, boolean2);
        }
        if (this.skyEngine != null) {
            this.skyEngine.enableLightSources(bra, boolean2);
        }
    }
    
    public LayerLightEventListener getLayerListener(final LightLayer bsc) {
        if (bsc == LightLayer.BLOCK) {
            if (this.blockEngine == null) {
                return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
            }
            return this.blockEngine;
        }
        else {
            if (this.skyEngine == null) {
                return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
            }
            return this.skyEngine;
        }
    }
    
    public String getDebugData(final LightLayer bsc, final SectionPos gp) {
        if (bsc == LightLayer.BLOCK) {
            if (this.blockEngine != null) {
                return this.blockEngine.getDebugData(gp.asLong());
            }
        }
        else if (this.skyEngine != null) {
            return this.skyEngine.getDebugData(gp.asLong());
        }
        return "n/a";
    }
    
    public void queueSectionData(final LightLayer bsc, final SectionPos gp, @Nullable final DataLayer cfy, final boolean boolean4) {
        if (bsc == LightLayer.BLOCK) {
            if (this.blockEngine != null) {
                this.blockEngine.queueSectionData(gp.asLong(), cfy, boolean4);
            }
        }
        else if (this.skyEngine != null) {
            this.skyEngine.queueSectionData(gp.asLong(), cfy, boolean4);
        }
    }
    
    public void retainData(final ChunkPos bra, final boolean boolean2) {
        if (this.blockEngine != null) {
            this.blockEngine.retainData(bra, boolean2);
        }
        if (this.skyEngine != null) {
            this.skyEngine.retainData(bra, boolean2);
        }
    }
    
    public int getRawBrightness(final BlockPos fx, final int integer) {
        final int integer2 = (this.skyEngine == null) ? 0 : (this.skyEngine.getLightValue(fx) - integer);
        final int integer3 = (this.blockEngine == null) ? 0 : this.blockEngine.getLightValue(fx);
        return Math.max(integer3, integer2);
    }
}
