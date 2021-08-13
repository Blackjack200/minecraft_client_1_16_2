package net.minecraft.client.renderer.entity;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Variant;
import java.util.Map;
import net.minecraft.client.model.HorseModel;
import net.minecraft.world.entity.animal.horse.Horse;

public final class HorseRenderer extends AbstractHorseRenderer<Horse, HorseModel<Horse>> {
    private static final Map<Variant, ResourceLocation> LOCATION_BY_VARIANT;
    
    public HorseRenderer(final EntityRenderDispatcher eel) {
        super(eel, new HorseModel(0.0f), 1.1f);
        this.addLayer(new HorseMarkingLayer(this));
        this.addLayer(new HorseArmorLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Horse bba) {
        return (ResourceLocation)HorseRenderer.LOCATION_BY_VARIANT.get(bba.getVariant());
    }
    
    static {
        LOCATION_BY_VARIANT = Util.<Map>make((Map)Maps.newEnumMap((Class)Variant.class), (java.util.function.Consumer<Map>)(enumMap -> {
            enumMap.put((Enum)Variant.WHITE, new ResourceLocation("textures/entity/horse/horse_white.png"));
            enumMap.put((Enum)Variant.CREAMY, new ResourceLocation("textures/entity/horse/horse_creamy.png"));
            enumMap.put((Enum)Variant.CHESTNUT, new ResourceLocation("textures/entity/horse/horse_chestnut.png"));
            enumMap.put((Enum)Variant.BROWN, new ResourceLocation("textures/entity/horse/horse_brown.png"));
            enumMap.put((Enum)Variant.BLACK, new ResourceLocation("textures/entity/horse/horse_black.png"));
            enumMap.put((Enum)Variant.GRAY, new ResourceLocation("textures/entity/horse/horse_gray.png"));
            enumMap.put((Enum)Variant.DARKBROWN, new ResourceLocation("textures/entity/horse/horse_darkbrown.png"));
        }));
    }
}
