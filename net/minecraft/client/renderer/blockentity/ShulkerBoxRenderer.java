package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class ShulkerBoxRenderer extends BlockEntityRenderer<ShulkerBoxBlockEntity> {
    private final ShulkerModel<?> model;
    
    public ShulkerBoxRenderer(final ShulkerModel<?> dvi, final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.model = dvi;
    }
    
    @Override
    public void render(final ShulkerBoxBlockEntity cdb, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        Direction gc8 = Direction.UP;
        if (cdb.hasLevel()) {
            final BlockState cee9 = cdb.getLevel().getBlockState(cdb.getBlockPos());
            if (cee9.getBlock() instanceof ShulkerBoxBlock) {
                gc8 = cee9.<Direction>getValue(ShulkerBoxBlock.FACING);
            }
        }
        final DyeColor bku10 = cdb.getColor();
        Material elj9;
        if (bku10 == null) {
            elj9 = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
        }
        else {
            elj9 = (Material)Sheets.SHULKER_TEXTURE_LOCATION.get(bku10.getId());
        }
        dfj.pushPose();
        dfj.translate(0.5, 0.5, 0.5);
        final float float3 = 0.9995f;
        dfj.scale(0.9995f, 0.9995f, 0.9995f);
        dfj.mulPose(gc8.getRotation());
        dfj.scale(1.0f, -1.0f, -1.0f);
        dfj.translate(0.0, -1.0, 0.0);
        final VertexConsumer dfn12 = elj9.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entityCutoutNoCull);
        this.model.getBase().render(dfj, dfn12, integer5, integer6);
        dfj.translate(0.0, -cdb.getProgress(float2) * 0.5f, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(270.0f * cdb.getProgress(float2)));
        this.model.getLid().render(dfj, dfn12, integer5, integer6);
        dfj.popPose();
    }
}
