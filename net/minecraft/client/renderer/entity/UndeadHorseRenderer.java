package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import java.util.Map;
import net.minecraft.client.model.HorseModel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class UndeadHorseRenderer extends AbstractHorseRenderer<AbstractHorse, HorseModel<AbstractHorse>> {
    private static final Map<EntityType<?>, ResourceLocation> MAP;
    
    public UndeadHorseRenderer(final EntityRenderDispatcher eel) {
        super(eel, new HorseModel(0.0f), 1.0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final AbstractHorse bay) {
        return (ResourceLocation)UndeadHorseRenderer.MAP.get(bay.getType());
    }
    
    static {
        MAP = (Map)Maps.newHashMap((Map)ImmutableMap.of(EntityType.ZOMBIE_HORSE, new ResourceLocation("textures/entity/horse/horse_zombie.png"), EntityType.SKELETON_HORSE, new ResourceLocation("textures/entity/horse/horse_skeleton.png")));
    }
}
