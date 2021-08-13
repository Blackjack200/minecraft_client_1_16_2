package net.minecraft.world.entity.ai.sensing;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.EntityType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;

public class VillagerHostilesSensor extends Sensor<LivingEntity> {
    private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES;
    
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        aqj.getBrain().<LivingEntity>setMemory(MemoryModuleType.NEAREST_HOSTILE, this.getNearestHostile(aqj));
    }
    
    private Optional<LivingEntity> getNearestHostile(final LivingEntity aqj) {
        return (Optional<LivingEntity>)this.getVisibleEntities(aqj).flatMap(list -> list.stream().filter(this::isHostile).filter(aqj2 -> this.isClose(aqj, aqj2)).min((aqj2, aqj3) -> this.compareMobDistance(aqj, aqj2, aqj3)));
    }
    
    private Optional<List<LivingEntity>> getVisibleEntities(final LivingEntity aqj) {
        return aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
    }
    
    private int compareMobDistance(final LivingEntity aqj1, final LivingEntity aqj2, final LivingEntity aqj3) {
        return Mth.floor(aqj2.distanceToSqr(aqj1) - aqj3.distanceToSqr(aqj1));
    }
    
    private boolean isClose(final LivingEntity aqj1, final LivingEntity aqj2) {
        final float float4 = (float)VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(aqj2.getType());
        return aqj2.distanceToSqr(aqj1) <= float4 * float4;
    }
    
    private boolean isHostile(final LivingEntity aqj) {
        return VillagerHostilesSensor.ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(aqj.getType());
    }
    
    static {
        ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder().put(EntityType.DROWNED, 8.0f).put(EntityType.EVOKER, 12.0f).put(EntityType.HUSK, 8.0f).put(EntityType.ILLUSIONER, 12.0f).put(EntityType.PILLAGER, 15.0f).put(EntityType.RAVAGER, 12.0f).put(EntityType.VEX, 8.0f).put(EntityType.VINDICATOR, 10.0f).put(EntityType.ZOGLIN, 10.0f).put(EntityType.ZOMBIE, 8.0f).put(EntityType.ZOMBIE_VILLAGER, 8.0f).build();
    }
}
