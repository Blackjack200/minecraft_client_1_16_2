package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.List;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.ai.goal.Goal;

public class ResetUniversalAngerTargetGoal<T extends Mob> extends Goal {
    private final T mob;
    private final boolean alertOthersOfSameType;
    private int lastHurtByPlayerTimestamp;
    
    public ResetUniversalAngerTargetGoal(final T aqk, final boolean boolean2) {
        this.mob = aqk;
        this.alertOthersOfSameType = boolean2;
    }
    
    @Override
    public boolean canUse() {
        return ((Mob)this.mob).level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.wasHurtByPlayer();
    }
    
    private boolean wasHurtByPlayer() {
        return ((LivingEntity)this.mob).getLastHurtByMob() != null && ((LivingEntity)this.mob).getLastHurtByMob().getType() == EntityType.PLAYER && ((LivingEntity)this.mob).getLastHurtByMobTimestamp() > this.lastHurtByPlayerTimestamp;
    }
    
    @Override
    public void start() {
        this.lastHurtByPlayerTimestamp = ((LivingEntity)this.mob).getLastHurtByMobTimestamp();
        ((NeutralMob)this.mob).forgetCurrentTargetAndRefreshUniversalAnger();
        if (this.alertOthersOfSameType) {
            this.getNearbyMobsOfSameType().stream().filter(aqk -> aqk != this.mob).map(aqk -> (NeutralMob)aqk).forEach(NeutralMob::forgetCurrentTargetAndRefreshUniversalAnger);
        }
        super.start();
    }
    
    private List<Mob> getNearbyMobsOfSameType() {
        final double double2 = ((LivingEntity)this.mob).getAttributeValue(Attributes.FOLLOW_RANGE);
        final AABB dcf4 = AABB.unitCubeFromLowerCorner(((Entity)this.mob).position()).inflate(double2, 10.0, double2);
        return ((Mob)this.mob).level.<Mob>getLoadedEntitiesOfClass((java.lang.Class<? extends Mob>)this.mob.getClass(), dcf4);
    }
}
