package net.minecraft.commands.arguments.item;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import java.util.Collections;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.tags.Tag;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.resources.ResourceLocation;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class FunctionArgument implements ArgumentType<Result> {
    private static final Collection<String> EXAMPLES;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG;
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_FUNCTION;
    
    public static FunctionArgument functions() {
        return new FunctionArgument();
    }
    
    public Result parse(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '#') {
            stringReader.skip();
            final ResourceLocation vk3 = ResourceLocation.read(stringReader);
            return new Result() {
                public Collection<CommandFunction> create(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                    final Tag<CommandFunction> aej3 = getFunctionTag(commandContext, vk3);
                    return (Collection<CommandFunction>)aej3.getValues();
                }
                
                public Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> unwrap(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                    return (Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>>)Pair.of(vk3, Either.right((Object)getFunctionTag(commandContext, vk3)));
                }
            };
        }
        final ResourceLocation vk3 = ResourceLocation.read(stringReader);
        return new Result() {
            public Collection<CommandFunction> create(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                return (Collection<CommandFunction>)Collections.singleton(getFunction(commandContext, vk3));
            }
            
            public Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> unwrap(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                return (Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>>)Pair.of(vk3, Either.left((Object)getFunction(commandContext, vk3)));
            }
        };
    }
    
    private static CommandFunction getFunction(final CommandContext<CommandSourceStack> commandContext, final ResourceLocation vk) throws CommandSyntaxException {
        return (CommandFunction)((CommandSourceStack)commandContext.getSource()).getServer().getFunctions().get(vk).orElseThrow(() -> FunctionArgument.ERROR_UNKNOWN_FUNCTION.create(vk.toString()));
    }
    
    private static Tag<CommandFunction> getFunctionTag(final CommandContext<CommandSourceStack> commandContext, final ResourceLocation vk) throws CommandSyntaxException {
        final Tag<CommandFunction> aej3 = ((CommandSourceStack)commandContext.getSource()).getServer().getFunctions().getTag(vk);
        if (aej3 == null) {
            throw FunctionArgument.ERROR_UNKNOWN_TAG.create(vk.toString());
        }
        return aej3;
    }
    
    public static Collection<CommandFunction> getFunctions(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Result)commandContext.getArgument(string, (Class)Result.class)).create(commandContext);
    }
    
    public static Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> getFunctionOrTag(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Result)commandContext.getArgument(string, (Class)Result.class)).unwrap(commandContext);
    }
    
    public Collection<String> getExamples() {
        return FunctionArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "foo:bar", "#foo" });
        ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.function.tag.unknown", new Object[] { object }));
        ERROR_UNKNOWN_FUNCTION = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.function.unknown", new Object[] { object }));
    }
    
    public interface Result {
        Collection<CommandFunction> create(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
        
        Pair<ResourceLocation, Either<CommandFunction, Tag<CommandFunction>>> unwrap(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }
}
