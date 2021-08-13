package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.BossEvent;
import net.minecraft.network.chat.Component;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;

public class ClientboundBossEventPacket implements Packet<ClientGamePacketListener> {
    private UUID id;
    private Operation operation;
    private Component name;
    private float pct;
    private BossEvent.BossBarColor color;
    private BossEvent.BossBarOverlay overlay;
    private boolean darkenScreen;
    private boolean playMusic;
    private boolean createWorldFog;
    
    public ClientboundBossEventPacket() {
    }
    
    public ClientboundBossEventPacket(final Operation a, final BossEvent aoh) {
        this.operation = a;
        this.id = aoh.getId();
        this.name = aoh.getName();
        this.pct = aoh.getPercent();
        this.color = aoh.getColor();
        this.overlay = aoh.getOverlay();
        this.darkenScreen = aoh.shouldDarkenScreen();
        this.playMusic = aoh.shouldPlayBossMusic();
        this.createWorldFog = aoh.shouldCreateWorldFog();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readUUID();
        this.operation = nf.<Operation>readEnum(Operation.class);
        switch (this.operation) {
            case ADD: {
                this.name = nf.readComponent();
                this.pct = nf.readFloat();
                this.color = nf.<BossEvent.BossBarColor>readEnum(BossEvent.BossBarColor.class);
                this.overlay = nf.<BossEvent.BossBarOverlay>readEnum(BossEvent.BossBarOverlay.class);
                this.decodeProperties(nf.readUnsignedByte());
            }
            case UPDATE_PCT: {
                this.pct = nf.readFloat();
                break;
            }
            case UPDATE_NAME: {
                this.name = nf.readComponent();
                break;
            }
            case UPDATE_STYLE: {
                this.color = nf.<BossEvent.BossBarColor>readEnum(BossEvent.BossBarColor.class);
                this.overlay = nf.<BossEvent.BossBarOverlay>readEnum(BossEvent.BossBarOverlay.class);
                break;
            }
            case UPDATE_PROPERTIES: {
                this.decodeProperties(nf.readUnsignedByte());
                break;
            }
        }
    }
    
    private void decodeProperties(final int integer) {
        this.darkenScreen = ((integer & 0x1) > 0);
        this.playMusic = ((integer & 0x2) > 0);
        this.createWorldFog = ((integer & 0x4) > 0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUUID(this.id);
        nf.writeEnum(this.operation);
        switch (this.operation) {
            case ADD: {
                nf.writeComponent(this.name);
                nf.writeFloat(this.pct);
                nf.writeEnum(this.color);
                nf.writeEnum(this.overlay);
                nf.writeByte(this.encodeProperties());
            }
            case UPDATE_PCT: {
                nf.writeFloat(this.pct);
                break;
            }
            case UPDATE_NAME: {
                nf.writeComponent(this.name);
                break;
            }
            case UPDATE_STYLE: {
                nf.writeEnum(this.color);
                nf.writeEnum(this.overlay);
                break;
            }
            case UPDATE_PROPERTIES: {
                nf.writeByte(this.encodeProperties());
                break;
            }
        }
    }
    
    private int encodeProperties() {
        int integer2 = 0;
        if (this.darkenScreen) {
            integer2 |= 0x1;
        }
        if (this.playMusic) {
            integer2 |= 0x2;
        }
        if (this.createWorldFog) {
            integer2 |= 0x4;
        }
        return integer2;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleBossUpdate(this);
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public Operation getOperation() {
        return this.operation;
    }
    
    public Component getName() {
        return this.name;
    }
    
    public float getPercent() {
        return this.pct;
    }
    
    public BossEvent.BossBarColor getColor() {
        return this.color;
    }
    
    public BossEvent.BossBarOverlay getOverlay() {
        return this.overlay;
    }
    
    public boolean shouldDarkenScreen() {
        return this.darkenScreen;
    }
    
    public boolean shouldPlayMusic() {
        return this.playMusic;
    }
    
    public boolean shouldCreateWorldFog() {
        return this.createWorldFog;
    }
    
    public enum Operation {
        ADD, 
        REMOVE, 
        UPDATE_PCT, 
        UPDATE_NAME, 
        UPDATE_STYLE, 
        UPDATE_PROPERTIES;
    }
}
