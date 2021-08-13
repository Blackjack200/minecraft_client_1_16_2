package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.util.Mth;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class DragonHoldingPatternPhase extends AbstractDragonPhaseInstance {
    private static final TargetingConditions NEW_TARGET_TARGETING;
    private Path currentPath;
    private Vec3 targetLocation;
    private boolean clockwise;
    
    public DragonHoldingPatternPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    public EnderDragonPhase<DragonHoldingPatternPhase> getPhase() {
        return EnderDragonPhase.HOLDING_PATTERN;
    }
    
    @Override
    public void doServerTick() {
        final double double2 = (this.targetLocation == null) ? 0.0 : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (double2 < 100.0 || double2 > 22500.0 || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
            this.findNewTarget();
        }
    }
    
    @Override
    public void begin() {
        this.currentPath = null;
        this.targetLocation = null;
    }
    
    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }
    
    private void findNewTarget() {
        if (this.currentPath != null && this.currentPath.isDone()) {
            final BlockPos fx2 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(EndPodiumFeature.END_PODIUM_LOCATION));
            final int integer3 = (this.dragon.getDragonFight() == null) ? 0 : this.dragon.getDragonFight().getCrystalsAlive();
            if (this.dragon.getRandom().nextInt(integer3 + 3) == 0) {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING_APPROACH);
                return;
            }
            double double4 = 64.0;
            final Player bft6 = this.dragon.level.getNearestPlayer(DragonHoldingPatternPhase.NEW_TARGET_TARGETING, fx2.getX(), fx2.getY(), fx2.getZ());
            if (bft6 != null) {
                double4 = fx2.distSqr(bft6.position(), true) / 512.0;
            }
            if (bft6 != null && !bft6.abilities.invulnerable && (this.dragon.getRandom().nextInt(Mth.abs((int)double4) + 2) == 0 || this.dragon.getRandom().nextInt(integer3 + 2) == 0)) {
                this.strafePlayer(bft6);
                return;
            }
        }
        if (this.currentPath == null || this.currentPath.isDone()) {
            int integer3;
            final int integer4 = integer3 = this.dragon.findClosestNode();
            if (this.dragon.getRandom().nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                integer3 += 6;
            }
            if (this.clockwise) {
                ++integer3;
            }
            else {
                --integer3;
            }
            if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() < 0) {
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
            this.currentPath = this.dragon.findPath(integer4, integer3, null);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
    }
    
    private void strafePlayer(final Player bft) {
        this.dragon.getPhaseManager().setPhase(EnderDragonPhase.STRAFE_PLAYER);
        this.dragon.getPhaseManager().<DragonStrafePlayerPhase>getPhase(EnderDragonPhase.STRAFE_PLAYER).setTarget(bft);
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
    public void onCrystalDestroyed(final EndCrystal bbn, final BlockPos fx, final DamageSource aph, @Nullable final Player bft) {
        if (bft != null && !bft.abilities.invulnerable) {
            this.strafePlayer(bft);
        }
    }
    
    static {
        NEW_TARGET_TARGETING = new TargetingConditions().range(64.0);
    }
}
