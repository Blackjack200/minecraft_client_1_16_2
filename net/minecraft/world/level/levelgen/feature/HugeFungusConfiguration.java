package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.Blocks;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class HugeFungusConfiguration implements FeatureConfiguration {
    public static final Codec<HugeFungusConfiguration> CODEC;
    public static final HugeFungusConfiguration HUGE_CRIMSON_FUNGI_PLANTED_CONFIG;
    public static final HugeFungusConfiguration HUGE_CRIMSON_FUNGI_NOT_PLANTED_CONFIG;
    public static final HugeFungusConfiguration HUGE_WARPED_FUNGI_PLANTED_CONFIG;
    public static final HugeFungusConfiguration HUGE_WARPED_FUNGI_NOT_PLANTED_CONFIG;
    public final BlockState validBaseState;
    public final BlockState stemState;
    public final BlockState hatState;
    public final BlockState decorState;
    public final boolean planted;
    
    public HugeFungusConfiguration(final BlockState cee1, final BlockState cee2, final BlockState cee3, final BlockState cee4, final boolean boolean5) {
        this.validBaseState = cee1;
        this.stemState = cee2;
        this.hatState = cee3;
        this.decorState = cee4;
        this.planted = boolean5;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("valid_base_block").forGetter(cjn -> cjn.validBaseState), (App)BlockState.CODEC.fieldOf("stem_state").forGetter(cjn -> cjn.stemState), (App)BlockState.CODEC.fieldOf("hat_state").forGetter(cjn -> cjn.hatState), (App)BlockState.CODEC.fieldOf("decor_state").forGetter(cjn -> cjn.decorState), (App)Codec.BOOL.fieldOf("planted").orElse(false).forGetter(cjn -> cjn.planted)).apply((Applicative)instance, HugeFungusConfiguration::new));
        HUGE_CRIMSON_FUNGI_PLANTED_CONFIG = new HugeFungusConfiguration(Blocks.CRIMSON_NYLIUM.defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState(), Blocks.NETHER_WART_BLOCK.defaultBlockState(), Blocks.SHROOMLIGHT.defaultBlockState(), true);
        HUGE_CRIMSON_FUNGI_NOT_PLANTED_CONFIG = new HugeFungusConfiguration(HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.validBaseState, HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.stemState, HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.hatState, HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.decorState, false);
        HUGE_WARPED_FUNGI_PLANTED_CONFIG = new HugeFungusConfiguration(Blocks.WARPED_NYLIUM.defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(), Blocks.WARPED_WART_BLOCK.defaultBlockState(), Blocks.SHROOMLIGHT.defaultBlockState(), true);
        HUGE_WARPED_FUNGI_NOT_PLANTED_CONFIG = new HugeFungusConfiguration(HugeFungusConfiguration.HUGE_WARPED_FUNGI_PLANTED_CONFIG.validBaseState, HugeFungusConfiguration.HUGE_WARPED_FUNGI_PLANTED_CONFIG.stemState, HugeFungusConfiguration.HUGE_WARPED_FUNGI_PLANTED_CONFIG.hatState, HugeFungusConfiguration.HUGE_WARPED_FUNGI_PLANTED_CONFIG.decorState, false);
    }
}
