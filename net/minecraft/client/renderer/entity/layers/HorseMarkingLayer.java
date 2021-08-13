package net.minecraft.client.renderer.entity.layers;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.EnumMap;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Markings;
import java.util.Map;
import net.minecraft.client.model.HorseModel;
import net.minecraft.world.entity.animal.horse.Horse;

public class HorseMarkingLayer extends RenderLayer<Horse, HorseModel<Horse>> {
    private static final Map<Markings, ResourceLocation> LOCATION_BY_MARKINGS;
    
    public HorseMarkingLayer(final RenderLayerParent<Horse, HorseModel<Horse>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Horse bba, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final ResourceLocation vk12 = (ResourceLocation)HorseMarkingLayer.LOCATION_BY_MARKINGS.get(bba.getMarkings());
        if (vk12 == null || bba.isInvisible()) {
            return;
        }
        final VertexConsumer dfn13 = dzy.getBuffer(RenderType.entityTranslucent(vk12));
        ((RenderLayer<T, HorseModel>)this).getParentModel().renderToBuffer(dfj, dfn13, integer, LivingEntityRenderer.getOverlayCoords(bba, 0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        LOCATION_BY_MARKINGS = Util.<Map>make((Map)Maps.newEnumMap((Class)Markings.class), (java.util.function.Consumer<Map>)(enumMap -> {
            enumMap.put((Enum)Markings.NONE, null);
            enumMap.put((Enum)Markings.WHITE, new ResourceLocation("textures/entity/horse/horse_markings_white.png"));
            enumMap.put((Enum)Markings.WHITE_FIELD, new ResourceLocation("textures/entity/horse/horse_markings_whitefield.png"));
            enumMap.put((Enum)Markings.WHITE_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_whitedots.png"));
            enumMap.put((Enum)Markings.BLACK_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_blackdots.png"));
        }));
    }
}
