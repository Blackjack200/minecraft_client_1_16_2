package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.stream.Collector;
import net.minecraft.Util;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.shapes.Shapes;
import com.google.common.collect.ImmutableMap;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.world.level.block.state.properties.Property;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.BlockBehaviour;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FireBlock extends BaseFireBlock {
    public static final IntegerProperty AGE;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    private static final VoxelShape UP_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private final Map<BlockState, VoxelShape> shapesCache;
    private final Object2IntMap<Block> flameOdds;
    private final Object2IntMap<Block> burnOdds;
    
    public FireBlock(final Properties c) {
        super(c, 1.0f);
        this.flameOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
        this.burnOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
        this.registerDefaultState((((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)FireBlock.AGE, 0)).setValue((Property<Comparable>)FireBlock.NORTH, false)).setValue((Property<Comparable>)FireBlock.EAST, false)).setValue((Property<Comparable>)FireBlock.SOUTH, false)).setValue((Property<Comparable>)FireBlock.WEST, false)).<Comparable, Boolean>setValue((Property<Comparable>)FireBlock.UP, false));
        this.shapesCache = (Map<BlockState, VoxelShape>)ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().filter(cee -> cee.<Integer>getValue((Property<Integer>)FireBlock.AGE) == 0).collect(Collectors.toMap(Function.identity(), FireBlock::calculateShape)));
    }
    
    private static VoxelShape calculateShape(final BlockState cee) {
        VoxelShape dde2 = Shapes.empty();
        if (cee.<Boolean>getValue((Property<Boolean>)FireBlock.UP)) {
            dde2 = FireBlock.UP_AABB;
        }
        if (cee.<Boolean>getValue((Property<Boolean>)FireBlock.NORTH)) {
            dde2 = Shapes.or(dde2, FireBlock.NORTH_AABB);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)FireBlock.SOUTH)) {
            dde2 = Shapes.or(dde2, FireBlock.SOUTH_AABB);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)FireBlock.EAST)) {
            dde2 = Shapes.or(dde2, FireBlock.EAST_AABB);
        }
        if (cee.<Boolean>getValue((Property<Boolean>)FireBlock.WEST)) {
            dde2 = Shapes.or(dde2, FireBlock.WEST_AABB);
        }
        return dde2.isEmpty() ? FireBlock.DOWN_AABB : dde2;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (this.canSurvive(cee1, brv, fx5)) {
            return this.getStateWithAge(brv, fx5, cee1.<Integer>getValue((Property<Integer>)FireBlock.AGE));
        }
        return Blocks.AIR.defaultBlockState();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)this.shapesCache.get(((StateHolder<O, Object>)cee).<Comparable, Integer>setValue((Property<Comparable>)FireBlock.AGE, 0));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return this.getStateForPlacement(bnv.getLevel(), bnv.getClickedPos());
    }
    
    protected BlockState getStateForPlacement(final BlockGetter bqz, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        final BlockState cee5 = bqz.getBlockState(fx2);
        if (this.canBurn(cee5) || cee5.isFaceSturdy(bqz, fx2, Direction.UP)) {
            return this.defaultBlockState();
        }
        BlockState cee6 = this.defaultBlockState();
        for (final Direction gc10 : Direction.values()) {
            final BooleanProperty cev11 = (BooleanProperty)FireBlock.PROPERTY_BY_DIRECTION.get(gc10);
            if (cev11 != null) {
                cee6 = ((StateHolder<O, BlockState>)cee6).<Comparable, Boolean>setValue((Property<Comparable>)cev11, this.canBurn(bqz.getBlockState(fx.relative(gc10))));
            }
        }
        return cee6;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        return brw.getBlockState(fx2).isFaceSturdy(brw, fx2, Direction.UP) || this.isValidFireLocation(brw, fx);
    }
    
    @Override
    public void tick(BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        aag.getBlockTicks().scheduleTick(fx, this, getFireTickDelay(aag.random));
        if (!aag.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }
        if (!cee.canSurvive(aag, fx)) {
            aag.removeBlock(fx, false);
        }
        final BlockState cee2 = aag.getBlockState(fx.below());
        final boolean boolean7 = cee2.is(aag.dimensionType().infiniburn());
        final int integer8 = cee.<Integer>getValue((Property<Integer>)FireBlock.AGE);
        if (!boolean7 && aag.isRaining() && this.isNearRain(aag, fx) && random.nextFloat() < 0.2f + integer8 * 0.03f) {
            aag.removeBlock(fx, false);
            return;
        }
        final int integer9 = Math.min(15, integer8 + random.nextInt(3) / 2);
        if (integer8 != integer9) {
            cee = ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)FireBlock.AGE, integer9);
            aag.setBlock(fx, cee, 4);
        }
        if (!boolean7) {
            if (!this.isValidFireLocation(aag, fx)) {
                final BlockPos fx2 = fx.below();
                if (!aag.getBlockState(fx2).isFaceSturdy(aag, fx2, Direction.UP) || integer8 > 3) {
                    aag.removeBlock(fx, false);
                }
                return;
            }
            if (integer8 == 15 && random.nextInt(4) == 0 && !this.canBurn(aag.getBlockState(fx.below()))) {
                aag.removeBlock(fx, false);
                return;
            }
        }
        final boolean boolean8 = aag.isHumidAt(fx);
        final int integer10 = boolean8 ? -50 : 0;
        this.checkBurnOut(aag, fx.east(), 300 + integer10, random, integer8);
        this.checkBurnOut(aag, fx.west(), 300 + integer10, random, integer8);
        this.checkBurnOut(aag, fx.below(), 250 + integer10, random, integer8);
        this.checkBurnOut(aag, fx.above(), 250 + integer10, random, integer8);
        this.checkBurnOut(aag, fx.north(), 300 + integer10, random, integer8);
        this.checkBurnOut(aag, fx.south(), 300 + integer10, random, integer8);
        final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
        for (int integer11 = -1; integer11 <= 1; ++integer11) {
            for (int integer12 = -1; integer12 <= 1; ++integer12) {
                for (int integer13 = -1; integer13 <= 4; ++integer13) {
                    if (integer11 != 0 || integer13 != 0 || integer12 != 0) {
                        int integer14 = 100;
                        if (integer13 > 1) {
                            integer14 += (integer13 - 1) * 100;
                        }
                        a12.setWithOffset(fx, integer11, integer13, integer12);
                        final int integer15 = this.getFireOdds(aag, a12);
                        if (integer15 > 0) {
                            int integer16 = (integer15 + 40 + aag.getDifficulty().getId() * 7) / (integer8 + 30);
                            if (boolean8) {
                                integer16 /= 2;
                            }
                            if (integer16 > 0 && random.nextInt(integer14) <= integer16) {
                                if (!aag.isRaining() || !this.isNearRain(aag, a12)) {
                                    final int integer17 = Math.min(15, integer8 + random.nextInt(5) / 4);
                                    aag.setBlock(a12, this.getStateWithAge(aag, a12, integer17), 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected boolean isNearRain(final Level bru, final BlockPos fx) {
        return bru.isRainingAt(fx) || bru.isRainingAt(fx.west()) || bru.isRainingAt(fx.east()) || bru.isRainingAt(fx.north()) || bru.isRainingAt(fx.south());
    }
    
    private int getBurnOdd(final BlockState cee) {
        if (cee.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.WATERLOGGED) && cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED)) {
            return 0;
        }
        return this.burnOdds.getInt(cee.getBlock());
    }
    
    private int getFlameOdds(final BlockState cee) {
        if (cee.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.WATERLOGGED) && cee.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED)) {
            return 0;
        }
        return this.flameOdds.getInt(cee.getBlock());
    }
    
    private void checkBurnOut(final Level bru, final BlockPos fx, final int integer3, final Random random, final int integer5) {
        final int integer6 = this.getBurnOdd(bru.getBlockState(fx));
        if (random.nextInt(integer3) < integer6) {
            final BlockState cee8 = bru.getBlockState(fx);
            if (random.nextInt(integer5 + 10) < 5 && !bru.isRainingAt(fx)) {
                final int integer7 = Math.min(integer5 + random.nextInt(5) / 4, 15);
                bru.setBlock(fx, this.getStateWithAge(bru, fx, integer7), 3);
            }
            else {
                bru.removeBlock(fx, false);
            }
            final Block bul9 = cee8.getBlock();
            if (bul9 instanceof TntBlock) {
                final TntBlock tntBlock = (TntBlock)bul9;
                TntBlock.explode(bru, fx);
            }
        }
    }
    
    private BlockState getStateWithAge(final LevelAccessor brv, final BlockPos fx, final int integer) {
        final BlockState cee5 = BaseFireBlock.getState(brv, fx);
        if (cee5.is(Blocks.FIRE)) {
            return ((StateHolder<O, BlockState>)cee5).<Comparable, Integer>setValue((Property<Comparable>)FireBlock.AGE, integer);
        }
        return cee5;
    }
    
    private boolean isValidFireLocation(final BlockGetter bqz, final BlockPos fx) {
        for (final Direction gc7 : Direction.values()) {
            if (this.canBurn(bqz.getBlockState(fx.relative(gc7)))) {
                return true;
            }
        }
        return false;
    }
    
    private int getFireOdds(final LevelReader brw, final BlockPos fx) {
        if (!brw.isEmptyBlock(fx)) {
            return 0;
        }
        int integer4 = 0;
        for (final Direction gc8 : Direction.values()) {
            final BlockState cee9 = brw.getBlockState(fx.relative(gc8));
            integer4 = Math.max(this.getFlameOdds(cee9), integer4);
        }
        return integer4;
    }
    
    @Override
    protected boolean canBurn(final BlockState cee) {
        return this.getFlameOdds(cee) > 0;
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        super.onPlace(cee1, bru, fx, cee4, boolean5);
        bru.getBlockTicks().scheduleTick(fx, this, getFireTickDelay(bru.random));
    }
    
    private static int getFireTickDelay(final Random random) {
        return 30 + random.nextInt(10);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(FireBlock.AGE, FireBlock.NORTH, FireBlock.EAST, FireBlock.SOUTH, FireBlock.WEST, FireBlock.UP);
    }
    
    private void setFlammable(final Block bul, final int integer2, final int integer3) {
        this.flameOdds.put(bul, integer2);
        this.burnOdds.put(bul, integer3);
    }
    
    public static void bootStrap() {
        final FireBlock bwp1 = (FireBlock)Blocks.FIRE;
        bwp1.setFlammable(Blocks.OAK_PLANKS, 5, 20);
        bwp1.setFlammable(Blocks.SPRUCE_PLANKS, 5, 20);
        bwp1.setFlammable(Blocks.BIRCH_PLANKS, 5, 20);
        bwp1.setFlammable(Blocks.JUNGLE_PLANKS, 5, 20);
        bwp1.setFlammable(Blocks.ACACIA_PLANKS, 5, 20);
        bwp1.setFlammable(Blocks.DARK_OAK_PLANKS, 5, 20);
        bwp1.setFlammable(Blocks.OAK_SLAB, 5, 20);
        bwp1.setFlammable(Blocks.SPRUCE_SLAB, 5, 20);
        bwp1.setFlammable(Blocks.BIRCH_SLAB, 5, 20);
        bwp1.setFlammable(Blocks.JUNGLE_SLAB, 5, 20);
        bwp1.setFlammable(Blocks.ACACIA_SLAB, 5, 20);
        bwp1.setFlammable(Blocks.DARK_OAK_SLAB, 5, 20);
        bwp1.setFlammable(Blocks.OAK_FENCE_GATE, 5, 20);
        bwp1.setFlammable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        bwp1.setFlammable(Blocks.BIRCH_FENCE_GATE, 5, 20);
        bwp1.setFlammable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        bwp1.setFlammable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        bwp1.setFlammable(Blocks.ACACIA_FENCE_GATE, 5, 20);
        bwp1.setFlammable(Blocks.OAK_FENCE, 5, 20);
        bwp1.setFlammable(Blocks.SPRUCE_FENCE, 5, 20);
        bwp1.setFlammable(Blocks.BIRCH_FENCE, 5, 20);
        bwp1.setFlammable(Blocks.JUNGLE_FENCE, 5, 20);
        bwp1.setFlammable(Blocks.DARK_OAK_FENCE, 5, 20);
        bwp1.setFlammable(Blocks.ACACIA_FENCE, 5, 20);
        bwp1.setFlammable(Blocks.OAK_STAIRS, 5, 20);
        bwp1.setFlammable(Blocks.BIRCH_STAIRS, 5, 20);
        bwp1.setFlammable(Blocks.SPRUCE_STAIRS, 5, 20);
        bwp1.setFlammable(Blocks.JUNGLE_STAIRS, 5, 20);
        bwp1.setFlammable(Blocks.ACACIA_STAIRS, 5, 20);
        bwp1.setFlammable(Blocks.DARK_OAK_STAIRS, 5, 20);
        bwp1.setFlammable(Blocks.OAK_LOG, 5, 5);
        bwp1.setFlammable(Blocks.SPRUCE_LOG, 5, 5);
        bwp1.setFlammable(Blocks.BIRCH_LOG, 5, 5);
        bwp1.setFlammable(Blocks.JUNGLE_LOG, 5, 5);
        bwp1.setFlammable(Blocks.ACACIA_LOG, 5, 5);
        bwp1.setFlammable(Blocks.DARK_OAK_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_OAK_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.OAK_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.SPRUCE_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.BIRCH_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.JUNGLE_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.ACACIA_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.DARK_OAK_WOOD, 5, 5);
        bwp1.setFlammable(Blocks.OAK_LEAVES, 30, 60);
        bwp1.setFlammable(Blocks.SPRUCE_LEAVES, 30, 60);
        bwp1.setFlammable(Blocks.BIRCH_LEAVES, 30, 60);
        bwp1.setFlammable(Blocks.JUNGLE_LEAVES, 30, 60);
        bwp1.setFlammable(Blocks.ACACIA_LEAVES, 30, 60);
        bwp1.setFlammable(Blocks.DARK_OAK_LEAVES, 30, 60);
        bwp1.setFlammable(Blocks.BOOKSHELF, 30, 20);
        bwp1.setFlammable(Blocks.TNT, 15, 100);
        bwp1.setFlammable(Blocks.GRASS, 60, 100);
        bwp1.setFlammable(Blocks.FERN, 60, 100);
        bwp1.setFlammable(Blocks.DEAD_BUSH, 60, 100);
        bwp1.setFlammable(Blocks.SUNFLOWER, 60, 100);
        bwp1.setFlammable(Blocks.LILAC, 60, 100);
        bwp1.setFlammable(Blocks.ROSE_BUSH, 60, 100);
        bwp1.setFlammable(Blocks.PEONY, 60, 100);
        bwp1.setFlammable(Blocks.TALL_GRASS, 60, 100);
        bwp1.setFlammable(Blocks.LARGE_FERN, 60, 100);
        bwp1.setFlammable(Blocks.DANDELION, 60, 100);
        bwp1.setFlammable(Blocks.POPPY, 60, 100);
        bwp1.setFlammable(Blocks.BLUE_ORCHID, 60, 100);
        bwp1.setFlammable(Blocks.ALLIUM, 60, 100);
        bwp1.setFlammable(Blocks.AZURE_BLUET, 60, 100);
        bwp1.setFlammable(Blocks.RED_TULIP, 60, 100);
        bwp1.setFlammable(Blocks.ORANGE_TULIP, 60, 100);
        bwp1.setFlammable(Blocks.WHITE_TULIP, 60, 100);
        bwp1.setFlammable(Blocks.PINK_TULIP, 60, 100);
        bwp1.setFlammable(Blocks.OXEYE_DAISY, 60, 100);
        bwp1.setFlammable(Blocks.CORNFLOWER, 60, 100);
        bwp1.setFlammable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
        bwp1.setFlammable(Blocks.WITHER_ROSE, 60, 100);
        bwp1.setFlammable(Blocks.WHITE_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.ORANGE_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.MAGENTA_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.LIGHT_BLUE_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.YELLOW_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.LIME_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.PINK_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.GRAY_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.LIGHT_GRAY_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.CYAN_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.PURPLE_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.BLUE_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.BROWN_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.GREEN_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.RED_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.BLACK_WOOL, 30, 60);
        bwp1.setFlammable(Blocks.VINE, 15, 100);
        bwp1.setFlammable(Blocks.COAL_BLOCK, 5, 5);
        bwp1.setFlammable(Blocks.HAY_BLOCK, 60, 20);
        bwp1.setFlammable(Blocks.TARGET, 15, 20);
        bwp1.setFlammable(Blocks.WHITE_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.ORANGE_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.MAGENTA_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.LIGHT_BLUE_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.YELLOW_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.LIME_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.PINK_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.GRAY_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.LIGHT_GRAY_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.CYAN_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.PURPLE_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.BLUE_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.BROWN_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.GREEN_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.RED_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.BLACK_CARPET, 60, 20);
        bwp1.setFlammable(Blocks.DRIED_KELP_BLOCK, 30, 60);
        bwp1.setFlammable(Blocks.BAMBOO, 60, 60);
        bwp1.setFlammable(Blocks.SCAFFOLDING, 60, 60);
        bwp1.setFlammable(Blocks.LECTERN, 30, 20);
        bwp1.setFlammable(Blocks.COMPOSTER, 5, 20);
        bwp1.setFlammable(Blocks.SWEET_BERRY_BUSH, 60, 100);
        bwp1.setFlammable(Blocks.BEEHIVE, 5, 20);
        bwp1.setFlammable(Blocks.BEE_NEST, 30, 20);
    }
    
    static {
        AGE = BlockStateProperties.AGE_15;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        UP = PipeBlock.UP;
        PROPERTY_BY_DIRECTION = (Map)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(entry -> entry.getKey() != Direction.DOWN).collect((Collector)Util.toMap());
        UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
        EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    }
}
