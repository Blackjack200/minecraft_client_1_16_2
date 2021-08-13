package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.protocol.Packet;

public class ClientboundRemoveMobEffectPacket implements Packet<ClientGamePacketListener> {
    private int entityId;
    private MobEffect effect;
    
    public ClientboundRemoveMobEffectPacket() {
    }
    
    public ClientboundRemoveMobEffectPacket(final int integer, final MobEffect app) {
        this.entityId = integer;
        this.effect = app;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
        this.effect = MobEffect.byId(nf.readUnsignedByte());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
        nf.writeByte(MobEffect.getId(this.effect));
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleRemoveMobEffect(this);
    }
    
    @Nullable
    public Entity getEntity(final Level bru) {
        return bru.getEntity(this.entityId);
    }
    
    @Nullable
    public MobEffect getEffect() {
        return this.effect;
    }
}
