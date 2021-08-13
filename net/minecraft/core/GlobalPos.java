package net.minecraft.core;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import com.mojang.serialization.Codec;

public final class GlobalPos {
    public static final Codec<GlobalPos> CODEC;
    private final ResourceKey<Level> dimension;
    private final BlockPos pos;
    
    private GlobalPos(final ResourceKey<Level> vj, final BlockPos fx) {
        this.dimension = vj;
        this.pos = fx;
    }
    
    public static GlobalPos of(final ResourceKey<Level> vj, final BlockPos fx) {
        return new GlobalPos(vj, fx);
    }
    
    public ResourceKey<Level> dimension() {
        return this.dimension;
    }
    
    public BlockPos pos() {
        return this.pos;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final GlobalPos gf3 = (GlobalPos)object;
        return Objects.equals(this.dimension, gf3.dimension) && Objects.equals(this.pos, gf3.pos);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.dimension, this.pos });
    }
    
    public String toString() {
        return this.dimension.toString() + " " + this.pos;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalPos::dimension), (App)BlockPos.CODEC.fieldOf("pos").forGetter(GlobalPos::pos)).apply((Applicative)instance, GlobalPos::of));
    }
}
