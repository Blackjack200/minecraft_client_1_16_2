package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;

public class NearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {
    protected final Class<T> targetType;
    protected final int randomInterval;
    protected LivingEntity target;
    protected TargetingConditions targetConditions;
    
    public NearestAttackableTargetGoal(final Mob aqk, final Class<T> class2, final boolean boolean3) {
        this(aqk, class2, boolean3, false);
    }
    
    public NearestAttackableTargetGoal(final Mob aqk, final Class<T> class2, final boolean boolean3, final boolean boolean4) {
        this(aqk, class2, 10, boolean3, boolean4, null);
    }
    
    public NearestAttackableTargetGoal(final Mob aqk, final Class<T> class2, final int integer, final boolean boolean4, final boolean boolean5, @Nullable final Predicate<LivingEntity> predicate) {
        super(aqk, boolean4, boolean5);
        this.targetType = class2;
        this.randomInterval = integer;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.TARGET));
        this.targetConditions = new TargetingConditions().range(this.getFollowDistance()).selector(predicate);
    }
    
    @Override
    public boolean canUse() {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        }
        this.findTarget();
        return this.target != null;
    }
    
    protected AABB getTargetSearchArea(final double double1) {
        return this.mob.getBoundingBox().inflate(double1, 4.0, double1);
    }
    
    protected void findTarget() {
        if (this.targetType == Player.class || this.targetType == ServerPlayer.class) {
            this.target = this.mob.level.getNearestPlayer(this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        }
        else {
            this.target = this.mob.level.<LivingEntity>getNearestLoadedEntity((java.lang.Class<? extends LivingEntity>)this.targetType, this.targetConditions, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.getTargetSearchArea(this.getFollowDistance()));
        }
    }
    
    @Override
    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }
    
    public void setTarget(@Nullable final LivingEntity aqj) {
        this.target = aqj;
    }
}
