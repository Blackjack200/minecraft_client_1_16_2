package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Registry;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public interface StructureProcessorType<P extends StructureProcessor> {
    public static final StructureProcessorType<BlockIgnoreProcessor> BLOCK_IGNORE = StructureProcessorType.<BlockIgnoreProcessor>register("block_ignore", BlockIgnoreProcessor.CODEC);
    public static final StructureProcessorType<BlockRotProcessor> BLOCK_ROT = StructureProcessorType.<BlockRotProcessor>register("block_rot", BlockRotProcessor.CODEC);
    public static final StructureProcessorType<GravityProcessor> GRAVITY = StructureProcessorType.<GravityProcessor>register("gravity", GravityProcessor.CODEC);
    public static final StructureProcessorType<JigsawReplacementProcessor> JIGSAW_REPLACEMENT = StructureProcessorType.<JigsawReplacementProcessor>register("jigsaw_replacement", JigsawReplacementProcessor.CODEC);
    public static final StructureProcessorType<RuleProcessor> RULE = StructureProcessorType.<RuleProcessor>register("rule", RuleProcessor.CODEC);
    public static final StructureProcessorType<NopProcessor> NOP = StructureProcessorType.<NopProcessor>register("nop", NopProcessor.CODEC);
    public static final StructureProcessorType<BlockAgeProcessor> BLOCK_AGE = StructureProcessorType.<BlockAgeProcessor>register("block_age", BlockAgeProcessor.CODEC);
    public static final StructureProcessorType<BlackstoneReplaceProcessor> BLACKSTONE_REPLACE = StructureProcessorType.<BlackstoneReplaceProcessor>register("blackstone_replace", BlackstoneReplaceProcessor.CODEC);
    public static final StructureProcessorType<LavaSubmergedBlockProcessor> LAVA_SUBMERGED_BLOCK = StructureProcessorType.<LavaSubmergedBlockProcessor>register("lava_submerged_block", LavaSubmergedBlockProcessor.CODEC);
    public static final Codec<StructureProcessor> SINGLE_CODEC = Registry.STRUCTURE_PROCESSOR.dispatch("processor_type", StructureProcessor::getType, StructureProcessorType::codec);
    public static final Codec<StructureProcessorList> LIST_OBJECT_CODEC = StructureProcessorType.SINGLE_CODEC.listOf().xmap(StructureProcessorList::new, StructureProcessorList::list);
    public static final Codec<StructureProcessorList> DIRECT_CODEC = Codec.either(StructureProcessorType.LIST_OBJECT_CODEC.fieldOf("processors").codec(), (Codec)StructureProcessorType.LIST_OBJECT_CODEC).xmap(either -> (StructureProcessorList)either.map(csw -> csw, csw -> csw), Either::left);
    public static final Codec<Supplier<StructureProcessorList>> LIST_CODEC = RegistryFileCodec.<StructureProcessorList>create(Registry.PROCESSOR_LIST_REGISTRY, StructureProcessorType.DIRECT_CODEC);
    
    Codec<P> codec();
    
    default <P extends StructureProcessor> StructureProcessorType<P> register(final String string, final Codec<P> codec) {
        return Registry.<StructureProcessorType<P>>register(Registry.STRUCTURE_PROCESSOR, string, () -> codec);
    }
}
