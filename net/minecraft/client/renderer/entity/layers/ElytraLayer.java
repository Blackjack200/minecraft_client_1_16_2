package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;

public class ElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation WINGS_LOCATION;
    private final ElytraModel<T> elytraModel;
    
    public ElytraLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
        this.elytraModel = new ElytraModel<T>();
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final ItemStack bly12 = aqj.getItemBySlot(EquipmentSlot.CHEST);
        if (bly12.getItem() != Items.ELYTRA) {
            return;
        }
        ResourceLocation vk13;
        if (aqj instanceof AbstractClientPlayer) {
            final AbstractClientPlayer dzb14 = (AbstractClientPlayer)aqj;
            if (dzb14.isElytraLoaded() && dzb14.getElytraTextureLocation() != null) {
                vk13 = dzb14.getElytraTextureLocation();
            }
            else if (dzb14.isCapeLoaded() && dzb14.getCloakTextureLocation() != null && dzb14.isModelPartShown(PlayerModelPart.CAPE)) {
                vk13 = dzb14.getCloakTextureLocation();
            }
            else {
                vk13 = ElytraLayer.WINGS_LOCATION;
            }
        }
        else {
            vk13 = ElytraLayer.WINGS_LOCATION;
        }
        dfj.pushPose();
        dfj.translate(0.0, 0.0, 0.125);
        this.getParentModel().copyPropertiesTo(this.elytraModel);
        this.elytraModel.setupAnim(aqj, float5, float6, float8, float9, float10);
        final VertexConsumer dfn14 = ItemRenderer.getArmorFoilBuffer(dzy, RenderType.armorCutoutNoCull(vk13), false, bly12.hasFoil());
        this.elytraModel.renderToBuffer(dfj, dfn14, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
    }
    
    static {
        WINGS_LOCATION = new ResourceLocation("textures/entity/elytra.png");
    }
}
