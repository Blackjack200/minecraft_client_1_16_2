package net.minecraft.world.level.storage.loot;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Map;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;

public class LootContext {
    private final Random random;
    private final float luck;
    private final ServerLevel level;
    private final Function<ResourceLocation, LootTable> lootTables;
    private final Set<LootTable> visitedTables;
    private final Function<ResourceLocation, LootItemCondition> conditions;
    private final Set<LootItemCondition> visitedConditions;
    private final Map<LootContextParam<?>, Object> params;
    private final Map<ResourceLocation, DynamicDrop> dynamicDrops;
    
    private LootContext(final Random random, final float float2, final ServerLevel aag, final Function<ResourceLocation, LootTable> function4, final Function<ResourceLocation, LootItemCondition> function5, final Map<LootContextParam<?>, Object> map6, final Map<ResourceLocation, DynamicDrop> map7) {
        this.visitedTables = (Set<LootTable>)Sets.newLinkedHashSet();
        this.visitedConditions = (Set<LootItemCondition>)Sets.newLinkedHashSet();
        this.random = random;
        this.luck = float2;
        this.level = aag;
        this.lootTables = function4;
        this.conditions = function5;
        this.params = (Map<LootContextParam<?>, Object>)ImmutableMap.copyOf((Map)map6);
        this.dynamicDrops = (Map<ResourceLocation, DynamicDrop>)ImmutableMap.copyOf((Map)map7);
    }
    
    public boolean hasParam(final LootContextParam<?> daw) {
        return this.params.containsKey(daw);
    }
    
    public void addDynamicDrops(final ResourceLocation vk, final Consumer<ItemStack> consumer) {
        final DynamicDrop b4 = (DynamicDrop)this.dynamicDrops.get(vk);
        if (b4 != null) {
            b4.add(this, consumer);
        }
    }
    
    @Nullable
    public <T> T getParamOrNull(final LootContextParam<T> daw) {
        return (T)this.params.get(daw);
    }
    
    public boolean addVisitedTable(final LootTable cyv) {
        return this.visitedTables.add(cyv);
    }
    
    public void removeVisitedTable(final LootTable cyv) {
        this.visitedTables.remove(cyv);
    }
    
    public boolean addVisitedCondition(final LootItemCondition dbl) {
        return this.visitedConditions.add(dbl);
    }
    
    public void removeVisitedCondition(final LootItemCondition dbl) {
        this.visitedConditions.remove(dbl);
    }
    
    public LootTable getLootTable(final ResourceLocation vk) {
        return (LootTable)this.lootTables.apply(vk);
    }
    
    public LootItemCondition getCondition(final ResourceLocation vk) {
        return (LootItemCondition)this.conditions.apply(vk);
    }
    
    public Random getRandom() {
        return this.random;
    }
    
    public float getLuck() {
        return this.luck;
    }
    
    public ServerLevel getLevel() {
        return this.level;
    }
    
    public static class Builder {
        private final ServerLevel level;
        private final Map<LootContextParam<?>, Object> params;
        private final Map<ResourceLocation, DynamicDrop> dynamicDrops;
        private Random random;
        private float luck;
        
        public Builder(final ServerLevel aag) {
            this.params = (Map<LootContextParam<?>, Object>)Maps.newIdentityHashMap();
            this.dynamicDrops = (Map<ResourceLocation, DynamicDrop>)Maps.newHashMap();
            this.level = aag;
        }
        
        public Builder withRandom(final Random random) {
            this.random = random;
            return this;
        }
        
        public Builder withOptionalRandomSeed(final long long1) {
            if (long1 != 0L) {
                this.random = new Random(long1);
            }
            return this;
        }
        
        public Builder withOptionalRandomSeed(final long long1, final Random random) {
            if (long1 == 0L) {
                this.random = random;
            }
            else {
                this.random = new Random(long1);
            }
            return this;
        }
        
