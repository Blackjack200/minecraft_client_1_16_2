package net.minecraft.world.level.lighting;

import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;

public interface LightEventListener {
    default void updateSectionStatus(final BlockPos fx, final boolean boolean2) {
        this.updateSectionStatus(SectionPos.of(fx), boolean2);
    }
    
    void updateSectionStatus(final SectionPos gp, final boolean boolean2);
}
