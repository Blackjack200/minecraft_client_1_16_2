package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;

public class ClientboundTagQueryPacket implements Packet<ClientGamePacketListener> {
    private int transactionId;
    @Nullable
    private CompoundTag tag;
    
    public ClientboundTagQueryPacket() {
    }
    
    public ClientboundTagQueryPacket(final int integer, @Nullable final CompoundTag md) {
        this.transactionId = integer;
        this.tag = md;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.transactionId = nf.readVarInt();
        this.tag = nf.readNbt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.transactionId);
        nf.writeNbt(this.tag);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleTagQueryPacket(this);
    }
    
    public int getTransactionId() {
        return this.transactionId;
    }
    
    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }
    
    public boolean isSkippable() {
        return true;
    }
}
