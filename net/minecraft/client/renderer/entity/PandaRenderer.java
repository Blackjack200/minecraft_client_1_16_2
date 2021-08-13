package net.minecraft.client.renderer.entity;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.PandaHoldsItemLayer;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.client.model.PandaModel;
import net.minecraft.world.entity.animal.Panda;

public class PandaRenderer extends MobRenderer<Panda, PandaModel<Panda>> {
    private static final Map<Panda.Gene, ResourceLocation> TEXTURES;
    
    public PandaRenderer(final EntityRenderDispatcher eel) {
        super(eel, new PandaModel(9, 0.0f), 0.9f);
        this.addLayer(new PandaHoldsItemLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Panda bai) {
        return (ResourceLocation)PandaRenderer.TEXTURES.getOrDefault(bai.getVariant(), PandaRenderer.TEXTURES.get(Panda.Gene.NORMAL));
    }
    
    @Override
    protected void setupRotations(final Panda bai, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bai, dfj, float3, float4, float5);
        if (bai.rollCounter > 0) {
            final int integer7 = bai.rollCounter;
            final int integer8 = integer7 + 1;
            final float float6 = 7.0f;
            final float float7 = bai.isBaby() ? 0.3f : 0.8f;
            if (integer7 < 8) {
                final float float8 = 90 * integer7 / 7.0f;
                final float float9 = 90 * integer8 / 7.0f;
                final float float10 = this.getAngle(float8, float9, integer8, float5, 8.0f);
                dfj.translate(0.0, (float7 + 0.2f) * (float10 / 90.0f), 0.0);
                dfj.mulPose(Vector3f.XP.rotationDegrees(-float10));
            }
            else if (integer7 < 16) {
                final float float8 = (integer7 - 8.0f) / 7.0f;
                final float float9 = 90.0f + 90.0f * float8;
                final float float11 = 90.0f + 90.0f * (integer8 - 8.0f) / 7.0f;
                final float float10 = this.getAngle(float9, float11, integer8, float5, 16.0f);
                dfj.translate(0.0, float7 + 0.2f + (float7 - 0.2f) * (float10 - 90.0f) / 90.0f, 0.0);
                dfj.mulPose(Vector3f.XP.rotationDegrees(-float10));
            }
            else if (integer7 < 24.0f) {
                final float float8 = (integer7 - 16.0f) / 7.0f;
                final float float9 = 180.0f + 90.0f * float8;
                final float float11 = 180.0f + 90.0f * (integer8 - 16.0f) / 7.0f;
                final float float10 = this.getAngle(float9, float11, integer8, float5, 24.0f);
                dfj.translate(0.0, float7 + float7 * (270.0f - float10) / 90.0f, 0.0);
                dfj.mulPose(Vector3f.XP.rotationDegrees(-float10));
            }
            else if (integer7 < 32) {
                final float float8 = (integer7 - 24.0f) / 7.0f;
                final float float9 = 270.0f + 90.0f * float8;
                final float float11 = 270.0f + 90.0f * (integer8 - 24.0f) / 7.0f;
                final float float10 = this.getAngle(float9, float11, integer8, float5, 32.0f);
                dfj.translate(0.0, float7 * ((360.0f - float10) / 90.0f), 0.0);
                dfj.mulPose(Vector3f.XP.rotationDegrees(-float10));
            }
        }
        final float float12 = bai.getSitAmount(float5);
        if (float12 > 0.0f) {
            dfj.translate(0.0, 0.8f * float12, 0.0);
            dfj.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(float12, bai.xRot, bai.xRot + 90.0f)));
            dfj.translate(0.0, -1.0f * float12, 0.0);
            if (bai.isScared()) {
                final float float13 = (float)(Math.cos(bai.tickCount * 1.25) * 3.141592653589793 * 0.05000000074505806);
                dfj.mulPose(Vector3f.YP.rotationDegrees(float13));
                if (bai.isBaby()) {
                    dfj.translate(0.0, 0.800000011920929, 0.550000011920929);
                }
            }
        }
        final float float13 = bai.getLieOnBackAmount(float5);
        if (float13 > 0.0f) {
            final float float6 = bai.isBaby() ? 0.5f : 1.3f;
            dfj.translate(0.0, float6 * float13, 0.0);
            dfj.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(float13, bai.xRot, bai.xRot + 180.0f)));
        }
    }
    
    private float getAngle(final float float1, final float float2, final int integer, final float float4, final float float5) {
        if (integer < float5) {
            return Mth.lerp(float4, float1, float2);
        }
        return float1;
    }
    
    static {
        TEXTURES = Util.<Map>make((Map)Maps.newEnumMap((Class)Panda.Gene.class), (java.util.function.Consumer<Map>)(enumMap -> {
            enumMap.put((Enum)Panda.Gene.NORMAL, new ResourceLocation("textures/entity/panda/panda.png"));
            enumMap.put((Enum)Panda.Gene.LAZY, new ResourceLocation("textures/entity/panda/lazy_panda.png"));
            enumMap.put((Enum)Panda.Gene.WORRIED, new ResourceLocation("textures/entity/panda/worried_panda.png"));
            enumMap.put((Enum)Panda.Gene.PLAYFUL, new ResourceLocation("textures/entity/panda/playful_panda.png"));
            enumMap.put((Enum)Panda.Gene.BROWN, new ResourceLocation("textures/entity/panda/brown_panda.png"));
            enumMap.put((Enum)Panda.Gene.WEAK, new ResourceLocation("textures/entity/panda/weak_panda.png"));
            enumMap.put((Enum)Panda.Gene.AGGRESSIVE, new ResourceLocation("textures/entity/panda/aggressive_panda.png"));
        }));
    }
}
