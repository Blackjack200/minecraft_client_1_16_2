package net.minecraft.world.entity.ai.sensing;

import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.AgableMob;

public class AdultSensor extends Sensor<AgableMob> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.VISIBLE_LIVING_ENTITIES);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final AgableMob apv) {
        apv.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(list -> this.setNearestVisibleAdult(apv, (List<LivingEntity>)list));
    }
    
    private void setNearestVisibleAdult(final AgableMob apv, final List<LivingEntity> list) {
        final Optional<AgableMob> optional4 = (Optional<AgableMob>)list.stream().filter(aqj -> aqj.getType() == apv.getType()).map(aqj -> (AgableMob)aqj).filter(apv -> !apv.isBaby()).findFirst();
        apv.getBrain().<AgableMob>setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional4);
    }
}
