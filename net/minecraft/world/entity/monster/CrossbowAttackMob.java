package net.minecraft.world.entity.monster;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Items;
import javax.annotation.Nullable;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;

public interface CrossbowAttackMob extends RangedAttackMob {
    void setChargingCrossbow(final boolean boolean1);
    
    void shootCrossbowProjectile(final LivingEntity aqj, final ItemStack bly, final Projectile bgj, final float float4);
    
    @Nullable
    LivingEntity getTarget();
    
    void onCrossbowAttackPerformed();
    
    default void performCrossbowAttack(final LivingEntity aqj, final float float2) {
        final InteractionHand aoq4 = ProjectileUtil.getWeaponHoldingHand(aqj, Items.CROSSBOW);
        final ItemStack bly5 = aqj.getItemInHand(aoq4);
        if (aqj.isHolding(Items.CROSSBOW)) {
            CrossbowItem.performShooting(aqj.level, aqj, aoq4, bly5, float2, (float)(14 - aqj.level.getDifficulty().getId() * 4));
        }
        this.onCrossbowAttackPerformed();
    }
    
    default void shootCrossbowProjectile(final LivingEntity aqj1, final LivingEntity aqj2, final Projectile bgj, final float float4, final float float5) {
        final Entity apx7 = bgj;
        final double double8 = aqj2.getX() - aqj1.getX();
        final double double9 = aqj2.getZ() - aqj1.getZ();
        final double double10 = Mth.sqrt(double8 * double8 + double9 * double9);
        final double double11 = aqj2.getY(0.3333333333333333) - apx7.getY() + double10 * 0.20000000298023224;
        final Vector3f g16 = this.getProjectileShotVector(aqj1, new Vec3(double8, double11, double9), float4);
        bgj.shoot(g16.x(), g16.y(), g16.z(), float5, (float)(14 - aqj1.level.getDifficulty().getId() * 4));
        aqj1.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0f, 1.0f / (aqj1.getRandom().nextFloat() * 0.4f + 0.8f));
    }
    
    default Vector3f getProjectileShotVector(final LivingEntity aqj, final Vec3 dck, final float float3) {
        final Vec3 dck2 = dck.normalize();
        Vec3 dck3 = dck2.cross(new Vec3(0.0, 1.0, 0.0));
        if (dck3.lengthSqr() <= 1.0E-7) {
            dck3 = dck2.cross(aqj.getUpVector(1.0f));
        }
        final Quaternion d7 = new Quaternion(new Vector3f(dck3), 90.0f, true);
        final Vector3f g8 = new Vector3f(dck2);
        g8.transform(d7);
        final Quaternion d8 = new Quaternion(g8, float3, true);
        final Vector3f g9 = new Vector3f(dck2);
        g9.transform(d8);
        return g9;
    }
}
