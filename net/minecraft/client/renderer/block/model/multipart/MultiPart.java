package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import com.google.gson.JsonDeserializer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.Material;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import java.util.Objects;
import java.util.Iterator;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.block.model.MultiVariant;
import java.util.Set;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.client.resources.model.UnbakedModel;

public class MultiPart implements UnbakedModel {
    private final StateDefinition<Block, BlockState> definition;
    private final List<Selector> selectors;
    
    public MultiPart(final StateDefinition<Block, BlockState> cef, final List<Selector> list) {
        this.definition = cef;
        this.selectors = list;
    }
    
    public List<Selector> getSelectors() {
        return this.selectors;
    }
    
    public Set<MultiVariant> getMultiVariants() {
        final Set<MultiVariant> set2 = (Set<MultiVariant>)Sets.newHashSet();
        for (final Selector ebm4 : this.selectors) {
            set2.add(ebm4.getVariant());
        }
        return set2;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof MultiPart) {
            final MultiPart ebk3 = (MultiPart)object;
            return Objects.equals(this.definition, ebk3.definition) && Objects.equals(this.selectors, ebk3.selectors);
        }
        return false;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.definition, this.selectors });
    }
    
    public Collection<ResourceLocation> getDependencies() {
        return (Collection<ResourceLocation>)this.getSelectors().stream().flatMap(ebm -> ebm.getVariant().getDependencies().stream()).collect(Collectors.toSet());
    }
    
    public Collection<Material> getMaterials(final Function<ResourceLocation, UnbakedModel> function, final Set<Pair<String, String>> set) {
        return (Collection<Material>)this.getSelectors().stream().flatMap(ebm -> ebm.getVariant().getMaterials(function, set).stream()).collect(Collectors.toSet());
    }
    
    @Nullable
    public BakedModel bake(final ModelBakery elk, final Function<Material, TextureAtlasSprite> function, final ModelState eln, final ResourceLocation vk) {
        final MultiPartBakedModel.Builder a6 = new MultiPartBakedModel.Builder();
        for (final Selector ebm8 : this.getSelectors()) {
            final BakedModel elg9 = ebm8.getVariant().bake(elk, function, eln, vk);
            if (elg9 != null) {
                a6.add(ebm8.getPredicate(this.definition), elg9);
            }
        }
        return a6.build();
    }
    
    public static class Deserializer implements JsonDeserializer<MultiPart> {
        private final BlockModelDefinition.Context context;
        
        public Deserializer(final BlockModelDefinition.Context a) {
            this.context = a;
        }
        
        public MultiPart deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new MultiPart(this.context.getDefinition(), this.getSelectors(jsonDeserializationContext, jsonElement.getAsJsonArray()));
        }
        
        private List<Selector> getSelectors(final JsonDeserializationContext jsonDeserializationContext, final JsonArray jsonArray) {
            final List<Selector> list4 = (List<Selector>)Lists.newArrayList();
            for (final JsonElement jsonElement6 : jsonArray) {
                list4.add(jsonDeserializationContext.deserialize(jsonElement6, (Type)Selector.class));
            }
            return list4;
        }
    }
}
