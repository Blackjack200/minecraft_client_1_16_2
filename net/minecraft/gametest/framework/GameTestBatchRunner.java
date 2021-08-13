package net.minecraft.gametest.framework;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Rotation;
import java.util.Collection;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.Logger;

public class GameTestBatchRunner {
    private static final Logger LOGGER;
    private final BlockPos firstTestNorthWestCorner;
    private final ServerLevel level;
    private final GameTestTicker testTicker;
    private final int testsPerRow;
    private final List<GameTestInfo> allTestInfos;
    private final Map<GameTestInfo, BlockPos> northWestCorners;
    private final List<Pair<GameTestBatch, Collection<GameTestInfo>>> batches;
    private MultipleTestTracker currentBatchTracker;
    private int currentBatchIndex;
    private BlockPos.MutableBlockPos nextTestNorthWestCorner;
    
    public GameTestBatchRunner(final Collection<GameTestBatch> collection, final BlockPos fx, final Rotation bzj, final ServerLevel aag, final GameTestTicker ll, final int integer) {
        this.allTestInfos = (List<GameTestInfo>)Lists.newArrayList();
        this.northWestCorners = (Map<GameTestInfo, BlockPos>)Maps.newHashMap();
        this.batches = (List<Pair<GameTestBatch, Collection<GameTestInfo>>>)Lists.newArrayList();
        this.currentBatchIndex = 0;
        this.nextTestNorthWestCorner = fx.mutable();
        this.firstTestNorthWestCorner = fx;
        this.level = aag;
        this.testTicker = ll;
        this.testsPerRow = integer;
        collection.forEach(la -> {
            final Collection<GameTestInfo> collection5 = (Collection<GameTestInfo>)Lists.newArrayList();
            final Collection<TestFunction> collection6 = la.getTestFunctions();
            for (final TestFunction lu8 : collection6) {
                final GameTestInfo lf9 = new GameTestInfo(lu8, bzj, aag);
                collection5.add(lf9);
                this.allTestInfos.add(lf9);
            }
            this.batches.add(Pair.of((Object)la, (Object)collection5));
        });
    }
    
    public List<GameTestInfo> getTestInfos() {
        return this.allTestInfos;
    }
    
    public void start() {
        this.runBatch(0);
    }
    
    private void runBatch(final int integer) {
        this.currentBatchIndex = integer;
        this.currentBatchTracker = new MultipleTestTracker();
        if (integer >= this.batches.size()) {
            return;
        }
        final Pair<GameTestBatch, Collection<GameTestInfo>> pair3 = (Pair<GameTestBatch, Collection<GameTestInfo>>)this.batches.get(this.currentBatchIndex);
        final GameTestBatch la4 = (GameTestBatch)pair3.getFirst();
        final Collection<GameTestInfo> collection5 = (Collection<GameTestInfo>)pair3.getSecond();
        this.createStructuresForBatch(collection5);
        la4.runBeforeBatchFunction(this.level);
        final String string6 = la4.getName();
        GameTestBatchRunner.LOGGER.info("Running test batch '" + string6 + "' (" + collection5.size() + " tests)...");
        collection5.forEach(lf -> {
            this.currentBatchTracker.addTestToTrack(lf);
            this.currentBatchTracker.addListener(new GameTestListener() {
                public void testStructureLoaded(final GameTestInfo lf) {
                }
                
                public void testFailed(final GameTestInfo lf) {
                    GameTestBatchRunner.this.testCompleted(lf);
                }
            });
            final BlockPos fx3 = (BlockPos)this.northWestCorners.get(lf);
            GameTestRunner.runTest(lf, fx3, this.testTicker);
        });
    }
    
    private void testCompleted(final GameTestInfo lf) {
        if (this.currentBatchTracker.isDone()) {
            this.runBatch(this.currentBatchIndex + 1);
        }
    }
    
    private void createStructuresForBatch(final Collection<GameTestInfo> collection) {
        int integer3 = 0;
        AABB dcf4 = new AABB(this.nextTestNorthWestCorner);
        for (final GameTestInfo lf6 : collection) {
            final BlockPos fx7 = new BlockPos(this.nextTestNorthWestCorner);
            final StructureBlockEntity cdg8 = StructureUtils.spawnStructure(lf6.getStructureName(), fx7, lf6.getRotation(), 2, this.level, true);
            final AABB dcf5 = StructureUtils.getStructureBounds(cdg8);
            lf6.setStructureBlockPos(cdg8.getBlockPos());
            this.northWestCorners.put(lf6, new BlockPos(this.nextTestNorthWestCorner));
            dcf4 = dcf4.minmax(dcf5);
            this.nextTestNorthWestCorner.move((int)dcf5.getXsize() + 5, 0, 0);
            if (integer3++ % this.testsPerRow == this.testsPerRow - 1) {
                this.nextTestNorthWestCorner.move(0, 0, (int)dcf4.getZsize() + 6);
                this.nextTestNorthWestCorner.setX(this.firstTestNorthWestCorner.getX());
                dcf4 = new AABB(this.nextTestNorthWestCorner);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
