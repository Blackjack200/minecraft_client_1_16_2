package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.item.ItemStack;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class EnchantCommand {
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY;
    private static final DynamicCommandExceptionType ERROR_NO_ITEM;
    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE;
    private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH;
    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("enchant").requires(db -> db.hasPermission(2))).then(Commands.argument("targets", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).then(((RequiredArgumentBuilder)Commands.argument("enchantment", (com.mojang.brigadier.arguments.ArgumentType<Object>)ItemEnchantmentArgument.enchantment()).executes(commandContext -> enchant((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ItemEnchantmentArgument.getEnchantment((CommandContext<CommandSourceStack>)commandContext, "enchantment"), 1))).then(Commands.argument("level", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> enchant((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities((CommandContext<CommandSourceStack>)commandContext, "targets"), ItemEnchantmentArgument.getEnchantment((CommandContext<CommandSourceStack>)commandContext, "enchantment"), IntegerArgumentType.getInteger(commandContext, "level")))))));
    }
    
    private static int enchant(final CommandSourceStack db, final Collection<? extends Entity> collection, final Enchantment bpp, final int integer) throws CommandSyntaxException {
        if (integer > bpp.getMaxLevel()) {
            throw EnchantCommand.ERROR_LEVEL_TOO_HIGH.create(integer, bpp.getMaxLevel());
        }
        int integer2 = 0;
        for (final Entity apx7 : collection) {
            if (apx7 instanceof LivingEntity) {
                final LivingEntity aqj8 = (LivingEntity)apx7;
                final ItemStack bly9 = aqj8.getMainHandItem();
                if (!bly9.isEmpty()) {
                    if (bpp.canEnchant(bly9) && EnchantmentHelper.isEnchantmentCompatible((Collection<Enchantment>)EnchantmentHelper.getEnchantments(bly9).keySet(), bpp)) {
                        bly9.enchant(bpp, integer);
                        ++integer2;
                    }
                    else {
                        if (collection.size() == 1) {
                            throw EnchantCommand.ERROR_INCOMPATIBLE.create(bly9.getItem().getName(bly9).getString());
                        }
                        continue;
                    }
                }
                else {
                    if (collection.size() == 1) {
                        throw EnchantCommand.ERROR_NO_ITEM.create(aqj8.getName().getString());
                    }
                    continue;
                }
            }
            else {
                if (collection.size() == 1) {
                    throw EnchantCommand.ERROR_NOT_LIVING_ENTITY.create(apx7.getName().getString());
                }
                continue;
            }
        }
        if (integer2 == 0) {
            throw EnchantCommand.ERROR_NOTHING_HAPPENED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.enchant.success.single", new Object[] { bpp.getFullname(integer), ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.enchant.success.multiple", new Object[] { bpp.getFullname(integer), collection.size() }), true);
        }
        return integer2;
    }
    
    static {
        ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.enchant.failed.entity", new Object[] { object }));
        ERROR_NO_ITEM = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.enchant.failed.itemless", new Object[] { object }));
        ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.enchant.failed.incompatible", new Object[] { object }));
        ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.enchant.failed.level", new Object[] { object1, object2 }));
        ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.enchant.failed"));
    }
}
