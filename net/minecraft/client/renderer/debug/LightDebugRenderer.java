package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.Level;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class LightDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    
    public LightDebugRenderer(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final Level bru10 = this.minecraft.level;
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        final BlockPos fx11 = new BlockPos(double3, double4, double5);
        final LongSet longSet12 = (LongSet)new LongOpenHashSet();
        for (final BlockPos fx12 : BlockPos.betweenClosed(fx11.offset(-10, -10, -10), fx11.offset(10, 10, 10))) {
            final int integer15 = bru10.getBrightness(LightLayer.SKY, fx12);
            final float float16 = (15 - integer15) / 15.0f * 0.5f + 0.16f;
            final int integer16 = Mth.hsvToRgb(float16, 0.9f, 0.9f);
            final long long18 = SectionPos.blockToSection(fx12.asLong());
            if (longSet12.add(long18)) {
                DebugRenderer.renderFloatingText(bru10.getChunkSource().getLightEngine().getDebugData(LightLayer.SKY, SectionPos.of(long18)), SectionPos.x(long18) * 16 + 8, SectionPos.y(long18) * 16 + 8, SectionPos.z(long18) * 16 + 8, 16711680, 0.3f);
            }
            if (integer15 != 15) {
                DebugRenderer.renderFloatingText(String.valueOf(integer15), fx12.getX() + 0.5, fx12.getY() + 0.25, fx12.getZ() + 0.5, integer16);
            }
        }
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
    }
}
