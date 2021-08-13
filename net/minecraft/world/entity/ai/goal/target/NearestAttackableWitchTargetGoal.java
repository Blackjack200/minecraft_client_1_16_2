package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.entity.Mob;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.LivingEntity;

public class NearestAttackableWitchTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private boolean canAttack;
    
    public NearestAttackableWitchTargetGoal(final Raider bgz, final Class<T> class2, final int integer, final boolean boolean4, final boolean boolean5, @Nullable final Predicate<LivingEntity> predicate) {
        super(bgz, class2, integer, boolean4, boolean5, predicate);
        this.canAttack = true;
    }
    
    public void setCanAttack(final boolean boolean1) {
        this.canAttack = boolean1;
    }
    
    @Override
    public boolean canUse() {
        return this.canAttack && super.canUse();
    }
}
