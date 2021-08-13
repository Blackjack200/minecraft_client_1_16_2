package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;

public class CampfireRenderer extends BlockEntityRenderer<CampfireBlockEntity> {
    public CampfireRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final CampfireBlockEntity ccj, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final Direction gc8 = ccj.getBlockState().<Direction>getValue((Property<Direction>)CampfireBlock.FACING);
        final NonNullList<ItemStack> gj9 = ccj.getItems();
        for (int integer7 = 0; integer7 < gj9.size(); ++integer7) {
            final ItemStack bly11 = gj9.get(integer7);
            if (bly11 != ItemStack.EMPTY) {
                dfj.pushPose();
                dfj.translate(0.5, 0.44921875, 0.5);
                final Direction gc9 = Direction.from2DDataValue((integer7 + gc8.get2DDataValue()) % 4);
                final float float3 = -gc9.toYRot();
                dfj.mulPose(Vector3f.YP.rotationDegrees(float3));
                dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
                dfj.translate(-0.3125, -0.3125, 0.0);
                dfj.scale(0.375f, 0.375f, 0.375f);
                Minecraft.getInstance().getItemRenderer().renderStatic(bly11, ItemTransforms.TransformType.FIXED, integer5, integer6, dfj, dzy);
                dfj.popPose();
            }
        }
    }
}
