package net.minecraft.world.entity.projectile;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Iterator;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;

public final class ProjectileUtil {
    public static HitResult getHitResult(final Entity apx, final Predicate<Entity> predicate) {
        final Vec3 dck3 = apx.getDeltaMovement();
        final Level bru4 = apx.level;
        final Vec3 dck4 = apx.position();
        Vec3 dck5 = dck4.add(dck3);
        HitResult dci7 = bru4.clip(new ClipContext(dck4, dck5, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, apx));
        if (dci7.getType() != HitResult.Type.MISS) {
            dck5 = dci7.getLocation();
        }
        final HitResult dci8 = getEntityHitResult(bru4, apx, dck4, dck5, apx.getBoundingBox().expandTowards(apx.getDeltaMovement()).inflate(1.0), predicate);
        if (dci8 != null) {
            dci7 = dci8;
        }
        return dci7;
    }
    
    @Nullable
    public static EntityHitResult getEntityHitResult(final Entity apx, final Vec3 dck2, final Vec3 dck3, final AABB dcf, final Predicate<Entity> predicate, final double double6) {
        final Level bru8 = apx.level;
        double double7 = double6;
        Entity apx2 = null;
        Vec3 dck4 = null;
        for (final Entity apx3 : bru8.getEntities(apx, dcf, predicate)) {
            final AABB dcf2 = apx3.getBoundingBox().inflate(apx3.getPickRadius());
            final Optional<Vec3> optional16 = dcf2.clip(dck2, dck3);
            if (dcf2.contains(dck2)) {
                if (double7 < 0.0) {
                    continue;
                }
                apx2 = apx3;
                dck4 = (Vec3)optional16.orElse(dck2);
                double7 = 0.0;
            }
            else {
                if (!optional16.isPresent()) {
                    continue;
                }
                final Vec3 dck5 = (Vec3)optional16.get();
                final double double8 = dck2.distanceToSqr(dck5);
                if (double8 >= double7 && double7 != 0.0) {
                    continue;
                }
                if (apx3.getRootVehicle() == apx.getRootVehicle()) {
                    if (double7 != 0.0) {
                        continue;
                    }
                    apx2 = apx3;
                    dck4 = dck5;
                }
                else {
                    apx2 = apx3;
                    dck4 = dck5;
                    double7 = double8;
                }
            }
        }
        if (apx2 == null) {
            return null;
        }
        return new EntityHitResult(apx2, dck4);
    }
    
    @Nullable
    public static EntityHitResult getEntityHitResult(final Level bru, final Entity apx, final Vec3 dck3, final Vec3 dck4, final AABB dcf, final Predicate<Entity> predicate) {
        double double7 = Double.MAX_VALUE;
        Entity apx2 = null;
        for (final Entity apx3 : bru.getEntities(apx, dcf, predicate)) {
            final AABB dcf2 = apx3.getBoundingBox().inflate(0.30000001192092896);
            final Optional<Vec3> optional13 = dcf2.clip(dck3, dck4);
            if (optional13.isPresent()) {
                final double double8 = dck3.distanceToSqr((Vec3)optional13.get());
                if (double8 >= double7) {
                    continue;
                }
                apx2 = apx3;
                double7 = double8;
            }
        }
        if (apx2 == null) {
            return null;
        }
        return new EntityHitResult(apx2);
    }
    
    public static final void rotateTowardsMovement(final Entity apx, final float float2) {
        final Vec3 dck3 = apx.getDeltaMovement();
        if (dck3.lengthSqr() == 0.0) {
            return;
        }
        final float float3 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck3));
        apx.yRot = (float)(Mth.atan2(dck3.z, dck3.x) * 57.2957763671875) + 90.0f;
        apx.xRot = (float)(Mth.atan2(float3, dck3.y) * 57.2957763671875) - 90.0f;
        while (apx.xRot - apx.xRotO < -180.0f) {
            apx.xRotO -= 360.0f;
        }
        while (apx.xRot - apx.xRotO >= 180.0f) {
            apx.xRotO += 360.0f;
        }
        while (apx.yRot - apx.yRotO < -180.0f) {
            apx.yRotO -= 360.0f;
        }
        while (apx.yRot - apx.yRotO >= 180.0f) {
            apx.yRotO += 360.0f;
        }
        apx.xRot = Mth.lerp(float2, apx.xRotO, apx.xRot);
        apx.yRot = Mth.lerp(float2, apx.yRotO, apx.yRot);
    }
    
    public static InteractionHand getWeaponHoldingHand(final LivingEntity aqj, final Item blu) {
        return (aqj.getMainHandItem().getItem() == blu) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
    
    public static AbstractArrow getMobArrow(final LivingEntity aqj, final ItemStack bly, final float float3) {
        final ArrowItem bjz4 = (ArrowItem)((bly.getItem() instanceof ArrowItem) ? bly.getItem() : Items.ARROW);
        final AbstractArrow bfx5 = bjz4.createArrow(aqj.level, bly, aqj);
        bfx5.setEnchantmentEffectsFromEntity(aqj, float3);
        if (bly.getItem() == Items.TIPPED_ARROW && bfx5 instanceof Arrow) {
            ((Arrow)bfx5).setEffectsFromItem(bly);
        }
        return bfx5;
    }
}
