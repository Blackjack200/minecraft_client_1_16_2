package net.minecraft.commands;

import org.apache.logging.log4j.LogManager;
import java.util.Collection;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import com.mojang.brigadier.context.CommandContext;
import java.util.stream.Collectors;
import java.util.Set;
import net.minecraft.commands.synchronization.ArgumentTypes;
import javax.annotation.Nullable;
import com.mojang.brigadier.ParseResults;
import java.util.function.Predicate;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import java.util.Iterator;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Map;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.Util;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.ComponentUtils;
import com.mojang.brigadier.StringReader;
import net.minecraft.server.commands.PublishCommand;
import net.minecraft.server.commands.WhitelistCommand;
import net.minecraft.server.commands.StopCommand;
import net.minecraft.server.commands.SetPlayerIdleTimeoutCommand;
import net.minecraft.server.commands.SaveOnCommand;
import net.minecraft.server.commands.SaveOffCommand;
import net.minecraft.server.commands.SaveAllCommand;
import net.minecraft.server.commands.PardonIpCommand;
import net.minecraft.server.commands.PardonCommand;
import net.minecraft.server.commands.OpCommand;
import net.minecraft.server.commands.DeOpCommands;
import net.minecraft.server.commands.BanPlayerCommands;
import net.minecraft.server.commands.BanListCommands;
import net.minecraft.server.commands.BanIpCommands;
import net.minecraft.gametest.framework.TestCommand;
import net.minecraft.SharedConstants;
import net.minecraft.server.commands.WorldBorderCommand;
import net.minecraft.server.commands.WeatherCommand;
import net.minecraft.server.commands.TriggerCommand;
import net.minecraft.server.commands.TitleCommand;
import net.minecraft.server.commands.TimeCommand;
import net.minecraft.server.commands.TellRawCommand;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.commands.TeamMsgCommand;
import net.minecraft.server.commands.TeamCommand;
import net.minecraft.server.commands.TagCommand;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.commands.StopSoundCommand;
import net.minecraft.server.commands.SpreadPlayersCommand;
import net.minecraft.server.commands.SpectateCommand;
import net.minecraft.server.commands.SetWorldSpawnCommand;
import net.minecraft.server.commands.SetSpawnCommand;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.commands.SeedCommand;
import net.minecraft.server.commands.ScoreboardCommand;
import net.minecraft.server.commands.ScheduleCommand;
import net.minecraft.server.commands.SayCommand;
import net.minecraft.server.commands.ReplaceItemCommand;
import net.minecraft.server.commands.RecipeCommand;
import net.minecraft.server.commands.ReloadCommand;
import net.minecraft.server.commands.PlaySoundCommand;
import net.minecraft.server.commands.ParticleCommand;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.commands.LootCommand;
import net.minecraft.server.commands.LocateBiomeCommand;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.commands.ListPlayersCommand;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.server.commands.KickCommand;
import net.minecraft.server.commands.HelpCommand;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.server.commands.GameRuleCommand;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.server.commands.FunctionCommand;
import net.minecraft.server.commands.ForceLoadCommand;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.server.commands.ExperienceCommand;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.server.commands.EmoteCommands;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.server.commands.DifficultyCommand;
import net.minecraft.server.commands.DefaultGameModeCommands;
import net.minecraft.server.commands.DebugCommand;
import net.minecraft.server.commands.DataPackCommand;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.server.commands.CloneCommands;
import net.minecraft.server.commands.ClearInventoryCommands;
import net.minecraft.server.commands.BossBarCommands;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.commands.AttributeCommand;
import net.minecraft.server.commands.AdvancementCommands;
import com.mojang.brigadier.CommandDispatcher;
import org.apache.logging.log4j.Logger;

public class Commands {
    private static final Logger LOGGER;
    private final CommandDispatcher<CommandSourceStack> dispatcher;
    
