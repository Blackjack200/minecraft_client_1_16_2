package net.minecraft.client.renderer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.material.FluidState;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.math.Vector3f;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.Util;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Camera;

public class FogRenderer {
    private static float fogRed;
    private static float fogGreen;
    private static float fogBlue;
    private static int targetBiomeFog;
    private static int previousBiomeFog;
    private static long biomeChangedTime;
    
    public static void setupColor(final Camera djh, final float float2, final ClientLevel dwl, final int integer, final float float5) {
        final FluidState cuu6 = djh.getFluidInCamera();
        if (cuu6.is(FluidTags.WATER)) {
            final long long7 = Util.getMillis();
            final int integer7 = dwl.getBiome(new BlockPos(djh.getPosition())).getWaterFogColor();
            if (FogRenderer.biomeChangedTime < 0L) {
                FogRenderer.targetBiomeFog = integer7;
                FogRenderer.previousBiomeFog = integer7;
                FogRenderer.biomeChangedTime = long7;
            }
            final int integer8 = FogRenderer.targetBiomeFog >> 16 & 0xFF;
            final int integer9 = FogRenderer.targetBiomeFog >> 8 & 0xFF;
            final int integer10 = FogRenderer.targetBiomeFog & 0xFF;
            final int integer11 = FogRenderer.previousBiomeFog >> 16 & 0xFF;
            final int integer12 = FogRenderer.previousBiomeFog >> 8 & 0xFF;
            final int integer13 = FogRenderer.previousBiomeFog & 0xFF;
            final float float6 = Mth.clamp((long7 - FogRenderer.biomeChangedTime) / 5000.0f, 0.0f, 1.0f);
            final float float7 = Mth.lerp(float6, (float)integer11, (float)integer8);
            final float float8 = Mth.lerp(float6, (float)integer12, (float)integer9);
            final float float9 = Mth.lerp(float6, (float)integer13, (float)integer10);
            FogRenderer.fogRed = float7 / 255.0f;
            FogRenderer.fogGreen = float8 / 255.0f;
            FogRenderer.fogBlue = float9 / 255.0f;
            if (FogRenderer.targetBiomeFog != integer7) {
                FogRenderer.targetBiomeFog = integer7;
                FogRenderer.previousBiomeFog = (Mth.floor(float7) << 16 | Mth.floor(float8) << 8 | Mth.floor(float9));
                FogRenderer.biomeChangedTime = long7;
            }
        }
        else if (cuu6.is(FluidTags.LAVA)) {
            FogRenderer.fogRed = 0.6f;
            FogRenderer.fogGreen = 0.1f;
            FogRenderer.fogBlue = 0.0f;
            FogRenderer.biomeChangedTime = -1L;
        }
        else {
            float float10 = 0.25f + 0.75f * integer / 32.0f;
            float10 = 1.0f - (float)Math.pow((double)float10, 0.25);
            final Vec3 dck8 = dwl.getSkyColor(djh.getBlockPosition(), float2);
            final float float11 = (float)dck8.x;
            final float float12 = (float)dck8.y;
            final float float13 = (float)dck8.z;
            final float float14 = Mth.clamp(Mth.cos(dwl.getTimeOfDay(float2) * 6.2831855f) * 2.0f + 0.5f, 0.0f, 1.0f);
            final BiomeManager bsu13 = dwl.getBiomeManager();
            final Vec3 dck9 = djh.getPosition().subtract(2.0, 2.0, 2.0).scale(0.25);
            final Vec3 dck10 = CubicSampler.gaussianSampleVec3(dck9, (integer4, integer5, integer6) -> dwl.effects().getBrightnessDependentFogColor(Vec3.fromRGB24(bsu13.getNoiseBiomeAtQuart(integer4, integer5, integer6).getFogColor()), float14));
            FogRenderer.fogRed = (float)dck10.x();
            FogRenderer.fogGreen = (float)dck10.y();
            FogRenderer.fogBlue = (float)dck10.z();
            if (integer >= 4) {
                final float float6 = (Mth.sin(dwl.getSunAngle(float2)) > 0.0f) ? -1.0f : 1.0f;
                final Vector3f g17 = new Vector3f(float6, 0.0f, 0.0f);
                float float8 = djh.getLookVector().dot(g17);
                if (float8 < 0.0f) {
                    float8 = 0.0f;
                }
                if (float8 > 0.0f) {
                    final float[] arr19 = dwl.effects().getSunriseColor(dwl.getTimeOfDay(float2), float2);
                    if (arr19 != null) {
                        float8 *= arr19[3];
                        FogRenderer.fogRed = FogRenderer.fogRed * (1.0f - float8) + arr19[0] * float8;
                        FogRenderer.fogGreen = FogRenderer.fogGreen * (1.0f - float8) + arr19[1] * float8;
                        FogRenderer.fogBlue = FogRenderer.fogBlue * (1.0f - float8) + arr19[2] * float8;
                    }
                }
            }
            FogRenderer.fogRed += (float11 - FogRenderer.fogRed) * float10;
            FogRenderer.fogGreen += (float12 - FogRenderer.fogGreen) * float10;
            FogRenderer.fogBlue += (float13 - FogRenderer.fogBlue) * float10;
            final float float6 = dwl.getRainLevel(float2);
            if (float6 > 0.0f) {
                final float float7 = 1.0f - float6 * 0.5f;
                final float float8 = 1.0f - float6 * 0.4f;
                FogRenderer.fogRed *= float7;
                FogRenderer.fogGreen *= float7;
                FogRenderer.fogBlue *= float8;
            }
            final float float7 = dwl.getThunderLevel(float2);
            if (float7 > 0.0f) {
                final float float8 = 1.0f - float7 * 0.5f;
                FogRenderer.fogRed *= float8;
                FogRenderer.fogGreen *= float8;
                FogRenderer.fogBlue *= float8;
            }
            FogRenderer.biomeChangedTime = -1L;
        }
        double double7 = djh.getPosition().y * dwl.getLevelData().getClearColorScale();
        if (djh.getEntity() instanceof LivingEntity && ((LivingEntity)djh.getEntity()).hasEffect(MobEffects.BLINDNESS)) {
            final int integer7 = ((LivingEntity)djh.getEntity()).getEffect(MobEffects.BLINDNESS).getDuration();
            if (integer7 < 20) {
                double7 *= 1.0f - integer7 / 20.0f;
            }
            else {
                double7 = 0.0;
            }
        }
        if (double7 < 1.0 && !cuu6.is(FluidTags.LAVA)) {
            if (double7 < 0.0) {
                double7 = 0.0;
            }
            double7 *= double7;
            FogRenderer.fogRed *= (float)double7;
            FogRenderer.fogGreen *= (float)double7;
            FogRenderer.fogBlue *= (float)double7;
        }
        if (float5 > 0.0f) {
            FogRenderer.fogRed = FogRenderer.fogRed * (1.0f - float5) + FogRenderer.fogRed * 0.7f * float5;
            FogRenderer.fogGreen = FogRenderer.fogGreen * (1.0f - float5) + FogRenderer.fogGreen * 0.6f * float5;
            FogRenderer.fogBlue = FogRenderer.fogBlue * (1.0f - float5) + FogRenderer.fogBlue * 0.6f * float5;
        }
        if (cuu6.is(FluidTags.WATER)) {
            float float11 = 0.0f;
            if (djh.getEntity() instanceof LocalPlayer) {
                final LocalPlayer dze10 = (LocalPlayer)djh.getEntity();
                float11 = dze10.getWaterVision();
            }
            final float float12 = Math.min(1.0f / FogRenderer.fogRed, Math.min(1.0f / FogRenderer.fogGreen, 1.0f / FogRenderer.fogBlue));
            FogRenderer.fogRed = FogRenderer.fogRed * (1.0f - float11) + FogRenderer.fogRed * float12 * float11;
            FogRenderer.fogGreen = FogRenderer.fogGreen * (1.0f - float11) + FogRenderer.fogGreen * float12 * float11;
            FogRenderer.fogBlue = FogRenderer.fogBlue * (1.0f - float11) + FogRenderer.fogBlue * float12 * float11;
        }
        else if (djh.getEntity() instanceof LivingEntity && ((LivingEntity)djh.getEntity()).hasEffect(MobEffects.NIGHT_VISION)) {
            final float float11 = GameRenderer.getNightVisionScale((LivingEntity)djh.getEntity(), float2);
            final float float12 = Math.min(1.0f / FogRenderer.fogRed, Math.min(1.0f / FogRenderer.fogGreen, 1.0f / FogRenderer.fogBlue));
            FogRenderer.fogRed = FogRenderer.fogRed * (1.0f - float11) + FogRenderer.fogRed * float12 * float11;
            FogRenderer.fogGreen = FogRenderer.fogGreen * (1.0f - float11) + FogRenderer.fogGreen * float12 * float11;
            FogRenderer.fogBlue = FogRenderer.fogBlue * (1.0f - float11) + FogRenderer.fogBlue * float12 * float11;
        }
        RenderSystem.clearColor(FogRenderer.fogRed, FogRenderer.fogGreen, FogRenderer.fogBlue, 0.0f);
    }
    
