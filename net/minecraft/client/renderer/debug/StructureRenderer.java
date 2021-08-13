package net.minecraft.client.renderer.debug;

import java.util.List;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class StructureRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private final Map<DimensionType, Map<String, BoundingBox>> postMainBoxes;
    private final Map<DimensionType, Map<String, BoundingBox>> postPiecesBoxes;
    private final Map<DimensionType, Map<String, Boolean>> startPiecesMap;
    
    public StructureRenderer(final Minecraft djw) {
        this.postMainBoxes = (Map<DimensionType, Map<String, BoundingBox>>)Maps.newIdentityHashMap();
        this.postPiecesBoxes = (Map<DimensionType, Map<String, BoundingBox>>)Maps.newIdentityHashMap();
        this.startPiecesMap = (Map<DimensionType, Map<String, Boolean>>)Maps.newIdentityHashMap();
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final Camera djh10 = this.minecraft.gameRenderer.getMainCamera();
        final LevelAccessor brv11 = this.minecraft.level;
        final DimensionType cha12 = brv11.dimensionType();
        final BlockPos fx13 = new BlockPos(djh10.getPosition().x, 0.0, djh10.getPosition().z);
        final VertexConsumer dfn14 = dzy.getBuffer(RenderType.lines());
        if (this.postMainBoxes.containsKey(cha12)) {
            for (final BoundingBox cqx16 : ((Map)this.postMainBoxes.get(cha12)).values()) {
                if (fx13.closerThan(cqx16.getCenter(), 500.0)) {
                    LevelRenderer.renderLineBox(dfj, dfn14, cqx16.x0 - double3, cqx16.y0 - double4, cqx16.z0 - double5, cqx16.x1 + 1 - double3, cqx16.y1 + 1 - double4, cqx16.z1 + 1 - double5, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
        if (this.postPiecesBoxes.containsKey(cha12)) {
            for (final Map.Entry<String, BoundingBox> entry16 : ((Map)this.postPiecesBoxes.get(cha12)).entrySet()) {
                final String string17 = (String)entry16.getKey();
                final BoundingBox cqx17 = (BoundingBox)entry16.getValue();
                final Boolean boolean19 = (Boolean)((Map)this.startPiecesMap.get(cha12)).get(string17);
                if (fx13.closerThan(cqx17.getCenter(), 500.0)) {
                    if (boolean19) {
                        LevelRenderer.renderLineBox(dfj, dfn14, cqx17.x0 - double3, cqx17.y0 - double4, cqx17.z0 - double5, cqx17.x1 + 1 - double3, cqx17.y1 + 1 - double4, cqx17.z1 + 1 - double5, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f);
                    }
                    else {
                        LevelRenderer.renderLineBox(dfj, dfn14, cqx17.x0 - double3, cqx17.y0 - double4, cqx17.z0 - double5, cqx17.x1 + 1 - double3, cqx17.y1 + 1 - double4, cqx17.z1 + 1 - double5, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f);
                    }
                }
            }
        }
    }
    
    public void addBoundingBox(final BoundingBox cqx, final List<BoundingBox> list2, final List<Boolean> list3, final DimensionType cha) {
        if (!this.postMainBoxes.containsKey(cha)) {
            this.postMainBoxes.put(cha, Maps.newHashMap());
        }
        if (!this.postPiecesBoxes.containsKey(cha)) {
            this.postPiecesBoxes.put(cha, Maps.newHashMap());
            this.startPiecesMap.put(cha, Maps.newHashMap());
        }
        ((Map)this.postMainBoxes.get(cha)).put(cqx.toString(), cqx);
        for (int integer6 = 0; integer6 < list2.size(); ++integer6) {
            final BoundingBox cqx2 = (BoundingBox)list2.get(integer6);
            final Boolean boolean8 = (Boolean)list3.get(integer6);
            ((Map)this.postPiecesBoxes.get(cha)).put(cqx2.toString(), cqx2);
            ((Map)this.startPiecesMap.get(cha)).put(cqx2.toString(), boolean8);
        }
    }
    
    public void clear() {
        this.postMainBoxes.clear();
        this.postPiecesBoxes.clear();
        this.startPiecesMap.clear();
    }
}
