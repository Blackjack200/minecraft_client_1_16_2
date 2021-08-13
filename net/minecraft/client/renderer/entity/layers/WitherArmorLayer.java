package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public class WitherArmorLayer extends EnergySwirlLayer<WitherBoss, WitherBossModel<WitherBoss>> {
    private static final ResourceLocation WITHER_ARMOR_LOCATION;
    private final WitherBossModel<WitherBoss> model;
    
    public WitherArmorLayer(final RenderLayerParent<WitherBoss, WitherBossModel<WitherBoss>> egc) {
        super(egc);
        this.model = new WitherBossModel<WitherBoss>(0.5f);
    }
    
    @Override
    protected float xOffset(final float float1) {
        return Mth.cos(float1 * 0.02f) * 3.0f;
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return WitherArmorLayer.WITHER_ARMOR_LOCATION;
    }
    
    @Override
    protected EntityModel<WitherBoss> model() {
        return this.model;
    }
    
    static {
        WITHER_ARMOR_LOCATION = new ResourceLocation("textures/entity/wither/wither_armor.png");
    }
}
