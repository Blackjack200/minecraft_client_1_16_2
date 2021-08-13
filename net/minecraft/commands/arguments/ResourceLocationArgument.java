package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.Recipe;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.arguments.ArgumentType;

public class ResourceLocationArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_ADVANCEMENT;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_RECIPE;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_PREDICATE;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_ATTRIBUTE;
    
    public static ResourceLocationArgument id() {
        return new ResourceLocationArgument();
    }
    
    public static Advancement getAdvancement(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final ResourceLocation vk3 = (ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class);
        final Advancement y4 = ((CommandSourceStack)commandContext.getSource()).getServer().getAdvancements().getAdvancement(vk3);
        if (y4 == null) {
            throw ResourceLocationArgument.ERROR_UNKNOWN_ADVANCEMENT.create(vk3);
        }
        return y4;
    }
    
    public static Recipe<?> getRecipe(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final RecipeManager boo3 = ((CommandSourceStack)commandContext.getSource()).getServer().getRecipeManager();
        final ResourceLocation vk4 = (ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class);
        return boo3.byKey(vk4).orElseThrow(() -> ResourceLocationArgument.ERROR_UNKNOWN_RECIPE.create(vk4));
    }
    
    public static LootItemCondition getPredicate(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final ResourceLocation vk3 = (ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class);
        final PredicateManager cyx4 = ((CommandSourceStack)commandContext.getSource()).getServer().getPredicateManager();
        final LootItemCondition dbl5 = cyx4.get(vk3);
        if (dbl5 == null) {
            throw ResourceLocationArgument.ERROR_UNKNOWN_PREDICATE.create(vk3);
        }
        return dbl5;
    }
    
    public static Attribute getAttribute(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final ResourceLocation vk3 = (ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class);
        return (Attribute)Registry.ATTRIBUTE.getOptional(vk3).orElseThrow(() -> ResourceLocationArgument.ERROR_UNKNOWN_ATTRIBUTE.create(vk3));
    }
    
    public static ResourceLocation getId(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (ResourceLocation)commandContext.getArgument(string, (Class)ResourceLocation.class);
    }
    
    public ResourceLocation parse(final StringReader stringReader) throws CommandSyntaxException {
        return ResourceLocation.read(stringReader);
    }
    
    public Collection<String> getExamples() {
        return ResourceLocationArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "foo:bar", "012" });
        ERROR_UNKNOWN_ADVANCEMENT = new DynamicCommandExceptionType(object -> new TranslatableComponent("advancement.advancementNotFound", new Object[] { object }));
        ERROR_UNKNOWN_RECIPE = new DynamicCommandExceptionType(object -> new TranslatableComponent("recipe.notFound", new Object[] { object }));
        ERROR_UNKNOWN_PREDICATE = new DynamicCommandExceptionType(object -> new TranslatableComponent("predicate.unknown", new Object[] { object }));
        ERROR_UNKNOWN_ATTRIBUTE = new DynamicCommandExceptionType(object -> new TranslatableComponent("attribute.unknown", new Object[] { object }));
    }
}
