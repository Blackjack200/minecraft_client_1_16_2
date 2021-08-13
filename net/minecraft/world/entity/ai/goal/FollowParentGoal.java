package net.minecraft.world.entity.ai.goal;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;

public class FollowParentGoal extends Goal {
    private final Animal animal;
    private Animal parent;
    private final double speedModifier;
    private int timeToRecalcPath;
    
    public FollowParentGoal(final Animal azw, final double double2) {
        this.animal = azw;
        this.speedModifier = double2;
    }
    
    @Override
    public boolean canUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        }
        final List<Animal> list2 = this.animal.level.<Animal>getEntitiesOfClass((java.lang.Class<? extends Animal>)this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0, 4.0, 8.0));
        Animal azw3 = null;
        double double4 = Double.MAX_VALUE;
        for (final Animal azw4 : list2) {
            if (azw4.getAge() < 0) {
                continue;
            }
            final double double5 = this.animal.distanceToSqr(azw4);
            if (double5 > double4) {
                continue;
            }
            double4 = double5;
            azw3 = azw4;
        }
        if (azw3 == null) {
            return false;
        }
        if (double4 < 9.0) {
            return false;
        }
        this.parent = azw3;
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        }
        if (!this.parent.isAlive()) {
            return false;
        }
        final double double2 = this.animal.distanceToSqr(this.parent);
        return double2 >= 9.0 && double2 <= 256.0;
    }
    
    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }
    
    @Override
    public void stop() {
        this.parent = null;
    }
    
    @Override
    public void tick() {
        final int timeToRecalcPath = this.timeToRecalcPath - 1;
        this.timeToRecalcPath = timeToRecalcPath;
        if (timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = 10;
        this.animal.getNavigation().moveTo(this.parent, this.speedModifier);
    }
}
