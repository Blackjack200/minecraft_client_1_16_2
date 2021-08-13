package net.minecraft.client.renderer.debug;

import com.mojang.math.Vector3f;
import net.minecraft.world.level.ChunkPos;
import java.util.Iterator;
import net.minecraft.world.level.chunk.ChunkAccess;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class HeightMapRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    
    public HeightMapRenderer(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final LevelAccessor brv10 = this.minecraft.level;
        RenderSystem.pushMatrix();
        RenderSystem.disableBlend();
        RenderSystem.disableTexture();
        RenderSystem.enableDepthTest();
        final BlockPos fx11 = new BlockPos(double3, 0.0, double5);
        final Tesselator dfl12 = Tesselator.getInstance();
        final BufferBuilder dfe13 = dfl12.getBuilder();
        dfe13.begin(5, DefaultVertexFormat.POSITION_COLOR);
        for (int integer14 = -32; integer14 <= 32; integer14 += 16) {
            for (int integer15 = -32; integer15 <= 32; integer15 += 16) {
                final ChunkAccess cft16 = brv10.getChunk(fx11.offset(integer14, 0, integer15));
                for (final Map.Entry<Heightmap.Types, Heightmap> entry18 : cft16.getHeightmaps()) {
                    final Heightmap.Types a19 = (Heightmap.Types)entry18.getKey();
                    final ChunkPos bra20 = cft16.getPos();
                    final Vector3f g21 = this.getColor(a19);
                    for (int integer16 = 0; integer16 < 16; ++integer16) {
                        for (int integer17 = 0; integer17 < 16; ++integer17) {
                            final int integer18 = bra20.x * 16 + integer16;
                            final int integer19 = bra20.z * 16 + integer17;
                            final float float26 = (float)(brv10.getHeight(a19, integer18, integer19) + a19.ordinal() * 0.09375f - double4);
                            LevelRenderer.addChainedFilledBoxVertices(dfe13, integer18 + 0.25f - double3, float26, integer19 + 0.25f - double5, integer18 + 0.75f - double3, float26 + 0.09375f, integer19 + 0.75f - double5, g21.x(), g21.y(), g21.z(), 1.0f);
                        }
                    }
                }
            }
        }
        dfl12.end();
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
    }
    
    private Vector3f getColor(final Heightmap.Types a) {
        switch (a) {
            case WORLD_SURFACE_WG: {
                return new Vector3f(1.0f, 1.0f, 0.0f);
            }
            case OCEAN_FLOOR_WG: {
                return new Vector3f(1.0f, 0.0f, 1.0f);
            }
            case WORLD_SURFACE: {
                return new Vector3f(0.0f, 0.7f, 0.0f);
            }
            case OCEAN_FLOOR: {
                return new Vector3f(0.0f, 0.0f, 0.5f);
            }
            case MOTION_BLOCKING: {
                return new Vector3f(0.0f, 0.3f, 0.3f);
            }
            case MOTION_BLOCKING_NO_LEAVES: {
                return new Vector3f(0.0f, 0.5f, 0.5f);
            }
            default: {
                return new Vector3f(0.0f, 0.0f, 0.0f);
            }
        }
    }
}
