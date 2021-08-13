package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetPassengersPacket implements Packet<ClientGamePacketListener> {
    private int vehicle;
    private int[] passengers;
    
    public ClientboundSetPassengersPacket() {
    }
    
    public ClientboundSetPassengersPacket(final Entity apx) {
        this.vehicle = apx.getId();
        final List<Entity> list3 = apx.getPassengers();
        this.passengers = new int[list3.size()];
        for (int integer4 = 0; integer4 < list3.size(); ++integer4) {
            this.passengers[integer4] = ((Entity)list3.get(integer4)).getId();
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.vehicle = nf.readVarInt();
        this.passengers = nf.readVarIntArray();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.vehicle);
        nf.writeVarIntArray(this.passengers);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetEntityPassengersPacket(this);
    }
    
    public int[] getPassengers() {
        return this.passengers;
    }
    
    public int getVehicle() {
        return this.vehicle;
    }
}
