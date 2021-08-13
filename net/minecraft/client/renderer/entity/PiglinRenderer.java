package net.minecraft.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import java.util.Map;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.world.entity.Mob;

public class PiglinRenderer extends HumanoidMobRenderer<Mob, PiglinModel<Mob>> {
    private static final Map<EntityType<?>, ResourceLocation> resourceLocations;
    
    public PiglinRenderer(final EntityRenderDispatcher eel, final boolean boolean2) {
        super(eel, createModel(boolean2), 0.5f, 1.0019531f, 1.0f, 1.0019531f);
        this.addLayer((RenderLayer<Mob, PiglinModel<Mob>>)new HumanoidArmorLayer((RenderLayerParent<LivingEntity, HumanoidModel>)this, new HumanoidModel(0.5f), new HumanoidModel(1.02f)));
    }
    
    private static PiglinModel<Mob> createModel(final boolean boolean1) {
        final PiglinModel<Mob> duu2 = new PiglinModel<Mob>(0.0f, 64, 64);
        if (boolean1) {
            duu2.earLeft.visible = false;
        }
        return duu2;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Mob aqk) {
        final ResourceLocation vk3 = (ResourceLocation)PiglinRenderer.resourceLocations.get(aqk.getType());
        if (vk3 == null) {
            throw new IllegalArgumentException(new StringBuilder().append("I don't know what texture to use for ").append(aqk.getType()).toString());
        }
        return vk3;
    }
    
    @Override
    protected boolean isShaking(final Mob aqk) {
        return aqk instanceof AbstractPiglin && ((AbstractPiglin)aqk).isConverting();
    }
    
    static {
        resourceLocations = (Map)ImmutableMap.of(EntityType.PIGLIN, new ResourceLocation("textures/entity/piglin/piglin.png"), EntityType.ZOMBIFIED_PIGLIN, new ResourceLocation("textures/entity/piglin/zombified_piglin.png"), EntityType.PIGLIN_BRUTE, new ResourceLocation("textures/entity/piglin/piglin_brute.png"));
    }
}
