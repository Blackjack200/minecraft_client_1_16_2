package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.EnumSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.PathfinderMob;

public class FleeSunGoal extends Goal {
    protected final PathfinderMob mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    private final Level level;
    
    public FleeSunGoal(final PathfinderMob aqr, final double double2) {
        this.mob = aqr;
        this.speedModifier = double2;
        this.level = aqr.level;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        return this.mob.getTarget() == null && this.level.isDay() && this.mob.isOnFire() && this.level.canSeeSky(this.mob.blockPosition()) && this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && this.setWantedPos();
    }
    
    protected boolean setWantedPos() {
        final Vec3 dck2 = this.getHidePos();
        if (dck2 == null) {
            return false;
        }
        this.wantedX = dck2.x;
        this.wantedY = dck2.y;
        this.wantedZ = dck2.z;
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
    
    @Nullable
    protected Vec3 getHidePos() {
        final Random random2 = this.mob.getRandom();
        final BlockPos fx3 = this.mob.blockPosition();
        for (int integer4 = 0; integer4 < 10; ++integer4) {
            final BlockPos fx4 = fx3.offset(random2.nextInt(20) - 10, random2.nextInt(6) - 3, random2.nextInt(20) - 10);
            if (!this.level.canSeeSky(fx4) && this.mob.getWalkTargetValue(fx4) < 0.0f) {
                return Vec3.atBottomCenterOf(fx4);
            }
        }
        return null;
    }
}
