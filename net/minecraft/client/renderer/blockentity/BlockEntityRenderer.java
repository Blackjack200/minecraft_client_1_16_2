package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityRenderer<T extends BlockEntity> {
    protected final BlockEntityRenderDispatcher renderer;
    
    public BlockEntityRenderer(final BlockEntityRenderDispatcher ebv) {
        this.renderer = ebv;
    }
    
    public abstract void render(final T ccg, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6);
    
    public boolean shouldRenderOffScreen(final T ccg) {
        return false;
    }
}
