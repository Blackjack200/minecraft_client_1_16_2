package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;

public class UpdateActivityFromSchedule extends Behavior<LivingEntity> {
    public UpdateActivityFromSchedule() {
        super((Map)ImmutableMap.of());
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        aqj.getBrain().updateActivityFromSchedule(aag.getDayTime(), aag.getGameTime());
    }
}
