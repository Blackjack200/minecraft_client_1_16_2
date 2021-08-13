package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Set;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.AABB;
import net.minecraft.client.renderer.RenderType;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Comparator;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import net.minecraft.core.BlockPos;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class NeighborsUpdateRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private final Map<Long, Map<BlockPos, Integer>> lastUpdate;
    
    NeighborsUpdateRenderer(final Minecraft djw) {
        this.lastUpdate = (Map<Long, Map<BlockPos, Integer>>)Maps.newTreeMap((Comparator)Ordering.natural().reverse());
        this.minecraft = djw;
    }
    
    public void addUpdate(final long long1, final BlockPos fx) {
        final Map<BlockPos, Integer> map5 = (Map<BlockPos, Integer>)this.lastUpdate.computeIfAbsent(long1, long1 -> Maps.newHashMap());
        final int integer6 = (int)map5.getOrDefault(fx, 0);
        map5.put(fx, (integer6 + 1));
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final long long10 = this.minecraft.level.getGameTime();
        final int integer12 = 200;
        final double double6 = 0.0025;
        final Set<BlockPos> set15 = (Set<BlockPos>)Sets.newHashSet();
        final Map<BlockPos, Integer> map16 = (Map<BlockPos, Integer>)Maps.newHashMap();
        final VertexConsumer dfn17 = dzy.getBuffer(RenderType.lines());
        final Iterator<Map.Entry<Long, Map<BlockPos, Integer>>> iterator18 = (Iterator<Map.Entry<Long, Map<BlockPos, Integer>>>)this.lastUpdate.entrySet().iterator();
        while (iterator18.hasNext()) {
            final Map.Entry<Long, Map<BlockPos, Integer>> entry19 = (Map.Entry<Long, Map<BlockPos, Integer>>)iterator18.next();
            final Long long11 = (Long)entry19.getKey();
            final Map<BlockPos, Integer> map17 = (Map<BlockPos, Integer>)entry19.getValue();
            final long long12 = long10 - long11;
            if (long12 > 200L) {
                iterator18.remove();
            }
            else {
                for (final Map.Entry<BlockPos, Integer> entry20 : map17.entrySet()) {
                    final BlockPos fx26 = (BlockPos)entry20.getKey();
                    final Integer integer13 = (Integer)entry20.getValue();
                    if (set15.add(fx26)) {
                        final AABB dcf28 = new AABB(BlockPos.ZERO).inflate(0.002).deflate(0.0025 * long12).move(fx26.getX(), fx26.getY(), fx26.getZ()).move(-double3, -double4, -double5);
                        LevelRenderer.renderLineBox(dfj, dfn17, dcf28.minX, dcf28.minY, dcf28.minZ, dcf28.maxX, dcf28.maxY, dcf28.maxZ, 1.0f, 1.0f, 1.0f, 1.0f);
                        map16.put(fx26, integer13);
                    }
                }
            }
        }
        for (final Map.Entry<BlockPos, Integer> entry21 : map16.entrySet()) {
            final BlockPos fx27 = (BlockPos)entry21.getKey();
            final Integer integer14 = (Integer)entry21.getValue();
            DebugRenderer.renderFloatingText(String.valueOf(integer14), fx27.getX(), fx27.getY(), fx27.getZ(), -1);
        }
    }
}
