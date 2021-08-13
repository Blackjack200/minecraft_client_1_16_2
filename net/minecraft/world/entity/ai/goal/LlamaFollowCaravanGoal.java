package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.entity.animal.horse.Llama;

public class LlamaFollowCaravanGoal extends Goal {
    public final Llama llama;
    private double speedModifier;
    private int distCheckCounter;
    
    public LlamaFollowCaravanGoal(final Llama bbb, final double double2) {
        this.llama = bbb;
        this.speedModifier = double2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.llama.isLeashed() || this.llama.inCaravan()) {
            return false;
        }
        final List<Entity> list2 = this.llama.level.getEntities(this.llama, this.llama.getBoundingBox().inflate(9.0, 4.0, 9.0), (apx -> {
            final EntityType<?> aqb2 = apx.getType();
            return aqb2 == EntityType.LLAMA || aqb2 == EntityType.TRADER_LLAMA;
        }));
        Llama bbb3 = null;
        double double4 = Double.MAX_VALUE;
        for (final Entity apx7 : list2) {
            final Llama bbb4 = (Llama)apx7;
            if (bbb4.inCaravan()) {
                if (bbb4.hasCaravanTail()) {
                    continue;
                }
                final double double5 = this.llama.distanceToSqr(bbb4);
                if (double5 > double4) {
                    continue;
                }
                double4 = double5;
                bbb3 = bbb4;
            }
        }
        if (bbb3 == null) {
            for (final Entity apx7 : list2) {
                final Llama bbb4 = (Llama)apx7;
                if (!bbb4.isLeashed()) {
                    continue;
                }
                if (bbb4.hasCaravanTail()) {
                    continue;
                }
                final double double5 = this.llama.distanceToSqr(bbb4);
                if (double5 > double4) {
                    continue;
                }
                double4 = double5;
                bbb3 = bbb4;
            }
        }
        if (bbb3 == null) {
            return false;
        }
        if (double4 < 4.0) {
            return false;
        }
        if (!bbb3.isLeashed() && !this.firstIsLeashed(bbb3, 1)) {
            return false;
        }
        this.llama.joinCaravan(bbb3);
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        if (!this.llama.inCaravan() || !this.llama.getCaravanHead().isAlive() || !this.firstIsLeashed(this.llama, 0)) {
            return false;
        }
        final double double2 = this.llama.distanceToSqr(this.llama.getCaravanHead());
        if (double2 > 676.0) {
            if (this.speedModifier <= 3.0) {
                this.speedModifier *= 1.2;
                this.distCheckCounter = 40;
                return true;
            }
            if (this.distCheckCounter == 0) {
                return false;
            }
        }
        if (this.distCheckCounter > 0) {
            --this.distCheckCounter;
        }
        return true;
    }
    
    @Override
    public void stop() {
        this.llama.leaveCaravan();
        this.speedModifier = 2.1;
    }
    
    @Override
    public void tick() {
        if (!this.llama.inCaravan()) {
            return;
        }
        if (this.llama.getLeashHolder() instanceof LeashFenceKnotEntity) {
            return;
        }
        final Llama bbb2 = this.llama.getCaravanHead();
        final double double3 = this.llama.distanceTo(bbb2);
        final float float5 = 2.0f;
        final Vec3 dck6 = new Vec3(bbb2.getX() - this.llama.getX(), bbb2.getY() - this.llama.getY(), bbb2.getZ() - this.llama.getZ()).normalize().scale(Math.max(double3 - 2.0, 0.0));
        this.llama.getNavigation().moveTo(this.llama.getX() + dck6.x, this.llama.getY() + dck6.y, this.llama.getZ() + dck6.z, this.speedModifier);
    }
    
    private boolean firstIsLeashed(final Llama bbb, int integer) {
        return integer <= 8 && bbb.inCaravan() && (bbb.getCaravanHead().isLeashed() || this.firstIsLeashed(bbb.getCaravanHead(), ++integer));
    }
}
