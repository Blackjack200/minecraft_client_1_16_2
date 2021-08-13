package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SweetBerryBushBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    private static final VoxelShape SAPLING_SHAPE;
    private static final VoxelShape MID_GROWTH_SHAPE;
    
    public SweetBerryBushBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, 0));
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack(Items.SWEET_BERRIES);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) == 0) {
            return SweetBerryBushBlock.SAPLING_SHAPE;
        }
        if (cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) < 3) {
            return SweetBerryBushBlock.MID_GROWTH_SHAPE;
        }
        return super.getShape(cee, bqz, fx, dcp);
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) < 3;
    }
    
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final int integer6 = cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE);
        if (integer6 < 3 && random.nextInt(5) == 0 && aag.getRawBrightness(fx.above(), 0) >= 9) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, integer6 + 1), 2);
        }
    }
    
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (!(apx instanceof LivingEntity) || apx.getType() == EntityType.FOX || apx.getType() == EntityType.BEE) {
            return;
        }
        apx.makeStuckInBlock(cee, new Vec3(0.800000011920929, 0.75, 0.800000011920929));
        if (!bru.isClientSide && cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) > 0 && (apx.xOld != apx.getX() || apx.zOld != apx.getZ())) {
            final double double6 = Math.abs(apx.getX() - apx.xOld);
            final double double7 = Math.abs(apx.getZ() - apx.zOld);
            if (double6 >= 0.003000000026077032 || double7 >= 0.003000000026077032) {
                apx.hurt(DamageSource.SWEET_BERRY_BUSH, 1.0f);
            }
        }
    }
    
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final int integer8 = cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE);
        final boolean boolean9 = integer8 == 3;
        if (!boolean9 && bft.getItemInHand(aoq).getItem() == Items.BONE_MEAL) {
            return InteractionResult.PASS;
        }
        if (integer8 > 1) {
            final int integer9 = 1 + bru.random.nextInt(2);
            Block.popResource(bru, fx, new ItemStack(Items.SWEET_BERRIES, integer9 + (boolean9 ? 1 : 0)));
            bru.playSound(null, fx, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 0.8f + bru.random.nextFloat() * 0.4f);
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, 1), 2);
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return super.use(cee, bru, fx, bft, aoq, dcg);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SweetBerryBushBlock.AGE);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) < 3;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        final int integer6 = Math.min(3, cee.<Integer>getValue((Property<Integer>)SweetBerryBushBlock.AGE) + 1);
        aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, integer6), 2);
    }
    
    static {
        AGE = BlockStateProperties.AGE_3;
        SAPLING_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
        MID_GROWTH_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }
}
