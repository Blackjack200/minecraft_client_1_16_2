package net.minecraft.client.renderer.entity;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.HashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.client.model.CowModel;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowRenderer extends MobRenderer<MushroomCow, CowModel<MushroomCow>> {
    private static final Map<MushroomCow.MushroomType, ResourceLocation> TEXTURES;
    
    public MushroomCowRenderer(final EntityRenderDispatcher eel) {
        super(eel, new CowModel(), 0.7f);
        this.addLayer((RenderLayer<MushroomCow, CowModel<MushroomCow>>)new MushroomCowMushroomLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final MushroomCow bag) {
        return (ResourceLocation)MushroomCowRenderer.TEXTURES.get(bag.getMushroomType());
    }
    
    static {
        TEXTURES = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(MushroomCow.MushroomType.BROWN, new ResourceLocation("textures/entity/cow/brown_mooshroom.png"));
            hashMap.put(MushroomCow.MushroomType.RED, new ResourceLocation("textures/entity/cow/red_mooshroom.png"));
        }));
    }
}
