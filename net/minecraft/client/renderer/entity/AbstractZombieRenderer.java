package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.world.entity.monster.Zombie;

public abstract class AbstractZombieRenderer<T extends Zombie, M extends ZombieModel<T>> extends HumanoidMobRenderer<T, M> {
    private static final ResourceLocation ZOMBIE_LOCATION;
    
    protected AbstractZombieRenderer(final EntityRenderDispatcher eel, final M dwb2, final M dwb3, final M dwb4) {
        super(eel, dwb2, 0.5f);
        this.addLayer((RenderLayer<T, M>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, dwb3, dwb4));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Zombie beg) {
        return AbstractZombieRenderer.ZOMBIE_LOCATION;
    }
    
    @Override
    protected boolean isShaking(final T beg) {
        return beg.isUnderWaterConverting();
    }
    
    static {
        ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");
    }
}
