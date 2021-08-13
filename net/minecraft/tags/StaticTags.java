package net.minecraft.tags;

import com.google.common.collect.Maps;
import java.util.stream.Stream;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class StaticTags {
    private static final Map<ResourceLocation, StaticTagHelper<?>> HELPERS;
    
    public static <T> StaticTagHelper<T> create(final ResourceLocation vk, final Function<TagContainer, TagCollection<T>> function) {
        final StaticTagHelper<T> aeh3 = new StaticTagHelper<T>(function);
        final StaticTagHelper<?> aeh4 = StaticTags.HELPERS.putIfAbsent(vk, aeh3);
        if (aeh4 != null) {
            throw new IllegalStateException(new StringBuilder().append("Duplicate entry for static tag collection: ").append(vk).toString());
        }
        return aeh3;
    }
    
    public static void resetAll(final TagContainer ael) {
        StaticTags.HELPERS.values().forEach(aeh -> aeh.reset(ael));
    }
    
    public static void resetAllToEmpty() {
        StaticTags.HELPERS.values().forEach(StaticTagHelper::resetToEmpty);
    }
    
    public static Multimap<ResourceLocation, ResourceLocation> getAllMissingTags(final TagContainer ael) {
        final Multimap<ResourceLocation, ResourceLocation> multimap2 = (Multimap<ResourceLocation, ResourceLocation>)HashMultimap.create();
        StaticTags.HELPERS.forEach((vk, aeh) -> multimap2.putAll(vk, (Iterable)aeh.getMissingTags(ael)));
        return multimap2;
    }
    
    public static void bootStrap() {
        final StaticTagHelper[] arr1 = { BlockTags.HELPER, ItemTags.HELPER, FluidTags.HELPER, EntityTypeTags.HELPER };
        final boolean boolean2 = Stream.of((Object[])arr1).anyMatch(aeh -> !StaticTags.HELPERS.containsValue(aeh));
        if (boolean2) {
            throw new IllegalStateException("Missing helper registrations");
        }
    }
    
    static {
        HELPERS = (Map)Maps.newHashMap();
    }
}
