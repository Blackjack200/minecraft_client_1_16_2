package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.Path;

public class DragonTakeoffPhase extends AbstractDragonPhaseInstance {
    private boolean firstTick;
    private Path currentPath;
    private Vec3 targetLocation;
    
    public DragonTakeoffPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    @Override
    public void doServerTick() {
        if (this.firstTick || this.currentPath == null) {
            this.firstTick = false;
            this.findNewTarget();
        }
        else {
            final BlockPos fx2 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION);
            if (!fx2.closerThan(this.dragon.position(), 10.0)) {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
            }
        }
    }
    
    @Override
    public void begin() {
        this.firstTick = true;
        this.currentPath = null;
        this.targetLocation = null;
    }
    
    private void findNewTarget() {
        final int integer2 = this.dragon.findClosestNode();
        final Vec3 dck3 = this.dragon.getHeadLookVector(1.0f);
        int integer3 = this.dragon.findClosestNode(-dck3.x * 40.0, 105.0, -dck3.z * 40.0);
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
        this.navigateToNextPathNode();
    }
    
    private void navigateToNextPathNode() {
        if (this.currentPath != null) {
            this.currentPath.advance();
            if (!this.currentPath.isDone()) {
                final Vec3i gr2 = this.currentPath.getNextNodePos();
                this.currentPath.advance();
                double double3;
                do {
                    double3 = gr2.getY() + this.dragon.getRandom().nextFloat() * 20.0f;
                } while (double3 < gr2.getY());
                this.targetLocation = new Vec3(gr2.getX(), double3, gr2.getZ());
            }
        }
    }
    
    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }
    
    public EnderDragonPhase<DragonTakeoffPhase> getPhase() {
        return EnderDragonPhase.TAKEOFF;
    }
}
