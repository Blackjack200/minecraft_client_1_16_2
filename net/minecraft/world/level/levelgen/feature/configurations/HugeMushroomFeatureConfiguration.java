package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class HugeMushroomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<HugeMushroomFeatureConfiguration> CODEC;
    public final BlockStateProvider capProvider;
    public final BlockStateProvider stemProvider;
    public final int foliageRadius;
    
    public HugeMushroomFeatureConfiguration(final BlockStateProvider cnq1, final BlockStateProvider cnq2, final int integer) {
        this.capProvider = cnq1;
        this.stemProvider = cnq2;
        this.foliageRadius = integer;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(cly -> cly.capProvider), (App)BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(cly -> cly.stemProvider), (App)Codec.INT.fieldOf("foliage_radius").orElse(2).forGetter(cly -> cly.foliageRadius)).apply((Applicative)instance, HugeMushroomFeatureConfiguration::new));
    }
}
