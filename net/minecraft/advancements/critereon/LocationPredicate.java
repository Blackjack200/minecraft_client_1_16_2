package net.minecraft.advancements.critereon;

import org.apache.logging.log4j.LogManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import java.util.Optional;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import javax.annotation.Nullable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.Logger;

public class LocationPredicate {
    private static final Logger LOGGER;
    public static final LocationPredicate ANY;
    private final MinMaxBounds.Floats x;
    private final MinMaxBounds.Floats y;
    private final MinMaxBounds.Floats z;
    @Nullable
    private final ResourceKey<Biome> biome;
    @Nullable
    private final StructureFeature<?> feature;
    @Nullable
    private final ResourceKey<Level> dimension;
    @Nullable
    private final Boolean smokey;
    private final LightPredicate light;
    private final BlockPredicate block;
    private final FluidPredicate fluid;
    
    public LocationPredicate(final MinMaxBounds.Floats c1, final MinMaxBounds.Floats c2, final MinMaxBounds.Floats c3, @Nullable final ResourceKey<Biome> vj4, @Nullable final StructureFeature<?> ckx, @Nullable final ResourceKey<Level> vj6, @Nullable final Boolean boolean7, final LightPredicate bv, final BlockPredicate an, final FluidPredicate bl) {
        this.x = c1;
        this.y = c2;
        this.z = c3;
        this.biome = vj4;
        this.feature = ckx;
        this.dimension = vj6;
        this.smokey = boolean7;
        this.light = bv;
        this.block = an;
        this.fluid = bl;
    }
    
    public static LocationPredicate inBiome(final ResourceKey<Biome> vj) {
        return new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, vj, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }
    
    public static LocationPredicate inDimension(final ResourceKey<Level> vj) {
        return new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, null, null, vj, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }
    
    public static LocationPredicate inFeature(final StructureFeature<?> ckx) {
        return new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, null, ckx, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }
    
    public boolean matches(final ServerLevel aag, final double double2, final double double3, final double double4) {
        return this.matches(aag, (float)double2, (float)double3, (float)double4);
    }
    
    public boolean matches(final ServerLevel aag, final float float2, final float float3, final float float4) {
        if (!this.x.matches(float2)) {
            return false;
        }
        if (!this.y.matches(float3)) {
            return false;
        }
        if (!this.z.matches(float4)) {
            return false;
        }
        if (this.dimension != null && this.dimension != aag.dimension()) {
            return false;
        }
        final BlockPos fx6 = new BlockPos(float2, float3, float4);
        final boolean boolean7 = aag.isLoaded(fx6);
        final Optional<ResourceKey<Biome>> optional8 = aag.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(aag.getBiome(fx6));
        return optional8.isPresent() && (this.biome == null || (boolean7 && this.biome == optional8.get())) && (this.feature == null || (boolean7 && aag.structureFeatureManager().getStructureAt(fx6, true, this.feature).isValid())) && (this.smokey == null || (boolean7 && this.smokey == CampfireBlock.isSmokeyPos(aag, fx6))) && this.light.matches(aag, fx6) && this.block.matches(aag, fx6) && this.fluid.matches(aag, fx6);
    }
    
