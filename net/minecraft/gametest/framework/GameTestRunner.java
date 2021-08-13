package net.minecraft.gametest.framework;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import com.google.common.collect.Streams;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.mutable.MutableInt;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerPlayer;
import java.util.function.Predicate;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import java.util.Arrays;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.ChatFormatting;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import java.util.Collection;
import net.minecraft.Util;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;

public class GameTestRunner {
    public static TestReporter TEST_REPORTER;
    
    public static void runTest(final GameTestInfo lf, final BlockPos fx, final GameTestTicker ll) {
        lf.startExecution();
        ll.add(lf);
        lf.addListener(new GameTestListener() {
            public void testStructureLoaded(final GameTestInfo lf) {
                spawnBeacon(lf, Blocks.LIGHT_GRAY_STAINED_GLASS);
            }
            
            public void testFailed(final GameTestInfo lf) {
                spawnBeacon(lf, lf.isRequired() ? Blocks.RED_STAINED_GLASS : Blocks.ORANGE_STAINED_GLASS);
                spawnLectern(lf, Util.describeError(lf.getError()));
                visualizeFailedTest(lf);
            }
        });
        lf.spawnStructure(fx, 2);
    }
    
    public static Collection<GameTestInfo> runTestBatches(final Collection<GameTestBatch> collection, final BlockPos fx, final Rotation bzj, final ServerLevel aag, final GameTestTicker ll, final int integer) {
        final GameTestBatchRunner lb7 = new GameTestBatchRunner(collection, fx, bzj, aag, ll, integer);
        lb7.start();
        return (Collection<GameTestInfo>)lb7.getTestInfos();
    }
    
    public static Collection<GameTestInfo> runTests(final Collection<TestFunction> collection, final BlockPos fx, final Rotation bzj, final ServerLevel aag, final GameTestTicker ll, final int integer) {
        return runTestBatches(groupTestsIntoBatches(collection), fx, bzj, aag, ll, integer);
    }
    
    public static Collection<GameTestBatch> groupTestsIntoBatches(final Collection<TestFunction> collection) {
        final Map<String, Collection<TestFunction>> map2 = (Map<String, Collection<TestFunction>>)Maps.newHashMap();
        collection.forEach(lu -> {
            final String string3 = lu.getBatchName();
            final Collection<TestFunction> collection4 = (Collection<TestFunction>)map2.computeIfAbsent(string3, string -> Lists.newArrayList());
            collection4.add(lu);
        });
        return (Collection<GameTestBatch>)map2.keySet().stream().flatMap(string -> {
            final Collection<TestFunction> collection3 = (Collection<TestFunction>)map2.get(string);
            final Consumer<ServerLevel> consumer4 = GameTestRegistry.getBeforeBatchFunction(string);
            final MutableInt mutableInt5 = new MutableInt();
            return Streams.stream(Iterables.partition((Iterable)collection3, 100)).map2(list -> new GameTestBatch(string + ":" + mutableInt5.incrementAndGet(), collection3, consumer4));
        }).collect(Collectors.toList());
    }
    
    private static void visualizeFailedTest(final GameTestInfo lf) {
        final Throwable throwable2 = lf.getError();
        final String string3 = (lf.isRequired() ? "" : "(optional) ") + lf.getTestName() + " failed! " + Util.describeError(throwable2);
        say(lf.getLevel(), lf.isRequired() ? ChatFormatting.RED : ChatFormatting.YELLOW, string3);
        if (throwable2 instanceof GameTestAssertPosException) {
            final GameTestAssertPosException kz4 = (GameTestAssertPosException)throwable2;
            showRedBox(lf.getLevel(), kz4.getAbsolutePos(), kz4.getMessageToShowAtBlock());
        }
        GameTestRunner.TEST_REPORTER.onTestFailed(lf);
    }
    
