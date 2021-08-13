package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.gson.JsonSyntaxException;
import com.google.common.collect.Lists;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.Util;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.UUID;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.List;

public class SetAttributesFunction extends LootItemConditionalFunction {
    private final List<Modifier> modifiers;
    
    private SetAttributesFunction(final LootItemCondition[] arr, final List<Modifier> list) {
        super(arr);
        this.modifiers = (List<Modifier>)ImmutableList.copyOf((Collection)list);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_ATTRIBUTES;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final Random random4 = cys.getRandom();
        for (final Modifier b6 : this.modifiers) {
            UUID uUID7 = b6.id;
            if (uUID7 == null) {
                uUID7 = UUID.randomUUID();
            }
            final EquipmentSlot aqc8 = Util.<EquipmentSlot>getRandom(b6.slots, random4);
            bly.addAttributeModifier(b6.attribute, new AttributeModifier(uUID7, b6.name, b6.amount.getFloat(random4), b6.operation), aqc8);
        }
        return bly;
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetAttributesFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetAttributesFunction dak, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dak, jsonSerializationContext);
            final JsonArray jsonArray5 = new JsonArray();
            for (final Modifier b7 : dak.modifiers) {
                jsonArray5.add((JsonElement)b7.serialize(jsonSerializationContext));
            }
            jsonObject.add("modifiers", (JsonElement)jsonArray5);
        }
        
        @Override
        public SetAttributesFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final JsonArray jsonArray5 = GsonHelper.getAsJsonArray(jsonObject, "modifiers");
            final List<Modifier> list6 = (List<Modifier>)Lists.newArrayListWithExpectedSize(jsonArray5.size());
            for (final JsonElement jsonElement8 : jsonArray5) {
                list6.add(Modifier.deserialize(GsonHelper.convertToJsonObject(jsonElement8, "modifier"), jsonDeserializationContext));
            }
            if (list6.isEmpty()) {
                throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
            }
            return new SetAttributesFunction(arr, list6, null);
        }
    }
    
    static class Modifier {
        private final String name;
        private final Attribute attribute;
        private final AttributeModifier.Operation operation;
        private final RandomValueBounds amount;
        @Nullable
        private final UUID id;
        private final EquipmentSlot[] slots;
        
        private Modifier(final String string, final Attribute ard, final AttributeModifier.Operation a, final RandomValueBounds cza, final EquipmentSlot[] arr, @Nullable final UUID uUID) {
            this.name = string;
            this.attribute = ard;
            this.operation = a;
            this.amount = cza;
            this.id = uUID;
            this.slots = arr;
        }
        
        public JsonObject serialize(final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject3 = new JsonObject();
            jsonObject3.addProperty("name", this.name);
            jsonObject3.addProperty("attribute", Registry.ATTRIBUTE.getKey(this.attribute).toString());
            jsonObject3.addProperty("operation", operationToString(this.operation));
            jsonObject3.add("amount", jsonSerializationContext.serialize(this.amount));
            if (this.id != null) {
                jsonObject3.addProperty("id", this.id.toString());
            }
            if (this.slots.length == 1) {
                jsonObject3.addProperty("slot", this.slots[0].getName());
            }
            else {
                final JsonArray jsonArray4 = new JsonArray();
                for (final EquipmentSlot aqc8 : this.slots) {
                    jsonArray4.add((JsonElement)new JsonPrimitive(aqc8.getName()));
                }
                jsonObject3.add("slot", (JsonElement)jsonArray4);
            }
            return jsonObject3;
        }
        
        public static Modifier deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final String string3 = GsonHelper.getAsString(jsonObject, "name");
            final ResourceLocation vk4 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "attribute"));
            final Attribute ard5 = Registry.ATTRIBUTE.get(vk4);
            if (ard5 == null) {
                throw new JsonSyntaxException(new StringBuilder().append("Unknown attribute: ").append(vk4).toString());
            }
            final AttributeModifier.Operation a6 = operationFromString(GsonHelper.getAsString(jsonObject, "operation"));
            final RandomValueBounds cza7 = GsonHelper.<RandomValueBounds>getAsObject(jsonObject, "amount", jsonDeserializationContext, (java.lang.Class<? extends RandomValueBounds>)RandomValueBounds.class);
            UUID uUID9 = null;
            EquipmentSlot[] arr8;
            if (GsonHelper.isStringValue(jsonObject, "slot")) {
                arr8 = new EquipmentSlot[] { EquipmentSlot.byName(GsonHelper.getAsString(jsonObject, "slot")) };
            }
            else {
                if (!GsonHelper.isArrayNode(jsonObject, "slot")) {
                    throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
                }
                final JsonArray jsonArray10 = GsonHelper.getAsJsonArray(jsonObject, "slot");
                arr8 = new EquipmentSlot[jsonArray10.size()];
                int integer11 = 0;
                for (final JsonElement jsonElement13 : jsonArray10) {
                    arr8[integer11++] = EquipmentSlot.byName(GsonHelper.convertToString(jsonElement13, "slot"));
                }
                if (arr8.length == 0) {
                    throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
                }
            }
            if (jsonObject.has("id")) {
                final String string4 = GsonHelper.getAsString(jsonObject, "id");
                try {
                    uUID9 = UUID.fromString(string4);
                }
                catch (IllegalArgumentException illegalArgumentException11) {
                    throw new JsonSyntaxException("Invalid attribute modifier id '" + string4 + "' (must be UUID format, with dashes)");
                }
            }
            return new Modifier(string3, ard5, a6, cza7, arr8, uUID9);
        }
        
        private static String operationToString(final AttributeModifier.Operation a) {
            switch (a) {
                case ADDITION: {
                    return "addition";
                }
                case MULTIPLY_BASE: {
                    return "multiply_base";
                }
                case MULTIPLY_TOTAL: {
                    return "multiply_total";
                }
                default: {
                    throw new IllegalArgumentException(new StringBuilder().append("Unknown operation ").append(a).toString());
                }
            }
        }
        
        private static AttributeModifier.Operation operationFromString(final String string) {
            switch (string) {
                case "addition": {
                    return AttributeModifier.Operation.ADDITION;
                }
                case "multiply_base": {
                    return AttributeModifier.Operation.MULTIPLY_BASE;
                }
                case "multiply_total": {
                    return AttributeModifier.Operation.MULTIPLY_TOTAL;
                }
                default: {
                    throw new JsonSyntaxException("Unknown attribute modifier operation " + string);
                }
            }
        }
    }
}
