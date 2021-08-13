package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;

public class ClientboundContainerSetSlotPacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private int slot;
    private ItemStack itemStack;
    
    public ClientboundContainerSetSlotPacket() {
        this.itemStack = ItemStack.EMPTY;
    }
    
    public ClientboundContainerSetSlotPacket(final int integer1, final int integer2, final ItemStack bly) {
        this.itemStack = ItemStack.EMPTY;
        this.containerId = integer1;
        this.slot = integer2;
        this.itemStack = bly.copy();
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleContainerSetSlot(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readByte();
        this.slot = nf.readShort();
        this.itemStack = nf.readItem();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeShort(this.slot);
        nf.writeItem(this.itemStack);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public ItemStack getItem() {
        return this.itemStack;
    }
}
