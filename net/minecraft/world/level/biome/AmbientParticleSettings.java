package net.minecraft.world.level.biome;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.core.particles.ParticleTypes;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.core.particles.ParticleOptions;
import com.mojang.serialization.Codec;

public class AmbientParticleSettings {
    public static final Codec<AmbientParticleSettings> CODEC;
    private final ParticleOptions options;
    private final float probability;
    
    public AmbientParticleSettings(final ParticleOptions hf, final float float2) {
        this.options = hf;
        this.probability = float2;
    }
    
    public ParticleOptions getOptions() {
        return this.options;
    }
    
    public boolean canSpawn(final Random random) {
        return random.nextFloat() <= this.probability;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ParticleTypes.CODEC.fieldOf("options").forGetter(bsr -> bsr.options), (App)Codec.FLOAT.fieldOf("probability").forGetter(bsr -> bsr.probability)).apply((Applicative)instance, AmbientParticleSettings::new));
    }
}
