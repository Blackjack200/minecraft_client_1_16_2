package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import com.google.common.collect.Lists;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundLightUpdatePacket implements Packet<ClientGamePacketListener> {
    private int x;
    private int z;
    private int skyYMask;
    private int blockYMask;
    private int emptySkyYMask;
    private int emptyBlockYMask;
    private List<byte[]> skyUpdates;
    private List<byte[]> blockUpdates;
    private boolean trustEdges;
    
    public ClientboundLightUpdatePacket() {
    }
    
    public ClientboundLightUpdatePacket(final ChunkPos bra, final LevelLightEngine cul, final boolean boolean3) {
        this.x = bra.x;
        this.z = bra.z;
        this.trustEdges = boolean3;
        this.skyUpdates = (List<byte[]>)Lists.newArrayList();
        this.blockUpdates = (List<byte[]>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < 18; ++integer5) {
            final DataLayer cfy6 = cul.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(bra, -1 + integer5));
            final DataLayer cfy7 = cul.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(bra, -1 + integer5));
            if (cfy6 != null) {
                if (cfy6.isEmpty()) {
                    this.emptySkyYMask |= 1 << integer5;
                }
                else {
                    this.skyYMask |= 1 << integer5;
                    this.skyUpdates.add(cfy6.getData().clone());
                }
            }
            if (cfy7 != null) {
                if (cfy7.isEmpty()) {
                    this.emptyBlockYMask |= 1 << integer5;
                }
                else {
                    this.blockYMask |= 1 << integer5;
                    this.blockUpdates.add(cfy7.getData().clone());
                }
            }
        }
    }
    
    public ClientboundLightUpdatePacket(final ChunkPos bra, final LevelLightEngine cul, final int integer3, final int integer4, final boolean boolean5) {
        this.x = bra.x;
        this.z = bra.z;
        this.trustEdges = boolean5;
        this.skyYMask = integer3;
        this.blockYMask = integer4;
        this.skyUpdates = (List<byte[]>)Lists.newArrayList();
        this.blockUpdates = (List<byte[]>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < 18; ++integer5) {
            if ((this.skyYMask & 1 << integer5) != 0x0) {
                final DataLayer cfy8 = cul.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(bra, -1 + integer5));
                if (cfy8 == null || cfy8.isEmpty()) {
                    this.skyYMask &= ~(1 << integer5);
                    if (cfy8 != null) {
                        this.emptySkyYMask |= 1 << integer5;
                    }
                }
                else {
                    this.skyUpdates.add(cfy8.getData().clone());
                }
            }
            if ((this.blockYMask & 1 << integer5) != 0x0) {
                final DataLayer cfy8 = cul.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(bra, -1 + integer5));
                if (cfy8 == null || cfy8.isEmpty()) {
                    this.blockYMask &= ~(1 << integer5);
                    if (cfy8 != null) {
                        this.emptyBlockYMask |= 1 << integer5;
                    }
                }
                else {
                    this.blockUpdates.add(cfy8.getData().clone());
                }
            }
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readVarInt();
        this.z = nf.readVarInt();
        this.trustEdges = nf.readBoolean();
        this.skyYMask = nf.readVarInt();
        this.blockYMask = nf.readVarInt();
        this.emptySkyYMask = nf.readVarInt();
        this.emptyBlockYMask = nf.readVarInt();
        this.skyUpdates = (List<byte[]>)Lists.newArrayList();
        for (int integer3 = 0; integer3 < 18; ++integer3) {
            if ((this.skyYMask & 1 << integer3) != 0x0) {
                this.skyUpdates.add(nf.readByteArray(2048));
            }
        }
        this.blockUpdates = (List<byte[]>)Lists.newArrayList();
        for (int integer3 = 0; integer3 < 18; ++integer3) {
            if ((this.blockYMask & 1 << integer3) != 0x0) {
                this.blockUpdates.add(nf.readByteArray(2048));
            }
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.x);
        nf.writeVarInt(this.z);
        nf.writeBoolean(this.trustEdges);
        nf.writeVarInt(this.skyYMask);
        nf.writeVarInt(this.blockYMask);
        nf.writeVarInt(this.emptySkyYMask);
        nf.writeVarInt(this.emptyBlockYMask);
        for (final byte[] arr4 : this.skyUpdates) {
            nf.writeByteArray(arr4);
        }
        for (final byte[] arr4 : this.blockUpdates) {
            nf.writeByteArray(arr4);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleLightUpdatePacked(this);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public int getSkyYMask() {
        return this.skyYMask;
    }
    
    public int getEmptySkyYMask() {
        return this.emptySkyYMask;
    }
    
    public List<byte[]> getSkyUpdates() {
        return this.skyUpdates;
    }
    
    public int getBlockYMask() {
        return this.blockYMask;
    }
    
    public int getEmptyBlockYMask() {
        return this.emptyBlockYMask;
    }
    
    public List<byte[]> getBlockUpdates() {
        return this.blockUpdates;
    }
    
    public boolean getTrustEdges() {
        return this.trustEdges;
    }
}
