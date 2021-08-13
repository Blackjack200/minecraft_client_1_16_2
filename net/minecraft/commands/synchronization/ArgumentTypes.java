package net.minecraft.commands.synchronization;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.CommandDispatcher;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.gametest.framework.TestClassNameArgument;
import net.minecraft.gametest.framework.TestFunctionArgument;
import net.minecraft.SharedConstants;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import java.util.function.Supplier;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.brigadier.BrigadierArgumentSerializers;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ArgumentTypes {
    private static final Logger LOGGER;
    private static final Map<Class<?>, Entry<?>> BY_CLASS;
    private static final Map<ResourceLocation, Entry<?>> BY_NAME;
    
    public static <T extends ArgumentType<?>> void register(final String string, final Class<T> class2, final ArgumentSerializer<T> fj) {
        final ResourceLocation vk4 = new ResourceLocation(string);
        if (ArgumentTypes.BY_CLASS.containsKey(class2)) {
            throw new IllegalArgumentException("Class " + class2.getName() + " already has a serializer!");
        }
        if (ArgumentTypes.BY_NAME.containsKey(vk4)) {
            throw new IllegalArgumentException(new StringBuilder().append("'").append(vk4).append("' is already a registered serializer!").toString());
        }
        final Entry<T> a5 = new Entry<T>((Class)class2, (ArgumentSerializer)fj, vk4);
        ArgumentTypes.BY_CLASS.put(class2, a5);
        ArgumentTypes.BY_NAME.put(vk4, a5);
    }
    
    public static void bootStrap() {
        BrigadierArgumentSerializers.bootstrap();
        ArgumentTypes.<ArgumentType>register("entity", (java.lang.Class<ArgumentType>)EntityArgument.class, (ArgumentSerializer<ArgumentType>)new EntityArgument.Serializer());
        ArgumentTypes.<ArgumentType>register("game_profile", (java.lang.Class<ArgumentType>)GameProfileArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)GameProfileArgument::gameProfile));
        ArgumentTypes.<ArgumentType>register("block_pos", (java.lang.Class<ArgumentType>)BlockPosArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)BlockPosArgument::blockPos));
        ArgumentTypes.<ArgumentType>register("column_pos", (java.lang.Class<ArgumentType>)ColumnPosArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ColumnPosArgument::columnPos));
        ArgumentTypes.<ArgumentType>register("vec3", (java.lang.Class<ArgumentType>)Vec3Argument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)Vec3Argument::vec3));
        ArgumentTypes.<ArgumentType>register("vec2", (java.lang.Class<ArgumentType>)Vec2Argument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)Vec2Argument::vec2));
        ArgumentTypes.<ArgumentType>register("block_state", (java.lang.Class<ArgumentType>)BlockStateArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)BlockStateArgument::block));
        ArgumentTypes.<ArgumentType>register("block_predicate", (java.lang.Class<ArgumentType>)BlockPredicateArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)BlockPredicateArgument::blockPredicate));
        ArgumentTypes.<ArgumentType>register("item_stack", (java.lang.Class<ArgumentType>)ItemArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ItemArgument::item));
        ArgumentTypes.<ArgumentType>register("item_predicate", (java.lang.Class<ArgumentType>)ItemPredicateArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ItemPredicateArgument::itemPredicate));
        ArgumentTypes.<ArgumentType>register("color", (java.lang.Class<ArgumentType>)ColorArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ColorArgument::color));
        ArgumentTypes.<ArgumentType>register("component", (java.lang.Class<ArgumentType>)ComponentArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ComponentArgument::textComponent));
        ArgumentTypes.<ArgumentType>register("message", (java.lang.Class<ArgumentType>)MessageArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)MessageArgument::message));
        ArgumentTypes.<ArgumentType>register("nbt_compound_tag", (java.lang.Class<ArgumentType>)CompoundTagArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)CompoundTagArgument::compoundTag));
        ArgumentTypes.<ArgumentType>register("nbt_tag", (java.lang.Class<ArgumentType>)NbtTagArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)NbtTagArgument::nbtTag));
        ArgumentTypes.<ArgumentType>register("nbt_path", (java.lang.Class<ArgumentType>)NbtPathArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)NbtPathArgument::nbtPath));
        ArgumentTypes.<ArgumentType>register("objective", (java.lang.Class<ArgumentType>)ObjectiveArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ObjectiveArgument::objective));
        ArgumentTypes.<ArgumentType>register("objective_criteria", (java.lang.Class<ArgumentType>)ObjectiveCriteriaArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ObjectiveCriteriaArgument::criteria));
        ArgumentTypes.<ArgumentType>register("operation", (java.lang.Class<ArgumentType>)OperationArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)OperationArgument::operation));
        ArgumentTypes.<ArgumentType>register("particle", (java.lang.Class<ArgumentType>)ParticleArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ParticleArgument::particle));
        ArgumentTypes.<ArgumentType>register("angle", (java.lang.Class<ArgumentType>)AngleArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)AngleArgument::angle));
        ArgumentTypes.<ArgumentType>register("rotation", (java.lang.Class<ArgumentType>)RotationArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)RotationArgument::rotation));
        ArgumentTypes.<ArgumentType>register("scoreboard_slot", (java.lang.Class<ArgumentType>)ScoreboardSlotArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ScoreboardSlotArgument::displaySlot));
        ArgumentTypes.<ArgumentType>register("score_holder", (java.lang.Class<ArgumentType>)ScoreHolderArgument.class, (ArgumentSerializer<ArgumentType>)new ScoreHolderArgument.Serializer());
        ArgumentTypes.<ArgumentType>register("swizzle", (java.lang.Class<ArgumentType>)SwizzleArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)SwizzleArgument::swizzle));
        ArgumentTypes.<ArgumentType>register("team", (java.lang.Class<ArgumentType>)TeamArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)TeamArgument::team));
        ArgumentTypes.<ArgumentType>register("item_slot", (java.lang.Class<ArgumentType>)SlotArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)SlotArgument::slot));
        ArgumentTypes.<ArgumentType>register("resource_location", (java.lang.Class<ArgumentType>)ResourceLocationArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ResourceLocationArgument::id));
        ArgumentTypes.<ArgumentType>register("mob_effect", (java.lang.Class<ArgumentType>)MobEffectArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)MobEffectArgument::effect));
        ArgumentTypes.<ArgumentType>register("function", (java.lang.Class<ArgumentType>)FunctionArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)FunctionArgument::functions));
        ArgumentTypes.<ArgumentType>register("entity_anchor", (java.lang.Class<ArgumentType>)EntityAnchorArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)EntityAnchorArgument::anchor));
        ArgumentTypes.<ArgumentType>register("int_range", (java.lang.Class<ArgumentType>)RangeArgument.Ints.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)RangeArgument::intRange));
        ArgumentTypes.<ArgumentType>register("float_range", (java.lang.Class<ArgumentType>)RangeArgument.Floats.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)RangeArgument::floatRange));
        ArgumentTypes.<ArgumentType>register("item_enchantment", (java.lang.Class<ArgumentType>)ItemEnchantmentArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)ItemEnchantmentArgument::enchantment));
        ArgumentTypes.<ArgumentType>register("entity_summon", (java.lang.Class<ArgumentType>)EntitySummonArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)EntitySummonArgument::id));
        ArgumentTypes.<ArgumentType>register("dimension", (java.lang.Class<ArgumentType>)DimensionArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)DimensionArgument::dimension));
        ArgumentTypes.<ArgumentType>register("time", (java.lang.Class<ArgumentType>)TimeArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)TimeArgument::time));
        ArgumentTypes.<ArgumentType>register("uuid", (java.lang.Class<ArgumentType>)UuidArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)UuidArgument::uuid));
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            ArgumentTypes.<ArgumentType>register("test_argument", (java.lang.Class<ArgumentType>)TestFunctionArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)TestFunctionArgument::testFunctionArgument));
            ArgumentTypes.<ArgumentType>register("test_class", (java.lang.Class<ArgumentType>)TestClassNameArgument.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)TestClassNameArgument::testClassName));
        }
    }
    
    @Nullable
    private static Entry<?> get(final ResourceLocation vk) {
        return ArgumentTypes.BY_NAME.get(vk);
    }
    
    @Nullable
    private static Entry<?> get(final ArgumentType<?> argumentType) {
        return ArgumentTypes.BY_CLASS.get(argumentType.getClass());
    }
    
    public static <T extends ArgumentType<?>> void serialize(final FriendlyByteBuf nf, final T argumentType) {
        final Entry<T> a3 = (Entry<T>)get(argumentType);
        if (a3 == null) {
            ArgumentTypes.LOGGER.error("Could not serialize {} ({}) - will not be sent to client!", argumentType, argumentType.getClass());
            nf.writeResourceLocation(new ResourceLocation(""));
            return;
        }
        nf.writeResourceLocation(a3.name);
        a3.serializer.serializeToNetwork(argumentType, nf);
    }
    
    @Nullable
    public static ArgumentType<?> deserialize(final FriendlyByteBuf nf) {
        final ResourceLocation vk2 = nf.readResourceLocation();
        final Entry<?> a3 = get(vk2);
        if (a3 == null) {
            ArgumentTypes.LOGGER.error("Could not deserialize {}", vk2);
            return null;
        }
        return a3.serializer.deserializeFromNetwork(nf);
    }
    
    private static <T extends ArgumentType<?>> void serializeToJson(final JsonObject jsonObject, final T argumentType) {
        final Entry<T> a3 = (Entry<T>)get(argumentType);
        if (a3 == null) {
            ArgumentTypes.LOGGER.error("Could not serialize argument {} ({})!", argumentType, argumentType.getClass());
            jsonObject.addProperty("type", "unknown");
        }
        else {
            jsonObject.addProperty("type", "argument");
            jsonObject.addProperty("parser", a3.name.toString());
            final JsonObject jsonObject2 = new JsonObject();
            a3.serializer.serializeToJson(argumentType, jsonObject2);
            if (jsonObject2.size() > 0) {
                jsonObject.add("properties", (JsonElement)jsonObject2);
            }
        }
    }
    
    public static <S> JsonObject serializeNodeToJson(final CommandDispatcher<S> commandDispatcher, final CommandNode<S> commandNode) {
        final JsonObject jsonObject3 = new JsonObject();
        if (commandNode instanceof RootCommandNode) {
            jsonObject3.addProperty("type", "root");
        }
        else if (commandNode instanceof LiteralCommandNode) {
            jsonObject3.addProperty("type", "literal");
        }
        else if (commandNode instanceof ArgumentCommandNode) {
            ArgumentTypes.<ArgumentType>serializeToJson(jsonObject3, ((ArgumentCommandNode)commandNode).getType());
        }
        else {
            ArgumentTypes.LOGGER.error("Could not serialize node {} ({})!", commandNode, commandNode.getClass());
            jsonObject3.addProperty("type", "unknown");
        }
        final JsonObject jsonObject4 = new JsonObject();
        for (final CommandNode<S> commandNode2 : commandNode.getChildren()) {
            jsonObject4.add(commandNode2.getName(), (JsonElement)ArgumentTypes.serializeNodeToJson((com.mojang.brigadier.CommandDispatcher<Object>)commandDispatcher, (com.mojang.brigadier.tree.CommandNode<Object>)commandNode2));
        }
        if (jsonObject4.size() > 0) {
            jsonObject3.add("children", (JsonElement)jsonObject4);
        }
        if (commandNode.getCommand() != null) {
            jsonObject3.addProperty("executable", Boolean.valueOf(true));
        }
        if (commandNode.getRedirect() != null) {
            final Collection<String> collection5 = (Collection<String>)commandDispatcher.getPath(commandNode.getRedirect());
            if (!collection5.isEmpty()) {
                final JsonArray jsonArray6 = new JsonArray();
                for (final String string8 : collection5) {
                    jsonArray6.add(string8);
                }
                jsonObject3.add("redirect", (JsonElement)jsonArray6);
            }
        }
        return jsonObject3;
    }
    
    public static boolean isTypeRegistered(final ArgumentType<?> argumentType) {
        return get(argumentType) != null;
    }
    
    public static <T> Set<ArgumentType<?>> findUsedArgumentTypes(final CommandNode<T> commandNode) {
        final Set<CommandNode<T>> set2 = (Set<CommandNode<T>>)Sets.newIdentityHashSet();
        final Set<ArgumentType<?>> set3 = (Set<ArgumentType<?>>)Sets.newHashSet();
        ArgumentTypes.<T>findUsedArgumentTypes(commandNode, set3, set2);
        return set3;
    }
    
    private static <T> void findUsedArgumentTypes(final CommandNode<T> commandNode, final Set<ArgumentType<?>> set2, final Set<CommandNode<T>> set3) {
        if (!set3.add(commandNode)) {
            return;
        }
        if (commandNode instanceof ArgumentCommandNode) {
            set2.add(((ArgumentCommandNode)commandNode).getType());
        }
        commandNode.getChildren().forEach(commandNode -> ArgumentTypes.findUsedArgumentTypes((com.mojang.brigadier.tree.CommandNode<Object>)commandNode, set2, (java.util.Set<com.mojang.brigadier.tree.CommandNode<Object>>)set3));
        final CommandNode<T> commandNode2 = (CommandNode<T>)commandNode.getRedirect();
        if (commandNode2 != null) {
            ArgumentTypes.findUsedArgumentTypes((com.mojang.brigadier.tree.CommandNode<Object>)commandNode2, set2, (java.util.Set<com.mojang.brigadier.tree.CommandNode<Object>>)set3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BY_CLASS = (Map)Maps.newHashMap();
        BY_NAME = (Map)Maps.newHashMap();
    }
    
    static class Entry<T extends ArgumentType<?>> {
        public final Class<T> clazz;
        public final ArgumentSerializer<T> serializer;
        public final ResourceLocation name;
        
        private Entry(final Class<T> class1, final ArgumentSerializer<T> fj, final ResourceLocation vk) {
            this.clazz = class1;
            this.serializer = fj;
            this.name = vk;
        }
    }
}
