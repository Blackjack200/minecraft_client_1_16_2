package net.minecraft.client.renderer.entity.layers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.world.entity.LivingEntity;

public class EnderEyesLayer<T extends LivingEntity> extends EyesLayer<T, EndermanModel<T>> {
    private static final RenderType ENDERMAN_EYES;
    
    public EnderEyesLayer(final RenderLayerParent<T, EndermanModel<T>> egc) {
        super(egc);
    }
    
    @Override
    public RenderType renderType() {
        return EnderEyesLayer.ENDERMAN_EYES;
    }
    
    static {
        ENDERMAN_EYES = RenderType.eyes(new ResourceLocation("textures/entity/enderman/enderman_eyes.png"));
    }
}
