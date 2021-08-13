package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.world.entity.npc.Villager;

public class VillagerRenderer extends MobRenderer<Villager, VillagerModel<Villager>> {
    private static final ResourceLocation VILLAGER_BASE_SKIN;
    
    public VillagerRenderer(final EntityRenderDispatcher eel, final ReloadableResourceManager acd) {
        super(eel, new VillagerModel(0.0f), 0.5f);
        this.addLayer(new CustomHeadLayer<Villager, VillagerModel<Villager>>(this));
        this.addLayer(new VillagerProfessionLayer<Villager, VillagerModel<Villager>>((RenderLayerParent<Villager, VillagerModel<Villager>>)this, acd, "villager"));
        this.addLayer(new CrossedArmsItemLayer<Villager, VillagerModel<Villager>>(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Villager bfg) {
        return VillagerRenderer.VILLAGER_BASE_SKIN;
    }
    
    @Override
    protected void scale(final Villager bfg, final PoseStack dfj, final float float3) {
        float float4 = 0.9375f;
        if (bfg.isBaby()) {
            float4 *= 0.5;
            this.shadowRadius = 0.25f;
        }
        else {
            this.shadowRadius = 0.5f;
        }
        dfj.scale(float4, float4, float4);
    }
    
    static {
        VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/villager/villager.png");
    }
}
