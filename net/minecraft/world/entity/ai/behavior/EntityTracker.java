package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import java.util.List;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class EntityTracker implements PositionTracker {
    private final Entity entity;
    private final boolean trackEyeHeight;
    
    public EntityTracker(final Entity apx, final boolean boolean2) {
        this.entity = apx;
        this.trackEyeHeight = boolean2;
    }
    
    public Vec3 currentPosition() {
        return this.trackEyeHeight ? this.entity.position().add(0.0, this.entity.getEyeHeight(), 0.0) : this.entity.position();
    }
    
    public BlockPos currentBlockPosition() {
        return this.entity.blockPosition();
    }
    
    public boolean isVisibleBy(final LivingEntity aqj) {
        if (this.entity instanceof LivingEntity) {
            final Optional<List<LivingEntity>> optional3 = aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
            return this.entity.isAlive() && optional3.isPresent() && ((List)optional3.get()).contains(this.entity);
        }
        return true;
    }
    
    public String toString() {
        return new StringBuilder().append("EntityTracker for ").append(this.entity).toString();
    }
}
