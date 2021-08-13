package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.player.Player;

public class ParrotOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final ParrotModel model;
    
    public ParrotOnShoulderLayer(final RenderLayerParent<T, PlayerModel<T>> egc) {
        super(egc);
        this.model = new ParrotModel();
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T bft, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        this.render(dfj, dzy, integer, bft, float5, float6, float9, float10, true);
        this.render(dfj, dzy, integer, bft, float5, float6, float9, float10, false);
    }
    
    private void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T bft, final float float5, final float float6, final float float7, final float float8, final boolean boolean9) {
        final CompoundTag md11 = boolean9 ? bft.getShoulderEntityLeft() : bft.getShoulderEntityRight();
        EntityType.byString(md11.getString("id")).filter(aqb -> aqb == EntityType.PARROT).ifPresent(aqb -> {
            dfj.pushPose();
            dfj.translate(boolean9 ? 0.4000000059604645 : -0.4000000059604645, bft.isCrouching() ? -1.2999999523162842 : -1.5, 0.0);
            final VertexConsumer dfn13 = dzy.getBuffer(this.model.renderType(ParrotRenderer.PARROT_LOCATIONS[md11.getInt("Variant")]));
            this.model.renderOnShoulder(dfj, dfn13, integer, OverlayTexture.NO_OVERLAY, float5, float6, float7, float8, bft.tickCount);
            dfj.popPose();
        });
    }
}
