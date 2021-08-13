package net.minecraft.world.level.chunk;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.List;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Blocks;
import com.google.common.collect.Sets;
import java.util.IdentityHashMap;
import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import java.util.Map;
import java.util.EnumSet;
import net.minecraft.core.Direction8;
import org.apache.logging.log4j.Logger;

public class UpgradeData {
    private static final Logger LOGGER;
    public static final UpgradeData EMPTY;
    private static final Direction8[] DIRECTIONS;
    private final EnumSet<Direction8> sides;
    private final int[][] index;
    private static final Map<Block, BlockFixer> MAP;
    private static final Set<BlockFixer> CHUNKY_FIXERS;
    
    private UpgradeData() {
        this.sides = (EnumSet<Direction8>)EnumSet.noneOf((Class)Direction8.class);
        this.index = new int[16][];
    }
    
    public UpgradeData(final CompoundTag md) {
        this();
        if (md.contains("Indices", 10)) {
            final CompoundTag md2 = md.getCompound("Indices");
            for (int integer4 = 0; integer4 < this.index.length; ++integer4) {
                final String string5 = String.valueOf(integer4);
                if (md2.contains(string5, 11)) {
                    this.index[integer4] = md2.getIntArray(string5);
                }
            }
        }
        final int integer5 = md.getInt("Sides");
        for (final Direction8 gd7 : Direction8.values()) {
            if ((integer5 & 1 << gd7.ordinal()) != 0x0) {
                this.sides.add(gd7);
            }
        }
    }
    
    public void upgrade(final LevelChunk cge) {
        this.upgradeInside(cge);
        for (final Direction8 gd6 : UpgradeData.DIRECTIONS) {
            upgradeSides(cge, gd6);
        }
        final Level bru3 = cge.getLevel();
        UpgradeData.CHUNKY_FIXERS.forEach(a -> a.processChunk(bru3));
    }
    
    private static void upgradeSides(final LevelChunk cge, final Direction8 gd) {
        final Level bru3 = cge.getLevel();
        if (!cge.getUpgradeData().sides.remove(gd)) {
            return;
        }
        final Set<Direction> set4 = gd.getDirections();
        final int integer5 = 0;
        final int integer6 = 15;
        final boolean boolean7 = set4.contains(Direction.EAST);
        final boolean boolean8 = set4.contains(Direction.WEST);
        final boolean boolean9 = set4.contains(Direction.SOUTH);
        final boolean boolean10 = set4.contains(Direction.NORTH);
        final boolean boolean11 = set4.size() == 1;
        final ChunkPos bra12 = cge.getPos();
        final int integer7 = bra12.getMinBlockX() + ((boolean11 && (boolean10 || boolean9)) ? 1 : (boolean8 ? 0 : 15));
        final int integer8 = bra12.getMinBlockX() + ((boolean11 && (boolean10 || boolean9)) ? 14 : (boolean8 ? 0 : 15));
        final int integer9 = bra12.getMinBlockZ() + ((boolean11 && (boolean7 || boolean8)) ? 1 : (boolean10 ? 0 : 15));
        final int integer10 = bra12.getMinBlockZ() + ((boolean11 && (boolean7 || boolean8)) ? 14 : (boolean10 ? 0 : 15));
        final Direction[] arr17 = Direction.values();
        final BlockPos.MutableBlockPos a18 = new BlockPos.MutableBlockPos();
        for (final BlockPos fx20 : BlockPos.betweenClosed(integer7, 0, integer9, integer8, bru3.getMaxBuildHeight() - 1, integer10)) {
            BlockState cee22;
            final BlockState cee21 = cee22 = bru3.getBlockState(fx20);
            for (final Direction gc26 : arr17) {
                a18.setWithOffset(fx20, gc26);
                cee22 = updateState(cee22, gc26, bru3, fx20, a18);
            }
            Block.updateOrDestroy(cee21, cee22, bru3, fx20, 18);
        }
    }
    
    private static BlockState updateState(final BlockState cee, final Direction gc, final LevelAccessor brv, final BlockPos fx4, final BlockPos fx5) {
        return ((BlockFixer)UpgradeData.MAP.getOrDefault(cee.getBlock(), BlockFixers.DEFAULT)).updateShape(cee, gc, brv.getBlockState(fx5), brv, fx4, fx5);
    }
    
