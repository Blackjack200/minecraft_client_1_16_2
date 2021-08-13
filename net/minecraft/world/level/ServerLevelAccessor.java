package net.minecraft.world.level;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

public interface ServerLevelAccessor extends LevelAccessor {
    ServerLevel getLevel();
    
    default void addFreshEntityWithPassengers(final Entity apx) {
        apx.getSelfAndPassengers().forEach(this::addFreshEntity);
    }
}
