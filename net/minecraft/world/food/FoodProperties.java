package net.minecraft.world.food;

import com.google.common.collect.Lists;
import net.minecraft.world.effect.MobEffectInstance;
import com.mojang.datafixers.util.Pair;
import java.util.List;

public class FoodProperties {
    private final int nutrition;
    private final float saturationModifier;
    private final boolean isMeat;
    private final boolean canAlwaysEat;
    private final boolean fastFood;
    private final List<Pair<MobEffectInstance, Float>> effects;
    
    private FoodProperties(final int integer, final float float2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final List<Pair<MobEffectInstance, Float>> list) {
        this.nutrition = integer;
        this.saturationModifier = float2;
        this.isMeat = boolean3;
        this.canAlwaysEat = boolean4;
        this.fastFood = boolean5;
        this.effects = list;
    }
    
    public int getNutrition() {
        return this.nutrition;
    }
    
    public float getSaturationModifier() {
        return this.saturationModifier;
    }
    
    public boolean isMeat() {
        return this.isMeat;
    }
    
    public boolean canAlwaysEat() {
        return this.canAlwaysEat;
    }
    
    public boolean isFastFood() {
        return this.fastFood;
    }
    
    public List<Pair<MobEffectInstance, Float>> getEffects() {
        return this.effects;
    }
    
    public static class Builder {
        private int nutrition;
        private float saturationModifier;
        private boolean isMeat;
        private boolean canAlwaysEat;
        private boolean fastFood;
        private final List<Pair<MobEffectInstance, Float>> effects;
        
        public Builder() {
            this.effects = (List<Pair<MobEffectInstance, Float>>)Lists.newArrayList();
        }
        
        public Builder nutrition(final int integer) {
            this.nutrition = integer;
            return this;
        }
        
        public Builder saturationMod(final float float1) {
            this.saturationModifier = float1;
            return this;
        }
        
        public Builder meat() {
            this.isMeat = true;
            return this;
        }
        
        public Builder alwaysEat() {
            this.canAlwaysEat = true;
            return this;
        }
        
        public Builder fast() {
            this.fastFood = true;
            return this;
        }
        
        public Builder effect(final MobEffectInstance apr, final float float2) {
            this.effects.add(Pair.of((Object)apr, (Object)float2));
            return this;
        }
        
        public FoodProperties build() {
            return new FoodProperties(this.nutrition, this.saturationModifier, this.isMeat, this.canAlwaysEat, this.fastFood, this.effects, null);
        }
    }
}
