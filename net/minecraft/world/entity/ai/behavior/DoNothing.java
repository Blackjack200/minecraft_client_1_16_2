package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;

public class DoNothing extends Behavior<LivingEntity> {
    public DoNothing(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(), integer1, integer2);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        return true;
    }
}
