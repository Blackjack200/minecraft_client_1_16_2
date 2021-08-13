package net.minecraft.world.entity.animal.horse;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;

public class SkeletonTrapGoal extends Goal {
    private final SkeletonHorse horse;
    
    public SkeletonTrapGoal(final SkeletonHorse bbe) {
        this.horse = bbe;
    }
    
    @Override
    public boolean canUse() {
        return this.horse.level.hasNearbyAlivePlayer(this.horse.getX(), this.horse.getY(), this.horse.getZ(), 10.0);
    }
    
    @Override
    public void tick() {
        final ServerLevel aag2 = (ServerLevel)this.horse.level;
        final DifficultyInstance aop3 = aag2.getCurrentDifficultyAt(this.horse.blockPosition());
        this.horse.setTrap(false);
        this.horse.setTamed(true);
        this.horse.setAge(0);
        final LightningBolt aqi4 = EntityType.LIGHTNING_BOLT.create(aag2);
        aqi4.moveTo(this.horse.getX(), this.horse.getY(), this.horse.getZ());
        aqi4.setVisualOnly(true);
        aag2.addFreshEntity(aqi4);
        final Skeleton bdv5 = this.createSkeleton(aop3, this.horse);
        bdv5.startRiding(this.horse);
        aag2.addFreshEntityWithPassengers(bdv5);
        for (int integer6 = 0; integer6 < 3; ++integer6) {
            final AbstractHorse bay7 = this.createHorse(aop3);
            final Skeleton bdv6 = this.createSkeleton(aop3, bay7);
            bdv6.startRiding(bay7);
            bay7.push(this.horse.getRandom().nextGaussian() * 0.5, 0.0, this.horse.getRandom().nextGaussian() * 0.5);
            aag2.addFreshEntityWithPassengers(bay7);
        }
    }
    
    private AbstractHorse createHorse(final DifficultyInstance aop) {
        final SkeletonHorse bbe3 = EntityType.SKELETON_HORSE.create(this.horse.level);
        bbe3.finalizeSpawn((ServerLevelAccessor)this.horse.level, aop, MobSpawnType.TRIGGERED, null, null);
        bbe3.setPos(this.horse.getX(), this.horse.getY(), this.horse.getZ());
        bbe3.invulnerableTime = 60;
        bbe3.setPersistenceRequired();
        bbe3.setTamed(true);
        bbe3.setAge(0);
        return bbe3;
    }
    
    private Skeleton createSkeleton(final DifficultyInstance aop, final AbstractHorse bay) {
        final Skeleton bdv4 = EntityType.SKELETON.create(bay.level);
        bdv4.finalizeSpawn((ServerLevelAccessor)bay.level, aop, MobSpawnType.TRIGGERED, null, null);
        bdv4.setPos(bay.getX(), bay.getY(), bay.getZ());
        bdv4.invulnerableTime = 60;
        bdv4.setPersistenceRequired();
        if (bdv4.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            bdv4.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        bdv4.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(bdv4.getRandom(), this.disenchant(bdv4.getMainHandItem()), (int)(5.0f + aop.getSpecialMultiplier() * bdv4.getRandom().nextInt(18)), false));
        bdv4.setItemSlot(EquipmentSlot.HEAD, EnchantmentHelper.enchantItem(bdv4.getRandom(), this.disenchant(bdv4.getItemBySlot(EquipmentSlot.HEAD)), (int)(5.0f + aop.getSpecialMultiplier() * bdv4.getRandom().nextInt(18)), false));
        return bdv4;
    }
    
    private ItemStack disenchant(final ItemStack bly) {
        bly.removeTagKey("Enchantments");
        return bly;
    }
}
