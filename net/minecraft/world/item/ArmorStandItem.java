package net.minecraft.world.item;

import net.minecraft.core.Rotations;
import java.util.Random;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.server.level.ServerLevel;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class ArmorStandItem extends Item {
    public ArmorStandItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Direction gc3 = bnx.getClickedFace();
        if (gc3 == Direction.DOWN) {
            return InteractionResult.FAIL;
        }
        final Level bru4 = bnx.getLevel();
        final BlockPlaceContext bnv5 = new BlockPlaceContext(bnx);
        final BlockPos fx6 = bnv5.getClickedPos();
        final ItemStack bly7 = bnx.getItemInHand();
        final Vec3 dck8 = Vec3.atBottomCenterOf(fx6);
        final AABB dcf9 = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox(dck8.x(), dck8.y(), dck8.z());
        if (!bru4.noCollision(null, dcf9, (Predicate<Entity>)(apx -> true)) || !bru4.getEntities(null, dcf9).isEmpty()) {
            return InteractionResult.FAIL;
        }
        if (bru4 instanceof ServerLevel) {
            final ServerLevel aag10 = (ServerLevel)bru4;
            final ArmorStand bck11 = EntityType.ARMOR_STAND.create(aag10, bly7.getTag(), null, bnx.getPlayer(), fx6, MobSpawnType.SPAWN_EGG, true, true);
            if (bck11 == null) {
                return InteractionResult.FAIL;
            }
            aag10.addFreshEntityWithPassengers(bck11);
            final float float12 = Mth.floor((Mth.wrapDegrees(bnx.getRotation() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            bck11.moveTo(bck11.getX(), bck11.getY(), bck11.getZ(), float12, 0.0f);
            this.randomizePose(bck11, bru4.random);
            bru4.addFreshEntity(bck11);
            bru4.playSound(null, bck11.getX(), bck11.getY(), bck11.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75f, 0.8f);
        }
        bly7.shrink(1);
        return InteractionResult.sidedSuccess(bru4.isClientSide);
    }
    
    private void randomizePose(final ArmorStand bck, final Random random) {
        Rotations go4 = bck.getHeadPose();
        float float6 = random.nextFloat() * 5.0f;
        final float float7 = random.nextFloat() * 20.0f - 10.0f;
        Rotations go5 = new Rotations(go4.getX() + float6, go4.getY() + float7, go4.getZ());
        bck.setHeadPose(go5);
        go4 = bck.getBodyPose();
        float6 = random.nextFloat() * 10.0f - 5.0f;
        go5 = new Rotations(go4.getX(), go4.getY() + float6, go4.getZ());
        bck.setBodyPose(go5);
    }
}
