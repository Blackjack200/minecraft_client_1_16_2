package net.minecraft.core;

import net.minecraft.data.BuiltinRegistries;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.apache.commons.lang3.Validate;
import net.minecraft.SharedConstants;
import java.util.stream.StreamSupport;
import java.util.Set;
import java.util.Optional;
import javax.annotation.Nullable;
import java.util.stream.Stream;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceKey;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Codec;

public abstract class Registry<T> implements Codec<T>, Keyable, IdMap<T> {
    protected static final Logger LOGGER;
    private static final Map<ResourceLocation, Supplier<?>> LOADERS;
    public static final ResourceLocation ROOT_REGISTRY_NAME;
    protected static final WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY;
    public static final Registry<? extends Registry<?>> REGISTRY;
    public static final ResourceKey<Registry<SoundEvent>> SOUND_EVENT_REGISTRY;
    public static final ResourceKey<Registry<Fluid>> FLUID_REGISTRY;
    public static final ResourceKey<Registry<MobEffect>> MOB_EFFECT_REGISTRY;
    public static final ResourceKey<Registry<Block>> BLOCK_REGISTRY;
    public static final ResourceKey<Registry<Enchantment>> ENCHANTMENT_REGISTRY;
    public static final ResourceKey<Registry<EntityType<?>>> ENTITY_TYPE_REGISTRY;
    public static final ResourceKey<Registry<Item>> ITEM_REGISTRY;
    public static final ResourceKey<Registry<Potion>> POTION_REGISTRY;
    public static final ResourceKey<Registry<ParticleType<?>>> PARTICLE_TYPE_REGISTRY;
    public static final ResourceKey<Registry<BlockEntityType<?>>> BLOCK_ENTITY_TYPE_REGISTRY;
    public static final ResourceKey<Registry<Motive>> MOTIVE_REGISTRY;
    public static final ResourceKey<Registry<ResourceLocation>> CUSTOM_STAT_REGISTRY;
    public static final ResourceKey<Registry<ChunkStatus>> CHUNK_STATUS_REGISTRY;
    public static final ResourceKey<Registry<RuleTestType<?>>> RULE_TEST_REGISTRY;
    public static final ResourceKey<Registry<PosRuleTestType<?>>> POS_RULE_TEST_REGISTRY;
    public static final ResourceKey<Registry<MenuType<?>>> MENU_REGISTRY;
    public static final ResourceKey<Registry<RecipeType<?>>> RECIPE_TYPE_REGISTRY;
    public static final ResourceKey<Registry<RecipeSerializer<?>>> RECIPE_SERIALIZER_REGISTRY;
    public static final ResourceKey<Registry<Attribute>> ATTRIBUTE_REGISTRY;
    public static final ResourceKey<Registry<StatType<?>>> STAT_TYPE_REGISTRY;
    public static final ResourceKey<Registry<VillagerType>> VILLAGER_TYPE_REGISTRY;
    public static final ResourceKey<Registry<VillagerProfession>> VILLAGER_PROFESSION_REGISTRY;
    public static final ResourceKey<Registry<PoiType>> POINT_OF_INTEREST_TYPE_REGISTRY;
    public static final ResourceKey<Registry<MemoryModuleType<?>>> MEMORY_MODULE_TYPE_REGISTRY;
    public static final ResourceKey<Registry<SensorType<?>>> SENSOR_TYPE_REGISTRY;
    public static final ResourceKey<Registry<Schedule>> SCHEDULE_REGISTRY;
    public static final ResourceKey<Registry<Activity>> ACTIVITY_REGISTRY;
    public static final ResourceKey<Registry<LootPoolEntryType>> LOOT_ENTRY_REGISTRY;
    public static final ResourceKey<Registry<LootItemFunctionType>> LOOT_FUNCTION_REGISTRY;
    public static final ResourceKey<Registry<LootItemConditionType>> LOOT_ITEM_REGISTRY;
    public static final ResourceKey<Registry<DimensionType>> DIMENSION_TYPE_REGISTRY;
    public static final ResourceKey<Registry<Level>> DIMENSION_REGISTRY;
    public static final ResourceKey<Registry<LevelStem>> LEVEL_STEM_REGISTRY;
    public static final Registry<SoundEvent> SOUND_EVENT;
    public static final DefaultedRegistry<Fluid> FLUID;
    public static final Registry<MobEffect> MOB_EFFECT;
    public static final DefaultedRegistry<Block> BLOCK;
    public static final Registry<Enchantment> ENCHANTMENT;
    public static final DefaultedRegistry<EntityType<?>> ENTITY_TYPE;
    public static final DefaultedRegistry<Item> ITEM;
    public static final DefaultedRegistry<Potion> POTION;
    public static final Registry<ParticleType<?>> PARTICLE_TYPE;
    public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPE;
    public static final DefaultedRegistry<Motive> MOTIVE;
    public static final Registry<ResourceLocation> CUSTOM_STAT;
    public static final DefaultedRegistry<ChunkStatus> CHUNK_STATUS;
    public static final Registry<RuleTestType<?>> RULE_TEST;
    public static final Registry<PosRuleTestType<?>> POS_RULE_TEST;
    public static final Registry<MenuType<?>> MENU;
    public static final Registry<RecipeType<?>> RECIPE_TYPE;
    public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZER;
    public static final Registry<Attribute> ATTRIBUTE;
    public static final Registry<StatType<?>> STAT_TYPE;
    public static final DefaultedRegistry<VillagerType> VILLAGER_TYPE;
    public static final DefaultedRegistry<VillagerProfession> VILLAGER_PROFESSION;
    public static final DefaultedRegistry<PoiType> POINT_OF_INTEREST_TYPE;
    public static final DefaultedRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPE;
    public static final DefaultedRegistry<SensorType<?>> SENSOR_TYPE;
    public static final Registry<Schedule> SCHEDULE;
    public static final Registry<Activity> ACTIVITY;
    public static final Registry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE;
    public static final Registry<LootItemFunctionType> LOOT_FUNCTION_TYPE;
    public static final Registry<LootItemConditionType> LOOT_CONDITION_TYPE;
    public static final ResourceKey<Registry<NoiseGeneratorSettings>> NOISE_GENERATOR_SETTINGS_REGISTRY;
    public static final ResourceKey<Registry<ConfiguredSurfaceBuilder<?>>> CONFIGURED_SURFACE_BUILDER_REGISTRY;
    public static final ResourceKey<Registry<ConfiguredWorldCarver<?>>> CONFIGURED_CARVER_REGISTRY;
    public static final ResourceKey<Registry<ConfiguredFeature<?, ?>>> CONFIGURED_FEATURE_REGISTRY;
    public static final ResourceKey<Registry<ConfiguredStructureFeature<?, ?>>> CONFIGURED_STRUCTURE_FEATURE_REGISTRY;
    public static final ResourceKey<Registry<StructureProcessorList>> PROCESSOR_LIST_REGISTRY;
    public static final ResourceKey<Registry<StructureTemplatePool>> TEMPLATE_POOL_REGISTRY;
    public static final ResourceKey<Registry<Biome>> BIOME_REGISTRY;
    public static final ResourceKey<Registry<SurfaceBuilder<?>>> SURFACE_BUILDER_REGISTRY;
    public static final Registry<SurfaceBuilder<?>> SURFACE_BUILDER;
    public static final ResourceKey<Registry<WorldCarver<?>>> CARVER_REGISTRY;
    public static final Registry<WorldCarver<?>> CARVER;
    public static final ResourceKey<Registry<Feature<?>>> FEATURE_REGISTRY;
    public static final Registry<Feature<?>> FEATURE;
    public static final ResourceKey<Registry<StructureFeature<?>>> STRUCTURE_FEATURE_REGISTRY;
    public static final Registry<StructureFeature<?>> STRUCTURE_FEATURE;
    public static final ResourceKey<Registry<StructurePieceType>> STRUCTURE_PIECE_REGISTRY;
    public static final Registry<StructurePieceType> STRUCTURE_PIECE;
    public static final ResourceKey<Registry<FeatureDecorator<?>>> DECORATOR_REGISTRY;
    public static final Registry<FeatureDecorator<?>> DECORATOR;
    public static final ResourceKey<Registry<BlockStateProviderType<?>>> BLOCK_STATE_PROVIDER_TYPE_REGISTRY;
    public static final ResourceKey<Registry<BlockPlacerType<?>>> BLOCK_PLACER_TYPE_REGISTRY;
    public static final ResourceKey<Registry<FoliagePlacerType<?>>> FOLIAGE_PLACER_TYPE_REGISTRY;
    public static final ResourceKey<Registry<TrunkPlacerType<?>>> TRUNK_PLACER_TYPE_REGISTRY;
    public static final ResourceKey<Registry<TreeDecoratorType<?>>> TREE_DECORATOR_TYPE_REGISTRY;
    public static final ResourceKey<Registry<FeatureSizeType<?>>> FEATURE_SIZE_TYPE_REGISTRY;
    public static final ResourceKey<Registry<Codec<? extends BiomeSource>>> BIOME_SOURCE_REGISTRY;
    public static final ResourceKey<Registry<Codec<? extends ChunkGenerator>>> CHUNK_GENERATOR_REGISTRY;
    public static final ResourceKey<Registry<StructureProcessorType<?>>> STRUCTURE_PROCESSOR_REGISTRY;
    public static final ResourceKey<Registry<StructurePoolElementType<?>>> STRUCTURE_POOL_ELEMENT_REGISTRY;
    public static final Registry<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPES;
    public static final Registry<BlockPlacerType<?>> BLOCK_PLACER_TYPES;
    public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES;
    public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPES;
    public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPES;
    public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPES;
    public static final Registry<Codec<? extends BiomeSource>> BIOME_SOURCE;
    public static final Registry<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR;
    public static final Registry<StructureProcessorType<?>> STRUCTURE_PROCESSOR;
    public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT;
    private final ResourceKey<? extends Registry<T>> key;
    private final Lifecycle lifecycle;
    
    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String string) {
        return ResourceKey.<T>createRegistryKey(new ResourceLocation(string));
    }
    
    public static <T extends WritableRegistry<?>> void checkRegistry(final WritableRegistry<T> gs) {
        gs.forEach(gs2 -> {
            if (gs2.keySet().isEmpty()) {
                Registry.LOGGER.error("Registry '{}' was empty after loading", gs.getKey((DefaultedRegistry<Object>)gs2));
                if (SharedConstants.IS_RUNNING_IN_IDE) {
                    throw new IllegalStateException(new StringBuilder().append("Registry: '").append(gs.getKey((DefaultedRegistry<Object>)gs2)).append("' is empty, not allowed, fix me!").toString());
                }
            }
            if (gs2 instanceof DefaultedRegistry) {
                final ResourceLocation vk3 = ((DefaultedRegistry<Object>)gs2).getDefaultKey();
                Validate.notNull(gs2.get(vk3), new StringBuilder().append("Missing default of DefaultedMappedRegistry: ").append(vk3).toString(), new Object[0]);
            }
        });
    }
    
    private static <T> Registry<T> registerSimple(final ResourceKey<? extends Registry<T>> vj, final Supplier<T> supplier) {
        return Registry.<T>registerSimple(vj, Lifecycle.experimental(), supplier);
    }
    
    private static <T> DefaultedRegistry<T> registerDefaulted(final ResourceKey<? extends Registry<T>> vj, final String string, final Supplier<T> supplier) {
        return Registry.<T>registerDefaulted(vj, string, Lifecycle.experimental(), supplier);
    }
    
    private static <T> Registry<T> registerSimple(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle, final Supplier<T> supplier) {
        return Registry.<T, MappedRegistry<T>>internalRegister(vj, new MappedRegistry<T>(vj, lifecycle), supplier, lifecycle);
    }
    
    private static <T> DefaultedRegistry<T> registerDefaulted(final ResourceKey<? extends Registry<T>> vj, final String string, final Lifecycle lifecycle, final Supplier<T> supplier) {
        return Registry.<T, DefaultedRegistry<T>>internalRegister(vj, new DefaultedRegistry<T>(string, vj, lifecycle), supplier, lifecycle);
    }
    
    private static <T, R extends WritableRegistry<T>> R internalRegister(final ResourceKey<? extends Registry<T>> vj, final R gs, final Supplier<T> supplier, final Lifecycle lifecycle) {
        final ResourceLocation vk5 = vj.location();
        Registry.LOADERS.put(vk5, supplier);
        final WritableRegistry<R> gs2 = (WritableRegistry<R>)Registry.WRITABLE_REGISTRY;
        return gs2.<R>register((ResourceKey<R>)vj, gs, lifecycle);
    }
    
    protected Registry(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle) {
        this.key = vj;
        this.lifecycle = lifecycle;
    }
    
    public ResourceKey<? extends Registry<T>> key() {
        return this.key;
    }
    
    public String toString() {
        return new StringBuilder().append("Registry[").append(this.key).append(" (").append(this.lifecycle).append(")]").toString();
    }
    
    public <U> DataResult<Pair<T, U>> decode(final DynamicOps<U> dynamicOps, final U object) {
        if (dynamicOps.compressMaps()) {
            return (DataResult<Pair<T, U>>)dynamicOps.getNumberValue(object).flatMap(number -> {
                final T object3 = this.byId(number.intValue());
                if (object3 == null) {
                    return DataResult.error(new StringBuilder().append("Unknown registry id: ").append(number).toString());
                }
                return DataResult.success(object3, this.lifecycle(object3));
            }).map(object -> Pair.of(object, dynamicOps.empty()));
        }
        return (DataResult<Pair<T, U>>)ResourceLocation.CODEC.decode((DynamicOps)dynamicOps, object).flatMap(pair -> {
            final T object3 = this.get((ResourceLocation)pair.getFirst());
            if (object3 == null) {
                return DataResult.error(new StringBuilder().append("Unknown registry key: ").append(pair.getFirst()).toString());
            }
            return DataResult.success(Pair.of((Object)object3, pair.getSecond()), this.lifecycle(object3));
        });
    }
    
    public <U> DataResult<U> encode(final T object1, final DynamicOps<U> dynamicOps, final U object3) {
        final ResourceLocation vk5 = this.getKey(object1);
        if (vk5 == null) {
            return (DataResult<U>)DataResult.error(new StringBuilder().append("Unknown registry element ").append(object1).toString());
        }
        if (dynamicOps.compressMaps()) {
            return (DataResult<U>)dynamicOps.mergeToPrimitive(object3, dynamicOps.createInt(this.getId(object1))).setLifecycle(this.lifecycle);
        }
        return (DataResult<U>)dynamicOps.mergeToPrimitive(object3, dynamicOps.createString(vk5.toString())).setLifecycle(this.lifecycle);
    }
    
    public <U> Stream<U> keys(final DynamicOps<U> dynamicOps) {
        return (Stream<U>)this.keySet().stream().map(vk -> dynamicOps.createString(vk.toString()));
    }
    
    @Nullable
    public abstract ResourceLocation getKey(final T object);
    
    public abstract Optional<ResourceKey<T>> getResourceKey(final T object);
    
    public abstract int getId(@Nullable final T object);
    
    @Nullable
    public abstract T get(@Nullable final ResourceKey<T> vj);
    
    @Nullable
    public abstract T get(@Nullable final ResourceLocation vk);
    
    protected abstract Lifecycle lifecycle(final T object);
    
    public abstract Lifecycle elementsLifecycle();
    
    public Optional<T> getOptional(@Nullable final ResourceLocation vk) {
        return (Optional<T>)Optional.ofNullable(this.get(vk));
    }
    
    public Optional<T> getOptional(@Nullable final ResourceKey<T> vj) {
        return (Optional<T>)Optional.ofNullable(this.get((ResourceKey<Object>)vj));
    }
    
    public T getOrThrow(final ResourceKey<T> vj) {
        final T object3 = this.get(vj);
        if (object3 == null) {
            throw new IllegalStateException(new StringBuilder().append("Missing: ").append(vj).toString());
        }
        return object3;
    }
    
    public abstract Set<ResourceLocation> keySet();
    
    public abstract Set<Map.Entry<ResourceKey<T>, T>> entrySet();
    
    public Stream<T> stream() {
        return (Stream<T>)StreamSupport.stream(this.spliterator(), false);
    }
    
    public abstract boolean containsKey(final ResourceLocation vk);
    
    public static <T> T register(final Registry<? super T> gm, final String string, final T object) {
        return Registry.register(gm, new ResourceLocation(string), object);
    }
    
    public static <V, T extends V> T register(final Registry<V> gm, final ResourceLocation vk, final T object) {
        return ((WritableRegistry)gm).<T>register(ResourceKey.create(gm.key, vk), object, Lifecycle.stable());
    }
    
    public static <V, T extends V> T registerMapping(final Registry<V> gm, final int integer, final String string, final T object) {
        return ((WritableRegistry)gm).<T>registerMapping(integer, ResourceKey.create(gm.key, new ResourceLocation(string)), object, Lifecycle.stable());
    }
    
    static {
        LOGGER = LogManager.getLogger();
        LOADERS = (Map)Maps.newLinkedHashMap();
        ROOT_REGISTRY_NAME = new ResourceLocation("root");
        WRITABLE_REGISTRY = new MappedRegistry<WritableRegistry<?>>(Registry.createRegistryKey("root"), Lifecycle.experimental());
        REGISTRY = Registry.WRITABLE_REGISTRY;
        SOUND_EVENT_REGISTRY = Registry.<SoundEvent>createRegistryKey("sound_event");
        FLUID_REGISTRY = Registry.<Fluid>createRegistryKey("fluid");
        MOB_EFFECT_REGISTRY = Registry.<MobEffect>createRegistryKey("mob_effect");
        BLOCK_REGISTRY = Registry.<Block>createRegistryKey("block");
        ENCHANTMENT_REGISTRY = Registry.<Enchantment>createRegistryKey("enchantment");
        ENTITY_TYPE_REGISTRY = Registry.<EntityType<?>>createRegistryKey("entity_type");
        ITEM_REGISTRY = Registry.<Item>createRegistryKey("item");
        POTION_REGISTRY = Registry.<Potion>createRegistryKey("potion");
        PARTICLE_TYPE_REGISTRY = Registry.<ParticleType<?>>createRegistryKey("particle_type");
        BLOCK_ENTITY_TYPE_REGISTRY = Registry.<BlockEntityType<?>>createRegistryKey("block_entity_type");
        MOTIVE_REGISTRY = Registry.<Motive>createRegistryKey("motive");
        CUSTOM_STAT_REGISTRY = Registry.<ResourceLocation>createRegistryKey("custom_stat");
        CHUNK_STATUS_REGISTRY = Registry.<ChunkStatus>createRegistryKey("chunk_status");
        RULE_TEST_REGISTRY = Registry.<RuleTestType<?>>createRegistryKey("rule_test");
        POS_RULE_TEST_REGISTRY = Registry.<PosRuleTestType<?>>createRegistryKey("pos_rule_test");
        MENU_REGISTRY = Registry.<MenuType<?>>createRegistryKey("menu");
        RECIPE_TYPE_REGISTRY = Registry.<RecipeType<?>>createRegistryKey("recipe_type");
        RECIPE_SERIALIZER_REGISTRY = Registry.<RecipeSerializer<?>>createRegistryKey("recipe_serializer");
        ATTRIBUTE_REGISTRY = Registry.<Attribute>createRegistryKey("attribute");
        STAT_TYPE_REGISTRY = Registry.<StatType<?>>createRegistryKey("stat_type");
        VILLAGER_TYPE_REGISTRY = Registry.<VillagerType>createRegistryKey("villager_type");
        VILLAGER_PROFESSION_REGISTRY = Registry.<VillagerProfession>createRegistryKey("villager_profession");
        POINT_OF_INTEREST_TYPE_REGISTRY = Registry.<PoiType>createRegistryKey("point_of_interest_type");
        MEMORY_MODULE_TYPE_REGISTRY = Registry.<MemoryModuleType<?>>createRegistryKey("memory_module_type");
        SENSOR_TYPE_REGISTRY = Registry.<SensorType<?>>createRegistryKey("sensor_type");
        SCHEDULE_REGISTRY = Registry.<Schedule>createRegistryKey("schedule");
        ACTIVITY_REGISTRY = Registry.<Activity>createRegistryKey("activity");
        LOOT_ENTRY_REGISTRY = Registry.<LootPoolEntryType>createRegistryKey("loot_pool_entry_type");
        LOOT_FUNCTION_REGISTRY = Registry.<LootItemFunctionType>createRegistryKey("loot_function_type");
        LOOT_ITEM_REGISTRY = Registry.<LootItemConditionType>createRegistryKey("loot_condition_type");
        DIMENSION_TYPE_REGISTRY = Registry.<DimensionType>createRegistryKey("dimension_type");
        DIMENSION_REGISTRY = Registry.<Level>createRegistryKey("dimension");
        LEVEL_STEM_REGISTRY = Registry.<LevelStem>createRegistryKey("dimension");
        SOUND_EVENT = Registry.<SoundEvent>registerSimple(Registry.SOUND_EVENT_REGISTRY, (java.util.function.Supplier<SoundEvent>)(() -> SoundEvents.ITEM_PICKUP));
        FLUID = Registry.<Fluid>registerDefaulted(Registry.FLUID_REGISTRY, "empty", (java.util.function.Supplier<Fluid>)(() -> Fluids.EMPTY));
        MOB_EFFECT = Registry.<MobEffect>registerSimple(Registry.MOB_EFFECT_REGISTRY, (java.util.function.Supplier<MobEffect>)(() -> MobEffects.LUCK));
        BLOCK = Registry.<Block>registerDefaulted(Registry.BLOCK_REGISTRY, "air", (java.util.function.Supplier<Block>)(() -> Blocks.AIR));
        ENCHANTMENT = Registry.<Enchantment>registerSimple(Registry.ENCHANTMENT_REGISTRY, (java.util.function.Supplier<Enchantment>)(() -> Enchantments.BLOCK_FORTUNE));
        ENTITY_TYPE = Registry.<EntityType<?>>registerDefaulted(Registry.ENTITY_TYPE_REGISTRY, "pig", (java.util.function.Supplier<EntityType<?>>)(() -> EntityType.PIG));
        ITEM = Registry.<Item>registerDefaulted(Registry.ITEM_REGISTRY, "air", (java.util.function.Supplier<Item>)(() -> Items.AIR));
        POTION = Registry.<Potion>registerDefaulted(Registry.POTION_REGISTRY, "empty", (java.util.function.Supplier<Potion>)(() -> Potions.EMPTY));
        PARTICLE_TYPE = Registry.<ParticleType<?>>registerSimple(Registry.PARTICLE_TYPE_REGISTRY, (java.util.function.Supplier<ParticleType<?>>)(() -> ParticleTypes.BLOCK));
        BLOCK_ENTITY_TYPE = Registry.<BlockEntityType<?>>registerSimple(Registry.BLOCK_ENTITY_TYPE_REGISTRY, (java.util.function.Supplier<BlockEntityType<?>>)(() -> BlockEntityType.FURNACE));
        MOTIVE = Registry.<Motive>registerDefaulted(Registry.MOTIVE_REGISTRY, "kebab", (java.util.function.Supplier<Motive>)(() -> Motive.KEBAB));
        CUSTOM_STAT = Registry.<ResourceLocation>registerSimple(Registry.CUSTOM_STAT_REGISTRY, (java.util.function.Supplier<ResourceLocation>)(() -> Stats.JUMP));
        CHUNK_STATUS = Registry.<ChunkStatus>registerDefaulted(Registry.CHUNK_STATUS_REGISTRY, "empty", (java.util.function.Supplier<ChunkStatus>)(() -> ChunkStatus.EMPTY));
        RULE_TEST = Registry.<RuleTestType<?>>registerSimple(Registry.RULE_TEST_REGISTRY, (java.util.function.Supplier<RuleTestType<?>>)(() -> RuleTestType.ALWAYS_TRUE_TEST));
        POS_RULE_TEST = Registry.<PosRuleTestType<?>>registerSimple(Registry.POS_RULE_TEST_REGISTRY, (java.util.function.Supplier<PosRuleTestType<?>>)(() -> PosRuleTestType.ALWAYS_TRUE_TEST));
        MENU = Registry.<MenuType<?>>registerSimple(Registry.MENU_REGISTRY, (java.util.function.Supplier<MenuType<?>>)(() -> MenuType.ANVIL));
        RECIPE_TYPE = Registry.<RecipeType<?>>registerSimple(Registry.RECIPE_TYPE_REGISTRY, (java.util.function.Supplier<RecipeType<?>>)(() -> RecipeType.CRAFTING));
        RECIPE_SERIALIZER = Registry.<RecipeSerializer<?>>registerSimple(Registry.RECIPE_SERIALIZER_REGISTRY, (java.util.function.Supplier<RecipeSerializer<?>>)(() -> RecipeSerializer.SHAPELESS_RECIPE));
        ATTRIBUTE = Registry.<Attribute>registerSimple(Registry.ATTRIBUTE_REGISTRY, (java.util.function.Supplier<Attribute>)(() -> Attributes.LUCK));
        STAT_TYPE = Registry.<StatType<?>>registerSimple(Registry.STAT_TYPE_REGISTRY, (java.util.function.Supplier<StatType<?>>)(() -> Stats.ITEM_USED));
        VILLAGER_TYPE = Registry.<VillagerType>registerDefaulted(Registry.VILLAGER_TYPE_REGISTRY, "plains", (java.util.function.Supplier<VillagerType>)(() -> VillagerType.PLAINS));
        VILLAGER_PROFESSION = Registry.<VillagerProfession>registerDefaulted(Registry.VILLAGER_PROFESSION_REGISTRY, "none", (java.util.function.Supplier<VillagerProfession>)(() -> VillagerProfession.NONE));
        POINT_OF_INTEREST_TYPE = Registry.<PoiType>registerDefaulted(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, "unemployed", (java.util.function.Supplier<PoiType>)(() -> PoiType.UNEMPLOYED));
        MEMORY_MODULE_TYPE = Registry.<MemoryModuleType<?>>registerDefaulted(Registry.MEMORY_MODULE_TYPE_REGISTRY, "dummy", (java.util.function.Supplier<MemoryModuleType<?>>)(() -> MemoryModuleType.DUMMY));
        SENSOR_TYPE = Registry.<SensorType<?>>registerDefaulted(Registry.SENSOR_TYPE_REGISTRY, "dummy", (java.util.function.Supplier<SensorType<?>>)(() -> SensorType.DUMMY));
        SCHEDULE = Registry.<Schedule>registerSimple(Registry.SCHEDULE_REGISTRY, (java.util.function.Supplier<Schedule>)(() -> Schedule.EMPTY));
        ACTIVITY = Registry.<Activity>registerSimple(Registry.ACTIVITY_REGISTRY, (java.util.function.Supplier<Activity>)(() -> Activity.IDLE));
        LOOT_POOL_ENTRY_TYPE = Registry.<LootPoolEntryType>registerSimple(Registry.LOOT_ENTRY_REGISTRY, (java.util.function.Supplier<LootPoolEntryType>)(() -> LootPoolEntries.EMPTY));
        LOOT_FUNCTION_TYPE = Registry.<LootItemFunctionType>registerSimple(Registry.LOOT_FUNCTION_REGISTRY, (java.util.function.Supplier<LootItemFunctionType>)(() -> LootItemFunctions.SET_COUNT));
        LOOT_CONDITION_TYPE = Registry.<LootItemConditionType>registerSimple(Registry.LOOT_ITEM_REGISTRY, (java.util.function.Supplier<LootItemConditionType>)(() -> LootItemConditions.INVERTED));
        NOISE_GENERATOR_SETTINGS_REGISTRY = Registry.<NoiseGeneratorSettings>createRegistryKey("worldgen/noise_settings");
        CONFIGURED_SURFACE_BUILDER_REGISTRY = Registry.<ConfiguredSurfaceBuilder<?>>createRegistryKey("worldgen/configured_surface_builder");
        CONFIGURED_CARVER_REGISTRY = Registry.<ConfiguredWorldCarver<?>>createRegistryKey("worldgen/configured_carver");
        CONFIGURED_FEATURE_REGISTRY = Registry.<ConfiguredFeature<?, ?>>createRegistryKey("worldgen/configured_feature");
        CONFIGURED_STRUCTURE_FEATURE_REGISTRY = Registry.<ConfiguredStructureFeature<?, ?>>createRegistryKey("worldgen/configured_structure_feature");
        PROCESSOR_LIST_REGISTRY = Registry.<StructureProcessorList>createRegistryKey("worldgen/processor_list");
        TEMPLATE_POOL_REGISTRY = Registry.<StructureTemplatePool>createRegistryKey("worldgen/template_pool");
        BIOME_REGISTRY = Registry.<Biome>createRegistryKey("worldgen/biome");
        SURFACE_BUILDER_REGISTRY = Registry.<SurfaceBuilder<?>>createRegistryKey("worldgen/surface_builder");
        SURFACE_BUILDER = Registry.<SurfaceBuilder<?>>registerSimple(Registry.SURFACE_BUILDER_REGISTRY, (java.util.function.Supplier<SurfaceBuilder<?>>)(() -> SurfaceBuilder.DEFAULT));
        CARVER_REGISTRY = Registry.<WorldCarver<?>>createRegistryKey("worldgen/carver");
        CARVER = Registry.<WorldCarver<?>>registerSimple(Registry.CARVER_REGISTRY, (java.util.function.Supplier<WorldCarver<?>>)(() -> WorldCarver.CAVE));
        FEATURE_REGISTRY = Registry.<Feature<?>>createRegistryKey("worldgen/feature");
        FEATURE = Registry.<Feature<?>>registerSimple(Registry.FEATURE_REGISTRY, (java.util.function.Supplier<Feature<?>>)(() -> Feature.ORE));
        STRUCTURE_FEATURE_REGISTRY = Registry.<StructureFeature<?>>createRegistryKey("worldgen/structure_feature");
        STRUCTURE_FEATURE = Registry.<StructureFeature<?>>registerSimple(Registry.STRUCTURE_FEATURE_REGISTRY, (java.util.function.Supplier<StructureFeature<?>>)(() -> StructureFeature.MINESHAFT));
        STRUCTURE_PIECE_REGISTRY = Registry.<StructurePieceType>createRegistryKey("worldgen/structure_piece");
        STRUCTURE_PIECE = Registry.<StructurePieceType>registerSimple(Registry.STRUCTURE_PIECE_REGISTRY, (java.util.function.Supplier<StructurePieceType>)(() -> StructurePieceType.MINE_SHAFT_ROOM));
        DECORATOR_REGISTRY = Registry.<FeatureDecorator<?>>createRegistryKey("worldgen/decorator");
        DECORATOR = Registry.<FeatureDecorator<?>>registerSimple(Registry.DECORATOR_REGISTRY, (java.util.function.Supplier<FeatureDecorator<?>>)(() -> FeatureDecorator.NOPE));
        BLOCK_STATE_PROVIDER_TYPE_REGISTRY = Registry.<BlockStateProviderType<?>>createRegistryKey("worldgen/block_state_provider_type");
        BLOCK_PLACER_TYPE_REGISTRY = Registry.<BlockPlacerType<?>>createRegistryKey("worldgen/block_placer_type");
        FOLIAGE_PLACER_TYPE_REGISTRY = Registry.<FoliagePlacerType<?>>createRegistryKey("worldgen/foliage_placer_type");
        TRUNK_PLACER_TYPE_REGISTRY = Registry.<TrunkPlacerType<?>>createRegistryKey("worldgen/trunk_placer_type");
        TREE_DECORATOR_TYPE_REGISTRY = Registry.<TreeDecoratorType<?>>createRegistryKey("worldgen/tree_decorator_type");
        FEATURE_SIZE_TYPE_REGISTRY = Registry.<FeatureSizeType<?>>createRegistryKey("worldgen/feature_size_type");
        BIOME_SOURCE_REGISTRY = Registry.<Codec<? extends BiomeSource>>createRegistryKey("worldgen/biome_source");
        CHUNK_GENERATOR_REGISTRY = Registry.<Codec<? extends ChunkGenerator>>createRegistryKey("worldgen/chunk_generator");
        STRUCTURE_PROCESSOR_REGISTRY = Registry.<StructureProcessorType<?>>createRegistryKey("worldgen/structure_processor");
        STRUCTURE_POOL_ELEMENT_REGISTRY = Registry.<StructurePoolElementType<?>>createRegistryKey("worldgen/structure_pool_element");
        BLOCKSTATE_PROVIDER_TYPES = Registry.<BlockStateProviderType<?>>registerSimple(Registry.BLOCK_STATE_PROVIDER_TYPE_REGISTRY, (java.util.function.Supplier<BlockStateProviderType<?>>)(() -> BlockStateProviderType.SIMPLE_STATE_PROVIDER));
        BLOCK_PLACER_TYPES = Registry.<BlockPlacerType<?>>registerSimple(Registry.BLOCK_PLACER_TYPE_REGISTRY, (java.util.function.Supplier<BlockPlacerType<?>>)(() -> BlockPlacerType.SIMPLE_BLOCK_PLACER));
        FOLIAGE_PLACER_TYPES = Registry.<FoliagePlacerType<?>>registerSimple(Registry.FOLIAGE_PLACER_TYPE_REGISTRY, (java.util.function.Supplier<FoliagePlacerType<?>>)(() -> FoliagePlacerType.BLOB_FOLIAGE_PLACER));
        TRUNK_PLACER_TYPES = Registry.<TrunkPlacerType<?>>registerSimple(Registry.TRUNK_PLACER_TYPE_REGISTRY, (java.util.function.Supplier<TrunkPlacerType<?>>)(() -> TrunkPlacerType.STRAIGHT_TRUNK_PLACER));
        TREE_DECORATOR_TYPES = Registry.<TreeDecoratorType<?>>registerSimple(Registry.TREE_DECORATOR_TYPE_REGISTRY, (java.util.function.Supplier<TreeDecoratorType<?>>)(() -> TreeDecoratorType.LEAVE_VINE));
        FEATURE_SIZE_TYPES = Registry.<FeatureSizeType<?>>registerSimple(Registry.FEATURE_SIZE_TYPE_REGISTRY, (java.util.function.Supplier<FeatureSizeType<?>>)(() -> FeatureSizeType.TWO_LAYERS_FEATURE_SIZE));
        BIOME_SOURCE = Registry.<Codec<? extends BiomeSource>>registerSimple(Registry.BIOME_SOURCE_REGISTRY, Lifecycle.stable(), (java.util.function.Supplier<Codec<? extends BiomeSource>>)(() -> BiomeSource.CODEC));
        CHUNK_GENERATOR = Registry.<Codec<? extends ChunkGenerator>>registerSimple(Registry.CHUNK_GENERATOR_REGISTRY, Lifecycle.stable(), (java.util.function.Supplier<Codec<? extends ChunkGenerator>>)(() -> ChunkGenerator.CODEC));
        STRUCTURE_PROCESSOR = Registry.<StructureProcessorType<?>>registerSimple(Registry.STRUCTURE_PROCESSOR_REGISTRY, (java.util.function.Supplier<StructureProcessorType<?>>)(() -> StructureProcessorType.BLOCK_IGNORE));
        STRUCTURE_POOL_ELEMENT = Registry.<StructurePoolElementType<?>>registerSimple(Registry.STRUCTURE_POOL_ELEMENT_REGISTRY, (java.util.function.Supplier<StructurePoolElementType<?>>)(() -> StructurePoolElementType.EMPTY));
        BuiltinRegistries.bootstrap();
        Registry.LOADERS.forEach((vk, supplier) -> {
            if (supplier.get() == null) {
                Registry.LOGGER.error("Unable to bootstrap registry '{}'", vk);
            }
        });
        Registry.<WritableRegistry<?>>checkRegistry(Registry.WRITABLE_REGISTRY);
    }
}