    private void upgradeInside(final LevelChunk cge) {
        final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        final ChunkPos bra5 = cge.getPos();
        final LevelAccessor brv6 = cge.getLevel();
        for (int integer7 = 0; integer7 < 16; ++integer7) {
            final LevelChunkSection cgf8 = cge.getSections()[integer7];
            final int[] arr9 = this.index[integer7];
            this.index[integer7] = null;
            if (cgf8 != null && arr9 != null) {
                if (arr9.length > 0) {
                    final Direction[] arr10 = Direction.values();
                    final PalettedContainer<BlockState> cgl11 = cgf8.getStates();
                    for (final int integer8 : arr9) {
                        final int integer9 = integer8 & 0xF;
                        final int integer10 = integer8 >> 8 & 0xF;
                        final int integer11 = integer8 >> 4 & 0xF;
                        a3.set(bra5.getMinBlockX() + integer9, (integer7 << 4) + integer10, bra5.getMinBlockZ() + integer11);
                        BlockState cee20;
                        final BlockState cee19 = cee20 = cgl11.get(integer8);
                        for (final Direction gc24 : arr10) {
                            a4.setWithOffset(a3, gc24);
                            if (a3.getX() >> 4 == bra5.x) {
                                if (a3.getZ() >> 4 == bra5.z) {
                                    cee20 = updateState(cee20, gc24, brv6, a3, a4);
                                }
                            }
                        }
                        Block.updateOrDestroy(cee19, cee20, brv6, a3, 18);
                    }
                }
            }
        }
        for (int integer7 = 0; integer7 < this.index.length; ++integer7) {
            if (this.index[integer7] != null) {
                UpgradeData.LOGGER.warn("Discarding update data for section {} for chunk ({} {})", integer7, bra5.x, bra5.z);
            }
            this.index[integer7] = null;
        }
    }
    
    public boolean isEmpty() {
        for (final int[] arr5 : this.index) {
            if (arr5 != null) {
                return false;
            }
        }
        return this.sides.isEmpty();
    }
    
    public CompoundTag write() {
        final CompoundTag md2 = new CompoundTag();
        final CompoundTag md3 = new CompoundTag();
        for (int integer4 = 0; integer4 < this.index.length; ++integer4) {
            final String string5 = String.valueOf(integer4);
            if (this.index[integer4] != null && this.index[integer4].length != 0) {
                md3.putIntArray(string5, this.index[integer4]);
            }
        }
        if (!md3.isEmpty()) {
            md2.put("Indices", (Tag)md3);
        }
        int integer4 = 0;
        for (final Direction8 gd6 : this.sides) {
            integer4 |= 1 << gd6.ordinal();
        }
        md2.putByte("Sides", (byte)integer4);
        return md2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY = new UpgradeData();
        DIRECTIONS = Direction8.values();
        MAP = (Map)new IdentityHashMap();
        CHUNKY_FIXERS = (Set)Sets.newHashSet();
    }
    
    public interface BlockFixer {
        BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6);
        
