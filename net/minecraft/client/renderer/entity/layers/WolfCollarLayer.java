package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.WolfModel;
import net.minecraft.world.entity.animal.Wolf;

public class WolfCollarLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {
    private static final ResourceLocation WOLF_COLLAR_LOCATION;
    
    public WolfCollarLayer(final RenderLayerParent<Wolf, WolfModel<Wolf>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Wolf baw, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!baw.isTame() || baw.isInvisible()) {
            return;
        }
        final float[] arr12 = baw.getCollarColor().getTextureDiffuseColors();
        RenderLayer.<Wolf>renderColoredCutoutModel(((RenderLayer<T, EntityModel<Wolf>>)this).getParentModel(), WolfCollarLayer.WOLF_COLLAR_LOCATION, dfj, dzy, integer, baw, arr12[0], arr12[1], arr12[2]);
    }
    
    static {
        WOLF_COLLAR_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    }
}
