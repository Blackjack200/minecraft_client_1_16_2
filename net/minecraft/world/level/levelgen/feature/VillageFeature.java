package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import com.mojang.serialization.Codec;

public class VillageFeature extends JigsawFeature {
    public VillageFeature(final Codec<JigsawConfiguration> codec) {
        super(codec, 0, true, true);
    }
}
