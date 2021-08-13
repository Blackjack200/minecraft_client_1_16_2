package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TurtleEggBlock extends Block {
    private static final VoxelShape ONE_EGG_AABB;
    private static final VoxelShape MULTIPLE_EGGS_AABB;
    public static final IntegerProperty HATCH;
    public static final IntegerProperty EGGS;
    
    public TurtleEggBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)TurtleEggBlock.HATCH, 0)).<Comparable, Integer>setValue((Property<Comparable>)TurtleEggBlock.EGGS, 1));
    }
    
    @Override
    public void stepOn(final Level bru, final BlockPos fx, final Entity apx) {
        this.destroyEgg(bru, fx, apx, 100);
        super.stepOn(bru, fx, apx);
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        if (!(apx instanceof Zombie)) {
            this.destroyEgg(bru, fx, apx, 3);
        }
        super.fallOn(bru, fx, apx, float4);
    }
    
    private void destroyEgg(final Level bru, final BlockPos fx, final Entity apx, final int integer) {
        if (!this.canDestroyEgg(bru, apx)) {
            return;
        }
        if (!bru.isClientSide && bru.random.nextInt(integer) == 0) {
            final BlockState cee6 = bru.getBlockState(fx);
            if (cee6.is(Blocks.TURTLE_EGG)) {
                this.decreaseEggs(bru, fx, cee6);
            }
        }
    }
    
    private void decreaseEggs(final Level bru, final BlockPos fx, final BlockState cee) {
        bru.playSound(null, fx, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7f, 0.9f + bru.random.nextFloat() * 0.2f);
        final int integer5 = cee.<Integer>getValue((Property<Integer>)TurtleEggBlock.EGGS);
        if (integer5 <= 1) {
            bru.destroyBlock(fx, false);
        }
        else {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)TurtleEggBlock.EGGS, integer5 - 1), 2);
            bru.levelEvent(2001, fx, Block.getId(cee));
        }
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (this.shouldUpdateHatchLevel(aag) && onSand(aag, fx)) {
            final int integer6 = cee.<Integer>getValue((Property<Integer>)TurtleEggBlock.HATCH);
            if (integer6 < 2) {
                aag.playSound(null, fx, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)TurtleEggBlock.HATCH, integer6 + 1), 2);
            }
            else {
                aag.playSound(null, fx, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                aag.removeBlock(fx, false);
                for (int integer7 = 0; integer7 < cee.<Integer>getValue((Property<Integer>)TurtleEggBlock.EGGS); ++integer7) {
                    aag.levelEvent(2001, fx, Block.getId(cee));
                    final Turtle bau8 = EntityType.TURTLE.create(aag);
                    bau8.setAge(-24000);
                    bau8.setHomePos(fx);
                    bau8.moveTo(fx.getX() + 0.3 + integer7 * 0.2, fx.getY(), fx.getZ() + 0.3, 0.0f, 0.0f);
                    aag.addFreshEntity(bau8);
                }
            }
        }
    }
    
    public static boolean onSand(final BlockGetter bqz, final BlockPos fx) {
        return isSand(bqz, fx.below());
    }
    
    public static boolean isSand(final BlockGetter bqz, final BlockPos fx) {
        return bqz.getBlockState(fx).is(BlockTags.SAND);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (onSand(bru, fx) && !bru.isClientSide) {
            bru.levelEvent(2005, fx, 0);
        }
    }
    
    private boolean shouldUpdateHatchLevel(final Level bru) {
        final float float3 = bru.getTimeOfDay(1.0f);
        return (float3 < 0.69 && float3 > 0.65) || bru.random.nextInt(500) == 0;
    }
    
    @Override
    public void playerDestroy(final Level bru, final Player bft, final BlockPos fx, final BlockState cee, @Nullable final BlockEntity ccg, final ItemStack bly) {
        super.playerDestroy(bru, bft, fx, cee, ccg, bly);
        this.decreaseEggs(bru, fx, cee);
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        return (bnv.getItemInHand().getItem() == this.asItem() && cee.<Integer>getValue((Property<Integer>)TurtleEggBlock.EGGS) < 4) || super.canBeReplaced(cee, bnv);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = bnv.getLevel().getBlockState(bnv.getClickedPos());
        if (cee3.is(this)) {
            return ((StateHolder<O, BlockState>)cee3).<Comparable, Integer>setValue((Property<Comparable>)TurtleEggBlock.EGGS, Math.min(4, cee3.<Integer>getValue((Property<Integer>)TurtleEggBlock.EGGS) + 1));
        }
        return super.getStateForPlacement(bnv);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (cee.<Integer>getValue((Property<Integer>)TurtleEggBlock.EGGS) > 1) {
            return TurtleEggBlock.MULTIPLE_EGGS_AABB;
        }
        return TurtleEggBlock.ONE_EGG_AABB;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(TurtleEggBlock.HATCH, TurtleEggBlock.EGGS);
    }
    
    private boolean canDestroyEgg(final Level bru, final Entity apx) {
        return !(apx instanceof Turtle) && !(apx instanceof Bat) && apx instanceof LivingEntity && (apx instanceof Player || bru.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING));
    }
    
    static {
        ONE_EGG_AABB = Block.box(3.0, 0.0, 3.0, 12.0, 7.0, 12.0);
        MULTIPLE_EGGS_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);
        HATCH = BlockStateProperties.HATCH;
        EGGS = BlockStateProperties.EGGS;
    }
}
