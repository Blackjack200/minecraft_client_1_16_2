package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ClientboundCustomSoundPacket implements Packet<ClientGamePacketListener> {
    private ResourceLocation name;
    private SoundSource source;
    private int x;
    private int y;
    private int z;
    private float volume;
    private float pitch;
    
    public ClientboundCustomSoundPacket() {
        this.y = Integer.MAX_VALUE;
    }
    
    public ClientboundCustomSoundPacket(final ResourceLocation vk, final SoundSource adp, final Vec3 dck, final float float4, final float float5) {
        this.y = Integer.MAX_VALUE;
        this.name = vk;
        this.source = adp;
        this.x = (int)(dck.x * 8.0);
        this.y = (int)(dck.y * 8.0);
        this.z = (int)(dck.z * 8.0);
        this.volume = float4;
        this.pitch = float5;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.name = nf.readResourceLocation();
        this.source = nf.<SoundSource>readEnum(SoundSource.class);
        this.x = nf.readInt();
        this.y = nf.readInt();
        this.z = nf.readInt();
        this.volume = nf.readFloat();
        this.pitch = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeResourceLocation(this.name);
        nf.writeEnum(this.source);
        nf.writeInt(this.x);
        nf.writeInt(this.y);
        nf.writeInt(this.z);
        nf.writeFloat(this.volume);
        nf.writeFloat(this.pitch);
    }
    
    public ResourceLocation getName() {
        return this.name;
    }
    
    public SoundSource getSource() {
        return this.source;
    }
    
    public double getX() {
        return this.x / 8.0f;
    }
    
    public double getY() {
        return this.y / 8.0f;
    }
    
    public double getZ() {
        return this.z / 8.0f;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleCustomSoundEvent(this);
    }
}
