package net.minecraft.world.entity.ai.behavior;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;

public class AnimalMakeLove extends Behavior<Animal> {
    private final EntityType<? extends Animal> partnerType;
    private final float speedModifier;
    private long spawnChildAtTime;
    
    public AnimalMakeLove(final EntityType<? extends Animal> aqb, final float float2) {
        super((Map)ImmutableMap.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 325);
        this.partnerType = aqb;
        this.speedModifier = float2;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Animal azw) {
        return azw.isInLove() && this.findValidBreedPartner(azw).isPresent();
    }
    
    @Override
    protected void start(final ServerLevel aag, final Animal azw, final long long3) {
        final Animal azw2 = (Animal)this.findValidBreedPartner(azw).get();
        azw.getBrain().<AgableMob>setMemory(MemoryModuleType.BREED_TARGET, azw2);
        azw2.getBrain().<AgableMob>setMemory(MemoryModuleType.BREED_TARGET, azw);
        BehaviorUtils.lockGazeAndWalkToEachOther(azw, azw2, this.speedModifier);
        final int integer7 = 275 + azw.getRandom().nextInt(50);
        this.spawnChildAtTime = long3 + integer7;
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Animal azw, final long long3) {
        if (!this.hasBreedTargetOfRightType(azw)) {
            return false;
        }
        final Animal azw2 = this.getBreedTarget(azw);
        return azw2.isAlive() && azw.canMate(azw2) && BehaviorUtils.entityIsVisible(azw.getBrain(), azw2) && long3 <= this.spawnChildAtTime;
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Animal azw, final long long3) {
        final Animal azw2 = this.getBreedTarget(azw);
        BehaviorUtils.lockGazeAndWalkToEachOther(azw, azw2, this.speedModifier);
        if (!azw.closerThan(azw2, 3.0)) {
            return;
        }
        if (long3 >= this.spawnChildAtTime) {
            azw.spawnChildFromBreeding(aag, azw2);
            azw.getBrain().<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
            azw2.getBrain().<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
        }
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Animal azw, final long long3) {
        azw.getBrain().<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
        azw.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        azw.getBrain().<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
        this.spawnChildAtTime = 0L;
    }
    
    private Animal getBreedTarget(final Animal azw) {
        return (Animal)azw.getBrain().<AgableMob>getMemory(MemoryModuleType.BREED_TARGET).get();
    }
    
    private boolean hasBreedTargetOfRightType(final Animal azw) {
        final Brain<?> arc3 = azw.getBrain();
        return arc3.hasMemoryValue(MemoryModuleType.BREED_TARGET) && ((AgableMob)arc3.<AgableMob>getMemory(MemoryModuleType.BREED_TARGET).get()).getType() == this.partnerType;
    }
    
    private Optional<? extends Animal> findValidBreedPartner(final Animal azw) {
        return ((List)azw.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).stream().filter(aqj -> aqj.getType() == this.partnerType).map(aqj -> (Animal)aqj).filter(azw::canMate).findFirst();
    }
}