    public Commands(final CommandSelection a) {
        AdvancementCommands.register(this.dispatcher = (CommandDispatcher<CommandSourceStack>)new CommandDispatcher());
        AttributeCommand.register(this.dispatcher);
        ExecuteCommand.register(this.dispatcher);
        BossBarCommands.register(this.dispatcher);
        ClearInventoryCommands.register(this.dispatcher);
        CloneCommands.register(this.dispatcher);
        DataCommands.register(this.dispatcher);
        DataPackCommand.register(this.dispatcher);
        DebugCommand.register(this.dispatcher);
        DefaultGameModeCommands.register(this.dispatcher);
        DifficultyCommand.register(this.dispatcher);
        EffectCommands.register(this.dispatcher);
        EmoteCommands.register(this.dispatcher);
        EnchantCommand.register(this.dispatcher);
        ExperienceCommand.register(this.dispatcher);
        FillCommand.register(this.dispatcher);
        ForceLoadCommand.register(this.dispatcher);
        FunctionCommand.register(this.dispatcher);
        GameModeCommand.register(this.dispatcher);
        GameRuleCommand.register(this.dispatcher);
        GiveCommand.register(this.dispatcher);
        HelpCommand.register(this.dispatcher);
        KickCommand.register(this.dispatcher);
        KillCommand.register(this.dispatcher);
        ListPlayersCommand.register(this.dispatcher);
        LocateCommand.register(this.dispatcher);
        LocateBiomeCommand.register(this.dispatcher);
        LootCommand.register(this.dispatcher);
        MsgCommand.register(this.dispatcher);
        ParticleCommand.register(this.dispatcher);
        PlaySoundCommand.register(this.dispatcher);
        ReloadCommand.register(this.dispatcher);
        RecipeCommand.register(this.dispatcher);
        ReplaceItemCommand.register(this.dispatcher);
        SayCommand.register(this.dispatcher);
        ScheduleCommand.register(this.dispatcher);
        ScoreboardCommand.register(this.dispatcher);
        SeedCommand.register(this.dispatcher, a != CommandSelection.INTEGRATED);
        SetBlockCommand.register(this.dispatcher);
        SetSpawnCommand.register(this.dispatcher);
        SetWorldSpawnCommand.register(this.dispatcher);
        SpectateCommand.register(this.dispatcher);
        SpreadPlayersCommand.register(this.dispatcher);
        StopSoundCommand.register(this.dispatcher);
        SummonCommand.register(this.dispatcher);
        TagCommand.register(this.dispatcher);
        TeamCommand.register(this.dispatcher);
        TeamMsgCommand.register(this.dispatcher);
        TeleportCommand.register(this.dispatcher);
        TellRawCommand.register(this.dispatcher);
        TimeCommand.register(this.dispatcher);
        TitleCommand.register(this.dispatcher);
        TriggerCommand.register(this.dispatcher);
        WeatherCommand.register(this.dispatcher);
        WorldBorderCommand.register(this.dispatcher);
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            TestCommand.register(this.dispatcher);
        }
        if (a.includeDedicated) {
            BanIpCommands.register(this.dispatcher);
            BanListCommands.register(this.dispatcher);
            BanPlayerCommands.register(this.dispatcher);
            DeOpCommands.register(this.dispatcher);
            OpCommand.register(this.dispatcher);
            PardonCommand.register(this.dispatcher);
            PardonIpCommand.register(this.dispatcher);
            SaveAllCommand.register(this.dispatcher);
            SaveOffCommand.register(this.dispatcher);
            SaveOnCommand.register(this.dispatcher);
            SetPlayerIdleTimeoutCommand.register(this.dispatcher);
            StopCommand.register(this.dispatcher);
            WhitelistCommand.register(this.dispatcher);
        }
        if (a.includeIntegrated) {
            PublishCommand.register(this.dispatcher);
        }
        this.dispatcher.findAmbiguities((commandNode1, commandNode2, commandNode3, collection) -> Commands.LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", this.dispatcher.getPath(commandNode2), this.dispatcher.getPath(commandNode3), collection));
        this.dispatcher.setConsumer((commandContext, boolean2, integer) -> ((CommandSourceStack)commandContext.getSource()).onCommandComplete((CommandContext<CommandSourceStack>)commandContext, boolean2, integer));
    }
    
