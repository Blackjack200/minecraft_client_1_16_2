package net.minecraft.commands.arguments;

import java.util.Arrays;
import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.regex.Matcher;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import java.util.regex.Pattern;
import java.util.Collection;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.UUID;
import com.mojang.brigadier.arguments.ArgumentType;

public class UuidArgument implements ArgumentType<UUID> {
    public static final SimpleCommandExceptionType ERROR_INVALID_UUID;
    private static final Collection<String> EXAMPLES;
    private static final Pattern ALLOWED_CHARACTERS;
    
    public static UUID getUuid(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (UUID)commandContext.getArgument(string, (Class)UUID.class);
    }
    
    public static UuidArgument uuid() {
        return new UuidArgument();
    }
    
    public UUID parse(final StringReader stringReader) throws CommandSyntaxException {
        final String string3 = stringReader.getRemaining();
        final Matcher matcher4 = UuidArgument.ALLOWED_CHARACTERS.matcher((CharSequence)string3);
        if (matcher4.find()) {
            final String string4 = matcher4.group(1);
            try {
                final UUID uUID6 = UUID.fromString(string4);
                stringReader.setCursor(stringReader.getCursor() + string4.length());
                return uUID6;
            }
            catch (IllegalArgumentException ex) {}
        }
        throw UuidArgument.ERROR_INVALID_UUID.create();
    }
    
    public Collection<String> getExamples() {
        return UuidArgument.EXAMPLES;
    }
    
    static {
        ERROR_INVALID_UUID = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.uuid.invalid"));
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "dd12be42-52a9-4a91-a8a1-11c01849e498" });
        ALLOWED_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");
    }
}
