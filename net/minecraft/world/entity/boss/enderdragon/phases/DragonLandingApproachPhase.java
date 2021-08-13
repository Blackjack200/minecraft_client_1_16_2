package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class DragonLandingApproachPhase extends AbstractDragonPhaseInstance {
    private static final TargetingConditions NEAR_EGG_TARGETING;
    private Path currentPath;
    private Vec3 targetLocation;
    
    public DragonLandingApproachPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    public EnderDragonPhase<DragonLandingApproachPhase> getPhase() {
        return EnderDragonPhase.LANDING_APPROACH;
    }
    
    @Override
    public void begin() {
        this.currentPath = null;
        this.targetLocation = null;
    }
    
    @Override
    public void doServerTick() {
        final double double2 = (this.targetLocation == null) ? 0.0 : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (double2 < 100.0 || double2 > 22500.0 || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
            this.findNewTarget();
        }
    }
    
    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }
    
    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isDone()) {
            final int integer2 = this.dragon.findClosestNode();
            final BlockPos fx3 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
            final Player bft4 = this.dragon.level.getNearestPlayer(DragonLandingApproachPhase.NEAR_EGG_TARGETING, fx3.getX(), fx3.getY(), fx3.getZ());
            int integer3;
            if (bft4 != null) {
                final Vec3 dck6 = new Vec3(bft4.getX(), 0.0, bft4.getZ()).normalize();
                integer3 = this.dragon.findClosestNode(-dck6.x * 40.0, 105.0, -dck6.z * 40.0);
            }
            else {
                integer3 = this.dragon.findClosestNode(40.0, fx3.getY(), 0.0);
            }
            final Node cwy6 = new Node(fx3.getX(), fx3.getY(), fx3.getZ());
            this.currentPath = this.dragon.findPath(integer2, integer3, cwy6);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }
        this.navigateToNextPathNode();
        if (this.currentPath != null && this.currentPath.isDone()) {
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING);
        }
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
    
    static {
        NEAR_EGG_TARGETING = new TargetingConditions().range(128.0);
    }
}
