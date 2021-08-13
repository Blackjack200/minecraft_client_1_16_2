package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundBlockEventPacket implements Packet<ClientGamePacketListener> {
    private BlockPos pos;
    private int b0;
    private int b1;
    private Block block;
    
    public ClientboundBlockEventPacket() {
    }
    
    public ClientboundBlockEventPacket(final BlockPos fx, final Block bul, final int integer3, final int integer4) {
        this.pos = fx;
        this.block = bul;
        this.b0 = integer3;
        this.b1 = integer4;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.b0 = nf.readUnsignedByte();
        this.b1 = nf.readUnsignedByte();
        this.block = Registry.BLOCK.byId(nf.readVarInt());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeByte(this.b0);
        nf.writeByte(this.b1);
        nf.writeVarInt(Registry.BLOCK.getId(this.block));
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleBlockEvent(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public int getB0() {
        return this.b0;
    }
    
    public int getB1() {
        return this.b1;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
