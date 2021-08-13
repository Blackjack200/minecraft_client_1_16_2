package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.EntityType;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;

public class VillagerBabiesSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        aqj.getBrain().<List<LivingEntity>>setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, this.getNearestVillagerBabies(aqj));
    }
    
    private List<LivingEntity> getNearestVillagerBabies(final LivingEntity aqj) {
        return (List<LivingEntity>)this.getVisibleEntities(aqj).stream().filter(this::isVillagerBaby).collect(Collectors.toList());
    }
    
    private boolean isVillagerBaby(final LivingEntity aqj) {
        return aqj.getType() == EntityType.VILLAGER && aqj.isBaby();
    }
    
    private List<LivingEntity> getVisibleEntities(final LivingEntity aqj) {
        return (List<LivingEntity>)aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Lists.newArrayList());
    }
}
