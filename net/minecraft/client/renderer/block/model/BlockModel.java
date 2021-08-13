package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.Optional;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.BuiltInModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Objects;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import java.util.stream.Collectors;
import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.client.resources.model.ModelBakery;
import java.io.StringReader;
import net.minecraft.util.GsonHelper;
import java.io.Reader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.model.Material;
import com.mojang.datafixers.util.Either;
import java.util.Map;
import javax.annotation.Nullable;
import java.util.List;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.model.UnbakedModel;

public class BlockModel implements UnbakedModel {
    private static final Logger LOGGER;
    private static final FaceBakery FACE_BAKERY;
    @VisibleForTesting
    static final Gson GSON;
    private final List<BlockElement> elements;
    @Nullable
    private final GuiLight guiLight;
    private final boolean hasAmbientOcclusion;
    private final ItemTransforms transforms;
    private final List<ItemOverride> overrides;
    public String name;
    @VisibleForTesting
    protected final Map<String, Either<Material, String>> textureMap;
    @Nullable
    protected BlockModel parent;
    @Nullable
    protected ResourceLocation parentLocation;
    
    public static BlockModel fromStream(final Reader reader) {
        return GsonHelper.<BlockModel>fromJson(BlockModel.GSON, reader, BlockModel.class);
    }
    
    public static BlockModel fromString(final String string) {
        return fromStream((Reader)new StringReader(string));
    }
    
    public BlockModel(@Nullable final ResourceLocation vk, final List<BlockElement> list2, final Map<String, Either<Material, String>> map, final boolean boolean4, @Nullable final GuiLight b, final ItemTransforms ebe, final List<ItemOverride> list7) {
        this.name = "";
        this.elements = list2;
        this.hasAmbientOcclusion = boolean4;
        this.guiLight = b;
        this.textureMap = map;
        this.parentLocation = vk;
        this.transforms = ebe;
        this.overrides = list7;
    }
    
    public List<BlockElement> getElements() {
        if (this.elements.isEmpty() && this.parent != null) {
            return this.parent.getElements();
        }
        return this.elements;
    }
    
    public boolean hasAmbientOcclusion() {
        if (this.parent != null) {
            return this.parent.hasAmbientOcclusion();
        }
        return this.hasAmbientOcclusion;
    }
    
    public GuiLight getGuiLight() {
        if (this.guiLight != null) {
            return this.guiLight;
        }
        if (this.parent != null) {
            return this.parent.getGuiLight();
        }
        return GuiLight.SIDE;
    }
    
    public List<ItemOverride> getOverrides() {
        return this.overrides;
    }
    
    private ItemOverrides getItemOverrides(final ModelBakery elk, final BlockModel eax) {
        if (this.overrides.isEmpty()) {
            return ItemOverrides.EMPTY;
        }
        return new ItemOverrides(elk, eax, (Function<ResourceLocation, UnbakedModel>)elk::getModel, this.overrides);
    }
    
