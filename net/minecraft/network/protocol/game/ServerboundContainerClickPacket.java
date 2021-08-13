package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.Packet;

public class ServerboundContainerClickPacket implements Packet<ServerGamePacketListener> {
    private int containerId;
    private int slotNum;
    private int buttonNum;
    private short uid;
    private ItemStack itemStack;
    private ClickType clickType;
    
    public ServerboundContainerClickPacket() {
        this.itemStack = ItemStack.EMPTY;
    }
    
    public ServerboundContainerClickPacket(final int integer1, final int integer2, final int integer3, final ClickType bih, final ItemStack bly, final short short6) {
        this.itemStack = ItemStack.EMPTY;
        this.containerId = integer1;
        this.slotNum = integer2;
        this.buttonNum = integer3;
        this.itemStack = bly.copy();
        this.uid = short6;
        this.clickType = bih;
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleContainerClick(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readByte();
        this.slotNum = nf.readShort();
        this.buttonNum = nf.readByte();
        this.uid = nf.readShort();
        this.clickType = nf.<ClickType>readEnum(ClickType.class);
        this.itemStack = nf.readItem();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeShort(this.slotNum);
        nf.writeByte(this.buttonNum);
        nf.writeShort(this.uid);
        nf.writeEnum(this.clickType);
        nf.writeItem(this.itemStack);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public int getSlotNum() {
        return this.slotNum;
    }
    
    public int getButtonNum() {
        return this.buttonNum;
    }
    
    public short getUid() {
        return this.uid;
    }
    
    public ItemStack getItem() {
        return this.itemStack;
    }
    
    public ClickType getClickType() {
        return this.clickType;
    }
}
