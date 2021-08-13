package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.protocol.Packet;

public class ClientboundOpenBookPacket implements Packet<ClientGamePacketListener> {
    private InteractionHand hand;
    
    public ClientboundOpenBookPacket() {
    }
    
    public ClientboundOpenBookPacket(final InteractionHand aoq) {
        this.hand = aoq;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.hand = nf.<InteractionHand>readEnum(InteractionHand.class);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.hand);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleOpenBook(this);
    }
    
    public InteractionHand getHand() {
        return this.hand;
    }
}
