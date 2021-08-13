package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raider;

public abstract class AbstractIllager extends Raider {
    protected AbstractIllager(final EntityType<? extends AbstractIllager> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
    
    public MobType getMobType() {
        return MobType.ILLAGER;
    }
    
    public IllagerArmPose getArmPose() {
        return IllagerArmPose.CROSSED;
    }
    
    public enum IllagerArmPose {
        CROSSED, 
        ATTACKING, 
        SPELLCASTING, 
        BOW_AND_ARROW, 
        CROSSBOW_HOLD, 
        CROSSBOW_CHARGE, 
        CELEBRATING, 
        NEUTRAL;
    }
    
    public class RaiderOpenDoorGoal extends OpenDoorGoal {
        public RaiderOpenDoorGoal(final Raider bgz) {
            super(bgz, false);
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && AbstractIllager.this.hasActiveRaid();
        }
    }
}
