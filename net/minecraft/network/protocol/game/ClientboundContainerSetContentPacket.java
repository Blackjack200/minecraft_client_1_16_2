package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundContainerSetContentPacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private List<ItemStack> items;
    
    public ClientboundContainerSetContentPacket() {
    }
    
    public ClientboundContainerSetContentPacket(final int integer, final NonNullList<ItemStack> gj) {
        this.containerId = integer;
        this.items = NonNullList.withSize(gj.size(), ItemStack.EMPTY);
        for (int integer2 = 0; integer2 < this.items.size(); ++integer2) {
            this.items.set(integer2, gj.get(integer2).copy());
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readUnsignedByte();
        final int integer3 = nf.readShort();
        this.items = NonNullList.withSize(integer3, ItemStack.EMPTY);
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            this.items.set(integer4, nf.readItem());
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeShort(this.items.size());
        for (final ItemStack bly4 : this.items) {
            nf.writeItem(bly4);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleContainerContent(this);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public List<ItemStack> getItems() {
        return this.items;
    }
}
