package net.minecraft.data.models.model;

import com.google.gson.JsonObject;
import com.google.common.collect.ImmutableMap;
import java.util.function.Function;
import com.google.common.collect.Streams;
import java.util.stream.Stream;
import java.util.Map;
import com.google.gson.JsonElement;
import java.util.function.Supplier;
import java.util.function.BiConsumer;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.Optional;

public class ModelTemplate {
    private final Optional<ResourceLocation> model;
    private final Set<TextureSlot> requiredSlots;
    private Optional<String> suffix;
    
    public ModelTemplate(final Optional<ResourceLocation> optional1, final Optional<String> optional2, final TextureSlot... arr) {
        this.model = optional1;
        this.suffix = optional2;
        this.requiredSlots = (Set<TextureSlot>)ImmutableSet.copyOf((Object[])arr);
    }
    
    public ResourceLocation create(final Block bul, final TextureMapping iz, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        return this.create(ModelLocationUtils.getModelLocation(bul, (String)this.suffix.orElse("")), iz, biConsumer);
    }
    
    public ResourceLocation createWithSuffix(final Block bul, final String string, final TextureMapping iz, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        return this.create(ModelLocationUtils.getModelLocation(bul, string + (String)this.suffix.orElse("")), iz, biConsumer);
    }
    
    public ResourceLocation createWithOverride(final Block bul, final String string, final TextureMapping iz, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        return this.create(ModelLocationUtils.getModelLocation(bul, string), iz, biConsumer);
    }
    
    public ResourceLocation create(final ResourceLocation vk, final TextureMapping iz, final BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        final Map<TextureSlot, ResourceLocation> map5 = this.createMap(iz);
        biConsumer.accept(vk, (() -> {
            final JsonObject jsonObject3 = new JsonObject();
            this.model.ifPresent(vk -> jsonObject3.addProperty("parent", vk.toString()));
            if (!map5.isEmpty()) {
                final JsonObject jsonObject4 = new JsonObject();
                map5.forEach((ja, vk) -> jsonObject4.addProperty(ja.getId(), vk.toString()));
                jsonObject3.add("textures", (JsonElement)jsonObject4);
            }
            return jsonObject3;
        }));
        return vk;
    }
    
    private Map<TextureSlot, ResourceLocation> createMap(final TextureMapping iz) {
        return (Map<TextureSlot, ResourceLocation>)Streams.concat(new Stream[] { this.requiredSlots.stream(), iz.getForced() }).collect(ImmutableMap.toImmutableMap(Function.identity(), iz::get));
    }
}
