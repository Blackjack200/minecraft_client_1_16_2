package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.BlockGetter;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class SolidFaceRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    
    public SolidFaceRenderer(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final BlockGetter bqz10 = this.minecraft.player.level;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(2.0f);
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        final BlockPos fx11 = new BlockPos(double3, double4, double5);
        for (final BlockPos fx12 : BlockPos.betweenClosed(fx11.offset(-6, -6, -6), fx11.offset(6, 6, 6))) {
            final BlockState cee14 = bqz10.getBlockState(fx12);
            if (cee14.is(Blocks.AIR)) {
                continue;
            }
            final VoxelShape dde15 = cee14.getShape(bqz10, fx12);
            for (final AABB dcf17 : dde15.toAabbs()) {
                final AABB dcf18 = dcf17.move(fx12).inflate(0.002).move(-double3, -double4, -double5);
                final double double6 = dcf18.minX;
                final double double7 = dcf18.minY;
                final double double8 = dcf18.minZ;
                final double double9 = dcf18.maxX;
                final double double10 = dcf18.maxY;
                final double double11 = dcf18.maxZ;
                final float float31 = 1.0f;
                final float float32 = 0.0f;
                final float float33 = 0.0f;
                final float float34 = 0.5f;
                if (cee14.isFaceSturdy(bqz10, fx12, Direction.WEST)) {
                    final Tesselator dfl35 = Tesselator.getInstance();
                    final BufferBuilder dfe36 = dfl35.getBuilder();
                    dfe36.begin(5, DefaultVertexFormat.POSITION_COLOR);
                    dfe36.vertex(double6, double7, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double7, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double10, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double10, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfl35.end();
                }
                if (cee14.isFaceSturdy(bqz10, fx12, Direction.SOUTH)) {
                    final Tesselator dfl35 = Tesselator.getInstance();
                    final BufferBuilder dfe36 = dfl35.getBuilder();
                    dfe36.begin(5, DefaultVertexFormat.POSITION_COLOR);
                    dfe36.vertex(double6, double10, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double7, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double10, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double7, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfl35.end();
                }
                if (cee14.isFaceSturdy(bqz10, fx12, Direction.EAST)) {
                    final Tesselator dfl35 = Tesselator.getInstance();
                    final BufferBuilder dfe36 = dfl35.getBuilder();
                    dfe36.begin(5, DefaultVertexFormat.POSITION_COLOR);
                    dfe36.vertex(double9, double7, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double7, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double10, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double10, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfl35.end();
                }
                if (cee14.isFaceSturdy(bqz10, fx12, Direction.NORTH)) {
                    final Tesselator dfl35 = Tesselator.getInstance();
                    final BufferBuilder dfe36 = dfl35.getBuilder();
                    dfe36.begin(5, DefaultVertexFormat.POSITION_COLOR);
                    dfe36.vertex(double9, double10, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double7, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double10, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double7, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfl35.end();
                }
                if (cee14.isFaceSturdy(bqz10, fx12, Direction.DOWN)) {
                    final Tesselator dfl35 = Tesselator.getInstance();
                    final BufferBuilder dfe36 = dfl35.getBuilder();
                    dfe36.begin(5, DefaultVertexFormat.POSITION_COLOR);
                    dfe36.vertex(double6, double7, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double7, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double7, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double7, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfl35.end();
                }
                if (cee14.isFaceSturdy(bqz10, fx12, Direction.UP)) {
                    final Tesselator dfl35 = Tesselator.getInstance();
                    final BufferBuilder dfe36 = dfl35.getBuilder();
                    dfe36.begin(5, DefaultVertexFormat.POSITION_COLOR);
                    dfe36.vertex(double6, double10, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double6, double10, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double10, double8).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfe36.vertex(double9, double10, double11).color(1.0f, 0.0f, 0.0f, 0.5f).endVertex();
                    dfl35.end();
                }
            }
        }
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
