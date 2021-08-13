package net.minecraft.tags;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public interface TagCollection<T> {
    Map<ResourceLocation, Tag<T>> getAllTags();
    
    @Nullable
    default Tag<T> getTag(final ResourceLocation vk) {
        return (Tag<T>)this.getAllTags().get(vk);
    }
    
    Tag<T> getTagOrEmpty(final ResourceLocation vk);
    
    @Nullable
    ResourceLocation getId(final Tag<T> aej);
    
    default ResourceLocation getIdOrThrow(final Tag<T> aej) {
        final ResourceLocation vk3 = this.getId(aej);
        if (vk3 == null) {
            throw new IllegalStateException("Unrecognized tag");
        }
        return vk3;
    }
    
    default Collection<ResourceLocation> getAvailableTags() {
        return (Collection<ResourceLocation>)this.getAllTags().keySet();
    }
    
    default Collection<ResourceLocation> getMatchingTags(final T object) {
        final List<ResourceLocation> list3 = (List<ResourceLocation>)Lists.newArrayList();
        for (final Map.Entry<ResourceLocation, Tag<T>> entry5 : this.getAllTags().entrySet()) {
            if (((Tag)entry5.getValue()).contains(object)) {
                list3.add(entry5.getKey());
            }
        }
        return (Collection<ResourceLocation>)list3;
    }
    
    default void serializeToNetwork(final FriendlyByteBuf nf, final DefaultedRegistry<T> gb) {
        final Map<ResourceLocation, Tag<T>> map4 = this.getAllTags();
        nf.writeVarInt(map4.size());
        for (final Map.Entry<ResourceLocation, Tag<T>> entry6 : map4.entrySet()) {
            nf.writeResourceLocation((ResourceLocation)entry6.getKey());
            nf.writeVarInt(((Tag)entry6.getValue()).getValues().size());
            for (final T object8 : ((Tag)entry6.getValue()).getValues()) {
                nf.writeVarInt(gb.getId(object8));
            }
        }
    }
    
    default <T> TagCollection<T> loadFromNetwork(final FriendlyByteBuf nf, final Registry<T> gm) {
        final Map<ResourceLocation, Tag<T>> map3 = (Map<ResourceLocation, Tag<T>>)Maps.newHashMap();
        for (int integer4 = nf.readVarInt(), integer5 = 0; integer5 < integer4; ++integer5) {
            final ResourceLocation vk6 = nf.readResourceLocation();
            final int integer6 = nf.readVarInt();
            final ImmutableSet.Builder<T> builder8 = (ImmutableSet.Builder<T>)ImmutableSet.builder();
            for (int integer7 = 0; integer7 < integer6; ++integer7) {
                builder8.add(gm.byId(nf.readVarInt()));
            }
            map3.put(vk6, Tag.fromSet((java.util.Set<Object>)builder8.build()));
        }
        return TagCollection.<T>of(map3);
    }
    
    default <T> TagCollection<T> empty() {
        return TagCollection.<T>of((java.util.Map<ResourceLocation, Tag<T>>)ImmutableBiMap.of());
    }
    
    default <T> TagCollection<T> of(final Map<ResourceLocation, Tag<T>> map) {
        final BiMap<ResourceLocation, Tag<T>> biMap2 = (BiMap<ResourceLocation, Tag<T>>)ImmutableBiMap.copyOf((Map)map);
        return new TagCollection<T>() {
            private final Tag<T> empty = SetTag.empty();
            
            public Tag<T> getTagOrEmpty(final ResourceLocation vk) {
                return (Tag<T>)biMap2.getOrDefault(vk, this.empty);
            }
            
            @Nullable
            public ResourceLocation getId(final Tag<T> aej) {
                if (aej instanceof Tag.Named) {
                    return ((Tag.Named)aej).getName();
                }
                return (ResourceLocation)biMap2.inverse().get(aej);
            }
            
            public Map<ResourceLocation, Tag<T>> getAllTags() {
                return (Map<ResourceLocation, Tag<T>>)biMap2;
            }
        };
    }
}
