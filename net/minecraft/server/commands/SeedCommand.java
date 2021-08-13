package net.minecraft.server.commands;

import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.ChatFormatting;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.TextComponent;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class SeedCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher, final boolean boolean2) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("seed").requires(db -> !boolean2 || db.hasPermission(2))).executes(commandContext -> {
            final long long2 = ((CommandSourceStack)commandContext.getSource()).getLevel().getSeed();
            final Component nr4 = ComponentUtils.wrapInSquareBrackets(new TextComponent(String.valueOf(long2)).withStyle((UnaryOperator<Style>)(ob -> ob.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(long2))).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TranslatableComponent("chat.copy.click"))).withInsertion(String.valueOf(long2)))));
            ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TranslatableComponent("commands.seed.success", new Object[] { nr4 }), false);
            return (int)long2;
        }));
    }
}
