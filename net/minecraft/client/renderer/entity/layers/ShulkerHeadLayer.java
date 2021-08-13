package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import com.mojang.math.Quaternion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ShulkerRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.world.entity.monster.Shulker;

public class ShulkerHeadLayer extends RenderLayer<Shulker, ShulkerModel<Shulker>> {
    public ShulkerHeadLayer(final RenderLayerParent<Shulker, ShulkerModel<Shulker>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Shulker bdt, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        dfj.pushPose();
        dfj.translate(0.0, 1.0, 0.0);
        dfj.scale(-1.0f, -1.0f, 1.0f);
        final Quaternion d12 = bdt.getAttachFace().getOpposite().getRotation();
        d12.conj();
        dfj.mulPose(d12);
        dfj.scale(-1.0f, -1.0f, 1.0f);
        dfj.translate(0.0, -1.0, 0.0);
        final DyeColor bku13 = bdt.getColor();
        final ResourceLocation vk14 = (bku13 == null) ? ShulkerRenderer.DEFAULT_TEXTURE_LOCATION : ShulkerRenderer.TEXTURE_LOCATION[bku13.getId()];
        final VertexConsumer dfn15 = dzy.getBuffer(RenderType.entitySolid(vk14));
        ((RenderLayer<T, ShulkerModel>)this).getParentModel().getHead().render(dfj, dfn15, integer, LivingEntityRenderer.getOverlayCoords(bdt, 0.0f));
        dfj.popPose();
    }
}