    private static void spawnBeacon(final GameTestInfo lf, final Block bul) {
        final ServerLevel aag3 = lf.getLevel();
        final BlockPos fx4 = lf.getStructureBlockPos();
        final BlockPos fx5 = new BlockPos(-1, -1, -1);
        final BlockPos fx6 = StructureTemplate.transform(fx4.offset(fx5), Mirror.NONE, lf.getRotation(), fx4);
        aag3.setBlockAndUpdate(fx6, Blocks.BEACON.defaultBlockState().rotate(lf.getRotation()));
        final BlockPos fx7 = fx6.offset(0, 1, 0);
        aag3.setBlockAndUpdate(fx7, bul.defaultBlockState());
        for (int integer8 = -1; integer8 <= 1; ++integer8) {
            for (int integer9 = -1; integer9 <= 1; ++integer9) {
                final BlockPos fx8 = fx6.offset(integer8, -1, integer9);
                aag3.setBlockAndUpdate(fx8, Blocks.IRON_BLOCK.defaultBlockState());
            }
        }
    }
    
    private static void spawnLectern(final GameTestInfo lf, final String string) {
        final ServerLevel aag3 = lf.getLevel();
        final BlockPos fx4 = lf.getStructureBlockPos();
        final BlockPos fx5 = new BlockPos(-1, 1, -1);
        final BlockPos fx6 = StructureTemplate.transform(fx4.offset(fx5), Mirror.NONE, lf.getRotation(), fx4);
        aag3.setBlockAndUpdate(fx6, Blocks.LECTERN.defaultBlockState().rotate(lf.getRotation()));
        final BlockState cee7 = aag3.getBlockState(fx6);
        final ItemStack bly8 = createBook(lf.getTestName(), lf.isRequired(), string);
        LecternBlock.tryPlaceBook(aag3, fx6, cee7, bly8);
    }
    
    private static ItemStack createBook(final String string1, final boolean boolean2, final String string3) {
        final ItemStack bly4 = new ItemStack(Items.WRITABLE_BOOK);
        final ListTag mj5 = new ListTag();
        final StringBuffer stringBuffer6 = new StringBuffer();
        Arrays.stream((Object[])string1.split("\\.")).forEach(string -> stringBuffer6.append(string).append('\n'));
        if (!boolean2) {
            stringBuffer6.append("(optional)\n");
        }
        stringBuffer6.append("-------------------\n");
        mj5.add(StringTag.valueOf(stringBuffer6.toString() + string3));
        bly4.addTagElement("pages", (Tag)mj5);
        return bly4;
    }
    
    private static void say(final ServerLevel aag, final ChatFormatting k, final String string) {
        aag.getPlayers((aah -> true)).forEach(aah -> aah.sendMessage(new TextComponent(string).withStyle(k), Util.NIL_UUID));
    }
    
    public static void clearMarkers(final ServerLevel aag) {
        DebugPackets.sendGameTestClearPacket(aag);
    }
    
    private static void showRedBox(final ServerLevel aag, final BlockPos fx, final String string) {
        DebugPackets.sendGameTestAddMarker(aag, fx, string, -2130771968, Integer.MAX_VALUE);
    }
    
    public static void clearAllTests(final ServerLevel aag, final BlockPos fx, final GameTestTicker ll, final int integer) {
        ll.clear();
        final BlockPos fx2 = fx.offset(-integer, 0, -integer);
        final BlockPos fx3 = fx.offset(integer, 0, integer);
        BlockPos.betweenClosedStream(fx2, fx3).filter(fx -> aag.getBlockState(fx).is(Blocks.STRUCTURE_BLOCK)).forEach(fx -> {
            final StructureBlockEntity cdg3 = (StructureBlockEntity)aag.getBlockEntity(fx);
            final BlockPos fx2 = cdg3.getBlockPos();
            final BoundingBox cqx5 = StructureUtils.getStructureBoundingBox(cdg3);
            StructureUtils.clearSpaceForStructure(cqx5, fx2.getY(), aag);
        });
    }
    
    static {
        GameTestRunner.TEST_REPORTER = new LogTestReporter();
    }
}
