package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class StopAdmiringIfItemTooFarAway<E extends Piglin> extends Behavior<E> {
    private final int maxDistanceToItem;
    
    public StopAdmiringIfItemTooFarAway(final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.REGISTERED));
        this.maxDistanceToItem = integer;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E bep) {
        if (!bep.getOffhandItem().isEmpty()) {
            return false;
        }
        final Optional<ItemEntity> optional4 = bep.getBrain().<ItemEntity>getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
        return !optional4.isPresent() || !((ItemEntity)optional4.get()).closerThan(bep, this.maxDistanceToItem);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E bep, final long long3) {
        bep.getBrain().<Boolean>eraseMemory(MemoryModuleType.ADMIRING_ITEM);
    }
}
