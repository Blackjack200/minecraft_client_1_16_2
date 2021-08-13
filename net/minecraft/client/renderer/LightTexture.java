package net.minecraft.client.renderer;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class LightTexture implements AutoCloseable {
    private final DynamicTexture lightTexture;
    private final NativeImage lightPixels;
    private final ResourceLocation lightTextureLocation;
    private boolean updateLightTexture;
    private float blockLightRedFlicker;
    private final GameRenderer renderer;
    private final Minecraft minecraft;
    
    public LightTexture(final GameRenderer dzr, final Minecraft djw) {
        this.renderer = dzr;
        this.minecraft = djw;
        this.lightTexture = new DynamicTexture(16, 16, false);
        this.lightTextureLocation = this.minecraft.getTextureManager().register("light_map", this.lightTexture);
        this.lightPixels = this.lightTexture.getPixels();
        for (int integer4 = 0; integer4 < 16; ++integer4) {
            for (int integer5 = 0; integer5 < 16; ++integer5) {
                this.lightPixels.setPixelRGBA(integer5, integer4, -1);
            }
        }
        this.lightTexture.upload();
    }
    
    public void close() {
        this.lightTexture.close();
    }
    
    public void tick() {
        this.blockLightRedFlicker += (float)((Math.random() - Math.random()) * Math.random() * Math.random() * 0.1);
        this.blockLightRedFlicker *= (float)0.9;
        this.updateLightTexture = true;
    }
    
    public void turnOffLightLayer() {
        RenderSystem.activeTexture(33986);
        RenderSystem.disableTexture();
        RenderSystem.activeTexture(33984);
    }
    
    public void turnOnLightLayer() {
        RenderSystem.activeTexture(33986);
        RenderSystem.matrixMode(5890);
        RenderSystem.loadIdentity();
        final float float2 = 0.00390625f;
        RenderSystem.scalef(0.00390625f, 0.00390625f, 0.00390625f);
        RenderSystem.translatef(8.0f, 8.0f, 8.0f);
        RenderSystem.matrixMode(5888);
        this.minecraft.getTextureManager().bind(this.lightTextureLocation);
        RenderSystem.texParameter(3553, 10241, 9729);
        RenderSystem.texParameter(3553, 10240, 9729);
        RenderSystem.texParameter(3553, 10242, 10496);
        RenderSystem.texParameter(3553, 10243, 10496);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableTexture();
        RenderSystem.activeTexture(33984);
    }
    
    public void updateLightTexture(final float float1) {
        if (!this.updateLightTexture) {
            return;
        }
        this.updateLightTexture = false;
        this.minecraft.getProfiler().push("lightTex");
        final ClientLevel dwl3 = this.minecraft.level;
        if (dwl3 == null) {
            return;
        }
        final float float2 = dwl3.getSkyDarken(1.0f);
        float float3;
        if (dwl3.getSkyFlashTime() > 0) {
            float3 = 1.0f;
        }
        else {
            float3 = float2 * 0.95f + 0.05f;
        }
        final float float4 = this.minecraft.player.getWaterVision();
        float float5;
        if (this.minecraft.player.hasEffect(MobEffects.NIGHT_VISION)) {
            float5 = GameRenderer.getNightVisionScale(this.minecraft.player, float1);
        }
        else if (float4 > 0.0f && this.minecraft.player.hasEffect(MobEffects.CONDUIT_POWER)) {
            float5 = float4;
        }
        else {
            float5 = 0.0f;
        }
        final Vector3f g8 = new Vector3f(float2, float2, 1.0f);
        g8.lerp(new Vector3f(1.0f, 1.0f, 1.0f), 0.35f);
        final float float6 = this.blockLightRedFlicker + 1.5f;
        final Vector3f g9 = new Vector3f();
        for (int integer11 = 0; integer11 < 16; ++integer11) {
            for (int integer12 = 0; integer12 < 16; ++integer12) {
                final float float7 = this.getBrightness(dwl3, integer11) * float3;
                final float float9;
                final float float8 = float9 = this.getBrightness(dwl3, integer12) * float6;
                final float float10 = float8 * ((float8 * 0.6f + 0.4f) * 0.6f + 0.4f);
                final float float11 = float8 * (float8 * float8 * 0.6f + 0.4f);
                g9.set(float9, float10, float11);
                if (dwl3.effects().forceBrightLightmap()) {
                    g9.lerp(new Vector3f(0.99f, 1.12f, 1.0f), 0.25f);
                }
                else {
                    final Vector3f g10 = g8.copy();
                    g10.mul(float7);
                    g9.add(g10);
                    g9.lerp(new Vector3f(0.75f, 0.75f, 0.75f), 0.04f);
                    if (this.renderer.getDarkenWorldAmount(float1) > 0.0f) {
                        final float float12 = this.renderer.getDarkenWorldAmount(float1);
                        final Vector3f g11 = g9.copy();
                        g11.mul(0.7f, 0.6f, 0.6f);
                        g9.lerp(g11, float12);
                    }
                }
                g9.clamp(0.0f, 1.0f);
                if (float5 > 0.0f) {
                    final float float13 = Math.max(g9.x(), Math.max(g9.y(), g9.z()));
                    if (float13 < 1.0f) {
                        final float float12 = 1.0f / float13;
                        final Vector3f g11 = g9.copy();
                        g11.mul(float12);
                        g9.lerp(g11, float5);
                    }
                }
                final float float13 = (float)this.minecraft.options.gamma;
                final Vector3f g12 = g9.copy();
                g12.map(this::notGamma);
                g9.lerp(g12, float13);
                g9.lerp(new Vector3f(0.75f, 0.75f, 0.75f), 0.04f);
                g9.clamp(0.0f, 1.0f);
                g9.mul(255.0f);
                final int integer13 = 255;
                final int integer14 = (int)g9.x();
                final int integer15 = (int)g9.y();
                final int integer16 = (int)g9.z();
                this.lightPixels.setPixelRGBA(integer12, integer11, 0xFF000000 | integer16 << 16 | integer15 << 8 | integer14);
            }
        }
        this.lightTexture.upload();
        this.minecraft.getProfiler().pop();
    }
    
    private float notGamma(final float float1) {
        final float float2 = 1.0f - float1;
        return 1.0f - float2 * float2 * float2 * float2;
    }
    
    private float getBrightness(final Level bru, final int integer) {
        return bru.dimensionType().brightness(integer);
    }
    
    public static int pack(final int integer1, final int integer2) {
        return integer1 << 4 | integer2 << 20;
    }
    
    public static int block(final int integer) {
        return integer >> 4 & 0xFFFF;
    }
    
    public static int sky(final int integer) {
        return integer >> 20 & 0xFFFF;
    }
}
