package net.minecraft.core.dispenser;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.entity.vehicle.Boat;
import java.util.Random;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import java.util.Iterator;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import java.util.List;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Saddleable;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.Snowball;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;

public interface DispenseItemBehavior {
    public static final DispenseItemBehavior NOOP = (fy, bly) -> bly;
    
    ItemStack dispense(final BlockSource fy, final ItemStack bly);
    
    default void bootStrap() {
        DispenserBlock.registerBehavior(Items.ARROW, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                final Arrow bfz5 = new Arrow(bru, gk.x(), gk.y(), gk.z());
                bfz5.pickup = AbstractArrow.Pickup.ALLOWED;
                return bfz5;
            }
        });
        DispenserBlock.registerBehavior(Items.TIPPED_ARROW, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                final Arrow bfz5 = new Arrow(bru, gk.x(), gk.y(), gk.z());
                bfz5.setEffectsFromItem(bly);
                bfz5.pickup = AbstractArrow.Pickup.ALLOWED;
                return bfz5;
            }
        });
        DispenserBlock.registerBehavior(Items.SPECTRAL_ARROW, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                final AbstractArrow bfx5 = new SpectralArrow(bru, gk.x(), gk.y(), gk.z());
                bfx5.pickup = AbstractArrow.Pickup.ALLOWED;
                return bfx5;
            }
        });
        DispenserBlock.registerBehavior(Items.EGG, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                return Util.<ThrownEgg>make(new ThrownEgg(bru, gk.x(), gk.y(), gk.z()), (java.util.function.Consumer<ThrownEgg>)(bgr -> bgr.setItem(bly)));
            }
        });
        DispenserBlock.registerBehavior(Items.SNOWBALL, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                return Util.<Snowball>make(new Snowball(bru, gk.x(), gk.y(), gk.z()), (java.util.function.Consumer<Snowball>)(bgn -> bgn.setItem(bly)));
            }
        });
        DispenserBlock.registerBehavior(Items.EXPERIENCE_BOTTLE, new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                return Util.<ThrownExperienceBottle>make(new ThrownExperienceBottle(bru, gk.x(), gk.y(), gk.z()), (java.util.function.Consumer<ThrownExperienceBottle>)(bgt -> bgt.setItem(bly)));
            }
            
            @Override
            protected float getUncertainty() {
                return super.getUncertainty() * 0.5f;
            }
            
            @Override
            protected float getPower() {
                return super.getPower() * 1.25f;
            }
        });
        DispenserBlock.registerBehavior(Items.SPLASH_POTION, new DispenseItemBehavior() {
            public ItemStack dispense(final BlockSource fy, final ItemStack bly) {
                return new AbstractProjectileDispenseBehavior() {
                    @Override
                    protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                        return Util.<ThrownPotion>make(new ThrownPotion(bru, gk.x(), gk.y(), gk.z()), (java.util.function.Consumer<ThrownPotion>)(bgu -> bgu.setItem(bly)));
                    }
                    
                    @Override
                    protected float getUncertainty() {
                        return super.getUncertainty() * 0.5f;
                    }
                    
                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25f;
                    }
                }.dispense(fy, bly);
            }
        });
        DispenserBlock.registerBehavior(Items.LINGERING_POTION, new DispenseItemBehavior() {
            public ItemStack dispense(final BlockSource fy, final ItemStack bly) {
                return new AbstractProjectileDispenseBehavior() {
                    @Override
                    protected Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly) {
                        return Util.<ThrownPotion>make(new ThrownPotion(bru, gk.x(), gk.y(), gk.z()), (java.util.function.Consumer<ThrownPotion>)(bgu -> bgu.setItem(bly)));
                    }
                    
                    @Override
                    protected float getUncertainty() {
                        return super.getUncertainty() * 0.5f;
                    }
                    
                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25f;
                    }
                }.dispense(fy, bly);
            }
        });
        final DefaultDispenseItemBehavior gv1 = new DefaultDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final EntityType<?> aqb5 = ((SpawnEggItem)bly.getItem()).getType(bly.getTag());
                aqb5.spawn(fy.getLevel(), bly, null, fy.getPos().relative(gc4), MobSpawnType.DISPENSER, gc4 != Direction.UP, false);
                bly.shrink(1);
                return bly;
            }
        };
        for (final SpawnEggItem bmx3 : SpawnEggItem.eggs()) {
            DispenserBlock.registerBehavior(bmx3, gv1);
        }
        DispenserBlock.registerBehavior(Items.ARMOR_STAND, new DefaultDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final BlockPos fx5 = fy.getPos().relative(gc4);
                final Level bru6 = fy.getLevel();
                final ArmorStand bck7 = new ArmorStand(bru6, fx5.getX() + 0.5, fx5.getY(), fx5.getZ() + 0.5);
                EntityType.updateCustomEntityTag(bru6, null, bck7, bly.getTag());
                bck7.yRot = gc4.toYRot();
                bru6.addFreshEntity(bck7);
                bly.shrink(1);
                return bly;
            }
        });
        DispenserBlock.registerBehavior(Items.SADDLE, new OptionalDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final BlockPos fx4 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final List<LivingEntity> list5 = fy.getLevel().<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, new AABB(fx4), (java.util.function.Predicate<? super LivingEntity>)(aqj -> {
                    if (aqj instanceof Saddleable) {
                        final Saddleable aqx2 = (Saddleable)aqj;
                        return !aqx2.isSaddled() && aqx2.isSaddleable();
                    }
                    return false;
                }));
                if (!list5.isEmpty()) {
                    ((Saddleable)list5.get(0)).equipSaddle(SoundSource.BLOCKS);
                    bly.shrink(1);
                    this.setSuccess(true);
                    return bly;
                }
                return super.execute(fy, bly);
            }
        });
        final DefaultDispenseItemBehavior gv2 = new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final BlockPos fx4 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final List<AbstractHorse> list5 = fy.getLevel().<AbstractHorse>getEntitiesOfClass((java.lang.Class<? extends AbstractHorse>)AbstractHorse.class, new AABB(fx4), (java.util.function.Predicate<? super AbstractHorse>)(bay -> bay.isAlive() && bay.canWearArmor()));
                for (final AbstractHorse bay7 : list5) {
                    if (bay7.isArmor(bly) && !bay7.isWearingArmor() && bay7.isTamed()) {
                        bay7.setSlot(401, bly.split(1));
                        this.setSuccess(true);
                        return bly;
                    }
                }
                return super.execute(fy, bly);
            }
        };
        DispenserBlock.registerBehavior(Items.LEATHER_HORSE_ARMOR, gv2);
        DispenserBlock.registerBehavior(Items.IRON_HORSE_ARMOR, gv2);
        DispenserBlock.registerBehavior(Items.GOLDEN_HORSE_ARMOR, gv2);
        DispenserBlock.registerBehavior(Items.DIAMOND_HORSE_ARMOR, gv2);
        DispenserBlock.registerBehavior(Items.WHITE_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.ORANGE_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.CYAN_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.BLUE_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.BROWN_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.BLACK_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.GRAY_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.GREEN_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.LIGHT_BLUE_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.LIGHT_GRAY_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.LIME_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.MAGENTA_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.PINK_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.PURPLE_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.RED_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.YELLOW_CARPET, gv2);
        DispenserBlock.registerBehavior(Items.CHEST, new OptionalDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final BlockPos fx4 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final List<AbstractChestedHorse> list5 = fy.getLevel().<AbstractChestedHorse>getEntitiesOfClass((java.lang.Class<? extends AbstractChestedHorse>)AbstractChestedHorse.class, new AABB(fx4), (java.util.function.Predicate<? super AbstractChestedHorse>)(bax -> bax.isAlive() && !bax.hasChest()));
                for (final AbstractChestedHorse bax7 : list5) {
                    if (bax7.isTamed() && bax7.setSlot(499, bly)) {
                        bly.shrink(1);
                        this.setSuccess(true);
                        return bly;
                    }
                }
                return super.execute(fy, bly);
            }
        });
        DispenserBlock.registerBehavior(Items.FIREWORK_ROCKET, new DefaultDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final FireworkRocketEntity bge5 = new FireworkRocketEntity(fy.getLevel(), bly, fy.x(), fy.y(), fy.x(), true);
                DispenseItemBehavior.setEntityPokingOutOfBlock(fy, bge5, gc4);
                bge5.shoot(gc4.getStepX(), gc4.getStepY(), gc4.getStepZ(), 0.5f, 1.0f);
                fy.getLevel().addFreshEntity(bge5);
                bly.shrink(1);
                return bly;
            }
            
            @Override
            protected void playSound(final BlockSource fy) {
                fy.getLevel().levelEvent(1004, fy.getPos(), 0);
            }
        });
        DispenserBlock.registerBehavior(Items.FIRE_CHARGE, new DefaultDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final Position gk5 = DispenserBlock.getDispensePosition(fy);
                final double double6 = gk5.x() + gc4.getStepX() * 0.3f;
                final double double7 = gk5.y() + gc4.getStepY() * 0.3f;
                final double double8 = gk5.z() + gc4.getStepZ() * 0.3f;
                final Level bru12 = fy.getLevel();
                final Random random13 = bru12.random;
                final double double9 = random13.nextGaussian() * 0.05 + gc4.getStepX();
                final double double10 = random13.nextGaussian() * 0.05 + gc4.getStepY();
                final double double11 = random13.nextGaussian() * 0.05 + gc4.getStepZ();
                bru12.addFreshEntity(Util.<SmallFireball>make(new SmallFireball(bru12, double6, double7, double8, double9, double10, double11), (java.util.function.Consumer<SmallFireball>)(bgm -> bgm.setItem(bly))));
                bly.shrink(1);
                return bly;
            }
            
            @Override
            protected void playSound(final BlockSource fy) {
                fy.getLevel().levelEvent(1018, fy.getPos(), 0);
            }
        });
        DispenserBlock.registerBehavior(Items.OAK_BOAT, new BoatDispenseItemBehavior(Boat.Type.OAK));
        DispenserBlock.registerBehavior(Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(Boat.Type.SPRUCE));
        DispenserBlock.registerBehavior(Items.BIRCH_BOAT, new BoatDispenseItemBehavior(Boat.Type.BIRCH));
        DispenserBlock.registerBehavior(Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(Boat.Type.JUNGLE));
        DispenserBlock.registerBehavior(Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(Boat.Type.DARK_OAK));
        DispenserBlock.registerBehavior(Items.ACACIA_BOAT, new BoatDispenseItemBehavior(Boat.Type.ACACIA));
        final DispenseItemBehavior gw3 = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
            
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final BucketItem bkl4 = (BucketItem)bly.getItem();
                final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final Level bru6 = fy.getLevel();
                if (bkl4.emptyBucket(null, bru6, fx5, null)) {
                    bkl4.checkExtraContent(bru6, bly, fx5);
                    return new ItemStack(Items.BUCKET);
                }
                return this.defaultDispenseItemBehavior.dispense(fy, bly);
            }
        };
        DispenserBlock.registerBehavior(Items.LAVA_BUCKET, gw3);
        DispenserBlock.registerBehavior(Items.WATER_BUCKET, gw3);
        DispenserBlock.registerBehavior(Items.SALMON_BUCKET, gw3);
        DispenserBlock.registerBehavior(Items.COD_BUCKET, gw3);
        DispenserBlock.registerBehavior(Items.PUFFERFISH_BUCKET, gw3);
        DispenserBlock.registerBehavior(Items.TROPICAL_FISH_BUCKET, gw3);
        DispenserBlock.registerBehavior(Items.BUCKET, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
            
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final LevelAccessor brv4 = fy.getLevel();
                final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final BlockState cee6 = brv4.getBlockState(fx5);
                final Block bul7 = cee6.getBlock();
                if (!(bul7 instanceof BucketPickup)) {
                    return super.execute(fy, bly);
                }
                final Fluid cut9 = ((BucketPickup)bul7).takeLiquid(brv4, fx5, cee6);
                if (!(cut9 instanceof FlowingFluid)) {
                    return super.execute(fy, bly);
                }
                final Item blu8 = cut9.getBucket();
                bly.shrink(1);
                if (bly.isEmpty()) {
                    return new ItemStack(blu8);
                }
                if (fy.<DispenserBlockEntity>getEntity().addItem(new ItemStack(blu8)) < 0) {
                    this.defaultDispenseItemBehavior.dispense(fy, new ItemStack(blu8));
                }
                return bly;
            }
        });
        DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Level bru4 = fy.getLevel();
                this.setSuccess(true);
                final Direction gc5 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final BlockPos fx6 = fy.getPos().relative(gc5);
                final BlockState cee7 = bru4.getBlockState(fx6);
                if (BaseFireBlock.canBePlacedAt(bru4, fx6, gc5)) {
                    bru4.setBlockAndUpdate(fx6, BaseFireBlock.getState(bru4, fx6));
                }
                else if (CampfireBlock.canLight(cee7)) {
                    bru4.setBlockAndUpdate(fx6, ((StateHolder<O, BlockState>)cee7).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.LIT, true));
                }
                else if (cee7.getBlock() instanceof TntBlock) {
                    TntBlock.explode(bru4, fx6);
                    bru4.removeBlock(fx6, false);
                }
                else {
                    this.setSuccess(false);
                }
                if (this.isSuccess() && bly.hurt(1, bru4.random, null)) {
                    bly.setCount(0);
                }
                return bly;
            }
        });
        DispenserBlock.registerBehavior(Items.BONE_MEAL, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                this.setSuccess(true);
                final Level bru4 = fy.getLevel();
                final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                if (BoneMealItem.growCrop(bly, bru4, fx5) || BoneMealItem.growWaterPlant(bly, bru4, fx5, null)) {
                    if (!bru4.isClientSide) {
                        bru4.levelEvent(2005, fx5, 0);
                    }
                }
                else {
                    this.setSuccess(false);
                }
                return bly;
            }
        });
        DispenserBlock.registerBehavior(Blocks.TNT, new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Level bru4 = fy.getLevel();
                final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final PrimedTnt bct6 = new PrimedTnt(bru4, fx5.getX() + 0.5, fx5.getY(), fx5.getZ() + 0.5, null);
                bru4.addFreshEntity(bct6);
                bru4.playSound(null, bct6.getX(), bct6.getY(), bct6.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
                bly.shrink(1);
                return bly;
            }
        });
        final DispenseItemBehavior gw4 = new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                this.setSuccess(ArmorItem.dispenseArmor(fy, bly));
                return bly;
            }
        };
        DispenserBlock.registerBehavior(Items.CREEPER_HEAD, gw4);
        DispenserBlock.registerBehavior(Items.ZOMBIE_HEAD, gw4);
        DispenserBlock.registerBehavior(Items.DRAGON_HEAD, gw4);
        DispenserBlock.registerBehavior(Items.SKELETON_SKULL, gw4);
        DispenserBlock.registerBehavior(Items.PLAYER_HEAD, gw4);
        DispenserBlock.registerBehavior(Items.WITHER_SKELETON_SKULL, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Level bru4 = fy.getLevel();
                final Direction gc5 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final BlockPos fx6 = fy.getPos().relative(gc5);
                if (bru4.isEmptyBlock(fx6) && WitherSkullBlock.canSpawnMob(bru4, fx6, bly)) {
                    bru4.setBlock(fx6, ((StateHolder<O, BlockState>)Blocks.WITHER_SKELETON_SKULL.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)SkullBlock.ROTATION, (gc5.getAxis() == Direction.Axis.Y) ? 0 : (gc5.getOpposite().get2DDataValue() * 4)), 3);
                    final BlockEntity ccg7 = bru4.getBlockEntity(fx6);
                    if (ccg7 instanceof SkullBlockEntity) {
                        WitherSkullBlock.checkSpawn(bru4, fx6, (SkullBlockEntity)ccg7);
                    }
                    bly.shrink(1);
                    this.setSuccess(true);
                }
                else {
                    this.setSuccess(ArmorItem.dispenseArmor(fy, bly));
                }
                return bly;
            }
        });
        DispenserBlock.registerBehavior(Blocks.CARVED_PUMPKIN, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Level bru4 = fy.getLevel();
                final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final CarvedPumpkinBlock buy6 = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
                if (bru4.isEmptyBlock(fx5) && buy6.canSpawnGolem(bru4, fx5)) {
                    if (!bru4.isClientSide) {
                        bru4.setBlock(fx5, buy6.defaultBlockState(), 3);
                    }
                    bly.shrink(1);
                    this.setSuccess(true);
                }
                else {
                    this.setSuccess(ArmorItem.dispenseArmor(fy, bly));
                }
                return bly;
            }
        });
        DispenserBlock.registerBehavior(Blocks.SHULKER_BOX.asItem(), new ShulkerBoxDispenseBehavior());
        for (final DyeColor bku8 : DyeColor.values()) {
            DispenserBlock.registerBehavior(ShulkerBoxBlock.getBlockByColor(bku8).asItem(), new ShulkerBoxDispenseBehavior());
        }
        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE.asItem(), new OptionalDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
            
            private ItemStack takeLiquid(final BlockSource fy, final ItemStack bly2, final ItemStack bly3) {
                bly2.shrink(1);
                if (bly2.isEmpty()) {
                    return bly3.copy();
                }
                if (fy.<DispenserBlockEntity>getEntity().addItem(bly3.copy()) < 0) {
                    this.defaultDispenseItemBehavior.dispense(fy, bly3.copy());
                }
                return bly2;
            }
            
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                this.setSuccess(false);
                final ServerLevel aag4 = fy.getLevel();
                final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
                final BlockState cee6 = aag4.getBlockState(fx5);
                if (cee6.is(BlockTags.BEEHIVES, (Predicate<BlockBehaviour.BlockStateBase>)(a -> a.<Comparable>hasProperty((Property<Comparable>)BeehiveBlock.HONEY_LEVEL))) && cee6.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL) >= 5) {
                    ((BeehiveBlock)cee6.getBlock()).releaseBeesAndResetHoneyLevel(aag4, cee6, fx5, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
                    this.setSuccess(true);
                    return this.takeLiquid(fy, bly, new ItemStack(Items.HONEY_BOTTLE));
                }
                if (aag4.getFluidState(fx5).is(FluidTags.WATER)) {
                    this.setSuccess(true);
                    return this.takeLiquid(fy, bly, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                }
                return super.execute(fy, bly);
            }
        });
        DispenserBlock.registerBehavior(Items.GLOWSTONE, new OptionalDispenseItemBehavior() {
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final BlockPos fx5 = fy.getPos().relative(gc4);
                final Level bru6 = fy.getLevel();
                final BlockState cee7 = bru6.getBlockState(fx5);
                this.setSuccess(true);
                if (cee7.is(Blocks.RESPAWN_ANCHOR)) {
                    if (cee7.<Integer>getValue((Property<Integer>)RespawnAnchorBlock.CHARGE) != 4) {
                        RespawnAnchorBlock.charge(bru6, fx5, cee7);
                        bly.shrink(1);
                    }
                    else {
                        this.setSuccess(false);
                    }
                    return bly;
                }
                return super.execute(fy, bly);
            }
        });
        DispenserBlock.registerBehavior(Items.SHEARS.asItem(), new ShearsDispenseItemBehavior());
    }
    
    default void setEntityPokingOutOfBlock(final BlockSource fy, final Entity apx, final Direction gc) {
        apx.setPos(fy.x() + gc.getStepX() * (0.5000099999997474 - apx.getBbWidth() / 2.0), fy.y() + gc.getStepY() * (0.5000099999997474 - apx.getBbHeight() / 2.0) - apx.getBbHeight() / 2.0, fy.z() + gc.getStepZ() * (0.5000099999997474 - apx.getBbWidth() / 2.0));
    }
}
