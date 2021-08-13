package net.minecraft.world.level.storage.loot.parameters;

import com.google.common.collect.HashBiMap;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.BiMap;

public class LootContextParamSets {
    private static final BiMap<ResourceLocation, LootContextParamSet> REGISTRY;
    public static final LootContextParamSet EMPTY;
    public static final LootContextParamSet CHEST;
    public static final LootContextParamSet COMMAND;
    public static final LootContextParamSet SELECTOR;
    public static final LootContextParamSet FISHING;
    public static final LootContextParamSet ENTITY;
    public static final LootContextParamSet GIFT;
    public static final LootContextParamSet PIGLIN_BARTER;
    public static final LootContextParamSet ADVANCEMENT_REWARD;
    public static final LootContextParamSet ADVANCEMENT_ENTITY;
    public static final LootContextParamSet ALL_PARAMS;
    public static final LootContextParamSet BLOCK;
    
    private static LootContextParamSet register(final String string, final Consumer<LootContextParamSet.Builder> consumer) {
        final LootContextParamSet.Builder a3 = new LootContextParamSet.Builder();
        consumer.accept(a3);
        final LootContextParamSet dax4 = a3.build();
        final ResourceLocation vk5 = new ResourceLocation(string);
        final LootContextParamSet dax5 = (LootContextParamSet)LootContextParamSets.REGISTRY.put(vk5, dax4);
        if (dax5 != null) {
            throw new IllegalStateException(new StringBuilder().append("Loot table parameter set ").append(vk5).append(" is already registered").toString());
        }
        return dax4;
    }
    
    @Nullable
    public static LootContextParamSet get(final ResourceLocation vk) {
        return (LootContextParamSet)LootContextParamSets.REGISTRY.get(vk);
    }
    
    @Nullable
    public static ResourceLocation getKey(final LootContextParamSet dax) {
        return (ResourceLocation)LootContextParamSets.REGISTRY.inverse().get(dax);
    }
    
    static {
        REGISTRY = (BiMap)HashBiMap.create();
        EMPTY = register("empty", (Consumer<LootContextParamSet.Builder>)(a -> {}));
        CHEST = register("chest", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY)));
        COMMAND = register("command", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY)));
        SELECTOR = register("selector", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY)));
        FISHING = register("fishing", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY)));
        ENTITY = register("entity", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.KILLER_ENTITY).optional(LootContextParams.DIRECT_KILLER_ENTITY).optional(LootContextParams.LAST_DAMAGE_PLAYER)));
        GIFT = register("gift", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY)));
        PIGLIN_BARTER = register("barter", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.THIS_ENTITY)));
        ADVANCEMENT_REWARD = register("advancement_reward", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)));
        ADVANCEMENT_ENTITY = register("advancement_entity", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)));
        ALL_PARAMS = register("generic", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.THIS_ENTITY).required(LootContextParams.LAST_DAMAGE_PLAYER).required(LootContextParams.DAMAGE_SOURCE).required(LootContextParams.KILLER_ENTITY).required(LootContextParams.DIRECT_KILLER_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).required(LootContextParams.TOOL).required(LootContextParams.EXPLOSION_RADIUS)));
        BLOCK = register("block", (Consumer<LootContextParamSet.Builder>)(a -> a.required(LootContextParams.BLOCK_STATE).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.EXPLOSION_RADIUS)));
    }
}
