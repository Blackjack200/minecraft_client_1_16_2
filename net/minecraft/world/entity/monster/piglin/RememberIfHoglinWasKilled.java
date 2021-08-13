package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class RememberIfHoglinWasKilled<E extends Piglin> extends Behavior<E> {
    public RememberIfHoglinWasKilled() {
        super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HUNTED_RECENTLY, MemoryStatus.REGISTERED));
    }
    
    @Override
    protected void start(final ServerLevel aag, final E bep, final long long3) {
        if (this.isAttackTargetDeadHoglin(bep)) {
            PiglinAi.dontKillAnyMoreHoglinsForAWhile(bep);
        }
    }
    
    private boolean isAttackTargetDeadHoglin(final E bep) {
        final LivingEntity aqj3 = (LivingEntity)bep.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
        return aqj3.getType() == EntityType.HOGLIN && aqj3.isDeadOrDying();
    }
}
