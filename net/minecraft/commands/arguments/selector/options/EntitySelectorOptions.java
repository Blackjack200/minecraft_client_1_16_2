package net.minecraft.commands.arguments.selector.options;

import net.minecraft.network.chat.TranslatableComponent;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.advancements.critereon.WrappedMinMaxBounds;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Team;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.Objects;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.AdvancementProgress;
import com.mojang.brigadier.StringReader;
import com.google.common.collect.Maps;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import com.mojang.brigadier.Message;
import java.util.Locale;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import java.util.function.Predicate;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Map;

public class EntitySelectorOptions {
    private static final Map<String, Option> OPTIONS;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_OPTION;
    public static final DynamicCommandExceptionType ERROR_INAPPLICABLE_OPTION;
    public static final SimpleCommandExceptionType ERROR_RANGE_NEGATIVE;
    public static final SimpleCommandExceptionType ERROR_LEVEL_NEGATIVE;
    public static final SimpleCommandExceptionType ERROR_LIMIT_TOO_SMALL;
    public static final DynamicCommandExceptionType ERROR_SORT_UNKNOWN;
    public static final DynamicCommandExceptionType ERROR_GAME_MODE_INVALID;
    public static final DynamicCommandExceptionType ERROR_ENTITY_TYPE_INVALID;
    
    private static void register(final String string, final Modifier a, final Predicate<EntitySelectorParser> predicate, final Component nr) {
        EntitySelectorOptions.OPTIONS.put(string, new Option(a, (Predicate)predicate, nr));
    }
    
