package net.minecraft.commands.arguments;

import javax.annotation.Nullable;
import net.minecraft.commands.arguments.selector.EntitySelector;
import java.util.List;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import java.util.Arrays;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class MessageArgument implements ArgumentType<Message> {
    private static final Collection<String> EXAMPLES;
    
    public static MessageArgument message() {
        return new MessageArgument();
    }
    
    public static Component getMessage(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return ((Message)commandContext.getArgument(string, (Class)Message.class)).toComponent((CommandSourceStack)commandContext.getSource(), ((CommandSourceStack)commandContext.getSource()).hasPermission(2));
    }
    
    public Message parse(final StringReader stringReader) throws CommandSyntaxException {
        return Message.parseText(stringReader, true);
    }
    
    public Collection<String> getExamples() {
        return MessageArgument.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "Hello world!", "foo", "@e", "Hello @p :)" });
    }
    
    public static class Message {
        private final String text;
        private final Part[] parts;
        
        public Message(final String string, final Part[] arr) {
            this.text = string;
            this.parts = arr;
        }
        
        public Component toComponent(final CommandSourceStack db, final boolean boolean2) throws CommandSyntaxException {
            if (this.parts.length == 0 || !boolean2) {
                return new TextComponent(this.text);
            }
            final MutableComponent nx4 = new TextComponent(this.text.substring(0, this.parts[0].getStart()));
            int integer5 = this.parts[0].getStart();
            for (final Part b9 : this.parts) {
                final Component nr10 = b9.toComponent(db);
                if (integer5 < b9.getStart()) {
                    nx4.append(this.text.substring(integer5, b9.getStart()));
                }
                if (nr10 != null) {
                    nx4.append(nr10);
                }
                integer5 = b9.getEnd();
            }
            if (integer5 < this.text.length()) {
                nx4.append(this.text.substring(integer5, this.text.length()));
            }
            return nx4;
        }
        
        public static Message parseText(final StringReader stringReader, final boolean boolean2) throws CommandSyntaxException {
            final String string3 = stringReader.getString().substring(stringReader.getCursor(), stringReader.getTotalLength());
            if (!boolean2) {
                stringReader.setCursor(stringReader.getTotalLength());
                return new Message(string3, new Part[0]);
            }
            final List<Part> list4 = (List<Part>)Lists.newArrayList();
            final int integer5 = stringReader.getCursor();
            while (stringReader.canRead()) {
                if (stringReader.peek() == '@') {
                    final int integer6 = stringReader.getCursor();
                    EntitySelector fc7;
                    try {
                        final EntitySelectorParser fd8 = new EntitySelectorParser(stringReader);
                        fc7 = fd8.parse();
                    }
                    catch (CommandSyntaxException commandSyntaxException8) {
                        if (commandSyntaxException8.getType() == EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE || commandSyntaxException8.getType() == EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE) {
                            stringReader.setCursor(integer6 + 1);
                            continue;
                        }
                        throw commandSyntaxException8;
                    }
                    list4.add(new Part(integer6 - integer5, stringReader.getCursor() - integer5, fc7));
                }
                else {
                    stringReader.skip();
                }
            }
            return new Message(string3, (Part[])list4.toArray((Object[])new Part[list4.size()]));
        }
    }
    
    public static class Part {
        private final int start;
        private final int end;
        private final EntitySelector selector;
        
        public Part(final int integer1, final int integer2, final EntitySelector fc) {
            this.start = integer1;
            this.end = integer2;
            this.selector = fc;
        }
        
        public int getStart() {
            return this.start;
        }
        
        public int getEnd() {
            return this.end;
        }
        
        @Nullable
        public Component toComponent(final CommandSourceStack db) throws CommandSyntaxException {
            return EntitySelector.joinNames(this.selector.findEntities(db));
        }
    }
}
