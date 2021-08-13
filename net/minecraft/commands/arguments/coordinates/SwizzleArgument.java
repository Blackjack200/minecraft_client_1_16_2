package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.core.Direction;
import java.util.EnumSet;
import com.mojang.brigadier.arguments.ArgumentType;

public class SwizzleArgument implements ArgumentType<EnumSet<Direction.Axis>> {
    private static final Collection<String> EXAMPLES;
    private static final SimpleCommandExceptionType ERROR_INVALID;
    
    public static SwizzleArgument swizzle() {
        return new SwizzleArgument();
    }
    
    public static EnumSet<Direction.Axis> getSwizzle(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (EnumSet<Direction.Axis>)commandContext.getArgument(string, (Class)EnumSet.class);
    }
    
    public EnumSet<Direction.Axis> parse(final StringReader stringReader) throws CommandSyntaxException {
        final EnumSet<Direction.Axis> enumSet3 = (EnumSet<Direction.Axis>)EnumSet.noneOf((Class)Direction.Axis.class);
        while (stringReader.canRead() && stringReader.peek() != ' ') {
            final char character4 = stringReader.read();
            Direction.Axis a5 = null;
            switch (character4) {
                case 'x': {
                    a5 = Direction.Axis.X;
                    break;
                }
                case 'y': {
                    a5 = Direction.Axis.Y;
                    break;
                }
                case 'z': {
                    a5 = Direction.Axis.Z;
                    break;
                }
                default: {
                    throw SwizzleArgument.ERROR_INVALID.create();
                }
            }
            if (enumSet3.contains(a5)) {
                throw SwizzleArgument.ERROR_INVALID.create();
            }
            enumSet3.add(a5);
        }
        return enumSet3;
    }
    
    public Collection<String> getExamples() {
        return SwizzleArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "xyz", "x" });
        ERROR_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.swizzle.invalid"));
    }
}
