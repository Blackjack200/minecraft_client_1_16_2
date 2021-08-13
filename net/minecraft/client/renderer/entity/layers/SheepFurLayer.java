package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SheepModel;
import net.minecraft.world.entity.animal.Sheep;

public class SheepFurLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {
    private static final ResourceLocation SHEEP_FUR_LOCATION;
    private final SheepFurModel<Sheep> model;
    
    public SheepFurLayer(final RenderLayerParent<Sheep, SheepModel<Sheep>> egc) {
        super(egc);
        this.model = new SheepFurModel<Sheep>();
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Sheep bap, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (bap.isSheared() || bap.isInvisible()) {
            return;
        }
        float float12;
        float float13;
        float float14;
        if (bap.hasCustomName() && "jeb_".equals(bap.getName().getContents())) {
            final int integer2 = 25;
            final int integer3 = bap.tickCount / 25 + bap.getId();
            final int integer4 = DyeColor.values().length;
            final int integer5 = integer3 % integer4;
            final int integer6 = (integer3 + 1) % integer4;
            final float float11 = (bap.tickCount % 25 + float7) / 25.0f;
            final float[] arr21 = Sheep.getColorArray(DyeColor.byId(integer5));
            final float[] arr22 = Sheep.getColorArray(DyeColor.byId(integer6));
            float12 = arr21[0] * (1.0f - float11) + arr22[0] * float11;
            float13 = arr21[1] * (1.0f - float11) + arr22[1] * float11;
            float14 = arr21[2] * (1.0f - float11) + arr22[2] * float11;
        }
        else {
            final float[] arr23 = Sheep.getColorArray(bap.getColor());
            float12 = arr23[0];
            float13 = arr23[1];
            float14 = arr23[2];
        }
        RenderLayer.<Sheep>coloredCutoutModelCopyLayerRender(((RenderLayer<T, EntityModel<Sheep>>)this).getParentModel(), this.model, SheepFurLayer.SHEEP_FUR_LOCATION, dfj, dzy, integer, bap, float5, float6, float8, float9, float10, float7, float12, float13, float14);
    }
    
    static {
        SHEEP_FUR_LOCATION = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    }
}
