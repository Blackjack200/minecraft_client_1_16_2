package net.minecraft.world.level;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.function.BiConsumer;
import java.util.function.Function;
import com.mojang.brigadier.arguments.ArgumentType;
import java.util.function.Supplier;
import com.google.common.collect.Maps;
import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import java.util.Iterator;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DynamicLike;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class GameRules {
    private static final Logger LOGGER;
    private static final Map<Key<?>, Type<?>> GAME_RULE_TYPES;
    public static final Key<BooleanValue> RULE_DOFIRETICK;
    public static final Key<BooleanValue> RULE_MOBGRIEFING;
    public static final Key<BooleanValue> RULE_KEEPINVENTORY;
    public static final Key<BooleanValue> RULE_DOMOBSPAWNING;
    public static final Key<BooleanValue> RULE_DOMOBLOOT;
    public static final Key<BooleanValue> RULE_DOBLOCKDROPS;
    public static final Key<BooleanValue> RULE_DOENTITYDROPS;
    public static final Key<BooleanValue> RULE_COMMANDBLOCKOUTPUT;
    public static final Key<BooleanValue> RULE_NATURAL_REGENERATION;
    public static final Key<BooleanValue> RULE_DAYLIGHT;
    public static final Key<BooleanValue> RULE_LOGADMINCOMMANDS;
    public static final Key<BooleanValue> RULE_SHOWDEATHMESSAGES;
    public static final Key<IntegerValue> RULE_RANDOMTICKING;
    public static final Key<BooleanValue> RULE_SENDCOMMANDFEEDBACK;
    public static final Key<BooleanValue> RULE_REDUCEDDEBUGINFO;
    public static final Key<BooleanValue> RULE_SPECTATORSGENERATECHUNKS;
    public static final Key<IntegerValue> RULE_SPAWN_RADIUS;
    public static final Key<BooleanValue> RULE_DISABLE_ELYTRA_MOVEMENT_CHECK;
    public static final Key<IntegerValue> RULE_MAX_ENTITY_CRAMMING;
    public static final Key<BooleanValue> RULE_WEATHER_CYCLE;
    public static final Key<BooleanValue> RULE_LIMITED_CRAFTING;
    public static final Key<IntegerValue> RULE_MAX_COMMAND_CHAIN_LENGTH;
    public static final Key<BooleanValue> RULE_ANNOUNCE_ADVANCEMENTS;
    public static final Key<BooleanValue> RULE_DISABLE_RAIDS;
    public static final Key<BooleanValue> RULE_DOINSOMNIA;
    public static final Key<BooleanValue> RULE_DO_IMMEDIATE_RESPAWN;
    public static final Key<BooleanValue> RULE_DROWNING_DAMAGE;
    public static final Key<BooleanValue> RULE_FALL_DAMAGE;
    public static final Key<BooleanValue> RULE_FIRE_DAMAGE;
    public static final Key<BooleanValue> RULE_DO_PATROL_SPAWNING;
    public static final Key<BooleanValue> RULE_DO_TRADER_SPAWNING;
    public static final Key<BooleanValue> RULE_FORGIVE_DEAD_PLAYERS;
    public static final Key<BooleanValue> RULE_UNIVERSAL_ANGER;
    private final Map<Key<?>, Value<?>> rules;
    
    private static <T extends Value<T>> Key<T> register(final String string, final Category b, final Type<T> f) {
        final Key<T> e4 = new Key<T>(string, b);
        final Type<?> f2 = GameRules.GAME_RULE_TYPES.put(e4, f);
        if (f2 != null) {
            throw new IllegalStateException("Duplicate game rule registration for " + string);
        }
        return e4;
    }
    
    public GameRules(final DynamicLike<?> dynamicLike) {
        this();
        this.loadFromTag(dynamicLike);
    }
    
    public GameRules() {
        this.rules = (Map<Key<?>, Value<?>>)GameRules.GAME_RULE_TYPES.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((Type)entry.getValue()).createRule()));
    }
    
    private GameRules(final Map<Key<?>, Value<?>> map) {
        this.rules = map;
    }
    
    public <T extends Value<T>> T getRule(final Key<T> e) {
        return (T)this.rules.get(e);
    }
    
    public CompoundTag createTag() {
        final CompoundTag md2 = new CompoundTag();
        this.rules.forEach((e, g) -> md2.putString(e.id, g.serialize()));
        return md2;
    }
    
    private void loadFromTag(final DynamicLike<?> dynamicLike) {
        this.rules.forEach((e, g) -> dynamicLike.get(e.id).asString().result().ifPresent(g::deserialize));
    }
    
    public GameRules copy() {
        return new GameRules((Map<Key<?>, Value<?>>)this.rules.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((Value)entry.getValue()).copy())));
    }
    
    public static void visitGameRuleTypes(final GameRuleTypeVisitor c) {
        GameRules.GAME_RULE_TYPES.forEach((e, f) -> GameRules.<Value>callVisitorCap(c, e, f));
    }
    
    private static <T extends Value<T>> void callVisitorCap(final GameRuleTypeVisitor c, final Key<?> e, final Type<?> f) {
        final Key<T> e2 = (Key<T>)e;
        final Type<T> f2 = (Type<T>)f;
        c.<T>visit(e2, f2);
        f2.callVisitor(c, e2);
    }
    
    public void assignFrom(final GameRules brq, @Nullable final MinecraftServer minecraftServer) {
        brq.rules.keySet().forEach(e -> this.<Value>assignCap((Key<Value>)e, brq, minecraftServer));
    }
    
    private <T extends Value<T>> void assignCap(final Key<T> e, final GameRules brq, @Nullable final MinecraftServer minecraftServer) {
        final T g5 = brq.<T>getRule(e);
        this.<T>getRule(e).setFrom(g5, minecraftServer);
    }
    
    public boolean getBoolean(final Key<BooleanValue> e) {
        return this.<BooleanValue>getRule(e).get();
    }
    
    public int getInt(final Key<IntegerValue> e) {
        return this.<IntegerValue>getRule(e).get();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GAME_RULE_TYPES = (Map)Maps.newTreeMap(Comparator.comparing(e -> e.id));
        RULE_DOFIRETICK = GameRules.<BooleanValue>register("doFireTick", Category.UPDATES, (Type<BooleanValue>)create(true));
        RULE_MOBGRIEFING = GameRules.<BooleanValue>register("mobGriefing", Category.MOBS, (Type<BooleanValue>)create(true));
        RULE_KEEPINVENTORY = GameRules.<BooleanValue>register("keepInventory", Category.PLAYER, (Type<BooleanValue>)create(false));
        RULE_DOMOBSPAWNING = GameRules.<BooleanValue>register("doMobSpawning", Category.SPAWNING, (Type<BooleanValue>)create(true));
        RULE_DOMOBLOOT = GameRules.<BooleanValue>register("doMobLoot", Category.DROPS, (Type<BooleanValue>)create(true));
        RULE_DOBLOCKDROPS = GameRules.<BooleanValue>register("doTileDrops", Category.DROPS, (Type<BooleanValue>)create(true));
        RULE_DOENTITYDROPS = GameRules.<BooleanValue>register("doEntityDrops", Category.DROPS, (Type<BooleanValue>)create(true));
        RULE_COMMANDBLOCKOUTPUT = GameRules.<BooleanValue>register("commandBlockOutput", Category.CHAT, (Type<BooleanValue>)create(true));
        RULE_NATURAL_REGENERATION = GameRules.<BooleanValue>register("naturalRegeneration", Category.PLAYER, (Type<BooleanValue>)create(true));
        RULE_DAYLIGHT = GameRules.<BooleanValue>register("doDaylightCycle", Category.UPDATES, (Type<BooleanValue>)create(true));
        RULE_LOGADMINCOMMANDS = GameRules.<BooleanValue>register("logAdminCommands", Category.CHAT, (Type<BooleanValue>)create(true));
        RULE_SHOWDEATHMESSAGES = GameRules.<BooleanValue>register("showDeathMessages", Category.CHAT, (Type<BooleanValue>)create(true));
        RULE_RANDOMTICKING = GameRules.<IntegerValue>register("randomTickSpeed", Category.UPDATES, (Type<IntegerValue>)create(3));
        RULE_SENDCOMMANDFEEDBACK = GameRules.<BooleanValue>register("sendCommandFeedback", Category.CHAT, (Type<BooleanValue>)create(true));
        RULE_REDUCEDDEBUGINFO = GameRules.<BooleanValue>register("reducedDebugInfo", Category.MISC, (Type<BooleanValue>)create(false, (BiConsumer<MinecraftServer, BooleanValue>)((minecraftServer, a) -> {
            final byte byte3 = (byte)(a.get() ? 22 : 23);
            for (final ServerPlayer aah5 : minecraftServer.getPlayerList().getPlayers()) {
                aah5.connection.send(new ClientboundEntityEventPacket(aah5, byte3));
            }
        })));
        RULE_SPECTATORSGENERATECHUNKS = GameRules.<BooleanValue>register("spectatorsGenerateChunks", Category.PLAYER, (Type<BooleanValue>)create(true));
        RULE_SPAWN_RADIUS = GameRules.<IntegerValue>register("spawnRadius", Category.PLAYER, (Type<IntegerValue>)create(10));
        RULE_DISABLE_ELYTRA_MOVEMENT_CHECK = GameRules.<BooleanValue>register("disableElytraMovementCheck", Category.PLAYER, (Type<BooleanValue>)create(false));
        RULE_MAX_ENTITY_CRAMMING = GameRules.<IntegerValue>register("maxEntityCramming", Category.MOBS, (Type<IntegerValue>)create(24));
        RULE_WEATHER_CYCLE = GameRules.<BooleanValue>register("doWeatherCycle", Category.UPDATES, (Type<BooleanValue>)create(true));
        RULE_LIMITED_CRAFTING = GameRules.<BooleanValue>register("doLimitedCrafting", Category.PLAYER, (Type<BooleanValue>)create(false));
        RULE_MAX_COMMAND_CHAIN_LENGTH = GameRules.<IntegerValue>register("maxCommandChainLength", Category.MISC, (Type<IntegerValue>)create(65536));
        RULE_ANNOUNCE_ADVANCEMENTS = GameRules.<BooleanValue>register("announceAdvancements", Category.CHAT, (Type<BooleanValue>)create(true));
        RULE_DISABLE_RAIDS = GameRules.<BooleanValue>register("disableRaids", Category.MOBS, (Type<BooleanValue>)create(false));
        RULE_DOINSOMNIA = GameRules.<BooleanValue>register("doInsomnia", Category.SPAWNING, (Type<BooleanValue>)create(true));
        RULE_DO_IMMEDIATE_RESPAWN = GameRules.<BooleanValue>register("doImmediateRespawn", Category.PLAYER, (Type<BooleanValue>)create(false, (BiConsumer<MinecraftServer, BooleanValue>)((minecraftServer, a) -> {
            for (final ServerPlayer aah4 : minecraftServer.getPlayerList().getPlayers()) {
                aah4.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.IMMEDIATE_RESPAWN, a.get() ? 1.0f : 0.0f));
            }
        })));
        RULE_DROWNING_DAMAGE = GameRules.<BooleanValue>register("drowningDamage", Category.PLAYER, (Type<BooleanValue>)create(true));
        RULE_FALL_DAMAGE = GameRules.<BooleanValue>register("fallDamage", Category.PLAYER, (Type<BooleanValue>)create(true));
        RULE_FIRE_DAMAGE = GameRules.<BooleanValue>register("fireDamage", Category.PLAYER, (Type<BooleanValue>)create(true));
        RULE_DO_PATROL_SPAWNING = GameRules.<BooleanValue>register("doPatrolSpawning", Category.SPAWNING, (Type<BooleanValue>)create(true));
        RULE_DO_TRADER_SPAWNING = GameRules.<BooleanValue>register("doTraderSpawning", Category.SPAWNING, (Type<BooleanValue>)create(true));
        RULE_FORGIVE_DEAD_PLAYERS = GameRules.<BooleanValue>register("forgiveDeadPlayers", Category.MOBS, (Type<BooleanValue>)create(true));
        RULE_UNIVERSAL_ANGER = GameRules.<BooleanValue>register("universalAnger", Category.MOBS, (Type<BooleanValue>)create(false));
    }
    
    public enum Category {
        PLAYER("gamerule.category.player"), 
        MOBS("gamerule.category.mobs"), 
        SPAWNING("gamerule.category.spawning"), 
        DROPS("gamerule.category.drops"), 
        UPDATES("gamerule.category.updates"), 
        CHAT("gamerule.category.chat"), 
        MISC("gamerule.category.misc");
        
        private final String descriptionId;
        
        private Category(final String string3) {
            this.descriptionId = string3;
        }
        
        public String getDescriptionId() {
            return this.descriptionId;
        }
    }
    
    public interface GameRuleTypeVisitor {
        default <T extends Value<T>> void visit(final Key<T> e, final Type<T> f) {
        }
        
        default void visitBoolean(final Key<BooleanValue> e, final Type<BooleanValue> f) {
        }
        
        default void visitInteger(final Key<IntegerValue> e, final Type<IntegerValue> f) {
        }
    }
    
    public static final class Key<T extends Value<T>> {
        private final String id;
        private final Category category;
        
        public Key(final String string, final Category b) {
            this.id = string;
            this.category = b;
        }
        
        public String toString() {
            return this.id;
        }
        
        public boolean equals(final Object object) {
            return this == object || (object instanceof Key && ((Key)object).id.equals(this.id));
        }
        
        public int hashCode() {
            return this.id.hashCode();
        }
        
        public String getId() {
            return this.id;
        }
        
        public String getDescriptionId() {
            return "gamerule." + this.id;
        }
        
        public Category getCategory() {
            return this.category;
        }
    }
    
    public static final class Key<T extends Value<T>> {
        private final String id;
        private final Category category;
        
        public Key(final String string, final Category b) {
            this.id = string;
            this.category = b;
        }
        
        public String toString() {
            return this.id;
        }
        
        public boolean equals(final Object object) {
            return this == object || (object instanceof Key && ((Key)object).id.equals(this.id));
        }
        
        public int hashCode() {
            return this.id.hashCode();
        }
        
        public String getId() {
            return this.id;
        }
        
        public String getDescriptionId() {
            return "gamerule." + this.id;
        }
        
        public Category getCategory() {
            return this.category;
        }
    }
    
    public static class Type<T extends Value<T>> {
        private final Supplier<ArgumentType<?>> argument;
        private final Function<Type<T>, T> constructor;
        private final BiConsumer<MinecraftServer, T> callback;
        private final VisitorCaller<T> visitorCaller;
        
        private Type(final Supplier<ArgumentType<?>> supplier, final Function<Type<T>, T> function, final BiConsumer<MinecraftServer, T> biConsumer, final VisitorCaller<T> h) {
            this.argument = supplier;
            this.constructor = function;
            this.callback = biConsumer;
            this.visitorCaller = h;
        }
        
        public RequiredArgumentBuilder<CommandSourceStack, ?> createArgument(final String string) {
            return Commands.argument(string, (com.mojang.brigadier.arguments.ArgumentType<?>)this.argument.get());
        }
        
        public T createRule() {
            return (T)this.constructor.apply(this);
        }
        
        public void callVisitor(final GameRuleTypeVisitor c, final Key<T> e) {
            this.visitorCaller.call(c, e, this);
        }
    }
    
    public static class Type<T extends Value<T>> {
        private final Supplier<ArgumentType<?>> argument;
        private final Function<Type<T>, T> constructor;
        private final BiConsumer<MinecraftServer, T> callback;
        private final VisitorCaller<T> visitorCaller;
        
        private Type(final Supplier<ArgumentType<?>> supplier, final Function<Type<T>, T> function, final BiConsumer<MinecraftServer, T> biConsumer, final VisitorCaller<T> h) {
            this.argument = supplier;
            this.constructor = function;
            this.callback = biConsumer;
            this.visitorCaller = h;
        }
        
        public RequiredArgumentBuilder<CommandSourceStack, ?> createArgument(final String string) {
            return Commands.argument(string, (com.mojang.brigadier.arguments.ArgumentType<?>)this.argument.get());
        }
        
        public T createRule() {
            return (T)this.constructor.apply(this);
        }
        
        public void callVisitor(final GameRuleTypeVisitor c, final Key<T> e) {
            this.visitorCaller.call(c, e, this);
        }
    }
    
    public static class Type<T extends Value<T>> {
        private final Supplier<ArgumentType<?>> argument;
        private final Function<Type<T>, T> constructor;
        private final BiConsumer<MinecraftServer, T> callback;
        private final VisitorCaller<T> visitorCaller;
        
        private Type(final Supplier<ArgumentType<?>> supplier, final Function<Type<T>, T> function, final BiConsumer<MinecraftServer, T> biConsumer, final VisitorCaller<T> h) {
            this.argument = supplier;
            this.constructor = function;
            this.callback = biConsumer;
            this.visitorCaller = h;
        }
        
        public RequiredArgumentBuilder<CommandSourceStack, ?> createArgument(final String string) {
            return Commands.argument(string, (com.mojang.brigadier.arguments.ArgumentType<?>)this.argument.get());
        }
        
        public T createRule() {
            return (T)this.constructor.apply(this);
        }
        
        public void callVisitor(final GameRuleTypeVisitor c, final Key<T> e) {
            this.visitorCaller.call(c, e, this);
        }
    }
    
    public abstract static class Value<T extends Value<T>> {
        protected final Type<T> type;
        
        public Value(final Type<T> f) {
            this.type = f;
        }
        
        protected abstract void updateFromArgument(final CommandContext<CommandSourceStack> commandContext, final String string);
        
        public void setFromArgument(final CommandContext<CommandSourceStack> commandContext, final String string) {
            this.updateFromArgument(commandContext, string);
            this.onChanged(((CommandSourceStack)commandContext.getSource()).getServer());
        }
        
        protected void onChanged(@Nullable final MinecraftServer minecraftServer) {
            if (minecraftServer != null) {
                ((Type<Value>)this.type).callback.accept(minecraftServer, this.getSelf());
            }
        }
        
        protected abstract void deserialize(final String string);
        
        public abstract String serialize();
        
        public String toString() {
            return this.serialize();
        }
        
        public abstract int getCommandResult();
        
        protected abstract T getSelf();
        
        protected abstract T copy();
        
        public abstract void setFrom(final T g, @Nullable final MinecraftServer minecraftServer);
    }
    
    public static class IntegerValue extends Value<IntegerValue> {
        private int value;
        
        private static Type<IntegerValue> create(final int integer, final BiConsumer<MinecraftServer, IntegerValue> biConsumer) {
            return new Type<IntegerValue>(IntegerArgumentType::integer, f -> new IntegerValue(f, integer), (BiConsumer)biConsumer, GameRuleTypeVisitor::visitInteger);
        }
        
        private static Type<IntegerValue> create(final int integer) {
            return create(integer, (BiConsumer<MinecraftServer, IntegerValue>)((minecraftServer, d) -> {}));
        }
        
        public IntegerValue(final Type<IntegerValue> f, final int integer) {
            super(f);
            this.value = integer;
        }
        
        @Override
        protected void updateFromArgument(final CommandContext<CommandSourceStack> commandContext, final String string) {
            this.value = IntegerArgumentType.getInteger((CommandContext)commandContext, string);
        }
        
        public int get() {
            return this.value;
        }
        
        @Override
        public String serialize() {
            return Integer.toString(this.value);
        }
        
        @Override
        protected void deserialize(final String string) {
            this.value = safeParse(string);
        }
        
        public boolean tryDeserialize(final String string) {
            try {
                this.value = Integer.parseInt(string);
                return true;
            }
            catch (NumberFormatException ex) {
                return false;
            }
        }
        
        private static int safeParse(final String string) {
            if (!string.isEmpty()) {
                try {
                    return Integer.parseInt(string);
                }
                catch (NumberFormatException numberFormatException2) {
                    GameRules.LOGGER.warn("Failed to parse integer {}", string);
                }
            }
            return 0;
        }
        
        @Override
        public int getCommandResult() {
            return this.value;
        }
        
        @Override
        protected IntegerValue getSelf() {
            return this;
        }
        
        @Override
        protected IntegerValue copy() {
            return new IntegerValue((Type<IntegerValue>)this.type, this.value);
        }
        
        @Override
        public void setFrom(final IntegerValue d, @Nullable final MinecraftServer minecraftServer) {
            this.value = d.value;
            this.onChanged(minecraftServer);
        }
    }
    
    public static class BooleanValue extends Value<BooleanValue> {
        private boolean value;
        
        private static Type<BooleanValue> create(final boolean boolean1, final BiConsumer<MinecraftServer, BooleanValue> biConsumer) {
            return new Type<BooleanValue>(BoolArgumentType::bool, f -> new BooleanValue(f, boolean1), (BiConsumer)biConsumer, GameRuleTypeVisitor::visitBoolean);
        }
        
        private static Type<BooleanValue> create(final boolean boolean1) {
            return create(boolean1, (BiConsumer<MinecraftServer, BooleanValue>)((minecraftServer, a) -> {}));
        }
        
        public BooleanValue(final Type<BooleanValue> f, final boolean boolean2) {
            super(f);
            this.value = boolean2;
        }
        
        @Override
        protected void updateFromArgument(final CommandContext<CommandSourceStack> commandContext, final String string) {
            this.value = BoolArgumentType.getBool((CommandContext)commandContext, string);
        }
        
        public boolean get() {
            return this.value;
        }
        
        public void set(final boolean boolean1, @Nullable final MinecraftServer minecraftServer) {
            this.value = boolean1;
            this.onChanged(minecraftServer);
        }
        
        @Override
        public String serialize() {
            return Boolean.toString(this.value);
        }
        
        @Override
        protected void deserialize(final String string) {
            this.value = Boolean.parseBoolean(string);
        }
        
        @Override
        public int getCommandResult() {
            return this.value ? 1 : 0;
        }
        
        @Override
        protected BooleanValue getSelf() {
            return this;
        }
        
        @Override
        protected BooleanValue copy() {
            return new BooleanValue((Type<BooleanValue>)this.type, this.value);
        }
        
        @Override
        public void setFrom(final BooleanValue a, @Nullable final MinecraftServer minecraftServer) {
            this.value = a.value;
            this.onChanged(minecraftServer);
        }
    }
    
    interface VisitorCaller<T extends Value<T>> {
        void call(final GameRuleTypeVisitor c, final Key<T> e, final Type<T> f);
    }
    
    interface VisitorCaller<T extends Value<T>> {
        void call(final GameRuleTypeVisitor c, final Key<T> e, final Type<T> f);
    }
}
