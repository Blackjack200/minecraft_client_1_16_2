package net.minecraft.advancements.critereon;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;

public class DamagePredicate {
    public static final DamagePredicate ANY;
    private final MinMaxBounds.Floats dealtDamage;
    private final MinMaxBounds.Floats takenDamage;
    private final EntityPredicate sourceEntity;
    private final Boolean blocked;
    private final DamageSourcePredicate type;
    
    public DamagePredicate() {
        this.dealtDamage = MinMaxBounds.Floats.ANY;
        this.takenDamage = MinMaxBounds.Floats.ANY;
        this.sourceEntity = EntityPredicate.ANY;
        this.blocked = null;
        this.type = DamageSourcePredicate.ANY;
    }
    
    public DamagePredicate(final MinMaxBounds.Floats c1, final MinMaxBounds.Floats c2, final EntityPredicate bg, @Nullable final Boolean boolean4, final DamageSourcePredicate aw) {
        this.dealtDamage = c1;
        this.takenDamage = c2;
        this.sourceEntity = bg;
        this.blocked = boolean4;
        this.type = aw;
    }
    
    public boolean matches(final ServerPlayer aah, final DamageSource aph, final float float3, final float float4, final boolean boolean5) {
        return this == DamagePredicate.ANY || (this.dealtDamage.matches(float3) && this.takenDamage.matches(float4) && this.sourceEntity.matches(aah, aph.getEntity()) && (this.blocked == null || this.blocked == boolean5) && this.type.matches(aah, aph));
    }
    
    public static DamagePredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return DamagePredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "damage");
        final MinMaxBounds.Floats c3 = MinMaxBounds.Floats.fromJson(jsonObject2.get("dealt"));
        final MinMaxBounds.Floats c4 = MinMaxBounds.Floats.fromJson(jsonObject2.get("taken"));
        final Boolean boolean5 = jsonObject2.has("blocked") ? Boolean.valueOf(GsonHelper.getAsBoolean(jsonObject2, "blocked")) : null;
        final EntityPredicate bg6 = EntityPredicate.fromJson(jsonObject2.get("source_entity"));
        final DamageSourcePredicate aw7 = DamageSourcePredicate.fromJson(jsonObject2.get("type"));
        return new DamagePredicate(c3, c4, bg6, boolean5, aw7);
    }
    
    public JsonElement serializeToJson() {
        if (this == DamagePredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("dealt", this.dealtDamage.serializeToJson());
        jsonObject2.add("taken", this.takenDamage.serializeToJson());
        jsonObject2.add("source_entity", this.sourceEntity.serializeToJson());
        jsonObject2.add("type", this.type.serializeToJson());
        if (this.blocked != null) {
            jsonObject2.addProperty("blocked", this.blocked);
        }
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = Builder.damageInstance().build();
    }
    
    public static class Builder {
        private MinMaxBounds.Floats dealtDamage;
        private MinMaxBounds.Floats takenDamage;
        private EntityPredicate sourceEntity;
        private Boolean blocked;
        private DamageSourcePredicate type;
        
        public Builder() {
            this.dealtDamage = MinMaxBounds.Floats.ANY;
            this.takenDamage = MinMaxBounds.Floats.ANY;
            this.sourceEntity = EntityPredicate.ANY;
            this.type = DamageSourcePredicate.ANY;
        }
        
        public static Builder damageInstance() {
            return new Builder();
        }
        
        public Builder blocked(final Boolean boolean1) {
            this.blocked = boolean1;
            return this;
        }
        
        public Builder type(final DamageSourcePredicate.Builder a) {
            this.type = a.build();
            return this;
        }
        
        public DamagePredicate build() {
            return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
        }
    }
}
