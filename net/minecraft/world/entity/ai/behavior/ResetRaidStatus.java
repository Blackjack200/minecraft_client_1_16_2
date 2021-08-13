package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;

public class ResetRaidStatus extends Behavior<LivingEntity> {
    public ResetRaidStatus() {
        super((Map)ImmutableMap.of());
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return aag.random.nextInt(20) == 0;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        final Raid bgy7 = aag.getRaidAt(aqj.blockPosition());
        if (bgy7 == null || bgy7.isStopped() || bgy7.isLoss()) {
            arc6.setDefaultActivity(Activity.IDLE);
            arc6.updateActivityFromSchedule(aag.getDayTime(), aag.getGameTime());
        }
    }
}
