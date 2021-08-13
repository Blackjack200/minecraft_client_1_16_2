package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;

public class RandomLookAroundGoal extends Goal {
    private final Mob mob;
    private double relX;
    private double relZ;
    private int lookTime;
    
    public RandomLookAroundGoal(final Mob aqk) {
        this.mob = aqk;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        return this.mob.getRandom().nextFloat() < 0.02f;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.lookTime >= 0;
    }
    
    @Override
    public void start() {
        final double double2 = 6.283185307179586 * this.mob.getRandom().nextDouble();
        this.relX = Math.cos(double2);
        this.relZ = Math.sin(double2);
        this.lookTime = 20 + this.mob.getRandom().nextInt(20);
    }
    
    @Override
    public void tick() {
        --this.lookTime;
        this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
    }
}
