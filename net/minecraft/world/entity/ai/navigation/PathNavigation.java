package net.minecraft.world.entity.ai.navigation;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableSet;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;

public abstract class PathNavigation {
    protected final Mob mob;
    protected final Level level;
    @Nullable
    protected Path path;
    protected double speedModifier;
    protected int tick;
    protected int lastStuckCheck;
    protected Vec3 lastStuckCheckPos;
    protected Vec3i timeoutCachedNode;
    protected long timeoutTimer;
    protected long lastTimeoutCheck;
    protected double timeoutLimit;
    protected float maxDistanceToWaypoint;
    protected boolean hasDelayedRecomputation;
    protected long timeLastRecompute;
    protected NodeEvaluator nodeEvaluator;
    private BlockPos targetPos;
    private int reachRange;
    private float maxVisitedNodesMultiplier;
    private final PathFinder pathFinder;
    
    public PathNavigation(final Mob aqk, final Level bru) {
        this.lastStuckCheckPos = Vec3.ZERO;
        this.timeoutCachedNode = Vec3i.ZERO;
        this.maxDistanceToWaypoint = 0.5f;
        this.maxVisitedNodesMultiplier = 1.0f;
        this.mob = aqk;
        this.level = bru;
        final int integer4 = Mth.floor(aqk.getAttributeValue(Attributes.FOLLOW_RANGE) * 16.0);
        this.pathFinder = this.createPathFinder(integer4);
    }
    
    public void resetMaxVisitedNodesMultiplier() {
        this.maxVisitedNodesMultiplier = 1.0f;
    }
    
    public void setMaxVisitedNodesMultiplier(final float float1) {
        this.maxVisitedNodesMultiplier = float1;
    }
    
    public BlockPos getTargetPos() {
        return this.targetPos;
    }
    
    protected abstract PathFinder createPathFinder(final int integer);
    
    public void setSpeedModifier(final double double1) {
        this.speedModifier = double1;
    }
    
    public boolean hasDelayedRecomputation() {
        return this.hasDelayedRecomputation;
    }
    
