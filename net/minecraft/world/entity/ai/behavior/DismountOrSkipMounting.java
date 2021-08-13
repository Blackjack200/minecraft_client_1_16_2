package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class DismountOrSkipMounting<E extends LivingEntity, T extends Entity> extends Behavior<E> {
    private final int maxWalkDistToRideTarget;
    private final BiPredicate<E, Entity> dontRideIf;
    
    public DismountOrSkipMounting(final int integer, final BiPredicate<E, Entity> biPredicate) {
        super((Map)ImmutableMap.of(MemoryModuleType.RIDE_TARGET, MemoryStatus.REGISTERED));
        this.maxWalkDistToRideTarget = integer;
        this.dontRideIf = biPredicate;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        final Entity apx4 = aqj.getVehicle();
        final Entity apx5 = (Entity)aqj.getBrain().<Entity>getMemory(MemoryModuleType.RIDE_TARGET).orElse(null);
        if (apx4 == null && apx5 == null) {
            return false;
        }
        final Entity apx6 = (apx4 == null) ? apx5 : apx4;
        return !this.isVehicleValid(aqj, apx6) || this.dontRideIf.test(aqj, apx6);
    }
    
    private boolean isVehicleValid(final E aqj, final Entity apx) {
        return apx.isAlive() && apx.closerThan(aqj, this.maxWalkDistToRideTarget) && apx.level == aqj.level;
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        aqj.stopRiding();
        aqj.getBrain().<Entity>eraseMemory(MemoryModuleType.RIDE_TARGET);
    }
}
