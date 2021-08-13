package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import java.util.List;
import com.mojang.serialization.Codec;

public class SpikeConfiguration implements FeatureConfiguration {
    public static final Codec<SpikeConfiguration> CODEC;
    private final boolean crystalInvulnerable;
    private final List<SpikeFeature.EndSpike> spikes;
    @Nullable
    private final BlockPos crystalBeamTarget;
    
    public SpikeConfiguration(final boolean boolean1, final List<SpikeFeature.EndSpike> list, @Nullable final BlockPos fx) {
        this(boolean1, list, (Optional<BlockPos>)Optional.ofNullable(fx));
    }
    
    private SpikeConfiguration(final boolean boolean1, final List<SpikeFeature.EndSpike> list, final Optional<BlockPos> optional) {
        this.crystalInvulnerable = boolean1;
        this.spikes = list;
        this.crystalBeamTarget = (BlockPos)optional.orElse(null);
    }
    
    public boolean isCrystalInvulnerable() {
        return this.crystalInvulnerable;
    }
    
    public List<SpikeFeature.EndSpike> getSpikes() {
        return this.spikes;
    }
    
    @Nullable
    public BlockPos getCrystalBeamTarget() {
        return this.crystalBeamTarget;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.BOOL.fieldOf("crystal_invulnerable").orElse(false).forGetter(cms -> cms.crystalInvulnerable), (App)SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(cms -> cms.spikes), (App)BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(cms -> Optional.ofNullable(cms.crystalBeamTarget))).apply((Applicative)instance, SpikeConfiguration::new));
    }
}
