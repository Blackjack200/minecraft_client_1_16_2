package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class StartHuntingHoglin<E extends Piglin> extends Behavior<E> {
    public StartHuntingHoglin() {
        super((Map)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ANGRY_AT, MemoryStatus.VALUE_ABSENT, MemoryModuleType.HUNTED_RECENTLY, MemoryStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryStatus.REGISTERED));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Piglin bep) {
        return !bep.isBaby() && !PiglinAi.hasAnyoneNearbyHuntedRecently(bep);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E bep, final long long3) {
        final Hoglin bej6 = (Hoglin)bep.getBrain().<Hoglin>getMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN).get();
        PiglinAi.setAngerTarget(bep, bej6);
        PiglinAi.dontKillAnyMoreHoglinsForAWhile(bep);
        PiglinAi.broadcastAngerTarget(bep, bej6);
        PiglinAi.broadcastDontKillAnyMoreHoglinsForAWhile(bep);
    }
}
