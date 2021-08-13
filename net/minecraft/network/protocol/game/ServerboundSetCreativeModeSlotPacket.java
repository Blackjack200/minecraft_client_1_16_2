package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetCreativeModeSlotPacket implements Packet<ServerGamePacketListener> {
    private int slotNum;
    private ItemStack itemStack;
    
    public ServerboundSetCreativeModeSlotPacket() {
        this.itemStack = ItemStack.EMPTY;
    }
    
    public ServerboundSetCreativeModeSlotPacket(final int integer, final ItemStack bly) {
        this.itemStack = ItemStack.EMPTY;
        this.slotNum = integer;
        this.itemStack = bly.copy();
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetCreativeModeSlot(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.slotNum = nf.readShort();
        this.itemStack = nf.readItem();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeShort(this.slotNum);
        nf.writeItem(this.itemStack);
    }
    
    public int getSlotNum() {
        return this.slotNum;
    }
    
    public ItemStack getItem() {
        return this.itemStack;
    }
}
