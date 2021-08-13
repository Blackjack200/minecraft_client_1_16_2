package net.minecraft.advancements.critereon;

import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;

public class EntityFlagsPredicate {
    public static final EntityFlagsPredicate ANY;
    @Nullable
    private final Boolean isOnFire;
    @Nullable
    private final Boolean isCrouching;
    @Nullable
    private final Boolean isSprinting;
    @Nullable
    private final Boolean isSwimming;
    @Nullable
    private final Boolean isBaby;
    
    public EntityFlagsPredicate(@Nullable final Boolean boolean1, @Nullable final Boolean boolean2, @Nullable final Boolean boolean3, @Nullable final Boolean boolean4, @Nullable final Boolean boolean5) {
        this.isOnFire = boolean1;
        this.isCrouching = boolean2;
        this.isSprinting = boolean3;
        this.isSwimming = boolean4;
        this.isBaby = boolean5;
    }
    
    public boolean matches(final Entity apx) {
        return (this.isOnFire == null || apx.isOnFire() == this.isOnFire) && (this.isCrouching == null || apx.isCrouching() == this.isCrouching) && (this.isSprinting == null || apx.isSprinting() == this.isSprinting) && (this.isSwimming == null || apx.isSwimming() == this.isSwimming) && (this.isBaby == null || !(apx instanceof LivingEntity) || ((LivingEntity)apx).isBaby() == this.isBaby);
    }
    
    @Nullable
    private static Boolean getOptionalBoolean(final JsonObject jsonObject, final String string) {
        return jsonObject.has(string) ? Boolean.valueOf(GsonHelper.getAsBoolean(jsonObject, string)) : null;
    }
    
    public static EntityFlagsPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return EntityFlagsPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "entity flags");
        final Boolean boolean3 = getOptionalBoolean(jsonObject2, "is_on_fire");
        final Boolean boolean4 = getOptionalBoolean(jsonObject2, "is_sneaking");
        final Boolean boolean5 = getOptionalBoolean(jsonObject2, "is_sprinting");
        final Boolean boolean6 = getOptionalBoolean(jsonObject2, "is_swimming");
        final Boolean boolean7 = getOptionalBoolean(jsonObject2, "is_baby");
        return new EntityFlagsPredicate(boolean3, boolean4, boolean5, boolean6, boolean7);
    }
    
    private void addOptionalBoolean(final JsonObject jsonObject, final String string, @Nullable final Boolean boolean3) {
        if (boolean3 != null) {
            jsonObject.addProperty(string, boolean3);
        }
    }
    
    public JsonElement serializeToJson() {
        if (this == EntityFlagsPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        this.addOptionalBoolean(jsonObject2, "is_on_fire", this.isOnFire);
        this.addOptionalBoolean(jsonObject2, "is_sneaking", this.isCrouching);
        this.addOptionalBoolean(jsonObject2, "is_sprinting", this.isSprinting);
        this.addOptionalBoolean(jsonObject2, "is_swimming", this.isSwimming);
        this.addOptionalBoolean(jsonObject2, "is_baby", this.isBaby);
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new Builder().build();
    }
    
    public static class Builder {
        @Nullable
        private Boolean isOnFire;
        @Nullable
        private Boolean isCrouching;
        @Nullable
        private Boolean isSprinting;
        @Nullable
        private Boolean isSwimming;
        @Nullable
        private Boolean isBaby;
        
        public static Builder flags() {
            return new Builder();
        }
        
        public Builder setOnFire(@Nullable final Boolean boolean1) {
            this.isOnFire = boolean1;
            return this;
        }
        
        public Builder setIsBaby(@Nullable final Boolean boolean1) {
            this.isBaby = boolean1;
            return this;
        }
        
        public EntityFlagsPredicate build() {
            return new EntityFlagsPredicate(this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isBaby);
        }
    }
}
