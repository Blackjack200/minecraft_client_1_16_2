package net.minecraft.commands.arguments;

import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.arguments.ArgumentType;

public class ComponentArgument implements ArgumentType<Component> {
    private static final Collection<String> EXAMPLES;
    public static final DynamicCommandExceptionType ERROR_INVALID_JSON;
    
    private ComponentArgument() {
    }
    
    public static Component getComponent(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (Component)commandContext.getArgument(string, (Class)Component.class);
    }
    
    public static ComponentArgument textComponent() {
        return new ComponentArgument();
    }
    
    public Component parse(final StringReader stringReader) throws CommandSyntaxException {
        try {
            final Component nr3 = Component.Serializer.fromJson(stringReader);
            if (nr3 == null) {
                throw ComponentArgument.ERROR_INVALID_JSON.createWithContext((ImmutableStringReader)stringReader, "empty");
            }
            return nr3;
        }
        catch (JsonParseException jsonParseException3) {
            final String string4 = (jsonParseException3.getCause() != null) ? jsonParseException3.getCause().getMessage() : jsonParseException3.getMessage();
            throw ComponentArgument.ERROR_INVALID_JSON.createWithContext((ImmutableStringReader)stringReader, string4);
        }
    }
    
    public Collection<String> getExamples() {
        return ComponentArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "\"hello world\"", "\"\"", "\"{\"text\":\"hello world\"}", "[\"\"]" });
        ERROR_INVALID_JSON = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.component.invalid", new Object[] { object }));
    }
}
