package net.minecraft.advancements;

import net.minecraft.advancements.critereon.SerializationContext;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.advancements.critereon.DeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public class Criterion {
    private final CriterionTriggerInstance trigger;
    
    public Criterion(final CriterionTriggerInstance ag) {
        this.trigger = ag;
    }
    
    public Criterion() {
        this.trigger = null;
    }
    
    public void serializeToNetwork(final FriendlyByteBuf nf) {
    }
    
    public static Criterion criterionFromJson(final JsonObject jsonObject, final DeserializationContext ax) {
        final ResourceLocation vk3 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "trigger"));
        final CriterionTrigger<?> af4 = CriteriaTriggers.getCriterion(vk3);
        if (af4 == null) {
            throw new JsonSyntaxException(new StringBuilder().append("Invalid criterion trigger: ").append(vk3).toString());
        }
        final CriterionTriggerInstance ag5 = (CriterionTriggerInstance)af4.createInstance(GsonHelper.getAsJsonObject(jsonObject, "conditions", new JsonObject()), ax);
        return new Criterion(ag5);
    }
    
    public static Criterion criterionFromNetwork(final FriendlyByteBuf nf) {
        return new Criterion();
    }
    
    public static Map<String, Criterion> criteriaFromJson(final JsonObject jsonObject, final DeserializationContext ax) {
        final Map<String, Criterion> map3 = (Map<String, Criterion>)Maps.newHashMap();
        for (final Map.Entry<String, JsonElement> entry5 : jsonObject.entrySet()) {
            map3.put(entry5.getKey(), criterionFromJson(GsonHelper.convertToJsonObject((JsonElement)entry5.getValue(), "criterion"), ax));
        }
        return map3;
    }
    
    public static Map<String, Criterion> criteriaFromNetwork(final FriendlyByteBuf nf) {
        final Map<String, Criterion> map2 = (Map<String, Criterion>)Maps.newHashMap();
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            map2.put(nf.readUtf(32767), criterionFromNetwork(nf));
        }
        return map2;
    }
    
    public static void serializeToNetwork(final Map<String, Criterion> map, final FriendlyByteBuf nf) {
        nf.writeVarInt(map.size());
        for (final Map.Entry<String, Criterion> entry4 : map.entrySet()) {
            nf.writeUtf((String)entry4.getKey());
            ((Criterion)entry4.getValue()).serializeToNetwork(nf);
        }
    }
    
    @Nullable
    public CriterionTriggerInstance getTrigger() {
        return this.trigger;
    }
    
    public JsonElement serializeToJson() {
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("trigger", this.trigger.getCriterion().toString());
        final JsonObject jsonObject3 = this.trigger.serializeToJson(SerializationContext.INSTANCE);
        if (jsonObject3.size() != 0) {
            jsonObject2.add("conditions", (JsonElement)jsonObject3);
        }
        return (JsonElement)jsonObject2;
    }
}
