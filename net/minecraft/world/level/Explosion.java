package net.minecraft.world.level;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.item.ItemEntity;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Iterator;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.server.level.ServerLevel;
import java.util.Collections;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.Optional;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import com.google.common.collect.Sets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.Mth;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import java.util.Map;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import java.util.Random;

public class Explosion {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR;
    private final boolean fire;
    private final BlockInteraction blockInteraction;
    private final Random random;
    private final Level level;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity source;
    private final float radius;
    private final DamageSource damageSource;
    private final ExplosionDamageCalculator damageCalculator;
    private final List<BlockPos> toBlow;
    private final Map<Player, Vec3> hitPlayers;
    
    public Explosion(final Level bru, @Nullable final Entity apx, final double double3, final double double4, final double double5, final float float6, final List<BlockPos> list) {
        this(bru, apx, double3, double4, double5, float6, false, BlockInteraction.DESTROY, list);
    }
    
    public Explosion(final Level bru, @Nullable final Entity apx, final double double3, final double double4, final double double5, final float float6, final boolean boolean7, final BlockInteraction a, final List<BlockPos> list) {
        this(bru, apx, double3, double4, double5, float6, boolean7, a);
        this.toBlow.addAll((Collection)list);
    }
    
    public Explosion(final Level bru, @Nullable final Entity apx, final double double3, final double double4, final double double5, final float float6, final boolean boolean7, final BlockInteraction a) {
        this(bru, apx, null, null, double3, double4, double5, float6, boolean7, a);
    }
    
    public Explosion(final Level bru, @Nullable final Entity apx, @Nullable final DamageSource aph, @Nullable final ExplosionDamageCalculator brn, final double double5, final double double6, final double double7, final float float8, final boolean boolean9, final BlockInteraction a) {
        this.random = new Random();
        this.toBlow = (List<BlockPos>)Lists.newArrayList();
        this.hitPlayers = (Map<Player, Vec3>)Maps.newHashMap();
        this.level = bru;
        this.source = apx;
        this.radius = float8;
        this.x = double5;
        this.y = double6;
        this.z = double7;
        this.fire = boolean9;
        this.blockInteraction = a;
        this.damageSource = ((aph == null) ? DamageSource.explosion(this) : aph);
        this.damageCalculator = ((brn == null) ? this.makeDamageCalculator(apx) : brn);
    }
    
    private ExplosionDamageCalculator makeDamageCalculator(@Nullable final Entity apx) {
        return (apx == null) ? Explosion.EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(apx);
    }
    
    public static float getSeenPercent(final Vec3 dck, final Entity apx) {
        final AABB dcf3 = apx.getBoundingBox();
        final double double4 = 1.0 / ((dcf3.maxX - dcf3.minX) * 2.0 + 1.0);
        final double double5 = 1.0 / ((dcf3.maxY - dcf3.minY) * 2.0 + 1.0);
        final double double6 = 1.0 / ((dcf3.maxZ - dcf3.minZ) * 2.0 + 1.0);
        final double double7 = (1.0 - Math.floor(1.0 / double4) * double4) / 2.0;
        final double double8 = (1.0 - Math.floor(1.0 / double6) * double6) / 2.0;
        if (double4 < 0.0 || double5 < 0.0 || double6 < 0.0) {
            return 0.0f;
        }
        int integer14 = 0;
        int integer15 = 0;
        for (float float16 = 0.0f; float16 <= 1.0f; float16 += (float)double4) {
            for (float float17 = 0.0f; float17 <= 1.0f; float17 += (float)double5) {
                for (float float18 = 0.0f; float18 <= 1.0f; float18 += (float)double6) {
                    final double double9 = Mth.lerp(float16, dcf3.minX, dcf3.maxX);
                    final double double10 = Mth.lerp(float17, dcf3.minY, dcf3.maxY);
                    final double double11 = Mth.lerp(float18, dcf3.minZ, dcf3.maxZ);
                    final Vec3 dck2 = new Vec3(double9 + double7, double10, double11 + double8);
                    if (apx.level.clip(new ClipContext(dck2, dck, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, apx)).getType() == HitResult.Type.MISS) {
                        ++integer14;
                    }
                    ++integer15;
                }
            }
        }
        return integer14 / (float)integer15;
    }
    
