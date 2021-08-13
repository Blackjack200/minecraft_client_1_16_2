package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Objects;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.world.scores.Objective;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetDisplayObjectivePacket implements Packet<ClientGamePacketListener> {
    private int slot;
    private String objectiveName;
    
    public ClientboundSetDisplayObjectivePacket() {
    }
    
    public ClientboundSetDisplayObjectivePacket(final int integer, @Nullable final Objective ddh) {
        this.slot = integer;
        if (ddh == null) {
            this.objectiveName = "";
        }
        else {
            this.objectiveName = ddh.getName();
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.slot = nf.readByte();
        this.objectiveName = nf.readUtf(16);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.slot);
        nf.writeUtf(this.objectiveName);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetDisplayObjective(this);
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    @Nullable
    public String getObjectiveName() {
        return Objects.equals(this.objectiveName, "") ? null : this.objectiveName;
    }
}
