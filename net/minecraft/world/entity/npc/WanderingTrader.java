package net.minecraft.world.entity.npc;

import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.Item;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.entity.Entity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;

public class WanderingTrader extends AbstractVillager {
    @Nullable
    private BlockPos wanderTarget;
    private int despawnDelay;
    
    public WanderingTrader(final EntityType<? extends WanderingTrader> aqb, final Level bru) {
        super(aqb, bru);
        this.forcedLoading = true;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new UseItemGoal<>(this, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY), SoundEvents.WANDERING_TRADER_DISAPPEARED, (java.util.function.Predicate<?>)(bfm -> this.level.isNight() && !bfm.isInvisible())));
        this.goalSelector.addGoal(0, new UseItemGoal<>(this, new ItemStack(Items.MILK_BUCKET), SoundEvents.WANDERING_TRADER_REAPPEARED, (java.util.function.Predicate<?>)(bfm -> this.level.isDay() && bfm.isInvisible())));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Evoker.class, 12.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vindicator.class, 8.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Pillager.class, 15.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Illusioner.class, 12.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 10.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(2, new WanderToPositionGoal(this, 2.0, 0.35));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return null;
    }
    
    @Override
    public boolean showProgressBar() {
        return false;
    }
    
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.VILLAGER_SPAWN_EGG || !this.isAlive() || this.isTrading() || this.isBaby()) {
            return super.mobInteract(bft, aoq);
        }
        if (aoq == InteractionHand.MAIN_HAND) {
            bft.awardStat(Stats.TALKED_TO_VILLAGER);
        }
        if (this.getOffers().isEmpty()) {
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (!this.level.isClientSide) {
            this.setTradingPlayer(bft);
            this.openTradingScreen(bft, this.getDisplayName(), 1);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
    
    @Override
    protected void updateTrades() {
        final VillagerTrades.ItemListing[] arr2 = (VillagerTrades.ItemListing[])VillagerTrades.WANDERING_TRADER_TRADES.get(1);
        final VillagerTrades.ItemListing[] arr3 = (VillagerTrades.ItemListing[])VillagerTrades.WANDERING_TRADER_TRADES.get(2);
        if (arr2 == null || arr3 == null) {
            return;
        }
        final MerchantOffers bqt4 = this.getOffers();
        this.addOffersFromItemListings(bqt4, arr2, 5);
        final int integer5 = this.random.nextInt(arr3.length);
        final VillagerTrades.ItemListing f6 = arr3[integer5];
        final MerchantOffer bqs7 = f6.getOffer(this, this.random);
        if (bqs7 != null) {
            bqt4.add(bqs7);
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("DespawnDelay", this.despawnDelay);
        if (this.wanderTarget != null) {
            md.put("WanderTarget", (Tag)NbtUtils.writeBlockPos(this.wanderTarget));
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("DespawnDelay", 99)) {
            this.despawnDelay = md.getInt("DespawnDelay");
        }
        if (md.contains("WanderTarget")) {
            this.wanderTarget = NbtUtils.readBlockPos(md.getCompound("WanderTarget"));
        }
        this.setAge(Math.max(0, this.getAge()));
    }
    
    public boolean removeWhenFarAway(final double double1) {
        return false;
    }
    
    @Override
    protected void rewardTradeXp(final MerchantOffer bqs) {
        if (bqs.shouldRewardExp()) {
            final int integer3 = 3 + this.random.nextInt(4);
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5, this.getZ(), integer3));
        }
    }
    
    protected SoundEvent getAmbientSound() {
        if (this.isTrading()) {
            return SoundEvents.WANDERING_TRADER_TRADE;
        }
        return SoundEvents.WANDERING_TRADER_AMBIENT;
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.WANDERING_TRADER_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.WANDERING_TRADER_DEATH;
    }
    
    protected SoundEvent getDrinkingSound(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        if (blu3 == Items.MILK_BUCKET) {
            return SoundEvents.WANDERING_TRADER_DRINK_MILK;
        }
        return SoundEvents.WANDERING_TRADER_DRINK_POTION;
    }
    
    @Override
    protected SoundEvent getTradeUpdatedSound(final boolean boolean1) {
        return boolean1 ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
    }
    
    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }
    
    public void setDespawnDelay(final int integer) {
        this.despawnDelay = integer;
    }
    
    public int getDespawnDelay() {
        return this.despawnDelay;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            this.maybeDespawn();
        }
    }
    
    private void maybeDespawn() {
        if (this.despawnDelay > 0 && !this.isTrading() && --this.despawnDelay == 0) {
            this.remove();
        }
    }
    
    public void setWanderTarget(@Nullable final BlockPos fx) {
        this.wanderTarget = fx;
    }
    
    @Nullable
    private BlockPos getWanderTarget() {
        return this.wanderTarget;
    }
    
    class WanderToPositionGoal extends Goal {
        final WanderingTrader trader;
        final double stopDistance;
        final double speedModifier;
        
        WanderToPositionGoal(final WanderingTrader bfm2, final double double3, final double double4) {
            this.trader = bfm2;
            this.stopDistance = double3;
            this.speedModifier = double4;
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        }
        
        @Override
        public void stop() {
            this.trader.setWanderTarget(null);
            WanderingTrader.this.navigation.stop();
        }
        
        @Override
        public boolean canUse() {
            final BlockPos fx2 = this.trader.getWanderTarget();
            return fx2 != null && this.isTooFarAway(fx2, this.stopDistance);
        }
        
        @Override
        public void tick() {
            final BlockPos fx2 = this.trader.getWanderTarget();
            if (fx2 != null && WanderingTrader.this.navigation.isDone()) {
                if (this.isTooFarAway(fx2, 10.0)) {
                    final Vec3 dck3 = new Vec3(fx2.getX() - this.trader.getX(), fx2.getY() - this.trader.getY(), fx2.getZ() - this.trader.getZ()).normalize();
                    final Vec3 dck4 = dck3.scale(10.0).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
                    WanderingTrader.this.navigation.moveTo(dck4.x, dck4.y, dck4.z, this.speedModifier);
                }
                else {
                    WanderingTrader.this.navigation.moveTo(fx2.getX(), fx2.getY(), fx2.getZ(), this.speedModifier);
                }
            }
        }
        
        private boolean isTooFarAway(final BlockPos fx, final double double2) {
            return !fx.closerThan(this.trader.position(), double2);
        }
    }
}