    public void explode() {
        final Set<BlockPos> set2 = (Set<BlockPos>)Sets.newHashSet();
        final int integer3 = 16;
        for (int integer4 = 0; integer4 < 16; ++integer4) {
            for (int integer5 = 0; integer5 < 16; ++integer5) {
                for (int integer6 = 0; integer6 < 16; ++integer6) {
                    if (integer4 == 0 || integer4 == 15 || integer5 == 0 || integer5 == 15 || integer6 == 0 || integer6 == 15) {
                        double double7 = integer4 / 15.0f * 2.0f - 1.0f;
                        double double8 = integer5 / 15.0f * 2.0f - 1.0f;
                        double double9 = integer6 / 15.0f * 2.0f - 1.0f;
                        final double double10 = Math.sqrt(double7 * double7 + double8 * double8 + double9 * double9);
                        double7 /= double10;
                        double8 /= double10;
                        double9 /= double10;
                        float float15 = this.radius * (0.7f + this.level.random.nextFloat() * 0.6f);
                        double double11 = this.x;
                        double double12 = this.y;
                        double double13 = this.z;
                        final float float16 = 0.3f;
                        while (float15 > 0.0f) {
                            final BlockPos fx23 = new BlockPos(double11, double12, double13);
                            final BlockState cee24 = this.level.getBlockState(fx23);
                            final FluidState cuu25 = this.level.getFluidState(fx23);
                            final Optional<Float> optional26 = this.damageCalculator.getBlockExplosionResistance(this, this.level, fx23, cee24, cuu25);
                            if (optional26.isPresent()) {
                                float15 -= ((float)optional26.get() + 0.3f) * 0.3f;
                            }
                            if (float15 > 0.0f && this.damageCalculator.shouldBlockExplode(this, this.level, fx23, cee24, float15)) {
                                set2.add(fx23);
                            }
                            double11 += double7 * 0.30000001192092896;
                            double12 += double8 * 0.30000001192092896;
                            double13 += double9 * 0.30000001192092896;
                            float15 -= 0.22500001f;
                        }
                    }
                }
            }
        }
        this.toBlow.addAll((Collection)set2);
        final float float17 = this.radius * 2.0f;
        int integer5 = Mth.floor(this.x - float17 - 1.0);
        int integer6 = Mth.floor(this.x + float17 + 1.0);
        final int integer7 = Mth.floor(this.y - float17 - 1.0);
        final int integer8 = Mth.floor(this.y + float17 + 1.0);
        final int integer9 = Mth.floor(this.z - float17 - 1.0);
        final int integer10 = Mth.floor(this.z + float17 + 1.0);
        final List<Entity> list11 = this.level.getEntities(this.source, new AABB(integer5, integer7, integer9, integer6, integer8, integer10));
        final Vec3 dck12 = new Vec3(this.x, this.y, this.z);
        for (int integer11 = 0; integer11 < list11.size(); ++integer11) {
            final Entity apx14 = (Entity)list11.get(integer11);
            if (!apx14.ignoreExplosion()) {
                final double double14 = Mth.sqrt(apx14.distanceToSqr(dck12)) / float17;
                if (double14 <= 1.0) {
                    double double15 = apx14.getX() - this.x;
                    double double16 = ((apx14 instanceof PrimedTnt) ? apx14.getY() : apx14.getEyeY()) - this.y;
                    double double17 = apx14.getZ() - this.z;
                    final double double18 = Mth.sqrt(double15 * double15 + double16 * double16 + double17 * double17);
                    if (double18 != 0.0) {
                        double15 /= double18;
                        double16 /= double18;
                        double17 /= double18;
                        final double double19 = getSeenPercent(dck12, apx14);
                        final double double20 = (1.0 - double14) * double19;
                        apx14.hurt(this.getDamageSource(), (float)(int)((double20 * double20 + double20) / 2.0 * 7.0 * float17 + 1.0));
                        double double21 = double20;
                        if (apx14 instanceof LivingEntity) {
                            double21 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)apx14, double20);
                        }
                        apx14.setDeltaMovement(apx14.getDeltaMovement().add(double15 * double21, double16 * double21, double17 * double21));
                        if (apx14 instanceof Player) {
                            final Player bft31 = (Player)apx14;
                            if (!bft31.isSpectator() && (!bft31.isCreative() || !bft31.abilities.flying)) {
                                this.hitPlayers.put(bft31, new Vec3(double15 * double20, double16 * double20, double17 * double20));
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void finalizeExplosion(final boolean boolean1) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0f, (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f, false);
        }
        final boolean boolean2 = this.blockInteraction != BlockInteraction.NONE;
        if (boolean1) {
            if (this.radius < 2.0f || !boolean2) {
                this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
            else {
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        if (boolean2) {
            final ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList4 = (ObjectArrayList<Pair<ItemStack, BlockPos>>)new ObjectArrayList();
            Collections.shuffle((List)this.toBlow, this.level.random);
            for (final BlockPos fx6 : this.toBlow) {
                final BlockState cee7 = this.level.getBlockState(fx6);
                final Block bul8 = cee7.getBlock();
                if (!cee7.isAir()) {
                    final BlockPos fx7 = fx6.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    if (bul8.dropFromExplosion(this) && this.level instanceof ServerLevel) {
                        final BlockEntity ccg10 = bul8.isEntityBlock() ? this.level.getBlockEntity(fx6) : null;
                        final LootContext.Builder a11 = new LootContext.Builder((ServerLevel)this.level).withRandom(this.level.random).<Vec3>withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(fx6)).<ItemStack>withParameter(LootContextParams.TOOL, ItemStack.EMPTY).<BlockEntity>withOptionalParameter(LootContextParams.BLOCK_ENTITY, ccg10).<Entity>withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                        if (this.blockInteraction == BlockInteraction.DESTROY) {
                            a11.<Float>withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                        }
                        cee7.getDrops(a11).forEach(bly -> addBlockDrops(objectArrayList4, bly, fx7));
                    }
                    this.level.setBlock(fx6, Blocks.AIR.defaultBlockState(), 3);
                    bul8.wasExploded(this.level, fx6, this);
                    this.level.getProfiler().pop();
                }
            }
            for (final Pair<ItemStack, BlockPos> pair6 : objectArrayList4) {
                Block.popResource(this.level, (BlockPos)pair6.getSecond(), (ItemStack)pair6.getFirst());
            }
        }
        if (this.fire) {
            for (final BlockPos fx8 : this.toBlow) {
                if (this.random.nextInt(3) == 0 && this.level.getBlockState(fx8).isAir() && this.level.getBlockState(fx8.below()).isSolidRender(this.level, fx8.below())) {
                    this.level.setBlockAndUpdate(fx8, BaseFireBlock.getState(this.level, fx8));
                }
            }
        }
    }
    
    private static void addBlockDrops(final ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, final ItemStack bly, final BlockPos fx) {
        for (int integer4 = objectArrayList.size(), integer5 = 0; integer5 < integer4; ++integer5) {
            final Pair<ItemStack, BlockPos> pair6 = (Pair<ItemStack, BlockPos>)objectArrayList.get(integer5);
            final ItemStack bly2 = (ItemStack)pair6.getFirst();
            if (ItemEntity.areMergable(bly2, bly)) {
                final ItemStack bly3 = ItemEntity.merge(bly2, bly, 16);
                objectArrayList.set(integer5, Pair.of((Object)bly3, pair6.getSecond()));
                if (bly.isEmpty()) {
                    return;
                }
            }
        }
        objectArrayList.add(Pair.of((Object)bly, (Object)fx));
    }
    
    public DamageSource getDamageSource() {
        return this.damageSource;
    }
    
    public Map<Player, Vec3> getHitPlayers() {
        return this.hitPlayers;
    }
    
    @Nullable
    public LivingEntity getSourceMob() {
        if (this.source == null) {
            return null;
        }
        if (this.source instanceof PrimedTnt) {
            return ((PrimedTnt)this.source).getOwner();
        }
        if (this.source instanceof LivingEntity) {
            return (LivingEntity)this.source;
        }
        if (this.source instanceof Projectile) {
            final Entity apx2 = ((Projectile)this.source).getOwner();
            if (apx2 instanceof LivingEntity) {
                return (LivingEntity)apx2;
            }
        }
        return null;
    }
    
    public void clearToBlow() {
        this.toBlow.clear();
    }
    
    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }
    
    static {
        EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
    }
    
    public enum BlockInteraction {
        NONE, 
        BREAK, 
        DESTROY;
    }
}
