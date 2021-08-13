package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.network.protocol.Packet;

public class ClientboundCooldownPacket implements Packet<ClientGamePacketListener> {
    private Item item;
    private int duration;
    
    public ClientboundCooldownPacket() {
    }
    
    public ClientboundCooldownPacket(final Item blu, final int integer) {
        this.item = blu;
        this.duration = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.item = Item.byId(nf.readVarInt());
        this.duration = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(Item.getId(this.item));
        nf.writeVarInt(this.duration);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleItemCooldown(this);
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public int getDuration() {
        return this.duration;
    }
}
