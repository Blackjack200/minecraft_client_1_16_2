package net.minecraft.world.level.storage.loot;

import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.util.Mth;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.world.Container;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.apache.logging.log4j.Logger;

public class LootTable {
    private static final Logger LOGGER;
    public static final LootTable EMPTY;
    public static final LootContextParamSet DEFAULT_PARAM_SET;
    private final LootContextParamSet paramSet;
    private final LootPool[] pools;
    private final LootItemFunction[] functions;
    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    
    private LootTable(final LootContextParamSet dax, final LootPool[] arr, final LootItemFunction[] arr) {
        this.paramSet = dax;
        this.pools = arr;
        this.functions = arr;
        this.compositeFunction = LootItemFunctions.compose(arr);
    }
    
    public static Consumer<ItemStack> createStackSplitter(final Consumer<ItemStack> consumer) {
        return (Consumer<ItemStack>)(bly -> {
            if (bly.getCount() < bly.getMaxStackSize()) {
                consumer.accept(bly);
            }
            else {
                int integer3 = bly.getCount();
                while (integer3 > 0) {
                    final ItemStack bly2 = bly.copy();
                    bly2.setCount(Math.min(bly.getMaxStackSize(), integer3));
                    integer3 -= bly2.getCount();
                    consumer.accept(bly2);
                }
            }
        });
    }
    
    public void getRandomItemsRaw(final LootContext cys, final Consumer<ItemStack> consumer) {
        if (cys.addVisitedTable(this)) {
            final Consumer<ItemStack> consumer2 = LootItemFunction.decorate(this.compositeFunction, consumer, cys);
            for (final LootPool cyu8 : this.pools) {
                cyu8.addRandomItems(consumer2, cys);
            }
            cys.removeVisitedTable(this);
        }
        else {
            LootTable.LOGGER.warn("Detected infinite loop in loot tables");
        }
    }
    
    public void getRandomItems(final LootContext cys, final Consumer<ItemStack> consumer) {
        this.getRandomItemsRaw(cys, createStackSplitter(consumer));
    }
    
    public List<ItemStack> getRandomItems(final LootContext cys) {
        final List<ItemStack> list3 = (List<ItemStack>)Lists.newArrayList();
        this.getRandomItems(cys, (Consumer<ItemStack>)list3::add);
        return list3;
    }
    
    public LootContextParamSet getParamSet() {
        return this.paramSet;
    }
    
    public void validate(final ValidationContext czd) {
        for (int integer3 = 0; integer3 < this.pools.length; ++integer3) {
            this.pools[integer3].validate(czd.forChild(new StringBuilder().append(".pools[").append(integer3).append("]").toString()));
        }
        for (int integer3 = 0; integer3 < this.functions.length; ++integer3) {
            this.functions[integer3].validate(czd.forChild(new StringBuilder().append(".functions[").append(integer3).append("]").toString()));
        }
    }
    
    public void fill(final Container aok, final LootContext cys) {
        final List<ItemStack> list4 = this.getRandomItems(cys);
        final Random random5 = cys.getRandom();
        final List<Integer> list5 = this.getAvailableSlots(aok, random5);
        this.shuffleAndSplitItems(list4, list5.size(), random5);
        for (final ItemStack bly8 : list4) {
            if (list5.isEmpty()) {
                LootTable.LOGGER.warn("Tried to over-fill a container");
                return;
            }
            if (bly8.isEmpty()) {
                aok.setItem((int)list5.remove(list5.size() - 1), ItemStack.EMPTY);
            }
            else {
                aok.setItem((int)list5.remove(list5.size() - 1), bly8);
            }
        }
    }
    