        default void processChunk(final LevelAccessor brv) {
        }
    }
    
    enum BlockFixers implements BlockFixer {
        BLACKLIST(new Block[] { Blocks.OBSERVER, Blocks.NETHER_PORTAL, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL, Blocks.DRAGON_EGG, Blocks.GRAVEL, Blocks.SAND, Blocks.RED_SAND, Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN }) {
            public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
                return cee1;
            }
        }, 
        DEFAULT(new Block[0]) {
            public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
                return cee1.updateShape(gc, brv.getBlockState(fx6), brv, fx5, fx6);
            }
        }, 
        CHEST(new Block[] { Blocks.CHEST, Blocks.TRAPPED_CHEST }) {
            public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
                if (cee3.is(cee1.getBlock()) && gc.getAxis().isHorizontal() && cee1.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE && cee3.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
                    final Direction gc2 = cee1.<Direction>getValue((Property<Direction>)ChestBlock.FACING);
                    if (gc.getAxis() != gc2.getAxis() && gc2 == cee3.<Comparable>getValue((Property<Comparable>)ChestBlock.FACING)) {
                        final ChestType cew9 = (gc == gc2.getClockWise()) ? ChestType.LEFT : ChestType.RIGHT;
                        brv.setBlock(fx6, ((StateHolder<O, BlockState>)cee3).<ChestType, ChestType>setValue(ChestBlock.TYPE, cew9.getOpposite()), 18);
                        if (gc2 == Direction.NORTH || gc2 == Direction.EAST) {
                            final BlockEntity ccg10 = brv.getBlockEntity(fx5);
                            final BlockEntity ccg11 = brv.getBlockEntity(fx6);
                            if (ccg10 instanceof ChestBlockEntity && ccg11 instanceof ChestBlockEntity) {
                                ChestBlockEntity.swapContents((ChestBlockEntity)ccg10, (ChestBlockEntity)ccg11);
                            }
                        }
                        return ((StateHolder<O, BlockState>)cee1).<ChestType, ChestType>setValue(ChestBlock.TYPE, cew9);
                    }
                }
                return cee1;
            }
        }, 
        LEAVES(true, new Block[] { Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES }) {
            private final ThreadLocal<List<ObjectSet<BlockPos>>> queue;
            
            {
                this.queue = (ThreadLocal<List<ObjectSet<BlockPos>>>)ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(7));
            }
            
            public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
                final BlockState cee4 = cee1.updateShape(gc, brv.getBlockState(fx6), brv, fx5, fx6);
                if (cee1 != cee4) {
                    final int integer9 = cee4.<Integer>getValue((Property<Integer>)BlockStateProperties.DISTANCE);
                    final List<ObjectSet<BlockPos>> list10 = (List<ObjectSet<BlockPos>>)this.queue.get();
                    if (list10.isEmpty()) {
                        for (int integer10 = 0; integer10 < 7; ++integer10) {
                            list10.add(new ObjectOpenHashSet());
                        }
                    }
                    ((ObjectSet)list10.get(integer9)).add(fx5.immutable());
                }
                return cee1;
            }
            
            public void processChunk(final LevelAccessor brv) {
                final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
                final List<ObjectSet<BlockPos>> list4 = (List<ObjectSet<BlockPos>>)this.queue.get();
                for (int integer5 = 2; integer5 < list4.size(); ++integer5) {
                    final int integer6 = integer5 - 1;
                    final ObjectSet<BlockPos> objectSet7 = (ObjectSet<BlockPos>)list4.get(integer6);
                    final ObjectSet<BlockPos> objectSet8 = (ObjectSet<BlockPos>)list4.get(integer5);
                    for (final BlockPos fx10 : objectSet7) {
                        final BlockState cee11 = brv.getBlockState(fx10);
                        if (cee11.<Integer>getValue((Property<Integer>)BlockStateProperties.DISTANCE) < integer6) {
                            continue;
                        }
                        brv.setBlock(fx10, ((StateHolder<O, BlockState>)cee11).<Comparable, Integer>setValue((Property<Comparable>)BlockStateProperties.DISTANCE, integer6), 18);
                        if (integer5 == 7) {
                            continue;
                        }
                        for (final Direction gc15 : UpgradeData$BlockFixers$4.DIRECTIONS) {
                            a3.setWithOffset(fx10, gc15);
                            final BlockState cee12 = brv.getBlockState(a3);
                            if (cee12.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.DISTANCE) && cee11.<Integer>getValue((Property<Integer>)BlockStateProperties.DISTANCE) > integer5) {
                                objectSet8.add(a3.immutable());
                            }
                        }
                    }
                }
                list4.clear();
            }
        }, 
        STEM_BLOCK(new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM }) {
            public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
                if (cee1.<Integer>getValue((Property<Integer>)StemBlock.AGE) == 7) {
                    final StemGrownBlock cak8 = ((StemBlock)cee1.getBlock()).getFruit();
                    if (cee3.is(cak8)) {
                        return ((StateHolder<O, BlockState>)cak8.getAttachedStem().defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)HorizontalDirectionalBlock.FACING, gc);
                    }
                }
                return cee1;
            }
        };
        
        public static final Direction[] DIRECTIONS;
        
        private BlockFixers(final Block[] arr) {
            this(false, arr);
        }
        
        private BlockFixers(final boolean boolean3, final Block[] arr) {
            for (final Block bul9 : arr) {
                UpgradeData.MAP.put(bul9, this);
            }
            if (boolean3) {
                UpgradeData.CHUNKY_FIXERS.add(this);
            }
        }
        
        static {
            DIRECTIONS = Direction.values();
        }
    }
}
