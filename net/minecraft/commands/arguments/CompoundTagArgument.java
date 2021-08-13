package net.minecraft.commands.arguments;

import java.util.Arrays;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.nbt.CompoundTag;
import com.mojang.brigadier.arguments.ArgumentType;

public class CompoundTagArgument implements ArgumentType<CompoundTag> {
    private static final Collection<String> EXAMPLES;
    
    private CompoundTagArgument() {
    }
    
    public static CompoundTagArgument compoundTag() {
        return new CompoundTagArgument();
    }
    
    public static <S> CompoundTag getCompoundTag(final CommandContext<S> commandContext, final String string) {
        return (CompoundTag)commandContext.getArgument(string, (Class)CompoundTag.class);
    }
    
    public CompoundTag parse(final StringReader stringReader) throws CommandSyntaxException {
        return new TagParser(stringReader).readStruct();
    }
    
    public Collection<String> getExamples() {
        return CompoundTagArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "{}", "{foo=bar}" });
    }
}
