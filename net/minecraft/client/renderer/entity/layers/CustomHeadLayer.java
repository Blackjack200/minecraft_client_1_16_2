package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;

public class CustomHeadLayer<T extends LivingEntity, M extends EntityModel> extends RenderLayer<T, M> {
    private final float scaleX;
    private final float scaleY;
    private final float scaleZ;
    
    public CustomHeadLayer(final RenderLayerParent<T, M> egc) {
        this(egc, 1.0f, 1.0f, 1.0f);
    }
    
    public CustomHeadLayer(final RenderLayerParent<T, M> egc, final float float2, final float float3, final float float4) {
        super(egc);
        this.scaleX = float2;
        this.scaleY = float3;
        this.scaleZ = float4;
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final ItemStack bly12 = aqj.getItemBySlot(EquipmentSlot.HEAD);
        if (bly12.isEmpty()) {
            return;
        }
        final Item blu13 = bly12.getItem();
        dfj.pushPose();
        dfj.scale(this.scaleX, this.scaleY, this.scaleZ);
        final boolean boolean14 = aqj instanceof Villager || aqj instanceof ZombieVillager;
        if (aqj.isBaby() && !(aqj instanceof Villager)) {
            final float float11 = 2.0f;
            final float float12 = 1.4f;
            dfj.translate(0.0, 0.03125, 0.0);
            dfj.scale(0.7f, 0.7f, 0.7f);
            dfj.translate(0.0, 1.0, 0.0);
        }
        this.getParentModel().getHead().translateAndRotate(dfj);
        if (blu13 instanceof BlockItem && ((BlockItem)blu13).getBlock() instanceof AbstractSkullBlock) {
            final float float11 = 1.1875f;
            dfj.scale(1.1875f, -1.1875f, -1.1875f);
            if (boolean14) {
                dfj.translate(0.0, 0.0625, 0.0);
            }
            GameProfile gameProfile16 = null;
            if (bly12.hasTag()) {
                final CompoundTag md17 = bly12.getTag();
                if (md17.contains("SkullOwner", 10)) {
                    gameProfile16 = NbtUtils.readGameProfile(md17.getCompound("SkullOwner"));
                }
                else if (md17.contains("SkullOwner", 8)) {
                    final String string18 = md17.getString("SkullOwner");
                    if (!StringUtils.isBlank((CharSequence)string18)) {
                        gameProfile16 = SkullBlockEntity.updateGameprofile(new GameProfile((UUID)null, string18));
                        md17.put("SkullOwner", (Tag)NbtUtils.writeGameProfile(new CompoundTag(), gameProfile16));
                    }
                }
            }
            dfj.translate(-0.5, 0.0, -0.5);
            SkullBlockRenderer.renderSkull(null, 180.0f, ((AbstractSkullBlock)((BlockItem)blu13).getBlock()).getType(), gameProfile16, float5, dfj, dzy, integer);
        }
        else if (!(blu13 instanceof ArmorItem) || ((ArmorItem)blu13).getSlot() != EquipmentSlot.HEAD) {
            final float float11 = 0.625f;
            dfj.translate(0.0, -0.25, 0.0);
            dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
            dfj.scale(0.625f, -0.625f, -0.625f);
            if (boolean14) {
                dfj.translate(0.0, 0.1875, 0.0);
            }
            Minecraft.getInstance().getItemInHandRenderer().renderItem(aqj, bly12, ItemTransforms.TransformType.HEAD, false, dfj, dzy, integer);
        }
        dfj.popPose();
    }
}
