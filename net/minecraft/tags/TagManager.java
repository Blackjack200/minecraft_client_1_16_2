package net.minecraft.tags;

import com.google.common.collect.Multimap;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class TagManager implements PreparableReloadListener {
    private final TagLoader<Block> blocks;
    private final TagLoader<Item> items;
    private final TagLoader<Fluid> fluids;
    private final TagLoader<EntityType<?>> entityTypes;
    private TagContainer tags;
    
    public TagManager() {
        this.blocks = new TagLoader<Block>((java.util.function.Function<ResourceLocation, java.util.Optional<Block>>)Registry.BLOCK::getOptional, "tags/blocks", "block");
        this.items = new TagLoader<Item>((java.util.function.Function<ResourceLocation, java.util.Optional<Item>>)Registry.ITEM::getOptional, "tags/items", "item");
        this.fluids = new TagLoader<Fluid>((java.util.function.Function<ResourceLocation, java.util.Optional<Fluid>>)Registry.FLUID::getOptional, "tags/fluids", "fluid");
        this.entityTypes = new TagLoader<EntityType<?>>((java.util.function.Function<ResourceLocation, java.util.Optional<EntityType<?>>>)Registry.ENTITY_TYPE::getOptional, "tags/entity_types", "entity_type");
        this.tags = TagContainer.EMPTY;
    }
    
    public TagContainer getTags() {
        return this.tags;
    }
    
    public CompletableFuture<Void> reload(final PreparationBarrier a, final ResourceManager acf, final ProfilerFiller ant3, final ProfilerFiller ant4, final Executor executor5, final Executor executor6) {
        final CompletableFuture<Map<ResourceLocation, Tag.Builder>> completableFuture8 = this.blocks.prepare(acf, executor5);
        final CompletableFuture<Map<ResourceLocation, Tag.Builder>> completableFuture9 = this.items.prepare(acf, executor5);
        final CompletableFuture<Map<ResourceLocation, Tag.Builder>> completableFuture10 = this.fluids.prepare(acf, executor5);
        final CompletableFuture<Map<ResourceLocation, Tag.Builder>> completableFuture11 = this.entityTypes.prepare(acf, executor5);
        return (CompletableFuture<Void>)CompletableFuture.allOf(new CompletableFuture[] { completableFuture8, completableFuture9, completableFuture10, completableFuture11 }).thenCompose(a::wait).thenAcceptAsync(void5 -> {
            final TagCollection<Block> aek7 = this.blocks.load((Map<ResourceLocation, Tag.Builder>)completableFuture8.join());
            final TagCollection<Item> aek8 = this.items.load((Map<ResourceLocation, Tag.Builder>)completableFuture9.join());
            final TagCollection<Fluid> aek9 = this.fluids.load((Map<ResourceLocation, Tag.Builder>)completableFuture10.join());
            final TagCollection<EntityType<?>> aek10 = this.entityTypes.load((Map<ResourceLocation, Tag.Builder>)completableFuture11.join());
            final TagContainer ael11 = TagContainer.of(aek7, aek8, aek9, aek10);
            final Multimap<ResourceLocation, ResourceLocation> multimap12 = StaticTags.getAllMissingTags(ael11);
            if (!multimap12.isEmpty()) {
                throw new IllegalStateException("Missing required tags: " + (String)multimap12.entries().stream().map(entry -> new StringBuilder().append(entry.getKey()).append(":").append(entry.getValue()).toString()).sorted().collect(Collectors.joining(",")));
            }
            SerializationTags.bind(ael11);
            this.tags = ael11;
        }, executor6);
    }
}
