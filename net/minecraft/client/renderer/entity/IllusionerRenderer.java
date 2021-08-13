package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Illusioner;

public class IllusionerRenderer extends IllagerRenderer<Illusioner> {
    private static final ResourceLocation ILLUSIONER;
    
    public IllusionerRenderer(final EntityRenderDispatcher eel) {
        super(eel, new IllagerModel(0.0f, 0.0f, 64, 64), 0.5f);
        this.addLayer((RenderLayer<T, IllagerModel<T>>)new ItemInHandLayer<Illusioner, IllagerModel<Illusioner>>(this) {
            @Override
            public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Illusioner bdl, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
                if (bdl.isCastingSpell() || bdl.isAggressive()) {
                    super.render(dfj, dzy, integer, bdl, float5, float6, float7, float8, float9, float10);
                }
            }
        });
        ((IllagerModel)this.model).getHat().visible = true;
    }
    
    public ResourceLocation getTextureLocation(final Illusioner bdl) {
        return IllusionerRenderer.ILLUSIONER;
    }
    
    public void render(final Illusioner bdl, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (bdl.isInvisible()) {
            final Vec3[] arr8 = bdl.getIllusionOffsets(float3);
            final float float4 = ((LivingEntityRenderer<T, M>)this).getBob((T)bdl, float3);
            for (int integer2 = 0; integer2 < arr8.length; ++integer2) {
                dfj.pushPose();
                dfj.translate(arr8[integer2].x + Mth.cos(integer2 + float4 * 0.5f) * 0.025, arr8[integer2].y + Mth.cos(integer2 + float4 * 0.75f) * 0.0125, arr8[integer2].z + Mth.cos(integer2 + float4 * 0.7f) * 0.025);
                super.render((T)bdl, float2, float3, dfj, dzy, integer);
                dfj.popPose();
            }
        }
        else {
            super.render((T)bdl, float2, float3, dfj, dzy, integer);
        }
    }
    
    protected boolean isBodyVisible(final Illusioner bdl) {
        return true;
    }
    
    static {
        ILLUSIONER = new ResourceLocation("textures/entity/illager/illusioner.png");
    }
}
