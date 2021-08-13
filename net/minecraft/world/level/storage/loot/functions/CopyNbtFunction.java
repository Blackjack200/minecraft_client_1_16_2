package net.minecraft.world.level.storage.loot.functions;

import java.util.Iterator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.nbt.ListTag;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.NbtPredicate;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.NbtPathArgument;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import java.util.function.Function;
import java.util.List;

public class CopyNbtFunction extends LootItemConditionalFunction {
    private final DataSource source;
    private final List<CopyOperation> operations;
    private static final Function<Entity, Tag> ENTITY_GETTER;
    private static final Function<BlockEntity, Tag> BLOCK_ENTITY_GETTER;
    
    private CopyNbtFunction(final LootItemCondition[] arr, final DataSource c, final List<CopyOperation> list) {
        super(arr);
        this.source = c;
        this.operations = (List<CopyOperation>)ImmutableList.copyOf((Collection)list);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.COPY_NBT;
    }
    
    private static NbtPathArgument.NbtPath compileNbtPath(final String string) {
        try {
            return new NbtPathArgument().parse(new StringReader(string));
        }
        catch (CommandSyntaxException commandSyntaxException2) {
            throw new IllegalArgumentException("Failed to parse path " + string, (Throwable)commandSyntaxException2);
        }
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(this.source.param);
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final Tag mt4 = (Tag)this.source.getter.apply(cys);
        if (mt4 != null) {
            this.operations.forEach(b -> b.apply((Supplier<Tag>)bly::getOrCreateTag, mt4));
        }
        return bly;
    }
    
    public static Builder copyData(final DataSource c) {
        return new Builder(c);
    }
    
    static {
        ENTITY_GETTER = NbtPredicate::getEntityTagToCompare;
        BLOCK_ENTITY_GETTER = (ccg -> ccg.save(new CompoundTag()));
    }
    
    static class CopyOperation {
        private final String sourcePathText;
        private final NbtPathArgument.NbtPath sourcePath;
        private final String targetPathText;
        private final NbtPathArgument.NbtPath targetPath;
        private final MergeStrategy op;
        
        private CopyOperation(final String string1, final String string2, final MergeStrategy d) {
            this.sourcePathText = string1;
            this.sourcePath = compileNbtPath(string1);
            this.targetPathText = string2;
            this.targetPath = compileNbtPath(string2);
            this.op = d;
        }
        
        public void apply(final Supplier<Tag> supplier, final Tag mt) {
            try {
                final List<Tag> list4 = this.sourcePath.get(mt);
                if (!list4.isEmpty()) {
                    this.op.merge((Tag)supplier.get(), this.targetPath, list4);
                }
            }
            catch (CommandSyntaxException ex) {}
        }
        
        public JsonObject toJson() {
            final JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("source", this.sourcePathText);
            jsonObject2.addProperty("target", this.targetPathText);
            jsonObject2.addProperty("op", this.op.name);
            return jsonObject2;
        }
        
        public static CopyOperation fromJson(final JsonObject jsonObject) {
            final String string2 = GsonHelper.getAsString(jsonObject, "source");
            final String string3 = GsonHelper.getAsString(jsonObject, "target");
            final MergeStrategy d4 = MergeStrategy.getByName(GsonHelper.getAsString(jsonObject, "op"));
            return new CopyOperation(string2, string3, d4);
        }
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final DataSource source;
        private final List<CopyOperation> ops;
        
        private Builder(final DataSource c) {
            this.ops = (List<CopyOperation>)Lists.newArrayList();
            this.source = c;
        }
        
        public Builder copy(final String string1, final String string2, final MergeStrategy d) {
            this.ops.add(new CopyOperation(string1, string2, d));
            return this;
        }
        
        public Builder copy(final String string1, final String string2) {
            return this.copy(string1, string2, MergeStrategy.REPLACE);
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public LootItemFunction build() {
            return new CopyNbtFunction(this.getConditions(), this.source, this.ops, null);
        }
    }
    
