package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import java.util.EnumSet;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class RunAroundLikeCrazyGoal extends Goal {
    private final AbstractHorse horse;
    private final double speedModifier;
    private double posX;
    private double posY;
    private double posZ;
    
    public RunAroundLikeCrazyGoal(final AbstractHorse bay, final double double2) {
        this.horse = bay;
        this.speedModifier = double2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.horse.isTamed() || !this.horse.isVehicle()) {
            return false;
        }
        final Vec3 dck2 = RandomPos.getPos(this.horse, 5, 4);
        if (dck2 == null) {
            return false;
        }
        this.posX = dck2.x;
        this.posY = dck2.y;
        this.posZ = dck2.z;
        return true;
    }
    
    @Override
    public void start() {
        this.horse.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.horse.isTamed() && !this.horse.getNavigation().isDone() && this.horse.isVehicle();
    }
    
    @Override
    public void tick() {
        if (!this.horse.isTamed() && this.horse.getRandom().nextInt(50) == 0) {
            final Entity apx2 = (Entity)this.horse.getPassengers().get(0);
            if (apx2 == null) {
                return;
            }
            if (apx2 instanceof Player) {
                final int integer3 = this.horse.getTemper();
                final int integer4 = this.horse.getMaxTemper();
                if (integer4 > 0 && this.horse.getRandom().nextInt(integer4) < integer3) {
                    this.horse.tameWithName((Player)apx2);
                    return;
                }
                this.horse.modifyTemper(5);
            }
            this.horse.ejectPassengers();
            this.horse.makeMad();
            this.horse.level.broadcastEntityEvent(this.horse, (byte)6);
        }
    }
}
