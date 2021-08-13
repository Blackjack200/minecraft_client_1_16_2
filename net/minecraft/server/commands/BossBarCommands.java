package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collections;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.BossEvent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.arguments.ComponentArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class BossBarCommands {
    private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS;
    private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST;
    private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE;
    private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN;
    private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE;
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("bossbar").requires(db -> db.hasPermission(2))).then(Commands.literal("add").then(Commands.argument("id", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)ComponentArgument.textComponent()).executes(commandContext -> createBar((CommandSourceStack)commandContext.getSource(), ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "id"), ComponentArgument.getComponent((CommandContext<CommandSourceStack>)commandContext, "name"))))))).then(Commands.literal("remove").then(Commands.argument("id", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)BossBarCommands.SUGGEST_BOSS_BAR).executes(commandContext -> removeBar((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext)))))).then(Commands.literal("list").executes(commandContext -> listBars((CommandSourceStack)commandContext.getSource())))).then(Commands.literal("set").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)BossBarCommands.SUGGEST_BOSS_BAR).then(Commands.literal("name").then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)ComponentArgument.textComponent()).executes(commandContext -> setName((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), ComponentArgument.getComponent((CommandContext<CommandSourceStack>)commandContext, "name")))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color").then(Commands.literal("pink").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.PINK)))).then(Commands.literal("blue").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.BLUE)))).then(Commands.literal("red").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.RED)))).then(Commands.literal("green").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.GREEN)))).then(Commands.literal("yellow").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.YELLOW)))).then(Commands.literal("purple").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.PURPLE)))).then(Commands.literal("white").executes(commandContext -> setColor((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarColor.WHITE))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("style").then(Commands.literal("progress").executes(commandContext -> setStyle((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarOverlay.PROGRESS)))).then(Commands.literal("notched_6").executes(commandContext -> setStyle((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarOverlay.NOTCHED_6)))).then(Commands.literal("notched_10").executes(commandContext -> setStyle((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarOverlay.NOTCHED_10)))).then(Commands.literal("notched_12").executes(commandContext -> setStyle((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarOverlay.NOTCHED_12)))).then(Commands.literal("notched_20").executes(commandContext -> setStyle((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BossEvent.BossBarOverlay.NOTCHED_20))))).then(Commands.literal("value").then(Commands.argument("value", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> setValue((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), IntegerArgumentType.getInteger(commandContext, "value")))))).then(Commands.literal("max").then(Commands.argument("max", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(1)).executes(commandContext -> setMax((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), IntegerArgumentType.getInteger(commandContext, "max")))))).then(Commands.literal("visible").then(Commands.argument("visible", (com.mojang.brigadier.arguments.ArgumentType<Object>)BoolArgumentType.bool()).executes(commandContext -> setVisible((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), BoolArgumentType.getBool(commandContext, "visible")))))).then(((LiteralArgumentBuilder)Commands.literal("players").executes(commandContext -> setPlayers((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), (Collection<ServerPlayer>)Collections.emptyList()))).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> setPlayers((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext), EntityArgument.getOptionalPlayers((CommandContext<CommandSourceStack>)commandContext, "targets")))))))).then(Commands.literal("get").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("id", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)BossBarCommands.SUGGEST_BOSS_BAR).then(Commands.literal("value").executes(commandContext -> getValue((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext))))).then(Commands.literal("max").executes(commandContext -> getMax((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext))))).then(Commands.literal("visible").executes(commandContext -> getVisible((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext))))).then(Commands.literal("players").executes(commandContext -> getPlayers((CommandSourceStack)commandContext.getSource(), getBossBar((CommandContext<CommandSourceStack>)commandContext)))))));
    }
    
    private static int getValue(final CommandSourceStack db, final CustomBossEvent wc) {
        db.sendSuccess(new TranslatableComponent("commands.bossbar.get.value", new Object[] { wc.getDisplayName(), wc.getValue() }), true);
        return wc.getValue();
    }
    
    private static int getMax(final CommandSourceStack db, final CustomBossEvent wc) {
        db.sendSuccess(new TranslatableComponent("commands.bossbar.get.max", new Object[] { wc.getDisplayName(), wc.getMax() }), true);
        return wc.getMax();
    }
    
    private static int getVisible(final CommandSourceStack db, final CustomBossEvent wc) {
        if (wc.isVisible()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.get.visible.visible", new Object[] { wc.getDisplayName() }), true);
            return 1;
        }
        db.sendSuccess(new TranslatableComponent("commands.bossbar.get.visible.hidden", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int getPlayers(final CommandSourceStack db, final CustomBossEvent wc) {
        if (wc.getPlayers().isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.get.players.none", new Object[] { wc.getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.get.players.some", new Object[] { wc.getDisplayName(), wc.getPlayers().size(), ComponentUtils.<ServerPlayer>formatList(wc.getPlayers(), (java.util.function.Function<ServerPlayer, Component>)Player::getDisplayName) }), true);
        }
        return wc.getPlayers().size();
    }
    
    private static int setVisible(final CommandSourceStack db, final CustomBossEvent wc, final boolean boolean3) throws CommandSyntaxException {
        if (wc.isVisible() != boolean3) {
            wc.setVisible(boolean3);
            if (boolean3) {
                db.sendSuccess(new TranslatableComponent("commands.bossbar.set.visible.success.visible", new Object[] { wc.getDisplayName() }), true);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.bossbar.set.visible.success.hidden", new Object[] { wc.getDisplayName() }), true);
            }
            return 0;
        }
        if (boolean3) {
            throw BossBarCommands.ERROR_ALREADY_VISIBLE.create();
        }
        throw BossBarCommands.ERROR_ALREADY_HIDDEN.create();
    }
    
    private static int setValue(final CommandSourceStack db, final CustomBossEvent wc, final int integer) throws CommandSyntaxException {
        if (wc.getValue() == integer) {
            throw BossBarCommands.ERROR_NO_VALUE_CHANGE.create();
        }
        wc.setValue(integer);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.value.success", new Object[] { wc.getDisplayName(), integer }), true);
        return integer;
    }
    
    private static int setMax(final CommandSourceStack db, final CustomBossEvent wc, final int integer) throws CommandSyntaxException {
        if (wc.getMax() == integer) {
            throw BossBarCommands.ERROR_NO_MAX_CHANGE.create();
        }
        wc.setMax(integer);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.max.success", new Object[] { wc.getDisplayName(), integer }), true);
        return integer;
    }
    
    private static int setColor(final CommandSourceStack db, final CustomBossEvent wc, final BossEvent.BossBarColor a) throws CommandSyntaxException {
        if (wc.getColor().equals(a)) {
            throw BossBarCommands.ERROR_NO_COLOR_CHANGE.create();
        }
        wc.setColor(a);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.color.success", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int setStyle(final CommandSourceStack db, final CustomBossEvent wc, final BossEvent.BossBarOverlay b) throws CommandSyntaxException {
        if (wc.getOverlay().equals(b)) {
            throw BossBarCommands.ERROR_NO_STYLE_CHANGE.create();
        }
        wc.setOverlay(b);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.style.success", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int setName(final CommandSourceStack db, final CustomBossEvent wc, final Component nr) throws CommandSyntaxException {
        final Component nr2 = ComponentUtils.updateForEntity(db, nr, null, 0);
        if (wc.getName().equals(nr2)) {
            throw BossBarCommands.ERROR_NO_NAME_CHANGE.create();
        }
        wc.setName(nr2);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.name.success", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int setPlayers(final CommandSourceStack db, final CustomBossEvent wc, final Collection<ServerPlayer> collection) throws CommandSyntaxException {
        final boolean boolean4 = wc.setPlayers(collection);
        if (!boolean4) {
            throw BossBarCommands.ERROR_NO_PLAYER_CHANGE.create();
        }
        if (wc.getPlayers().isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.set.players.success.none", new Object[] { wc.getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.set.players.success.some", new Object[] { wc.getDisplayName(), collection.size(), ComponentUtils.<ServerPlayer>formatList(collection, (java.util.function.Function<ServerPlayer, Component>)Player::getDisplayName) }), true);
        }
        return wc.getPlayers().size();
    }
    
    private static int listBars(final CommandSourceStack db) {
        final Collection<CustomBossEvent> collection2 = db.getServer().getCustomBossEvents().getEvents();
        if (collection2.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.list.bars.none"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.list.bars.some", new Object[] { collection2.size(), ComponentUtils.<CustomBossEvent>formatList(collection2, (java.util.function.Function<CustomBossEvent, Component>)CustomBossEvent::getDisplayName) }), false);
        }
        return collection2.size();
    }
    
    private static int createBar(final CommandSourceStack db, final ResourceLocation vk, final Component nr) throws CommandSyntaxException {
        final CustomBossEvents wd4 = db.getServer().getCustomBossEvents();
        if (wd4.get(vk) != null) {
            throw BossBarCommands.ERROR_ALREADY_EXISTS.create(vk.toString());
        }
        final CustomBossEvent wc5 = wd4.create(vk, ComponentUtils.updateForEntity(db, nr, null, 0));
        db.sendSuccess(new TranslatableComponent("commands.bossbar.create.success", new Object[] { wc5.getDisplayName() }), true);
        return wd4.getEvents().size();
    }
    
    private static int removeBar(final CommandSourceStack db, final CustomBossEvent wc) {
        final CustomBossEvents wd3 = db.getServer().getCustomBossEvents();
        wc.removeAllPlayers();
        wd3.remove(wc);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.remove.success", new Object[] { wc.getDisplayName() }), true);
        return wd3.getEvents().size();
    }
    
    public static CustomBossEvent getBossBar(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        final ResourceLocation vk2 = ResourceLocationArgument.getId(commandContext, "id");
        final CustomBossEvent wc3 = ((CommandSourceStack)commandContext.getSource()).getServer().getCustomBossEvents().get(vk2);
        if (wc3 == null) {
            throw BossBarCommands.ERROR_DOESNT_EXIST.create(vk2.toString());
        }
        return wc3;
    }
    
    static {
        ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.bossbar.create.failed", new Object[] { object }));
        ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.bossbar.unknown", new Object[] { object }));
        ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.players.unchanged"));
        ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.name.unchanged"));
        ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.color.unchanged"));
        ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.style.unchanged"));
        ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.value.unchanged"));
        ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.max.unchanged"));
        ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.visibility.unchanged.hidden"));
        ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.bossbar.set.visibility.unchanged.visible"));
        SUGGEST_BOSS_BAR = ((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)((CommandSourceStack)commandContext.getSource()).getServer().getCustomBossEvents().getIds(), suggestionsBuilder));
    }
}
