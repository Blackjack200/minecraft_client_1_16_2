package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundBlockUpdatePacket implements Packet<ClientGamePacketListener> {
    private BlockPos pos;
    private BlockState blockState;
    
    public ClientboundBlockUpdatePacket() {
    }
    
    public ClientboundBlockUpdatePacket(final BlockPos fx, final BlockState cee) {
        this.pos = fx;
        this.blockState = cee;
    }
    
    public ClientboundBlockUpdatePacket(final BlockGetter bqz, final BlockPos fx) {
        this(fx, bqz.getBlockState(fx));
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.blockState = Block.BLOCK_STATE_REGISTRY.byId(nf.readVarInt());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeVarInt(Block.getId(this.blockState));
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleBlockUpdate(this);
    }
    
    public BlockState getBlockState() {
        return this.blockState;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
