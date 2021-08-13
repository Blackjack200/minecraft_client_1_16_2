package net.minecraft.client.renderer.debug;

import net.minecraft.world.level.material.FluidState;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class WaterDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    
    public WaterDebugRenderer(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final BlockPos fx10 = this.minecraft.player.blockPosition();
        final LevelReader brw11 = this.minecraft.player.level;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(0.0f, 1.0f, 0.0f, 0.75f);
        RenderSystem.disableTexture();
        RenderSystem.lineWidth(6.0f);
        for (final BlockPos fx11 : BlockPos.betweenClosed(fx10.offset(-10, -10, -10), fx10.offset(10, 10, 10))) {
            final FluidState cuu14 = brw11.getFluidState(fx11);
            if (cuu14.is(FluidTags.WATER)) {
                final double double6 = fx11.getY() + cuu14.getHeight(brw11, fx11);
                DebugRenderer.renderFilledBox(new AABB(fx11.getX() + 0.01f, fx11.getY() + 0.01f, fx11.getZ() + 0.01f, fx11.getX() + 0.99f, double6, fx11.getZ() + 0.99f).move(-double3, -double4, -double5), 1.0f, 1.0f, 1.0f, 0.2f);
            }
        }
        for (final BlockPos fx11 : BlockPos.betweenClosed(fx10.offset(-10, -10, -10), fx10.offset(10, 10, 10))) {
            final FluidState cuu14 = brw11.getFluidState(fx11);
            if (cuu14.is(FluidTags.WATER)) {
                DebugRenderer.renderFloatingText(String.valueOf(cuu14.getAmount()), fx11.getX() + 0.5, fx11.getY() + cuu14.getHeight(brw11, fx11), fx11.getZ() + 0.5, -16777216);
            }
        }
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
