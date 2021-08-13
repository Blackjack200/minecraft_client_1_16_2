package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.world.entity.monster.AbstractIllager;

public abstract class IllagerRenderer<T extends AbstractIllager> extends MobRenderer<T, IllagerModel<T>> {
    protected IllagerRenderer(final EntityRenderDispatcher eel, final IllagerModel<T> duf, final float float3) {
        super(eel, duf, float3);
        this.addLayer(new CustomHeadLayer<T, IllagerModel<T>>(this));
    }
    
    @Override
    protected void scale(final T bcv, final PoseStack dfj, final float float3) {
        final float float4 = 0.9375f;
        dfj.scale(0.9375f, 0.9375f, 0.9375f);
    }
}
