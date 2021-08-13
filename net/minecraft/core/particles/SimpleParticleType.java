package net.minecraft.core.particles;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.serialization.Codec;

public class SimpleParticleType extends ParticleType<SimpleParticleType> implements ParticleOptions {
    private static final Deserializer<SimpleParticleType> DESERIALIZER;
    private final Codec<SimpleParticleType> codec;
    
    protected SimpleParticleType(final boolean boolean1) {
        super(boolean1, SimpleParticleType.DESERIALIZER);
        this.codec = (Codec<SimpleParticleType>)Codec.unit(this::getType);
    }
    
    @Override
    public SimpleParticleType getType() {
        return this;
    }
    
    @Override
    public Codec<SimpleParticleType> codec() {
        return this.codec;
    }
    
    @Override
    public void writeToNetwork(final FriendlyByteBuf nf) {
    }
    
    @Override
    public String writeToString() {
        return Registry.PARTICLE_TYPE.getKey(this).toString();
    }
    
    static {
        DESERIALIZER = new Deserializer<SimpleParticleType>() {
            public SimpleParticleType fromCommand(final ParticleType<SimpleParticleType> hg, final StringReader stringReader) throws CommandSyntaxException {
                return (SimpleParticleType)hg;
            }
            
            public SimpleParticleType fromNetwork(final ParticleType<SimpleParticleType> hg, final FriendlyByteBuf nf) {
                return (SimpleParticleType)hg;
            }
        };
    }
}
