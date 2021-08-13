package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.WitchModel;
import net.minecraft.world.entity.monster.Witch;

public class WitchRenderer extends MobRenderer<Witch, WitchModel<Witch>> {
    private static final ResourceLocation WITCH_LOCATION;
    
    public WitchRenderer(final EntityRenderDispatcher eel) {
        super(eel, new WitchModel(0.0f), 0.5f);
        this.addLayer((RenderLayer<Witch, WitchModel<Witch>>)new WitchItemLayer((RenderLayerParent<LivingEntity, WitchModel<LivingEntity>>)this));
    }
    
    @Override
    public void render(final Witch bed, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        ((WitchModel)this.model).setHoldingItem(!bed.getMainHandItem().isEmpty());
        super.render(bed, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Witch bed) {
        return WitchRenderer.WITCH_LOCATION;
    }
    
    @Override
    protected void scale(final Witch bed, final PoseStack dfj, final float float3) {
        final float float4 = 0.9375f;
        dfj.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    static {
        WITCH_LOCATION = new ResourceLocation("textures/entity/witch.png");
    }
}
