package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class LocateCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("locate").requires(db -> db.hasPermission(2));
        for (final Map.Entry<String, StructureFeature<?>> entry4 : StructureFeature.STRUCTURES_REGISTRY.entrySet()) {
            literalArgumentBuilder2 = (LiteralArgumentBuilder<CommandSourceStack>)literalArgumentBuilder2.then(Commands.literal((String)entry4.getKey()).executes(commandContext -> locate((CommandSourceStack)commandContext.getSource(), entry4.getValue())));
        }
        commandDispatcher.register((LiteralArgumentBuilder)literalArgumentBuilder2);
    }
    
    private static int locate(final CommandSourceStack db, final StructureFeature<?> ckx) throws CommandSyntaxException {
        final BlockPos fx3 = new BlockPos(db.getPosition());
        final BlockPos fx4 = db.getLevel().findNearestMapFeature(ckx, fx3, 100, false);
        if (fx4 == null) {
            throw LocateCommand.ERROR_FAILED.create();
        }
        return showLocateResult(db, ckx.getFeatureName(), fx3, fx4, "commands.locate.success");
    }
    
    public static int showLocateResult(final CommandSourceStack db, final String string2, final BlockPos fx3, final BlockPos fx4, final String string5) {
        final int integer6 = Mth.floor(dist(fx3.getX(), fx3.getZ(), fx4.getX(), fx4.getZ()));
        final Component nr7 = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("chat.coordinates", new Object[] { fx4.getX(), "~", fx4.getZ() })).withStyle((UnaryOperator<Style>)(ob -> ob.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, new StringBuilder().append("/tp @s ").append(fx4.getX()).append(" ~ ").append(fx4.getZ()).toString())).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TranslatableComponent("chat.coordinates.tooltip")))));
        db.sendSuccess(new TranslatableComponent(string5, new Object[] { string2, nr7, integer6 }), false);
        return integer6;
    }
    
    private static float dist(final int integer1, final int integer2, final int integer3, final int integer4) {
        final int integer5 = integer3 - integer1;
        final int integer6 = integer4 - integer2;
        return Mth.sqrt((float)(integer5 * integer5 + integer6 * integer6));
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.locate.failed"));
    }
}
