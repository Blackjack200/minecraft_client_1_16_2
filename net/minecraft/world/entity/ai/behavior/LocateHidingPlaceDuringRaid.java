package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;

public class LocateHidingPlaceDuringRaid extends LocateHidingPlace {
    public LocateHidingPlaceDuringRaid(final int integer, final float float2) {
        super(integer, float2, 1);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        final Raid bgy4 = aag.getRaidAt(aqj.blockPosition());
        return super.checkExtraStartConditions(aag, aqj) && bgy4 != null && bgy4.isActive() && !bgy4.isVictory() && !bgy4.isLoss();
    }
}