    public Collection<ResourceLocation> getDependencies() {
        final Set<ResourceLocation> set2 = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ItemOverride ebb4 : this.overrides) {
            set2.add(ebb4.getModel());
        }
        if (this.parentLocation != null) {
            set2.add(this.parentLocation);
        }
        return (Collection<ResourceLocation>)set2;
    }
    
    public Collection<Material> getMaterials(final Function<ResourceLocation, UnbakedModel> function, final Set<Pair<String, String>> set) {
        final Set<UnbakedModel> set2 = (Set<UnbakedModel>)Sets.newLinkedHashSet();
        for (BlockModel eax5 = this; eax5.parentLocation != null && eax5.parent == null; eax5 = eax5.parent) {
            set2.add(eax5);
            UnbakedModel elq6 = (UnbakedModel)function.apply(eax5.parentLocation);
            if (elq6 == null) {
                BlockModel.LOGGER.warn("No parent '{}' while loading model '{}'", this.parentLocation, eax5);
            }
            if (set2.contains(elq6)) {
                BlockModel.LOGGER.warn("Found 'parent' loop while loading model '{}' in chain: {} -> {}", eax5, set2.stream().map(Object::toString).collect(Collectors.joining(" -> ")), this.parentLocation);
                elq6 = null;
            }
            if (elq6 == null) {
                eax5.parentLocation = ModelBakery.MISSING_MODEL_LOCATION;
                elq6 = (UnbakedModel)function.apply(eax5.parentLocation);
            }
            if (!(elq6 instanceof BlockModel)) {
                throw new IllegalStateException("BlockModel parent has to be a block model.");
            }
            eax5.parent = (BlockModel)elq6;
        }
        final Set<Material> set3 = (Set<Material>)Sets.newHashSet((Object[])new Material[] { this.getMaterial("particle") });
        for (final BlockElement eat8 : this.getElements()) {
            for (final BlockElementFace eau10 : eat8.faces.values()) {
                final Material elj11 = this.getMaterial(eau10.texture);
                if (Objects.equals(elj11.texture(), MissingTextureAtlasSprite.getLocation())) {
                    set.add(Pair.of((Object)eau10.texture, (Object)this.name));
                }
                set3.add(elj11);
            }
        }
        this.overrides.forEach(ebb -> {
            final UnbakedModel elq6 = (UnbakedModel)function.apply(ebb.getModel());
            if (Objects.equals(elq6, this)) {
                return;
            }
            set3.addAll((Collection)elq6.getMaterials(function, set));
        });
        if (this.getRootModel() == ModelBakery.GENERATION_MARKER) {
            ItemModelGenerator.LAYERS.forEach(string -> set3.add(this.getMaterial(string)));
        }
        return (Collection<Material>)set3;
    }
    
    public BakedModel bake(final ModelBakery elk, final Function<Material, TextureAtlasSprite> function, final ModelState eln, final ResourceLocation vk) {
        return this.bake(elk, this, function, eln, vk, true);
    }
    
    public BakedModel bake(final ModelBakery elk, final BlockModel eax, final Function<Material, TextureAtlasSprite> function, final ModelState eln, final ResourceLocation vk, final boolean boolean6) {
        final TextureAtlasSprite eju8 = (TextureAtlasSprite)function.apply(this.getMaterial("particle"));
        if (this.getRootModel() == ModelBakery.BLOCK_ENTITY_MARKER) {
            return new BuiltInModel(this.getTransforms(), this.getItemOverrides(elk, eax), eju8, this.getGuiLight().lightLikeBlock());
        }
        final SimpleBakedModel.Builder a9 = new SimpleBakedModel.Builder(this, this.getItemOverrides(elk, eax), boolean6).particle(eju8);
        for (final BlockElement eat11 : this.getElements()) {
            for (final Direction gc13 : eat11.faces.keySet()) {
                final BlockElementFace eau14 = (BlockElementFace)eat11.faces.get(gc13);
                final TextureAtlasSprite eju9 = (TextureAtlasSprite)function.apply(this.getMaterial(eau14.texture));
                if (eau14.cullForDirection == null) {
                    a9.addUnculledFace(bakeFace(eat11, eau14, eju9, gc13, eln, vk));
                }
                else {
                    a9.addCulledFace(Direction.rotate(eln.getRotation().getMatrix(), eau14.cullForDirection), bakeFace(eat11, eau14, eju9, gc13, eln, vk));
                }
            }
        }
        return a9.build();
    }
    
    private static BakedQuad bakeFace(final BlockElement eat, final BlockElementFace eau, final TextureAtlasSprite eju, final Direction gc, final ModelState eln, final ResourceLocation vk) {
        return BlockModel.FACE_BAKERY.bakeQuad(eat.from, eat.to, eau, eju, gc, eln, eat.rotation, eat.shade, vk);
    }
    
    public boolean hasTexture(final String string) {
        return !MissingTextureAtlasSprite.getLocation().equals(this.getMaterial(string).texture());
    }
    
    public Material getMaterial(String string) {
        if (isTextureReference(string)) {
            string = string.substring(1);
        }
        final List<String> list3 = (List<String>)Lists.newArrayList();
        while (true) {
            final Either<Material, String> either4 = this.findTextureEntry(string);
            final Optional<Material> optional5 = (Optional<Material>)either4.left();
            if (optional5.isPresent()) {
                return (Material)optional5.get();
            }
            string = (String)either4.right().get();
            if (list3.contains(string)) {
                BlockModel.LOGGER.warn("Unable to resolve texture due to reference chain {}->{} in {}", Joiner.on("->").join((Iterable)list3), string, this.name);
                return new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());
            }
            list3.add(string);
        }
    }
    
    private Either<Material, String> findTextureEntry(final String string) {
        for (BlockModel eax3 = this; eax3 != null; eax3 = eax3.parent) {
            final Either<Material, String> either4 = (Either<Material, String>)eax3.textureMap.get(string);
            if (either4 != null) {
                return either4;
            }
        }
        return (Either<Material, String>)Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()));
    }
    
    private static boolean isTextureReference(final String string) {
        return string.charAt(0) == '#';
    }
    
    public BlockModel getRootModel() {
        return (this.parent == null) ? this : this.parent.getRootModel();
    }
    
    public ItemTransforms getTransforms() {
        final ItemTransform ebd2 = this.getTransform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        final ItemTransform ebd3 = this.getTransform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        final ItemTransform ebd4 = this.getTransform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
        final ItemTransform ebd5 = this.getTransform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
        final ItemTransform ebd6 = this.getTransform(ItemTransforms.TransformType.HEAD);
        final ItemTransform ebd7 = this.getTransform(ItemTransforms.TransformType.GUI);
        final ItemTransform ebd8 = this.getTransform(ItemTransforms.TransformType.GROUND);
        final ItemTransform ebd9 = this.getTransform(ItemTransforms.TransformType.FIXED);
        return new ItemTransforms(ebd2, ebd3, ebd4, ebd5, ebd6, ebd7, ebd8, ebd9);
    }
    
    private ItemTransform getTransform(final ItemTransforms.TransformType b) {
        if (this.parent != null && !this.transforms.hasTransform(b)) {
            return this.parent.getTransform(b);
        }
        return this.transforms.getTransform(b);
    }
    
    public String toString() {
        return this.name;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        FACE_BAKERY = new FaceBakery();
        GSON = new GsonBuilder().registerTypeAdapter((Type)BlockModel.class, new Deserializer()).registerTypeAdapter((Type)BlockElement.class, new BlockElement.Deserializer()).registerTypeAdapter((Type)BlockElementFace.class, new BlockElementFace.Deserializer()).registerTypeAdapter((Type)BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter((Type)ItemTransform.class, new ItemTransform.Deserializer()).registerTypeAdapter((Type)ItemTransforms.class, new ItemTransforms.Deserializer()).registerTypeAdapter((Type)ItemOverride.class, new ItemOverride.Deserializer()).create();
    }
    
    public static class Deserializer implements JsonDeserializer<BlockModel> {
        public BlockModel deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            final List<BlockElement> list6 = this.getElements(jsonDeserializationContext, jsonObject5);
            final String string7 = this.getParentName(jsonObject5);
            final Map<String, Either<Material, String>> map8 = this.getTextureMap(jsonObject5);
            final boolean boolean9 = this.getAmbientOcclusion(jsonObject5);
            ItemTransforms ebe10 = ItemTransforms.NO_TRANSFORMS;
            if (jsonObject5.has("display")) {
                final JsonObject jsonObject6 = GsonHelper.getAsJsonObject(jsonObject5, "display");
                ebe10 = (ItemTransforms)jsonDeserializationContext.deserialize((JsonElement)jsonObject6, (Type)ItemTransforms.class);
            }
            final List<ItemOverride> list7 = this.getOverrides(jsonDeserializationContext, jsonObject5);
            GuiLight b12 = null;
            if (jsonObject5.has("gui_light")) {
                b12 = GuiLight.getByName(GsonHelper.getAsString(jsonObject5, "gui_light"));
            }
            final ResourceLocation vk13 = string7.isEmpty() ? null : new ResourceLocation(string7);
            return new BlockModel(vk13, list6, map8, boolean9, b12, ebe10, list7);
        }
        
        protected List<ItemOverride> getOverrides(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject) {
            final List<ItemOverride> list4 = (List<ItemOverride>)Lists.newArrayList();
            if (jsonObject.has("overrides")) {
                final JsonArray jsonArray5 = GsonHelper.getAsJsonArray(jsonObject, "overrides");
                for (final JsonElement jsonElement7 : jsonArray5) {
                    list4.add(jsonDeserializationContext.deserialize(jsonElement7, (Type)ItemOverride.class));
                }
            }
            return list4;
        }
        
        private Map<String, Either<Material, String>> getTextureMap(final JsonObject jsonObject) {
            final ResourceLocation vk3 = TextureAtlas.LOCATION_BLOCKS;
            final Map<String, Either<Material, String>> map4 = (Map<String, Either<Material, String>>)Maps.newHashMap();
            if (jsonObject.has("textures")) {
                final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "textures");
                for (final Map.Entry<String, JsonElement> entry7 : jsonObject2.entrySet()) {
                    map4.put(entry7.getKey(), parseTextureLocationOrReference(vk3, ((JsonElement)entry7.getValue()).getAsString()));
                }
            }
            return map4;
        }
        
        private static Either<Material, String> parseTextureLocationOrReference(final ResourceLocation vk, final String string) {
            if (isTextureReference(string)) {
                return (Either<Material, String>)Either.right(string.substring(1));
            }
            final ResourceLocation vk2 = ResourceLocation.tryParse(string);
            if (vk2 == null) {
                throw new JsonParseException(string + " is not valid resource location");
            }
            return (Either<Material, String>)Either.left(new Material(vk, vk2));
        }
        
        private String getParentName(final JsonObject jsonObject) {
            return GsonHelper.getAsString(jsonObject, "parent", "");
        }
        
        protected boolean getAmbientOcclusion(final JsonObject jsonObject) {
            return GsonHelper.getAsBoolean(jsonObject, "ambientocclusion", true);
        }
        
        protected List<BlockElement> getElements(final JsonDeserializationContext jsonDeserializationContext, final JsonObject jsonObject) {
            final List<BlockElement> list4 = (List<BlockElement>)Lists.newArrayList();
            if (jsonObject.has("elements")) {
                for (final JsonElement jsonElement6 : GsonHelper.getAsJsonArray(jsonObject, "elements")) {
                    list4.add(jsonDeserializationContext.deserialize(jsonElement6, (Type)BlockElement.class));
                }
            }
            return list4;
        }
    }
    
    public enum GuiLight {
        FRONT("front"), 
        SIDE("side");
        
        private final String name;
        
        private GuiLight(final String string3) {
            this.name = string3;
        }
        
        public static GuiLight getByName(final String string) {
            for (final GuiLight b5 : values()) {
                if (b5.name.equals(string)) {
                    return b5;
                }
            }
            throw new IllegalArgumentException("Invalid gui light: " + string);
        }
        
        public boolean lightLikeBlock() {
            return this == GuiLight.SIDE;
        }
    }
}
