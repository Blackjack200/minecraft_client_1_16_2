package net.minecraft.world.level.pathfinder;

import java.util.List;
import com.google.common.collect.Lists;
import java.util.Optional;
import java.util.Iterator;
import java.util.Comparator;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Map;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;

public class PathFinder {
    private final Node[] neighbors;
    private final int maxVisitedNodes;
    private final NodeEvaluator nodeEvaluator;
    private final BinaryHeap openSet;
    
    public PathFinder(final NodeEvaluator cwz, final int integer) {
        this.neighbors = new Node[32];
        this.openSet = new BinaryHeap();
        this.nodeEvaluator = cwz;
        this.maxVisitedNodes = integer;
    }
    
    @Nullable
    public Path findPath(final PathNavigationRegion bsf, final Mob aqk, final Set<BlockPos> set, final float float4, final int integer, final float float6) {
        this.openSet.clear();
        this.nodeEvaluator.prepare(bsf, aqk);
        final Node cwy8 = this.nodeEvaluator.getStart();
        final Map<Target, BlockPos> map9 = (Map<Target, BlockPos>)set.stream().collect(Collectors.toMap(fx -> this.nodeEvaluator.getGoal(fx.getX(), fx.getY(), fx.getZ()), Function.identity()));
        final Path cxa10 = this.findPath(cwy8, map9, float4, integer, float6);
        this.nodeEvaluator.done();
        return cxa10;
    }
    
    @Nullable
    private Path findPath(final Node cwy, final Map<Target, BlockPos> map, final float float3, final int integer, final float float5) {
        final Set<Target> set7 = (Set<Target>)map.keySet();
        cwy.g = 0.0f;
        cwy.h = this.getBestH(cwy, set7);
        cwy.f = cwy.h;
        this.openSet.clear();
        this.openSet.insert(cwy);
        final Set<Node> set8 = (Set<Node>)ImmutableSet.of();
        int integer2 = 0;
        final Set<Target> set9 = (Set<Target>)Sets.newHashSetWithExpectedSize(set7.size());
        final int integer3 = (int)(this.maxVisitedNodes * float5);
        while (!this.openSet.isEmpty() && ++integer2 < integer3) {
            final Node cwy2 = this.openSet.pop();
            cwy2.closed = true;
            for (final Target cxe14 : set7) {
                if (cwy2.distanceManhattan(cxe14) <= integer) {
                    cxe14.setReached();
                    set9.add(cxe14);
                }
            }
            if (!set9.isEmpty()) {
                break;
            }
            if (cwy2.distanceTo(cwy) >= float3) {
                continue;
            }
            for (int integer4 = this.nodeEvaluator.getNeighbors(this.neighbors, cwy2), integer5 = 0; integer5 < integer4; ++integer5) {
                final Node cwy3 = this.neighbors[integer5];
                final float float6 = cwy2.distanceTo(cwy3);
                cwy3.walkedDistance = cwy2.walkedDistance + float6;
                final float float7 = cwy2.g + float6 + cwy3.costMalus;
                if (cwy3.walkedDistance < float3 && (!cwy3.inOpenSet() || float7 < cwy3.g)) {
                    cwy3.cameFrom = cwy2;
                    cwy3.g = float7;
                    cwy3.h = this.getBestH(cwy3, set7) * 1.5f;
                    if (cwy3.inOpenSet()) {
                        this.openSet.changeCost(cwy3, cwy3.g + cwy3.h);
                    }
                    else {
                        cwy3.f = cwy3.g + cwy3.h;
                        this.openSet.insert(cwy3);
                    }
                }
            }
        }
        final Optional<Path> optional12 = (Optional<Path>)(set9.isEmpty() ? set7.stream().map(cxe -> this.reconstructPath(cxe.getBestNode(), (BlockPos)map.get(cxe), false)).min(Comparator.comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount)) : set9.stream().map(cxe -> this.reconstructPath(cxe.getBestNode(), (BlockPos)map.get(cxe), true)).min(Comparator.comparingInt(Path::getNodeCount)));
        if (!optional12.isPresent()) {
            return null;
        }
        final Path cxa13 = (Path)optional12.get();
        return cxa13;
    }
    
    private float getBestH(final Node cwy, final Set<Target> set) {
        float float4 = Float.MAX_VALUE;
        for (final Target cxe6 : set) {
            final float float5 = cwy.distanceTo(cxe6);
            cxe6.updateBest(float5, cwy);
            float4 = Math.min(float5, float4);
        }
        return float4;
    }
    
    private Path reconstructPath(final Node cwy, final BlockPos fx, final boolean boolean3) {
        final List<Node> list5 = (List<Node>)Lists.newArrayList();
        Node cwy2 = cwy;
        list5.add(0, cwy2);
        while (cwy2.cameFrom != null) {
            cwy2 = cwy2.cameFrom;
            list5.add(0, cwy2);
        }
        return new Path(list5, fx, boolean3);
    }
}
