package net.minecraft.tags;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.world.entity.EntityType;

public final class EntityTypeTags {
    protected static final StaticTagHelper<EntityType<?>> HELPER;
    public static final Tag.Named<EntityType<?>> SKELETONS;
    public static final Tag.Named<EntityType<?>> RAIDERS;
    public static final Tag.Named<EntityType<?>> BEEHIVE_INHABITORS;
    public static final Tag.Named<EntityType<?>> ARROWS;
    public static final Tag.Named<EntityType<?>> IMPACT_PROJECTILES;
    
    private static Tag.Named<EntityType<?>> bind(final String string) {
        return EntityTypeTags.HELPER.bind(string);
    }
    
    public static TagCollection<EntityType<?>> getAllTags() {
        return EntityTypeTags.HELPER.getAllTags();
    }
    
    public static List<? extends Tag.Named<EntityType<?>>> getWrappers() {
        return EntityTypeTags.HELPER.getWrappers();
    }
    
    static {
        HELPER = StaticTags.<EntityType<?>>create(new ResourceLocation("entity_type"), (java.util.function.Function<TagContainer, TagCollection<EntityType<?>>>)TagContainer::getEntityTypes);
        SKELETONS = bind("skeletons");
        RAIDERS = bind("raiders");
        BEEHIVE_INHABITORS = bind("beehive_inhabitors");
        ARROWS = bind("arrows");
        IMPACT_PROJECTILES = bind("impact_projectiles");
    }
}
