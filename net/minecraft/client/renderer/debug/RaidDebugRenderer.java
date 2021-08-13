package net.minecraft.client.renderer.debug;

import net.minecraft.client.Camera;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import java.util.Collection;
import net.minecraft.client.Minecraft;

public class RaidDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private Collection<BlockPos> raidCenters;
    
    public RaidDebugRenderer(final Minecraft djw) {
        this.raidCenters = (Collection<BlockPos>)Lists.newArrayList();
        this.minecraft = djw;
    }
    
    public void setRaidCenters(final Collection<BlockPos> collection) {
        this.raidCenters = collection;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final BlockPos fx10 = this.getCamera().getBlockPosition();
        for (final BlockPos fx11 : this.raidCenters) {
            if (fx10.closerThan(fx11, 160.0)) {
                highlightRaidCenter(fx11);
            }
        }
    }
    
    private static void highlightRaidCenter(final BlockPos fx) {
        DebugRenderer.renderFilledBox(fx.offset(-0.5, -0.5, -0.5), fx.offset(1.5, 1.5, 1.5), 1.0f, 0.0f, 0.0f, 0.15f);
        final int integer2 = -65536;
        renderTextOverBlock("Raid center", fx, -65536);
    }
    
    private static void renderTextOverBlock(final String string, final BlockPos fx, final int integer) {
        final double double4 = fx.getX() + 0.5;
        final double double5 = fx.getY() + 1.3;
        final double double6 = fx.getZ() + 0.5;
        DebugRenderer.renderFloatingText(string, double4, double5, double6, integer, 0.04f, true, 0.0f, true);
    }
    
    private Camera getCamera() {
        return this.minecraft.gameRenderer.getMainCamera();
    }
}
