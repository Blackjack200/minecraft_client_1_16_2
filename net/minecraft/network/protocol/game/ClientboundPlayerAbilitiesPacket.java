package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerAbilitiesPacket implements Packet<ClientGamePacketListener> {
    private boolean invulnerable;
    private boolean isFlying;
    private boolean canFly;
    private boolean instabuild;
    private float flyingSpeed;
    private float walkingSpeed;
    
    public ClientboundPlayerAbilitiesPacket() {
    }
    
    public ClientboundPlayerAbilitiesPacket(final Abilities bfq) {
        this.invulnerable = bfq.invulnerable;
        this.isFlying = bfq.flying;
        this.canFly = bfq.mayfly;
        this.instabuild = bfq.instabuild;
        this.flyingSpeed = bfq.getFlyingSpeed();
        this.walkingSpeed = bfq.getWalkingSpeed();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final byte byte3 = nf.readByte();
        this.invulnerable = ((byte3 & 0x1) != 0x0);
        this.isFlying = ((byte3 & 0x2) != 0x0);
        this.canFly = ((byte3 & 0x4) != 0x0);
        this.instabuild = ((byte3 & 0x8) != 0x0);
        this.flyingSpeed = nf.readFloat();
        this.walkingSpeed = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        byte byte3 = 0;
        if (this.invulnerable) {
            byte3 |= 0x1;
        }
        if (this.isFlying) {
            byte3 |= 0x2;
        }
        if (this.canFly) {
            byte3 |= 0x4;
        }
        if (this.instabuild) {
            byte3 |= 0x8;
        }
        nf.writeByte(byte3);
        nf.writeFloat(this.flyingSpeed);
        nf.writeFloat(this.walkingSpeed);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handlePlayerAbilities(this);
    }
    
    public boolean isInvulnerable() {
        return this.invulnerable;
    }
    
    public boolean isFlying() {
        return this.isFlying;
    }
    
    public boolean canFly() {
        return this.canFly;
    }
    
    public boolean canInstabuild() {
        return this.instabuild;
    }
    
    public float getFlyingSpeed() {
        return this.flyingSpeed;
    }
    
    public float getWalkingSpeed() {
        return this.walkingSpeed;
    }
}
