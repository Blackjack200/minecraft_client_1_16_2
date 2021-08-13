package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class RotationArgument implements ArgumentType<Coordinates> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE;
    
    public static RotationArgument rotation() {
        return new RotationArgument();
    }
    
    public static Coordinates getRotation(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (Coordinates)commandContext.getArgument(string, (Class)Coordinates.class);
    }
    
    public Coordinates parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        if (!stringReader.canRead()) {
            throw RotationArgument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        final WorldCoordinate es4 = WorldCoordinate.parseDouble(stringReader, false);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer3);
            throw RotationArgument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es5 = WorldCoordinate.parseDouble(stringReader, false);
        return new WorldCoordinates(es5, es4, new WorldCoordinate(true, 0.0));
    }
    
    public Collection<String> getExamples() {
        return RotationArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0 0", "~ ~", "~-5 ~5" });
        ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.rotation.incomplete"));
    }
}
