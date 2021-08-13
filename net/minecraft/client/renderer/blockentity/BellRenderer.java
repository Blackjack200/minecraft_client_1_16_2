package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BellBlockEntity;

public class BellRenderer extends BlockEntityRenderer<BellBlockEntity> {
    public static final Material BELL_RESOURCE_LOCATION;
    private final ModelPart bellBody;
    
    public BellRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        (this.bellBody = new ModelPart(32, 32, 0, 0)).addBox(-3.0f, -6.0f, -3.0f, 6.0f, 7.0f, 6.0f);
        this.bellBody.setPos(8.0f, 12.0f, 8.0f);
        final ModelPart dwf3 = new ModelPart(32, 32, 0, 13);
        dwf3.addBox(4.0f, 4.0f, 4.0f, 8.0f, 2.0f, 8.0f);
        dwf3.setPos(-8.0f, -12.0f, -8.0f);
        this.bellBody.addChild(dwf3);
    }
    
    @Override
    public void render(final BellBlockEntity cce, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final float float3 = cce.ticks + float2;
        float float4 = 0.0f;
        float float5 = 0.0f;
        if (cce.shaking) {
            final float float6 = Mth.sin(float3 / 3.1415927f) / (4.0f + float3 / 3.0f);
            if (cce.clickDirection == Direction.NORTH) {
                float4 = -float6;
            }
            else if (cce.clickDirection == Direction.SOUTH) {
                float4 = float6;
            }
            else if (cce.clickDirection == Direction.EAST) {
                float5 = -float6;
            }
            else if (cce.clickDirection == Direction.WEST) {
                float5 = float6;
            }
        }
        this.bellBody.xRot = float4;
        this.bellBody.zRot = float5;
        final VertexConsumer dfn11 = BellRenderer.BELL_RESOURCE_LOCATION.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.bellBody.render(dfj, dfn11, integer5, integer6);
    }
    
    static {
        BELL_RESOURCE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/bell/bell_body"));
    }
}
