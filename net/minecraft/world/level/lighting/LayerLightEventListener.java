package net.minecraft.world.level.lighting;

import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.core.SectionPos;

public interface LayerLightEventListener extends LightEventListener {
    @Nullable
    DataLayer getDataLayerData(final SectionPos gp);
    
    int getLightValue(final BlockPos fx);
    
    public enum DummyLightLayerEventListener implements LayerLightEventListener {
        INSTANCE;
        
        @Nullable
        public DataLayer getDataLayerData(final SectionPos gp) {
            return null;
        }
        
        public int getLightValue(final BlockPos fx) {
            return 0;
        }
        
        public void updateSectionStatus(final SectionPos gp, final boolean boolean2) {
        }
    }
}
