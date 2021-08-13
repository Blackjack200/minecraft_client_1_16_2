package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Registry;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import net.minecraft.world.level.material.FluidState;
import com.mojang.serialization.Codec;

public class SpringConfiguration implements FeatureConfiguration {
    public static final Codec<SpringConfiguration> CODEC;
    public final FluidState state;
    public final boolean requiresBlockBelow;
    public final int rockCount;
    public final int holeCount;
    public final Set<Block> validBlocks;
    
    public SpringConfiguration(final FluidState cuu, final boolean boolean2, final int integer3, final int integer4, final Set<Block> set) {
        this.state = cuu;
        this.requiresBlockBelow = boolean2;
        this.rockCount = integer3;
        this.holeCount = integer4;
        this.validBlocks = set;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)FluidState.CODEC.fieldOf("state").forGetter(cmt -> cmt.state), (App)Codec.BOOL.fieldOf("requires_block_below").orElse(true).forGetter(cmt -> cmt.requiresBlockBelow), (App)Codec.INT.fieldOf("rock_count").orElse(4).forGetter(cmt -> cmt.rockCount), (App)Codec.INT.fieldOf("hole_count").orElse(1).forGetter(cmt -> cmt.holeCount), (App)Registry.BLOCK.listOf().fieldOf("valid_blocks").xmap(ImmutableSet::copyOf, ImmutableList::copyOf).forGetter(cmt -> cmt.validBlocks)).apply((Applicative)instance, SpringConfiguration::new));
    }
}
