package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WitherArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public class WitherBossRenderer extends MobRenderer<WitherBoss, WitherBossModel<WitherBoss>> {
    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION;
    private static final ResourceLocation WITHER_LOCATION;
    
    public WitherBossRenderer(final EntityRenderDispatcher eel) {
        super(eel, new WitherBossModel(0.0f), 1.0f);
        this.addLayer(new WitherArmorLayer(this));
    }
    
    protected int getBlockLightLevel(final WitherBoss bci, final BlockPos fx) {
        return 15;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final WitherBoss bci) {
        final int integer3 = bci.getInvulnerableTicks();
        if (integer3 <= 0 || (integer3 <= 80 && integer3 / 5 % 2 == 1)) {
            return WitherBossRenderer.WITHER_LOCATION;
        }
        return WitherBossRenderer.WITHER_INVULNERABLE_LOCATION;
    }
    
    @Override
    protected void scale(final WitherBoss bci, final PoseStack dfj, final float float3) {
        float float4 = 2.0f;
        final int integer6 = bci.getInvulnerableTicks();
        if (integer6 > 0) {
            float4 -= (integer6 - float3) / 220.0f * 0.5f;
        }
        dfj.scale(float4, float4, float4);
    }
    
    static {
        WITHER_INVULNERABLE_LOCATION = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
        WITHER_LOCATION = new ResourceLocation("textures/entity/wither/wither.png");
    }
}
