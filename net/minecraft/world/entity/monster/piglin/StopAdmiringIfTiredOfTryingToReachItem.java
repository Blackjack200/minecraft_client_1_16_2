package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class StopAdmiringIfTiredOfTryingToReachItem<E extends Piglin> extends Behavior<E> {
    private final int maxTimeToReachItem;
    private final int disableTime;
    
    public StopAdmiringIfTiredOfTryingToReachItem(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryStatus.REGISTERED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.REGISTERED));
        this.maxTimeToReachItem = integer1;
        this.disableTime = integer2;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E bep) {
        return bep.getOffhandItem().isEmpty();
    }
    
    @Override
    protected void start(final ServerLevel aag, final E bep, final long long3) {
        final Brain<Piglin> arc6 = bep.getBrain();
        final Optional<Integer> optional7 = arc6.<Integer>getMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
        if (!optional7.isPresent()) {
            arc6.<Integer>setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, 0);
        }
        else {
            final int integer8 = (int)optional7.get();
            if (integer8 > this.maxTimeToReachItem) {
                arc6.<Boolean>eraseMemory(MemoryModuleType.ADMIRING_ITEM);
                arc6.<Integer>eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
                arc6.<Boolean>setMemoryWithExpiry(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, true, this.disableTime);
            }
            else {
                arc6.<Integer>setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, integer8 + 1);
            }
        }
    }
}
