package net.minecraft.commands.arguments;

import net.minecraft.util.Mth;
import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class AngleArgument implements ArgumentType<SingleAngle> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE;
    
    public static AngleArgument angle() {
        return new AngleArgument();
    }
    
    public static float getAngle(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return ((SingleAngle)commandContext.getArgument(string, (Class)SingleAngle.class)).getAngle((CommandSourceStack)commandContext.getSource());
    }
    
    public SingleAngle parse(final StringReader stringReader) throws CommandSyntaxException {
        if (!stringReader.canRead()) {
            throw AngleArgument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        final boolean boolean3 = WorldCoordinate.isRelative(stringReader);
        final float float4 = (stringReader.canRead() && stringReader.peek() != ' ') ? stringReader.readFloat() : 0.0f;
        return new SingleAngle(float4, boolean3);
    }
    
    public Collection<String> getExamples() {
        return AngleArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0", "~", "~-5" });
        ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.angle.incomplete"));
    }
    
    public static final class SingleAngle {
        private final float angle;
        private final boolean isRelative;
        
        private SingleAngle(final float float1, final boolean boolean2) {
            this.angle = float1;
            this.isRelative = boolean2;
        }
        
        public float getAngle(final CommandSourceStack db) {
            return Mth.wrapDegrees(this.isRelative ? (this.angle + db.getRotation().y) : this.angle);
        }
    }
}
