package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HorseModel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public abstract class AbstractHorseRenderer<T extends AbstractHorse, M extends HorseModel<T>> extends MobRenderer<T, M> {
    private final float scale;
    
    public AbstractHorseRenderer(final EntityRenderDispatcher eel, final M duc, final float float3) {
        super(eel, duc, 0.75f);
        this.scale = float3;
    }
    
    @Override
    protected void scale(final T bay, final PoseStack dfj, final float float3) {
        dfj.scale(this.scale, this.scale, this.scale);
        super.scale(bay, dfj, float3);
    }
}
