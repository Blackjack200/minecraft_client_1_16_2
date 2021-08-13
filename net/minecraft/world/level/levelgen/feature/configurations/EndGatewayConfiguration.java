package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import com.mojang.serialization.Codec;

public class EndGatewayConfiguration implements FeatureConfiguration {
    public static final Codec<EndGatewayConfiguration> CODEC;
    private final Optional<BlockPos> exit;
    private final boolean exact;
    
    private EndGatewayConfiguration(final Optional<BlockPos> optional, final boolean boolean2) {
        this.exit = optional;
        this.exact = boolean2;
    }
    
    public static EndGatewayConfiguration knownExit(final BlockPos fx, final boolean boolean2) {
        return new EndGatewayConfiguration((Optional<BlockPos>)Optional.of(fx), boolean2);
    }
    
    public static EndGatewayConfiguration delayedExitSearch() {
        return new EndGatewayConfiguration((Optional<BlockPos>)Optional.empty(), false);
    }
    
    public Optional<BlockPos> getExit() {
        return this.exit;
    }
    
    public boolean isExitExact() {
        return this.exact;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockPos.CODEC.optionalFieldOf("exit").forGetter(clw -> clw.exit), (App)Codec.BOOL.fieldOf("exact").forGetter(clw -> clw.exact)).apply((Applicative)instance, EndGatewayConfiguration::new));
    }
}
