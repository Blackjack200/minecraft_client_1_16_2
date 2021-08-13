package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;

public class ClientboundLevelParticlesPacket implements Packet<ClientGamePacketListener> {
    private double x;
    private double y;
    private double z;
    private float xDist;
    private float yDist;
    private float zDist;
    private float maxSpeed;
    private int count;
    private boolean overrideLimiter;
    private ParticleOptions particle;
    
    public ClientboundLevelParticlesPacket() {
    }
    
    public <T extends ParticleOptions> ClientboundLevelParticlesPacket(final T hf, final boolean boolean2, final double double3, final double double4, final double double5, final float float6, final float float7, final float float8, final float float9, final int integer) {
        this.particle = hf;
        this.overrideLimiter = boolean2;
        this.x = double3;
        this.y = double4;
        this.z = double5;
        this.xDist = float6;
        this.yDist = float7;
        this.zDist = float8;
        this.maxSpeed = float9;
        this.count = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        ParticleType<?> hg3 = Registry.PARTICLE_TYPE.byId(nf.readInt());
        if (hg3 == null) {
            hg3 = ParticleTypes.BARRIER;
        }
        this.overrideLimiter = nf.readBoolean();
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.xDist = nf.readFloat();
        this.yDist = nf.readFloat();
        this.zDist = nf.readFloat();
        this.maxSpeed = nf.readFloat();
        this.count = nf.readInt();
        this.particle = this.<ParticleOptions>readParticle(nf, hg3);
    }
    
    private <T extends ParticleOptions> T readParticle(final FriendlyByteBuf nf, final ParticleType<T> hg) {
        return hg.getDeserializer().fromNetwork(hg, nf);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(Registry.PARTICLE_TYPE.getId(this.particle.getType()));
        nf.writeBoolean(this.overrideLimiter);
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeFloat(this.xDist);
        nf.writeFloat(this.yDist);
        nf.writeFloat(this.zDist);
        nf.writeFloat(this.maxSpeed);
        nf.writeInt(this.count);
        this.particle.writeToNetwork(nf);
    }
    
    public boolean isOverrideLimiter() {
        return this.overrideLimiter;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getXDist() {
        return this.xDist;
    }
    
    public float getYDist() {
        return this.yDist;
    }
    
    public float getZDist() {
        return this.zDist;
    }
    
    public float getMaxSpeed() {
        return this.maxSpeed;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public ParticleOptions getParticle() {
        return this.particle;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleParticleEvent(this);
    }
}
