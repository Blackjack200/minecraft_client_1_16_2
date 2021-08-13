package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

public class HumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;
    private final A innerModel;
    private final A outerModel;
    
    public HumanoidArmorLayer(final RenderLayerParent<T, M> egc, final A due2, final A due3) {
        super(egc);
        this.innerModel = due2;
        this.outerModel = due3;
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        this.renderArmorPiece(dfj, dzy, aqj, EquipmentSlot.CHEST, integer, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece(dfj, dzy, aqj, EquipmentSlot.LEGS, integer, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece(dfj, dzy, aqj, EquipmentSlot.FEET, integer, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece(dfj, dzy, aqj, EquipmentSlot.HEAD, integer, this.getArmorModel(EquipmentSlot.HEAD));
    }
    
    private void renderArmorPiece(final PoseStack dfj, final MultiBufferSource dzy, final T aqj, final EquipmentSlot aqc, final int integer, final A due) {
        final ItemStack bly8 = aqj.getItemBySlot(aqc);
        if (!(bly8.getItem() instanceof ArmorItem)) {
            return;
        }
        final ArmorItem bjv9 = (ArmorItem)bly8.getItem();
        if (bjv9.getSlot() != aqc) {
            return;
        }
        this.getParentModel().copyPropertiesTo(due);
        this.setPartVisibility(due, aqc);
        final boolean boolean10 = this.usesInnerModel(aqc);
        final boolean boolean11 = bly8.hasFoil();
        if (bjv9 instanceof DyeableArmorItem) {
            final int integer2 = ((DyeableArmorItem)bjv9).getColor(bly8);
            final float float13 = (integer2 >> 16 & 0xFF) / 255.0f;
            final float float14 = (integer2 >> 8 & 0xFF) / 255.0f;
            final float float15 = (integer2 & 0xFF) / 255.0f;
            this.renderModel(dfj, dzy, integer, bjv9, boolean11, due, boolean10, float13, float14, float15, null);
            this.renderModel(dfj, dzy, integer, bjv9, boolean11, due, boolean10, 1.0f, 1.0f, 1.0f, "overlay");
        }
        else {
            this.renderModel(dfj, dzy, integer, bjv9, boolean11, due, boolean10, 1.0f, 1.0f, 1.0f, null);
        }
    }
    
    protected void setPartVisibility(final A due, final EquipmentSlot aqc) {
        due.setAllVisible(false);
        switch (aqc) {
            case HEAD: {
                due.head.visible = true;
                due.hat.visible = true;
                break;
            }
            case CHEST: {
                due.body.visible = true;
                due.rightArm.visible = true;
                due.leftArm.visible = true;
                break;
            }
            case LEGS: {
                due.body.visible = true;
                due.rightLeg.visible = true;
                due.leftLeg.visible = true;
                break;
            }
            case FEET: {
                due.rightLeg.visible = true;
                due.leftLeg.visible = true;
                break;
            }
        }
    }
    
    private void renderModel(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final ArmorItem bjv, final boolean boolean5, final A due, final boolean boolean7, final float float8, final float float9, final float float10, @Nullable final String string) {
        final VertexConsumer dfn13 = ItemRenderer.getArmorFoilBuffer(dzy, RenderType.armorCutoutNoCull(this.getArmorLocation(bjv, boolean7, string)), false, boolean5);
        due.renderToBuffer(dfj, dfn13, integer, OverlayTexture.NO_OVERLAY, float8, float9, float10, 1.0f);
    }
    
    private A getArmorModel(final EquipmentSlot aqc) {
        return this.usesInnerModel(aqc) ? this.innerModel : this.outerModel;
    }
    
    private boolean usesInnerModel(final EquipmentSlot aqc) {
        return aqc == EquipmentSlot.LEGS;
    }
    
    private ResourceLocation getArmorLocation(final ArmorItem bjv, final boolean boolean2, @Nullable final String string) {
        final String string2 = "textures/models/armor/" + bjv.getMaterial().getName() + "_layer_" + (boolean2 ? 2 : 1) + ((string == null) ? "" : ("_" + string)) + ".png";
        return (ResourceLocation)HumanoidArmorLayer.ARMOR_LOCATION_CACHE.computeIfAbsent(string2, ResourceLocation::new);
    }
    
    static {
        ARMOR_LOCATION_CACHE = (Map)Maps.newHashMap();
    }
}
