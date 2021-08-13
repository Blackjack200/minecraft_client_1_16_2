package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.EntityModel;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemCrackinessLayer extends RenderLayer<IronGolem, IronGolemModel<IronGolem>> {
    private static final Map<IronGolem.Crackiness, ResourceLocation> resourceLocations;
    
    public IronGolemCrackinessLayer(final RenderLayerParent<IronGolem, IronGolemModel<IronGolem>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final IronGolem baf, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (baf.isInvisible()) {
            return;
        }
        final IronGolem.Crackiness a12 = baf.getCrackiness();
        if (a12 == IronGolem.Crackiness.NONE) {
            return;
        }
        final ResourceLocation vk13 = (ResourceLocation)IronGolemCrackinessLayer.resourceLocations.get(a12);
        RenderLayer.<IronGolem>renderColoredCutoutModel(((RenderLayer<T, EntityModel<IronGolem>>)this).getParentModel(), vk13, dfj, dzy, integer, baf, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        resourceLocations = (Map)ImmutableMap.of(IronGolem.Crackiness.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), IronGolem.Crackiness.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), IronGolem.Crackiness.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));
    }
}
