package net.minecraft.commands;

import java.util.Optional;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import net.minecraft.server.ServerFunctionManager;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.google.common.collect.Lists;
import java.util.List;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.resources.ResourceLocation;

public class CommandFunction {
    private final Entry[] entries;
    private final ResourceLocation id;
    
    public CommandFunction(final ResourceLocation vk, final Entry[] arr) {
        this.id = vk;
        this.entries = arr;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public Entry[] getEntries() {
        return this.entries;
    }
    
    public static CommandFunction fromLines(final ResourceLocation vk, final CommandDispatcher<CommandSourceStack> commandDispatcher, final CommandSourceStack db, final List<String> list) {
        final List<Entry> list2 = (List<Entry>)Lists.newArrayListWithCapacity(list.size());
        for (int integer6 = 0; integer6 < list.size(); ++integer6) {
            final int integer7 = integer6 + 1;
            final String string8 = ((String)list.get(integer6)).trim();
            final StringReader stringReader9 = new StringReader(string8);
            if (stringReader9.canRead()) {
                if (stringReader9.peek() != '#') {
                    if (stringReader9.peek() == '/') {
                        stringReader9.skip();
                        if (stringReader9.peek() == '/') {
                            throw new IllegalArgumentException("Unknown or invalid command '" + string8 + "' on line " + integer7 + " (if you intended to make a comment, use '#' not '//')");
                        }
                        final String string9 = stringReader9.readUnquotedString();
                        throw new IllegalArgumentException("Unknown or invalid command '" + string8 + "' on line " + integer7 + " (did you mean '" + string9 + "'? Do not use a preceding forwards slash.)");
                    }
                    else {
                        try {
                            final ParseResults<CommandSourceStack> parseResults10 = (ParseResults<CommandSourceStack>)commandDispatcher.parse(stringReader9, db);
                            if (parseResults10.getReader().canRead()) {
                                throw Commands.<CommandSourceStack>getParseException(parseResults10);
                            }
                            list2.add(new CommandEntry(parseResults10));
                        }
                        catch (CommandSyntaxException commandSyntaxException10) {
                            throw new IllegalArgumentException(new StringBuilder().append("Whilst parsing command on line ").append(integer7).append(": ").append(commandSyntaxException10.getMessage()).toString());
                        }
                    }
                }
            }
        }
        return new CommandFunction(vk, (Entry[])list2.toArray((Object[])new Entry[0]));
    }
    
    public static class CommandEntry implements Entry {
        private final ParseResults<CommandSourceStack> parse;
        
        public CommandEntry(final ParseResults<CommandSourceStack> parseResults) {
            this.parse = parseResults;
        }
        
        public void execute(final ServerFunctionManager vx, final CommandSourceStack db, final ArrayDeque<ServerFunctionManager.QueuedCommand> arrayDeque, final int integer) throws CommandSyntaxException {
            vx.getDispatcher().execute(new ParseResults(this.parse.getContext().withSource(db), this.parse.getReader(), this.parse.getExceptions()));
        }
        
        public String toString() {
            return this.parse.getReader().getString();
        }
    }
    
    public static class FunctionEntry implements Entry {
        private final CacheableFunction function;
        
        public FunctionEntry(final CommandFunction cy) {
            this.function = new CacheableFunction(cy);
        }
        
        public void execute(final ServerFunctionManager vx, final CommandSourceStack db, final ArrayDeque<ServerFunctionManager.QueuedCommand> arrayDeque, final int integer) {
            this.function.get(vx).ifPresent(cy -> {
                final Entry[] arr6 = cy.getEntries();
                final int integer2 = integer - arrayDeque.size();
                final int integer3 = Math.min(arr6.length, integer2);
                for (int integer4 = integer3 - 1; integer4 >= 0; --integer4) {
                    arrayDeque.addFirst(new ServerFunctionManager.QueuedCommand(vx, db, arr6[integer4]));
                }
            });
        }
        
        public String toString() {
            return new StringBuilder().append("function ").append(this.function.getId()).toString();
        }
    }
    
    public static class CacheableFunction {
        public static final CacheableFunction NONE;
        @Nullable
        private final ResourceLocation id;
        private boolean resolved;
        private Optional<CommandFunction> function;
        
        public CacheableFunction(@Nullable final ResourceLocation vk) {
            this.function = (Optional<CommandFunction>)Optional.empty();
            this.id = vk;
        }
        
        public CacheableFunction(final CommandFunction cy) {
            this.function = (Optional<CommandFunction>)Optional.empty();
            this.resolved = true;
            this.id = null;
            this.function = (Optional<CommandFunction>)Optional.of(cy);
        }
        
        public Optional<CommandFunction> get(final ServerFunctionManager vx) {
            if (!this.resolved) {
                if (this.id != null) {
                    this.function = vx.get(this.id);
                }
                this.resolved = true;
            }
            return this.function;
        }
        
        @Nullable
        public ResourceLocation getId() {
            return (ResourceLocation)this.function.map(cy -> cy.id).orElse(this.id);
        }
        
        static {
            NONE = new CacheableFunction((ResourceLocation)null);
        }
    }
    
    public interface Entry {
        void execute(final ServerFunctionManager vx, final CommandSourceStack db, final ArrayDeque<ServerFunctionManager.QueuedCommand> arrayDeque, final int integer) throws CommandSyntaxException;
    }
}
