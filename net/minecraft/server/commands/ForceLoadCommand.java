package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import it.unimi.dsi.fastutil.longs.LongSet;
import com.google.common.base.Joiner;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ColumnPos;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

public class ForceLoadCommand {
    private static final Dynamic2CommandExceptionType ERROR_TOO_MANY_CHUNKS;
    private static final Dynamic2CommandExceptionType ERROR_NOT_TICKING;
    private static final SimpleCommandExceptionType ERROR_ALL_ADDED;
    private static final SimpleCommandExceptionType ERROR_NONE_REMOVED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("forceload").requires(db -> db.hasPermission(2))).then(Commands.literal("add").then(((RequiredArgumentBuilder)Commands.argument("from", (com.mojang.brigadier.arguments.ArgumentType<Object>)ColumnPosArgument.columnPos()).executes(commandContext -> changeForceLoad((CommandSourceStack)commandContext.getSource(), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "from"), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "from"), true))).then(Commands.argument("to", (com.mojang.brigadier.arguments.ArgumentType<Object>)ColumnPosArgument.columnPos()).executes(commandContext -> changeForceLoad((CommandSourceStack)commandContext.getSource(), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "from"), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "to"), true)))))).then(((LiteralArgumentBuilder)Commands.literal("remove").then(((RequiredArgumentBuilder)Commands.argument("from", (com.mojang.brigadier.arguments.ArgumentType<Object>)ColumnPosArgument.columnPos()).executes(commandContext -> changeForceLoad((CommandSourceStack)commandContext.getSource(), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "from"), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "from"), false))).then(Commands.argument("to", (com.mojang.brigadier.arguments.ArgumentType<Object>)ColumnPosArgument.columnPos()).executes(commandContext -> changeForceLoad((CommandSourceStack)commandContext.getSource(), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "from"), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "to"), false))))).then(Commands.literal("all").executes(commandContext -> removeAll((CommandSourceStack)commandContext.getSource()))))).then(((LiteralArgumentBuilder)Commands.literal("query").executes(commandContext -> listForceLoad((CommandSourceStack)commandContext.getSource()))).then(Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)ColumnPosArgument.columnPos()).executes(commandContext -> queryForceLoad((CommandSourceStack)commandContext.getSource(), ColumnPosArgument.getColumnPos((CommandContext<CommandSourceStack>)commandContext, "pos"))))));
    }
    
    private static int queryForceLoad(final CommandSourceStack db, final ColumnPos zw) throws CommandSyntaxException {
        final ChunkPos bra3 = new ChunkPos(zw.x >> 4, zw.z >> 4);
        final ServerLevel aag4 = db.getLevel();
        final ResourceKey<Level> vj5 = aag4.dimension();
        final boolean boolean6 = aag4.getForcedChunks().contains(bra3.toLong());
        if (boolean6) {
            db.sendSuccess(new TranslatableComponent("commands.forceload.query.success", new Object[] { bra3, vj5.location() }), false);
            return 1;
        }
        throw ForceLoadCommand.ERROR_NOT_TICKING.create(bra3, vj5.location());
    }
    
    private static int listForceLoad(final CommandSourceStack db) {
        final ServerLevel aag2 = db.getLevel();
        final ResourceKey<Level> vj3 = aag2.dimension();
        final LongSet longSet4 = aag2.getForcedChunks();
        final int integer5 = longSet4.size();
        if (integer5 > 0) {
            final String string6 = Joiner.on(", ").join(longSet4.stream().sorted().map(ChunkPos::new).map(ChunkPos::toString).iterator());
            if (integer5 == 1) {
                db.sendSuccess(new TranslatableComponent("commands.forceload.list.single", new Object[] { vj3.location(), string6 }), false);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.forceload.list.multiple", new Object[] { integer5, vj3.location(), string6 }), false);
            }
        }
        else {
            db.sendFailure(new TranslatableComponent("commands.forceload.added.none", new Object[] { vj3.location() }));
        }
        return integer5;
    }
    
    private static int removeAll(final CommandSourceStack db) {
        final ServerLevel aag2 = db.getLevel();
        final ResourceKey<Level> vj3 = aag2.dimension();
        final LongSet longSet4 = aag2.getForcedChunks();
        longSet4.forEach(long2 -> aag2.setChunkForced(ChunkPos.getX(long2), ChunkPos.getZ(long2), false));
        db.sendSuccess(new TranslatableComponent("commands.forceload.removed.all", new Object[] { vj3.location() }), true);
        return 0;
    }
    
    private static int changeForceLoad(final CommandSourceStack db, final ColumnPos zw2, final ColumnPos zw3, final boolean boolean4) throws CommandSyntaxException {
        final int integer5 = Math.min(zw2.x, zw3.x);
        final int integer6 = Math.min(zw2.z, zw3.z);
        final int integer7 = Math.max(zw2.x, zw3.x);
        final int integer8 = Math.max(zw2.z, zw3.z);
        if (integer5 < -30000000 || integer6 < -30000000 || integer7 >= 30000000 || integer8 >= 30000000) {
            throw BlockPosArgument.ERROR_OUT_OF_WORLD.create();
        }
        final int integer9 = integer5 >> 4;
        final int integer10 = integer6 >> 4;
        final int integer11 = integer7 >> 4;
        final int integer12 = integer8 >> 4;
        final long long13 = (integer11 - integer9 + 1L) * (integer12 - integer10 + 1L);
        if (long13 > 256L) {
            throw ForceLoadCommand.ERROR_TOO_MANY_CHUNKS.create(256, long13);
        }
        final ServerLevel aag15 = db.getLevel();
        final ResourceKey<Level> vj16 = aag15.dimension();
        ChunkPos bra17 = null;
        int integer13 = 0;
        for (int integer14 = integer9; integer14 <= integer11; ++integer14) {
            for (int integer15 = integer10; integer15 <= integer12; ++integer15) {
                final boolean boolean5 = aag15.setChunkForced(integer14, integer15, boolean4);
                if (boolean5) {
                    ++integer13;
                    if (bra17 == null) {
                        bra17 = new ChunkPos(integer14, integer15);
                    }
                }
            }
        }
        if (integer13 == 0) {
            throw (boolean4 ? ForceLoadCommand.ERROR_ALL_ADDED : ForceLoadCommand.ERROR_NONE_REMOVED).create();
        }
        if (integer13 == 1) {
            db.sendSuccess(new TranslatableComponent(new StringBuilder().append("commands.forceload.").append(boolean4 ? "added" : "removed").append(".single").toString(), new Object[] { bra17, vj16.location() }), true);
        }
        else {
            final ChunkPos bra18 = new ChunkPos(integer9, integer10);
            final ChunkPos bra19 = new ChunkPos(integer11, integer12);
            db.sendSuccess(new TranslatableComponent(new StringBuilder().append("commands.forceload.").append(boolean4 ? "added" : "removed").append(".multiple").toString(), new Object[] { integer13, vj16.location(), bra18, bra19 }), true);
        }
        return integer13;
    }
    
    static {
        ERROR_TOO_MANY_CHUNKS = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.forceload.toobig", new Object[] { object1, object2 }));
        ERROR_NOT_TICKING = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.forceload.query.failure", new Object[] { object1, object2 }));
        ERROR_ALL_ADDED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.forceload.added.failure"));
        ERROR_NONE_REMOVED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.forceload.removed.failure"));
    }
}
