package net.minecraft.advancements;

import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.Maps;
import net.minecraft.network.chat.HoverEvent;
import java.util.Arrays;
import net.minecraft.ChatFormatting;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import java.util.Set;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class Advancement {
    private final Advancement parent;
    private final DisplayInfo display;
    private final AdvancementRewards rewards;
    private final ResourceLocation id;
    private final Map<String, Criterion> criteria;
    private final String[][] requirements;
    private final Set<Advancement> children;
    private final Component chatComponent;
    
    public Advancement(final ResourceLocation vk, @Nullable final Advancement y, @Nullable final DisplayInfo ah, final AdvancementRewards ab, final Map<String, Criterion> map, final String[][] arr) {
        this.children = (Set<Advancement>)Sets.newLinkedHashSet();
        this.id = vk;
        this.display = ah;
        this.criteria = (Map<String, Criterion>)ImmutableMap.copyOf((Map)map);
        this.parent = y;
        this.rewards = ab;
        this.requirements = arr;
        if (y != null) {
            y.addChild(this);
        }
        if (ah == null) {
            this.chatComponent = new TextComponent(vk.toString());
        }
        else {
            final Component nr8 = ah.getTitle();
            final ChatFormatting k9 = ah.getFrame().getChatColor();
            final Component nr9 = ComponentUtils.mergeStyles(nr8.copy(), Style.EMPTY.withColor(k9)).append("\n").append(ah.getDescription());
            final Component nr10 = nr8.copy().withStyle((UnaryOperator<Style>)(ob -> ob.withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)nr9))));
            this.chatComponent = ComponentUtils.wrapInSquareBrackets(nr10).withStyle(k9);
        }
    }
    
    public Builder deconstruct() {
        return new Builder((this.parent == null) ? null : this.parent.getId(), this.display, this.rewards, (Map)this.criteria, this.requirements);
    }
    
    @Nullable
    public Advancement getParent() {
        return this.parent;
    }
    
    @Nullable
    public DisplayInfo getDisplay() {
        return this.display;
    }
    
    public AdvancementRewards getRewards() {
        return this.rewards;
    }
    
    public String toString() {
        return new StringBuilder().append("SimpleAdvancement{id=").append(this.getId()).append(", parent=").append((this.parent == null) ? "null" : this.parent.getId()).append(", display=").append(this.display).append(", rewards=").append(this.rewards).append(", criteria=").append(this.criteria).append(", requirements=").append(Arrays.deepToString((Object[])this.requirements)).append('}').toString();
    }
    
    public Iterable<Advancement> getChildren() {
        return (Iterable<Advancement>)this.children;
    }
    
    public Map<String, Criterion> getCriteria() {
        return this.criteria;
    }
    
    public int getMaxCriteraRequired() {
        return this.requirements.length;
    }
    
    public void addChild(final Advancement y) {
        this.children.add(y);
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Advancement)) {
            return false;
        }
        final Advancement y3 = (Advancement)object;
        return this.id.equals(y3.id);
    }
    
    public int hashCode() {
        return this.id.hashCode();
    }
    
    public String[][] getRequirements() {
        return this.requirements;
    }
    
    public Component getChatComponent() {
        return this.chatComponent;
    }
    
    public static class Builder {
        private ResourceLocation parentId;
        private Advancement parent;
        private DisplayInfo display;
        private AdvancementRewards rewards;
        private Map<String, Criterion> criteria;
        private String[][] requirements;
        private RequirementsStrategy requirementsStrategy;
        
        private Builder(@Nullable final ResourceLocation vk, @Nullable final DisplayInfo ah, final AdvancementRewards ab, final Map<String, Criterion> map, final String[][] arr) {
            this.rewards = AdvancementRewards.EMPTY;
            this.criteria = (Map<String, Criterion>)Maps.newLinkedHashMap();
            this.requirementsStrategy = RequirementsStrategy.AND;
            this.parentId = vk;
            this.display = ah;
            this.rewards = ab;
            this.criteria = map;
            this.requirements = arr;
        }
        
        private Builder() {
            this.rewards = AdvancementRewards.EMPTY;
            this.criteria = (Map<String, Criterion>)Maps.newLinkedHashMap();
            this.requirementsStrategy = RequirementsStrategy.AND;
        }
        
        public static Builder advancement() {
            return new Builder();
        }
        
        public Builder parent(final Advancement y) {
            this.parent = y;
            return this;
        }
        
        public Builder parent(final ResourceLocation vk) {
            this.parentId = vk;
            return this;
        }
        
        public Builder display(final ItemStack bly, final Component nr2, final Component nr3, @Nullable final ResourceLocation vk, final FrameType ai, final boolean boolean6, final boolean boolean7, final boolean boolean8) {
            return this.display(new DisplayInfo(bly, nr2, nr3, vk, ai, boolean6, boolean7, boolean8));
        }
        
        public Builder display(final ItemLike brt, final Component nr2, final Component nr3, @Nullable final ResourceLocation vk, final FrameType ai, final boolean boolean6, final boolean boolean7, final boolean boolean8) {
            return this.display(new DisplayInfo(new ItemStack(brt.asItem()), nr2, nr3, vk, ai, boolean6, boolean7, boolean8));
        }
        
        public Builder display(final DisplayInfo ah) {
            this.display = ah;
            return this;
        }
        
        public Builder rewards(final AdvancementRewards.Builder a) {
            return this.rewards(a.build());
        }
        
        public Builder rewards(final AdvancementRewards ab) {
            this.rewards = ab;
            return this;
        }
        
        public Builder addCriterion(final String string, final CriterionTriggerInstance ag) {
            return this.addCriterion(string, new Criterion(ag));
        }
        
        public Builder addCriterion(final String string, final Criterion ad) {
            if (this.criteria.containsKey(string)) {
                throw new IllegalArgumentException("Duplicate criterion " + string);
            }
            this.criteria.put(string, ad);
            return this;
        }
        
        public Builder requirements(final RequirementsStrategy aj) {
            this.requirementsStrategy = aj;
            return this;
        }
        
        public boolean canBuild(final Function<ResourceLocation, Advancement> function) {
            if (this.parentId == null) {
                return true;
            }
            if (this.parent == null) {
                this.parent = (Advancement)function.apply(this.parentId);
            }
            return this.parent != null;
        }
        
        public Advancement build(final ResourceLocation vk) {
            if (!this.canBuild((Function<ResourceLocation, Advancement>)(vk -> null))) {
                throw new IllegalStateException("Tried to build incomplete advancement!");
            }
            if (this.requirements == null) {
                this.requirements = this.requirementsStrategy.createRequirements((Collection<String>)this.criteria.keySet());
            }
            return new Advancement(vk, this.parent, this.display, this.rewards, this.criteria, this.requirements);
        }
        
        public Advancement save(final Consumer<Advancement> consumer, final String string) {
            final Advancement y4 = this.build(new ResourceLocation(string));
            consumer.accept(y4);
            return y4;
        }
        
        public JsonObject serializeToJson() {
            if (this.requirements == null) {
                this.requirements = this.requirementsStrategy.createRequirements((Collection<String>)this.criteria.keySet());
            }
            final JsonObject jsonObject2 = new JsonObject();
            if (this.parent != null) {
                jsonObject2.addProperty("parent", this.parent.getId().toString());
            }
            else if (this.parentId != null) {
                jsonObject2.addProperty("parent", this.parentId.toString());
            }
            if (this.display != null) {
                jsonObject2.add("display", this.display.serializeToJson());
            }
            jsonObject2.add("rewards", this.rewards.serializeToJson());
            final JsonObject jsonObject3 = new JsonObject();
            for (final Map.Entry<String, Criterion> entry5 : this.criteria.entrySet()) {
                jsonObject3.add((String)entry5.getKey(), ((Criterion)entry5.getValue()).serializeToJson());
            }
            jsonObject2.add("criteria", (JsonElement)jsonObject3);
            final JsonArray jsonArray4 = new JsonArray();
            for (final String[] arr8 : this.requirements) {
                final JsonArray jsonArray5 = new JsonArray();
                for (final String string13 : arr8) {
                    jsonArray5.add(string13);
                }
                jsonArray4.add((JsonElement)jsonArray5);
            }
            jsonObject2.add("requirements", (JsonElement)jsonArray4);
            return jsonObject2;
        }
        
        public void serializeToNetwork(final FriendlyByteBuf nf) {
            if (this.parentId == null) {
                nf.writeBoolean(false);
            }
            else {
                nf.writeBoolean(true);
                nf.writeResourceLocation(this.parentId);
            }
            if (this.display == null) {
                nf.writeBoolean(false);
            }
            else {
                nf.writeBoolean(true);
                this.display.serializeToNetwork(nf);
            }
            Criterion.serializeToNetwork(this.criteria, nf);
            nf.writeVarInt(this.requirements.length);
            for (final String[] arr6 : this.requirements) {
                nf.writeVarInt(arr6.length);
                for (final String string10 : arr6) {
                    nf.writeUtf(string10);
                }
            }
        }
        
        public String toString() {
            return new StringBuilder().append("Task Advancement{parentId=").append(this.parentId).append(", display=").append(this.display).append(", rewards=").append(this.rewards).append(", criteria=").append(this.criteria).append(", requirements=").append(Arrays.deepToString((Object[])this.requirements)).append('}').toString();
        }
        
        public static Builder fromJson(final JsonObject jsonObject, final DeserializationContext ax) {
            final ResourceLocation vk3 = jsonObject.has("parent") ? new ResourceLocation(GsonHelper.getAsString(jsonObject, "parent")) : null;
            final DisplayInfo ah4 = jsonObject.has("display") ? DisplayInfo.fromJson(GsonHelper.getAsJsonObject(jsonObject, "display")) : null;
            final AdvancementRewards ab5 = jsonObject.has("rewards") ? AdvancementRewards.deserialize(GsonHelper.getAsJsonObject(jsonObject, "rewards")) : AdvancementRewards.EMPTY;
            final Map<String, Criterion> map6 = Criterion.criteriaFromJson(GsonHelper.getAsJsonObject(jsonObject, "criteria"), ax);
            if (map6.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            }
            final JsonArray jsonArray7 = GsonHelper.getAsJsonArray(jsonObject, "requirements", new JsonArray());
            String[][] arr8 = new String[jsonArray7.size()][];
            for (int integer9 = 0; integer9 < jsonArray7.size(); ++integer9) {
                final JsonArray jsonArray8 = GsonHelper.convertToJsonArray(jsonArray7.get(integer9), new StringBuilder().append("requirements[").append(integer9).append("]").toString());
                arr8[integer9] = new String[jsonArray8.size()];
                for (int integer10 = 0; integer10 < jsonArray8.size(); ++integer10) {
                    arr8[integer9][integer10] = GsonHelper.convertToString(jsonArray8.get(integer10), new StringBuilder().append("requirements[").append(integer9).append("][").append(integer10).append("]").toString());
                }
            }
            if (arr8.length == 0) {
                arr8 = new String[map6.size()][];
                int integer9 = 0;
                for (final String string11 : map6.keySet()) {
                    arr8[integer9++] = new String[] { string11 };
                }
            }
            for (final String[] arr9 : arr8) {
                if (arr9.length == 0 && map6.isEmpty()) {
                    throw new JsonSyntaxException("Requirement entry cannot be empty");
                }
                for (final String string12 : arr9) {
                    if (!map6.containsKey(string12)) {
                        throw new JsonSyntaxException("Unknown required criterion '" + string12 + "'");
                    }
                }
            }
            for (final String string13 : map6.keySet()) {
                boolean boolean11 = false;
                for (final String[] arr10 : arr8) {
                    if (ArrayUtils.contains((Object[])arr10, string13)) {
                        boolean11 = true;
                        break;
                    }
                }
                if (!boolean11) {
                    throw new JsonSyntaxException("Criterion '" + string13 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
                }
            }
            return new Builder(vk3, ah4, ab5, map6, arr8);
        }
        
        public static Builder fromNetwork(final FriendlyByteBuf nf) {
            final ResourceLocation vk2 = nf.readBoolean() ? nf.readResourceLocation() : null;
            final DisplayInfo ah3 = nf.readBoolean() ? DisplayInfo.fromNetwork(nf) : null;
            final Map<String, Criterion> map4 = Criterion.criteriaFromNetwork(nf);
            final String[][] arr5 = new String[nf.readVarInt()][];
            for (int integer6 = 0; integer6 < arr5.length; ++integer6) {
                arr5[integer6] = new String[nf.readVarInt()];
                for (int integer7 = 0; integer7 < arr5[integer6].length; ++integer7) {
                    arr5[integer6][integer7] = nf.readUtf(32767);
                }
            }
            return new Builder(vk2, ah3, AdvancementRewards.EMPTY, map4, arr5);
        }
        
        public Map<String, Criterion> getCriteria() {
            return this.criteria;
        }
    }
}
