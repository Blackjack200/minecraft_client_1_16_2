package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlayerCommandPacket implements Packet<ServerGamePacketListener> {
    private int id;
    private Action action;
    private int data;
    
    public ServerboundPlayerCommandPacket() {
    }
    
    public ServerboundPlayerCommandPacket(final Entity apx, final Action a) {
        this(apx, a, 0);
    }
    
    public ServerboundPlayerCommandPacket(final Entity apx, final Action a, final int integer) {
        this.id = apx.getId();
        this.action = a;
        this.data = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.action = nf.<Action>readEnum(Action.class);
        this.data = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeEnum(this.action);
        nf.writeVarInt(this.data);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePlayerCommand(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public int getData() {
        return this.data;
    }
    
    public enum Action {
        PRESS_SHIFT_KEY, 
        RELEASE_SHIFT_KEY, 
        STOP_SLEEPING, 
        START_SPRINTING, 
        STOP_SPRINTING, 
        START_RIDING_JUMP, 
        STOP_RIDING_JUMP, 
        OPEN_INVENTORY, 
        START_FALL_FLYING;
    }
}
