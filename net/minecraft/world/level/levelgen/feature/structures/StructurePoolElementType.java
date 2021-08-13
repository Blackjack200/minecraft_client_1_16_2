package net.minecraft.world.level.levelgen.feature.structures;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public interface StructurePoolElementType<P extends StructurePoolElement> {
    public static final StructurePoolElementType<SinglePoolElement> SINGLE = StructurePoolElementType.<SinglePoolElement>register("single_pool_element", SinglePoolElement.CODEC);
    public static final StructurePoolElementType<ListPoolElement> LIST = StructurePoolElementType.<ListPoolElement>register("list_pool_element", ListPoolElement.CODEC);
    public static final StructurePoolElementType<FeaturePoolElement> FEATURE = StructurePoolElementType.<FeaturePoolElement>register("feature_pool_element", FeaturePoolElement.CODEC);
    public static final StructurePoolElementType<EmptyPoolElement> EMPTY = StructurePoolElementType.<EmptyPoolElement>register("empty_pool_element", EmptyPoolElement.CODEC);
    public static final StructurePoolElementType<LegacySinglePoolElement> LEGACY = StructurePoolElementType.<LegacySinglePoolElement>register("legacy_single_pool_element", LegacySinglePoolElement.CODEC);
    
    Codec<P> codec();
    
    default <P extends StructurePoolElement> StructurePoolElementType<P> register(final String string, final Codec<P> codec) {
        return Registry.<StructurePoolElementType<P>>register(Registry.STRUCTURE_POOL_ELEMENT, string, () -> codec);
    }
}
