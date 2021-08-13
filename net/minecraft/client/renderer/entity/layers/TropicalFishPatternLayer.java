package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.TropicalFishModelB;
import net.minecraft.client.model.TropicalFishModelA;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.animal.TropicalFish;

public class TropicalFishPatternLayer extends RenderLayer<TropicalFish, EntityModel<TropicalFish>> {
    private final TropicalFishModelA<TropicalFish> modelA;
    private final TropicalFishModelB<TropicalFish> modelB;
    
    public TropicalFishPatternLayer(final RenderLayerParent<TropicalFish, EntityModel<TropicalFish>> egc) {
        super(egc);
        this.modelA = new TropicalFishModelA<TropicalFish>(0.008f);
        this.modelB = new TropicalFishModelB<TropicalFish>(0.008f);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final TropicalFish bat, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final EntityModel<TropicalFish> dtu12 = (EntityModel<TropicalFish>)((bat.getBaseVariant() == 0) ? this.modelA : this.modelB);
        final float[] arr13 = bat.getPatternColor();
        RenderLayer.<TropicalFish>coloredCutoutModelCopyLayerRender(((RenderLayer<T, EntityModel<TropicalFish>>)this).getParentModel(), dtu12, bat.getPatternTextureLocation(), dfj, dzy, integer, bat, float5, float6, float8, float9, float10, float7, arr13[0], arr13[1], arr13[2]);
    }
}
