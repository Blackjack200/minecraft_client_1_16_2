package net.minecraft.world.entity.animal;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import java.util.function.Consumer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Shearable;

public class MushroomCow extends Cow implements Shearable {
    private static final EntityDataAccessor<String> DATA_TYPE;
    private MobEffect effect;
    private int effectDuration;
    private UUID lastLightningBoltUUID;
    
    public MushroomCow(final EntityType<? extends MushroomCow> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (brw.getBlockState(fx.below()).is(Blocks.MYCELIUM)) {
            return 10.0f;
        }
        return brw.getBrightness(fx) - 0.5f;
    }
    
    public static boolean checkMushroomSpawnRules(final EntityType<MushroomCow> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getBlockState(fx.below()).is(Blocks.MYCELIUM) && brv.getRawBrightness(fx, 0) > 8;
    }
    
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
        final UUID uUID4 = aqi.getUUID();
        if (!uUID4.equals(this.lastLightningBoltUUID)) {
            this.setMushroomType((this.getMushroomType() == MushroomType.RED) ? MushroomType.BROWN : MushroomType.RED);
            this.lastLightningBoltUUID = uUID4;
            this.playSound(SoundEvents.MOOSHROOM_CONVERT, 2.0f, 1.0f);
        }
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<String>define(MushroomCow.DATA_TYPE, MushroomType.RED.type);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.BOWL && !this.isBaby()) {
            boolean boolean6 = false;
            ItemStack bly5;
            if (this.effect != null) {
                boolean6 = true;
                bly5 = new ItemStack(Items.SUSPICIOUS_STEW);
                SuspiciousStewItem.saveMobEffect(bly5, this.effect, this.effectDuration);
                this.effect = null;
                this.effectDuration = 0;
            }
            else {
                bly5 = new ItemStack(Items.MUSHROOM_STEW);
            }
            final ItemStack bly6 = ItemUtils.createFilledResult(bly4, bft, bly5, false);
            bft.setItemInHand(aoq, bly6);
            SoundEvent adn8;
            if (boolean6) {
                adn8 = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
            }
            else {
                adn8 = SoundEvents.MOOSHROOM_MILK;
            }
            this.playSound(adn8, 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (bly4.getItem() == Items.SHEARS && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            if (!this.level.isClientSide) {
                bly4.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (this.getMushroomType() == MushroomType.BROWN && bly4.getItem().is(ItemTags.SMALL_FLOWERS)) {
            if (this.effect != null) {
                for (int integer5 = 0; integer5 < 2; ++integer5) {
                    this.level.addParticle(ParticleTypes.SMOKE, this.getX() + this.random.nextDouble() / 2.0, this.getY(0.5), this.getZ() + this.random.nextDouble() / 2.0, 0.0, this.random.nextDouble() / 5.0, 0.0);
                }
            }
            else {
                final Optional<Pair<MobEffect, Integer>> optional5 = this.getEffectFromItemStack(bly4);
                if (!optional5.isPresent()) {
                    return InteractionResult.PASS;
                }
                final Pair<MobEffect, Integer> pair6 = (Pair<MobEffect, Integer>)optional5.get();
                if (!bft.abilities.instabuild) {
                    bly4.shrink(1);
                }
                for (int integer6 = 0; integer6 < 4; ++integer6) {
                    this.level.addParticle(ParticleTypes.EFFECT, this.getX() + this.random.nextDouble() / 2.0, this.getY(0.5), this.getZ() + this.random.nextDouble() / 2.0, 0.0, this.random.nextDouble() / 5.0, 0.0);
                }
                this.effect = (MobEffect)pair6.getLeft();
                this.effectDuration = (int)pair6.getRight();
                this.playSound(SoundEvents.MOOSHROOM_EAT, 2.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(bft, aoq);
    }
    
    @Override
    public void shear(final SoundSource adp) {
        this.level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, adp, 1.0f, 1.0f);
        if (!this.level.isClientSide()) {
            ((ServerLevel)this.level).<SimpleParticleType>sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.remove();
            final Cow bab3 = EntityType.COW.create(this.level);
            bab3.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            bab3.setHealth(this.getHealth());
            bab3.yBodyRot = this.yBodyRot;
            if (this.hasCustomName()) {
                bab3.setCustomName(this.getCustomName());
                bab3.setCustomNameVisible(this.isCustomNameVisible());
            }
            if (this.isPersistenceRequired()) {
                bab3.setPersistenceRequired();
            }
            bab3.setInvulnerable(this.isInvulnerable());
            this.level.addFreshEntity(bab3);
            for (int integer4 = 0; integer4 < 5; ++integer4) {
                this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(this.getMushroomType().blockState.getBlock())));
            }
        }
    }
    
    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isBaby();
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putString("Type", this.getMushroomType().type);
        if (this.effect != null) {
            md.putByte("EffectId", (byte)MobEffect.getId(this.effect));
            md.putInt("EffectDuration", this.effectDuration);
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setMushroomType(byType(md.getString("Type")));
        if (md.contains("EffectId", 1)) {
            this.effect = MobEffect.byId(md.getByte("EffectId"));
        }
        if (md.contains("EffectDuration", 3)) {
            this.effectDuration = md.getInt("EffectDuration");
        }
    }
    
    private Optional<Pair<MobEffect, Integer>> getEffectFromItemStack(final ItemStack bly) {
        final Item blu3 = bly.getItem();
        if (blu3 instanceof BlockItem) {
            final Block bul4 = ((BlockItem)blu3).getBlock();
            if (bul4 instanceof FlowerBlock) {
                final FlowerBlock bwr5 = (FlowerBlock)bul4;
                return (Optional<Pair<MobEffect, Integer>>)Optional.of(Pair.of((Object)bwr5.getSuspiciousStewEffect(), (Object)bwr5.getEffectDuration()));
            }
        }
        return (Optional<Pair<MobEffect, Integer>>)Optional.empty();
    }
    
    private void setMushroomType(final MushroomType a) {
        this.entityData.<String>set(MushroomCow.DATA_TYPE, a.type);
    }
    
    public MushroomType getMushroomType() {
        return byType(this.entityData.<String>get(MushroomCow.DATA_TYPE));
    }
    
    @Override
    public MushroomCow getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        final MushroomCow bag4 = EntityType.MOOSHROOM.create(aag);
        bag4.setMushroomType(this.getOffspringType((MushroomCow)apv));
        return bag4;
    }
    
    private MushroomType getOffspringType(final MushroomCow bag) {
        final MushroomType a3 = this.getMushroomType();
        final MushroomType a4 = bag.getMushroomType();
        MushroomType a5;
        if (a3 == a4 && this.random.nextInt(1024) == 0) {
            a5 = ((a3 == MushroomType.BROWN) ? MushroomType.RED : MushroomType.BROWN);
        }
        else {
            a5 = (this.random.nextBoolean() ? a3 : a4);
        }
        return a5;
    }
    
    static {
        DATA_TYPE = SynchedEntityData.<String>defineId(MushroomCow.class, EntityDataSerializers.STRING);
    }
    
    public enum MushroomType {
        RED("red", Blocks.RED_MUSHROOM.defaultBlockState()), 
        BROWN("brown", Blocks.BROWN_MUSHROOM.defaultBlockState());
        
        private final String type;
        private final BlockState blockState;
        
        private MushroomType(final String string3, final BlockState cee) {
            this.type = string3;
            this.blockState = cee;
        }
        
        public BlockState getBlockState() {
            return this.blockState;
        }
        
        private static MushroomType byType(final String string) {
            for (final MushroomType a5 : values()) {
                if (a5.type.equals(string)) {
                    return a5;
                }
            }
            return MushroomType.RED;
        }
    }
}
