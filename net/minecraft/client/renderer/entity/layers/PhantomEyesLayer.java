package net.minecraft.client.renderer.entity.layers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.world.entity.Entity;

public class PhantomEyesLayer<T extends Entity> extends EyesLayer<T, PhantomModel<T>> {
    private static final RenderType PHANTOM_EYES;
    
    public PhantomEyesLayer(final RenderLayerParent<T, PhantomModel<T>> egc) {
        super(egc);
    }
    
    @Override
    public RenderType renderType() {
        return PhantomEyesLayer.PHANTOM_EYES;
    }
    
    static {
        PHANTOM_EYES = RenderType.eyes(new ResourceLocation("textures/entity/phantom_eyes.png"));
    }
}
