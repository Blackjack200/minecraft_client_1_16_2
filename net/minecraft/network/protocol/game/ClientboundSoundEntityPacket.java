package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.Packet;

public class ClientboundSoundEntityPacket implements Packet<ClientGamePacketListener> {
    private SoundEvent sound;
    private SoundSource source;
    private int id;
    private float volume;
    private float pitch;
    
    public ClientboundSoundEntityPacket() {
    }
    
    public ClientboundSoundEntityPacket(final SoundEvent adn, final SoundSource adp, final Entity apx, final float float4, final float float5) {
        Validate.notNull(adn, "sound", new Object[0]);
        this.sound = adn;
        this.source = adp;
        this.id = apx.getId();
        this.volume = float4;
        this.pitch = float5;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.sound = Registry.SOUND_EVENT.byId(nf.readVarInt());
        this.source = nf.<SoundSource>readEnum(SoundSource.class);
        this.id = nf.readVarInt();
        this.volume = nf.readFloat();
        this.pitch = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(Registry.SOUND_EVENT.getId(this.sound));
        nf.writeEnum(this.source);
        nf.writeVarInt(this.id);
        nf.writeFloat(this.volume);
        nf.writeFloat(this.pitch);
    }
    
    public SoundEvent getSound() {
        return this.sound;
    }
    
    public SoundSource getSource() {
        return this.source;
    }
    
    public int getId() {
        return this.id;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSoundEntityEvent(this);
    }
}
