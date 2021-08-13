package net.minecraft.world.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import java.util.UUID;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.npc.VillagerDataHolder;

public class ZombieVillager extends Zombie implements VillagerDataHolder {
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID;
    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA;
    private int villagerConversionTime;
    private UUID conversionStarter;
    private Tag gossips;
    private CompoundTag tradeOffers;
    private int villagerXp;
    
    public ZombieVillager(final EntityType<? extends ZombieVillager> aqb, final Level bru) {
        super(aqb, bru);
        this.setVillagerData(this.getVillagerData().setProfession(Registry.VILLAGER_PROFESSION.getRandom(this.random)));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(ZombieVillager.DATA_CONVERTING_ID, false);
        this.entityData.<VillagerData>define(ZombieVillager.DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        VillagerData.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.getVillagerData()).resultOrPartial(ZombieVillager.LOGGER::error).ifPresent(mt -> md.put("VillagerData", mt));
        if (this.tradeOffers != null) {
            md.put("Offers", (Tag)this.tradeOffers);
        }
        if (this.gossips != null) {
            md.put("Gossips", this.gossips);
        }
        md.putInt("ConversionTime", this.isConverting() ? this.villagerConversionTime : -1);
        if (this.conversionStarter != null) {
            md.putUUID("ConversionPlayer", this.conversionStarter);
        }
        md.putInt("Xp", this.villagerXp);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("VillagerData", 10)) {
            final DataResult<VillagerData> dataResult3 = (DataResult<VillagerData>)VillagerData.CODEC.parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, md.get("VillagerData")));
            dataResult3.resultOrPartial(ZombieVillager.LOGGER::error).ifPresent(this::setVillagerData);
        }
        if (md.contains("Offers", 10)) {
            this.tradeOffers = md.getCompound("Offers");
        }
        if (md.contains("Gossips", 10)) {
            this.gossips = md.getList("Gossips", 10);
        }
        if (md.contains("ConversionTime", 99) && md.getInt("ConversionTime") > -1) {
            this.startConverting(md.hasUUID("ConversionPlayer") ? md.getUUID("ConversionPlayer") : null, md.getInt("ConversionTime"));
        }
        if (md.contains("Xp", 3)) {
            this.villagerXp = md.getInt("Xp");
        }
    }
    
    @Override
    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && this.isConverting()) {
            final int integer2 = this.getConversionProgress();
            this.villagerConversionTime -= integer2;
            if (this.villagerConversionTime <= 0) {
                this.finishConversion((ServerLevel)this.level);
            }
        }
        super.tick();
    }
    
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() != Items.GOLDEN_APPLE) {
            return super.mobInteract(bft, aoq);
        }
        if (this.hasEffect(MobEffects.WEAKNESS)) {
            if (!bft.abilities.instabuild) {
                bly4.shrink(1);
            }
            if (!this.level.isClientSide) {
                this.startConverting(bft.getUUID(), this.random.nextInt(2401) + 3600);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    protected boolean convertsInWater() {
        return false;
    }
    
    public boolean removeWhenFarAway(final double double1) {
        return !this.isConverting() && this.villagerXp == 0;
    }
    
    public boolean isConverting() {
        return this.getEntityData().<Boolean>get(ZombieVillager.DATA_CONVERTING_ID);
    }
    
    private void startConverting(@Nullable final UUID uUID, final int integer) {
        this.conversionStarter = uUID;
        this.villagerConversionTime = integer;
        this.getEntityData().<Boolean>set(ZombieVillager.DATA_CONVERTING_ID, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, integer, Math.min(this.level.getDifficulty().getId() - 1, 0)));
        this.level.broadcastEntityEvent(this, (byte)16);
    }
    
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 16) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0f + this.random.nextFloat(), this.random.nextFloat() * 0.7f + 0.3f, false);
            }
            return;
        }
        super.handleEntityEvent(byte1);
    }
    
    private void finishConversion(final ServerLevel aag) {
        final Villager bfg3 = this.<Villager>convertTo(EntityType.VILLAGER, false);
        for (final EquipmentSlot aqc7 : EquipmentSlot.values()) {
            final ItemStack bly8 = this.getItemBySlot(aqc7);
            if (!bly8.isEmpty()) {
                if (EnchantmentHelper.hasBindingCurse(bly8)) {
                    bfg3.setSlot(aqc7.getIndex() + 300, bly8);
                }
                else {
                    final double double9 = this.getEquipmentDropChance(aqc7);
                    if (double9 > 1.0) {
                        this.spawnAtLocation(bly8);
                    }
                }
            }
        }
        bfg3.setVillagerData(this.getVillagerData());
        if (this.gossips != null) {
            bfg3.setGossips(this.gossips);
        }
        if (this.tradeOffers != null) {
            bfg3.setOffers(new MerchantOffers(this.tradeOffers));
        }
        bfg3.setVillagerXp(this.villagerXp);
        bfg3.finalizeSpawn(aag, aag.getCurrentDifficultyAt(bfg3.blockPosition()), MobSpawnType.CONVERSION, null, null);
        if (this.conversionStarter != null) {
            final Player bft4 = aag.getPlayerByUUID(this.conversionStarter);
            if (bft4 instanceof ServerPlayer) {
                CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((ServerPlayer)bft4, this, bfg3);
                aag.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, bft4, bfg3);
            }
        }
        bfg3.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        if (!this.isSilent()) {
            aag.levelEvent(null, 1027, this.blockPosition(), 0);
        }
    }
    
    private int getConversionProgress() {
        int integer2 = 1;
        if (this.random.nextFloat() < 0.01f) {
            int integer3 = 0;
            final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
            for (int integer4 = (int)this.getX() - 4; integer4 < (int)this.getX() + 4 && integer3 < 14; ++integer4) {
                for (int integer5 = (int)this.getY() - 4; integer5 < (int)this.getY() + 4 && integer3 < 14; ++integer5) {
                    for (int integer6 = (int)this.getZ() - 4; integer6 < (int)this.getZ() + 4 && integer3 < 14; ++integer6) {
                        final Block bul8 = this.level.getBlockState(a4.set(integer4, integer5, integer6)).getBlock();
                        if (bul8 == Blocks.IRON_BARS || bul8 instanceof BedBlock) {
                            if (this.random.nextFloat() < 0.3f) {
                                ++integer2;
                            }
                            ++integer3;
                        }
                    }
                }
            }
        }
        return integer2;
    }
    
    protected float getVoicePitch() {
        if (this.isBaby()) {
            return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 2.0f;
        }
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f;
    }
    
    public SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
    }
    
    public SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.ZOMBIE_VILLAGER_HURT;
    }
    
    public SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_VILLAGER_DEATH;
    }
    
    public SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_VILLAGER_STEP;
    }
    
    @Override
    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
    
    public void setTradeOffers(final CompoundTag md) {
        this.tradeOffers = md;
    }
    
    public void setGossips(final Tag mt) {
        this.gossips = mt;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        this.setVillagerData(this.getVillagerData().setType(VillagerType.byBiome(bsh.getBiomeName(this.blockPosition()))));
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    public void setVillagerData(final VillagerData bfh) {
        final VillagerData bfh2 = this.getVillagerData();
        if (bfh2.getProfession() != bfh.getProfession()) {
            this.tradeOffers = null;
        }
        this.entityData.<VillagerData>set(ZombieVillager.DATA_VILLAGER_DATA, bfh);
    }
    
    @Override
    public VillagerData getVillagerData() {
        return this.entityData.<VillagerData>get(ZombieVillager.DATA_VILLAGER_DATA);
    }
    
    public void setVillagerXp(final int integer) {
        this.villagerXp = integer;
    }
    
    static {
        DATA_CONVERTING_ID = SynchedEntityData.<Boolean>defineId(ZombieVillager.class, EntityDataSerializers.BOOLEAN);
        DATA_VILLAGER_DATA = SynchedEntityData.<VillagerData>defineId(ZombieVillager.class, EntityDataSerializers.VILLAGER_DATA);
    }
}
