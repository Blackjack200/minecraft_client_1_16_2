package net.minecraft.server.commands;

import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.Message;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import java.util.Objects;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.SlotArgument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public class LootCommand {
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_LOOT_TABLE;
    private static final DynamicCommandExceptionType ERROR_NO_HELD_ITEMS;
    private static final DynamicCommandExceptionType ERROR_NO_LOOT_TABLE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)LootCommand.<LiteralArgumentBuilder>addTargets((LiteralArgumentBuilder)Commands.literal("loot").requires(db -> db.hasPermission(2)), (argumentBuilder, b) -> argumentBuilder.then(Commands.literal("fish").then(Commands.argument("loot_table", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)LootCommand.SUGGEST_LOOT_TABLE).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).executes(commandContext -> dropFishingLoot((CommandContext<CommandSourceStack>)commandContext, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "loot_table"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), ItemStack.EMPTY, b))).then(Commands.argument("tool", (com.mojang.brigadier.arguments.ArgumentType<Object>)ItemArgument.item()).executes(commandContext -> dropFishingLoot((CommandContext<CommandSourceStack>)commandContext, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "loot_table"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), ItemArgument.getItem((com.mojang.brigadier.context.CommandContext<Object>)commandContext, "tool").createItemStack(1, false), b)))).then(Commands.literal("mainhand").executes(commandContext -> dropFishingLoot((CommandContext<CommandSourceStack>)commandContext, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "loot_table"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), getSourceHandItem((CommandSourceStack)commandContext.getSource(), EquipmentSlot.MAINHAND), b)))).then(Commands.literal("offhand").executes(commandContext -> dropFishingLoot((CommandContext<CommandSourceStack>)commandContext, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "loot_table"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), getSourceHandItem((CommandSourceStack)commandContext.getSource(), EquipmentSlot.OFFHAND), b)))))).then(Commands.literal("loot").then(Commands.argument("loot_table", (com.mojang.brigadier.arguments.ArgumentType<Object>)ResourceLocationArgument.id()).suggests((SuggestionProvider)LootCommand.SUGGEST_LOOT_TABLE).executes(commandContext -> dropChestLoot((CommandContext<CommandSourceStack>)commandContext, ResourceLocationArgument.getId((CommandContext<CommandSourceStack>)commandContext, "loot_table"), b)))).then(Commands.literal("kill").then(Commands.argument("target", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entity()).executes(commandContext -> dropKillLoot((CommandContext<CommandSourceStack>)commandContext, EntityArgument.getEntity((CommandContext<CommandSourceStack>)commandContext, "target"), b)))).then(Commands.literal("mine").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).executes(commandContext -> dropBlockLoot((CommandContext<CommandSourceStack>)commandContext, BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), ItemStack.EMPTY, b))).then(Commands.argument("tool", (com.mojang.brigadier.arguments.ArgumentType<Object>)ItemArgument.item()).executes(commandContext -> dropBlockLoot((CommandContext<CommandSourceStack>)commandContext, BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), ItemArgument.getItem((com.mojang.brigadier.context.CommandContext<Object>)commandContext, "tool").createItemStack(1, false), b)))).then(Commands.literal("mainhand").executes(commandContext -> dropBlockLoot((CommandContext<CommandSourceStack>)commandContext, BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), getSourceHandItem((CommandSourceStack)commandContext.getSource(), EquipmentSlot.MAINHAND), b)))).then(Commands.literal("offhand").executes(commandContext -> dropBlockLoot((CommandContext<CommandSourceStack>)commandContext, BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), getSourceHandItem((CommandSourceStack)commandContext.getSource(), EquipmentSlot.OFFHAND), b)))))));
    }
    
    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T addTargets(final T argumentBuilder, final TailProvider c) {
        return (T)argumentBuilder.then(((LiteralArgumentBuilder)Commands.literal("replace").then(Commands.literal("entity").then(Commands.argument("entities", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.entities()).then(c.construct(Commands.argument("slot", (com.mojang.brigadier.arguments.ArgumentType<Object>)SlotArgument.slot()), (commandContext, list, a) -> entityReplace(EntityArgument.getEntities(commandContext, "entities"), SlotArgument.getSlot(commandContext, "slot"), list.size(), list, a)).then((ArgumentBuilder)c.construct(Commands.argument("count", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)), (commandContext, list, a) -> entityReplace(EntityArgument.getEntities(commandContext, "entities"), SlotArgument.getSlot(commandContext, "slot"), IntegerArgumentType.getInteger((CommandContext)commandContext, "count"), list, a))))))).then(Commands.literal("block").then(Commands.argument("targetPos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(c.construct(Commands.argument("slot", (com.mojang.brigadier.arguments.ArgumentType<Object>)SlotArgument.slot()), (commandContext, list, a) -> blockReplace((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos(commandContext, "targetPos"), SlotArgument.getSlot(commandContext, "slot"), list.size(), list, a)).then((ArgumentBuilder)c.construct(Commands.argument("count", (com.mojang.brigadier.arguments.ArgumentType<Object>)IntegerArgumentType.integer(0)), (commandContext, list, a) -> blockReplace((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos(commandContext, "targetPos"), IntegerArgumentType.getInteger((CommandContext)commandContext, "slot"), IntegerArgumentType.getInteger((CommandContext)commandContext, "count"), list, a))))))).then(Commands.literal("insert").then((ArgumentBuilder)c.construct(Commands.argument("targetPos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()), (commandContext, list, a) -> blockDistribute((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos(commandContext, "targetPos"), list, a)))).then(Commands.literal("give").then((ArgumentBuilder)c.construct(Commands.argument("players", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntityArgument.players()), (commandContext, list, a) -> playerGive(EntityArgument.getPlayers(commandContext, "players"), list, a)))).then(Commands.literal("spawn").then((ArgumentBuilder)c.construct(Commands.argument("targetPos", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()), (commandContext, list, a) -> dropInWorld((CommandSourceStack)commandContext.getSource(), Vec3Argument.getVec3(commandContext, "targetPos"), list, a))));
    }
    
    private static Container getContainer(final CommandSourceStack db, final BlockPos fx) throws CommandSyntaxException {
        final BlockEntity ccg3 = db.getLevel().getBlockEntity(fx);
        if (!(ccg3 instanceof Container)) {
            throw ReplaceItemCommand.ERROR_NOT_A_CONTAINER.create();
        }
        return (Container)ccg3;
    }
    
    private static int blockDistribute(final CommandSourceStack db, final BlockPos fx, final List<ItemStack> list, final Callback a) throws CommandSyntaxException {
        final Container aok5 = getContainer(db, fx);
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayListWithCapacity(list.size());
        for (final ItemStack bly8 : list) {
            if (distributeToContainer(aok5, bly8.copy())) {
                aok5.setChanged();
                list2.add(bly8);
            }
        }
        a.accept(list2);
        return list2.size();
    }
    
    private static boolean distributeToContainer(final Container aok, final ItemStack bly) {
        boolean boolean3 = false;
        for (int integer4 = 0; integer4 < aok.getContainerSize() && !bly.isEmpty(); ++integer4) {
            final ItemStack bly2 = aok.getItem(integer4);
            if (aok.canPlaceItem(integer4, bly)) {
                if (bly2.isEmpty()) {
                    aok.setItem(integer4, bly);
                    boolean3 = true;
                    break;
                }
                if (canMergeItems(bly2, bly)) {
                    final int integer5 = bly.getMaxStackSize() - bly2.getCount();
                    final int integer6 = Math.min(bly.getCount(), integer5);
                    bly.shrink(integer6);
                    bly2.grow(integer6);
                    boolean3 = true;
                }
            }
        }
        return boolean3;
    }
    
    private static int blockReplace(final CommandSourceStack db, final BlockPos fx, final int integer3, final int integer4, final List<ItemStack> list, final Callback a) throws CommandSyntaxException {
        final Container aok7 = getContainer(db, fx);
        final int integer5 = aok7.getContainerSize();
        if (integer3 < 0 || integer3 >= integer5) {
            throw ReplaceItemCommand.ERROR_INAPPLICABLE_SLOT.create(integer3);
        }
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayListWithCapacity(list.size());
        for (int integer6 = 0; integer6 < integer4; ++integer6) {
            final int integer7 = integer3 + integer6;
            final ItemStack bly12 = (ItemStack)((integer6 < list.size()) ? list.get(integer6) : ItemStack.EMPTY);
            if (aok7.canPlaceItem(integer7, bly12)) {
                aok7.setItem(integer7, bly12);
                list2.add(bly12);
            }
        }
        a.accept(list2);
        return list2.size();
    }
    
    private static boolean canMergeItems(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getItem() == bly2.getItem() && bly1.getDamageValue() == bly2.getDamageValue() && bly1.getCount() <= bly1.getMaxStackSize() && Objects.equals(bly1.getTag(), bly2.getTag());
    }
    
    private static int playerGive(final Collection<ServerPlayer> collection, final List<ItemStack> list, final Callback a) throws CommandSyntaxException {
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayListWithCapacity(list.size());
        for (final ItemStack bly6 : list) {
            for (final ServerPlayer aah8 : collection) {
                if (aah8.inventory.add(bly6.copy())) {
                    list2.add(bly6);
                }
            }
        }
        a.accept(list2);
        return list2.size();
    }
    
    private static void setSlots(final Entity apx, final List<ItemStack> list2, final int integer3, final int integer4, final List<ItemStack> list5) {
        for (int integer5 = 0; integer5 < integer4; ++integer5) {
            final ItemStack bly7 = (ItemStack)((integer5 < list2.size()) ? list2.get(integer5) : ItemStack.EMPTY);
            if (apx.setSlot(integer3 + integer5, bly7.copy())) {
                list5.add(bly7);
            }
        }
    }
    
    private static int entityReplace(final Collection<? extends Entity> collection, final int integer2, final int integer3, final List<ItemStack> list, final Callback a) throws CommandSyntaxException {
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayListWithCapacity(list.size());
        for (final Entity apx8 : collection) {
            if (apx8 instanceof ServerPlayer) {
                final ServerPlayer aah9 = (ServerPlayer)apx8;
                aah9.inventoryMenu.broadcastChanges();
                setSlots(apx8, list, integer2, integer3, list2);
                aah9.inventoryMenu.broadcastChanges();
            }
            else {
                setSlots(apx8, list, integer2, integer3, list2);
            }
        }
        a.accept(list2);
        return list2.size();
    }
    
    private static int dropInWorld(final CommandSourceStack db, final Vec3 dck, final List<ItemStack> list, final Callback a) throws CommandSyntaxException {
        final ServerLevel aag5 = db.getLevel();
        list.forEach(bly -> {
            final ItemEntity bcs4 = new ItemEntity(aag5, dck.x, dck.y, dck.z, bly.copy());
            bcs4.setDefaultPickUpDelay();
            aag5.addFreshEntity(bcs4);
        });
        a.accept(list);
        return list.size();
    }
    
    private static void callback(final CommandSourceStack db, final List<ItemStack> list) {
        if (list.size() == 1) {
            final ItemStack bly3 = (ItemStack)list.get(0);
            db.sendSuccess(new TranslatableComponent("commands.drop.success.single", new Object[] { bly3.getCount(), bly3.getDisplayName() }), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.drop.success.multiple", new Object[] { list.size() }), false);
        }
    }
    
    private static void callback(final CommandSourceStack db, final List<ItemStack> list, final ResourceLocation vk) {
        if (list.size() == 1) {
            final ItemStack bly4 = (ItemStack)list.get(0);
            db.sendSuccess(new TranslatableComponent("commands.drop.success.single_with_table", new Object[] { bly4.getCount(), bly4.getDisplayName(), vk }), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.drop.success.multiple_with_table", new Object[] { list.size(), vk }), false);
        }
    }
    
    private static ItemStack getSourceHandItem(final CommandSourceStack db, final EquipmentSlot aqc) throws CommandSyntaxException {
        final Entity apx3 = db.getEntityOrException();
        if (apx3 instanceof LivingEntity) {
            return ((LivingEntity)apx3).getItemBySlot(aqc);
        }
        throw LootCommand.ERROR_NO_HELD_ITEMS.create(apx3.getDisplayName());
    }
    
    private static int dropBlockLoot(final CommandContext<CommandSourceStack> commandContext, final BlockPos fx, final ItemStack bly, final DropConsumer b) throws CommandSyntaxException {
        final CommandSourceStack db5 = (CommandSourceStack)commandContext.getSource();
        final ServerLevel aag6 = db5.getLevel();
        final BlockState cee7 = aag6.getBlockState(fx);
        final BlockEntity ccg8 = aag6.getBlockEntity(fx);
        final LootContext.Builder a9 = new LootContext.Builder(aag6).<Vec3>withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(fx)).<BlockState>withParameter(LootContextParams.BLOCK_STATE, cee7).<BlockEntity>withOptionalParameter(LootContextParams.BLOCK_ENTITY, ccg8).<Entity>withOptionalParameter(LootContextParams.THIS_ENTITY, db5.getEntity()).<ItemStack>withParameter(LootContextParams.TOOL, bly);
        final List<ItemStack> list2 = cee7.getDrops(a9);
        return b.accept(commandContext, list2, list -> callback(db5, list, cee7.getBlock().getLootTable()));
    }
    
    private static int dropKillLoot(final CommandContext<CommandSourceStack> commandContext, final Entity apx, final DropConsumer b) throws CommandSyntaxException {
        if (!(apx instanceof LivingEntity)) {
            throw LootCommand.ERROR_NO_LOOT_TABLE.create(apx.getDisplayName());
        }
        final ResourceLocation vk4 = ((LivingEntity)apx).getLootTable();
        final CommandSourceStack db5 = (CommandSourceStack)commandContext.getSource();
        final LootContext.Builder a6 = new LootContext.Builder(db5.getLevel());
        final Entity apx2 = db5.getEntity();
        if (apx2 instanceof Player) {
            a6.<Player>withParameter(LootContextParams.LAST_DAMAGE_PLAYER, (Player)apx2);
        }
        a6.<DamageSource>withParameter(LootContextParams.DAMAGE_SOURCE, DamageSource.MAGIC);
        a6.<Entity>withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, apx2);
        a6.<Entity>withOptionalParameter(LootContextParams.KILLER_ENTITY, apx2);
        a6.<Entity>withParameter(LootContextParams.THIS_ENTITY, apx);
        a6.<Vec3>withParameter(LootContextParams.ORIGIN, db5.getPosition());
        final LootTable cyv8 = db5.getServer().getLootTables().get(vk4);
        final List<ItemStack> list2 = cyv8.getRandomItems(a6.create(LootContextParamSets.ENTITY));
        return b.accept(commandContext, list2, list -> callback(db5, list, vk4));
    }
    
    private static int dropChestLoot(final CommandContext<CommandSourceStack> commandContext, final ResourceLocation vk, final DropConsumer b) throws CommandSyntaxException {
        final CommandSourceStack db4 = (CommandSourceStack)commandContext.getSource();
        final LootContext.Builder a5 = new LootContext.Builder(db4.getLevel()).<Entity>withOptionalParameter(LootContextParams.THIS_ENTITY, db4.getEntity()).<Vec3>withParameter(LootContextParams.ORIGIN, db4.getPosition());
        return drop(commandContext, vk, a5.create(LootContextParamSets.CHEST), b);
    }
    
    private static int dropFishingLoot(final CommandContext<CommandSourceStack> commandContext, final ResourceLocation vk, final BlockPos fx, final ItemStack bly, final DropConsumer b) throws CommandSyntaxException {
        final CommandSourceStack db6 = (CommandSourceStack)commandContext.getSource();
        final LootContext cys7 = new LootContext.Builder(db6.getLevel()).<Vec3>withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(fx)).<ItemStack>withParameter(LootContextParams.TOOL, bly).<Entity>withOptionalParameter(LootContextParams.THIS_ENTITY, db6.getEntity()).create(LootContextParamSets.FISHING);
        return drop(commandContext, vk, cys7, b);
    }
    
    private static int drop(final CommandContext<CommandSourceStack> commandContext, final ResourceLocation vk, final LootContext cys, final DropConsumer b) throws CommandSyntaxException {
        final CommandSourceStack db5 = (CommandSourceStack)commandContext.getSource();
        final LootTable cyv6 = db5.getServer().getLootTables().get(vk);
        final List<ItemStack> list2 = cyv6.getRandomItems(cys);
        return b.accept(commandContext, list2, list -> callback(db5, list));
    }
    
    static {
        SUGGEST_LOOT_TABLE = ((commandContext, suggestionsBuilder) -> {
            final LootTables cyw3 = ((CommandSourceStack)commandContext.getSource()).getServer().getLootTables();
            return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)cyw3.getIds(), suggestionsBuilder);
        });
        ERROR_NO_HELD_ITEMS = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.drop.no_held_items", new Object[] { object }));
        ERROR_NO_LOOT_TABLE = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands.drop.no_loot_table", new Object[] { object }));
    }
    
    @FunctionalInterface
    interface TailProvider {
        ArgumentBuilder<CommandSourceStack, ?> construct(final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final DropConsumer b);
    }
    
    @FunctionalInterface
    interface DropConsumer {
        int accept(final CommandContext<CommandSourceStack> commandContext, final List<ItemStack> list, final Callback a) throws CommandSyntaxException;
    }
    
    @FunctionalInterface
    interface Callback {
        void accept(final List<ItemStack> list) throws CommandSyntaxException;
    }
}
