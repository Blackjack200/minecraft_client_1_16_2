package net.minecraft.world.entity.ai.navigation;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;

public class WaterBoundPathNavigation extends PathNavigation {
    private boolean allowBreaching;
    
    public WaterBoundPathNavigation(final Mob aqk, final Level bru) {
        super(aqk, bru);
    }
    
    @Override
    protected PathFinder createPathFinder(final int integer) {
        this.allowBreaching = (this.mob instanceof Dolphin);
        this.nodeEvaluator = new SwimNodeEvaluator(this.allowBreaching);
        return new PathFinder(this.nodeEvaluator, integer);
    }
    
    @Override
    protected boolean canUpdatePath() {
        return this.allowBreaching || this.isInLiquid();
    }
    
    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.mob.getY(0.5), this.mob.getZ());
    }
    
    @Override
    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }
        if (this.isDone()) {
            return;
        }
        if (this.canUpdatePath()) {
            this.followThePath();
        }
        else if (this.path != null && !this.path.isDone()) {
            final Vec3 dck2 = this.path.getNextEntityPos(this.mob);
            if (Mth.floor(this.mob.getX()) == Mth.floor(dck2.x) && Mth.floor(this.mob.getY()) == Mth.floor(dck2.y) && Mth.floor(this.mob.getZ()) == Mth.floor(dck2.z)) {
                this.path.advance();
            }
        }
        DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
        if (this.isDone()) {
            return;
        }
        final Vec3 dck2 = this.path.getNextEntityPos(this.mob);
        this.mob.getMoveControl().setWantedPosition(dck2.x, dck2.y, dck2.z, this.speedModifier);
    }
    
    @Override
    protected void followThePath() {
        if (this.path == null) {
            return;
        }
        final Vec3 dck2 = this.getTempMobPos();
        final float float3 = this.mob.getBbWidth();
        float float4 = (float3 > 0.75f) ? (float3 / 2.0f) : (0.75f - float3 / 2.0f);
        final Vec3 dck3 = this.mob.getDeltaMovement();
        if (Math.abs(dck3.x) > 0.2 || Math.abs(dck3.z) > 0.2) {
            float4 *= (float)(dck3.length() * 6.0);
        }
        final int integer6 = 6;
        Vec3 dck4 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
        if (Math.abs(this.mob.getX() - dck4.x) < float4 && Math.abs(this.mob.getZ() - dck4.z) < float4 && Math.abs(this.mob.getY() - dck4.y) < float4 * 2.0f) {
            this.path.advance();
        }
        for (int integer7 = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); integer7 > this.path.getNextNodeIndex(); --integer7) {
            dck4 = this.path.getEntityPosAtNode(this.mob, integer7);
            if (dck4.distanceToSqr(dck2) <= 36.0) {
                if (this.canMoveDirectly(dck2, dck4, 0, 0, 0)) {
                    this.path.setNextNodeIndex(integer7);
                    break;
                }
            }
        }
        this.doStuckDetection(dck2);
    }
    
    @Override
    protected void doStuckDetection(final Vec3 dck) {
        if (this.tick - this.lastStuckCheck > 100) {
            if (dck.distanceToSqr(this.lastStuckCheckPos) < 2.25) {
                this.stop();
            }
            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = dck;
        }
        if (this.path != null && !this.path.isDone()) {
            final Vec3i gr3 = this.path.getNextNodePos();
            if (gr3.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
            }
            else {
                this.timeoutCachedNode = gr3;
                final double double4 = dck.distanceTo(Vec3.atCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = ((this.mob.getSpeed() > 0.0f) ? (double4 / this.mob.getSpeed() * 100.0) : 0.0);
            }
            if (this.timeoutLimit > 0.0 && this.timeoutTimer > this.timeoutLimit * 2.0) {
                this.timeoutCachedNode = Vec3i.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0;
                this.stop();
            }
            this.lastTimeoutCheck = Util.getMillis();
        }
    }
    
    @Override
    protected boolean canMoveDirectly(final Vec3 dck1, final Vec3 dck2, final int integer3, final int integer4, final int integer5) {
        final Vec3 dck3 = new Vec3(dck2.x, dck2.y + this.mob.getBbHeight() * 0.5, dck2.z);
        return this.level.clip(new ClipContext(dck1, dck3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob)).getType() == HitResult.Type.MISS;
    }
    
    @Override
    public boolean isStableDestination(final BlockPos fx) {
        return !this.level.getBlockState(fx).isSolidRender(this.level, fx);
    }
    
    @Override
    public void setCanFloat(final boolean boolean1) {
    }
}
