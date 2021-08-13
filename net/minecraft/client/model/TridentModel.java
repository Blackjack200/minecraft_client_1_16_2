package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;

public class TridentModel extends Model {
    public static final ResourceLocation TEXTURE;
    private final ModelPart pole;
    
    public TridentModel() {
        super((Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        (this.pole = new ModelPart(32, 32, 0, 6)).addBox(-0.5f, 2.0f, -0.5f, 1.0f, 25.0f, 1.0f, 0.0f);
        final ModelPart dwf2 = new ModelPart(32, 32, 4, 0);
        dwf2.addBox(-1.5f, 0.0f, -0.5f, 3.0f, 2.0f, 1.0f);
        this.pole.addChild(dwf2);
        final ModelPart dwf3 = new ModelPart(32, 32, 4, 3);
        dwf3.addBox(-2.5f, -3.0f, -0.5f, 1.0f, 4.0f, 1.0f);
        this.pole.addChild(dwf3);
        final ModelPart dwf4 = new ModelPart(32, 32, 0, 0);
        dwf4.addBox(-0.5f, -4.0f, -0.5f, 1.0f, 4.0f, 1.0f, 0.0f);
        this.pole.addChild(dwf4);
        final ModelPart dwf5 = new ModelPart(32, 32, 4, 3);
        dwf5.mirror = true;
        dwf5.addBox(1.5f, -3.0f, -0.5f, 1.0f, 4.0f, 1.0f);
        this.pole.addChild(dwf5);
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        this.pole.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/entity/trident.png");
    }
}
