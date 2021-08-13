package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import java.util.EnumSet;
import net.minecraft.world.entity.Mob;

public class FloatGoal extends Goal {
    private final Mob mob;
    
    public FloatGoal(final Mob aqk) {
        this.mob = aqk;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP));
        aqk.getNavigation().setCanFloat(true);
    }
    
    @Override
    public boolean canUse() {
        return (this.mob.isInWater() && this.mob.getFluidHeight(FluidTags.WATER) > this.mob.getFluidJumpThreshold()) || this.mob.isInLava();
    }
    
    @Override
    public void tick() {
        if (this.mob.getRandom().nextFloat() < 0.8f) {
            this.mob.getJumpControl().jump();
        }
    }
}
