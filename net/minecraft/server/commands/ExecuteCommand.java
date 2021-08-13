package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ByteTag;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import java.util.function.Supplier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.OptionalInt;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Collections;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.scores.Score;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.Command;
import net.minecraft.commands.arguments.RangeArgument;
import java.util.function.BiPredicate;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.nbt.Tag;
import java.util.function.IntFunction;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.ResultConsumer;
import java.util.function.BinaryOperator;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

public class ExecuteCommand {
    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
    private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED;
    private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT;
    private static final BinaryOperator<ResultConsumer<CommandSourceStack>> CALLBACK_CHAINER;
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_PREDICATE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralCommandNode<CommandSourceStack> literalCommandNode2 = (LiteralCommandNode<CommandSourceStack>)commandDispatcher.register((LiteralArgumentBuilder)Commands.literal("execute").requires(db -> db.hasPermission(2)));
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("execute").requires(db -> db.hasPermission(2))).then(Commands.literal("run").redirect((CommandNode)commandDispatcher.getRoot()))).then((ArgumentBuilder)addConditionals((CommandNode<CommandSourceStack>)literalCommandNode2, Commands.literal("if"), true))).then((ArgumentBuilder)addConditionals((CommandNode<CommandSourceStack>)literalCommandNode2, Commands.literal("unless"), false))).then(Commands.literal("as").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).fork((CommandNode)literalCommandNode2, commandContext -> {
            final List<CommandSourceStack> list2 = (List<CommandSourceStack>)Lists.newArrayList();
            for (final Entity apx4 : EntityArgument.getOptionalEntities((CommandContext<CommandSourceStack>)commandContext, "targets")) {
                list2.add(((CommandSourceStack)commandContext.getSource()).withEntity(apx4));
            }
            return (Collection)list2;
        })))).then(Commands.literal("at").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).fork((CommandNode)literalCommandNode2, commandContext -> {
            final List<CommandSourceStack> list2 = (List<CommandSourceStack>)Lists.newArrayList();
            for (final Entity apx4 : EntityArgument.getOptionalEntities((CommandContext<CommandSourceStack>)commandContext, "targets")) {
                list2.add(((CommandSourceStack)commandContext.getSource()).withLevel((ServerLevel)apx4.level).withPosition(apx4.position()).withRotation(apx4.getRotationVector()));
            }
            return (Collection)list2;
        })))).then(((LiteralArgumentBuilder)Commands.literal("store").then((ArgumentBuilder)wrapStores(literalCommandNode2, Commands.literal("result"), true))).then((ArgumentBuilder)wrapStores(literalCommandNode2, Commands.literal("success"), false)))).then(((LiteralArgumentBuilder)Commands.literal("positioned").then(Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).redirect((CommandNode)literalCommandNode2, commandContext -> ((CommandSourceStack)commandContext.getSource()).withPosition(Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET)))).then(Commands.literal("as").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).fork((CommandNode)literalCommandNode2, commandContext -> {
            final List<CommandSourceStack> list2 = (List<CommandSourceStack>)Lists.newArrayList();
            for (final Entity apx4 : EntityArgument.getOptionalEntities((CommandContext<CommandSourceStack>)commandContext, "targets")) {
                list2.add(((CommandSourceStack)commandContext.getSource()).withPosition(apx4.position()));
            }
            return (Collection)list2;
        }))))).then(((LiteralArgumentBuilder)Commands.literal("rotated").then(Commands.argument("rot", (com.mojang.brigadier.arguments.ArgumentType<Object>)RotationArgument.rotation()).redirect((CommandNode)literalCommandNode2, commandContext -> ((CommandSourceStack)commandContext.getSource()).withRotation(RotationArgument.getRotation((CommandContext<CommandSourceStack>)commandContext, "rot").getRotation((CommandSourceStack)commandContext.getSource()))))).then(Commands.literal("as").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).fork((CommandNode)literalCommandNode2, commandContext -> {
            final List<CommandSourceStack> list2 = (List<CommandSourceStack>)Lists.newArrayList();
            for (final Entity apx4 : EntityArgument.getOptionalEntities((CommandContext<CommandSourceStack>)commandContext, "targets")) {
                list2.add(((CommandSourceStack)commandContext.getSource()).withRotation(apx4.getRotationVector()));
            }
            return (Collection)list2;
        }))))).then(((LiteralArgumentBuilder)Commands.literal("facing").then(Commands.literal("entity").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).then(Commands.argument("anchor", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityAnchorArgument.anchor()).fork((CommandNode)literalCommandNode2, commandContext -> {
            final List<CommandSourceStack> list2 = (List<CommandSourceStack>)Lists.newArrayList();
            final EntityAnchorArgument.Anchor a3 = EntityAnchorArgument.getAnchor((CommandContext<CommandSourceStack>)commandContext, "anchor");
            for (final Entity apx5 : EntityArgument.getOptionalEntities((CommandContext<CommandSourceStack>)commandContext, "targets")) {
                list2.add(((CommandSourceStack)commandContext.getSource()).facing(apx5, a3));
            }
            return (Collection)list2;
        }))))).then(Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).redirect((CommandNode)literalCommandNode2, commandContext -> ((CommandSourceStack)commandContext.getSource()).facing(Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos")))))).then(Commands.literal("align").then(Commands.argument("axes", (com.mojang.brigadier.arguments.ArgumentType<Object>)SwizzleArgument.swizzle()).redirect((CommandNode)literalCommandNode2, commandContext -> ((CommandSourceStack)commandContext.getSource()).withPosition(((CommandSourceStack)commandContext.getSource()).getPosition().align(SwizzleArgument.getSwizzle((CommandContext<CommandSourceStack>)commandContext, "axes"))))))).then(Commands.literal("anchored").then(Commands.argument("anchor", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityAnchorArgument.anchor()).redirect((CommandNode)literalCommandNode2, commandContext -> ((CommandSourceStack)commandContext.getSource()).withAnchor(EntityAnchorArgument.getAnchor((CommandContext<CommandSourceStack>)commandContext, "anchor")))))).then(Commands.literal("in").then(Commands.argument("dimension", (com.mojang.brigadier.arguments.ArgumentType<Object>)DimensionArgument.dimension()).redirect((CommandNode)literalCommandNode2, commandContext -> ((CommandSourceStack)commandContext.getSource()).withLevel(DimensionArgument.getDimension((CommandContext<CommandSourceStack>)commandContext, "dimension"))))));
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(final LiteralCommandNode<CommandSourceStack> literalCommandNode, final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, final boolean boolean3) {
        literalArgumentBuilder.then(Commands.literal("score").then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolders()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()).redirect((CommandNode)literalCommandNode, commandContext -> storeValue((CommandSourceStack)commandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard((CommandContext<CommandSourceStack>)commandContext, "targets"), ObjectiveArgument.getObjective((CommandContext<CommandSourceStack>)commandContext, "objective"), boolean3)))));
        literalArgumentBuilder.then(Commands.literal("bossbar").then(((RequiredArgumentBuilder)Commands.argument("id", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)BossBarCommands.SUGGEST_BOSS_BAR).then(Commands.literal("value").redirect((CommandNode)literalCommandNode, commandContext -> storeValue((CommandSourceStack)commandContext.getSource(), BossBarCommands.getBossBar((CommandContext<CommandSourceStack>)commandContext), true, boolean3)))).then(Commands.literal("max").redirect((CommandNode)literalCommandNode, commandContext -> storeValue((CommandSourceStack)commandContext.getSource(), BossBarCommands.getBossBar((CommandContext<CommandSourceStack>)commandContext), false, boolean3)))));
        for (final DataCommands.DataProvider c5 : DataCommands.TARGET_PROVIDERS) {
            c5.wrap(literalArgumentBuilder, (Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>>)(argumentBuilder -> argumentBuilder.then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("path", (com.mojang.brigadier.arguments.ArgumentType<Object>)NbtPathArgument.nbtPath()).then(Commands.literal("int").then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect((CommandNode)literalCommandNode, commandContext -> storeData((CommandSourceStack)commandContext.getSource(), c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path"), (IntFunction<Tag>)(integer -> IntTag.valueOf((int)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("float").then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect((CommandNode)literalCommandNode, commandContext -> storeData((CommandSourceStack)commandContext.getSource(), c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path"), (IntFunction<Tag>)(integer -> FloatTag.valueOf((float)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("short").then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect((CommandNode)literalCommandNode, commandContext -> storeData((CommandSourceStack)commandContext.getSource(), c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path"), (IntFunction<Tag>)(integer -> ShortTag.valueOf((short)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("long").then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect((CommandNode)literalCommandNode, commandContext -> storeData((CommandSourceStack)commandContext.getSource(), c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path"), (IntFunction<Tag>)(integer -> LongTag.valueOf((long)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("double").then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect((CommandNode)literalCommandNode, commandContext -> storeData((CommandSourceStack)commandContext.getSource(), c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path"), (IntFunction<Tag>)(integer -> DoubleTag.valueOf(integer * DoubleArgumentType.getDouble(commandContext, "scale"))), boolean3))))).then(Commands.literal("byte").then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect((CommandNode)literalCommandNode, commandContext -> storeData((CommandSourceStack)commandContext.getSource(), c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path"), (IntFunction<Tag>)(integer -> ByteTag.valueOf((byte)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3)))))));
        }
        return literalArgumentBuilder;
    }
    
    private static CommandSourceStack storeValue(final CommandSourceStack db, final Collection<String> collection, final Objective ddh, final boolean boolean4) {
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        return db.withCallback((ResultConsumer<CommandSourceStack>)((commandContext, boolean6, integer) -> {
            for (final String string9 : collection) {
                final Score ddj10 = ddk5.getOrCreatePlayerScore(string9, ddh);
                final int integer2 = boolean4 ? integer : (boolean6 ? 1 : 0);
                ddj10.setScore(integer2);
            }
        }), ExecuteCommand.CALLBACK_CHAINER);
    }
    
    private static CommandSourceStack storeValue(final CommandSourceStack db, final CustomBossEvent wc, final boolean boolean3, final boolean boolean4) {
        return db.withCallback((ResultConsumer<CommandSourceStack>)((commandContext, boolean5, integer) -> {
            final int integer2 = boolean4 ? integer : (boolean5 ? 1 : 0);
            if (boolean3) {
                wc.setValue(integer2);
            }
            else {
                wc.setMax(integer2);
            }
        }), ExecuteCommand.CALLBACK_CHAINER);
    }
    
    private static CommandSourceStack storeData(final CommandSourceStack db, final DataAccessor yz, final NbtPathArgument.NbtPath h, final IntFunction<Tag> intFunction, final boolean boolean5) {
        return db.withCallback((ResultConsumer<CommandSourceStack>)((commandContext, boolean6, integer) -> {
            try {
                final CompoundTag md8 = yz.getData();
                final int integer2 = boolean5 ? integer : (boolean6 ? 1 : 0);
                h.set(md8, (Supplier<Tag>)(() -> (Tag)intFunction.apply(integer2)));
                yz.setData(md8);
            }
            catch (CommandSyntaxException ex) {}
        }), ExecuteCommand.CALLBACK_CHAINER);
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(final CommandNode<CommandSourceStack> commandNode, final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, final boolean boolean3) {
        ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)literalArgumentBuilder.then(Commands.literal("block").then(Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then((ArgumentBuilder)addConditional(commandNode, Commands.argument("block", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPredicateArgument.blockPredicate()), boolean3, commandContext -> BlockPredicateArgument.getBlockPredicate(commandContext, "block").test(new BlockInWorld(((CommandSourceStack)commandContext.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(commandContext, "pos"), true))))))).then(Commands.literal("score").then(Commands.argument("target", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targetObjective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()).then(Commands.literal("=").then(Commands.argument("source", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then((ArgumentBuilder)addConditional(commandNode, Commands.argument("sourceObjective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)Integer::equals)))))).then(Commands.literal("<").then(Commands.argument("source", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then((ArgumentBuilder)addConditional(commandNode, Commands.argument("sourceObjective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 < integer2))))))).then(Commands.literal("<=").then(Commands.argument("source", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then((ArgumentBuilder)addConditional(commandNode, Commands.argument("sourceObjective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 <= integer2))))))).then(Commands.literal(">").then(Commands.argument("source", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then((ArgumentBuilder)addConditional(commandNode, Commands.argument("sourceObjective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 > integer2))))))).then(Commands.literal(">=").then(Commands.argument("source", (com.mojang.brigadier.arguments.ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests((SuggestionProvider)ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then((ArgumentBuilder)addConditional(commandNode, Commands.argument("sourceObjective", (com.mojang.brigadier.arguments.ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 >= integer2))))))).then(Commands.literal("matches").then((ArgumentBuilder)addConditional(commandNode, Commands.argument("range", (com.mojang.brigadier.arguments.ArgumentType<Object>)RangeArgument.intRange()), boolean3, commandContext -> checkScore(commandContext, RangeArgument.Ints.getRange(commandContext, "range"))))))))).then(Commands.literal("blocks").then(Commands.argument("start", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(Commands.argument("end", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder)Commands.argument("destination", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then((ArgumentBuilder)addIfBlocksConditional(commandNode, Commands.literal("all"), boolean3, false))).then((ArgumentBuilder)addIfBlocksConditional(commandNode, Commands.literal("masked"), boolean3, true))))))).then(Commands.literal("entity").then(((RequiredArgumentBuilder)Commands.argument("entities", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).fork((CommandNode)commandNode, commandContext -> expect((CommandContext<CommandSourceStack>)commandContext, boolean3, !EntityArgument.getOptionalEntities((CommandContext<CommandSourceStack>)commandContext, "entities").isEmpty()))).executes((Command)createNumericConditionalHandler(boolean3, commandContext -> EntityArgument.getOptionalEntities(commandContext, "entities").size()))))).then(Commands.literal("predicate").then((ArgumentBuilder)addConditional(commandNode, Commands.argument("predicate", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)ExecuteCommand.SUGGEST_PREDICATE), boolean3, commandContext -> checkCustomPredicate((CommandSourceStack)commandContext.getSource(), ResourceLocationArgument.getPredicate(commandContext, "predicate")))));
        for (final DataCommands.DataProvider c5 : DataCommands.SOURCE_PROVIDERS) {
            literalArgumentBuilder.then((ArgumentBuilder)c5.wrap(Commands.literal("data"), (Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>>)(argumentBuilder -> argumentBuilder.then(((RequiredArgumentBuilder)Commands.argument("path", (com.mojang.brigadier.arguments.ArgumentType<Object>)NbtPathArgument.nbtPath()).fork(commandNode, commandContext -> expect((CommandContext<CommandSourceStack>)commandContext, boolean3, checkMatchingData(c5.access((CommandContext<CommandSourceStack>)commandContext), NbtPathArgument.getPath((CommandContext<CommandSourceStack>)commandContext, "path")) > 0))).executes((Command)createNumericConditionalHandler(boolean3, commandContext -> checkMatchingData(c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"))))))));
        }
        return literalArgumentBuilder;
    }
    
    private static Command<CommandSourceStack> createNumericConditionalHandler(final boolean boolean1, final CommandNumericPredicate a) {
        if (boolean1) {
            return (Command<CommandSourceStack>)(commandContext -> {
                final int integer3 = a.test((CommandContext<CommandSourceStack>)commandContext);
                if (integer3 > 0) {
                    ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.execute.conditional.pass_count", new Object[] { integer3 }), false);
                    return integer3;
                }
                throw ExecuteCommand.ERROR_CONDITIONAL_FAILED.create();
            });
        }
        return (Command<CommandSourceStack>)(commandContext -> {
            final int integer3 = a.test((CommandContext<CommandSourceStack>)commandContext);
            if (integer3 == 0) {
                ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
                return 1;
            }
            throw ExecuteCommand.ERROR_CONDITIONAL_FAILED_COUNT.create(integer3);
        });
    }
    
    private static int checkMatchingData(final DataAccessor yz, final NbtPathArgument.NbtPath h) throws CommandSyntaxException {
        return h.countMatching(yz.getData());
    }
    
    private static boolean checkScore(final CommandContext<CommandSourceStack> commandContext, final BiPredicate<Integer, Integer> biPredicate) throws CommandSyntaxException {
        final String string3 = ScoreHolderArgument.getName(commandContext, "target");
        final Objective ddh4 = ObjectiveArgument.getObjective(commandContext, "targetObjective");
        final String string4 = ScoreHolderArgument.getName(commandContext, "source");
        final Objective ddh5 = ObjectiveArgument.getObjective(commandContext, "sourceObjective");
        final Scoreboard ddk7 = ((CommandSourceStack)commandContext.getSource()).getServer().getScoreboard();
        if (!ddk7.hasPlayerScore(string3, ddh4) || !ddk7.hasPlayerScore(string4, ddh5)) {
            return false;
        }
        final Score ddj8 = ddk7.getOrCreatePlayerScore(string3, ddh4);
        final Score ddj9 = ddk7.getOrCreatePlayerScore(string4, ddh5);
        return biPredicate.test(ddj8.getScore(), ddj9.getScore());
    }
    
    private static boolean checkScore(final CommandContext<CommandSourceStack> commandContext, final MinMaxBounds.Ints d) throws CommandSyntaxException {
        final String string3 = ScoreHolderArgument.getName(commandContext, "target");
        final Objective ddh4 = ObjectiveArgument.getObjective(commandContext, "targetObjective");
        final Scoreboard ddk5 = ((CommandSourceStack)commandContext.getSource()).getServer().getScoreboard();
        return ddk5.hasPlayerScore(string3, ddh4) && d.matches(ddk5.getOrCreatePlayerScore(string3, ddh4).getScore());
    }
    
    private static boolean checkCustomPredicate(final CommandSourceStack db, final LootItemCondition dbl) {
        final ServerLevel aag3 = db.getLevel();
        final LootContext.Builder a4 = new LootContext.Builder(aag3).<Vec3>withParameter(LootContextParams.ORIGIN, db.getPosition()).<Entity>withOptionalParameter(LootContextParams.THIS_ENTITY, db.getEntity());
        return dbl.test(a4.create(LootContextParamSets.COMMAND));
    }
    
    private static Collection<CommandSourceStack> expect(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2, final boolean boolean3) {
        if (boolean3 == boolean2) {
            return (Collection<CommandSourceStack>)Collections.singleton(commandContext.getSource());
        }
        return (Collection<CommandSourceStack>)Collections.emptyList();
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> addConditional(final CommandNode<CommandSourceStack> commandNode, final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final boolean boolean3, final CommandPredicate b) {
        return argumentBuilder.fork((CommandNode)commandNode, commandContext -> expect((CommandContext<CommandSourceStack>)commandContext, boolean3, b.test((CommandContext<CommandSourceStack>)commandContext))).executes(commandContext -> {
            if (boolean3 == b.test((CommandContext<CommandSourceStack>)commandContext)) {
                ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
                return 1;
            }
            throw ExecuteCommand.ERROR_CONDITIONAL_FAILED.create();
        });
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(final CommandNode<CommandSourceStack> commandNode, final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final boolean boolean3, final boolean boolean4) {
        return argumentBuilder.fork((CommandNode)commandNode, commandContext -> expect((CommandContext<CommandSourceStack>)commandContext, boolean3, checkRegions((CommandContext<CommandSourceStack>)commandContext, boolean4).isPresent())).executes(boolean3 ? (commandContext -> checkIfRegions((CommandContext<CommandSourceStack>)commandContext, boolean4)) : (commandContext -> checkUnlessRegions((CommandContext<CommandSourceStack>)commandContext, boolean4)));
    }
    
    private static int checkIfRegions(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2) throws CommandSyntaxException {
        final OptionalInt optionalInt3 = checkRegions(commandContext, boolean2);
        if (optionalInt3.isPresent()) {
            ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.execute.conditional.pass_count", new Object[] { optionalInt3.getAsInt() }), false);
            return optionalInt3.getAsInt();
        }
        throw ExecuteCommand.ERROR_CONDITIONAL_FAILED.create();
    }
    
    private static int checkUnlessRegions(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2) throws CommandSyntaxException {
        final OptionalInt optionalInt3 = checkRegions(commandContext, boolean2);
        if (optionalInt3.isPresent()) {
            throw ExecuteCommand.ERROR_CONDITIONAL_FAILED_COUNT.create(optionalInt3.getAsInt());
        }
        ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
        return 1;
    }
    
    private static OptionalInt checkRegions(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2) throws CommandSyntaxException {
        return checkRegions(((CommandSourceStack)commandContext.getSource()).getLevel(), BlockPosArgument.getLoadedBlockPos(commandContext, "start"), BlockPosArgument.getLoadedBlockPos(commandContext, "end"), BlockPosArgument.getLoadedBlockPos(commandContext, "destination"), boolean2);
    }
    
    private static OptionalInt checkRegions(final ServerLevel aag, final BlockPos fx2, final BlockPos fx3, final BlockPos fx4, final boolean boolean5) throws CommandSyntaxException {
        final BoundingBox cqx6 = new BoundingBox(fx2, fx3);
        final BoundingBox cqx7 = new BoundingBox(fx4, fx4.offset(cqx6.getLength()));
        final BlockPos fx5 = new BlockPos(cqx7.x0 - cqx6.x0, cqx7.y0 - cqx6.y0, cqx7.z0 - cqx6.z0);
        final int integer9 = cqx6.getXSpan() * cqx6.getYSpan() * cqx6.getZSpan();
        if (integer9 > 32768) {
            throw ExecuteCommand.ERROR_AREA_TOO_LARGE.create(32768, integer9);
        }
        int integer10 = 0;
        for (int integer11 = cqx6.z0; integer11 <= cqx6.z1; ++integer11) {
            for (int integer12 = cqx6.y0; integer12 <= cqx6.y1; ++integer12) {
                for (int integer13 = cqx6.x0; integer13 <= cqx6.x1; ++integer13) {
                    final BlockPos fx6 = new BlockPos(integer13, integer12, integer11);
                    final BlockPos fx7 = fx6.offset(fx5);
                    final BlockState cee16 = aag.getBlockState(fx6);
                    if (!boolean5 || !cee16.is(Blocks.AIR)) {
                        if (cee16 != aag.getBlockState(fx7)) {
                            return OptionalInt.empty();
                        }
                        final BlockEntity ccg17 = aag.getBlockEntity(fx6);
                        final BlockEntity ccg18 = aag.getBlockEntity(fx7);
                        if (ccg17 != null) {
                            if (ccg18 == null) {
                                return OptionalInt.empty();
                            }
                            final CompoundTag md19 = ccg17.save(new CompoundTag());
                            md19.remove("x");
                            md19.remove("y");
                            md19.remove("z");
                            final CompoundTag md20 = ccg18.save(new CompoundTag());
                            md20.remove("x");
                            md20.remove("y");
                            md20.remove("z");
                            if (!md19.equals(md20)) {
                                return OptionalInt.empty();
                            }
                        }
                        ++integer10;
                    }
                }
            }
        }
        return OptionalInt.of(integer10);
    }
    
    static {
        ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.execute.blocks.toobig", new Object[] { object1, object2 }));
        ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.execute.conditional.fail"));
        ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.execute.conditional.fail_count", new Object[] { object }));
        CALLBACK_CHAINER = ((resultConsumer1, resultConsumer2) -> (commandContext, boolean4, integer) -> {
            resultConsumer1.onCommandComplete(commandContext, boolean4, integer);
            resultConsumer2.onCommandComplete(commandContext, boolean4, integer);
        });
        SUGGEST_PREDICATE = ((commandContext, suggestionsBuilder) -> {
            final PredicateManager cyx3 = ((CommandSourceStack)commandContext.getSource()).getServer().getPredicateManager();
            return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)cyx3.getKeys(), suggestionsBuilder);
        });
    }
    
    @FunctionalInterface
    interface CommandNumericPredicate {
        int test(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }
    
    @FunctionalInterface
    interface CommandPredicate {
        boolean test(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }
}
