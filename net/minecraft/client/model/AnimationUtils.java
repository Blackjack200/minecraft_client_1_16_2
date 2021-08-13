package net.minecraft.client.model;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.geom.ModelPart;

public class AnimationUtils {
    public static void animateCrossbowHold(final ModelPart dwf1, final ModelPart dwf2, final ModelPart dwf3, final boolean boolean4) {
        final ModelPart dwf4 = boolean4 ? dwf1 : dwf2;
        final ModelPart dwf5 = boolean4 ? dwf2 : dwf1;
        dwf4.yRot = (boolean4 ? -0.3f : 0.3f) + dwf3.yRot;
        dwf5.yRot = (boolean4 ? 0.6f : -0.6f) + dwf3.yRot;
        dwf4.xRot = -1.5707964f + dwf3.xRot + 0.1f;
        dwf5.xRot = -1.5f + dwf3.xRot;
    }
    
    public static void animateCrossbowCharge(final ModelPart dwf1, final ModelPart dwf2, final LivingEntity aqj, final boolean boolean4) {
        final ModelPart dwf3 = boolean4 ? dwf1 : dwf2;
        final ModelPart dwf4 = boolean4 ? dwf2 : dwf1;
        dwf3.yRot = (boolean4 ? -0.8f : 0.8f);
        dwf3.xRot = -0.97079635f;
        dwf4.xRot = dwf3.xRot;
        final float float7 = (float)CrossbowItem.getChargeDuration(aqj.getUseItem());
        final float float8 = Mth.clamp((float)aqj.getTicksUsingItem(), 0.0f, float7);
        final float float9 = float8 / float7;
        dwf4.yRot = Mth.lerp(float9, 0.4f, 0.85f) * (boolean4 ? 1 : -1);
        dwf4.xRot = Mth.lerp(float9, dwf4.xRot, -1.5707964f);
    }
    
    public static <T extends Mob> void swingWeaponDown(final ModelPart dwf1, final ModelPart dwf2, final T aqk, final float float4, final float float5) {
        final float float6 = Mth.sin(float4 * 3.1415927f);
        final float float7 = Mth.sin((1.0f - (1.0f - float4) * (1.0f - float4)) * 3.1415927f);
        dwf1.zRot = 0.0f;
        dwf2.zRot = 0.0f;
        dwf1.yRot = 0.15707964f;
        dwf2.yRot = -0.15707964f;
        if (aqk.getMainArm() == HumanoidArm.RIGHT) {
            dwf1.xRot = -1.8849558f + Mth.cos(float5 * 0.09f) * 0.15f;
            dwf2.xRot = -0.0f + Mth.cos(float5 * 0.19f) * 0.5f;
            dwf1.xRot += float6 * 2.2f - float7 * 0.4f;
            dwf2.xRot += float6 * 1.2f - float7 * 0.4f;
        }
        else {
            dwf1.xRot = -0.0f + Mth.cos(float5 * 0.19f) * 0.5f;
            dwf2.xRot = -1.8849558f + Mth.cos(float5 * 0.09f) * 0.15f;
            dwf1.xRot += float6 * 1.2f - float7 * 0.4f;
            dwf2.xRot += float6 * 2.2f - float7 * 0.4f;
        }
        bobArms(dwf1, dwf2, float5);
    }
    
    public static void bobArms(final ModelPart dwf1, final ModelPart dwf2, final float float3) {
        dwf1.zRot += Mth.cos(float3 * 0.09f) * 0.05f + 0.05f;
        dwf2.zRot -= Mth.cos(float3 * 0.09f) * 0.05f + 0.05f;
        dwf1.xRot += Mth.sin(float3 * 0.067f) * 0.05f;
        dwf2.xRot -= Mth.sin(float3 * 0.067f) * 0.05f;
    }
    
    public static void animateZombieArms(final ModelPart dwf1, final ModelPart dwf2, final boolean boolean3, final float float4, final float float5) {
        final float float6 = Mth.sin(float4 * 3.1415927f);
        final float float7 = Mth.sin((1.0f - (1.0f - float4) * (1.0f - float4)) * 3.1415927f);
        dwf2.zRot = 0.0f;
        dwf1.zRot = 0.0f;
        dwf2.yRot = -(0.1f - float6 * 0.6f);
        dwf1.yRot = 0.1f - float6 * 0.6f;
        final float float8 = -3.1415927f / (boolean3 ? 1.5f : 2.25f);
        dwf2.xRot = float8;
        dwf1.xRot = float8;
        dwf2.xRot += float6 * 1.2f - float7 * 0.4f;
        dwf1.xRot += float6 * 1.2f - float7 * 0.4f;
        bobArms(dwf2, dwf1, float5);
    }
}
