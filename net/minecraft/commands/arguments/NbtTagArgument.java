package net.minecraft.commands.arguments;

import java.util.Arrays;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.nbt.Tag;
import com.mojang.brigadier.arguments.ArgumentType;

public class NbtTagArgument implements ArgumentType<Tag> {
    private static final Collection<String> EXAMPLES;
    
    private NbtTagArgument() {
    }
    
    public static NbtTagArgument nbtTag() {
        return new NbtTagArgument();
    }
    
    public static <S> Tag getNbtTag(final CommandContext<S> commandContext, final String string) {
        return (Tag)commandContext.getArgument(string, (Class)Tag.class);
    }
    
    public Tag parse(final StringReader stringReader) throws CommandSyntaxException {
        return new TagParser(stringReader).readValue();
    }
    
    public Collection<String> getExamples() {
        return NbtTagArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0", "0b", "0l", "0.0", "\"foo\"", "{foo=bar}", "[0]" });
    }
}