    private void shuffleAndSplitItems(final List<ItemStack> list, final int integer, final Random random) {
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayList();
        final Iterator<ItemStack> iterator6 = (Iterator<ItemStack>)list.iterator();
        while (iterator6.hasNext()) {
            final ItemStack bly7 = (ItemStack)iterator6.next();
            if (bly7.isEmpty()) {
                iterator6.remove();
            }
            else {
                if (bly7.getCount() <= 1) {
                    continue;
                }
                list2.add(bly7);
                iterator6.remove();
            }
        }
        while (integer - list.size() - list2.size() > 0 && !list2.isEmpty()) {
            final ItemStack bly8 = (ItemStack)list2.remove(Mth.nextInt(random, 0, list2.size() - 1));
            final int integer2 = Mth.nextInt(random, 1, bly8.getCount() / 2);
            final ItemStack bly9 = bly8.split(integer2);
            if (bly8.getCount() > 1 && random.nextBoolean()) {
                list2.add(bly8);
            }
            else {
                list.add(bly8);
            }
            if (bly9.getCount() > 1 && random.nextBoolean()) {
                list2.add(bly9);
            }
            else {
                list.add(bly9);
            }
        }
        list.addAll((Collection)list2);
        Collections.shuffle((List)list, random);
    }
    
    private List<Integer> getAvailableSlots(final Container aok, final Random random) {
        final List<Integer> list4 = (List<Integer>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < aok.getContainerSize(); ++integer5) {
            if (aok.getItem(integer5).isEmpty()) {
                list4.add(integer5);
            }
        }
        Collections.shuffle((List)list4, random);
        return list4;
    }
    
    public static Builder lootTable() {
        return new Builder();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY = new LootTable(LootContextParamSets.EMPTY, new LootPool[0], new LootItemFunction[0]);
        DEFAULT_PARAM_SET = LootContextParamSets.ALL_PARAMS;
    }
    
    public static class Builder implements FunctionUserBuilder<Builder> {
        private final List<LootPool> pools;
        private final List<LootItemFunction> functions;
        private LootContextParamSet paramSet;
        
        public Builder() {
            this.pools = (List<LootPool>)Lists.newArrayList();
            this.functions = (List<LootItemFunction>)Lists.newArrayList();
            this.paramSet = LootTable.DEFAULT_PARAM_SET;
        }
        
        public Builder withPool(final LootPool.Builder a) {
            this.pools.add(a.build());
            return this;
        }
        
        public Builder setParamSet(final LootContextParamSet dax) {
            this.paramSet = dax;
            return this;
        }
        
        public Builder apply(final LootItemFunction.Builder a) {
            this.functions.add(a.build());
            return this;
        }
        
        public Builder unwrap() {
            return this;
        }
        
        public LootTable build() {
            return new LootTable(this.paramSet, (LootPool[])this.pools.toArray((Object[])new LootPool[0]), (LootItemFunction[])this.functions.toArray((Object[])new LootItemFunction[0]), null);
        }
    }
    
    public static class Serializer implements JsonDeserializer<LootTable>, JsonSerializer<LootTable> {
        public LootTable deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "loot table");
            final LootPool[] arr6 = GsonHelper.<LootPool[]>getAsObject(jsonObject5, "pools", new LootPool[0], jsonDeserializationContext, (java.lang.Class<? extends LootPool[]>)LootPool[].class);
            LootContextParamSet dax7 = null;
            if (jsonObject5.has("type")) {
                final String string8 = GsonHelper.getAsString(jsonObject5, "type");
                dax7 = LootContextParamSets.get(new ResourceLocation(string8));
            }
            final LootItemFunction[] arr7 = GsonHelper.<LootItemFunction[]>getAsObject(jsonObject5, "functions", new LootItemFunction[0], jsonDeserializationContext, (java.lang.Class<? extends LootItemFunction[]>)LootItemFunction[].class);
            return new LootTable((dax7 != null) ? dax7 : LootContextParamSets.ALL_PARAMS, arr6, arr7, null);
        }
        
        public JsonElement serialize(final LootTable cyv, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            if (cyv.paramSet != LootTable.DEFAULT_PARAM_SET) {
                final ResourceLocation vk6 = LootContextParamSets.getKey(cyv.paramSet);
                if (vk6 != null) {
                    jsonObject5.addProperty("type", vk6.toString());
                }
                else {
                    LootTable.LOGGER.warn(new StringBuilder().append("Failed to find id for param set ").append(cyv.paramSet).toString());
                }
            }
            if (cyv.pools.length > 0) {
                jsonObject5.add("pools", jsonSerializationContext.serialize(cyv.pools));
            }
            if (!ArrayUtils.isEmpty((Object[])cyv.functions)) {
                jsonObject5.add("functions", jsonSerializationContext.serialize(cyv.functions));
            }
            return (JsonElement)jsonObject5;
        }
    }
}
