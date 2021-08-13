package net.minecraft.advancements.critereon;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.tags.SerializationTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;

public class FluidPredicate {
    public static final FluidPredicate ANY;
    @Nullable
    private final Tag<Fluid> tag;
    @Nullable
    private final Fluid fluid;
    private final StatePropertiesPredicate properties;
    
    public FluidPredicate(@Nullable final Tag<Fluid> aej, @Nullable final Fluid cut, final StatePropertiesPredicate cm) {
        this.tag = aej;
        this.fluid = cut;
        this.properties = cm;
    }
    
    public boolean matches(final ServerLevel aag, final BlockPos fx) {
        if (this == FluidPredicate.ANY) {
            return true;
        }
        if (!aag.isLoaded(fx)) {
            return false;
        }
        final FluidState cuu4 = aag.getFluidState(fx);
        final Fluid cut5 = cuu4.getType();
        return (this.tag == null || this.tag.contains(cut5)) && (this.fluid == null || cut5 == this.fluid) && this.properties.matches(cuu4);
    }
    
    public static FluidPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return FluidPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "fluid");
        Fluid cut3 = null;
        if (jsonObject2.has("fluid")) {
            final ResourceLocation vk4 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "fluid"));
            cut3 = Registry.FLUID.get(vk4);
        }
        Tag<Fluid> aej4 = null;
        if (jsonObject2.has("tag")) {
            final ResourceLocation vk5 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "tag"));
            aej4 = SerializationTags.getInstance().getFluids().getTag(vk5);
            if (aej4 == null) {
                throw new JsonSyntaxException(new StringBuilder().append("Unknown fluid tag '").append(vk5).append("'").toString());
            }
        }
        final StatePropertiesPredicate cm5 = StatePropertiesPredicate.fromJson(jsonObject2.get("state"));
        return new FluidPredicate(aej4, cut3, cm5);
    }
    
    public JsonElement serializeToJson() {
        if (this == FluidPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (this.fluid != null) {
            jsonObject2.addProperty("fluid", Registry.FLUID.getKey(this.fluid).toString());
        }
        if (this.tag != null) {
            jsonObject2.addProperty("tag", SerializationTags.getInstance().getFluids().getIdOrThrow(this.tag).toString());
        }
        jsonObject2.add("state", this.properties.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new FluidPredicate(null, null, StatePropertiesPredicate.ANY);
    }
}
