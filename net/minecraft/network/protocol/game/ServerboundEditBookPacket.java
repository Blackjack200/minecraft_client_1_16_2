package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;

public class ServerboundEditBookPacket implements Packet<ServerGamePacketListener> {
    private ItemStack book;
    private boolean signing;
    private InteractionHand hand;
    
    public ServerboundEditBookPacket() {
    }
    
    public ServerboundEditBookPacket(final ItemStack bly, final boolean boolean2, final InteractionHand aoq) {
        this.book = bly.copy();
        this.signing = boolean2;
        this.hand = aoq;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.book = nf.readItem();
        this.signing = nf.readBoolean();
        this.hand = nf.<InteractionHand>readEnum(InteractionHand.class);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeItem(this.book);
        nf.writeBoolean(this.signing);
        nf.writeEnum(this.hand);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleEditBook(this);
    }
    
    public ItemStack getBook() {
        return this.book;
    }
    
    public boolean isSigning() {
        return this.signing;
    }
    
    public InteractionHand getHand() {
        return this.hand;
    }
}
