package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.EntitySelector;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class LookAtPlayerGoal extends Goal {
    protected final Mob mob;
    protected Entity lookAt;
    protected final float lookDistance;
    private int lookTime;
    protected final float probability;
    protected final Class<? extends LivingEntity> lookAtType;
    protected final TargetingConditions lookAtContext;
    
    public LookAtPlayerGoal(final Mob aqk, final Class<? extends LivingEntity> class2, final float float3) {
        this(aqk, class2, float3, 0.02f);
    }
    
    public LookAtPlayerGoal(final Mob aqk, final Class<? extends LivingEntity> class2, final float float3, final float float4) {
        this.mob = aqk;
        this.lookAtType = class2;
        this.lookDistance = float3;
        this.probability = float4;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK));
        if (class2 == Player.class) {
            this.lookAtContext = new TargetingConditions().range(float3).allowSameTeam().allowInvulnerable().allowNonAttackable().selector((Predicate<LivingEntity>)(aqj -> EntitySelector.notRiding(aqk).test(aqj)));
        }
        else {
            this.lookAtContext = new TargetingConditions().range(float3).allowSameTeam().allowInvulnerable().allowNonAttackable();
        }
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return false;
        }
        if (this.mob.getTarget() != null) {
            this.lookAt = this.mob.getTarget();
        }
        if (this.lookAtType == Player.class) {
            this.lookAt = this.mob.level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        else {
            this.lookAt = this.mob.level.<Entity>getNearestLoadedEntity((java.lang.Class<? extends Entity>)this.lookAtType, this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.lookDistance, 3.0, this.lookDistance));
        }
        return this.lookAt != null;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.lookAt.isAlive() && this.mob.distanceToSqr(this.lookAt) <= this.lookDistance * this.lookDistance && this.lookTime > 0;
    }
    
    @Override
    public void start() {
        this.lookTime = 40 + this.mob.getRandom().nextInt(40);
    }
    
    @Override
    public void stop() {
        this.lookAt = null;
    }
    
    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.lookAt.getX(), this.lookAt.getEyeY(), this.lookAt.getZ());
        --this.lookTime;
    }
}
