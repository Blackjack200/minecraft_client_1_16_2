package net.minecraft.tags;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import java.util.stream.Collectors;
import java.util.Map;

public class SerializationTags {
    private static volatile TagContainer instance;
    
    public static TagContainer getInstance() {
        return SerializationTags.instance;
    }
    
    public static void bind(final TagContainer ael) {
        SerializationTags.instance = ael;
    }
    
    static {
        SerializationTags.instance = TagContainer.of(TagCollection.<Block>of((java.util.Map<ResourceLocation, Tag<Block>>)BlockTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, e -> e))), TagCollection.<Item>of((java.util.Map<ResourceLocation, Tag<Item>>)ItemTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, e -> e))), TagCollection.<Fluid>of((java.util.Map<ResourceLocation, Tag<Fluid>>)FluidTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, e -> e))), TagCollection.<EntityType<?>>of((java.util.Map<ResourceLocation, Tag<EntityType<?>>>)EntityTypeTags.getWrappers().stream().collect(Collectors.toMap(Tag.Named::getName, e -> e))));
    }
}