    public static void bootStrap() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokeinterface java/util/Map.isEmpty:()Z
        //     8: ifne            12
        //    11: return         
        //    12: ldc             "name"
        //    14: invokedynamic   BootstrapMethod #0, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //    19: invokedynamic   BootstrapMethod #1, test:()Ljava/util/function/Predicate;
        //    24: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    27: dup            
        //    28: ldc             "argument.entity.options.name.description"
        //    30: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    33: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //    36: ldc             "distance"
        //    38: invokedynamic   BootstrapMethod #2, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //    43: invokedynamic   BootstrapMethod #3, test:()Ljava/util/function/Predicate;
        //    48: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    51: dup            
        //    52: ldc             "argument.entity.options.distance.description"
        //    54: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    57: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //    60: ldc             "level"
        //    62: invokedynamic   BootstrapMethod #4, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //    67: invokedynamic   BootstrapMethod #5, test:()Ljava/util/function/Predicate;
        //    72: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    75: dup            
        //    76: ldc             "argument.entity.options.level.description"
        //    78: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    81: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //    84: ldc             "x"
        //    86: invokedynamic   BootstrapMethod #6, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //    91: invokedynamic   BootstrapMethod #7, test:()Ljava/util/function/Predicate;
        //    96: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    99: dup            
        //   100: ldc             "argument.entity.options.x.description"
        //   102: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   105: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   108: ldc             "y"
        //   110: invokedynamic   BootstrapMethod #8, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   115: invokedynamic   BootstrapMethod #9, test:()Ljava/util/function/Predicate;
        //   120: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   123: dup            
        //   124: ldc             "argument.entity.options.y.description"
        //   126: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   129: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   132: ldc             "z"
        //   134: invokedynamic   BootstrapMethod #10, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   139: invokedynamic   BootstrapMethod #11, test:()Ljava/util/function/Predicate;
        //   144: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   147: dup            
        //   148: ldc             "argument.entity.options.z.description"
        //   150: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   153: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   156: ldc             "dx"
        //   158: invokedynamic   BootstrapMethod #12, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   163: invokedynamic   BootstrapMethod #13, test:()Ljava/util/function/Predicate;
        //   168: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   171: dup            
        //   172: ldc             "argument.entity.options.dx.description"
        //   174: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   177: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   180: ldc             "dy"
        //   182: invokedynamic   BootstrapMethod #14, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   187: invokedynamic   BootstrapMethod #15, test:()Ljava/util/function/Predicate;
        //   192: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   195: dup            
        //   196: ldc             "argument.entity.options.dy.description"
        //   198: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   201: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   204: ldc             "dz"
        //   206: invokedynamic   BootstrapMethod #16, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   211: invokedynamic   BootstrapMethod #17, test:()Ljava/util/function/Predicate;
        //   216: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   219: dup            
        //   220: ldc             "argument.entity.options.dz.description"
        //   222: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   225: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   228: ldc             "x_rotation"
        //   230: invokedynamic   BootstrapMethod #18, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   235: invokedynamic   BootstrapMethod #19, test:()Ljava/util/function/Predicate;
        //   240: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   243: dup            
        //   244: ldc             "argument.entity.options.x_rotation.description"
        //   246: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   249: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   252: ldc             "y_rotation"
        //   254: invokedynamic   BootstrapMethod #20, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   259: invokedynamic   BootstrapMethod #21, test:()Ljava/util/function/Predicate;
        //   264: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   267: dup            
        //   268: ldc_w           "argument.entity.options.y_rotation.description"
        //   271: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   274: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   277: ldc_w           "limit"
        //   280: invokedynamic   BootstrapMethod #22, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   285: invokedynamic   BootstrapMethod #23, test:()Ljava/util/function/Predicate;
        //   290: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   293: dup            
        //   294: ldc_w           "argument.entity.options.limit.description"
        //   297: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   300: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   303: ldc_w           "sort"
        //   306: invokedynamic   BootstrapMethod #24, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   311: invokedynamic   BootstrapMethod #25, test:()Ljava/util/function/Predicate;
        //   316: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   319: dup            
        //   320: ldc_w           "argument.entity.options.sort.description"
        //   323: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   326: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   329: ldc_w           "gamemode"
        //   332: invokedynamic   BootstrapMethod #26, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   337: invokedynamic   BootstrapMethod #27, test:()Ljava/util/function/Predicate;
        //   342: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   345: dup            
        //   346: ldc_w           "argument.entity.options.gamemode.description"
        //   349: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   352: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   355: ldc_w           "team"
        //   358: invokedynamic   BootstrapMethod #28, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   363: invokedynamic   BootstrapMethod #29, test:()Ljava/util/function/Predicate;
        //   368: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   371: dup            
        //   372: ldc_w           "argument.entity.options.team.description"
        //   375: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   378: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   381: ldc_w           "type"
        //   384: invokedynamic   BootstrapMethod #30, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   389: invokedynamic   BootstrapMethod #31, test:()Ljava/util/function/Predicate;
        //   394: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   397: dup            
        //   398: ldc_w           "argument.entity.options.type.description"
        //   401: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   404: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   407: ldc_w           "tag"
        //   410: invokedynamic   BootstrapMethod #32, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   415: invokedynamic   BootstrapMethod #33, test:()Ljava/util/function/Predicate;
        //   420: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   423: dup            
        //   424: ldc_w           "argument.entity.options.tag.description"
        //   427: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   430: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   433: ldc_w           "nbt"
        //   436: invokedynamic   BootstrapMethod #34, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   441: invokedynamic   BootstrapMethod #35, test:()Ljava/util/function/Predicate;
        //   446: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   449: dup            
        //   450: ldc_w           "argument.entity.options.nbt.description"
        //   453: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   456: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   459: ldc_w           "scores"
        //   462: invokedynamic   BootstrapMethod #36, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   467: invokedynamic   BootstrapMethod #37, test:()Ljava/util/function/Predicate;
        //   472: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   475: dup            
        //   476: ldc_w           "argument.entity.options.scores.description"
        //   479: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   482: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   485: ldc_w           "advancements"
        //   488: invokedynamic   BootstrapMethod #38, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   493: invokedynamic   BootstrapMethod #39, test:()Ljava/util/function/Predicate;
        //   498: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   501: dup            
        //   502: ldc_w           "argument.entity.options.advancements.description"
        //   505: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   508: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   511: ldc_w           "predicate"
        //   514: invokedynamic   BootstrapMethod #40, handle:()Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;
        //   519: invokedynamic   BootstrapMethod #41, test:()Ljava/util/function/Predicate;
        //   524: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   527: dup            
        //   528: ldc_w           "argument.entity.options.predicate.description"
        //   531: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   534: invokestatic    net/minecraft/commands/arguments/selector/options/EntitySelectorOptions.register:(Ljava/lang/String;Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Modifier;Ljava/util/function/Predicate;Lnet/minecraft/network/chat/Component;)V
        //   537: return         
        //    StackMapTable: 00 01 0C
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.execLocalTasks(ForkJoinPool.java:1040)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1058)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Modifier get(final EntitySelectorParser fd, final String string, final int integer) throws CommandSyntaxException {
        final Option b4 = (Option)EntitySelectorOptions.OPTIONS.get(string);
        if (b4 == null) {
            fd.getReader().setCursor(integer);
            throw EntitySelectorOptions.ERROR_UNKNOWN_OPTION.createWithContext((ImmutableStringReader)fd.getReader(), string);
        }
        if (b4.predicate.test(fd)) {
            return b4.modifier;
        }
        throw EntitySelectorOptions.ERROR_INAPPLICABLE_OPTION.createWithContext((ImmutableStringReader)fd.getReader(), string);
    }
    
    public static void suggestNames(final EntitySelectorParser fd, final SuggestionsBuilder suggestionsBuilder) {
        final String string3 = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
        for (final Map.Entry<String, Option> entry5 : EntitySelectorOptions.OPTIONS.entrySet()) {
            if (((Option)entry5.getValue()).predicate.test(fd) && ((String)entry5.getKey()).toLowerCase(Locale.ROOT).startsWith(string3)) {
                suggestionsBuilder.suggest((String)entry5.getKey() + '=', (Message)((Option)entry5.getValue()).description);
            }
        }
    }
    
    static {
        OPTIONS = (Map)Maps.newHashMap();
        ERROR_UNKNOWN_OPTION = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.options.unknown", new Object[] { object }));
        ERROR_INAPPLICABLE_OPTION = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.options.inapplicable", new Object[] { object }));
        ERROR_RANGE_NEGATIVE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.distance.negative"));
        ERROR_LEVEL_NEGATIVE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.level.negative"));
        ERROR_LIMIT_TOO_SMALL = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.limit.toosmall"));
        ERROR_SORT_UNKNOWN = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.options.sort.irreversible", new Object[] { object }));
        ERROR_GAME_MODE_INVALID = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.options.mode.invalid", new Object[] { object }));
        ERROR_ENTITY_TYPE_INVALID = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.options.type.invalid", new Object[] { object }));
    }
    
    static class Option {
        public final Modifier modifier;
        public final Predicate<EntitySelectorParser> predicate;
        public final Component description;
        
        private Option(final Modifier a, final Predicate<EntitySelectorParser> predicate, final Component nr) {
            this.modifier = a;
            this.predicate = predicate;
            this.description = nr;
        }
    }
    
    public interface Modifier {
        void handle(final EntitySelectorParser fd) throws CommandSyntaxException;
    }
}
