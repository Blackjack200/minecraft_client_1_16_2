package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import java.util.function.UnaryOperator;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;

public class SetNameFunction extends LootItemConditionalFunction {
    private static final Logger LOGGER;
    private final Component name;
    @Nullable
    private final LootContext.EntityTarget resolutionContext;
    
    private SetNameFunction(final LootItemCondition[] arr, @Nullable final Component nr, @Nullable final LootContext.EntityTarget c) {
        super(arr);
        this.name = nr;
        this.resolutionContext = c;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_NAME;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)((this.resolutionContext != null) ? ImmutableSet.of(this.resolutionContext.getParam()) : ImmutableSet.of());
    }
    
    public static UnaryOperator<Component> createResolver(final LootContext cys, @Nullable final LootContext.EntityTarget c) {
        if (c != null) {
            final Entity apx3 = cys.<Entity>getParamOrNull(c.getParam());
            if (apx3 != null) {
                final CommandSourceStack db4 = apx3.createCommandSourceStack().withPermission(2);
                return (UnaryOperator<Component>)(nr -> {
                    try {
                        return ComponentUtils.updateForEntity(db4, nr, apx3, 0);
                    }
                    catch (CommandSyntaxException commandSyntaxException4) {
                        SetNameFunction.LOGGER.warn("Failed to resolve text component", (Throwable)commandSyntaxException4);
                        return nr;
                    }
                });
            }
        }
        return (UnaryOperator<Component>)(nr -> nr);
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (this.name != null) {
            bly.setHoverName((Component)createResolver(cys, this.resolutionContext).apply(this.name));
        }
        return bly;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetNameFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetNameFunction daq, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, daq, jsonSerializationContext);
            if (daq.name != null) {
                jsonObject.add("name", Component.Serializer.toJsonTree(daq.name));
            }
            if (daq.resolutionContext != null) {
                jsonObject.add("entity", jsonSerializationContext.serialize(daq.resolutionContext));
            }
        }
        
        @Override
        public SetNameFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final Component nr5 = Component.Serializer.fromJson(jsonObject.get("name"));
            final LootContext.EntityTarget c6 = GsonHelper.<LootContext.EntityTarget>getAsObject(jsonObject, "entity", (LootContext.EntityTarget)null, jsonDeserializationContext, (java.lang.Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class);
            return new SetNameFunction(arr, nr5, c6, null);
        }
    }
}
