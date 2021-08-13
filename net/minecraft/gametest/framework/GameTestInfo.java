package net.minecraft.gametest.framework;

import net.minecraft.world.level.block.entity.StructureBlockEntity;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Rotation;
import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import java.util.Collection;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;

public class GameTestInfo {
    private final TestFunction testFunction;
    @Nullable
    private BlockPos structureBlockPos;
    private final ServerLevel level;
    private final Collection<GameTestListener> listeners;
    private final int timeoutTicks;
    private final Collection<GameTestSequence> sequences;
    private Object2LongMap<Runnable> runAtTickTimeMap;
    private long startTick;
    private long tickCount;
    private boolean started;
    private final Stopwatch timer;
    private boolean done;
    private final Rotation rotation;
    @Nullable
    private Throwable error;
    
    public GameTestInfo(final TestFunction lu, final Rotation bzj, final ServerLevel aag) {
        this.listeners = (Collection<GameTestListener>)Lists.newArrayList();
        this.sequences = (Collection<GameTestSequence>)Lists.newCopyOnWriteArrayList();
        this.runAtTickTimeMap = (Object2LongMap<Runnable>)new Object2LongOpenHashMap();
        this.started = false;
        this.timer = Stopwatch.createUnstarted();
        this.done = false;
        this.testFunction = lu;
        this.level = aag;
        this.timeoutTicks = lu.getMaxTicks();
        this.rotation = lu.getRotation().getRotated(bzj);
    }
    
    void setStructureBlockPos(final BlockPos fx) {
        this.structureBlockPos = fx;
    }
    
    void startExecution() {
        this.startTick = this.level.getGameTime() + 1L + this.testFunction.getSetupTicks();
        this.timer.start();
    }
    
    public void tick() {
        if (this.isDone()) {
            return;
        }
        this.tickCount = this.level.getGameTime() - this.startTick;
        if (this.tickCount < 0L) {
            return;
        }
        if (this.tickCount == 0L) {
            this.startTest();
        }
        final ObjectIterator<Object2LongMap.Entry<Runnable>> objectIterator2 = (ObjectIterator<Object2LongMap.Entry<Runnable>>)this.runAtTickTimeMap.object2LongEntrySet().iterator();
        while (objectIterator2.hasNext()) {
            final Object2LongMap.Entry<Runnable> entry3 = (Object2LongMap.Entry<Runnable>)objectIterator2.next();
            if (entry3.getLongValue() <= this.tickCount) {
                try {
                    ((Runnable)entry3.getKey()).run();
                }
                catch (Exception exception4) {
                    this.fail((Throwable)exception4);
                }
                objectIterator2.remove();
            }
        }
        if (this.tickCount > this.timeoutTicks) {
            if (this.sequences.isEmpty()) {
                this.fail((Throwable)new GameTestTimeoutException(new StringBuilder().append("Didn't succeed or fail within ").append(this.testFunction.getMaxTicks()).append(" ticks").toString()));
            }
            else {
                this.sequences.forEach(lj -> lj.tickAndFailIfNotComplete(this.tickCount));
                if (this.error == null) {
                    this.fail((Throwable)new GameTestTimeoutException("No sequences finished"));
                }
            }
        }
        else {
            this.sequences.forEach(lj -> lj.tickAndContinue(this.tickCount));
        }
    }
    
    private void startTest() {
        if (this.started) {
            throw new IllegalStateException("Test already started");
        }
        this.started = true;
        try {
            this.testFunction.run(new GameTestHelper(this));
        }
        catch (Exception exception2) {
            this.fail((Throwable)exception2);
        }
    }
    
    public String getTestName() {
        return this.testFunction.getTestName();
    }
    
    public BlockPos getStructureBlockPos() {
        return this.structureBlockPos;
    }
    
    public ServerLevel getLevel() {
        return this.level;
    }
    
    public boolean hasSucceeded() {
        return this.done && this.error == null;
    }
    
    public boolean hasFailed() {
        return this.error != null;
    }
    
    public boolean hasStarted() {
        return this.started;
    }
    
    public boolean isDone() {
        return this.done;
    }
    
    private void finish() {
        if (!this.done) {
            this.done = true;
            this.timer.stop();
        }
    }
    
    public void fail(final Throwable throwable) {
        this.finish();
        this.error = throwable;
        this.listeners.forEach(lg -> lg.testFailed(this));
    }
    
    @Nullable
    public Throwable getError() {
        return this.error;
    }
    
    public String toString() {
        return this.getTestName();
    }
    
    public void addListener(final GameTestListener lg) {
        this.listeners.add(lg);
    }
    
    public void spawnStructure(final BlockPos fx, final int integer) {
        final StructureBlockEntity cdg4 = StructureUtils.spawnStructure(this.getStructureName(), fx, this.getRotation(), integer, this.level, false);
        this.setStructureBlockPos(cdg4.getBlockPos());
        cdg4.setStructureName(this.getTestName());
        StructureUtils.addCommandBlockAndButtonToStartTest(this.structureBlockPos, new BlockPos(1, 0, -1), this.getRotation(), this.level);
        this.listeners.forEach(lg -> lg.testStructureLoaded(this));
    }
    
    public boolean isRequired() {
        return this.testFunction.isRequired();
    }
    
    public boolean isOptional() {
        return !this.testFunction.isRequired();
    }
    
    public String getStructureName() {
        return this.testFunction.getStructureName();
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public TestFunction getTestFunction() {
        return this.testFunction;
    }
}
