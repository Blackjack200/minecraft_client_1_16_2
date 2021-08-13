package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class StartAdmiringItemIfSeen<E extends Piglin> extends Behavior<E> {
    private final int admireDuration;
    
    public StartAdmiringItemIfSeen(final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.VALUE_ABSENT));
        this.admireDuration = integer;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E bep) {
        final ItemEntity bcs4 = (ItemEntity)bep.getBrain().<ItemEntity>getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
        return PiglinAi.isLovedItem(bcs4.getItem().getItem());
    }
    
    @Override
    protected void start(final ServerLevel aag, final E bep, final long long3) {
        bep.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, this.admireDuration);
    }
}
