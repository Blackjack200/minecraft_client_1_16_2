package net.minecraft.world.level.lighting;

import net.minecraft.util.Mth;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.function.LongPredicate;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;

public abstract class DynamicGraphMinFixedPoint {
    private final int levelCount;
    private final LongLinkedOpenHashSet[] queues;
    private final Long2ByteMap computedLevels;
    private int firstQueuedLevel;
    private volatile boolean hasWork;
    
    protected DynamicGraphMinFixedPoint(final int integer1, final int integer2, final int integer3) {
        if (integer1 >= 254) {
            throw new IllegalArgumentException("Level count must be < 254.");
        }
        this.levelCount = integer1;
        this.queues = new LongLinkedOpenHashSet[integer1];
        for (int integer4 = 0; integer4 < integer1; ++integer4) {
            this.queues[integer4] = new LongLinkedOpenHashSet(integer2, 0.5f) {
                protected void rehash(final int integer) {
                    if (integer > integer2) {
                        super.rehash(integer);
                    }
                }
            };
        }
        (this.computedLevels = (Long2ByteMap)new Long2ByteOpenHashMap(integer3, 0.5f) {
            protected void rehash(final int integer) {
                if (integer > integer3) {
                    super.rehash(integer);
                }
            }
        }).defaultReturnValue((byte)(-1));
        this.firstQueuedLevel = integer1;
    }
    
    private int getKey(final int integer1, final int integer2) {
        int integer3 = integer1;
        if (integer3 > integer2) {
            integer3 = integer2;
        }
        if (integer3 > this.levelCount - 1) {
            integer3 = this.levelCount - 1;
        }
        return integer3;
    }
    
    private void checkFirstQueuedLevel(final int integer) {
        final int integer2 = this.firstQueuedLevel;
        this.firstQueuedLevel = integer;
        for (int integer3 = integer2 + 1; integer3 < integer; ++integer3) {
            if (!this.queues[integer3].isEmpty()) {
                this.firstQueuedLevel = integer3;
                break;
            }
        }
    }
    
    protected void removeFromQueue(final long long1) {
        final int integer4 = this.computedLevels.get(long1) & 0xFF;
        if (integer4 == 255) {
            return;
        }
        final int integer5 = this.getLevel(long1);
        final int integer6 = this.getKey(integer5, integer4);
        this.dequeue(long1, integer6, this.levelCount, true);
        this.hasWork = (this.firstQueuedLevel < this.levelCount);
    }
    
    public void removeIf(final LongPredicate longPredicate) {
        final LongList longList3 = (LongList)new LongArrayList();
        this.computedLevels.keySet().forEach(long3 -> {
            if (longPredicate.test(long3)) {
                longList3.add(long3);
            }
        });
        longList3.forEach(this::removeFromQueue);
    }
    
    private void dequeue(final long long1, final int integer2, final int integer3, final boolean boolean4) {
        if (boolean4) {
            this.computedLevels.remove(long1);
        }
        this.queues[integer2].remove(long1);
        if (this.queues[integer2].isEmpty() && this.firstQueuedLevel == integer2) {
            this.checkFirstQueuedLevel(integer3);
        }
    }
    
    private void enqueue(final long long1, final int integer2, final int integer3) {
        this.computedLevels.put(long1, (byte)integer2);
        this.queues[integer3].add(long1);
        if (this.firstQueuedLevel > integer3) {
            this.firstQueuedLevel = integer3;
        }
    }
    
    protected void checkNode(final long long1) {
        this.checkEdge(long1, long1, this.levelCount - 1, false);
    }
    
    protected void checkEdge(final long long1, final long long2, final int integer, final boolean boolean4) {
        this.checkEdge(long1, long2, integer, this.getLevel(long2), this.computedLevels.get(long2) & 0xFF, boolean4);
        this.hasWork = (this.firstQueuedLevel < this.levelCount);
    }
    
