package net.minecraft.world;

import net.minecraft.util.Mth;
import javax.annotation.concurrent.Immutable;

@Immutable
public class DifficultyInstance {
    private final Difficulty base;
    private final float effectiveDifficulty;
    
    public DifficultyInstance(final Difficulty aoo, final long long2, final long long3, final float float4) {
        this.base = aoo;
        this.effectiveDifficulty = this.calculateDifficulty(aoo, long2, long3, float4);
    }
    
    public Difficulty getDifficulty() {
        return this.base;
    }
    
    public float getEffectiveDifficulty() {
        return this.effectiveDifficulty;
    }
    
    public boolean isHarderThan(final float float1) {
        return this.effectiveDifficulty > float1;
    }
    
    public float getSpecialMultiplier() {
        if (this.effectiveDifficulty < 2.0f) {
            return 0.0f;
        }
        if (this.effectiveDifficulty > 4.0f) {
            return 1.0f;
        }
        return (this.effectiveDifficulty - 2.0f) / 2.0f;
    }
    
    private float calculateDifficulty(final Difficulty aoo, final long long2, final long long3, final float float4) {
        if (aoo == Difficulty.PEACEFUL) {
            return 0.0f;
        }
        final boolean boolean8 = aoo == Difficulty.HARD;
        float float5 = 0.75f;
        final float float6 = Mth.clamp((long2 - 72000.0f) / 1440000.0f, 0.0f, 1.0f) * 0.25f;
        float5 += float6;
        float float7 = 0.0f;
        float7 += Mth.clamp(long3 / 3600000.0f, 0.0f, 1.0f) * (boolean8 ? 1.0f : 0.75f);
        float7 += Mth.clamp(float4 * 0.25f, 0.0f, float6);
        if (aoo == Difficulty.EASY) {
            float7 *= 0.5f;
        }
        float5 += float7;
        return aoo.getId() * float5;
    }
}
