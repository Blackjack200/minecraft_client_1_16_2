package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class BecomePassiveIfMemoryPresent extends Behavior<LivingEntity> {
    private final int pacifyDuration;
    
    public BecomePassiveIfMemoryPresent(final MemoryModuleType<?> aya, final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.PACIFIED, MemoryStatus.VALUE_ABSENT, aya, MemoryStatus.VALUE_PRESENT));
        this.pacifyDuration = integer;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        aqj.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.PACIFIED, true, this.pacifyDuration);
        aqj.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
