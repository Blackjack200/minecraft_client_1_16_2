package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.world.entity.monster.Slime;

public class SlimeRenderer extends MobRenderer<Slime, SlimeModel<Slime>> {
    private static final ResourceLocation SLIME_LOCATION;
    
    public SlimeRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SlimeModel(16), 0.25f);
        this.addLayer((RenderLayer<Slime, SlimeModel<Slime>>)new SlimeOuterLayer((RenderLayerParent<LivingEntity, SlimeModel<LivingEntity>>)this));
    }
    
    @Override
    public void render(final Slime bdw, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        this.shadowRadius = 0.25f * bdw.getSize();
        super.render(bdw, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    protected void scale(final Slime bdw, final PoseStack dfj, final float float3) {
        final float float4 = 0.999f;
        dfj.scale(0.999f, 0.999f, 0.999f);
        dfj.translate(0.0, 0.0010000000474974513, 0.0);
        final float float5 = (float)bdw.getSize();
        final float float6 = Mth.lerp(float3, bdw.oSquish, bdw.squish) / (float5 * 0.5f + 1.0f);
        final float float7 = 1.0f / (float6 + 1.0f);
        dfj.scale(float7 * float5, 1.0f / float7 * float5, float7 * float5);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Slime bdw) {
        return SlimeRenderer.SLIME_LOCATION;
    }
    
    static {
        SLIME_LOCATION = new ResourceLocation("textures/entity/slime/slime.png");
    }
}
