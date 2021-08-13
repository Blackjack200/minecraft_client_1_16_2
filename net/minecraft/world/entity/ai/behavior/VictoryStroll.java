package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.server.level.ServerLevel;

public class VictoryStroll extends VillageBoundRandomStroll {
    public VictoryStroll(final float float1) {
        super(float1);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        final Raid bgy4 = aag.getRaidAt(aqr.blockPosition());
        return bgy4 != null && bgy4.isVictory() && super.checkExtraStartConditions(aag, aqr);
    }
}
