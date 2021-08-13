package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import javax.annotation.Nullable;
import net.minecraft.world.SimpleContainer;
import java.util.function.Consumer;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.Random;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.ItemLike;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.WorldlyContainerHolder;

public class ComposterBlock extends Block implements WorldlyContainerHolder {
    public static final IntegerProperty LEVEL;
    public static final Object2FloatMap<ItemLike> COMPOSTABLES;
    private static final VoxelShape OUTER_SHAPE;
    private static final VoxelShape[] SHAPES;
    
    public static void bootStrap() {
        ComposterBlock.COMPOSTABLES.defaultReturnValue(-1.0f);
        final float float1 = 0.3f;
        final float float2 = 0.5f;
        final float float3 = 0.65f;
        final float float4 = 0.85f;
        final float float5 = 1.0f;
        add(0.3f, Items.JUNGLE_LEAVES);
        add(0.3f, Items.OAK_LEAVES);
        add(0.3f, Items.SPRUCE_LEAVES);
        add(0.3f, Items.DARK_OAK_LEAVES);
        add(0.3f, Items.ACACIA_LEAVES);
        add(0.3f, Items.BIRCH_LEAVES);
        add(0.3f, Items.OAK_SAPLING);
        add(0.3f, Items.SPRUCE_SAPLING);
        add(0.3f, Items.BIRCH_SAPLING);
        add(0.3f, Items.JUNGLE_SAPLING);
        add(0.3f, Items.ACACIA_SAPLING);
        add(0.3f, Items.DARK_OAK_SAPLING);
        add(0.3f, Items.BEETROOT_SEEDS);
        add(0.3f, Items.DRIED_KELP);
        add(0.3f, Items.GRASS);
        add(0.3f, Items.KELP);
        add(0.3f, Items.MELON_SEEDS);
        add(0.3f, Items.PUMPKIN_SEEDS);
        add(0.3f, Items.SEAGRASS);
        add(0.3f, Items.SWEET_BERRIES);
        add(0.3f, Items.WHEAT_SEEDS);
        add(0.5f, Items.DRIED_KELP_BLOCK);
        add(0.5f, Items.TALL_GRASS);
        add(0.5f, Items.CACTUS);
        add(0.5f, Items.SUGAR_CANE);
        add(0.5f, Items.VINE);
        add(0.5f, Items.NETHER_SPROUTS);
        add(0.5f, Items.WEEPING_VINES);
        add(0.5f, Items.TWISTING_VINES);
        add(0.5f, Items.MELON_SLICE);
        add(0.65f, Items.SEA_PICKLE);
        add(0.65f, Items.LILY_PAD);
        add(0.65f, Items.PUMPKIN);
        add(0.65f, Items.CARVED_PUMPKIN);
        add(0.65f, Items.MELON);
        add(0.65f, Items.APPLE);
        add(0.65f, Items.BEETROOT);
        add(0.65f, Items.CARROT);
        add(0.65f, Items.COCOA_BEANS);
        add(0.65f, Items.POTATO);
        add(0.65f, Items.WHEAT);
        add(0.65f, Items.BROWN_MUSHROOM);
        add(0.65f, Items.RED_MUSHROOM);
        add(0.65f, Items.MUSHROOM_STEM);
        add(0.65f, Items.CRIMSON_FUNGUS);
        add(0.65f, Items.WARPED_FUNGUS);
        add(0.65f, Items.NETHER_WART);
        add(0.65f, Items.CRIMSON_ROOTS);
        add(0.65f, Items.WARPED_ROOTS);
        add(0.65f, Items.SHROOMLIGHT);
        add(0.65f, Items.DANDELION);
        add(0.65f, Items.POPPY);
        add(0.65f, Items.BLUE_ORCHID);
        add(0.65f, Items.ALLIUM);
        add(0.65f, Items.AZURE_BLUET);
        add(0.65f, Items.RED_TULIP);
        add(0.65f, Items.ORANGE_TULIP);
        add(0.65f, Items.WHITE_TULIP);
        add(0.65f, Items.PINK_TULIP);
        add(0.65f, Items.OXEYE_DAISY);
        add(0.65f, Items.CORNFLOWER);
        add(0.65f, Items.LILY_OF_THE_VALLEY);
        add(0.65f, Items.WITHER_ROSE);
        add(0.65f, Items.FERN);
        add(0.65f, Items.SUNFLOWER);
        add(0.65f, Items.LILAC);
        add(0.65f, Items.ROSE_BUSH);
        add(0.65f, Items.PEONY);
        add(0.65f, Items.LARGE_FERN);
        add(0.85f, Items.HAY_BLOCK);
        add(0.85f, Items.BROWN_MUSHROOM_BLOCK);
        add(0.85f, Items.RED_MUSHROOM_BLOCK);
        add(0.85f, Items.NETHER_WART_BLOCK);
        add(0.85f, Items.WARPED_WART_BLOCK);
        add(0.85f, Items.BREAD);
        add(0.85f, Items.BAKED_POTATO);
        add(0.85f, Items.COOKIE);
        add(1.0f, Items.CAKE);
        add(1.0f, Items.PUMPKIN_PIE);
    }
    
