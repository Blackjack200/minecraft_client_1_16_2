package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.core.BlockPos;
import java.util.function.BiConsumer;
import java.io.IOException;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.FriendlyByteBuf;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundSectionBlocksUpdatePacket implements Packet<ClientGamePacketListener> {
    private SectionPos sectionPos;
    private short[] positions;
    private BlockState[] states;
    private boolean suppressLightUpdates;
    
    public ClientboundSectionBlocksUpdatePacket() {
    }
    
    public ClientboundSectionBlocksUpdatePacket(final SectionPos gp, final ShortSet shortSet, final LevelChunkSection cgf, final boolean boolean4) {
        this.sectionPos = gp;
        this.suppressLightUpdates = boolean4;
        this.initFields(shortSet.size());
        int integer6 = 0;
        for (final short short8 : shortSet) {
            this.positions[integer6] = short8;
            this.states[integer6] = cgf.getBlockState(SectionPos.sectionRelativeX(short8), SectionPos.sectionRelativeY(short8), SectionPos.sectionRelativeZ(short8));
            ++integer6;
        }
    }
    
    private void initFields(final int integer) {
        this.positions = new short[integer];
        this.states = new BlockState[integer];
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.sectionPos = SectionPos.of(nf.readLong());
        this.suppressLightUpdates = nf.readBoolean();
        final int integer3 = nf.readVarInt();
        this.initFields(integer3);
        for (int integer4 = 0; integer4 < this.positions.length; ++integer4) {
            final long long5 = nf.readVarLong();
            this.positions[integer4] = (short)(long5 & 0xFFFL);
            this.states[integer4] = Block.BLOCK_STATE_REGISTRY.byId((int)(long5 >>> 12));
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeLong(this.sectionPos.asLong());
        nf.writeBoolean(this.suppressLightUpdates);
        nf.writeVarInt(this.positions.length);
        for (int integer3 = 0; integer3 < this.positions.length; ++integer3) {
            nf.writeVarLong(Block.getId(this.states[integer3]) << 12 | this.positions[integer3]);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleChunkBlocksUpdate(this);
    }
    
    public void runUpdates(final BiConsumer<BlockPos, BlockState> biConsumer) {
        final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
        for (int integer4 = 0; integer4 < this.positions.length; ++integer4) {
            final short short5 = this.positions[integer4];
            a3.set(this.sectionPos.relativeToBlockX(short5), this.sectionPos.relativeToBlockY(short5), this.sectionPos.relativeToBlockZ(short5));
            biConsumer.accept(a3, this.states[integer4]);
        }
    }
    
    public boolean shouldSuppressLightUpdates() {
        return this.suppressLightUpdates;
    }
}
