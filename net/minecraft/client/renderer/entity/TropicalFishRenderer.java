package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.model.ColorableListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.TropicalFishPatternLayer;
import net.minecraft.client.model.TropicalFishModelB;
import net.minecraft.client.model.TropicalFishModelA;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.animal.TropicalFish;

public class TropicalFishRenderer extends MobRenderer<TropicalFish, EntityModel<TropicalFish>> {
    private final TropicalFishModelA<TropicalFish> modelA;
    private final TropicalFishModelB<TropicalFish> modelB;
    
    public TropicalFishRenderer(final EntityRenderDispatcher eel) {
        super(eel, new TropicalFishModelA(0.0f), 0.15f);
        this.modelA = new TropicalFishModelA<TropicalFish>(0.0f);
        this.modelB = new TropicalFishModelB<TropicalFish>(0.0f);
        this.addLayer(new TropicalFishPatternLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final TropicalFish bat) {
        return bat.getBaseTextureLocation();
    }
    
    @Override
    public void render(final TropicalFish bat, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final ColorableListModel<TropicalFish> dtm8 = (ColorableListModel<TropicalFish>)((bat.getBaseVariant() == 0) ? this.modelA : this.modelB);
        this.model = (M)dtm8;
        final float[] arr9 = bat.getBaseColor();
        dtm8.setColor(arr9[0], arr9[1], arr9[2]);
        super.render(bat, float2, float3, dfj, dzy, integer);
        dtm8.setColor(1.0f, 1.0f, 1.0f);
    }
    
    @Override
    protected void setupRotations(final TropicalFish bat, final PoseStack dfj, final float float3, final float float4, final float float5) {
        super.setupRotations(bat, dfj, float3, float4, float5);
        final float float6 = 4.3f * Mth.sin(0.6f * float3);
        dfj.mulPose(Vector3f.YP.rotationDegrees(float6));
        if (!bat.isInWater()) {
            dfj.translate(0.20000000298023224, 0.10000000149011612, 0.0);
            dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
    }
}
