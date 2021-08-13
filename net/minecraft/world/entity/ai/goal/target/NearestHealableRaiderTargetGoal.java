package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.entity.Mob;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.LivingEntity;

public class NearestHealableRaiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private int cooldown;
    
    public NearestHealableRaiderTargetGoal(final Raider bgz, final Class<T> class2, final boolean boolean3, @Nullable final Predicate<LivingEntity> predicate) {
        super(bgz, class2, 500, boolean3, false, predicate);
        this.cooldown = 0;
    }
    
    public int getCooldown() {
        return this.cooldown;
    }
    
    public void decrementCooldown() {
        --this.cooldown;
    }
    
    @Override
    public boolean canUse() {
        if (this.cooldown > 0 || !this.mob.getRandom().nextBoolean()) {
            return false;
        }
        if (!((Raider)this.mob).hasActiveRaid()) {
            return false;
        }
        this.findTarget();
        return this.target != null;
    }
    
    @Override
    public void start() {
        this.cooldown = 200;
        super.start();
    }
}
