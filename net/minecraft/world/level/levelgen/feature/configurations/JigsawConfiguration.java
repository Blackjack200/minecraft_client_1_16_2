package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class JigsawConfiguration implements FeatureConfiguration {
    public static final Codec<JigsawConfiguration> CODEC;
    private final Supplier<StructureTemplatePool> startPool;
    private final int maxDepth;
    
    public JigsawConfiguration(final Supplier<StructureTemplatePool> supplier, final int integer) {
        this.startPool = supplier;
        this.maxDepth = integer;
    }
    
    public int maxDepth() {
        return this.maxDepth;
    }
    
    public Supplier<StructureTemplatePool> startPool() {
        return this.startPool;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool), (App)Codec.intRange(0, 7).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)).apply((Applicative)instance, JigsawConfiguration::new));
    }
}
