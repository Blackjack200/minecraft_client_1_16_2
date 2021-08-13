package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.core.BlockPos;
import java.util.Locale;
import net.minecraft.world.phys.AABB;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.world.level.pathfinder.Path;
import java.util.Map;

public class PathfindingRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Map<Integer, Path> pathMap;
    private final Map<Integer, Float> pathMaxDist;
    private final Map<Integer, Long> creationMap;
    
    public PathfindingRenderer() {
        this.pathMap = (Map<Integer, Path>)Maps.newHashMap();
        this.pathMaxDist = (Map<Integer, Float>)Maps.newHashMap();
        this.creationMap = (Map<Integer, Long>)Maps.newHashMap();
    }
    
    public void addPath(final int integer, final Path cxa, final float float3) {
        this.pathMap.put(integer, cxa);
        this.creationMap.put(integer, Util.getMillis());
        this.pathMaxDist.put(integer, float3);
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        if (this.pathMap.isEmpty()) {
            return;
        }
        final long long10 = Util.getMillis();
        for (final Integer integer13 : this.pathMap.keySet()) {
            final Path cxa14 = (Path)this.pathMap.get(integer13);
            final float float15 = (float)this.pathMaxDist.get(integer13);
            renderPath(cxa14, float15, true, true, double3, double4, double5);
        }
        for (final Integer integer14 : (Integer[])this.creationMap.keySet().toArray((Object[])new Integer[0])) {
            if (long10 - (long)this.creationMap.get(integer14) > 5000L) {
                this.pathMap.remove(integer14);
                this.creationMap.remove(integer14);
            }
        }
    }
    
    public static void renderPath(final Path cxa, final float float2, final boolean boolean3, final boolean boolean4, final double double5, final double double6, final double double7) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(0.0f, 1.0f, 0.0f, 0.75f);
        RenderSystem.disableTexture();
        RenderSystem.lineWidth(6.0f);
        doRenderPath(cxa, float2, boolean3, boolean4, double5, double6, double7);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
    
    private static void doRenderPath(final Path cxa, final float float2, final boolean boolean3, final boolean boolean4, final double double5, final double double6, final double double7) {
        renderPathLine(cxa, double5, double6, double7);
        final BlockPos fx11 = cxa.getTarget();
        if (distanceToCamera(fx11, double5, double6, double7) <= 80.0f) {
            DebugRenderer.renderFilledBox(new AABB(fx11.getX() + 0.25f, fx11.getY() + 0.25f, fx11.getZ() + 0.25, fx11.getX() + 0.75f, fx11.getY() + 0.75f, fx11.getZ() + 0.75f).move(-double5, -double6, -double7), 0.0f, 1.0f, 0.0f, 0.5f);
            for (int integer12 = 0; integer12 < cxa.getNodeCount(); ++integer12) {
                final Node cwy13 = cxa.getNode(integer12);
                if (distanceToCamera(cwy13.asBlockPos(), double5, double6, double7) <= 80.0f) {
                    final float float3 = (integer12 == cxa.getNextNodeIndex()) ? 1.0f : 0.0f;
                    final float float4 = (integer12 == cxa.getNextNodeIndex()) ? 0.0f : 1.0f;
                    DebugRenderer.renderFilledBox(new AABB(cwy13.x + 0.5f - float2, cwy13.y + 0.01f * integer12, cwy13.z + 0.5f - float2, cwy13.x + 0.5f + float2, cwy13.y + 0.25f + 0.01f * integer12, cwy13.z + 0.5f + float2).move(-double5, -double6, -double7), float3, 0.0f, float4, 0.5f);
                }
            }
        }
        if (boolean3) {
            for (final Node cwy14 : cxa.getClosedSet()) {
                if (distanceToCamera(cwy14.asBlockPos(), double5, double6, double7) <= 80.0f) {
                    DebugRenderer.renderFilledBox(new AABB(cwy14.x + 0.5f - float2 / 2.0f, cwy14.y + 0.01f, cwy14.z + 0.5f - float2 / 2.0f, cwy14.x + 0.5f + float2 / 2.0f, cwy14.y + 0.1, cwy14.z + 0.5f + float2 / 2.0f).move(-double5, -double6, -double7), 1.0f, 0.8f, 0.8f, 0.5f);
                }
            }
            for (final Node cwy14 : cxa.getOpenSet()) {
                if (distanceToCamera(cwy14.asBlockPos(), double5, double6, double7) <= 80.0f) {
                    DebugRenderer.renderFilledBox(new AABB(cwy14.x + 0.5f - float2 / 2.0f, cwy14.y + 0.01f, cwy14.z + 0.5f - float2 / 2.0f, cwy14.x + 0.5f + float2 / 2.0f, cwy14.y + 0.1, cwy14.z + 0.5f + float2 / 2.0f).move(-double5, -double6, -double7), 0.8f, 1.0f, 1.0f, 0.5f);
                }
            }
        }
        if (boolean4) {
            for (int integer12 = 0; integer12 < cxa.getNodeCount(); ++integer12) {
                final Node cwy13 = cxa.getNode(integer12);
                if (distanceToCamera(cwy13.asBlockPos(), double5, double6, double7) <= 80.0f) {
                    DebugRenderer.renderFloatingText(String.format("%s", new Object[] { cwy13.type }), cwy13.x + 0.5, cwy13.y + 0.75, cwy13.z + 0.5, -1, 0.02f, true, 0.0f, true);
                    DebugRenderer.renderFloatingText(String.format(Locale.ROOT, "%.2f", new Object[] { cwy13.costMalus }), cwy13.x + 0.5, cwy13.y + 0.25, cwy13.z + 0.5, -1, 0.02f, true, 0.0f, true);
                }
            }
        }
    }
    
    public static void renderPathLine(final Path cxa, final double double2, final double double3, final double double4) {
        final Tesselator dfl8 = Tesselator.getInstance();
        final BufferBuilder dfe9 = dfl8.getBuilder();
        dfe9.begin(3, DefaultVertexFormat.POSITION_COLOR);
        for (int integer10 = 0; integer10 < cxa.getNodeCount(); ++integer10) {
            final Node cwy11 = cxa.getNode(integer10);
            if (distanceToCamera(cwy11.asBlockPos(), double2, double3, double4) <= 80.0f) {
                final float float12 = integer10 / (float)cxa.getNodeCount() * 0.33f;
                final int integer11 = (integer10 == 0) ? 0 : Mth.hsvToRgb(float12, 0.9f, 0.9f);
                final int integer12 = integer11 >> 16 & 0xFF;
                final int integer13 = integer11 >> 8 & 0xFF;
                final int integer14 = integer11 & 0xFF;
                dfe9.vertex(cwy11.x - double2 + 0.5, cwy11.y - double3 + 0.5, cwy11.z - double4 + 0.5).color(integer12, integer13, integer14, 255).endVertex();
            }
        }
        dfl8.end();
    }
    
    private static float distanceToCamera(final BlockPos fx, final double double2, final double double3, final double double4) {
        return (float)(Math.abs(fx.getX() - double2) + Math.abs(fx.getY() - double3) + Math.abs(fx.getZ() - double4));
    }
}