    private static void add(final float float1, final ItemLike brt) {
        ComposterBlock.COMPOSTABLES.put(brt.asItem(), float1);
    }
    
    public ComposterBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)ComposterBlock.LEVEL, 0));
    }
    
    public static void handleFill(final Level bru, final BlockPos fx, final boolean boolean3) {
        final BlockState cee4 = bru.getBlockState(fx);
        bru.playLocalSound(fx.getX(), fx.getY(), fx.getZ(), boolean3 ? SoundEvents.COMPOSTER_FILL_SUCCESS : SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        final double double5 = cee4.getShape(bru, fx).max(Direction.Axis.Y, 0.5, 0.5) + 0.03125;
        final double double6 = 0.13124999403953552;
        final double double7 = 0.737500011920929;
        final Random random11 = bru.getRandom();
        for (int integer12 = 0; integer12 < 10; ++integer12) {
            final double double8 = random11.nextGaussian() * 0.02;
            final double double9 = random11.nextGaussian() * 0.02;
            final double double10 = random11.nextGaussian() * 0.02;
            bru.addParticle(ParticleTypes.COMPOSTER, fx.getX() + 0.13124999403953552 + 0.737500011920929 * random11.nextFloat(), fx.getY() + double5 + random11.nextFloat() * (1.0 - double5), fx.getZ() + 0.13124999403953552 + 0.737500011920929 * random11.nextFloat(), double8, double9, double10);
        }
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return ComposterBlock.SHAPES[cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL)];
    }
    
    @Override
    public VoxelShape getInteractionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return ComposterBlock.OUTER_SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return ComposterBlock.SHAPES[0];
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL) == 7) {
            bru.getBlockTicks().scheduleTick(fx, cee1.getBlock(), 20);
        }
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final int integer8 = cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL);
        final ItemStack bly9 = bft.getItemInHand(aoq);
        if (integer8 < 8 && ComposterBlock.COMPOSTABLES.containsKey(bly9.getItem())) {
            if (integer8 < 7 && !bru.isClientSide) {
                final BlockState cee2 = addItem(cee, bru, fx, bly9);
                bru.levelEvent(1500, fx, (cee != cee2) ? 1 : 0);
                if (!bft.abilities.instabuild) {
                    bly9.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (integer8 == 8) {
            extractProduce(cee, bru, fx);
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    public static BlockState insertItem(final BlockState cee, final ServerLevel aag, final ItemStack bly, final BlockPos fx) {
        final int integer5 = cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL);
        if (integer5 < 7 && ComposterBlock.COMPOSTABLES.containsKey(bly.getItem())) {
            final BlockState cee2 = addItem(cee, aag, fx, bly);
            bly.shrink(1);
            return cee2;
        }
        return cee;
    }
    
    public static BlockState extractProduce(final BlockState cee, final Level bru, final BlockPos fx) {
        if (!bru.isClientSide) {
            final float float4 = 0.7f;
            final double double5 = bru.random.nextFloat() * 0.7f + 0.15000000596046448;
            final double double6 = bru.random.nextFloat() * 0.7f + 0.06000000238418579 + 0.6;
            final double double7 = bru.random.nextFloat() * 0.7f + 0.15000000596046448;
            final ItemEntity bcs11 = new ItemEntity(bru, fx.getX() + double5, fx.getY() + double6, fx.getZ() + double7, new ItemStack(Items.BONE_MEAL));
            bcs11.setDefaultPickUpDelay();
            bru.addFreshEntity(bcs11);
        }
        final BlockState cee2 = empty(cee, bru, fx);
        bru.playSound(null, fx, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        return cee2;
    }
    
    private static BlockState empty(final BlockState cee, final LevelAccessor brv, final BlockPos fx) {
        final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)ComposterBlock.LEVEL, 0);
        brv.setBlock(fx, cee2, 3);
        return cee2;
    }
    
    private static BlockState addItem(final BlockState cee, final LevelAccessor brv, final BlockPos fx, final ItemStack bly) {
        final int integer5 = cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL);
        final float float6 = ComposterBlock.COMPOSTABLES.getFloat(bly.getItem());
        if ((integer5 == 0 && float6 > 0.0f) || brv.getRandom().nextDouble() < float6) {
            final int integer6 = integer5 + 1;
            final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)ComposterBlock.LEVEL, integer6);
            brv.setBlock(fx, cee2, 3);
            if (integer6 == 7) {
                brv.getBlockTicks().scheduleTick(fx, cee.getBlock(), 20);
            }
            return cee2;
        }
        return cee;
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL) == 7) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)ComposterBlock.LEVEL), 3);
            aag.playSound(null, fx, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ComposterBlock.LEVEL);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    @Override
    public WorldlyContainer getContainer(final BlockState cee, final LevelAccessor brv, final BlockPos fx) {
        final int integer5 = cee.<Integer>getValue((Property<Integer>)ComposterBlock.LEVEL);
        if (integer5 == 8) {
            return new OutputContainer(cee, brv, fx, new ItemStack(Items.BONE_MEAL));
        }
        if (integer5 < 7) {
            return new InputContainer(cee, brv, fx);
        }
        return new EmptyContainer();
    }
    
    static {
        LEVEL = BlockStateProperties.LEVEL_COMPOSTER;
        COMPOSTABLES = (Object2FloatMap)new Object2FloatOpenHashMap();
        OUTER_SHAPE = Shapes.block();
        SHAPES = Util.<VoxelShape[]>make(new VoxelShape[9], (java.util.function.Consumer<VoxelShape[]>)(arr -> {
            for (int integer2 = 0; integer2 < 8; ++integer2) {
                arr[integer2] = Shapes.join(ComposterBlock.OUTER_SHAPE, Block.box(2.0, Math.max(2, 1 + integer2 * 2), 2.0, 14.0, 16.0, 14.0), BooleanOp.ONLY_FIRST);
            }
            arr[8] = arr[7];
        }));
    }
    
    static class EmptyContainer extends SimpleContainer implements WorldlyContainer {
        public EmptyContainer() {
            super(0);
        }
        
        @Override
        public int[] getSlotsForFace(final Direction gc) {
            return new int[0];
        }
        
        @Override
        public boolean canPlaceItemThroughFace(final int integer, final ItemStack bly, @Nullable final Direction gc) {
            return false;
        }
        
        @Override
        public boolean canTakeItemThroughFace(final int integer, final ItemStack bly, final Direction gc) {
            return false;
        }
    }
    
    static class OutputContainer extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;
        private boolean changed;
        
        public OutputContainer(final BlockState cee, final LevelAccessor brv, final BlockPos fx, final ItemStack bly) {
            super(bly);
            this.state = cee;
            this.level = brv;
            this.pos = fx;
        }
        
        public int getMaxStackSize() {
            return 1;
        }
        
        @Override
        public int[] getSlotsForFace(final Direction gc) {
            return (gc == Direction.DOWN) ? new int[] { 0 } : new int[0];
        }
        
        @Override
        public boolean canPlaceItemThroughFace(final int integer, final ItemStack bly, @Nullable final Direction gc) {
            return false;
        }
        
        @Override
        public boolean canTakeItemThroughFace(final int integer, final ItemStack bly, final Direction gc) {
            return !this.changed && gc == Direction.DOWN && bly.getItem() == Items.BONE_MEAL;
        }
        
        @Override
        public void setChanged() {
            empty(this.state, this.level, this.pos);
            this.changed = true;
        }
    }
    
    static class InputContainer extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor level;
        private final BlockPos pos;
        private boolean changed;
        
        public InputContainer(final BlockState cee, final LevelAccessor brv, final BlockPos fx) {
            super(1);
            this.state = cee;
            this.level = brv;
            this.pos = fx;
        }
        
        public int getMaxStackSize() {
            return 1;
        }
        
        @Override
        public int[] getSlotsForFace(final Direction gc) {
            return (gc == Direction.UP) ? new int[] { 0 } : new int[0];
        }
        
        @Override
        public boolean canPlaceItemThroughFace(final int integer, final ItemStack bly, @Nullable final Direction gc) {
            return !this.changed && gc == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(bly.getItem());
        }
        
        @Override
        public boolean canTakeItemThroughFace(final int integer, final ItemStack bly, final Direction gc) {
            return false;
        }
        
        @Override
        public void setChanged() {
            final ItemStack bly2 = this.getItem(0);
            if (!bly2.isEmpty()) {
                this.changed = true;
                final BlockState cee3 = addItem(this.state, this.level, this.pos, bly2);
                this.level.levelEvent(1500, this.pos, (cee3 != this.state) ? 1 : 0);
                this.removeItemNoUpdate(0);
            }
        }
    }
}
