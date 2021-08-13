package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.world.phys.Vec2;
import net.minecraft.util.Mth;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Locale;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class WorldBorderCommand {
    private static final SimpleCommandExceptionType ERROR_SAME_CENTER;
    private static final SimpleCommandExceptionType ERROR_SAME_SIZE;
    private static final SimpleCommandExceptionType ERROR_TOO_SMALL;
    private static final SimpleCommandExceptionType ERROR_TOO_BIG;
    private static final SimpleCommandExceptionType ERROR_SAME_WARNING_TIME;
    private static final SimpleCommandExceptionType ERROR_SAME_WARNING_DISTANCE;
    private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_BUFFER;
    private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_AMOUNT;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("worldborder").requires(db -> db.hasPermission(2))).then(Commands.literal("add").then(((RequiredArgumentBuilder)Commands.argument("distance", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(-6.0E7f, 6.0E7f)).executes(commandContext -> setSize((CommandSourceStack)commandContext.getSource(), ((CommandSourceStack)commandContext.getSource()).getLevel().getWorldBorder().getSize() + FloatArgumentType.getFloat(commandContext, "distance"), 0L))).then(Commands.argument("time", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setSize((CommandSourceStack)commandContext.getSource(), ((CommandSourceStack)commandContext.getSource()).getLevel().getWorldBorder().getSize() + FloatArgumentType.getFloat(commandContext, "distance"), ((CommandSourceStack)commandContext.getSource()).getLevel().getWorldBorder().getLerpRemainingTime() + IntegerArgumentType.getInteger(commandContext, "time") * 1000L)))))).then(Commands.literal("set").then(((RequiredArgumentBuilder)Commands.argument("distance", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(-6.0E7f, 6.0E7f)).executes(commandContext -> setSize((CommandSourceStack)commandContext.getSource(), FloatArgumentType.getFloat(commandContext, "distance"), 0L))).then(Commands.argument("time", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setSize((CommandSourceStack)commandContext.getSource(), FloatArgumentType.getFloat(commandContext, "distance"), IntegerArgumentType.getInteger(commandContext, "time") * 1000L)))))).then(Commands.literal("center").then(Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec2Argument.vec2()).executes(commandContext -> setCenter((CommandSourceStack)commandContext.getSource(), Vec2Argument.getVec2((CommandContext<CommandSourceStack>)commandContext, "pos")))))).then(((LiteralArgumentBuilder)Commands.literal("damage").then(Commands.literal("amount").then(Commands.argument("damagePerBlock", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(0.0f)).executes(commandContext -> setDamageAmount((CommandSourceStack)commandContext.getSource(), FloatArgumentType.getFloat(commandContext, "damagePerBlock")))))).then(Commands.literal("buffer").then(Commands.argument("distance", (com.mojang.brigadier.arguments.ArgumentType<Object>)FloatArgumentType.floatArg(0.0f)).executes(commandContext -> setDamageBuffer((CommandSourceStack)commandContext.getSource(), FloatArgumentType.getFloat(commandContext, "distance"))))))).then(Commands.literal("get").executes(commandContext -> getSize((CommandSourceStack)commandContext.getSource())))).then(((LiteralArgumentBuilder)Commands.literal("warning").then(Commands.literal("distance").then(Commands.argument("distance", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setWarningDistance((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "distance")))))).then(Commands.literal("time").then(Commands.argument("time", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setWarningTime((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "time")))))));
    }
    
    private static int setDamageBuffer(final CommandSourceStack db, final float float2) throws CommandSyntaxException {
        final WorldBorder cfr3 = db.getLevel().getWorldBorder();
        if (cfr3.getDamageSafeZone() == float2) {
            throw WorldBorderCommand.ERROR_SAME_DAMAGE_BUFFER.create();
        }
        cfr3.setDamageSafeZone(float2);
        db.sendSuccess(new TranslatableComponent("commands.worldborder.damage.buffer.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { float2 }) }), true);
        return (int)float2;
    }
    
    private static int setDamageAmount(final CommandSourceStack db, final float float2) throws CommandSyntaxException {
        final WorldBorder cfr3 = db.getLevel().getWorldBorder();
        if (cfr3.getDamagePerBlock() == float2) {
            throw WorldBorderCommand.ERROR_SAME_DAMAGE_AMOUNT.create();
        }
        cfr3.setDamagePerBlock(float2);
        db.sendSuccess(new TranslatableComponent("commands.worldborder.damage.amount.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { float2 }) }), true);
        return (int)float2;
    }
    
    private static int setWarningTime(final CommandSourceStack db, final int integer) throws CommandSyntaxException {
        final WorldBorder cfr3 = db.getLevel().getWorldBorder();
        if (cfr3.getWarningTime() == integer) {
            throw WorldBorderCommand.ERROR_SAME_WARNING_TIME.create();
        }
        cfr3.setWarningTime(integer);
        db.sendSuccess(new TranslatableComponent("commands.worldborder.warning.time.success", new Object[] { integer }), true);
        return integer;
    }
    
    private static int setWarningDistance(final CommandSourceStack db, final int integer) throws CommandSyntaxException {
        final WorldBorder cfr3 = db.getLevel().getWorldBorder();
        if (cfr3.getWarningBlocks() == integer) {
            throw WorldBorderCommand.ERROR_SAME_WARNING_DISTANCE.create();
        }
        cfr3.setWarningBlocks(integer);
        db.sendSuccess(new TranslatableComponent("commands.worldborder.warning.distance.success", new Object[] { integer }), true);
        return integer;
    }
    
    private static int getSize(final CommandSourceStack db) {
        final double double2 = db.getLevel().getWorldBorder().getSize();
        db.sendSuccess(new TranslatableComponent("commands.worldborder.get", new Object[] { String.format(Locale.ROOT, "%.0f", new Object[] { double2 }) }), false);
        return Mth.floor(double2 + 0.5);
    }
    
    private static int setCenter(final CommandSourceStack db, final Vec2 dcj) throws CommandSyntaxException {
        final WorldBorder cfr3 = db.getLevel().getWorldBorder();
        if (cfr3.getCenterX() == dcj.x && cfr3.getCenterZ() == dcj.y) {
            throw WorldBorderCommand.ERROR_SAME_CENTER.create();
        }
        cfr3.setCenter(dcj.x, dcj.y);
        db.sendSuccess(new TranslatableComponent("commands.worldborder.center.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { dcj.x }), String.format("%.2f", new Object[] { dcj.y }) }), true);
        return 0;
    }
    
    private static int setSize(final CommandSourceStack db, final double double2, final long long3) throws CommandSyntaxException {
        final WorldBorder cfr6 = db.getLevel().getWorldBorder();
        final double double3 = cfr6.getSize();
        if (double3 == double2) {
            throw WorldBorderCommand.ERROR_SAME_SIZE.create();
        }
        if (double2 < 1.0) {
            throw WorldBorderCommand.ERROR_TOO_SMALL.create();
        }
        if (double2 > 6.0E7) {
            throw WorldBorderCommand.ERROR_TOO_BIG.create();
        }
        if (long3 > 0L) {
            cfr6.lerpSizeBetween(double3, double2, long3);
            if (double2 > double3) {
                db.sendSuccess(new TranslatableComponent("commands.worldborder.set.grow", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { double2 }), Long.toString(long3 / 1000L) }), true);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.worldborder.set.shrink", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { double2 }), Long.toString(long3 / 1000L) }), true);
            }
        }
        else {
            cfr6.setSize(double2);
            db.sendSuccess(new TranslatableComponent("commands.worldborder.set.immediate", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { double2 }) }), true);
        }
        return (int)(double2 - double3);
    }
    
    static {
        ERROR_SAME_CENTER = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.center.failed"));
        ERROR_SAME_SIZE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.set.failed.nochange"));
        ERROR_TOO_SMALL = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.set.failed.small."));
        ERROR_TOO_BIG = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.set.failed.big."));
        ERROR_SAME_WARNING_TIME = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.warning.time.failed"));
        ERROR_SAME_WARNING_DISTANCE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.warning.distance.failed"));
        ERROR_SAME_DAMAGE_BUFFER = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.damage.buffer.failed"));
        ERROR_SAME_DAMAGE_AMOUNT = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.worldborder.damage.amount.failed"));
    }
}
