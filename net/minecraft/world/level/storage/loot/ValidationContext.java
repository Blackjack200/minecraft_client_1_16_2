package net.minecraft.world.level.storage.loot;

import javax.annotation.Nullable;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.HashMultimap;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import java.util.function.Supplier;
import com.google.common.collect.Multimap;

public class ValidationContext {
    private final Multimap<String, String> problems;
    private final Supplier<String> context;
    private final LootContextParamSet params;
    private final Function<ResourceLocation, LootItemCondition> conditionResolver;
    private final Set<ResourceLocation> visitedConditions;
    private final Function<ResourceLocation, LootTable> tableResolver;
    private final Set<ResourceLocation> visitedTables;
    private String contextCache;
    
    public ValidationContext(final LootContextParamSet dax, final Function<ResourceLocation, LootItemCondition> function2, final Function<ResourceLocation, LootTable> function3) {
        this((Multimap<String, String>)HashMultimap.create(), (Supplier<String>)(() -> ""), dax, function2, (Set<ResourceLocation>)ImmutableSet.of(), function3, (Set<ResourceLocation>)ImmutableSet.of());
    }
    
    public ValidationContext(final Multimap<String, String> multimap, final Supplier<String> supplier, final LootContextParamSet dax, final Function<ResourceLocation, LootItemCondition> function4, final Set<ResourceLocation> set5, final Function<ResourceLocation, LootTable> function6, final Set<ResourceLocation> set7) {
        this.problems = multimap;
        this.context = supplier;
        this.params = dax;
        this.conditionResolver = function4;
        this.visitedConditions = set5;
        this.tableResolver = function6;
        this.visitedTables = set7;
    }
    
    private String getContext() {
        if (this.contextCache == null) {
            this.contextCache = (String)this.context.get();
        }
        return this.contextCache;
    }
    
    public void reportProblem(final String string) {
        this.problems.put(this.getContext(), string);
    }
    
    public ValidationContext forChild(final String string) {
        return new ValidationContext(this.problems, (Supplier<String>)(() -> this.getContext() + string), this.params, this.conditionResolver, this.visitedConditions, this.tableResolver, this.visitedTables);
    }
    
    public ValidationContext enterTable(final String string, final ResourceLocation vk) {
        final ImmutableSet<ResourceLocation> immutableSet4 = (ImmutableSet<ResourceLocation>)ImmutableSet.builder().addAll((Iterable)this.visitedTables).add(vk).build();
        return new ValidationContext(this.problems, (Supplier<String>)(() -> this.getContext() + string), this.params, this.conditionResolver, this.visitedConditions, this.tableResolver, (Set<ResourceLocation>)immutableSet4);
    }
    
    public ValidationContext enterCondition(final String string, final ResourceLocation vk) {
        final ImmutableSet<ResourceLocation> immutableSet4 = (ImmutableSet<ResourceLocation>)ImmutableSet.builder().addAll((Iterable)this.visitedConditions).add(vk).build();
        return new ValidationContext(this.problems, (Supplier<String>)(() -> this.getContext() + string), this.params, this.conditionResolver, (Set<ResourceLocation>)immutableSet4, this.tableResolver, this.visitedTables);
    }
    
    public boolean hasVisitedTable(final ResourceLocation vk) {
        return this.visitedTables.contains(vk);
    }
    
    public boolean hasVisitedCondition(final ResourceLocation vk) {
        return this.visitedConditions.contains(vk);
    }
    
    public Multimap<String, String> getProblems() {
        return (Multimap<String, String>)ImmutableMultimap.copyOf((Multimap)this.problems);
    }
    
    public void validateUser(final LootContextUser cyt) {
        this.params.validateUser(this, cyt);
    }
    
    @Nullable
    public LootTable resolveLootTable(final ResourceLocation vk) {
        return (LootTable)this.tableResolver.apply(vk);
    }
    
    @Nullable
    public LootItemCondition resolveCondition(final ResourceLocation vk) {
        return (LootItemCondition)this.conditionResolver.apply(vk);
    }
    
    public ValidationContext setParams(final LootContextParamSet dax) {
        return new ValidationContext(this.problems, this.context, dax, this.conditionResolver, this.visitedConditions, this.tableResolver, this.visitedTables);
    }
}
