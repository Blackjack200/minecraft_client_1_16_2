package net.minecraft.client.renderer.blockentity;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.client.model.dragon.DragonHeadModel;
import net.minecraft.client.model.HumanoidHeadModel;
import java.util.HashMap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.Player;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.authlib.GameProfile;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SkullModel;
import net.minecraft.world.level.block.SkullBlock;
import java.util.Map;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

public class SkullBlockRenderer extends BlockEntityRenderer<SkullBlockEntity> {
    private static final Map<SkullBlock.Type, SkullModel> MODEL_BY_TYPE;
    private static final Map<SkullBlock.Type, ResourceLocation> SKIN_BY_TYPE;
    
    public SkullBlockRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final SkullBlockEntity cdd, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final float float3 = cdd.getMouthAnimation(float2);
        final BlockState cee9 = cdd.getBlockState();
        final boolean boolean10 = cee9.getBlock() instanceof WallSkullBlock;
        final Direction gc11 = boolean10 ? cee9.<Direction>getValue((Property<Direction>)WallSkullBlock.FACING) : null;
        final float float4 = 22.5f * (boolean10 ? ((2 + gc11.get2DDataValue()) * 4) : cee9.<Integer>getValue((Property<Integer>)SkullBlock.ROTATION));
        renderSkull(gc11, float4, ((AbstractSkullBlock)cee9.getBlock()).getType(), cdd.getOwnerProfile(), float3, dfj, dzy, integer5);
    }
    
    public static void renderSkull(@Nullable final Direction gc, final float float2, final SkullBlock.Type a, @Nullable final GameProfile gameProfile, final float float5, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final SkullModel dvl9 = (SkullModel)SkullBlockRenderer.MODEL_BY_TYPE.get(a);
        dfj.pushPose();
        if (gc == null) {
            dfj.translate(0.5, 0.0, 0.5);
        }
        else {
            final float float6 = 0.25f;
            dfj.translate(0.5f - gc.getStepX() * 0.25f, 0.25, 0.5f - gc.getStepZ() * 0.25f);
        }
        dfj.scale(-1.0f, -1.0f, 1.0f);
        final VertexConsumer dfn10 = dzy.getBuffer(getRenderType(a, gameProfile));
        dvl9.setupAnim(float5, float2, 0.0f);
        dvl9.renderToBuffer(dfj, dfn10, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
    }
    
    private static RenderType getRenderType(final SkullBlock.Type a, @Nullable final GameProfile gameProfile) {
        final ResourceLocation vk3 = (ResourceLocation)SkullBlockRenderer.SKIN_BY_TYPE.get(a);
        if (a != SkullBlock.Types.PLAYER || gameProfile == null) {
            return RenderType.entityCutoutNoCullZOffset(vk3);
        }
        final Minecraft djw4 = Minecraft.getInstance();
        final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map5 = djw4.getSkinManager().getInsecureSkinInformation(gameProfile);
        if (map5.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return RenderType.entityTranslucent(djw4.getSkinManager().registerTexture((MinecraftProfileTexture)map5.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN));
        }
        return RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(gameProfile)));
    }
    
    static {
        MODEL_BY_TYPE = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            final SkullModel dvl2 = new SkullModel(0, 0, 64, 32);
            final SkullModel dvl3 = new HumanoidHeadModel();
            final DragonHeadModel dwd4 = new DragonHeadModel(0.0f);
            hashMap.put(SkullBlock.Types.SKELETON, dvl2);
            hashMap.put(SkullBlock.Types.WITHER_SKELETON, dvl2);
            hashMap.put(SkullBlock.Types.PLAYER, dvl3);
            hashMap.put(SkullBlock.Types.ZOMBIE, dvl3);
            hashMap.put(SkullBlock.Types.CREEPER, dvl2);
            hashMap.put(SkullBlock.Types.DRAGON, dwd4);
        }));
        SKIN_BY_TYPE = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(SkullBlock.Types.SKELETON, new ResourceLocation("textures/entity/skeleton/skeleton.png"));
            hashMap.put(SkullBlock.Types.WITHER_SKELETON, new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"));
            hashMap.put(SkullBlock.Types.ZOMBIE, new ResourceLocation("textures/entity/zombie/zombie.png"));
            hashMap.put(SkullBlock.Types.CREEPER, new ResourceLocation("textures/entity/creeper/creeper.png"));
            hashMap.put(SkullBlock.Types.DRAGON, new ResourceLocation("textures/entity/enderdragon/dragon.png"));
            hashMap.put(SkullBlock.Types.PLAYER, DefaultPlayerSkin.getDefaultSkin());
        }));
    }
}