    public JsonElement serializeToJson() {
        if (this == LocationPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (!this.x.isAny() || !this.y.isAny() || !this.z.isAny()) {
            final JsonObject jsonObject3 = new JsonObject();
            jsonObject3.add("x", this.x.serializeToJson());
            jsonObject3.add("y", this.y.serializeToJson());
            jsonObject3.add("z", this.z.serializeToJson());
            jsonObject2.add("position", (JsonElement)jsonObject3);
        }
        if (this.dimension != null) {
            Level.RESOURCE_KEY_CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, this.dimension).resultOrPartial(LocationPredicate.LOGGER::error).ifPresent(jsonElement -> jsonObject2.add("dimension", jsonElement));
        }
        if (this.feature != null) {
            jsonObject2.addProperty("feature", this.feature.getFeatureName());
        }
        if (this.biome != null) {
            jsonObject2.addProperty("biome", this.biome.location().toString());
        }
        if (this.smokey != null) {
            jsonObject2.addProperty("smokey", this.smokey);
        }
        jsonObject2.add("light", this.light.serializeToJson());
        jsonObject2.add("block", this.block.serializeToJson());
        jsonObject2.add("fluid", this.fluid.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    public static LocationPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return LocationPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "location");
        final JsonObject jsonObject3 = GsonHelper.getAsJsonObject(jsonObject2, "position", new JsonObject());
        final MinMaxBounds.Floats c4 = MinMaxBounds.Floats.fromJson(jsonObject3.get("x"));
        final MinMaxBounds.Floats c5 = MinMaxBounds.Floats.fromJson(jsonObject3.get("y"));
        final MinMaxBounds.Floats c6 = MinMaxBounds.Floats.fromJson(jsonObject3.get("z"));
        final ResourceKey<Level> vj7 = (ResourceKey<Level>)(jsonObject2.has("dimension") ? ((ResourceKey)ResourceLocation.CODEC.parse((DynamicOps)JsonOps.INSTANCE, jsonObject2.get("dimension")).resultOrPartial(LocationPredicate.LOGGER::error).map(vk -> ResourceKey.create(Registry.DIMENSION_REGISTRY, vk)).orElse(null)) : null);
        final StructureFeature<?> ckx8 = (jsonObject2.has("feature") ? ((StructureFeature)StructureFeature.STRUCTURES_REGISTRY.get(GsonHelper.getAsString(jsonObject2, "feature"))) : null);
        ResourceKey<Biome> vj8 = null;
        if (jsonObject2.has("biome")) {
            final ResourceLocation vk10 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "biome"));
            vj8 = ResourceKey.<Biome>create(Registry.BIOME_REGISTRY, vk10);
        }
        final Boolean boolean10 = jsonObject2.has("smokey") ? Boolean.valueOf(jsonObject2.get("smokey").getAsBoolean()) : null;
        final LightPredicate bv11 = LightPredicate.fromJson(jsonObject2.get("light"));
        final BlockPredicate an12 = BlockPredicate.fromJson(jsonObject2.get("block"));
        final FluidPredicate bl13 = FluidPredicate.fromJson(jsonObject2.get("fluid"));
        return new LocationPredicate(c4, c5, c6, vj8, ckx8, vj7, boolean10, bv11, an12, bl13);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ANY = new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, null, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
    }
    
    public static class Builder {
        private MinMaxBounds.Floats x;
        private MinMaxBounds.Floats y;
        private MinMaxBounds.Floats z;
        @Nullable
        private ResourceKey<Biome> biome;
        @Nullable
        private StructureFeature<?> feature;
        @Nullable
        private ResourceKey<Level> dimension;
        @Nullable
        private Boolean smokey;
        private LightPredicate light;
        private BlockPredicate block;
        private FluidPredicate fluid;
        
        public Builder() {
            this.x = MinMaxBounds.Floats.ANY;
            this.y = MinMaxBounds.Floats.ANY;
            this.z = MinMaxBounds.Floats.ANY;
            this.light = LightPredicate.ANY;
            this.block = BlockPredicate.ANY;
            this.fluid = FluidPredicate.ANY;
        }
        
        public static Builder location() {
            return new Builder();
        }
        
        public Builder setBiome(@Nullable final ResourceKey<Biome> vj) {
            this.biome = vj;
            return this;
        }
        
        public Builder setBlock(final BlockPredicate an) {
            this.block = an;
            return this;
        }
        
        public Builder setSmokey(final Boolean boolean1) {
            this.smokey = boolean1;
            return this;
        }
        
        public LocationPredicate build() {
            return new LocationPredicate(this.x, this.y, this.z, this.biome, this.feature, this.dimension, this.smokey, this.light, this.block, this.fluid);
        }
    }
}
