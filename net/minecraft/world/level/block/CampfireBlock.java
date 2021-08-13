package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.function.Predicate;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.Random;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    protected static final VoxelShape SHAPE;
    public static final BooleanProperty LIT;
    public static final BooleanProperty SIGNAL_FIRE;
    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    private static final VoxelShape VIRTUAL_FENCE_POST;
    private final boolean spawnParticles;
    private final int fireDamage;
    
    public CampfireBlock(final boolean boolean1, final int integer, final Properties c) {
        super(c);
        this.spawnParticles = boolean1;
        this.fireDamage = integer;
        this.registerDefaultState((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)CampfireBlock.LIT, true)).setValue((Property<Comparable>)CampfireBlock.SIGNAL_FIRE, false)).setValue((Property<Comparable>)CampfireBlock.WATERLOGGED, false)).<Comparable, Direction>setValue((Property<Comparable>)CampfireBlock.FACING, Direction.NORTH));
    }
    
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof CampfireBlockEntity) {
            final CampfireBlockEntity ccj9 = (CampfireBlockEntity)ccg8;
            final ItemStack bly10 = bft.getItemInHand(aoq);
            final Optional<CampfireCookingRecipe> optional11 = ccj9.getCookableRecipe(bly10);
            if (optional11.isPresent()) {
                if (!bru.isClientSide && ccj9.placeFood(bft.abilities.instabuild ? bly10.copy() : bly10, ((CampfireCookingRecipe)optional11.get()).getCookingTime())) {
                    bft.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }
    
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (!apx.fireImmune() && cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.LIT) && apx instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)apx)) {
            apx.hurt(DamageSource.IN_FIRE, (float)this.fireDamage);
        }
        super.entityInside(cee, bru, fx, apx);
    }
    
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof CampfireBlockEntity) {
            Containers.dropContents(bru, fx, ((CampfireBlockEntity)ccg7).getItems());
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final LevelAccessor brv3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final boolean boolean5 = brv3.getFluidState(fx4).getType() == Fluids.WATER;
        return (((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)CampfireBlock.WATERLOGGED, boolean5)).setValue((Property<Comparable>)CampfireBlock.SIGNAL_FIRE, this.isSmokeSource(brv3.getBlockState(fx4.below())))).setValue((Property<Comparable>)CampfireBlock.LIT, !boolean5)).<Comparable, Direction>setValue((Property<Comparable>)CampfireBlock.FACING, bnv.getHorizontalDirection());
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)CampfireBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc == Direction.DOWN) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)CampfireBlock.SIGNAL_FIRE, this.isSmokeSource(cee3));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    private boolean isSmokeSource(final BlockState cee) {
        return cee.is(Blocks.HAY_BLOCK);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return CampfireBlock.SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.LIT)) {
            return;
        }
        if (random.nextInt(10) == 0) {
            bru.playLocalSound(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
        if (this.spawnParticles && random.nextInt(5) == 0) {
            for (int integer6 = 0; integer6 < random.nextInt(1) + 1; ++integer6) {
                bru.addParticle(ParticleTypes.LAVA, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
            }
        }
    }
    
    public static void dowse(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        if (brv.isClientSide()) {
            for (int integer4 = 0; integer4 < 20; ++integer4) {
                makeParticles((Level)brv, fx, cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.SIGNAL_FIRE), true);
            }
        }
        final BlockEntity ccg4 = brv.getBlockEntity(fx);
        if (ccg4 instanceof CampfireBlockEntity) {
            ((CampfireBlockEntity)ccg4).dowse();
        }
    }
    
    @Override
    public boolean placeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        if (!cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED) && cuu.getType() == Fluids.WATER) {
            final boolean boolean6 = cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.LIT);
            if (boolean6) {
                if (!brv.isClientSide()) {
                    brv.playSound(null, fx, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                dowse(brv, fx, cee);
            }
            brv.setBlock(fx, (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)CampfireBlock.WATERLOGGED, true)).<Comparable, Boolean>setValue((Property<Comparable>)CampfireBlock.LIT, false), 3);
            brv.getLiquidTicks().scheduleTick(fx, cuu.getType(), cuu.getType().getTickDelay(brv));
            return true;
        }
        return false;
    }
    
    public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
        if (!bru.isClientSide && bgj.isOnFire()) {
            final Entity apx6 = bgj.getOwner();
            final boolean boolean7 = apx6 == null || apx6 instanceof Player || bru.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            if (boolean7 && !cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.LIT) && !cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.WATERLOGGED)) {
                final BlockPos fx8 = dcg.getBlockPos();
                bru.setBlock(fx8, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.LIT, true), 11);
            }
        }
    }
    
    public static void makeParticles(final Level bru, final BlockPos fx, final boolean boolean3, final boolean boolean4) {
        final Random random5 = bru.getRandom();
        final SimpleParticleType hi6 = boolean3 ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        bru.addAlwaysVisibleParticle(hi6, true, fx.getX() + 0.5 + random5.nextDouble() / 3.0 * (random5.nextBoolean() ? 1 : -1), fx.getY() + random5.nextDouble() + random5.nextDouble(), fx.getZ() + 0.5 + random5.nextDouble() / 3.0 * (random5.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
        if (boolean4) {
            bru.addParticle(ParticleTypes.SMOKE, fx.getX() + 0.25 + random5.nextDouble() / 2.0 * (random5.nextBoolean() ? 1 : -1), fx.getY() + 0.4, fx.getZ() + 0.25 + random5.nextDouble() / 2.0 * (random5.nextBoolean() ? 1 : -1), 0.0, 0.005, 0.0);
        }
    }
    
    public static boolean isSmokeyPos(final Level bru, final BlockPos fx) {
        for (int integer3 = 1; integer3 <= 5; ++integer3) {
            final BlockPos fx2 = fx.below(integer3);
            final BlockState cee5 = bru.getBlockState(fx2);
            if (isLitCampfire(cee5)) {
                return true;
            }
            final boolean boolean6 = Shapes.joinIsNotEmpty(CampfireBlock.VIRTUAL_FENCE_POST, cee5.getCollisionShape(bru, fx, CollisionContext.empty()), BooleanOp.AND);
            if (boolean6) {
                final BlockState cee6 = bru.getBlockState(fx2.below());
                return isLitCampfire(cee6);
            }
        }
        return false;
    }
    
    public static boolean isLitCampfire(final BlockState cee) {
        return cee.<Comparable>hasProperty((Property<Comparable>)CampfireBlock.LIT) && cee.is(BlockTags.CAMPFIRES) && cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.LIT);
    }
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)CampfireBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)CampfireBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)CampfireBlock.FACING)));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)CampfireBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CampfireBlock.LIT, CampfireBlock.SIGNAL_FIRE, CampfireBlock.WATERLOGGED, CampfireBlock.FACING);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new CampfireBlockEntity();
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    public static boolean canLight(final BlockState cee) {
        return cee.is(BlockTags.CAMPFIRES, (Predicate<BlockStateBase>)(a -> a.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.WATERLOGGED) && a.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.LIT))) && !cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED) && !cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.LIT);
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
        LIT = BlockStateProperties.LIT;
        SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        VIRTUAL_FENCE_POST = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    }
}
