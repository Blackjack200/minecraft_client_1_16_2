package net.minecraft.client.renderer.debug;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.client.multiplayer.ClientLevel;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.CompletableFuture;
import java.util.Iterator;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.level.ChunkPos;
import java.util.Map;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public class ChunkDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private double lastUpdateTime;
    private final int radius = 12;
    @Nullable
    private ChunkData data;
    
    public ChunkDebugRenderer(final Minecraft djw) {
        this.lastUpdateTime = Double.MIN_VALUE;
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final double double6 = (double)Util.getNanos();
        if (double6 - this.lastUpdateTime > 3.0E9) {
            this.lastUpdateTime = double6;
            final IntegratedServer emy12 = this.minecraft.getSingleplayerServer();
            if (emy12 != null) {
                this.data = new ChunkData(emy12, double3, double5);
            }
            else {
                this.data = null;
            }
        }
        if (this.data != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.lineWidth(2.0f);
            RenderSystem.disableTexture();
            RenderSystem.depthMask(false);
            final Map<ChunkPos, String> map12 = (Map<ChunkPos, String>)this.data.serverData.getNow(null);
            final double double7 = this.minecraft.gameRenderer.getMainCamera().getPosition().y * 0.85;
            for (final Map.Entry<ChunkPos, String> entry16 : this.data.clientData.entrySet()) {
                final ChunkPos bra17 = (ChunkPos)entry16.getKey();
                String string18 = (String)entry16.getValue();
                if (map12 != null) {
                    string18 += (String)map12.get(bra17);
                }
                final String[] arr19 = string18.split("\n");
                int integer20 = 0;
                for (final String string19 : arr19) {
                    DebugRenderer.renderFloatingText(string19, (bra17.x << 4) + 8, double7 + integer20, (bra17.z << 4) + 8, -1, 0.15f);
                    integer20 -= 2;
                }
            }
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
        }
    }
    
    final class ChunkData {
        private final Map<ChunkPos, String> clientData;
        private final CompletableFuture<Map<ChunkPos, String>> serverData;
        
        private ChunkData(final IntegratedServer emy, final double double3, final double double4) {
            final ClientLevel dwl8 = ChunkDebugRenderer.this.minecraft.level;
            final ResourceKey<Level> vj9 = dwl8.dimension();
            final int integer10 = (int)double3 >> 4;
            final int integer11 = (int)double4 >> 4;
            final ImmutableMap.Builder<ChunkPos, String> builder12 = (ImmutableMap.Builder<ChunkPos, String>)ImmutableMap.builder();
            final ClientChunkCache dwj13 = dwl8.getChunkSource();
            for (int integer12 = integer10 - 12; integer12 <= integer10 + 12; ++integer12) {
                for (int integer13 = integer11 - 12; integer13 <= integer11 + 12; ++integer13) {
                    final ChunkPos bra16 = new ChunkPos(integer12, integer13);
                    String string17 = "";
                    final LevelChunk cge18 = dwj13.getChunk(integer12, integer13, false);
                    string17 += "Client: ";
                    if (cge18 == null) {
                        string17 += "0n/a\n";
                    }
                    else {
                        string17 += (cge18.isEmpty() ? " E" : "");
                        string17 += "\n";
                    }
                    builder12.put(bra16, string17);
                }
            }
            this.clientData = (Map<ChunkPos, String>)builder12.build();
            this.serverData = emy.<Map<ChunkPos, String>>submit((java.util.function.Supplier<Map<ChunkPos, String>>)(() -> {
                final ServerLevel aag6 = emy.getLevel(vj9);
                if (aag6 == null) {
                    return ImmutableMap.of();
                }
                final ImmutableMap.Builder<ChunkPos, String> builder7 = (ImmutableMap.Builder<ChunkPos, String>)ImmutableMap.builder();
                final ServerChunkCache aae8 = aag6.getChunkSource();
                for (int integer5 = integer10 - 12; integer5 <= integer10 + 12; ++integer5) {
                    for (int integer6 = integer11 - 12; integer6 <= integer11 + 12; ++integer6) {
                        final ChunkPos bra11 = new ChunkPos(integer5, integer6);
                        builder7.put(bra11, ("Server: " + aae8.getChunkDebugData(bra11)));
                    }
                }
                return builder7.build();
            }));
        }
    }
}
