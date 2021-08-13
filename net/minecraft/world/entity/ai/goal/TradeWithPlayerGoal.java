package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.entity.npc.AbstractVillager;

public class TradeWithPlayerGoal extends Goal {
    private final AbstractVillager mob;
    
    public TradeWithPlayerGoal(final AbstractVillager bfb) {
        this.mob = bfb;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (!this.mob.isAlive()) {
            return false;
        }
        if (this.mob.isInWater()) {
            return false;
        }
        if (!this.mob.isOnGround()) {
            return false;
        }
        if (this.mob.hurtMarked) {
            return false;
        }
        final Player bft2 = this.mob.getTradingPlayer();
        return bft2 != null && this.mob.distanceToSqr(bft2) <= 16.0 && bft2.containerMenu != null;
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }
    
    @Override
    public void stop() {
        this.mob.setTradingPlayer(null);
    }
}
