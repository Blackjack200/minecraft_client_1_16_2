package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.HorseModel;
import net.minecraft.world.entity.animal.horse.Horse;

public class HorseArmorLayer extends RenderLayer<Horse, HorseModel<Horse>> {
    private final HorseModel<Horse> model;
    
    public HorseArmorLayer(final RenderLayerParent<Horse, HorseModel<Horse>> egc) {
        super(egc);
        this.model = new HorseModel<Horse>(0.1f);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Horse bba, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final ItemStack bly12 = bba.getArmor();
        if (!(bly12.getItem() instanceof HorseArmorItem)) {
            return;
        }
        final HorseArmorItem blt13 = (HorseArmorItem)bly12.getItem();
        ((RenderLayer<T, HorseModel<Horse>>)this).getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(bba, float5, float6, float7);
        this.model.setupAnim(bba, float5, float6, float8, float9, float10);
        float float11;
        float float12;
        float float13;
        if (blt13 instanceof DyeableHorseArmorItem) {
            final int integer2 = ((DyeableHorseArmorItem)blt13).getColor(bly12);
            float11 = (integer2 >> 16 & 0xFF) / 255.0f;
            float12 = (integer2 >> 8 & 0xFF) / 255.0f;
            float13 = (integer2 & 0xFF) / 255.0f;
        }
        else {
            float11 = 1.0f;
            float12 = 1.0f;
            float13 = 1.0f;
        }
        final VertexConsumer dfn17 = dzy.getBuffer(RenderType.entityCutoutNoCull(blt13.getTexture()));
        this.model.renderToBuffer(dfj, dfn17, integer, OverlayTexture.NO_OVERLAY, float11, float12, float13, 1.0f);
    }
}
