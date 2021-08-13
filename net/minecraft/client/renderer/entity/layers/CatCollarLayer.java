package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.CatModel;
import net.minecraft.world.entity.animal.Cat;

public class CatCollarLayer extends RenderLayer<Cat, CatModel<Cat>> {
    private static final ResourceLocation CAT_COLLAR_LOCATION;
    private final CatModel<Cat> catModel;
    
    public CatCollarLayer(final RenderLayerParent<Cat, CatModel<Cat>> egc) {
        super(egc);
        this.catModel = new CatModel<Cat>(0.01f);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Cat azy, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!azy.isTame()) {
            return;
        }
        final float[] arr12 = azy.getCollarColor().getTextureDiffuseColors();
        RenderLayer.<Cat>coloredCutoutModelCopyLayerRender(((RenderLayer<T, EntityModel<Cat>>)this).getParentModel(), this.catModel, CatCollarLayer.CAT_COLLAR_LOCATION, dfj, dzy, integer, azy, float5, float6, float8, float9, float10, float7, arr12[0], arr12[1], arr12[2]);
    }
    
    static {
        CAT_COLLAR_LOCATION = new ResourceLocation("textures/entity/cat/cat_collar.png");
    }
}
