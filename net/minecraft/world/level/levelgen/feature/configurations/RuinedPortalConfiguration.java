package net.minecraft.world.level.levelgen.feature.configurations;

import net.minecraft.world.level.levelgen.feature.RuinedPortalFeature;
import com.mojang.serialization.Codec;

public class RuinedPortalConfiguration implements FeatureConfiguration {
    public static final Codec<RuinedPortalConfiguration> CODEC;
    public final RuinedPortalFeature.Type portalType;
    
    public RuinedPortalConfiguration(final RuinedPortalFeature.Type b) {
        this.portalType = b;
    }
    
    static {
        CODEC = RuinedPortalFeature.Type.CODEC.fieldOf("portal_type").xmap(RuinedPortalConfiguration::new, cmo -> cmo.portalType).codec();
    }
}
