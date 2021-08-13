package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;

public class SetRaidStatus extends Behavior<LivingEntity> {
    public SetRaidStatus() {
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
        if (bgy7 != null) {
            if (!bgy7.hasFirstWaveSpawned() || bgy7.isBetweenWaves()) {
                arc6.setDefaultActivity(Activity.PRE_RAID);
                arc6.setActiveActivityIfPossible(Activity.PRE_RAID);
            }
            else {
                arc6.setDefaultActivity(Activity.RAID);
                arc6.setActiveActivityIfPossible(Activity.RAID);
            }
        }
    }
}
