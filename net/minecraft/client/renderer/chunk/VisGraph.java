package net.minecraft.client.renderer.chunk;

import java.util.function.Consumer;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.ints.IntPriorityQueue;
import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.BitSet;
import net.minecraft.core.Direction;

public class VisGraph {
    private static final int DX;
    private static final int DZ;
    private static final int DY;
    private static final Direction[] DIRECTIONS;
    private final BitSet bitSet;
    private static final int[] INDEX_OF_EDGES;
    private int empty;
    
    public VisGraph() {
        this.bitSet = new BitSet(4096);
        this.empty = 4096;
    }
    
    public void setOpaque(final BlockPos fx) {
        this.bitSet.set(getIndex(fx), true);
        --this.empty;
    }
    
    private static int getIndex(final BlockPos fx) {
        return getIndex(fx.getX() & 0xF, fx.getY() & 0xF, fx.getZ() & 0xF);
    }
    
    private static int getIndex(final int integer1, final int integer2, final int integer3) {
        return integer1 << 0 | integer2 << 8 | integer3 << 4;
    }
    
    public VisibilitySet resolve() {
        final VisibilitySet ecp2 = new VisibilitySet();
        if (4096 - this.empty < 256) {
            ecp2.setAll(true);
        }
        else if (this.empty == 0) {
            ecp2.setAll(false);
        }
        else {
            for (final int integer6 : VisGraph.INDEX_OF_EDGES) {
                if (!this.bitSet.get(integer6)) {
                    ecp2.add(this.floodFill(integer6));
                }
            }
        }
        return ecp2;
    }
    
    private Set<Direction> floodFill(final int integer) {
        final Set<Direction> set3 = (Set<Direction>)EnumSet.noneOf((Class)Direction.class);
        final IntPriorityQueue intPriorityQueue4 = (IntPriorityQueue)new IntArrayFIFOQueue();
        intPriorityQueue4.enqueue(integer);
        this.bitSet.set(integer, true);
        while (!intPriorityQueue4.isEmpty()) {
            final int integer2 = intPriorityQueue4.dequeueInt();
            this.addEdges(integer2, set3);
            for (final Direction gc9 : VisGraph.DIRECTIONS) {
                final int integer3 = this.getNeighborIndexAtFace(integer2, gc9);
                if (integer3 >= 0 && !this.bitSet.get(integer3)) {
                    this.bitSet.set(integer3, true);
                    intPriorityQueue4.enqueue(integer3);
                }
            }
        }
        return set3;
    }
    
    private void addEdges(final int integer, final Set<Direction> set) {
        final int integer2 = integer >> 0 & 0xF;
        if (integer2 == 0) {
            set.add(Direction.WEST);
        }
        else if (integer2 == 15) {
            set.add(Direction.EAST);
        }
        final int integer3 = integer >> 8 & 0xF;
        if (integer3 == 0) {
            set.add(Direction.DOWN);
        }
        else if (integer3 == 15) {
            set.add(Direction.UP);
        }
        final int integer4 = integer >> 4 & 0xF;
        if (integer4 == 0) {
            set.add(Direction.NORTH);
        }
        else if (integer4 == 15) {
            set.add(Direction.SOUTH);
        }
    }
    
    private int getNeighborIndexAtFace(final int integer, final Direction gc) {
        switch (gc) {
            case DOWN: {
                if ((integer >> 8 & 0xF) == 0x0) {
                    return -1;
                }
                return integer - VisGraph.DY;
            }
            case UP: {
                if ((integer >> 8 & 0xF) == 0xF) {
                    return -1;
                }
                return integer + VisGraph.DY;
            }
            case NORTH: {
                if ((integer >> 4 & 0xF) == 0x0) {
                    return -1;
                }
                return integer - VisGraph.DZ;
            }
            case SOUTH: {
                if ((integer >> 4 & 0xF) == 0xF) {
                    return -1;
                }
                return integer + VisGraph.DZ;
            }
            case WEST: {
                if ((integer >> 0 & 0xF) == 0x0) {
                    return -1;
                }
                return integer - VisGraph.DX;
            }
            case EAST: {
                if ((integer >> 0 & 0xF) == 0xF) {
                    return -1;
                }
                return integer + VisGraph.DX;
            }
            default: {
                return -1;
            }
        }
    }
    
    static {
        DX = (int)Math.pow(16.0, 0.0);
        DZ = (int)Math.pow(16.0, 1.0);
        DY = (int)Math.pow(16.0, 2.0);
        DIRECTIONS = Direction.values();
        INDEX_OF_EDGES = Util.<int[]>make(new int[1352], (java.util.function.Consumer<int[]>)(arr -> {
            final int integer2 = 0;
            final int integer3 = 15;
            int integer4 = 0;
            for (int integer5 = 0; integer5 < 16; ++integer5) {
                for (int integer6 = 0; integer6 < 16; ++integer6) {
                    for (int integer7 = 0; integer7 < 16; ++integer7) {
                        if (integer5 == 0 || integer5 == 15 || integer6 == 0 || integer6 == 15 || integer7 == 0 || integer7 == 15) {
                            arr[integer4++] = getIndex(integer5, integer6, integer7);
                        }
                    }
                }
            }
        }));
    }
}
