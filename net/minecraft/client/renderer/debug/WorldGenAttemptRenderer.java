package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import java.util.List;

public class WorldGenAttemptRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final List<BlockPos> toRender;
    private final List<Float> scales;
    private final List<Float> alphas;
    private final List<Float> reds;
    private final List<Float> greens;
    private final List<Float> blues;
    
    public WorldGenAttemptRenderer() {
        this.toRender = (List<BlockPos>)Lists.newArrayList();
        this.scales = (List<Float>)Lists.newArrayList();
        this.alphas = (List<Float>)Lists.newArrayList();
        this.reds = (List<Float>)Lists.newArrayList();
        this.greens = (List<Float>)Lists.newArrayList();
        this.blues = (List<Float>)Lists.newArrayList();
    }
    
    public void addPos(final BlockPos fx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.toRender.add(fx);
        this.scales.add(float2);
        this.alphas.add(float6);
        this.reds.add(float3);
        this.greens.add(float4);
        this.blues.add(float5);
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        final Tesselator dfl10 = Tesselator.getInstance();
        final BufferBuilder dfe11 = dfl10.getBuilder();
        dfe11.begin(5, DefaultVertexFormat.POSITION_COLOR);
        for (int integer12 = 0; integer12 < this.toRender.size(); ++integer12) {
            final BlockPos fx13 = (BlockPos)this.toRender.get(integer12);
            final Float float14 = (Float)this.scales.get(integer12);
            final float float15 = float14 / 2.0f;
            LevelRenderer.addChainedFilledBoxVertices(dfe11, fx13.getX() + 0.5f - float15 - double3, fx13.getY() + 0.5f - float15 - double4, fx13.getZ() + 0.5f - float15 - double5, fx13.getX() + 0.5f + float15 - double3, fx13.getY() + 0.5f + float15 - double4, fx13.getZ() + 0.5f + float15 - double5, (float)this.reds.get(integer12), (float)this.greens.get(integer12), (float)this.blues.get(integer12), (float)this.alphas.get(integer12));
        }
        dfl10.end();
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
    }
}
