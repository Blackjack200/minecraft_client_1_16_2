package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Vec3i;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import net.minecraft.core.BlockPos;
import java.util.Map;

public class CaveDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Map<BlockPos, BlockPos> tunnelsList;
    private final Map<BlockPos, Float> thicknessMap;
    private final List<BlockPos> startPoses;
    
    public CaveDebugRenderer() {
        this.tunnelsList = (Map<BlockPos, BlockPos>)Maps.newHashMap();
        this.thicknessMap = (Map<BlockPos, Float>)Maps.newHashMap();
        this.startPoses = (List<BlockPos>)Lists.newArrayList();
    }
    
    public void addTunnel(final BlockPos fx, final List<BlockPos> list2, final List<Float> list3) {
        for (int integer5 = 0; integer5 < list2.size(); ++integer5) {
            this.tunnelsList.put(list2.get(integer5), fx);
            this.thicknessMap.put(list2.get(integer5), list3.get(integer5));
        }
        this.startPoses.add(fx);
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        final BlockPos fx10 = new BlockPos(double3, 0.0, double5);
        final Tesselator dfl11 = Tesselator.getInstance();
        final BufferBuilder dfe12 = dfl11.getBuilder();
        dfe12.begin(5, DefaultVertexFormat.POSITION_COLOR);
        for (final Map.Entry<BlockPos, BlockPos> entry14 : this.tunnelsList.entrySet()) {
            final BlockPos fx11 = (BlockPos)entry14.getKey();
            final BlockPos fx12 = (BlockPos)entry14.getValue();
            final float float17 = fx12.getX() * 128 % 256 / 256.0f;
            final float float18 = fx12.getY() * 128 % 256 / 256.0f;
            final float float19 = fx12.getZ() * 128 % 256 / 256.0f;
            final float float20 = (float)this.thicknessMap.get(fx11);
            if (fx10.closerThan(fx11, 160.0)) {
                LevelRenderer.addChainedFilledBoxVertices(dfe12, fx11.getX() + 0.5f - double3 - float20, fx11.getY() + 0.5f - double4 - float20, fx11.getZ() + 0.5f - double5 - float20, fx11.getX() + 0.5f - double3 + float20, fx11.getY() + 0.5f - double4 + float20, fx11.getZ() + 0.5f - double5 + float20, float17, float18, float19, 0.5f);
            }
        }
        for (final BlockPos fx13 : this.startPoses) {
            if (fx10.closerThan(fx13, 160.0)) {
                LevelRenderer.addChainedFilledBoxVertices(dfe12, fx13.getX() - double3, fx13.getY() - double4, fx13.getZ() - double5, fx13.getX() + 1.0f - double3, fx13.getY() + 1.0f - double4, fx13.getZ() + 1.0f - double5, 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        dfl11.end();
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
    }
}
