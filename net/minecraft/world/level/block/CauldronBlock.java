package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CauldronBlock extends Block {
    public static final IntegerProperty LEVEL;
    private static final VoxelShape INSIDE;
    protected static final VoxelShape SHAPE;
    
    public CauldronBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)CauldronBlock.LEVEL, 0));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return CauldronBlock.SHAPE;
    }
    
    @Override
    public VoxelShape getInteractionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return CauldronBlock.INSIDE;
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        final int integer6 = cee.<Integer>getValue((Property<Integer>)CauldronBlock.LEVEL);
        final float float7 = fx.getY() + (6.0f + 3 * integer6) / 16.0f;
        if (!bru.isClientSide && apx.isOnFire() && integer6 > 0 && apx.getY() <= float7) {
            apx.clearFire();
            this.setWaterLevel(bru, fx, cee, integer6 - 1);
        }
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final ItemStack bly8 = bft.getItemInHand(aoq);
        if (bly8.isEmpty()) {
            return InteractionResult.PASS;
        }
        final int integer9 = cee.<Integer>getValue((Property<Integer>)CauldronBlock.LEVEL);
        final Item blu10 = bly8.getItem();
        if (blu10 == Items.WATER_BUCKET) {
            if (integer9 < 3 && !bru.isClientSide) {
                if (!bft.abilities.instabuild) {
                    bft.setItemInHand(aoq, new ItemStack(Items.BUCKET));
                }
                bft.awardStat(Stats.FILL_CAULDRON);
                this.setWaterLevel(bru, fx, cee, 3);
                bru.playSound(null, fx, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (blu10 == Items.BUCKET) {
            if (integer9 == 3 && !bru.isClientSide) {
                if (!bft.abilities.instabuild) {
                    bly8.shrink(1);
                    if (bly8.isEmpty()) {
                        bft.setItemInHand(aoq, new ItemStack(Items.WATER_BUCKET));
                    }
                    else if (!bft.inventory.add(new ItemStack(Items.WATER_BUCKET))) {
                        bft.drop(new ItemStack(Items.WATER_BUCKET), false);
                    }
                }
                bft.awardStat(Stats.USE_CAULDRON);
                this.setWaterLevel(bru, fx, cee, 0);
                bru.playSound(null, fx, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (blu10 == Items.GLASS_BOTTLE) {
            if (integer9 > 0 && !bru.isClientSide) {
                if (!bft.abilities.instabuild) {
                    final ItemStack bly9 = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                    bft.awardStat(Stats.USE_CAULDRON);
                    bly8.shrink(1);
                    if (bly8.isEmpty()) {
                        bft.setItemInHand(aoq, bly9);
                    }
                    else if (!bft.inventory.add(bly9)) {
                        bft.drop(bly9, false);
                    }
                    else if (bft instanceof ServerPlayer) {
                        ((ServerPlayer)bft).refreshContainer(bft.inventoryMenu);
                    }
                }
                bru.playSound(null, fx, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                this.setWaterLevel(bru, fx, cee, integer9 - 1);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (blu10 == Items.POTION && PotionUtils.getPotion(bly8) == Potions.WATER) {
            if (integer9 < 3 && !bru.isClientSide) {
                if (!bft.abilities.instabuild) {
                    final ItemStack bly9 = new ItemStack(Items.GLASS_BOTTLE);
                    bft.awardStat(Stats.USE_CAULDRON);
                    bft.setItemInHand(aoq, bly9);
                    if (bft instanceof ServerPlayer) {
                        ((ServerPlayer)bft).refreshContainer(bft.inventoryMenu);
                    }
                }
                bru.playSound(null, fx, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                this.setWaterLevel(bru, fx, cee, integer9 + 1);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (integer9 > 0 && blu10 instanceof DyeableLeatherItem) {
            final DyeableLeatherItem bky11 = (DyeableLeatherItem)blu10;
            if (bky11.hasCustomColor(bly8) && !bru.isClientSide) {
                bky11.clearColor(bly8);
                this.setWaterLevel(bru, fx, cee, integer9 - 1);
                bft.awardStat(Stats.CLEAN_ARMOR);
                return InteractionResult.SUCCESS;
            }
        }
        if (integer9 > 0 && blu10 instanceof BannerItem) {
            if (BannerBlockEntity.getPatternCount(bly8) > 0 && !bru.isClientSide) {
                final ItemStack bly9 = bly8.copy();
                bly9.setCount(1);
                BannerBlockEntity.removeLastPattern(bly9);
                bft.awardStat(Stats.CLEAN_BANNER);
                if (!bft.abilities.instabuild) {
                    bly8.shrink(1);
                    this.setWaterLevel(bru, fx, cee, integer9 - 1);
                }
                if (bly8.isEmpty()) {
                    bft.setItemInHand(aoq, bly9);
                }
                else if (!bft.inventory.add(bly9)) {
                    bft.drop(bly9, false);
                }
                else if (bft instanceof ServerPlayer) {
                    ((ServerPlayer)bft).refreshContainer(bft.inventoryMenu);
                }
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (integer9 <= 0 || !(blu10 instanceof BlockItem)) {
            return InteractionResult.PASS;
        }
        final Block bul11 = ((BlockItem)blu10).getBlock();
        if (bul11 instanceof ShulkerBoxBlock && !bru.isClientSide()) {
            final ItemStack bly10 = new ItemStack(Blocks.SHULKER_BOX, 1);
            if (bly8.hasTag()) {
                bly10.setTag(bly8.getTag().copy());
            }
            bft.setItemInHand(aoq, bly10);
            this.setWaterLevel(bru, fx, cee, integer9 - 1);
            bft.awardStat(Stats.CLEAN_SHULKER_BOX);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
    
    public void setWaterLevel(final Level bru, final BlockPos fx, final BlockState cee, final int integer) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)CauldronBlock.LEVEL, Mth.clamp(integer, 0, 3)), 2);
        bru.updateNeighbourForOutputSignal(fx, this);
    }
    
    @Override
    public void handleRain(final Level bru, final BlockPos fx) {
        if (bru.random.nextInt(20) != 1) {
            return;
        }
        final float float4 = bru.getBiome(fx).getTemperature(fx);
        if (float4 < 0.15f) {
            return;
        }
        final BlockState cee5 = bru.getBlockState(fx);
        if (cee5.<Integer>getValue((Property<Integer>)CauldronBlock.LEVEL) < 3) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee5).<Comparable>cycle((Property<Comparable>)CauldronBlock.LEVEL), 2);
        }
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return cee.<Integer>getValue((Property<Integer>)CauldronBlock.LEVEL);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CauldronBlock.LEVEL);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        LEVEL = BlockStateProperties.LEVEL_CAULDRON;
        INSIDE = Block.box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
        SHAPE = Shapes.join(Shapes.block(), Shapes.or(Block.box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), Block.box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), CauldronBlock.INSIDE), BooleanOp.ONLY_FIRST);
    }
}
