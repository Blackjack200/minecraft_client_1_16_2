package net.minecraft.world.entity.npc;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.trading.MerchantOffers;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.entity.AgableMob;

public abstract class AbstractVillager extends AgableMob implements Npc, Merchant {
    private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER;
    @Nullable
    private Player tradingPlayer;
    @Nullable
    protected MerchantOffers offers;
    private final SimpleContainer inventory;
    
    public AbstractVillager(final EntityType<? extends AbstractVillager> aqb, final Level bru) {
        super(aqb, bru);
        this.inventory = new SimpleContainer(8);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
    }
    
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        if (aqz == null) {
            aqz = new AgableMobGroupData(false);
        }
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public int getUnhappyCounter() {
        return this.entityData.<Integer>get(AbstractVillager.DATA_UNHAPPY_COUNTER);
    }
    
    public void setUnhappyCounter(final int integer) {
        this.entityData.<Integer>set(AbstractVillager.DATA_UNHAPPY_COUNTER, integer);
    }
    
    @Override
    public int getVillagerXp() {
        return 0;
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        if (this.isBaby()) {
            return 0.81f;
        }
        return 1.62f;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(AbstractVillager.DATA_UNHAPPY_COUNTER, 0);
    }
    
    @Override
    public void setTradingPlayer(@Nullable final Player bft) {
        this.tradingPlayer = bft;
    }
    
    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }
    
    public boolean isTrading() {
        return this.tradingPlayer != null;
    }
    
    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.updateTrades();
        }
        return this.offers;
    }
    
    @Override
    public void overrideOffers(@Nullable final MerchantOffers bqt) {
    }
    
    @Override
    public void overrideXp(final int integer) {
    }
    
    @Override
    public void notifyTrade(final MerchantOffer bqs) {
        bqs.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.rewardTradeXp(bqs);
        if (this.tradingPlayer instanceof ServerPlayer) {
            CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, bqs.getResult());
        }
    }
    
    protected abstract void rewardTradeXp(final MerchantOffer bqs);
    
    @Override
    public boolean showProgressBar() {
        return true;
    }
    
    @Override
    public void notifyTradeUpdated(final ItemStack bly) {
        if (!this.level.isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!bly.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }
    }
    
    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }
    
    protected SoundEvent getTradeUpdatedSound(final boolean boolean1) {
        return boolean1 ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
    }
    
    public void playCelebrateSound() {
        this.playSound(SoundEvents.VILLAGER_CELEBRATE, this.getSoundVolume(), this.getVoicePitch());
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        final MerchantOffers bqt3 = this.getOffers();
        if (!bqt3.isEmpty()) {
            md.put("Offers", (Tag)bqt3.createTag());
        }
        md.put("Inventory", (Tag)this.inventory.createTag());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("Offers", 10)) {
            this.offers = new MerchantOffers(md.getCompound("Offers"));
        }
        this.inventory.fromTag(md.getList("Inventory", 10));
    }
    
    @Nullable
    public Entity changeDimension(final ServerLevel aag) {
        this.stopTrading();
        return super.changeDimension(aag);
    }
    
    protected void stopTrading() {
        this.setTradingPlayer(null);
    }
    
    public void die(final DamageSource aph) {
        super.die(aph);
        this.stopTrading();
    }
    
    protected void addParticlesAroundSelf(final ParticleOptions hf) {
        for (int integer3 = 0; integer3 < 5; ++integer3) {
            final double double4 = this.random.nextGaussian() * 0.02;
            final double double5 = this.random.nextGaussian() * 0.02;
            final double double6 = this.random.nextGaussian() * 0.02;
            this.level.addParticle(hf, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), double4, double5, double6);
        }
    }
    
    public boolean canBeLeashed(final Player bft) {
        return false;
    }
    
    public SimpleContainer getInventory() {
        return this.inventory;
    }
    
    public boolean setSlot(final int integer, final ItemStack bly) {
        if (super.setSlot(integer, bly)) {
            return true;
        }
        final int integer2 = integer - 300;
        if (integer2 >= 0 && integer2 < this.inventory.getContainerSize()) {
            this.inventory.setItem(integer2, bly);
            return true;
        }
        return false;
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    protected abstract void updateTrades();
    
    protected void addOffersFromItemListings(final MerchantOffers bqt, final VillagerTrades.ItemListing[] arr, final int integer) {
        final Set<Integer> set5 = (Set<Integer>)Sets.newHashSet();
        if (arr.length > integer) {
            while (set5.size() < integer) {
                set5.add(this.random.nextInt(arr.length));
            }
        }
        else {
            for (int integer2 = 0; integer2 < arr.length; ++integer2) {
                set5.add(integer2);
            }
        }
        for (final Integer integer3 : set5) {
            final VillagerTrades.ItemListing f8 = arr[integer3];
            final MerchantOffer bqs9 = f8.getOffer(this, this.random);
            if (bqs9 != null) {
                bqt.add(bqs9);
            }
        }
    }
    
    public Vec3 getRopeHoldPosition(final float float1) {
        final float float2 = Mth.lerp(float1, this.yBodyRotO, this.yBodyRot) * 0.017453292f;
        final Vec3 dck4 = new Vec3(0.0, this.getBoundingBox().getYsize() - 1.0, 0.2);
        return this.getPosition(float1).add(dck4.yRot(-float2));
    }
    
    static {
        DATA_UNHAPPY_COUNTER = SynchedEntityData.<Integer>defineId(AbstractVillager.class, EntityDataSerializers.INT);
    }
}
