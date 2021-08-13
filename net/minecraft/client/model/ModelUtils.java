package net.minecraft.client.model;

public class ModelUtils {
    public static float rotlerpRad(final float float1, final float float2, final float float3) {
        float float4;
        for (float4 = float2 - float1; float4 < -3.1415927f; float4 += 6.2831855f) {}
        while (float4 >= 3.1415927f) {
            float4 -= 6.2831855f;
        }
        return float1 + float3 * float4;
    }
}
