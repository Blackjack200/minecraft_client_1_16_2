package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import java.util.Map;
import net.minecraft.client.model.ChestedHorseModel;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class ChestedHorseRenderer<T extends AbstractChestedHorse> extends AbstractHorseRenderer<T, ChestedHorseModel<T>> {
    private static final Map<EntityType<?>, ResourceLocation> MAP;
    
    public ChestedHorseRenderer(final EntityRenderDispatcher eel, final float float2) {
        super(eel, new ChestedHorseModel(0.0f), float2);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final T bax) {
        return (ResourceLocation)ChestedHorseRenderer.MAP.get(bax.getType());
    }
    
    static {
        MAP = (Map)Maps.newHashMap((Map)ImmutableMap.of(EntityType.DONKEY, new ResourceLocation("textures/entity/horse/donkey.png"), EntityType.MULE, new ResourceLocation("textures/entity/horse/mule.png")));
    }
}
