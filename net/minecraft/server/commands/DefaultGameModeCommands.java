package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class DefaultGameModeCommands {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("defaultgamemode").requires(db -> db.hasPermission(2));
        for (final GameType brr6 : GameType.values()) {
            if (brr6 != GameType.NOT_SET) {
                literalArgumentBuilder2.then(Commands.literal(brr6.getName()).executes(commandContext -> setMode((CommandSourceStack)commandContext.getSource(), brr6)));
            }
        }
        commandDispatcher.register((LiteralArgumentBuilder)literalArgumentBuilder2);
    }
    
    private static int setMode(final CommandSourceStack db, final GameType brr) {
        int integer3 = 0;
        final MinecraftServer minecraftServer4 = db.getServer();
        minecraftServer4.setDefaultGameType(brr);
        if (minecraftServer4.getForceGameType()) {
            for (final ServerPlayer aah6 : minecraftServer4.getPlayerList().getPlayers()) {
                if (aah6.gameMode.getGameModeForPlayer() != brr) {
                    aah6.setGameMode(brr);
                    ++integer3;
                }
            }
        }
        db.sendSuccess(new TranslatableComponent("commands.defaultgamemode.success", new Object[] { brr.getDisplayName() }), true);
        return integer3;
    }
}
