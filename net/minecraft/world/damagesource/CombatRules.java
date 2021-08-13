package net.minecraft.world.damagesource;

import net.minecraft.util.Mth;

public class CombatRules {
    public static float getDamageAfterAbsorb(final float float1, final float float2, final float float3) {
        final float float4 = 2.0f + float3 / 4.0f;
        final float float5 = Mth.clamp(float2 - float1 / float4, float2 * 0.2f, 20.0f);
        return float1 * (1.0f - float5 / 25.0f);
    }
    
    public static float getDamageAfterMagicAbsorb(final float float1, final float float2) {
        final float float3 = Mth.clamp(float2, 0.0f, 20.0f);
        return float1 * (1.0f - float3 / 25.0f);
    }
}
