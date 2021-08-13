package net.minecraft.gametest.framework;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;
import java.util.function.Predicate;
import java.io.OutputStream;
import java.io.BufferedReader;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.TagParser;
import java.nio.file.OpenOption;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.function.Consumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nullable;
import java.util.Collection;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import net.minecraft.commands.arguments.blocks.BlockInput;
import java.util.Collections;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class TestCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("test").then(Commands.literal("runthis").executes(commandContext -> runNearbyTest((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("runthese").executes(commandContext -> runAllNearbyTests((CommandSourceStack)commandContext.getSource())))).then(((LiteralArgumentBuilder)Commands.literal("runfailed").executes(commandContext -> runLastFailedTests((CommandSourceStack)commandContext.getSource(), false, 0, 8))).then(((RequiredArgumentBuilder)Commands.argument("onlyRequiredTests", (com.mojang.brigadier.arguments.ArgumentType<Object>)BoolArgumentType.bool()).executes(commandContext -> runLastFailedTests((CommandSourceStack)commandContext.getSource(), BoolArgumentType.getBool(commandContext, "onlyRequiredTests"), 0, 8))).then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runLastFailedTests((CommandSourceStack)commandContext.getSource(), BoolArgumentType.getBool(commandContext, "onlyRequiredTests"), IntegerArgumentType.getInteger(commandContext, "rotationSteps"), 8))).then(Commands.argument("testsPerRow", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runLastFailedTests((CommandSourceStack)commandContext.getSource(), BoolArgumentType.getBool(commandContext, "onlyRequiredTests"), IntegerArgumentType.getInteger(commandContext, "rotationSteps"), IntegerArgumentType.getInteger(commandContext, "testsPerRow")))))))).then(Commands.literal("run").then(((RequiredArgumentBuilder)Commands.argument("testName", (com.mojang.brigadier.arguments.ArgumentType<Object>)TestFunctionArgument.testFunctionArgument()).executes(commandContext -> runTest((CommandSourceStack)commandContext.getSource(), TestFunctionArgument.getTestFunction((CommandContext<CommandSourceStack>)commandContext, "testName"), 0))).then(Commands.argument("rotationSteps", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runTest((CommandSourceStack)commandContext.getSource(), TestFunctionArgument.getTestFunction((CommandContext<CommandSourceStack>)commandContext, "testName"), IntegerArgumentType.getInteger(commandContext, "rotationSteps"))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("runall").executes(commandContext -> runAllTests((CommandSourceStack)commandContext.getSource(), 0, 8))).then(((RequiredArgumentBuilder)Commands.argument("testClassName", (com.mojang.brigadier.arguments.ArgumentType<Object>)TestClassNameArgument.testClassName()).executes(commandContext -> runAllTestsInClass((CommandSourceStack)commandContext.getSource(), TestClassNameArgument.getTestClassName((CommandContext<CommandSourceStack>)commandContext, "testClassName"), 0, 8))).then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runAllTestsInClass((CommandSourceStack)commandContext.getSource(), TestClassNameArgument.getTestClassName((CommandContext<CommandSourceStack>)commandContext, "testClassName"), IntegerArgumentType.getInteger(commandContext, "rotationSteps"), 8))).then(Commands.argument("testsPerRow", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runAllTestsInClass((CommandSourceStack)commandContext.getSource(), TestClassNameArgument.getTestClassName((CommandContext<CommandSourceStack>)commandContext, "testClassName"), IntegerArgumentType.getInteger(commandContext, "rotationSteps"), IntegerArgumentType.getInteger(commandContext, "testsPerRow"))))))).then(((RequiredArgumentBuilder)Commands.argument("rotationSteps", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runAllTests((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "rotationSteps"), 8))).then(Commands.argument("testsPerRow", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> runAllTests((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "rotationSteps"), IntegerArgumentType.getInteger(commandContext, "testsPerRow"))))))).then(Commands.literal("export").then(Commands.argument("testName", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).executes(commandContext -> exportTestStructure((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "testName")))))).then(Commands.literal("exportthis").executes(commandContext -> exportNearestTestStructure((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("import").then(Commands.argument("testName", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).executes(commandContext -> importTestStructure((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "testName")))))).then(((LiteralArgumentBuilder)Commands.literal("pos").executes(commandContext -> showPos((CommandSourceStack)commandContext.getSource(), "pos"))).then(Commands.argument("var", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).executes(commandContext -> showPos((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "var")))))).then(Commands.literal("create").then(((RequiredArgumentBuilder)Commands.argument("testName", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.word()).executes(commandContext -> createNewStructure((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "testName"), 5, 5, 5))).then(((RequiredArgumentBuilder)Commands.argument("width", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> createNewStructure((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "testName"), IntegerArgumentType.getInteger(commandContext, "width"), IntegerArgumentType.getInteger(commandContext, "width"), IntegerArgumentType.getInteger(commandContext, "width")))).then(Commands.argument("height", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).then(Commands.argument("depth", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> createNewStructure((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "testName"), IntegerArgumentType.getInteger(commandContext, "width"), IntegerArgumentType.getInteger(commandContext, "height"), IntegerArgumentType.getInteger(commandContext, "depth"))))))))).then(((LiteralArgumentBuilder)Commands.literal("clearall").executes(commandContext -> clearAllTests((CommandSourceStack)commandContext.getSource(), 200))).then(Commands.argument("radius", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer()).executes(commandContext -> clearAllTests((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "radius"))))));
    }
    
    private static int createNewStructure(final CommandSourceStack db, final String string, final int integer3, final int integer4, final int integer5) {
        if (integer3 > 48 || integer4 > 48 || integer5 > 48) {
            throw new IllegalArgumentException("The structure must be less than 48 blocks big in each axis");
        }
        final ServerLevel aag6 = db.getLevel();
        final BlockPos fx7 = new BlockPos(db.getPosition());
        final BlockPos fx8 = new BlockPos(fx7.getX(), db.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, fx7).getY(), fx7.getZ() + 3);
        StructureUtils.createNewEmptyStructureBlock(string.toLowerCase(), fx8, new BlockPos(integer3, integer4, integer5), Rotation.NONE, aag6);
        for (int integer6 = 0; integer6 < integer3; ++integer6) {
            for (int integer7 = 0; integer7 < integer5; ++integer7) {
                final BlockPos fx9 = new BlockPos(fx8.getX() + integer6, fx8.getY() + 1, fx8.getZ() + integer7);
                final Block bul12 = Blocks.POLISHED_ANDESITE;
                final BlockInput ef13 = new BlockInput(bul12.defaultBlockState(), (Set<Property<?>>)Collections.EMPTY_SET, null);
                ef13.place(aag6, fx9, 2);
            }
        }
        StructureUtils.addCommandBlockAndButtonToStartTest(fx8, new BlockPos(1, 0, -1), Rotation.NONE, aag6);
        return 0;
    }
    
    private static int showPos(final CommandSourceStack db, final String string) throws CommandSyntaxException {
        final BlockHitResult dcg3 = (BlockHitResult)db.getPlayerOrException().pick(10.0, 1.0f, false);
        final BlockPos fx4 = dcg3.getBlockPos();
        final ServerLevel aag5 = db.getLevel();
        Optional<BlockPos> optional6 = StructureUtils.findStructureBlockContainingPos(fx4, 15, aag5);
        if (!optional6.isPresent()) {
            optional6 = StructureUtils.findStructureBlockContainingPos(fx4, 200, aag5);
        }
        if (!optional6.isPresent()) {
            db.sendFailure(new TextComponent(new StringBuilder().append("Can't find a structure block that contains the targeted pos ").append(fx4).toString()));
            return 0;
        }
        final StructureBlockEntity cdg7 = (StructureBlockEntity)aag5.getBlockEntity((BlockPos)optional6.get());
        final BlockPos fx5 = fx4.subtract((Vec3i)optional6.get());
        final String string2 = new StringBuilder().append(fx5.getX()).append(", ").append(fx5.getY()).append(", ").append(fx5.getZ()).toString();
        final String string3 = cdg7.getStructurePath();
        final Component nr11 = new TextComponent(string2).setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TextComponent("Click to copy to clipboard"))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "final BlockPos " + string + " = new BlockPos(" + string2 + ");")));
        db.sendSuccess(new TextComponent("Position relative to " + string3 + ": ").append(nr11), false);
        DebugPackets.sendGameTestAddMarker(aag5, new BlockPos(fx4), string2, -2147418368, 10000);
        return 1;
    }
    
    private static int runNearbyTest(final CommandSourceStack db) {
        final BlockPos fx2 = new BlockPos(db.getPosition());
        final ServerLevel aag3 = db.getLevel();
        final BlockPos fx3 = StructureUtils.findNearestStructureBlock(fx2, 15, aag3);
        if (fx3 == null) {
            say(aag3, "Couldn't find any structure block within 15 radius", ChatFormatting.RED);
            return 0;
        }
        GameTestRunner.clearMarkers(aag3);
        runTest(aag3, fx3, null);
        return 1;
    }
    
    private static int runAllNearbyTests(final CommandSourceStack db) {
        final BlockPos fx2 = new BlockPos(db.getPosition());
        final ServerLevel aag3 = db.getLevel();
        final Collection<BlockPos> collection4 = StructureUtils.findStructureBlocks(fx2, 200, aag3);
        if (collection4.isEmpty()) {
            say(aag3, "Couldn't find any structure blocks within 200 block radius", ChatFormatting.RED);
            return 1;
        }
        GameTestRunner.clearMarkers(aag3);
        say(db, new StringBuilder().append("Running ").append(collection4.size()).append(" tests...").toString());
        final MultipleTestTracker lp5 = new MultipleTestTracker();
        collection4.forEach(fx -> runTest(aag3, fx, lp5));
        return 1;
    }
    
    private static void runTest(final ServerLevel aag, final BlockPos fx, @Nullable final MultipleTestTracker lp) {
        final StructureBlockEntity cdg4 = (StructureBlockEntity)aag.getBlockEntity(fx);
        final String string5 = cdg4.getStructurePath();
        final TestFunction lu6 = GameTestRegistry.getTestFunction(string5);
        final GameTestInfo lf7 = new GameTestInfo(lu6, cdg4.getRotation(), aag);
        if (lp != null) {
            lp.addTestToTrack(lf7);
            lf7.addListener(new TestSummaryDisplayer(aag, lp));
        }
        runTestPreparation(lu6, aag);
        final AABB dcf8 = StructureUtils.getStructureBounds(cdg4);
        final BlockPos fx2 = new BlockPos(dcf8.minX, dcf8.minY, dcf8.minZ);
        GameTestRunner.runTest(lf7, fx2, GameTestTicker.singleton);
    }
    
    private static void showTestSummaryIfAllDone(final ServerLevel aag, final MultipleTestTracker lp) {
        if (lp.isDone()) {
            say(aag, new StringBuilder().append("GameTest done! ").append(lp.getTotalCount()).append(" tests were run").toString(), ChatFormatting.WHITE);
            if (lp.hasFailedRequired()) {
                say(aag, new StringBuilder().append("").append(lp.getFailedRequiredCount()).append(" required tests failed :(").toString(), ChatFormatting.RED);
            }
            else {
                say(aag, "All required tests passed :)", ChatFormatting.GREEN);
            }
            if (lp.hasFailedOptional()) {
                say(aag, new StringBuilder().append("").append(lp.getFailedOptionalCount()).append(" optional tests failed").toString(), ChatFormatting.GRAY);
            }
        }
    }
    
    private static int clearAllTests(final CommandSourceStack db, final int integer) {
        final ServerLevel aag3 = db.getLevel();
        GameTestRunner.clearMarkers(aag3);
        final BlockPos fx4 = new BlockPos(db.getPosition().x, db.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(db.getPosition())).getY(), db.getPosition().z);
        GameTestRunner.clearAllTests(aag3, fx4, GameTestTicker.singleton, Mth.clamp(integer, 0, 1024));
        return 1;
    }
    
    private static int runTest(final CommandSourceStack db, final TestFunction lu, final int integer) {
        final ServerLevel aag4 = db.getLevel();
        final BlockPos fx5 = new BlockPos(db.getPosition());
        final int integer2 = db.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, fx5).getY();
        final BlockPos fx6 = new BlockPos(fx5.getX(), integer2, fx5.getZ() + 3);
        GameTestRunner.clearMarkers(aag4);
        runTestPreparation(lu, aag4);
        final Rotation bzj8 = StructureUtils.getRotationForRotationSteps(integer);
        final GameTestInfo lf9 = new GameTestInfo(lu, bzj8, aag4);
        GameTestRunner.runTest(lf9, fx6, GameTestTicker.singleton);
        return 1;
    }
    
    private static void runTestPreparation(final TestFunction lu, final ServerLevel aag) {
        final Consumer<ServerLevel> consumer3 = GameTestRegistry.getBeforeBatchFunction(lu.getBatchName());
        if (consumer3 != null) {
            consumer3.accept(aag);
        }
    }
    
    private static int runAllTests(final CommandSourceStack db, final int integer2, final int integer3) {
        GameTestRunner.clearMarkers(db.getLevel());
        final Collection<TestFunction> collection4 = GameTestRegistry.getAllTestFunctions();
        say(db, new StringBuilder().append("Running all ").append(collection4.size()).append(" tests...").toString());
        GameTestRegistry.forgetFailedTests();
        runTests(db, collection4, integer2, integer3);
        return 1;
    }
    
    private static int runAllTestsInClass(final CommandSourceStack db, final String string, final int integer3, final int integer4) {
        final Collection<TestFunction> collection5 = GameTestRegistry.getTestFunctionsForClassName(string);
        GameTestRunner.clearMarkers(db.getLevel());
        say(db, new StringBuilder().append("Running ").append(collection5.size()).append(" tests from ").append(string).append("...").toString());
        GameTestRegistry.forgetFailedTests();
        runTests(db, collection5, integer3, integer4);
        return 1;
    }
    
    private static int runLastFailedTests(final CommandSourceStack db, final boolean boolean2, final int integer3, final int integer4) {
        Collection<TestFunction> collection5;
        if (boolean2) {
            collection5 = (Collection<TestFunction>)GameTestRegistry.getLastFailedTests().stream().filter(TestFunction::isRequired).collect(Collectors.toList());
        }
        else {
            collection5 = GameTestRegistry.getLastFailedTests();
        }
        if (collection5.isEmpty()) {
            say(db, "No failed tests to rerun");
            return 0;
        }
        GameTestRunner.clearMarkers(db.getLevel());
        say(db, new StringBuilder().append("Rerunning ").append(collection5.size()).append(" failed tests (").append(boolean2 ? "only required tests" : "including optional tests").append(")").toString());
        runTests(db, collection5, integer3, integer4);
        return 1;
    }
    
    private static void runTests(final CommandSourceStack db, final Collection<TestFunction> collection, final int integer3, final int integer4) {
        final BlockPos fx5 = new BlockPos(db.getPosition());
        final BlockPos fx6 = new BlockPos(fx5.getX(), db.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, fx5).getY(), fx5.getZ() + 3);
        final ServerLevel aag7 = db.getLevel();
        final Rotation bzj8 = StructureUtils.getRotationForRotationSteps(integer3);
        final Collection<GameTestInfo> collection2 = GameTestRunner.runTests(collection, fx6, bzj8, aag7, GameTestTicker.singleton, integer4);
        final MultipleTestTracker lp10 = new MultipleTestTracker(collection2);
        lp10.addListener(new TestSummaryDisplayer(aag7, lp10));
        lp10.addFailureListener((Consumer<GameTestInfo>)(lf -> GameTestRegistry.rememberFailedTest(lf.getTestFunction())));
    }
    
    private static void say(final CommandSourceStack db, final String string) {
        db.sendSuccess(new TextComponent(string), false);
    }
    
    private static int exportNearestTestStructure(final CommandSourceStack db) {
        final BlockPos fx2 = new BlockPos(db.getPosition());
        final ServerLevel aag3 = db.getLevel();
        final BlockPos fx3 = StructureUtils.findNearestStructureBlock(fx2, 15, aag3);
        if (fx3 == null) {
            say(aag3, "Couldn't find any structure block within 15 radius", ChatFormatting.RED);
            return 0;
        }
        final StructureBlockEntity cdg5 = (StructureBlockEntity)aag3.getBlockEntity(fx3);
        final String string6 = cdg5.getStructurePath();
        return exportTestStructure(db, string6);
    }
    
    private static int exportTestStructure(final CommandSourceStack db, final String string) {
        final Path path3 = Paths.get(StructureUtils.testStructuresDir, new String[0]);
        final ResourceLocation vk4 = new ResourceLocation("minecraft", string);
        final Path path4 = db.getLevel().getStructureManager().createPathToStructure(vk4, ".nbt");
        final Path path5 = NbtToSnbt.convertStructure(path4, string, path3);
        if (path5 == null) {
            say(db, new StringBuilder().append("Failed to export ").append(path4).toString());
            return 1;
        }
        try {
            Files.createDirectories(path5.getParent(), new FileAttribute[0]);
        }
        catch (IOException iOException7) {
            say(db, new StringBuilder().append("Could not create folder ").append(path5.getParent()).toString());
            iOException7.printStackTrace();
            return 1;
        }
        say(db, "Exported " + string + " to " + path5.toAbsolutePath());
        return 0;
    }
    
    private static int importTestStructure(final CommandSourceStack db, final String string) {
        final Path path3 = Paths.get(StructureUtils.testStructuresDir, new String[] { string + ".snbt" });
        final ResourceLocation vk4 = new ResourceLocation("minecraft", string);
        final Path path4 = db.getLevel().getStructureManager().createPathToStructure(vk4, ".nbt");
        try {
            final BufferedReader bufferedReader6 = Files.newBufferedReader(path3);
            final String string2 = IOUtils.toString((Reader)bufferedReader6);
            Files.createDirectories(path4.getParent(), new FileAttribute[0]);
            try (final OutputStream outputStream8 = Files.newOutputStream(path4, new OpenOption[0])) {
                NbtIo.writeCompressed(TagParser.parseTag(string2), outputStream8);
            }
            say(db, new StringBuilder().append("Imported to ").append(path4.toAbsolutePath()).toString());
            return 0;
        }
        catch (IOException | CommandSyntaxException ex2) {
            final Exception ex;
            final Exception exception6 = ex;
            System.err.println("Failed to load structure " + string);
            exception6.printStackTrace();
            return 1;
        }
    }
    
    private static void say(final ServerLevel aag, final String string, final ChatFormatting k) {
        aag.getPlayers((aah -> true)).forEach(aah -> aah.sendMessage(new TextComponent(k + string), Util.NIL_UUID));
    }
    
    static class TestSummaryDisplayer implements GameTestListener {
        private final ServerLevel level;
        private final MultipleTestTracker tracker;
        
        public TestSummaryDisplayer(final ServerLevel aag, final MultipleTestTracker lp) {
            this.level = aag;
            this.tracker = lp;
        }
        
        public void testStructureLoaded(final GameTestInfo lf) {
        }
        
        public void testFailed(final GameTestInfo lf) {
            showTestSummaryIfAllDone(this.level, this.tracker);
        }
    }
}
