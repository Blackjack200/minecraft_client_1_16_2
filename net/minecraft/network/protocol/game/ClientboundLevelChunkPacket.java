package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.world.level.chunk.LevelChunkSection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import java.util.Iterator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;

public class ClientboundLevelChunkPacket implements Packet<ClientGamePacketListener> {
    private int x;
    private int z;
    private int availableSections;
    private CompoundTag heightmaps;
    @Nullable
    private int[] biomes;
    private byte[] buffer;
    private List<CompoundTag> blockEntitiesTags;
    private boolean fullChunk;
    
    public ClientboundLevelChunkPacket() {
    }
    
    public ClientboundLevelChunkPacket(final LevelChunk cge, final int integer) {
        final ChunkPos bra4 = cge.getPos();
        this.x = bra4.x;
        this.z = bra4.z;
        this.fullChunk = (integer == 65535);
        this.heightmaps = new CompoundTag();
        for (final Map.Entry<Heightmap.Types, Heightmap> entry6 : cge.getHeightmaps()) {
            if (!((Heightmap.Types)entry6.getKey()).sendToClient()) {
                continue;
            }
            this.heightmaps.put(((Heightmap.Types)entry6.getKey()).getSerializationKey(), new LongArrayTag(((Heightmap)entry6.getValue()).getRawData()));
        }
        if (this.fullChunk) {
            this.biomes = cge.getBiomes().writeBiomes();
        }
        this.buffer = new byte[this.calculateChunkSize(cge, integer)];
        this.availableSections = this.extractChunkData(new FriendlyByteBuf(this.getWriteBuffer()), cge, integer);
        this.blockEntitiesTags = (List<CompoundTag>)Lists.newArrayList();
        for (final Map.Entry<BlockPos, BlockEntity> entry7 : cge.getBlockEntities().entrySet()) {
            final BlockPos fx7 = (BlockPos)entry7.getKey();
            final BlockEntity ccg8 = (BlockEntity)entry7.getValue();
            final int integer2 = fx7.getY() >> 4;
            if (!this.isFullChunk() && (integer & 1 << integer2) == 0x0) {
                continue;
            }
            final CompoundTag md10 = ccg8.getUpdateTag();
            this.blockEntitiesTags.add(md10);
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readInt();
        this.z = nf.readInt();
        this.fullChunk = nf.readBoolean();
        this.availableSections = nf.readVarInt();
        this.heightmaps = nf.readNbt();
        if (this.fullChunk) {
            this.biomes = nf.readVarIntArray(ChunkBiomeContainer.BIOMES_SIZE);
        }
        final int integer3 = nf.readVarInt();
        if (integer3 > 2097152) {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        }
        nf.readBytes(this.buffer = new byte[integer3]);
        final int integer4 = nf.readVarInt();
        this.blockEntitiesTags = (List<CompoundTag>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < integer4; ++integer5) {
            this.blockEntitiesTags.add(nf.readNbt());
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(this.x);
        nf.writeInt(this.z);
        nf.writeBoolean(this.fullChunk);
        nf.writeVarInt(this.availableSections);
        nf.writeNbt(this.heightmaps);
        if (this.biomes != null) {
            nf.writeVarIntArray(this.biomes);
        }
        nf.writeVarInt(this.buffer.length);
        nf.writeBytes(this.buffer);
        nf.writeVarInt(this.blockEntitiesTags.size());
        for (final CompoundTag md4 : this.blockEntitiesTags) {
            nf.writeNbt(md4);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleLevelChunk(this);
    }
    
    public FriendlyByteBuf getReadBuffer() {
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(this.buffer));
    }
    
    private ByteBuf getWriteBuffer() {
        final ByteBuf byteBuf2 = Unpooled.wrappedBuffer(this.buffer);
        byteBuf2.writerIndex(0);
        return byteBuf2;
    }
    
    public int extractChunkData(final FriendlyByteBuf nf, final LevelChunk cge, final int integer) {
        int integer2 = 0;
        final LevelChunkSection[] arr6 = cge.getSections();
        for (int integer3 = 0, integer4 = arr6.length; integer3 < integer4; ++integer3) {
            final LevelChunkSection cgf9 = arr6[integer3];
            if (cgf9 != LevelChunk.EMPTY_SECTION && (!this.isFullChunk() || !cgf9.isEmpty())) {
                if ((integer & 1 << integer3) != 0x0) {
                    integer2 |= 1 << integer3;
                    cgf9.write(nf);
                }
            }
        }
        return integer2;
    }
    
    protected int calculateChunkSize(final LevelChunk cge, final int integer) {
        int integer2 = 0;
        final LevelChunkSection[] arr5 = cge.getSections();
        for (int integer3 = 0, integer4 = arr5.length; integer3 < integer4; ++integer3) {
            final LevelChunkSection cgf8 = arr5[integer3];
            if (cgf8 != LevelChunk.EMPTY_SECTION && (!this.isFullChunk() || !cgf8.isEmpty())) {
                if ((integer & 1 << integer3) != 0x0) {
                    integer2 += cgf8.getSerializedSize();
                }
            }
        }
        return integer2;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public int getAvailableSections() {
        return this.availableSections;
    }
    
    public boolean isFullChunk() {
        return this.fullChunk;
    }
    
    public CompoundTag getHeightmaps() {
        return this.heightmaps;
    }
    
    public List<CompoundTag> getBlockEntitiesTags() {
        return this.blockEntitiesTags;
    }
    
    @Nullable
    public int[] getBiomes() {
        return this.biomes;
    }
}
