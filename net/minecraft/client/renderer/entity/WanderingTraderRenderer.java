package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.world.entity.npc.WanderingTrader;

public class WanderingTraderRenderer extends MobRenderer<WanderingTrader, VillagerModel<WanderingTrader>> {
    private static final ResourceLocation VILLAGER_BASE_SKIN;
    
    public WanderingTraderRenderer(final EntityRenderDispatcher eel) {
        super(eel, new VillagerModel(0.0f), 0.5f);
        this.addLayer(new CustomHeadLayer<WanderingTrader, VillagerModel<WanderingTrader>>(this));
        this.addLayer(new CrossedArmsItemLayer<WanderingTrader, VillagerModel<WanderingTrader>>(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final WanderingTrader bfm) {
        return WanderingTraderRenderer.VILLAGER_BASE_SKIN;
    }
    
    @Override
    protected void scale(final WanderingTrader bfm, final PoseStack dfj, final float float3) {
        final float float4 = 0.9375f;
        dfj.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    static {
        VILLAGER_BASE_SKIN = new ResourceLocation("textures/entity/wandering_trader.png");
    }
}