        public Builder withLuck(final float float1) {
            this.luck = float1;
            return this;
        }
        
        public <T> Builder withParameter(final LootContextParam<T> daw, final T object) {
            this.params.put(daw, object);
            return this;
        }
        
        public <T> Builder withOptionalParameter(final LootContextParam<T> daw, @Nullable final T object) {
            if (object == null) {
                this.params.remove(daw);
            }
            else {
                this.params.put(daw, object);
            }
            return this;
        }
        
        public Builder withDynamicDrop(final ResourceLocation vk, final DynamicDrop b) {
            final DynamicDrop b2 = (DynamicDrop)this.dynamicDrops.put(vk, b);
            if (b2 != null) {
                throw new IllegalStateException(new StringBuilder().append("Duplicated dynamic drop '").append(this.dynamicDrops).append("'").toString());
            }
            return this;
        }
        
        public ServerLevel getLevel() {
            return this.level;
        }
        
        public <T> T getParameter(final LootContextParam<T> daw) {
            final T object3 = (T)this.params.get(daw);
            if (object3 == null) {
                throw new IllegalArgumentException(new StringBuilder().append("No parameter ").append(daw).toString());
            }
            return object3;
        }
        
        @Nullable
        public <T> T getOptionalParameter(final LootContextParam<T> daw) {
            return (T)this.params.get(daw);
        }
        
        public LootContext create(final LootContextParamSet dax) {
            final Set<LootContextParam<?>> set3 = (Set<LootContextParam<?>>)Sets.difference(this.params.keySet(), (Set)dax.getAllowed());
            if (!set3.isEmpty()) {
                throw new IllegalArgumentException(new StringBuilder().append("Parameters not allowed in this parameter set: ").append(set3).toString());
            }
            final Set<LootContextParam<?>> set4 = (Set<LootContextParam<?>>)Sets.difference((Set)dax.getRequired(), this.params.keySet());
            if (!set4.isEmpty()) {
                throw new IllegalArgumentException(new StringBuilder().append("Missing required parameters: ").append(set4).toString());
            }
            Random random5 = this.random;
            if (random5 == null) {
                random5 = new Random();
            }
            final MinecraftServer minecraftServer6 = this.level.getServer();
            return new LootContext(random5, this.luck, this.level, minecraftServer6.getLootTables()::get, minecraftServer6.getPredicateManager()::get, this.params, this.dynamicDrops, null);
        }
    }
    
    public enum EntityTarget {
        THIS("this", LootContextParams.THIS_ENTITY), 
        KILLER("killer", LootContextParams.KILLER_ENTITY), 
        DIRECT_KILLER("direct_killer", LootContextParams.DIRECT_KILLER_ENTITY), 
        KILLER_PLAYER("killer_player", LootContextParams.LAST_DAMAGE_PLAYER);
        
        private final String name;
        private final LootContextParam<? extends Entity> param;
        
        private EntityTarget(final String string3, final LootContextParam<? extends Entity> daw) {
            this.name = string3;
            this.param = daw;
        }
        
        public LootContextParam<? extends Entity> getParam() {
            return this.param;
        }
        
        public static EntityTarget getByName(final String string) {
            for (final EntityTarget c5 : values()) {
                if (c5.name.equals(string)) {
                    return c5;
                }
            }
            throw new IllegalArgumentException("Invalid entity target " + string);
        }
        
        public static class Serializer extends TypeAdapter<EntityTarget> {
            public void write(final JsonWriter jsonWriter, final EntityTarget c) throws IOException {
                jsonWriter.value(c.name);
            }
            
            public EntityTarget read(final JsonReader jsonReader) throws IOException {
                return EntityTarget.getByName(jsonReader.nextString());
            }
        }
    }
    
    @FunctionalInterface
    public interface DynamicDrop {
        void add(final LootContext cys, final Consumer<ItemStack> consumer);
    }
}
