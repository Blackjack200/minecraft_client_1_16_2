package net.minecraft.world.entity.boss.enderdragon.phases;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.logging.log4j.Logger;

public class DragonStrafePlayerPhase extends AbstractDragonPhaseInstance {
    private static final Logger LOGGER;
    private int fireballCharge;
    private Path currentPath;
    private Vec3 targetLocation;
    private LivingEntity attackTarget;
    private boolean holdingPatternClockwise;
    
    public DragonStrafePlayerPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    @Override
    public void doServerTick() {
        if (this.attackTarget == null) {
            DragonStrafePlayerPhase.LOGGER.warn("Skipping player strafe phase because no player was found");
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
            return;
        }
        if (this.currentPath != null && this.currentPath.isDone()) {
            final double double2 = this.attackTarget.getX();
            final double double3 = this.attackTarget.getZ();
            final double double4 = double2 - this.dragon.getX();
            final double double5 = double3 - this.dragon.getZ();
            final double double6 = Mth.sqrt(double4 * double4 + double5 * double5);
            final double double7 = Math.min(0.4000000059604645 + double6 / 80.0 - 1.0, 10.0);
            this.targetLocation = new Vec3(double2, this.attackTarget.getY() + double7, double3);
        }
        final double double2 = (this.targetLocation == null) ? 0.0 : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (double2 < 100.0 || double2 > 22500.0) {
            this.findNewTarget();
        }
        final double double3 = 64.0;
        if (this.attackTarget.distanceToSqr(this.dragon) < 4096.0) {
            if (this.dragon.canSee(this.attackTarget)) {
                ++this.fireballCharge;
                final Vec3 dck6 = new Vec3(this.attackTarget.getX() - this.dragon.getX(), 0.0, this.attackTarget.getZ() - this.dragon.getZ()).normalize();
                final Vec3 dck7 = new Vec3(Mth.sin(this.dragon.yRot * 0.017453292f), 0.0, -Mth.cos(this.dragon.yRot * 0.017453292f)).normalize();
                final float float8 = (float)dck7.dot(dck6);
                float float9 = (float)(Math.acos((double)float8) * 57.2957763671875);
                float9 += 0.5f;
                if (this.fireballCharge >= 5 && float9 >= 0.0f && float9 < 10.0f) {
                    final double double6 = 1.0;
                    final Vec3 dck8 = this.dragon.getViewVector(1.0f);
                    final double double8 = this.dragon.head.getX() - dck8.x * 1.0;
                    final double double9 = this.dragon.head.getY(0.5) + 0.5;
                    final double double10 = this.dragon.head.getZ() - dck8.z * 1.0;
                    final double double11 = this.attackTarget.getX() - double8;
                    final double double12 = this.attackTarget.getY(0.5) - double9;
                    final double double13 = this.attackTarget.getZ() - double10;
                    if (!this.dragon.isSilent()) {
                        this.dragon.level.levelEvent(null, 1017, this.dragon.blockPosition(), 0);
                    }
                    final DragonFireball bga25 = new DragonFireball(this.dragon.level, this.dragon, double11, double12, double13);
                    bga25.moveTo(double8, double9, double10, 0.0f, 0.0f);
                    this.dragon.level.addFreshEntity(bga25);
                    this.fireballCharge = 0;
                    if (this.currentPath != null) {
                        while (!this.currentPath.isDone()) {
                            this.currentPath.advance();
                        }
                    }
                    this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
                }
            }
            else if (this.fireballCharge > 0) {
                --this.fireballCharge;
            }
        }
        else if (this.fireballCharge > 0) {
            --this.fireballCharge;
        }
    }
    
    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isDone()) {
            int integer3;
            final int integer2 = integer3 = this.dragon.findClosestNode();
            if (this.dragon.getRandom().nextInt(8) == 0) {
                this.holdingPatternClockwise = !this.holdingPatternClockwise;
                integer3 += 6;
            }
            if (this.holdingPatternClockwise) {
                ++integer3;
            }
            else {
                --integer3;
            }
            if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
                integer3 -= 12;
                integer3 &= 0x7;
                integer3 += 12;
            }
            else {
                integer3 %= 12;
                if (integer3 < 0) {
                    integer3 += 12;
                }
            }
            this.currentPath = this.dragon.findPath(integer2, integer3, null);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
    }
    
    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isDone()) {
            final Vec3i gr2 = this.currentPath.getNextNodePos();
            this.currentPath.advance();
            final double double3 = gr2.getX();
            final double double4 = gr2.getZ();
            double double5;
            do {
                double5 = gr2.getY() + this.dragon.getRandom().nextFloat() * 20.0f;
            } while (double5 < gr2.getY());
            this.targetLocation = new Vec3(double3, double5, double4);
        }
    }
    
    @Override
    public void begin() {
        this.fireballCharge = 0;
        this.targetLocation = null;
        this.currentPath = null;
        this.attackTarget = null;
    }
    
    public void setTarget(final LivingEntity aqj) {
        this.attackTarget = aqj;
        final int integer3 = this.dragon.findClosestNode();
        final int integer4 = this.dragon.findClosestNode(this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
        final int integer5 = Mth.floor(this.attackTarget.getX());
        final int integer6 = Mth.floor(this.attackTarget.getZ());
        final double double7 = integer5 - this.dragon.getX();
        final double double8 = integer6 - this.dragon.getZ();
        final double double9 = Mth.sqrt(double7 * double7 + double8 * double8);
        final double double10 = Math.min(0.4000000059604645 + double9 / 80.0 - 1.0, 10.0);
        final int integer7 = Mth.floor(this.attackTarget.getY() + double10);
        final Node cwy16 = new Node(integer5, integer7, integer6);
        this.currentPath = this.dragon.findPath(integer3, integer4, cwy16);
        if (this.currentPath != null) {
            this.currentPath.advance();
            this.navigateToNextPathNode();
        }
    }
    
    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }
    
    public EnderDragonPhase<DragonStrafePlayerPhase> getPhase() {
        return EnderDragonPhase.STRAFE_PLAYER;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
