package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetCommandMinecartPacket implements Packet<ServerGamePacketListener> {
    private int entity;
    private String command;
    private boolean trackOutput;
    
    public ServerboundSetCommandMinecartPacket() {
    }
    
    public ServerboundSetCommandMinecartPacket(final int integer, final String string, final boolean boolean3) {
        this.entity = integer;
        this.command = string;
        this.trackOutput = boolean3;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entity = nf.readVarInt();
        this.command = nf.readUtf(32767);
        this.trackOutput = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entity);
        nf.writeUtf(this.command);
        nf.writeBoolean(this.trackOutput);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetCommandMinecart(this);
    }
    
    @Nullable
    public BaseCommandBlock getCommandBlock(final Level bru) {
        final Entity apx3 = bru.getEntity(this.entity);
        if (apx3 instanceof MinecartCommandBlock) {
            return ((MinecartCommandBlock)apx3).getCommandBlock();
        }
        return null;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public boolean isTrackOutput() {
        return this.trackOutput;
    }
}
