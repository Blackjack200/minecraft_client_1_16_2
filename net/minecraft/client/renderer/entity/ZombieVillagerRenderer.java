package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.world.entity.monster.ZombieVillager;

public class ZombieVillagerRenderer extends HumanoidMobRenderer<ZombieVillager, ZombieVillagerModel<ZombieVillager>> {
    private static final ResourceLocation ZOMBIE_VILLAGER_LOCATION;
    
    public ZombieVillagerRenderer(final EntityRenderDispatcher eel, final ReloadableResourceManager acd) {
        super(eel, new ZombieVillagerModel(0.0f, false), 0.5f);
        this.addLayer((RenderLayer<ZombieVillager, ZombieVillagerModel<ZombieVillager>>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, new ZombieVillagerModel(0.5f, true), new ZombieVillagerModel(1.0f, true)));
        this.addLayer(new VillagerProfessionLayer<ZombieVillager, ZombieVillagerModel<ZombieVillager>>((RenderLayerParent<ZombieVillager, ZombieVillagerModel<ZombieVillager>>)this, acd, "zombie_villager"));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ZombieVillager beh) {
        return ZombieVillagerRenderer.ZOMBIE_VILLAGER_LOCATION;
    }
    
    @Override
    protected boolean isShaking(final ZombieVillager beh) {
        return beh.isConverting();
    }
    
    static {
        ZOMBIE_VILLAGER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
    }
}