    public static void setupNoFog() {
        RenderSystem.fogDensity(0.0f);
        RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
    }
    
    public static void setupFog(final Camera djh, final FogMode a, final float float3, final boolean boolean4) {
        final FluidState cuu5 = djh.getFluidInCamera();
        final Entity apx6 = djh.getEntity();
        if (cuu5.is(FluidTags.WATER)) {
            float float4 = 1.0f;
            float4 = 0.05f;
            if (apx6 instanceof LocalPlayer) {
                final LocalPlayer dze8 = (LocalPlayer)apx6;
                float4 -= dze8.getWaterVision() * dze8.getWaterVision() * 0.03f;
                final Biome bss9 = dze8.level.getBiome(dze8.blockPosition());
                if (bss9.getBiomeCategory() == Biome.BiomeCategory.SWAMP) {
                    float4 += 0.005f;
                }
            }
            RenderSystem.fogDensity(float4);
            RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
        }
        else {
            float float4;
            float float5;
            if (cuu5.is(FluidTags.LAVA)) {
                if (apx6 instanceof LivingEntity && ((LivingEntity)apx6).hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    float4 = 0.0f;
                    float5 = 3.0f;
                }
                else {
                    float4 = 0.25f;
                    float5 = 1.0f;
                }
            }
            else if (apx6 instanceof LivingEntity && ((LivingEntity)apx6).hasEffect(MobEffects.BLINDNESS)) {
                final int integer9 = ((LivingEntity)apx6).getEffect(MobEffects.BLINDNESS).getDuration();
                final float float6 = Mth.lerp(Math.min(1.0f, integer9 / 20.0f), float3, 5.0f);
                if (a == FogMode.FOG_SKY) {
                    float4 = 0.0f;
                    float5 = float6 * 0.8f;
                }
                else {
                    float4 = float6 * 0.25f;
                    float5 = float6;
                }
            }
            else if (boolean4) {
                float4 = float3 * 0.05f;
                float5 = Math.min(float3, 192.0f) * 0.5f;
            }
            else if (a == FogMode.FOG_SKY) {
                float4 = 0.0f;
                float5 = float3;
            }
            else {
                float4 = float3 * 0.75f;
                float5 = float3;
            }
            RenderSystem.fogStart(float4);
            RenderSystem.fogEnd(float5);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
            RenderSystem.setupNvFogDistance();
        }
    }
    
    public static void levelFogColor() {
        RenderSystem.fog(2918, FogRenderer.fogRed, FogRenderer.fogGreen, FogRenderer.fogBlue, 1.0f);
    }
    
    static {
        FogRenderer.targetBiomeFog = -1;
        FogRenderer.previousBiomeFog = -1;
        FogRenderer.biomeChangedTime = -1L;
    }
    
    public enum FogMode {
        FOG_SKY, 
        FOG_TERRAIN;
    }
}
