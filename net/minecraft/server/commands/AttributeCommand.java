package net.minecraft.server.commands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.Entity;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.UuidArgument;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public class AttributeCommand {
    private static final SuggestionProvider<CommandSourceStack> AVAILABLE_ATTRIBUTES;
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY;
    private static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ATTRIBUTE;
    private static final Dynamic3CommandExceptionType ERROR_NO_SUCH_MODIFIER;
    private static final Dynamic3CommandExceptionType ERROR_MODIFIER_ALREADY_PRESENT;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("attribute").requires(db -> db.hasPermission(2))).then(Commands.argument("target", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entity()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("attribute", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)AttributeCommand.AVAILABLE_ATTRIBUTES).then(((LiteralArgumentBuilder)Commands.literal("get").executes(commandContext -> getAttributeValue((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), 1.0))).then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).executes(commandContext -> getAttributeValue((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), DoubleArgumentType.getDouble(commandContext, "scale")))))).then(((LiteralArgumentBuilder)Commands.literal("base").then(Commands.literal("set").then(Commands.argument("value", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).executes(commandContext -> setAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), DoubleArgumentType.getDouble(commandContext, "value")))))).then(((LiteralArgumentBuilder)Commands.literal("get").executes(commandContext -> getAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), 1.0))).then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).executes(commandContext -> getAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), DoubleArgumentType.getDouble(commandContext, "scale"))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("modifier").then(Commands.literal("add").then(Commands.argument("uuid", (com.mojang.brigadier.arguments.ArgumentType<Object>)UuidArgument.uuid()).then(Commands.argument("name", (com.mojang.brigadier.arguments.ArgumentType<Object>)StringArgumentType.string()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("value", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(commandContext -> addModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), UuidArgument.getUuid((CommandContext<CommandSourceStack>)commandContext, "uuid"), StringArgumentType.getString(commandContext, "name"), DoubleArgumentType.getDouble(commandContext, "value"), AttributeModifier.Operation.ADDITION)))).then(Commands.literal("multiply").executes(commandContext -> addModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), UuidArgument.getUuid((CommandContext<CommandSourceStack>)commandContext, "uuid"), StringArgumentType.getString(commandContext, "name"), DoubleArgumentType.getDouble(commandContext, "value"), AttributeModifier.Operation.MULTIPLY_TOTAL)))).then(Commands.literal("multiply_base").executes(commandContext -> addModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), UuidArgument.getUuid((CommandContext<CommandSourceStack>)commandContext, "uuid"), StringArgumentType.getString(commandContext, "name"), DoubleArgumentType.getDouble(commandContext, "value"), AttributeModifier.Operation.MULTIPLY_BASE)))))))).then(Commands.literal("remove").then(Commands.argument("uuid", (com.mojang.brigadier.arguments.ArgumentType<Object>)UuidArgument.uuid()).executes(commandContext -> removeModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), UuidArgument.getUuid((CommandContext<CommandSourceStack>)commandContext, "uuid")))))).then(Commands.literal("value").then(Commands.literal("get").then(((RequiredArgumentBuilder)Commands.argument("uuid", (com.mojang.brigadier.arguments.ArgumentType<Object>)UuidArgument.uuid()).executes(commandContext -> getAttributeModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), UuidArgument.getUuid((CommandContext<CommandSourceStack>)commandContext, "uuid"), 1.0))).then(Commands.argument("scale", (com.mojang.brigadier.arguments.ArgumentType<Object>)DoubleArgumentType.doubleArg()).executes(commandContext -> getAttributeModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), ResourceLocationArgument.getAttribute((CommandContext<CommandSourceStack>)commandContext, "attribute"), UuidArgument.getUuid((CommandContext<CommandSourceStack>)commandContext, "uuid"), DoubleArgumentType.getDouble(commandContext, "scale")))))))))));
    }
    
    private static AttributeInstance getAttributeInstance(final Entity apx, final Attribute ard) throws CommandSyntaxException {
        final AttributeInstance are3 = getLivingEntity(apx).getAttributes().getInstance(ard);
        if (are3 == null) {
            throw AttributeCommand.ERROR_NO_SUCH_ATTRIBUTE.create(apx.getName(), new TranslatableComponent(ard.getDescriptionId()));
        }
        return are3;
    }
    
    private static LivingEntity getLivingEntity(final Entity apx) throws CommandSyntaxException {
        if (!(apx instanceof LivingEntity)) {
            throw AttributeCommand.ERROR_NOT_LIVING_ENTITY.create(apx.getName());
        }
        return (LivingEntity)apx;
    }
    
    private static LivingEntity getEntityWithAttribute(final Entity apx, final Attribute ard) throws CommandSyntaxException {
        final LivingEntity aqj3 = getLivingEntity(apx);
        if (!aqj3.getAttributes().hasAttribute(ard)) {
            throw AttributeCommand.ERROR_NO_SUCH_ATTRIBUTE.create(apx.getName(), new TranslatableComponent(ard.getDescriptionId()));
        }
        return aqj3;
    }
    
    private static int getAttributeValue(final CommandSourceStack db, final Entity apx, final Attribute ard, final double double4) throws CommandSyntaxException {
        final LivingEntity aqj6 = getEntityWithAttribute(apx, ard);
        final double double5 = aqj6.getAttributeValue(ard);
        db.sendSuccess(new TranslatableComponent("commands.attribute.value.get.success", new Object[] { new TranslatableComponent(ard.getDescriptionId()), apx.getName(), double5 }), false);
        return (int)(double5 * double4);
    }
    
    private static int getAttributeBase(final CommandSourceStack db, final Entity apx, final Attribute ard, final double double4) throws CommandSyntaxException {
        final LivingEntity aqj6 = getEntityWithAttribute(apx, ard);
        final double double5 = aqj6.getAttributeBaseValue(ard);
        db.sendSuccess(new TranslatableComponent("commands.attribute.base_value.get.success", new Object[] { new TranslatableComponent(ard.getDescriptionId()), apx.getName(), double5 }), false);
        return (int)(double5 * double4);
    }
    
    private static int getAttributeModifier(final CommandSourceStack db, final Entity apx, final Attribute ard, final UUID uUID, final double double5) throws CommandSyntaxException {
        final LivingEntity aqj7 = getEntityWithAttribute(apx, ard);
        final AttributeMap arf8 = aqj7.getAttributes();
        if (!arf8.hasModifier(ard, uUID)) {
            throw AttributeCommand.ERROR_NO_SUCH_MODIFIER.create(apx.getName(), new TranslatableComponent(ard.getDescriptionId()), uUID);
        }
        final double double6 = arf8.getModifierValue(ard, uUID);
        db.sendSuccess(new TranslatableComponent("commands.attribute.modifier.value.get.success", new Object[] { uUID, new TranslatableComponent(ard.getDescriptionId()), apx.getName(), double6 }), false);
        return (int)(double6 * double5);
    }
    
    private static int setAttributeBase(final CommandSourceStack db, final Entity apx, final Attribute ard, final double double4) throws CommandSyntaxException {
        getAttributeInstance(apx, ard).setBaseValue(double4);
        db.sendSuccess(new TranslatableComponent("commands.attribute.base_value.set.success", new Object[] { new TranslatableComponent(ard.getDescriptionId()), apx.getName(), double4 }), false);
        return 1;
    }
    
    private static int addModifier(final CommandSourceStack db, final Entity apx, final Attribute ard, final UUID uUID, final String string, final double double6, final AttributeModifier.Operation a) throws CommandSyntaxException {
        final AttributeInstance are9 = getAttributeInstance(apx, ard);
        final AttributeModifier arg10 = new AttributeModifier(uUID, string, double6, a);
        if (are9.hasModifier(arg10)) {
            throw AttributeCommand.ERROR_MODIFIER_ALREADY_PRESENT.create(apx.getName(), new TranslatableComponent(ard.getDescriptionId()), uUID);
        }
        are9.addPermanentModifier(arg10);
        db.sendSuccess(new TranslatableComponent("commands.attribute.modifier.add.success", new Object[] { uUID, new TranslatableComponent(ard.getDescriptionId()), apx.getName() }), false);
        return 1;
    }
    
    private static int removeModifier(final CommandSourceStack db, final Entity apx, final Attribute ard, final UUID uUID) throws CommandSyntaxException {
        final AttributeInstance are5 = getAttributeInstance(apx, ard);
        if (are5.removePermanentModifier(uUID)) {
            db.sendSuccess(new TranslatableComponent("commands.attribute.modifier.remove.success", new Object[] { uUID, new TranslatableComponent(ard.getDescriptionId()), apx.getName() }), false);
            return 1;
        }
        throw AttributeCommand.ERROR_NO_SUCH_MODIFIER.create(apx.getName(), new TranslatableComponent(ard.getDescriptionId()), uUID);
    }
    
    static {
        AVAILABLE_ATTRIBUTES = ((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)Registry.ATTRIBUTE.keySet(), suggestionsBuilder));
        ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.attribute.failed.entity", new Object[] { object }));
        ERROR_NO_SUCH_ATTRIBUTE = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.attribute.failed.no_attribute", new Object[] { object1, object2 }));
        ERROR_NO_SUCH_MODIFIER = new Dynamic3CommandExceptionType((object1, object2, object3) -> new TranslatableComponent("commands.attribute.failed.no_modifier", new Object[] { object2, object1, object3 }));
        ERROR_MODIFIER_ALREADY_PRESENT = new Dynamic3CommandExceptionType((object1, object2, object3) -> new TranslatableComponent("commands.attribute.failed.modifier_already_present", new Object[] { object3, object2, object1 }));
    }
}
