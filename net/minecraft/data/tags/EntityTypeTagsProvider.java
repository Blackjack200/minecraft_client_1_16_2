package net.minecraft.data.tags;

import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.tags.Tag;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsProvider extends TagsProvider<EntityType<?>> {
    public EntityTypeTagsProvider(final DataGenerator hl) {
        super(hl, Registry.ENTITY_TYPE);
    }
    
    @Override
    protected void addTags() {
        this.tag(EntityTypeTags.SKELETONS).add(EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON);
        this.tag(EntityTypeTags.RAIDERS).add(EntityType.EVOKER, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.WITCH);
        ((TagsProvider<EntityType<Bee>>)this).tag((Tag.Named<EntityType<Bee>>)EntityTypeTags.BEEHIVE_INHABITORS).add(EntityType.BEE);
        this.tag(EntityTypeTags.ARROWS).add(EntityType.ARROW, EntityType.SPECTRAL_ARROW);
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).addTag(EntityTypeTags.ARROWS).add(EntityType.SNOWBALL, EntityType.FIREBALL, EntityType.SMALL_FIREBALL, EntityType.EGG, EntityType.TRIDENT, EntityType.DRAGON_FIREBALL, EntityType.WITHER_SKULL);
    }
    
    @Override
    protected Path getPath(final ResourceLocation vk) {
        return this.generator.getOutputFolder().resolve("data/" + vk.getNamespace() + "/tags/entity_types/" + vk.getPath() + ".json");
    }
    
    public String getName() {
        return "Entity Type Tags";
    }
}
