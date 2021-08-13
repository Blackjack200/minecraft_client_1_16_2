package net.minecraft.core.particles;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import java.util.Locale;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import com.mojang.serialization.Codec;

public class DustParticleOptions implements ParticleOptions {
    public static final DustParticleOptions REDSTONE;
    public static final Codec<DustParticleOptions> CODEC;
    public static final Deserializer<DustParticleOptions> DESERIALIZER;
    private final float r;
    private final float g;
    private final float b;
    private final float scale;
    
    public DustParticleOptions(final float float1, final float float2, final float float3, final float float4) {
        this.r = float1;
        this.g = float2;
        this.b = float3;
        this.scale = Mth.clamp(float4, 0.01f, 4.0f);
    }
    
    public void writeToNetwork(final FriendlyByteBuf nf) {
        nf.writeFloat(this.r);
        nf.writeFloat(this.g);
        nf.writeFloat(this.b);
        nf.writeFloat(this.scale);
    }
    
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", new Object[] { Registry.PARTICLE_TYPE.getKey(this.getType()), this.r, this.g, this.b, this.scale });
    }
    
    public ParticleType<DustParticleOptions> getType() {
        return ParticleTypes.DUST;
    }
    
    public float getR() {
        return this.r;
    }
    
    public float getG() {
        return this.g;
    }
    
    public float getB() {
        return this.b;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    static {
        REDSTONE = new DustParticleOptions(1.0f, 0.0f, 0.0f, 1.0f);
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.FLOAT.fieldOf("r").forGetter(hd -> hd.r), (App)Codec.FLOAT.fieldOf("g").forGetter(hd -> hd.g), (App)Codec.FLOAT.fieldOf("b").forGetter(hd -> hd.b), (App)Codec.FLOAT.fieldOf("scale").forGetter(hd -> hd.scale)).apply((Applicative)instance, DustParticleOptions::new));
        DESERIALIZER = new Deserializer<DustParticleOptions>() {
            public DustParticleOptions fromCommand(final ParticleType<DustParticleOptions> hg, final StringReader stringReader) throws CommandSyntaxException {
                stringReader.expect(' ');
                final float float4 = (float)stringReader.readDouble();
                stringReader.expect(' ');
                final float float5 = (float)stringReader.readDouble();
                stringReader.expect(' ');
                final float float6 = (float)stringReader.readDouble();
                stringReader.expect(' ');
                final float float7 = (float)stringReader.readDouble();
                return new DustParticleOptions(float4, float5, float6, float7);
            }
            
            public DustParticleOptions fromNetwork(final ParticleType<DustParticleOptions> hg, final FriendlyByteBuf nf) {
                return new DustParticleOptions(nf.readFloat(), nf.readFloat(), nf.readFloat(), nf.readFloat());
            }
        };
    }
}
