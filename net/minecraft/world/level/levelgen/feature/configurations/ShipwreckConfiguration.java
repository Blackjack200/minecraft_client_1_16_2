package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;

public class ShipwreckConfiguration implements FeatureConfiguration {
    public static final Codec<ShipwreckConfiguration> CODEC;
    public final boolean isBeached;
    
    public ShipwreckConfiguration(final boolean boolean1) {
        this.isBeached = boolean1;
    }
    
    static {
        CODEC = Codec.BOOL.fieldOf("is_beached").orElse(false).xmap(ShipwreckConfiguration::new, cmp -> cmp.isBeached).codec();
    }
}
