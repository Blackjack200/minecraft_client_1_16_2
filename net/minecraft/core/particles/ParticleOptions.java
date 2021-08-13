package net.minecraft.core.particles;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.network.FriendlyByteBuf;

public interface ParticleOptions {
    ParticleType<?> getType();
    
    void writeToNetwork(final FriendlyByteBuf nf);
    
    String writeToString();
    
    @Deprecated
    public interface Deserializer<T extends ParticleOptions> {
        T fromCommand(final ParticleType<T> hg, final StringReader stringReader) throws CommandSyntaxException;
        
        T fromNetwork(final ParticleType<T> hg, final FriendlyByteBuf nf);
    }
}