    public enum MergeStrategy {
        REPLACE("replace") {
            @Override
            public void merge(final Tag mt, final NbtPathArgument.NbtPath h, final List<Tag> list) throws CommandSyntaxException {
                h.set(mt, (Supplier<Tag>)(Tag)Iterables.getLast((Iterable)list)::copy);
            }
        }, 
        APPEND("append") {
            @Override
            public void merge(final Tag mt, final NbtPathArgument.NbtPath h, final List<Tag> list) throws CommandSyntaxException {
                final List<Tag> list2 = h.getOrCreate(mt, (Supplier<Tag>)ListTag::new);
                list2.forEach(mt -> {
                    if (mt instanceof ListTag) {
                        list.forEach(mt2 -> ((ListTag)mt).add(mt2.copy()));
                    }
                });
            }
        }, 
        MERGE("merge") {
            @Override
            public void merge(final Tag mt, final NbtPathArgument.NbtPath h, final List<Tag> list) throws CommandSyntaxException {
                final List<Tag> list2 = h.getOrCreate(mt, (Supplier<Tag>)CompoundTag::new);
                list2.forEach(mt -> {
                    if (mt instanceof CompoundTag) {
                        list.forEach(mt2 -> {
                            if (mt2 instanceof CompoundTag) {
                                ((CompoundTag)mt).merge((CompoundTag)mt2);
                            }
                        });
                    }
                });
            }
        };
        
        private final String name;
        
        public abstract void merge(final Tag mt, final NbtPathArgument.NbtPath h, final List<Tag> list) throws CommandSyntaxException;
        
        private MergeStrategy(final String string3) {
            this.name = string3;
        }
        
        public static MergeStrategy getByName(final String string) {
            for (final MergeStrategy d5 : values()) {
                if (d5.name.equals(string)) {
                    return d5;
                }
            }
            throw new IllegalArgumentException("Invalid merge strategy" + string);
        }
    }
    
    public enum DataSource {
        THIS("this", (LootContextParam<T>)LootContextParams.THIS_ENTITY, CopyNbtFunction.ENTITY_GETTER), 
        KILLER("killer", (LootContextParam<T>)LootContextParams.KILLER_ENTITY, CopyNbtFunction.ENTITY_GETTER), 
        KILLER_PLAYER("killer_player", (LootContextParam<T>)LootContextParams.LAST_DAMAGE_PLAYER, CopyNbtFunction.ENTITY_GETTER), 
        BLOCK_ENTITY("block_entity", (LootContextParam<T>)LootContextParams.BLOCK_ENTITY, CopyNbtFunction.BLOCK_ENTITY_GETTER);
        
        public final String name;
        public final LootContextParam<?> param;
        public final Function<LootContext, Tag> getter;
        
        private <T> DataSource(final String string3, final LootContextParam<T> daw, final Function<? super T, Tag> function) {
            this.name = string3;
            this.param = daw;
            this.getter = (Function<LootContext, Tag>)(cys -> {
                final Object object4 = cys.getParamOrNull((LootContextParam<Object>)daw);
                return (object4 != null) ? ((Tag)function.apply(object4)) : null;
            });
        }
        
        public static DataSource getByName(final String string) {
            for (final DataSource c5 : values()) {
                if (c5.name.equals(string)) {
                    return c5;
                }
            }
            throw new IllegalArgumentException("Invalid tag source " + string);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyNbtFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final CopyNbtFunction czy, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czy, jsonSerializationContext);
            jsonObject.addProperty("source", czy.source.name);
            final JsonArray jsonArray5 = new JsonArray();
            czy.operations.stream().map(CopyOperation::toJson).forEach(jsonArray5::add);
            jsonObject.add("ops", (JsonElement)jsonArray5);
        }
        
        @Override
        public CopyNbtFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final DataSource c5 = DataSource.getByName(GsonHelper.getAsString(jsonObject, "source"));
            final List<CopyOperation> list6 = (List<CopyOperation>)Lists.newArrayList();
            final JsonArray jsonArray7 = GsonHelper.getAsJsonArray(jsonObject, "ops");
            for (final JsonElement jsonElement9 : jsonArray7) {
                final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement9, "op");
                list6.add(CopyOperation.fromJson(jsonObject2));
            }
            return new CopyNbtFunction(arr, c5, list6, null);
        }
    }
}