    public int performCommand(final CommandSourceStack db, final String string) {
        final StringReader stringReader4 = new StringReader(string);
        if (stringReader4.canRead() && stringReader4.peek() == '/') {
            stringReader4.skip();
        }
        db.getServer().getProfiler().push(string);
        try {
            return this.dispatcher.execute(stringReader4, db);
        }
        catch (CommandRuntimeException cz5) {
            db.sendFailure(cz5.getComponent());
            return 0;
        }
        catch (CommandSyntaxException commandSyntaxException5) {
            db.sendFailure(ComponentUtils.fromMessage(commandSyntaxException5.getRawMessage()));
            if (commandSyntaxException5.getInput() != null && commandSyntaxException5.getCursor() >= 0) {
                final int integer6 = Math.min(commandSyntaxException5.getInput().length(), commandSyntaxException5.getCursor());
                final MutableComponent nx7 = new TextComponent("").withStyle(ChatFormatting.GRAY).withStyle((UnaryOperator<Style>)(ob -> ob.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string))));
                if (integer6 > 10) {
                    nx7.append("...");
                }
                nx7.append(commandSyntaxException5.getInput().substring(Math.max(0, integer6 - 10), integer6));
                if (integer6 < commandSyntaxException5.getInput().length()) {
                    final Component nr8 = new TextComponent(commandSyntaxException5.getInput().substring(integer6)).withStyle(ChatFormatting.RED, ChatFormatting.UNDERLINE);
                    nx7.append(nr8);
                }
                nx7.append(new TranslatableComponent("command.context.here").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
                db.sendFailure(nx7);
            }
            return 0;
        }
        catch (Exception exception5) {
            final MutableComponent nx8 = new TextComponent((exception5.getMessage() == null) ? exception5.getClass().getName() : exception5.getMessage());
            if (Commands.LOGGER.isDebugEnabled()) {
                Commands.LOGGER.error("Command exception: {}", string, exception5);
                final StackTraceElement[] arr7 = exception5.getStackTrace();
                for (int integer7 = 0; integer7 < Math.min(arr7.length, 3); ++integer7) {
                    nx8.append("\n\n").append(arr7[integer7].getMethodName()).append("\n ").append(arr7[integer7].getFileName()).append(":").append(String.valueOf(arr7[integer7].getLineNumber()));
                }
            }
            db.sendFailure(new TranslatableComponent("command.failed").withStyle((UnaryOperator<Style>)(ob -> ob.withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)nx8)))));
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                db.sendFailure(new TextComponent(Util.describeError((Throwable)exception5)));
                Commands.LOGGER.error("'" + string + "' threw an exception", (Throwable)exception5);
            }
            return 0;
        }
        finally {
            db.getServer().getProfiler().pop();
        }
    }
    
    public void sendCommands(final ServerPlayer aah) {
        final Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> map3 = (Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>>)Maps.newHashMap();
        final RootCommandNode<SharedSuggestionProvider> rootCommandNode4 = (RootCommandNode<SharedSuggestionProvider>)new RootCommandNode();
        map3.put(this.dispatcher.getRoot(), rootCommandNode4);
        this.fillUsableCommands((CommandNode<CommandSourceStack>)this.dispatcher.getRoot(), (CommandNode<SharedSuggestionProvider>)rootCommandNode4, aah.createCommandSourceStack(), map3);
        aah.connection.send(new ClientboundCommandsPacket(rootCommandNode4));
    }
    
    private void fillUsableCommands(final CommandNode<CommandSourceStack> commandNode1, final CommandNode<SharedSuggestionProvider> commandNode2, final CommandSourceStack db, final Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> map) {
        for (final CommandNode<CommandSourceStack> commandNode3 : commandNode1.getChildren()) {
            if (commandNode3.canUse(db)) {
                final ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder8 = commandNode3.createBuilder();
                argumentBuilder8.requires(dd -> true);
                if (argumentBuilder8.getCommand() != null) {
                    argumentBuilder8.executes(commandContext -> 0);
                }
                if (argumentBuilder8 instanceof RequiredArgumentBuilder) {
                    final RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder9 = argumentBuilder8;
                    if (requiredArgumentBuilder9.getSuggestionsProvider() != null) {
                        requiredArgumentBuilder9.suggests((SuggestionProvider)SuggestionProviders.safelySwap((SuggestionProvider<SharedSuggestionProvider>)requiredArgumentBuilder9.getSuggestionsProvider()));
                    }
                }
                if (argumentBuilder8.getRedirect() != null) {
                    argumentBuilder8.redirect((CommandNode)map.get(argumentBuilder8.getRedirect()));
                }
                final CommandNode<SharedSuggestionProvider> commandNode4 = (CommandNode<SharedSuggestionProvider>)argumentBuilder8.build();
                map.put(commandNode3, commandNode4);
                commandNode2.addChild((CommandNode)commandNode4);
                if (commandNode3.getChildren().isEmpty()) {
                    continue;
                }
                this.fillUsableCommands(commandNode3, commandNode4, db, map);
            }
        }
    }
    
    public static LiteralArgumentBuilder<CommandSourceStack> literal(final String string) {
        return (LiteralArgumentBuilder<CommandSourceStack>)LiteralArgumentBuilder.literal(string);
    }
    
    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(final String string, final ArgumentType<T> argumentType) {
        return (RequiredArgumentBuilder<CommandSourceStack, T>)RequiredArgumentBuilder.argument(string, (ArgumentType)argumentType);
    }
    
    public static Predicate<String> createValidator(final ParseFunction b) {
        return (Predicate<String>)(string -> {
            try {
                b.parse(new StringReader(string));
                return true;
            }
            catch (CommandSyntaxException commandSyntaxException3) {
                return false;
            }
        });
    }
    
    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return this.dispatcher;
    }
    
    @Nullable
    public static <S> CommandSyntaxException getParseException(final ParseResults<S> parseResults) {
        if (!parseResults.getReader().canRead()) {
            return null;
        }
        if (parseResults.getExceptions().size() == 1) {
            return (CommandSyntaxException)parseResults.getExceptions().values().iterator().next();
        }
        if (parseResults.getContext().getRange().isEmpty()) {
            return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResults.getReader());
        }
        return CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parseResults.getReader());
    }
    
    public static void validate() {
        final RootCommandNode<CommandSourceStack> rootCommandNode1 = (RootCommandNode<CommandSourceStack>)new Commands(CommandSelection.ALL).getDispatcher().getRoot();
        final Set<ArgumentType<?>> set2 = ArgumentTypes.findUsedArgumentTypes((com.mojang.brigadier.tree.CommandNode<Object>)rootCommandNode1);
        final Set<ArgumentType<?>> set3 = (Set<ArgumentType<?>>)set2.stream().filter(argumentType -> !ArgumentTypes.isTypeRegistered(argumentType)).collect(Collectors.toSet());
        if (!set3.isEmpty()) {
            Commands.LOGGER.warn("Missing type registration for following arguments:\n {}", set3.stream().map(argumentType -> new StringBuilder().append("\t").append(argumentType).toString()).collect(Collectors.joining(",\n")));
            throw new IllegalStateException("Unregistered argument types");
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public enum CommandSelection {
        ALL(true, true), 
        DEDICATED(false, true), 
        INTEGRATED(true, false);
        
        private final boolean includeIntegrated;
        private final boolean includeDedicated;
        
        private CommandSelection(final boolean boolean3, final boolean boolean4) {
            this.includeIntegrated = boolean3;
            this.includeDedicated = boolean4;
        }
    }
    
    @FunctionalInterface
    public interface ParseFunction {
        void parse(final StringReader stringReader) throws CommandSyntaxException;
    }
}
