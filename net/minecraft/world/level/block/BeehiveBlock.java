package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.Util;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.Direction;

public class BeehiveBlock extends BaseEntityBlock {
    private static final Direction[] SPAWN_DIRECTIONS;
    public static final DirectionProperty FACING;
    public static final IntegerProperty HONEY_LEVEL;
    
    public BeehiveBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)BeehiveBlock.HONEY_LEVEL, 0)).<Comparable, Direction>setValue((Property<Comparable>)BeehiveBlock.FACING, Direction.NORTH));
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return cee.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL);
    }
    
    @Override
    public void playerDestroy(final Level bru, final Player bft, final BlockPos fx, final BlockState cee, @Nullable final BlockEntity ccg, final ItemStack bly) {
        super.playerDestroy(bru, bft, fx, cee, ccg, bly);
        if (!bru.isClientSide && ccg instanceof BeehiveBlockEntity) {
            final BeehiveBlockEntity ccd8 = (BeehiveBlockEntity)ccg;
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, bly) == 0) {
                ccd8.emptyAllLivingFromHive(bft, cee, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                bru.updateNeighbourForOutputSignal(fx, this);
                this.angerNearbyBees(bru, fx);
            }
            CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)bft, cee.getBlock(), bly, ccd8.getOccupantCount());
        }
    }
    
    private void angerNearbyBees(final Level bru, final BlockPos fx) {
        final List<Bee> list4 = bru.<Bee>getEntitiesOfClass((java.lang.Class<? extends Bee>)Bee.class, new AABB(fx).inflate(8.0, 6.0, 8.0));
        if (!list4.isEmpty()) {
            final List<Player> list5 = bru.<Player>getEntitiesOfClass((java.lang.Class<? extends Player>)Player.class, new AABB(fx).inflate(8.0, 6.0, 8.0));
            final int integer6 = list5.size();
            for (final Bee azx8 : list4) {
                if (azx8.getTarget() == null) {
                    azx8.setTarget((LivingEntity)list5.get(bru.random.nextInt(integer6)));
                }
            }
        }
    }
    
    public static void dropHoneycomb(final Level bru, final BlockPos fx) {
        Block.popResource(bru, fx, new ItemStack(Items.HONEYCOMB, 3));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final ItemStack bly8 = bft.getItemInHand(aoq);
        final int integer9 = cee.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL);
        boolean boolean10 = false;
        if (integer9 >= 5) {
            if (bly8.getItem() == Items.SHEARS) {
                bru.playSound(bft, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.NEUTRAL, 1.0f, 1.0f);
                dropHoneycomb(bru, fx);
                bly8.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
                boolean10 = true;
            }
            else if (bly8.getItem() == Items.GLASS_BOTTLE) {
                bly8.shrink(1);
                bru.playSound(bft, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0f, 1.0f);
                if (bly8.isEmpty()) {
                    bft.setItemInHand(aoq, new ItemStack(Items.HONEY_BOTTLE));
                }
                else if (!bft.inventory.add(new ItemStack(Items.HONEY_BOTTLE))) {
                    bft.drop(new ItemStack(Items.HONEY_BOTTLE), false);
                }
                boolean10 = true;
            }
        }
        if (boolean10) {
            if (!CampfireBlock.isSmokeyPos(bru, fx)) {
                if (this.hiveContainsBees(bru, fx)) {
                    this.angerNearbyBees(bru, fx);
                }
                this.releaseBeesAndResetHoneyLevel(bru, cee, fx, bft, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            }
            else {
                this.resetHoneyLevel(bru, cee, fx);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return super.use(cee, bru, fx, bft, aoq, dcg);
    }
    
    private boolean hiveContainsBees(final Level bru, final BlockPos fx) {
        final BlockEntity ccg4 = bru.getBlockEntity(fx);
        if (ccg4 instanceof BeehiveBlockEntity) {
            final BeehiveBlockEntity ccd5 = (BeehiveBlockEntity)ccg4;
            return !ccd5.isEmpty();
        }
        return false;
    }
    
    public void releaseBeesAndResetHoneyLevel(final Level bru, final BlockState cee, final BlockPos fx, @Nullable final Player bft, final BeehiveBlockEntity.BeeReleaseStatus b) {
        this.resetHoneyLevel(bru, cee, fx);
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof BeehiveBlockEntity) {
            final BeehiveBlockEntity ccd8 = (BeehiveBlockEntity)ccg7;
            ccd8.emptyAllLivingFromHive(bft, cee, b);
        }
    }
    
    public void resetHoneyLevel(final Level bru, final BlockState cee, final BlockPos fx) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)BeehiveBlock.HONEY_LEVEL, 0), 3);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL) >= 5) {
            for (int integer6 = 0; integer6 < random.nextInt(1) + 1; ++integer6) {
                this.trySpawnDripParticles(bru, fx, cee);
            }
        }
    }
    
    private void trySpawnDripParticles(final Level bru, final BlockPos fx, final BlockState cee) {
        if (!cee.getFluidState().isEmpty() || bru.random.nextFloat() < 0.3f) {
            return;
        }
        final VoxelShape dde5 = cee.getCollisionShape(bru, fx);
        final double double6 = dde5.max(Direction.Axis.Y);
        if (double6 >= 1.0 && !cee.is(BlockTags.IMPERMEABLE)) {
            final double double7 = dde5.min(Direction.Axis.Y);
            if (double7 > 0.0) {
                this.spawnParticle(bru, fx, dde5, fx.getY() + double7 - 0.05);
            }
            else {
                final BlockPos fx2 = fx.below();
                final BlockState cee2 = bru.getBlockState(fx2);
                final VoxelShape dde6 = cee2.getCollisionShape(bru, fx2);
                final double double8 = dde6.max(Direction.Axis.Y);
                if ((double8 < 1.0 || !cee2.isCollisionShapeFullBlock(bru, fx2)) && cee2.getFluidState().isEmpty()) {
                    this.spawnParticle(bru, fx, dde5, fx.getY() - 0.05);
                }
            }
        }
    }
    
    private void spawnParticle(final Level bru, final BlockPos fx, final VoxelShape dde, final double double4) {
        this.spawnFluidParticle(bru, fx.getX() + dde.min(Direction.Axis.X), fx.getX() + dde.max(Direction.Axis.X), fx.getZ() + dde.min(Direction.Axis.Z), fx.getZ() + dde.max(Direction.Axis.Z), double4);
    }
    
    private void spawnFluidParticle(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6) {
        bru.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(bru.random.nextDouble(), double2, double3), double6, Mth.lerp(bru.random.nextDouble(), double4, double5), 0.0, 0.0, 0.0);
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)BeehiveBlock.FACING, bnv.getHorizontalDirection().getOpposite());
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BeehiveBlock.HONEY_LEVEL, BeehiveBlock.FACING);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new BeehiveBlockEntity();
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide && bft.isCreative() && bru.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            final BlockEntity ccg6 = bru.getBlockEntity(fx);
            if (ccg6 instanceof BeehiveBlockEntity) {
                final BeehiveBlockEntity ccd7 = (BeehiveBlockEntity)ccg6;
                final ItemStack bly8 = new ItemStack(this);
                final int integer9 = cee.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL);
                final boolean boolean10 = !ccd7.isEmpty();
                if (!boolean10 && integer9 == 0) {
                    return;
                }
                if (boolean10) {
                    final CompoundTag md11 = new CompoundTag();
                    md11.put("Bees", (net.minecraft.nbt.Tag)ccd7.writeBees());
                    bly8.addTagElement("BlockEntityTag", (net.minecraft.nbt.Tag)md11);
                }
                final CompoundTag md11 = new CompoundTag();
                md11.putInt("honey_level", integer9);
                bly8.addTagElement("BlockStateTag", (net.minecraft.nbt.Tag)md11);
                final ItemEntity bcs12 = new ItemEntity(bru, fx.getX(), fx.getY(), fx.getZ(), bly8);
                bcs12.setDefaultPickUpDelay();
                bru.addFreshEntity(bcs12);
            }
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Override
    public List<ItemStack> getDrops(final BlockState cee, final LootContext.Builder a) {
        final Entity apx4 = a.<Entity>getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (apx4 instanceof PrimedTnt || apx4 instanceof Creeper || apx4 instanceof WitherSkull || apx4 instanceof WitherBoss || apx4 instanceof MinecartTNT) {
            final BlockEntity ccg5 = a.<BlockEntity>getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (ccg5 instanceof BeehiveBlockEntity) {
                final BeehiveBlockEntity ccd6 = (BeehiveBlockEntity)ccg5;
                ccd6.emptyAllLivingFromHive(null, cee, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            }
        }
        return super.getDrops(cee, a);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (brv.getBlockState(fx6).getBlock() instanceof FireBlock) {
            final BlockEntity ccg8 = brv.getBlockEntity(fx5);
            if (ccg8 instanceof BeehiveBlockEntity) {
                final BeehiveBlockEntity ccd9 = (BeehiveBlockEntity)ccg8;
                ccd9.emptyAllLivingFromHive(null, cee1, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            }
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public static Direction getRandomOffset(final Random random) {
        return Util.<Direction>getRandom(BeehiveBlock.SPAWN_DIRECTIONS, random);
    }
    
    static {
        SPAWN_DIRECTIONS = new Direction[] { Direction.WEST, Direction.EAST, Direction.SOUTH };
        FACING = HorizontalDirectionalBlock.FACING;
        HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;
    }
}
