package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import java.util.Collections;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class ClearInventoryCommands {
    private static final DynamicCommandExceptionType ERROR_SINGLE;
    private static final DynamicCommandExceptionType ERROR_MULTIPLE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clear").requires(db -> db.hasPermission(2))).executes(commandContext -> clearInventory((CommandSourceStack)commandContext.getSource(), (Collection<ServerPlayer>)Collections.singleton(((CommandSourceStack)commandContext.getSource()).getPlayerOrException()), (Predicate<ItemStack>)(bly -> true), -1))).then(((RequiredArgumentBuilder)Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> clearInventory((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), (Predicate<ItemStack>)(bly -> true), -1))).then(((RequiredArgumentBuilder)Commands.argument("item", (com.mojang.brigadier.arguments.ArgumentType<Object>)ItemPredicateArgument.itemPredicate()).executes(commandContext -> clearInventory((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), ItemPredicateArgument.getItemPredicate((CommandContext<CommandSourceStack>)commandContext, "item"), -1))).then(Commands.argument("maxCount", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> clearInventory((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), ItemPredicateArgument.getItemPredicate((CommandContext<CommandSourceStack>)commandContext, "item"), IntegerArgumentType.getInteger(commandContext, "maxCount")))))));
    }
    
    private static int clearInventory(final CommandSourceStack db, final Collection<ServerPlayer> collection, final Predicate<ItemStack> predicate, final int integer) throws CommandSyntaxException {
        int integer2 = 0;
        for (final ServerPlayer aah7 : collection) {
            integer2 += aah7.inventory.clearOrCountMatchingItems(predicate, integer, aah7.inventoryMenu.getCraftSlots());
            aah7.containerMenu.broadcastChanges();
            aah7.inventoryMenu.slotsChanged(aah7.inventory);
            aah7.broadcastCarriedItem();
        }
        if (integer2 != 0) {
            if (integer == 0) {
                if (collection.size() == 1) {
                    db.sendSuccess(new TranslatableComponent("commands.clear.test.single", new Object[] { integer2, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
                }
                else {
                    db.sendSuccess(new TranslatableComponent("commands.clear.test.multiple", new Object[] { integer2, collection.size() }), true);
                }
            }
            else if (collection.size() == 1) {
                db.sendSuccess(new TranslatableComponent("commands.clear.success.single", new Object[] { integer2, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.clear.success.multiple", new Object[] { integer2, collection.size() }), true);
            }
            return integer2;
        }
        if (collection.size() == 1) {
            throw ClearInventoryCommands.ERROR_SINGLE.create(((ServerPlayer)collection.iterator().next()).getName());
        }
        throw ClearInventoryCommands.ERROR_MULTIPLE.create(collection.size());
    }
    
    static {
        ERROR_SINGLE = new DynamicCommandExceptionType(object -> new TranslatableComponent("clear.failed.single", new Object[] { object }));
        ERROR_MULTIPLE = new DynamicCommandExceptionType(object -> new TranslatableComponent("clear.failed.multiple", new Object[] { object }));
    }
}
