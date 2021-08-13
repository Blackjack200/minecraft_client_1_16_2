package net.minecraft.advancements;

import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.Date;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Maps;
import java.util.Map;

public class AdvancementProgress implements Comparable<AdvancementProgress> {
    private final Map<String, CriterionProgress> criteria;
    private String[][] requirements;
    
    public AdvancementProgress() {
        this.criteria = (Map<String, CriterionProgress>)Maps.newHashMap();
        this.requirements = new String[0][];
    }
    
    public void update(final Map<String, Criterion> map, final String[][] arr) {
        final Set<String> set4 = (Set<String>)map.keySet();
        this.criteria.entrySet().removeIf(entry -> !set4.contains(entry.getKey()));
        for (final String string6 : set4) {
            if (!this.criteria.containsKey(string6)) {
                this.criteria.put(string6, new CriterionProgress());
            }
        }
        this.requirements = arr;
    }
    
    public boolean isDone() {
        if (this.requirements.length == 0) {
            return false;
        }
        for (final String[] arr5 : this.requirements) {
            boolean boolean6 = false;
            for (final String string10 : arr5) {
                final CriterionProgress ae11 = this.getCriterion(string10);
                if (ae11 != null && ae11.isDone()) {
                    boolean6 = true;
                    break;
                }
            }
            if (!boolean6) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasProgress() {
        for (final CriterionProgress ae3 : this.criteria.values()) {
            if (ae3.isDone()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean grantProgress(final String string) {
        final CriterionProgress ae3 = (CriterionProgress)this.criteria.get(string);
        if (ae3 != null && !ae3.isDone()) {
            ae3.grant();
            return true;
        }
        return false;
    }
    
    public boolean revokeProgress(final String string) {
        final CriterionProgress ae3 = (CriterionProgress)this.criteria.get(string);
        if (ae3 != null && ae3.isDone()) {
            ae3.revoke();
            return true;
        }
        return false;
    }
    
    public String toString() {
        return new StringBuilder().append("AdvancementProgress{criteria=").append(this.criteria).append(", requirements=").append(Arrays.deepToString((Object[])this.requirements)).append('}').toString();
    }
    
    public void serializeToNetwork(final FriendlyByteBuf nf) {
        nf.writeVarInt(this.criteria.size());
        for (final Map.Entry<String, CriterionProgress> entry4 : this.criteria.entrySet()) {
            nf.writeUtf((String)entry4.getKey());
            ((CriterionProgress)entry4.getValue()).serializeToNetwork(nf);
        }
    }
    
    public static AdvancementProgress fromNetwork(final FriendlyByteBuf nf) {
        final AdvancementProgress aa2 = new AdvancementProgress();
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            aa2.criteria.put(nf.readUtf(32767), CriterionProgress.fromNetwork(nf));
        }
        return aa2;
    }
    
    @Nullable
    public CriterionProgress getCriterion(final String string) {
        return (CriterionProgress)this.criteria.get(string);
    }
    
    public float getPercent() {
        if (this.criteria.isEmpty()) {
            return 0.0f;
        }
        final float float2 = (float)this.requirements.length;
        final float float3 = (float)this.countCompletedRequirements();
        return float3 / float2;
    }
    
    @Nullable
    public String getProgressText() {
        if (this.criteria.isEmpty()) {
            return null;
        }
        final int integer2 = this.requirements.length;
        if (integer2 <= 1) {
            return null;
        }
        final int integer3 = this.countCompletedRequirements();
        return new StringBuilder().append(integer3).append("/").append(integer2).toString();
    }
    
    private int countCompletedRequirements() {
        int integer2 = 0;
        for (final String[] arr6 : this.requirements) {
            boolean boolean7 = false;
            for (final String string11 : arr6) {
                final CriterionProgress ae12 = this.getCriterion(string11);
                if (ae12 != null && ae12.isDone()) {
                    boolean7 = true;
                    break;
                }
            }
            if (boolean7) {
                ++integer2;
            }
        }
        return integer2;
    }
    
    public Iterable<String> getRemainingCriteria() {
        final List<String> list2 = (List<String>)Lists.newArrayList();
        for (final Map.Entry<String, CriterionProgress> entry4 : this.criteria.entrySet()) {
            if (!((CriterionProgress)entry4.getValue()).isDone()) {
                list2.add(entry4.getKey());
            }
        }
        return (Iterable<String>)list2;
    }
    
    public Iterable<String> getCompletedCriteria() {
        final List<String> list2 = (List<String>)Lists.newArrayList();
        for (final Map.Entry<String, CriterionProgress> entry4 : this.criteria.entrySet()) {
            if (((CriterionProgress)entry4.getValue()).isDone()) {
                list2.add(entry4.getKey());
            }
        }
        return (Iterable<String>)list2;
    }
    
    @Nullable
    public Date getFirstProgressDate() {
        Date date2 = null;
        for (final CriterionProgress ae4 : this.criteria.values()) {
            if (ae4.isDone() && (date2 == null || ae4.getObtained().before(date2))) {
                date2 = ae4.getObtained();
            }
        }
        return date2;
    }
    
    public int compareTo(final AdvancementProgress aa) {
        final Date date3 = this.getFirstProgressDate();
        final Date date4 = aa.getFirstProgressDate();
        if (date3 == null && date4 != null) {
            return 1;
        }
        if (date3 != null && date4 == null) {
            return -1;
        }
        if (date3 == null && date4 == null) {
            return 0;
        }
        return date3.compareTo(date4);
    }
    
    public static class Serializer implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress> {
        public JsonElement serialize(final AdvancementProgress aa, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            final JsonObject jsonObject6 = new JsonObject();
            for (final Map.Entry<String, CriterionProgress> entry8 : aa.criteria.entrySet()) {
                final CriterionProgress ae9 = (CriterionProgress)entry8.getValue();
                if (ae9.isDone()) {
                    jsonObject6.add((String)entry8.getKey(), ae9.serializeToJson());
                }
            }
            if (!jsonObject6.entrySet().isEmpty()) {
                jsonObject5.add("criteria", (JsonElement)jsonObject6);
            }
            jsonObject5.addProperty("done", Boolean.valueOf(aa.isDone()));
            return (JsonElement)jsonObject5;
        }
        
        public AdvancementProgress deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "advancement");
            final JsonObject jsonObject6 = GsonHelper.getAsJsonObject(jsonObject5, "criteria", new JsonObject());
            final AdvancementProgress aa7 = new AdvancementProgress();
            for (final Map.Entry<String, JsonElement> entry9 : jsonObject6.entrySet()) {
                final String string10 = (String)entry9.getKey();
                aa7.criteria.put(string10, CriterionProgress.fromJson(GsonHelper.convertToString((JsonElement)entry9.getValue(), string10)));
            }
            return aa7;
        }
    }
}
