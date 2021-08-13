package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;

public class WakeUp extends Behavior<LivingEntity> {
    public WakeUp() {
        super((Map)ImmutableMap.of());
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return !aqj.getBrain().isActive(Activity.REST) && aqj.isSleeping();
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        aqj.stopSleeping();
    }
}
