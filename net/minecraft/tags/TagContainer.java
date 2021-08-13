package net.minecraft.tags;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface TagContainer {
    public static final TagContainer EMPTY = of(TagCollection.<Block>empty(), TagCollection.<Item>empty(), TagCollection.<Fluid>empty(), TagCollection.<EntityType<?>>empty());
    
    TagCollection<Block> getBlocks();
    
    TagCollection<Item> getItems();
    
    TagCollection<Fluid> getFluids();
    
    TagCollection<EntityType<?>> getEntityTypes();
    
    default void bindToGlobal() {
        StaticTags.resetAll(this);
        Blocks.rebuildCache();
    }
    
    default void serializeToNetwork(final FriendlyByteBuf nf) {
        this.getBlocks().serializeToNetwork(nf, Registry.BLOCK);
        this.getItems().serializeToNetwork(nf, Registry.ITEM);
        this.getFluids().serializeToNetwork(nf, Registry.FLUID);
        this.getEntityTypes().serializeToNetwork(nf, Registry.ENTITY_TYPE);
    }
    
    default TagContainer deserializeFromNetwork(final FriendlyByteBuf nf) {
        final TagCollection<Block> aek2 = TagCollection.<Block>loadFromNetwork(nf, Registry.BLOCK);
        final TagCollection<Item> aek3 = TagCollection.<Item>loadFromNetwork(nf, Registry.ITEM);
        final TagCollection<Fluid> aek4 = TagCollection.<Fluid>loadFromNetwork(nf, Registry.FLUID);
        final TagCollection<EntityType<?>> aek5 = TagCollection.<EntityType<?>>loadFromNetwork(nf, Registry.ENTITY_TYPE);
        return of(aek2, aek3, aek4, aek5);
    }
    
    default TagContainer of(final TagCollection<Block> aek1, final TagCollection<Item> aek2, final TagCollection<Fluid> aek3, final TagCollection<EntityType<?>> aek4) {
        return new TagContainer() {
            public TagCollection<Block> getBlocks() {
                return aek1;
            }
            
            public TagCollection<Item> getItems() {
                return aek2;
            }
            
            public TagCollection<Fluid> getFluids() {
                return aek3;
            }
            
            public TagCollection<EntityType<?>> getEntityTypes() {
                return aek4;
            }
        };
    }
}
