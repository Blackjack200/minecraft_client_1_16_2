package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ArmorStandRenderer extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {
    public static final ResourceLocation DEFAULT_SKIN_LOCATION;
    
    public ArmorStandRenderer(final EntityRenderDispatcher eel) {
        super(eel, new ArmorStandModel(), 0.0f);
        this.addLayer((RenderLayer<ArmorStand, ArmorStandArmorModel>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, new ArmorStandArmorModel(0.5f), new ArmorStandArmorModel(1.0f)));
        this.addLayer(new ItemInHandLayer<ArmorStand, ArmorStandArmorModel>(this));
        this.addLayer(new ElytraLayer<ArmorStand, ArmorStandArmorModel>(this));
        this.addLayer(new CustomHeadLayer<ArmorStand, ArmorStandArmorModel>(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ArmorStand bck) {
        return ArmorStandRenderer.DEFAULT_SKIN_LOCATION;
    }
    
    @Override
    protected void setupRotations(final ArmorStand bck, final PoseStack dfj, final float float3, final float float4, final float float5) {
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float4));
        final float float6 = bck.level.getGameTime() - bck.lastHit + float5;
        if (float6 < 5.0f) {
            dfj.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(float6 / 1.5f * 3.1415927f) * 3.0f));
        }
    }
    
    @Override
    protected boolean shouldShowName(final ArmorStand bck) {
        final double double3 = this.entityRenderDispatcher.distanceToSqr(bck);
        final float float5 = bck.isCrouching() ? 32.0f : 64.0f;
        return double3 < float5 * float5 && bck.isCustomNameVisible();
    }
    
    @Nullable
    @Override
    protected RenderType getRenderType(final ArmorStand bck, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        if (!bck.isMarker()) {
            return super.getRenderType(bck, boolean2, boolean3, boolean4);
        }
        final ResourceLocation vk6 = this.getTextureLocation(bck);
        if (boolean3) {
            return RenderType.entityTranslucent(vk6, false);
        }
        if (boolean2) {
            return RenderType.entityCutoutNoCull(vk6, false);
        }
        return null;
    }
    
    static {
        DEFAULT_SKIN_LOCATION = new ResourceLocation("textures/entity/armorstand/wood.png");
    }
}
