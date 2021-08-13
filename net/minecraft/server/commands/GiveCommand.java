package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.commands.arguments.item.ItemInput;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.item.ItemArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class GiveCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("give").requires(db -> db.hasPermission(2))).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()).then(((RequiredArgumentBuilder)Commands.argument("item", (com.mojang.brigadier.arguments.ArgumentType<Object>)ItemArgument.item()).executes(commandContext -> giveItem((CommandSourceStack)commandContext.getSource(), ItemArgument.getItem((com.mojang.brigadier.context.CommandContext<Object>)commandContext, "item"), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), 1))).then(Commands.argument("count", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(1)).executes(commandContext -> giveItem((CommandSourceStack)commandContext.getSource(), ItemArgument.getItem((com.mojang.brigadier.context.CommandContext<Object>)commandContext, "item"), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), IntegerArgumentType.getInteger(commandContext, "count")))))));
    }
    
    private static int giveItem(final CommandSourceStack db, final ItemInput ex, final Collection<ServerPlayer> collection, final int integer) throws CommandSyntaxException {
        for (final ServerPlayer aah6 : collection) {
            int integer2 = integer;
            while (integer2 > 0) {
                final int integer3 = Math.min(ex.getItem().getMaxStackSize(), integer2);
                integer2 -= integer3;
                final ItemStack bly9 = ex.createItemStack(integer3, false);
                final boolean boolean10 = aah6.inventory.add(bly9);
                if (!boolean10 || !bly9.isEmpty()) {
                    final ItemEntity bcs11 = aah6.drop(bly9, false);
                    if (bcs11 == null) {
                        continue;
                    }
                    bcs11.setNoPickUpDelay();
                    bcs11.setOwner(aah6.getUUID());
                }
                else {
                    bly9.setCount(1);
                    final ItemEntity bcs11 = aah6.drop(bly9, false);
                    if (bcs11 != null) {
                        bcs11.makeFakeItem();
                    }
                    aah6.level.playSound(null, aah6.getX(), aah6.getY(), aah6.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((aah6.getRandom().nextFloat() - aah6.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    aah6.inventoryMenu.broadcastChanges();
                }
            }
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.give.success.single", new Object[] { integer, ex.createItemStack(integer, false).getDisplayName(), ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.give.success.single", new Object[] { integer, ex.createItemStack(integer, false).getDisplayName(), collection.size() }), true);
        }
        return collection.size();
    }
}
