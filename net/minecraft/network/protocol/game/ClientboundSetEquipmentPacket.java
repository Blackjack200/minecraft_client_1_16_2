package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetEquipmentPacket implements Packet<ClientGamePacketListener> {
    private int entity;
    private final List<Pair<EquipmentSlot, ItemStack>> slots;
    
    public ClientboundSetEquipmentPacket() {
        this.slots = (List<Pair<EquipmentSlot, ItemStack>>)Lists.newArrayList();
    }
    
    public ClientboundSetEquipmentPacket(final int integer, final List<Pair<EquipmentSlot, ItemStack>> list) {
        this.entity = integer;
        this.slots = list;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entity = nf.readVarInt();
        final EquipmentSlot[] arr3 = EquipmentSlot.values();
        int integer4;
        do {
            integer4 = nf.readByte();
            final EquipmentSlot aqc5 = arr3[integer4 & 0x7F];
            final ItemStack bly6 = nf.readItem();
            this.slots.add(Pair.of((Object)aqc5, (Object)bly6));
        } while ((integer4 & 0xFFFFFF80) != 0x0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entity);
        for (int integer3 = this.slots.size(), integer4 = 0; integer4 < integer3; ++integer4) {
            final Pair<EquipmentSlot, ItemStack> pair5 = (Pair<EquipmentSlot, ItemStack>)this.slots.get(integer4);
            final EquipmentSlot aqc6 = (EquipmentSlot)pair5.getFirst();
            final boolean boolean7 = integer4 != integer3 - 1;
            final int integer5 = aqc6.ordinal();
            nf.writeByte(boolean7 ? (integer5 | 0xFFFFFF80) : integer5);
            nf.writeItem((ItemStack)pair5.getSecond());
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetEquipment(this);
    }
    
    public int getEntity() {
        return this.entity;
    }
    
    public List<Pair<EquipmentSlot, ItemStack>> getSlots() {
        return this.slots;
    }
}