    public void recomputePath() {
        if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
            if (this.targetPos != null) {
                this.path = null;
                this.path = this.createPath(this.targetPos, this.reachRange);
                this.timeLastRecompute = this.level.getGameTime();
                this.hasDelayedRecomputation = false;
            }
        }
        else {
            this.hasDelayedRecomputation = true;
        }
    }
    
    @Nullable
    public final Path createPath(final double double1, final double double2, final double double3, final int integer) {
        return this.createPath(new BlockPos(double1, double2, double3), integer);
    }
    
    @Nullable
    public Path createPath(final Stream<BlockPos> stream, final int integer) {
        return this.createPath((Set<BlockPos>)stream.collect(Collectors.toSet()), 8, false, integer);
    }
    
    @Nullable
    public Path createPath(final Set<BlockPos> set, final int integer) {
        return this.createPath(set, 8, false, integer);
    }
    
    @Nullable
    public Path createPath(final BlockPos fx, final int integer) {
        return this.createPath((Set<BlockPos>)ImmutableSet.of(fx), 8, false, integer);
    }
    
    @Nullable
    public Path createPath(final Entity apx, final int integer) {
        return this.createPath((Set<BlockPos>)ImmutableSet.of(apx.blockPosition()), 16, true, integer);
    }
    
    @Nullable
    protected Path createPath(final Set<BlockPos> set, final int integer2, final boolean boolean3, final int integer4) {
        if (set.isEmpty()) {
            return null;
        }
        if (this.mob.getY() < 0.0) {
            return null;
        }
        if (!this.canUpdatePath()) {
            return null;
        }
        if (this.path != null && !this.path.isDone() && set.contains(this.targetPos)) {
            return this.path;
        }
        this.level.getProfiler().push("pathfind");
        final float float6 = (float)this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        final BlockPos fx7 = boolean3 ? this.mob.blockPosition().above() : this.mob.blockPosition();
        final int integer5 = (int)(float6 + integer2);
        final PathNavigationRegion bsf9 = new PathNavigationRegion(this.level, fx7.offset(-integer5, -integer5, -integer5), fx7.offset(integer5, integer5, integer5));
        final Path cxa10 = this.pathFinder.findPath(bsf9, this.mob, set, float6, integer4, this.maxVisitedNodesMultiplier);
        this.level.getProfiler().pop();
        if (cxa10 != null && cxa10.getTarget() != null) {
            this.targetPos = cxa10.getTarget();
            this.reachRange = integer4;
            this.resetStuckTimeout();
        }
        return cxa10;
    }
    
    public boolean moveTo(final double double1, final double double2, final double double3, final double double4) {
        return this.moveTo(this.createPath(double1, double2, double3, 1), double4);
    }
    
    public boolean moveTo(final Entity apx, final double double2) {
        final Path cxa5 = this.createPath(apx, 1);
        return cxa5 != null && this.moveTo(cxa5, double2);
    }
    
    public boolean moveTo(@Nullable final Path cxa, final double double2) {
        if (cxa == null) {
            this.path = null;
            return false;
        }
        if (!cxa.sameAs(this.path)) {
            this.path = cxa;
        }
        if (this.isDone()) {
            return false;
        }
        this.trimPath();
        if (this.path.getNodeCount() <= 0) {
            return false;
        }
        this.speedModifier = double2;
        final Vec3 dck5 = this.getTempMobPos();
        this.lastStuckCheck = this.tick;
        this.lastStuckCheckPos = dck5;
        return true;
    }
    
    @Nullable
    public Path getPath() {
        return this.path;
    }
    
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
            final Vec3 dck2 = this.getTempMobPos();
            final Vec3 dck3 = this.path.getNextEntityPos(this.mob);
            if (dck2.y > dck3.y && !this.mob.isOnGround() && Mth.floor(dck2.x) == Mth.floor(dck3.x) && Mth.floor(dck2.z) == Mth.floor(dck3.z)) {
                this.path.advance();
            }
        }
        DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
        if (this.isDone()) {
            return;
        }
        final Vec3 dck2 = this.path.getNextEntityPos(this.mob);
        final BlockPos fx3 = new BlockPos(dck2);
        this.mob.getMoveControl().setWantedPosition(dck2.x, this.level.getBlockState(fx3.below()).isAir() ? dck2.y : WalkNodeEvaluator.getFloorLevel(this.level, fx3), dck2.z, this.speedModifier);
    }
    
    protected void followThePath() {
        final Vec3 dck2 = this.getTempMobPos();
        this.maxDistanceToWaypoint = ((this.mob.getBbWidth() > 0.75f) ? (this.mob.getBbWidth() / 2.0f) : (0.75f - this.mob.getBbWidth() / 2.0f));
        final Vec3i gr3 = this.path.getNextNodePos();
        final double double4 = Math.abs(this.mob.getX() - (gr3.getX() + 0.5));
        final double double5 = Math.abs(this.mob.getY() - gr3.getY());
        final double double6 = Math.abs(this.mob.getZ() - (gr3.getZ() + 0.5));
        final boolean boolean10 = double4 < this.maxDistanceToWaypoint && double6 < this.maxDistanceToWaypoint && double5 < 1.0;
        if (boolean10 || (this.mob.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(dck2))) {
            this.path.advance();
        }
        this.doStuckDetection(dck2);
    }
    
    private boolean shouldTargetNextNodeInDirection(final Vec3 dck) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        }
        final Vec3 dck2 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
        if (!dck.closerThan(dck2, 2.0)) {
            return false;
        }
        final Vec3 dck3 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
        final Vec3 dck4 = dck3.subtract(dck2);
        final Vec3 dck5 = dck.subtract(dck2);
        return dck4.dot(dck5) > 0.0;
    }
    
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
                final double double4 = dck.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = ((this.mob.getSpeed() > 0.0f) ? (double4 / this.mob.getSpeed() * 1000.0) : 0.0);
            }
            if (this.timeoutLimit > 0.0 && this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.resetStuckTimeout();
                this.stop();
            }
            this.lastTimeoutCheck = Util.getMillis();
        }
    }
    
    private void resetStuckTimeout() {
        this.timeoutCachedNode = Vec3i.ZERO;
        this.timeoutTimer = 0L;
        this.timeoutLimit = 0.0;
    }
    
    public boolean isDone() {
        return this.path == null || this.path.isDone();
    }
    
    public boolean isInProgress() {
        return !this.isDone();
    }
    
    public void stop() {
        this.path = null;
    }
    
    protected abstract Vec3 getTempMobPos();
    
    protected abstract boolean canUpdatePath();
    
    protected boolean isInLiquid() {
        return this.mob.isInWaterOrBubble() || this.mob.isInLava();
    }
    
    protected void trimPath() {
        if (this.path == null) {
            return;
        }
        for (int integer2 = 0; integer2 < this.path.getNodeCount(); ++integer2) {
            final Node cwy3 = this.path.getNode(integer2);
            final Node cwy4 = (integer2 + 1 < this.path.getNodeCount()) ? this.path.getNode(integer2 + 1) : null;
            final BlockState cee5 = this.level.getBlockState(new BlockPos(cwy3.x, cwy3.y, cwy3.z));
            if (cee5.is(Blocks.CAULDRON)) {
                this.path.replaceNode(integer2, cwy3.cloneAndMove(cwy3.x, cwy3.y + 1, cwy3.z));
                if (cwy4 != null && cwy3.y >= cwy4.y) {
                    this.path.replaceNode(integer2 + 1, cwy3.cloneAndMove(cwy4.x, cwy3.y + 1, cwy4.z));
                }
            }
        }
    }
    
    protected abstract boolean canMoveDirectly(final Vec3 dck1, final Vec3 dck2, final int integer3, final int integer4, final int integer5);
    
    public boolean isStableDestination(final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        return this.level.getBlockState(fx2).isSolidRender(this.level, fx2);
    }
    
    public NodeEvaluator getNodeEvaluator() {
        return this.nodeEvaluator;
    }
    
    public void setCanFloat(final boolean boolean1) {
        this.nodeEvaluator.setCanFloat(boolean1);
    }
    
    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }
    
    public void recomputePath(final BlockPos fx) {
        if (this.path == null || this.path.isDone() || this.path.getNodeCount() == 0) {
            return;
        }
        final Node cwy3 = this.path.getEndNode();
        final Vec3 dck4 = new Vec3((cwy3.x + this.mob.getX()) / 2.0, (cwy3.y + this.mob.getY()) / 2.0, (cwy3.z + this.mob.getZ()) / 2.0);
        if (fx.closerThan(dck4, this.path.getNodeCount() - this.path.getNextNodeIndex())) {
            this.recomputePath();
        }
    }
}
