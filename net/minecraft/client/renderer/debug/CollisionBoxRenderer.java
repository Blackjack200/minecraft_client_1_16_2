package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.List;
import net.minecraft.client.Minecraft;

public class CollisionBoxRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private double lastUpdateTime;
    private List<VoxelShape> shapes;
    
    public CollisionBoxRenderer(final Minecraft djw) {
        this.lastUpdateTime = Double.MIN_VALUE;
        this.shapes = (List<VoxelShape>)Collections.emptyList();
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final double double6 = (double)Util.getNanos();
        if (double6 - this.lastUpdateTime > 1.0E8) {
            this.lastUpdateTime = double6;
            final Entity apx12 = this.minecraft.gameRenderer.getMainCamera().getEntity();
            this.shapes = (List<VoxelShape>)apx12.level.getCollisions(apx12, apx12.getBoundingBox().inflate(6.0), (Predicate<Entity>)(apx -> true)).collect(Collectors.toList());
        }
        final VertexConsumer dfn12 = dzy.getBuffer(RenderType.lines());
        for (final VoxelShape dde14 : this.shapes) {
            LevelRenderer.renderVoxelShape(dfj, dfn12, dde14, -double3, -double4, -double5, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
