package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class StrongholdConfiguration {
    public static final Codec<StrongholdConfiguration> CODEC;
    private final int distance;
    private final int spread;
    private final int count;
    
    public StrongholdConfiguration(final int integer1, final int integer2, final int integer3) {
        this.distance = integer1;
        this.spread = integer2;
        this.count = integer3;
    }
    
    public int distance() {
        return this.distance;
    }
    
    public int spread() {
        return this.spread;
    }
    
    public int count() {
        return this.count;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.intRange(0, 1023).fieldOf("distance").forGetter(StrongholdConfiguration::distance), (App)Codec.intRange(0, 1023).fieldOf("spread").forGetter(StrongholdConfiguration::spread), (App)Codec.intRange(1, 4095).fieldOf("count").forGetter(StrongholdConfiguration::count)).apply((Applicative)instance, StrongholdConfiguration::new));
    }
}