    private void checkEdge(final long long1, final long long2, int integer3, int integer4, int integer5, final boolean boolean6) {
        if (this.isSource(long2)) {
            return;
        }
        integer3 = Mth.clamp(integer3, 0, this.levelCount - 1);
        integer4 = Mth.clamp(integer4, 0, this.levelCount - 1);
        boolean boolean7;
        if (integer5 == 255) {
            boolean7 = true;
            integer5 = integer4;
        }
        else {
            boolean7 = false;
        }
        int integer6;
        if (boolean6) {
            integer6 = Math.min(integer5, integer3);
        }
        else {
            integer6 = Mth.clamp(this.getComputedLevel(long2, long1, integer3), 0, this.levelCount - 1);
        }
        final int integer7 = this.getKey(integer4, integer5);
        if (integer4 != integer6) {
            final int integer8 = this.getKey(integer4, integer6);
            if (integer7 != integer8 && !boolean7) {
                this.dequeue(long2, integer7, integer8, false);
            }
            this.enqueue(long2, integer6, integer8);
        }
        else if (!boolean7) {
            this.dequeue(long2, integer7, this.levelCount, true);
        }
    }
    
    protected final void checkNeighbor(final long long1, final long long2, final int integer, final boolean boolean4) {
        final int integer2 = this.computedLevels.get(long2) & 0xFF;
        final int integer3 = Mth.clamp(this.computeLevelFromNeighbor(long1, long2, integer), 0, this.levelCount - 1);
        if (boolean4) {
            this.checkEdge(long1, long2, integer3, this.getLevel(long2), integer2, true);
        }
        else {
            boolean boolean5;
            int integer4;
            if (integer2 == 255) {
                boolean5 = true;
                integer4 = Mth.clamp(this.getLevel(long2), 0, this.levelCount - 1);
            }
            else {
                integer4 = integer2;
                boolean5 = false;
            }
            if (integer3 == integer4) {
                this.checkEdge(long1, long2, this.levelCount - 1, boolean5 ? integer4 : this.getLevel(long2), integer2, false);
            }
        }
    }
    
    protected final boolean hasWork() {
        return this.hasWork;
    }
    
    protected final int runUpdates(int integer) {
        if (this.firstQueuedLevel >= this.levelCount) {
            return integer;
        }
        while (this.firstQueuedLevel < this.levelCount && integer > 0) {
            --integer;
            final LongLinkedOpenHashSet longLinkedOpenHashSet3 = this.queues[this.firstQueuedLevel];
            final long long4 = longLinkedOpenHashSet3.removeFirstLong();
            final int integer2 = Mth.clamp(this.getLevel(long4), 0, this.levelCount - 1);
            if (longLinkedOpenHashSet3.isEmpty()) {
                this.checkFirstQueuedLevel(this.levelCount);
            }
            final int integer3 = this.computedLevels.remove(long4) & 0xFF;
            if (integer3 < integer2) {
                this.setLevel(long4, integer3);
                this.checkNeighborsAfterUpdate(long4, integer3, true);
            }
            else {
                if (integer3 <= integer2) {
                    continue;
                }
                this.enqueue(long4, integer3, this.getKey(this.levelCount - 1, integer3));
                this.setLevel(long4, this.levelCount - 1);
                this.checkNeighborsAfterUpdate(long4, integer2, false);
            }
        }
        this.hasWork = (this.firstQueuedLevel < this.levelCount);
        return integer;
    }
    
    public int getQueueSize() {
        return this.computedLevels.size();
    }
    
    protected abstract boolean isSource(final long long1);
    
    protected abstract int getComputedLevel(final long long1, final long long2, final int integer);
    
    protected abstract void checkNeighborsAfterUpdate(final long long1, final int integer, final boolean boolean3);
    
    protected abstract int getLevel(final long long1);
    
    protected abstract void setLevel(final long long1, final int integer);
    
    protected abstract int computeLevelFromNeighbor(final long long1, final long long2, final int integer);
}
