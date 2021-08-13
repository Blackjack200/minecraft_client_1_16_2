package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetBorderPacket implements Packet<ClientGamePacketListener> {
    private Type type;
    private int newAbsoluteMaxSize;
    private double newCenterX;
    private double newCenterZ;
    private double newSize;
    private double oldSize;
    private long lerpTime;
    private int warningTime;
    private int warningBlocks;
    
    public ClientboundSetBorderPacket() {
    }
    
    public ClientboundSetBorderPacket(final WorldBorder cfr, final Type a) {
        this.type = a;
        this.newCenterX = cfr.getCenterX();
        this.newCenterZ = cfr.getCenterZ();
        this.oldSize = cfr.getSize();
        this.newSize = cfr.getLerpTarget();
        this.lerpTime = cfr.getLerpRemainingTime();
        this.newAbsoluteMaxSize = cfr.getAbsoluteMaxSize();
        this.warningBlocks = cfr.getWarningBlocks();
        this.warningTime = cfr.getWarningTime();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.type = nf.<Type>readEnum(Type.class);
        switch (this.type) {
            case SET_SIZE: {
                this.newSize = nf.readDouble();
                break;
            }
            case LERP_SIZE: {
                this.oldSize = nf.readDouble();
                this.newSize = nf.readDouble();
                this.lerpTime = nf.readVarLong();
                break;
            }
            case SET_CENTER: {
                this.newCenterX = nf.readDouble();
                this.newCenterZ = nf.readDouble();
                break;
            }
            case SET_WARNING_BLOCKS: {
                this.warningBlocks = nf.readVarInt();
                break;
            }
            case SET_WARNING_TIME: {
                this.warningTime = nf.readVarInt();
                break;
            }
            case INITIALIZE: {
                this.newCenterX = nf.readDouble();
                this.newCenterZ = nf.readDouble();
                this.oldSize = nf.readDouble();
                this.newSize = nf.readDouble();
                this.lerpTime = nf.readVarLong();
                this.newAbsoluteMaxSize = nf.readVarInt();
                this.warningBlocks = nf.readVarInt();
                this.warningTime = nf.readVarInt();
                break;
            }
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.type);
        switch (this.type) {
            case SET_SIZE: {
                nf.writeDouble(this.newSize);
                break;
            }
            case LERP_SIZE: {
                nf.writeDouble(this.oldSize);
                nf.writeDouble(this.newSize);
                nf.writeVarLong(this.lerpTime);
                break;
            }
            case SET_CENTER: {
                nf.writeDouble(this.newCenterX);
                nf.writeDouble(this.newCenterZ);
                break;
            }
            case SET_WARNING_TIME: {
                nf.writeVarInt(this.warningTime);
                break;
            }
            case SET_WARNING_BLOCKS: {
                nf.writeVarInt(this.warningBlocks);
                break;
            }
            case INITIALIZE: {
                nf.writeDouble(this.newCenterX);
                nf.writeDouble(this.newCenterZ);
                nf.writeDouble(this.oldSize);
                nf.writeDouble(this.newSize);
                nf.writeVarLong(this.lerpTime);
                nf.writeVarInt(this.newAbsoluteMaxSize);
                nf.writeVarInt(this.warningBlocks);
                nf.writeVarInt(this.warningTime);
                break;
            }
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetBorder(this);
    }
    
    public void applyChanges(final WorldBorder cfr) {
        switch (this.type) {
            case SET_SIZE: {
                cfr.setSize(this.newSize);
                break;
            }
            case LERP_SIZE: {
                cfr.lerpSizeBetween(this.oldSize, this.newSize, this.lerpTime);
                break;
            }
            case SET_CENTER: {
                cfr.setCenter(this.newCenterX, this.newCenterZ);
                break;
            }
            case INITIALIZE: {
                cfr.setCenter(this.newCenterX, this.newCenterZ);
                if (this.lerpTime > 0L) {
                    cfr.lerpSizeBetween(this.oldSize, this.newSize, this.lerpTime);
                }
                else {
                    cfr.setSize(this.newSize);
                }
                cfr.setAbsoluteMaxSize(this.newAbsoluteMaxSize);
                cfr.setWarningBlocks(this.warningBlocks);
                cfr.setWarningTime(this.warningTime);
                break;
            }
            case SET_WARNING_TIME: {
                cfr.setWarningTime(this.warningTime);
                break;
            }
            case SET_WARNING_BLOCKS: {
                cfr.setWarningBlocks(this.warningBlocks);
                break;
            }
        }
    }
    
    public enum Type {
        SET_SIZE, 
        LERP_SIZE, 
        SET_CENTER, 
        INITIALIZE, 
        SET_WARNING_TIME, 
        SET_WARNING_BLOCKS;
    }
}
