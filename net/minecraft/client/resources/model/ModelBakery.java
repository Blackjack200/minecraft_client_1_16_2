package net.minecraft.client.resources.model;

import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.blockentity.ConduitRenderer;
import net.minecraft.client.renderer.blockentity.BellRenderer;
import java.util.HashSet;
import net.minecraft.client.renderer.block.BlockModelShaper;
import java.io.InputStream;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.server.packs.resources.Resource;
import java.io.Reader;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.StringReader;
import java.io.FileNotFoundException;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableList;
import java.util.Optional;
import java.util.Objects;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.function.Predicate;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import java.io.IOException;
import java.util.function.Consumer;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.util.profiling.ProfilerFiller;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import org.apache.commons.lang3.tuple.Triple;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.color.block.BlockColors;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.AtlasSet;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.client.renderer.block.model.BlockModel;
import com.google.common.base.Splitter;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public class ModelBakery {
    public static final Material FIRE_0;
    public static final Material FIRE_1;
    public static final Material LAVA_FLOW;
    public static final Material WATER_FLOW;
    public static final Material WATER_OVERLAY;
    public static final Material BANNER_BASE;
    public static final Material SHIELD_BASE;
    public static final Material NO_PATTERN_SHIELD;
    public static final List<ResourceLocation> DESTROY_STAGES;
    public static final List<ResourceLocation> BREAKING_LOCATIONS;
    public static final List<RenderType> DESTROY_TYPES;
    private static final Set<Material> UNREFERENCED_TEXTURES;
    private static final Logger LOGGER;
    public static final ModelResourceLocation MISSING_MODEL_LOCATION;
    private static final String MISSING_MODEL_LOCATION_STRING;
    @VisibleForTesting
    public static final String MISSING_MODEL_MESH;
    private static final Map<String, String> BUILTIN_MODELS;
    private static final Splitter COMMA_SPLITTER;
    private static final Splitter EQUAL_SPLITTER;
    public static final BlockModel GENERATION_MARKER;
    public static final BlockModel BLOCK_ENTITY_MARKER;
    private static final StateDefinition<Block, BlockState> ITEM_FRAME_FAKE_DEFINITION;
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR;
    private static final Map<ResourceLocation, StateDefinition<Block, BlockState>> STATIC_DEFINITIONS;
    private final ResourceManager resourceManager;
    @Nullable
    private AtlasSet atlasSet;
    private final BlockColors blockColors;
    private final Set<ResourceLocation> loadingStack;
    private final BlockModelDefinition.Context context;
    private final Map<ResourceLocation, UnbakedModel> unbakedCache;
    private final Map<Triple<ResourceLocation, Transformation, Boolean>, BakedModel> bakedCache;
    private final Map<ResourceLocation, UnbakedModel> topLevelModels;
    private final Map<ResourceLocation, BakedModel> bakedTopLevelModels;
    private final Map<ResourceLocation, Pair<TextureAtlas, TextureAtlas.Preparations>> atlasPreparations;
    private int nextModelGroup;
    private final Object2IntMap<BlockState> modelGroups;
    
    public ModelBakery(final ResourceManager acf, final BlockColors dkl, final ProfilerFiller ant, final int integer) {
        this.loadingStack = (Set<ResourceLocation>)Sets.newHashSet();
        this.context = new BlockModelDefinition.Context();
        this.unbakedCache = (Map<ResourceLocation, UnbakedModel>)Maps.newHashMap();
        this.bakedCache = (Map<Triple<ResourceLocation, Transformation, Boolean>, BakedModel>)Maps.newHashMap();
        this.topLevelModels = (Map<ResourceLocation, UnbakedModel>)Maps.newHashMap();
        this.bakedTopLevelModels = (Map<ResourceLocation, BakedModel>)Maps.newHashMap();
        this.nextModelGroup = 1;
        this.modelGroups = Util.make((Object2IntMap)new Object2IntOpenHashMap(), (java.util.function.Consumer<Object2IntMap>)(object2IntOpenHashMap -> object2IntOpenHashMap.defaultReturnValue(-1)));
        this.resourceManager = acf;
        this.blockColors = dkl;
        ant.push("missing_model");
        try {
            this.unbakedCache.put(ModelBakery.MISSING_MODEL_LOCATION, this.loadBlockModel(ModelBakery.MISSING_MODEL_LOCATION));
            this.loadTopLevel(ModelBakery.MISSING_MODEL_LOCATION);
        }
        catch (IOException iOException6) {
            ModelBakery.LOGGER.error("Error loading missing model, should never happen :(", (Throwable)iOException6);
            throw new RuntimeException((Throwable)iOException6);
        }
        ant.popPush("static_definitions");
        ModelBakery.STATIC_DEFINITIONS.forEach((vk, cef) -> cef.getPossibleStates().forEach(cee -> this.loadTopLevel(BlockModelShaper.stateToModelLocation(vk, cee))));
        ant.popPush("blocks");
        for (final Block bul7 : Registry.BLOCK) {
            bul7.getStateDefinition().getPossibleStates().forEach(cee -> this.loadTopLevel(BlockModelShaper.stateToModelLocation(cee)));
        }
        ant.popPush("items");
        for (final ResourceLocation vk7 : Registry.ITEM.keySet()) {
            this.loadTopLevel(new ModelResourceLocation(vk7, "inventory"));
        }
        ant.popPush("special");
        this.loadTopLevel(new ModelResourceLocation("minecraft:trident_in_hand#inventory"));
        ant.popPush("textures");
        final Set<Pair<String, String>> set6 = (Set<Pair<String, String>>)Sets.newLinkedHashSet();
        final Set<Material> set7 = (Set<Material>)this.topLevelModels.values().stream().flatMap(elq -> elq.getMaterials((Function<ResourceLocation, UnbakedModel>)this::getModel, set6).stream()).collect(Collectors.toSet());
        set7.addAll((Collection)ModelBakery.UNREFERENCED_TEXTURES);
        set6.stream().filter(pair -> !((String)pair.getSecond()).equals(ModelBakery.MISSING_MODEL_LOCATION_STRING)).forEach(pair -> ModelBakery.LOGGER.warn("Unable to resolve texture reference: {} in {}", pair.getFirst(), pair.getSecond()));
        final Map<ResourceLocation, List<Material>> map8 = (Map<ResourceLocation, List<Material>>)set7.stream().collect(Collectors.groupingBy(Material::atlasLocation));
        ant.popPush("stitching");
        this.atlasPreparations = (Map<ResourceLocation, Pair<TextureAtlas, TextureAtlas.Preparations>>)Maps.newHashMap();
        for (final Map.Entry<ResourceLocation, List<Material>> entry10 : map8.entrySet()) {
            final TextureAtlas ejt11 = new TextureAtlas((ResourceLocation)entry10.getKey());
            final TextureAtlas.Preparations a12 = ejt11.prepareToStitch(this.resourceManager, (Stream<ResourceLocation>)((List)entry10.getValue()).stream().map(Material::texture), ant, integer);
            this.atlasPreparations.put(entry10.getKey(), Pair.of((Object)ejt11, (Object)a12));
        }
        ant.pop();
    }
    
    public AtlasSet uploadTextures(final TextureManager ejv, final ProfilerFiller ant) {
        ant.push("atlas");
        for (final Pair<TextureAtlas, TextureAtlas.Preparations> pair5 : this.atlasPreparations.values()) {
            final TextureAtlas ejt6 = (TextureAtlas)pair5.getFirst();
            final TextureAtlas.Preparations a7 = (TextureAtlas.Preparations)pair5.getSecond();
            ejt6.reload(a7);
            ejv.register(ejt6.location(), ejt6);
            ejv.bind(ejt6.location());
            ejt6.updateFilter(a7);
        }
        this.atlasSet = new AtlasSet((Collection<TextureAtlas>)this.atlasPreparations.values().stream().map(Pair::getFirst).collect(Collectors.toList()));
        ant.popPush("baking");
        this.topLevelModels.keySet().forEach(vk -> {
            BakedModel elg3 = null;
            try {
                elg3 = this.bake(vk, BlockModelRotation.X0_Y0);
            }
            catch (Exception exception4) {
                ModelBakery.LOGGER.warn("Unable to bake model: '{}': {}", vk, exception4);
            }
            if (elg3 != null) {
                this.bakedTopLevelModels.put(vk, elg3);
            }
        });
        ant.pop();
        return this.atlasSet;
    }
    
    private static Predicate<BlockState> predicate(final StateDefinition<Block, BlockState> cef, final String string) {
        final Map<Property<?>, Comparable<?>> map3 = (Map<Property<?>, Comparable<?>>)Maps.newHashMap();
        for (final String string2 : ModelBakery.COMMA_SPLITTER.split((CharSequence)string)) {
            final Iterator<String> iterator6 = (Iterator<String>)ModelBakery.EQUAL_SPLITTER.split((CharSequence)string2).iterator();
            if (iterator6.hasNext()) {
                final String string3 = (String)iterator6.next();
                final Property<?> cfg8 = cef.getProperty(string3);
                if (cfg8 != null && iterator6.hasNext()) {
                    final String string4 = (String)iterator6.next();
                    final Comparable<?> comparable10 = ModelBakery.<Comparable<?>>getValueHelper(cfg8, string4);
                    if (comparable10 == null) {
                        throw new RuntimeException("Unknown value: '" + string4 + "' for blockstate property: '" + string3 + "' " + cfg8.getPossibleValues());
                    }
                    map3.put(cfg8, comparable10);
                }
                else {
                    if (!string3.isEmpty()) {
                        throw new RuntimeException("Unknown blockstate property: '" + string3 + "'");
                    }
                    continue;
                }
            }
        }
        final Block bul4 = cef.getOwner();
        return (Predicate<BlockState>)(cee -> {
            if (cee == null || bul4 != cee.getBlock()) {
                return false;
            }
            for (final Map.Entry<Property<?>, Comparable<?>> entry5 : map3.entrySet()) {
                if (!Objects.equals(cee.getValue((Property<Object>)entry5.getKey()), entry5.getValue())) {
                    return false;
                }
            }
            return true;
        });
    }
    
    @Nullable
    static <T extends Comparable<T>> T getValueHelper(final Property<T> cfg, final String string) {
        return (T)cfg.getValue(string).orElse(null);
    }
    
    public UnbakedModel getModel(final ResourceLocation vk) {
        if (this.unbakedCache.containsKey(vk)) {
            return (UnbakedModel)this.unbakedCache.get(vk);
        }
        if (this.loadingStack.contains(vk)) {
            throw new IllegalStateException(new StringBuilder().append("Circular reference while loading ").append(vk).toString());
        }
        this.loadingStack.add(vk);
        final UnbakedModel elq3 = (UnbakedModel)this.unbakedCache.get(ModelBakery.MISSING_MODEL_LOCATION);
        while (!this.loadingStack.isEmpty()) {
            final ResourceLocation vk2 = (ResourceLocation)this.loadingStack.iterator().next();
            try {
                if (!this.unbakedCache.containsKey(vk2)) {
                    this.loadModel(vk2);
                }
            }
            catch (BlockStateDefinitionException a5) {
                ModelBakery.LOGGER.warn(a5.getMessage());
                this.unbakedCache.put(vk2, elq3);
            }
            catch (Exception exception5) {
                ModelBakery.LOGGER.warn("Unable to load model: '{}' referenced from: {}: {}", vk2, vk, exception5);
                this.unbakedCache.put(vk2, elq3);
            }
            finally {
                this.loadingStack.remove(vk2);
            }
        }
        return (UnbakedModel)this.unbakedCache.getOrDefault(vk, elq3);
    }
    
    private void loadModel(final ResourceLocation vk) throws Exception {
        if (!(vk instanceof ModelResourceLocation)) {
            this.cacheAndQueueDependencies(vk, this.loadBlockModel(vk));
            return;
        }
        final ModelResourceLocation elm3 = (ModelResourceLocation)vk;
        if (Objects.equals(elm3.getVariant(), "inventory")) {
            final ResourceLocation vk2 = new ResourceLocation(vk.getNamespace(), "item/" + vk.getPath());
            final BlockModel eax5 = this.loadBlockModel(vk2);
            this.cacheAndQueueDependencies(elm3, eax5);
            this.unbakedCache.put(vk2, eax5);
        }
        else {
            final ResourceLocation vk2 = new ResourceLocation(vk.getNamespace(), vk.getPath());
            final StateDefinition<Block, BlockState> cef5 = (StateDefinition<Block, BlockState>)Optional.ofNullable(ModelBakery.STATIC_DEFINITIONS.get(vk2)).orElseGet(() -> Registry.BLOCK.get(vk2).getStateDefinition());
            this.context.setDefinition(cef5);
            final List<Property<?>> list6 = (List<Property<?>>)ImmutableList.copyOf((Collection)this.blockColors.getColoringProperties(cef5.getOwner()));
            final ImmutableList<BlockState> immutableList7 = cef5.getPossibleStates();
            final Map<ModelResourceLocation, BlockState> map8 = (Map<ModelResourceLocation, BlockState>)Maps.newHashMap();
            immutableList7.forEach(cee -> {
                final BlockState blockState = (BlockState)map8.put(BlockModelShaper.stateToModelLocation(vk2, cee), cee);
            });
            final Map<BlockState, Pair<UnbakedModel, Supplier<ModelGroupKey>>> map9 = (Map<BlockState, Pair<UnbakedModel, Supplier<ModelGroupKey>>>)Maps.newHashMap();
            final ResourceLocation vk3 = new ResourceLocation(vk.getNamespace(), "blockstates/" + vk.getPath() + ".json");
            final UnbakedModel elq11 = (UnbakedModel)this.unbakedCache.get(ModelBakery.MISSING_MODEL_LOCATION);
            final ModelGroupKey b12 = new ModelGroupKey((List<UnbakedModel>)ImmutableList.of(elq11), (List<Object>)ImmutableList.of());
            final Pair<UnbakedModel, Supplier<ModelGroupKey>> pair13 = (Pair<UnbakedModel, Supplier<ModelGroupKey>>)Pair.of(elq11, (() -> b12));
            try {
                List<Pair<String, BlockModelDefinition>> list7;
                try {
                    list7 = (List<Pair<String, BlockModelDefinition>>)this.resourceManager.getResources(vk3).stream().map(ace -> {
                        try (final InputStream inputStream3 = ace.getInputStream()) {
                            return Pair.of(ace.getSourceName(), BlockModelDefinition.fromStream(this.context, (Reader)new InputStreamReader(inputStream3, StandardCharsets.UTF_8)));
                        }
                        catch (Exception exception3) {
                            throw new BlockStateDefinitionException(String.format("Exception loading blockstate definition: '%s' in resourcepack: '%s': %s", new Object[] { ace.getLocation(), ace.getSourceName(), exception3.getMessage() }));
                        }
                    }).collect(Collectors.toList());
                }
                catch (IOException iOException15) {
                    ModelBakery.LOGGER.warn("Exception loading blockstate definition: {}: {}", vk3, iOException15);
                    return;
                }
                for (final Pair<String, BlockModelDefinition> pair14 : list7) {
                    final BlockModelDefinition eay17 = (BlockModelDefinition)pair14.getSecond();
                    final Map<BlockState, Pair<UnbakedModel, Supplier<ModelGroupKey>>> map10 = (Map<BlockState, Pair<UnbakedModel, Supplier<ModelGroupKey>>>)Maps.newIdentityHashMap();
                    MultiPart ebk19;
                    if (eay17.isMultiPart()) {
                        ebk19 = eay17.getMultiPart();
                        immutableList7.forEach(cee -> {
                            final Pair pair = (Pair)map10.put(cee, Pair.of((Object)ebk19, (Object)(() -> ModelGroupKey.create(cee, ebk19, (Collection<Property<?>>)list6))));
                        });
                    }
                    else {
                        ebk19 = null;
                    }
                    eay17.getVariants().forEach((string, ebf) -> {
                        try {
                            immutableList7.stream().filter((Predicate)predicate(cef5, string)).forEach(cee -> {
                                final Pair<UnbakedModel, Supplier<ModelGroupKey>> pair2 = (Pair<UnbakedModel, Supplier<ModelGroupKey>>)map10.put(cee, Pair.of((Object)ebf, (Object)(() -> ModelGroupKey.create(cee, ebf, (Collection<Property<?>>)list6))));
                                if (pair2 != null && pair2.getFirst() != ebk19) {
                                    map10.put(cee, pair13);
                                    throw new RuntimeException("Overlapping definition with: " + (String)((Map.Entry)eay17.getVariants().entrySet().stream().filter(entry -> entry.getValue() == pair2.getFirst()).findFirst().get()).getKey());
                                }
                            });
                        }
                        catch (Exception exception12) {
                            ModelBakery.LOGGER.warn("Exception loading blockstate definition: '{}' in resourcepack: '{}' for variant: '{}': {}", vk3, pair14.getFirst(), string, exception12.getMessage());
                        }
                    });
                    map9.putAll((Map)map10);
                }
            }
            catch (BlockStateDefinitionException a14) {
                throw a14;
            }
            catch (Exception exception14) {
                throw new BlockStateDefinitionException(String.format("Exception loading blockstate definition: '%s': %s", new Object[] { vk3, exception14 }));
            }
            finally {
                final Map<ModelGroupKey, Set<BlockState>> map11 = (Map<ModelGroupKey, Set<BlockState>>)Maps.newHashMap();
                map8.forEach((elm, cee) -> {
                    Pair<UnbakedModel, Supplier<ModelGroupKey>> pair2 = (Pair<UnbakedModel, Supplier<ModelGroupKey>>)map9.get(cee);
                    if (pair2 == null) {
                        ModelBakery.LOGGER.warn("Exception loading blockstate definition: '{}' missing model for variant: '{}'", vk3, elm);
                        pair2 = pair13;
                    }
                    this.cacheAndQueueDependencies(elm, (UnbakedModel)pair2.getFirst());
                    try {
                        final ModelGroupKey b9 = (ModelGroupKey)((Supplier)pair2.getSecond()).get();
                        ((Set)map11.computeIfAbsent(b9, b -> Sets.newIdentityHashSet())).add(cee);
                    }
                    catch (Exception exception9) {
                        ModelBakery.LOGGER.warn("Exception evaluating model definition: '{}'", elm, exception9);
                    }
                });
                map11.forEach((b, set) -> {
                    final Iterator<BlockState> iterator4 = (Iterator<BlockState>)set.iterator();
                    while (iterator4.hasNext()) {
                        final BlockState cee5 = (BlockState)iterator4.next();
                        if (cee5.getRenderShape() != RenderShape.MODEL) {
                            iterator4.remove();
                            this.modelGroups.put(cee5, 0);
                        }
                    }
                    if (set.size() > 1) {
                        this.registerModelGroup((Iterable<BlockState>)set);
                    }
                });
            }
        }
    }
    
    private void cacheAndQueueDependencies(final ResourceLocation vk, final UnbakedModel elq) {
        this.unbakedCache.put(vk, elq);
        this.loadingStack.addAll((Collection)elq.getDependencies());
    }
    
    private void loadTopLevel(final ModelResourceLocation elm) {
        final UnbakedModel elq3 = this.getModel(elm);
        this.unbakedCache.put(elm, elq3);
        this.topLevelModels.put(elm, elq3);
    }
    
    private void registerModelGroup(final Iterable<BlockState> iterable) {
        final int integer3 = this.nextModelGroup++;
        iterable.forEach(cee -> this.modelGroups.put(cee, integer3));
    }
    
    @Nullable
    public BakedModel bake(final ResourceLocation vk, final ModelState eln) {
        final Triple<ResourceLocation, Transformation, Boolean> triple4 = (Triple<ResourceLocation, Transformation, Boolean>)Triple.of(vk, eln.getRotation(), eln.isUvLocked());
        if (this.bakedCache.containsKey(triple4)) {
            return (BakedModel)this.bakedCache.get(triple4);
        }
        if (this.atlasSet == null) {
            throw new IllegalStateException("bake called too early");
        }
        final UnbakedModel elq5 = this.getModel(vk);
        if (elq5 instanceof BlockModel) {
            final BlockModel eax6 = (BlockModel)elq5;
            if (eax6.getRootModel() == ModelBakery.GENERATION_MARKER) {
                return ModelBakery.ITEM_MODEL_GENERATOR.generateBlockModel((Function<Material, TextureAtlasSprite>)this.atlasSet::getSprite, eax6).bake(this, eax6, (Function<Material, TextureAtlasSprite>)this.atlasSet::getSprite, eln, vk, false);
            }
        }
        final BakedModel elg6 = elq5.bake(this, (Function<Material, TextureAtlasSprite>)this.atlasSet::getSprite, eln, vk);
        this.bakedCache.put(triple4, elg6);
        return elg6;
    }
    
    private BlockModel loadBlockModel(final ResourceLocation vk) throws IOException {
        Reader reader3 = null;
        Resource ace4 = null;
        try {
            final String string5 = vk.getPath();
            if ("builtin/generated".equals(string5)) {
                return ModelBakery.GENERATION_MARKER;
            }
            if ("builtin/entity".equals(string5)) {
                return ModelBakery.BLOCK_ENTITY_MARKER;
            }
            if (string5.startsWith("builtin/")) {
                final String string6 = string5.substring("builtin/".length());
                final String string7 = (String)ModelBakery.BUILTIN_MODELS.get(string6);
                if (string7 == null) {
                    throw new FileNotFoundException(vk.toString());
                }
                reader3 = (Reader)new StringReader(string7);
            }
            else {
                ace4 = this.resourceManager.getResource(new ResourceLocation(vk.getNamespace(), "models/" + vk.getPath() + ".json"));
                reader3 = (Reader)new InputStreamReader(ace4.getInputStream(), StandardCharsets.UTF_8);
            }
            final BlockModel eax6 = BlockModel.fromStream(reader3);
            eax6.name = vk.toString();
            return eax6;
        }
        finally {
            IOUtils.closeQuietly(reader3);
            IOUtils.closeQuietly((Closeable)ace4);
        }
    }
    
    public Map<ResourceLocation, BakedModel> getBakedTopLevelModels() {
        return this.bakedTopLevelModels;
    }
    
    public Object2IntMap<BlockState> getModelGroups() {
        return this.modelGroups;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //     7: new             Lnet/minecraft/resources/ResourceLocation;
        //    10: dup            
        //    11: ldc_w           "block/fire_0"
        //    14: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    17: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //    20: putstatic       net/minecraft/client/resources/model/ModelBakery.FIRE_0:Lnet/minecraft/client/resources/model/Material;
        //    23: new             Lnet/minecraft/client/resources/model/Material;
        //    26: dup            
        //    27: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //    30: new             Lnet/minecraft/resources/ResourceLocation;
        //    33: dup            
        //    34: ldc_w           "block/fire_1"
        //    37: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    40: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //    43: putstatic       net/minecraft/client/resources/model/ModelBakery.FIRE_1:Lnet/minecraft/client/resources/model/Material;
        //    46: new             Lnet/minecraft/client/resources/model/Material;
        //    49: dup            
        //    50: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //    53: new             Lnet/minecraft/resources/ResourceLocation;
        //    56: dup            
        //    57: ldc_w           "block/lava_flow"
        //    60: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    63: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //    66: putstatic       net/minecraft/client/resources/model/ModelBakery.LAVA_FLOW:Lnet/minecraft/client/resources/model/Material;
        //    69: new             Lnet/minecraft/client/resources/model/Material;
        //    72: dup            
        //    73: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //    76: new             Lnet/minecraft/resources/ResourceLocation;
        //    79: dup            
        //    80: ldc_w           "block/water_flow"
        //    83: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    86: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //    89: putstatic       net/minecraft/client/resources/model/ModelBakery.WATER_FLOW:Lnet/minecraft/client/resources/model/Material;
        //    92: new             Lnet/minecraft/client/resources/model/Material;
        //    95: dup            
        //    96: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //    99: new             Lnet/minecraft/resources/ResourceLocation;
        //   102: dup            
        //   103: ldc_w           "block/water_overlay"
        //   106: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   109: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //   112: putstatic       net/minecraft/client/resources/model/ModelBakery.WATER_OVERLAY:Lnet/minecraft/client/resources/model/Material;
        //   115: new             Lnet/minecraft/client/resources/model/Material;
        //   118: dup            
        //   119: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //   122: new             Lnet/minecraft/resources/ResourceLocation;
        //   125: dup            
        //   126: ldc_w           "entity/banner_base"
        //   129: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   132: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //   135: putstatic       net/minecraft/client/resources/model/ModelBakery.BANNER_BASE:Lnet/minecraft/client/resources/model/Material;
        //   138: new             Lnet/minecraft/client/resources/model/Material;
        //   141: dup            
        //   142: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //   145: new             Lnet/minecraft/resources/ResourceLocation;
        //   148: dup            
        //   149: ldc_w           "entity/shield_base"
        //   152: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   155: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //   158: putstatic       net/minecraft/client/resources/model/ModelBakery.SHIELD_BASE:Lnet/minecraft/client/resources/model/Material;
        //   161: new             Lnet/minecraft/client/resources/model/Material;
        //   164: dup            
        //   165: getstatic       net/minecraft/client/renderer/texture/TextureAtlas.LOCATION_BLOCKS:Lnet/minecraft/resources/ResourceLocation;
        //   168: new             Lnet/minecraft/resources/ResourceLocation;
        //   171: dup            
        //   172: ldc_w           "entity/shield_base_nopattern"
        //   175: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   178: invokespecial   net/minecraft/client/resources/model/Material.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;)V
        //   181: putstatic       net/minecraft/client/resources/model/ModelBakery.NO_PATTERN_SHIELD:Lnet/minecraft/client/resources/model/Material;
        //   184: iconst_0       
        //   185: bipush          10
        //   187: invokestatic    java/util/stream/IntStream.range:(II)Ljava/util/stream/IntStream;
        //   190: invokedynamic   BootstrapMethod #29, apply:()Ljava/util/function/IntFunction;
        //   195: invokeinterface java/util/stream/IntStream.mapToObj:(Ljava/util/function/IntFunction;)Ljava/util/stream/Stream;
        //   200: invokestatic    java/util/stream/Collectors.toList:()Ljava/util/stream/Collector;
        //   203: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
        //   208: checkcast       Ljava/util/List;
        //   211: putstatic       net/minecraft/client/resources/model/ModelBakery.DESTROY_STAGES:Ljava/util/List;
        //   214: getstatic       net/minecraft/client/resources/model/ModelBakery.DESTROY_STAGES:Ljava/util/List;
        //   217: invokeinterface java/util/List.stream:()Ljava/util/stream/Stream;
        //   222: invokedynamic   BootstrapMethod #30, apply:()Ljava/util/function/Function;
        //   227: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
        //   232: invokestatic    java/util/stream/Collectors.toList:()Ljava/util/stream/Collector;
        //   235: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
        //   240: checkcast       Ljava/util/List;
        //   243: putstatic       net/minecraft/client/resources/model/ModelBakery.BREAKING_LOCATIONS:Ljava/util/List;
        //   246: getstatic       net/minecraft/client/resources/model/ModelBakery.BREAKING_LOCATIONS:Ljava/util/List;
        //   249: invokeinterface java/util/List.stream:()Ljava/util/stream/Stream;
        //   254: invokedynamic   BootstrapMethod #31, apply:()Ljava/util/function/Function;
        //   259: invokeinterface java/util/stream/Stream.map:(Ljava/util/function/Function;)Ljava/util/stream/Stream;
        //   264: invokestatic    java/util/stream/Collectors.toList:()Ljava/util/stream/Collector;
        //   267: invokeinterface java/util/stream/Stream.collect:(Ljava/util/stream/Collector;)Ljava/lang/Object;
        //   272: checkcast       Ljava/util/List;
        //   275: putstatic       net/minecraft/client/resources/model/ModelBakery.DESTROY_TYPES:Ljava/util/List;
        //   278: invokestatic    com/google/common/collect/Sets.newHashSet:()Ljava/util/HashSet;
        //   281: invokedynamic   BootstrapMethod #32, accept:()Ljava/util/function/Consumer;
        //   286: invokestatic    net/minecraft/Util.make:(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
        //   289: checkcast       Ljava/util/Set;
        //   292: putstatic       net/minecraft/client/resources/model/ModelBakery.UNREFERENCED_TEXTURES:Ljava/util/Set;
        //   295: invokestatic    org/apache/logging/log4j/LogManager.getLogger:()Lorg/apache/logging/log4j/Logger;
        //   298: putstatic       net/minecraft/client/resources/model/ModelBakery.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   301: new             Lnet/minecraft/client/resources/model/ModelResourceLocation;
        //   304: dup            
        //   305: ldc_w           "builtin/missing"
        //   308: ldc_w           "missing"
        //   311: invokespecial   net/minecraft/client/resources/model/ModelResourceLocation.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   314: putstatic       net/minecraft/client/resources/model/ModelBakery.MISSING_MODEL_LOCATION:Lnet/minecraft/client/resources/model/ModelResourceLocation;
        //   317: getstatic       net/minecraft/client/resources/model/ModelBakery.MISSING_MODEL_LOCATION:Lnet/minecraft/client/resources/model/ModelResourceLocation;
        //   320: invokevirtual   net/minecraft/client/resources/model/ModelResourceLocation.toString:()Ljava/lang/String;
        //   323: putstatic       net/minecraft/client/resources/model/ModelBakery.MISSING_MODEL_LOCATION_STRING:Ljava/lang/String;
        //   326: new             Ljava/lang/StringBuilder;
        //   329: dup            
        //   330: invokespecial   java/lang/StringBuilder.<init>:()V
        //   333: ldc_w           "{    'textures': {       'particle': '"
        //   336: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   339: invokestatic    net/minecraft/client/renderer/texture/MissingTextureAtlasSprite.getLocation:()Lnet/minecraft/resources/ResourceLocation;
        //   342: invokevirtual   net/minecraft/resources/ResourceLocation.getPath:()Ljava/lang/String;
        //   345: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   348: ldc_w           "',       'missingno': '"
        //   351: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   354: invokestatic    net/minecraft/client/renderer/texture/MissingTextureAtlasSprite.getLocation:()Lnet/minecraft/resources/ResourceLocation;
        //   357: invokevirtual   net/minecraft/resources/ResourceLocation.getPath:()Ljava/lang/String;
        //   360: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   363: ldc_w           "'    },    'elements': [         {  'from': [ 0, 0, 0 ],            'to': [ 16, 16, 16 ],            'faces': {                'down':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'down',  'texture': '#missingno' },                'up':    { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'up',    'texture': '#missingno' },                'north': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'north', 'texture': '#missingno' },                'south': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'south', 'texture': '#missingno' },                'west':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'west',  'texture': '#missingno' },                'east':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'east',  'texture': '#missingno' }            }        }    ]}"
        //   366: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   369: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   372: bipush          39
        //   374: bipush          34
        //   376: invokevirtual   java/lang/String.replace:(CC)Ljava/lang/String;
        //   379: putstatic       net/minecraft/client/resources/model/ModelBakery.MISSING_MODEL_MESH:Ljava/lang/String;
        //   382: ldc_w           "missing"
        //   385: getstatic       net/minecraft/client/resources/model/ModelBakery.MISSING_MODEL_MESH:Ljava/lang/String;
        //   388: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
        //   391: invokestatic    com/google/common/collect/Maps.newHashMap:(Ljava/util/Map;)Ljava/util/HashMap;
        //   394: putstatic       net/minecraft/client/resources/model/ModelBakery.BUILTIN_MODELS:Ljava/util/Map;
        //   397: bipush          44
        //   399: invokestatic    com/google/common/base/Splitter.on:(C)Lcom/google/common/base/Splitter;
        //   402: putstatic       net/minecraft/client/resources/model/ModelBakery.COMMA_SPLITTER:Lcom/google/common/base/Splitter;
        //   405: bipush          61
        //   407: invokestatic    com/google/common/base/Splitter.on:(C)Lcom/google/common/base/Splitter;
        //   410: iconst_2       
        //   411: invokevirtual   com/google/common/base/Splitter.limit:(I)Lcom/google/common/base/Splitter;
        //   414: putstatic       net/minecraft/client/resources/model/ModelBakery.EQUAL_SPLITTER:Lcom/google/common/base/Splitter;
        //   417: ldc_w           "{\"gui_light\": \"front\"}"
        //   420: invokestatic    net/minecraft/client/renderer/block/model/BlockModel.fromString:(Ljava/lang/String;)Lnet/minecraft/client/renderer/block/model/BlockModel;
        //   423: invokedynamic   BootstrapMethod #33, accept:()Ljava/util/function/Consumer;
        //   428: invokestatic    net/minecraft/Util.make:(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
        //   431: checkcast       Lnet/minecraft/client/renderer/block/model/BlockModel;
        //   434: putstatic       net/minecraft/client/resources/model/ModelBakery.GENERATION_MARKER:Lnet/minecraft/client/renderer/block/model/BlockModel;
        //   437: ldc_w           "{\"gui_light\": \"side\"}"
        //   440: invokestatic    net/minecraft/client/renderer/block/model/BlockModel.fromString:(Ljava/lang/String;)Lnet/minecraft/client/renderer/block/model/BlockModel;
        //   443: invokedynamic   BootstrapMethod #34, accept:()Ljava/util/function/Consumer;
        //   448: invokestatic    net/minecraft/Util.make:(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
        //   451: checkcast       Lnet/minecraft/client/renderer/block/model/BlockModel;
        //   454: putstatic       net/minecraft/client/resources/model/ModelBakery.BLOCK_ENTITY_MARKER:Lnet/minecraft/client/renderer/block/model/BlockModel;
        //   457: new             Lnet/minecraft/world/level/block/state/StateDefinition$Builder;
        //   460: dup            
        //   461: getstatic       net/minecraft/world/level/block/Blocks.AIR:Lnet/minecraft/world/level/block/Block;
        //   464: invokespecial   net/minecraft/world/level/block/state/StateDefinition$Builder.<init>:(Ljava/lang/Object;)V
        //   467: iconst_1       
        //   468: anewarray       Lnet/minecraft/world/level/block/state/properties/Property;
        //   471: dup            
        //   472: iconst_0       
        //   473: ldc_w           "map"
        //   476: invokestatic    net/minecraft/world/level/block/state/properties/BooleanProperty.create:(Ljava/lang/String;)Lnet/minecraft/world/level/block/state/properties/BooleanProperty;
        //   479: aastore        
        //   480: invokevirtual   net/minecraft/world/level/block/state/StateDefinition$Builder.add:([Lnet/minecraft/world/level/block/state/properties/Property;)Lnet/minecraft/world/level/block/state/StateDefinition$Builder;
        //   483: invokedynamic   BootstrapMethod #35, apply:()Ljava/util/function/Function;
        //   488: invokedynamic   BootstrapMethod #36, create:()Lnet/minecraft/world/level/block/state/StateDefinition$Factory;
        //   493: invokevirtual   net/minecraft/world/level/block/state/StateDefinition$Builder.create:(Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/StateDefinition$Factory;)Lnet/minecraft/world/level/block/state/StateDefinition;
        //   496: putstatic       net/minecraft/client/resources/model/ModelBakery.ITEM_FRAME_FAKE_DEFINITION:Lnet/minecraft/world/level/block/state/StateDefinition;
        //   499: new             Lnet/minecraft/client/renderer/block/model/ItemModelGenerator;
        //   502: dup            
        //   503: invokespecial   net/minecraft/client/renderer/block/model/ItemModelGenerator.<init>:()V
        //   506: putstatic       net/minecraft/client/resources/model/ModelBakery.ITEM_MODEL_GENERATOR:Lnet/minecraft/client/renderer/block/model/ItemModelGenerator;
        //   509: new             Lnet/minecraft/resources/ResourceLocation;
        //   512: dup            
        //   513: ldc_w           "item_frame"
        //   516: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   519: getstatic       net/minecraft/client/resources/model/ModelBakery.ITEM_FRAME_FAKE_DEFINITION:Lnet/minecraft/world/level/block/state/StateDefinition;
        //   522: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
        //   525: putstatic       net/minecraft/client/resources/model/ModelBakery.STATIC_DEFINITIONS:Ljava/util/Map;
        //   528: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.MetadataHelper.substituteGenericArguments(MetadataHelper.java:1100)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2676)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.pollAndExecCC(ForkJoinPool.java:1190)
        //     at java.util.concurrent.ForkJoinPool.helpComplete(ForkJoinPool.java:1879)
        //     at java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:2467)
        //     at java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:324)
        //     at java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:405)
        //     at java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:734)
        //     at java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:160)
        //     at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:174)
        //     at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.lang.Thread.run(Thread.java:748)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static class BlockStateDefinitionException extends RuntimeException {
        public BlockStateDefinitionException(final String string) {
            super(string);
        }
    }
    
    static class ModelGroupKey {
        private final List<UnbakedModel> models;
        private final List<Object> coloringValues;
        
        public ModelGroupKey(final List<UnbakedModel> list1, final List<Object> list2) {
            this.models = list1;
            this.coloringValues = list2;
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof ModelGroupKey) {
                final ModelGroupKey b3 = (ModelGroupKey)object;
                return Objects.equals(this.models, b3.models) && Objects.equals(this.coloringValues, b3.coloringValues);
            }
            return false;
        }
        
        public int hashCode() {
            return 31 * this.models.hashCode() + this.coloringValues.hashCode();
        }
        
        public static ModelGroupKey create(final BlockState cee, final MultiPart ebk, final Collection<Property<?>> collection) {
            final StateDefinition<Block, BlockState> cef4 = cee.getBlock().getStateDefinition();
            final List<UnbakedModel> list5 = (List<UnbakedModel>)ebk.getSelectors().stream().filter(ebm -> ebm.getPredicate(cef4).test(cee)).map(Selector::getVariant).collect(ImmutableList.toImmutableList());
            final List<Object> list6 = getColoringValues(cee, collection);
            return new ModelGroupKey(list5, list6);
        }
        
        public static ModelGroupKey create(final BlockState cee, final UnbakedModel elq, final Collection<Property<?>> collection) {
            final List<Object> list4 = getColoringValues(cee, collection);
            return new ModelGroupKey((List<UnbakedModel>)ImmutableList.of(elq), list4);
        }
        
        private static List<Object> getColoringValues(final BlockState cee, final Collection<Property<?>> collection) {
            return (List<Object>)collection.stream().map(cee::getValue).collect(ImmutableList.toImmutableList());
        }
    }
}
