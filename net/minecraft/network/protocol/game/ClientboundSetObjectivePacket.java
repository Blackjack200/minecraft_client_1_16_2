package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetObjectivePacket implements Packet<ClientGamePacketListener> {
    private String objectiveName;
    private Component displayName;
    private ObjectiveCriteria.RenderType renderType;
    private int method;
    
    public ClientboundSetObjectivePacket() {
    }
    
    public ClientboundSetObjectivePacket(final Objective ddh, final int integer) {
        this.objectiveName = ddh.getName();
        this.displayName = ddh.getDisplayName();
        this.renderType = ddh.getRenderType();
        this.method = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.objectiveName = nf.readUtf(16);
        this.method = nf.readByte();
        if (this.method == 0 || this.method == 2) {
            this.displayName = nf.readComponent();
            this.renderType = nf.<ObjectiveCriteria.RenderType>readEnum(ObjectiveCriteria.RenderType.class);
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.objectiveName);
        nf.writeByte(this.method);
        if (this.method == 0 || this.method == 2) {
            nf.writeComponent(this.displayName);
            nf.writeEnum(this.renderType);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddObjective(this);
    }
    
    public String getObjectiveName() {
        return this.objectiveName;
    }
    
    public Component getDisplayName() {
        return this.displayName;
    }
    
    public int getMethod() {
        return this.method;
    }
    
    public ObjectiveCriteria.RenderType getRenderType() {
        return this.renderType;
    }
}
