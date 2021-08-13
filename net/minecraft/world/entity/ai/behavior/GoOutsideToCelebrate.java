package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;

public class GoOutsideToCelebrate extends MoveToSkySeeingSpot {
    public GoOutsideToCelebrate(final float float1) {
        super(float1);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        final Raid bgy4 = aag.getRaidAt(aqj.blockPosition());
        return bgy4 != null && bgy4.isVictory() && super.checkExtraStartConditions(aag, aqj);
    }
}
