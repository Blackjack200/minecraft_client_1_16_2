package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BookModel;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

public class LecternRenderer extends BlockEntityRenderer<LecternBlockEntity> {
    private final BookModel bookModel;
    
    public LecternRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.bookModel = new BookModel();
    }
    
    @Override
    public void render(final LecternBlockEntity ccy, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final BlockState cee8 = ccy.getBlockState();
        if (!cee8.<Boolean>getValue((Property<Boolean>)LecternBlock.HAS_BOOK)) {
            return;
        }
        dfj.pushPose();
        dfj.translate(0.5, 1.0625, 0.5);
        final float float3 = cee8.<Direction>getValue((Property<Direction>)LecternBlock.FACING).getClockWise().toYRot();
        dfj.mulPose(Vector3f.YP.rotationDegrees(-float3));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(67.5f));
        dfj.translate(0.0, -0.125, 0.0);
        this.bookModel.setupAnim(0.0f, 0.1f, 0.9f, 1.2f);
        final VertexConsumer dfn10 = EnchantTableRenderer.BOOK_LOCATION.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.bookModel.render(dfj, dfn10, integer5, integer6, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
    }
}
