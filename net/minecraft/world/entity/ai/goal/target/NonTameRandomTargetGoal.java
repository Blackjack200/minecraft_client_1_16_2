package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.entity.Mob;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;

public class NonTameRandomTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final TamableAnimal tamableMob;
    
    public NonTameRandomTargetGoal(final TamableAnimal arb, final Class<T> class2, final boolean boolean3, @Nullable final Predicate<LivingEntity> predicate) {
        super(arb, class2, 10, boolean3, false, predicate);
        this.tamableMob = arb;
    }
    
    @Override
    public boolean canUse() {
        return !this.tamableMob.isTame() && super.canUse();
    }
    
    @Override
    public boolean canContinueToUse() {
        if (this.targetConditions != null) {
            return this.targetConditions.test(this.mob, this.target);
        }
        return super.canContinueToUse();
    }
}
