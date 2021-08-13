package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.world.entity.monster.Creeper;

public class CreeperPowerLayer extends EnergySwirlLayer<Creeper, CreeperModel<Creeper>> {
    private static final ResourceLocation POWER_LOCATION;
    private final CreeperModel<Creeper> model;
    
    public CreeperPowerLayer(final RenderLayerParent<Creeper, CreeperModel<Creeper>> egc) {
        super(egc);
        this.model = new CreeperModel<Creeper>(2.0f);
    }
    
    @Override
    protected float xOffset(final float float1) {
        return float1 * 0.01f;
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return CreeperPowerLayer.POWER_LOCATION;
    }
    
    @Override
    protected EntityModel<Creeper> model() {
        return this.model;
    }
    
    static {
        POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }
}
