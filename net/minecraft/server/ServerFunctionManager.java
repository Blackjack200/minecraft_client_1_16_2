package net.minecraft.server;

import net.minecraft.tags.Tag;
import java.util.Optional;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.world.level.GameRules;
import com.google.common.collect.Lists;
import net.minecraft.commands.CommandFunction;
import java.util.List;
import java.util.ArrayDeque;
import net.minecraft.resources.ResourceLocation;

public class ServerFunctionManager {
    private static final ResourceLocation TICK_FUNCTION_TAG;
    private static final ResourceLocation LOAD_FUNCTION_TAG;
    private final MinecraftServer server;
    private boolean isInFunction;
    private final ArrayDeque<QueuedCommand> commandQueue;
    private final List<QueuedCommand> nestedCalls;
    private final List<CommandFunction> ticking;
    private boolean postReload;
    private ServerFunctionLibrary library;
    
    public ServerFunctionManager(final MinecraftServer minecraftServer, final ServerFunctionLibrary vw) {
        this.commandQueue = (ArrayDeque<QueuedCommand>)new ArrayDeque();
        this.nestedCalls = (List<QueuedCommand>)Lists.newArrayList();
        this.ticking = (List<CommandFunction>)Lists.newArrayList();
        this.server = minecraftServer;
        this.postReload(this.library = vw);
    }
    
    public int getCommandLimit() {
        return this.server.getGameRules().getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH);
    }
    
    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return this.server.getCommands().getDispatcher();
    }
    
    public void tick() {
        this.executeTagFunctions((Collection<CommandFunction>)this.ticking, ServerFunctionManager.TICK_FUNCTION_TAG);
        if (this.postReload) {
            this.postReload = false;
            final Collection<CommandFunction> collection2 = (Collection<CommandFunction>)this.library.getTags().getTagOrEmpty(ServerFunctionManager.LOAD_FUNCTION_TAG).getValues();
            this.executeTagFunctions(collection2, ServerFunctionManager.LOAD_FUNCTION_TAG);
        }
    }
    
    private void executeTagFunctions(final Collection<CommandFunction> collection, final ResourceLocation vk) {
        this.server.getProfiler().push((Supplier<String>)vk::toString);
        for (final CommandFunction cy5 : collection) {
            this.execute(cy5, this.getGameLoopSender());
        }
        this.server.getProfiler().pop();
    }
    
    public int execute(final CommandFunction cy, final CommandSourceStack db) {
        final int integer4 = this.getCommandLimit();
        if (this.isInFunction) {
            if (this.commandQueue.size() + this.nestedCalls.size() < integer4) {
                this.nestedCalls.add(new QueuedCommand(this, db, new CommandFunction.FunctionEntry(cy)));
            }
            return 0;
        }
        try {
            this.isInFunction = true;
            int integer5 = 0;
            final CommandFunction.Entry[] arr6 = cy.getEntries();
            for (int integer6 = arr6.length - 1; integer6 >= 0; --integer6) {
                this.commandQueue.push(new QueuedCommand(this, db, arr6[integer6]));
            }
            while (!this.commandQueue.isEmpty()) {
                try {
                    final QueuedCommand a7 = (QueuedCommand)this.commandQueue.removeFirst();
                    this.server.getProfiler().push((Supplier<String>)a7::toString);
                    a7.execute(this.commandQueue, integer4);
                    if (!this.nestedCalls.isEmpty()) {
                        Lists.reverse((List)this.nestedCalls).forEach(this.commandQueue::addFirst);
                        this.nestedCalls.clear();
                    }
                }
                finally {
                    this.server.getProfiler().pop();
                }
                if (++integer5 >= integer4) {
                    return integer5;
                }
            }
            return integer5;
        }
        finally {
            this.commandQueue.clear();
            this.nestedCalls.clear();
            this.isInFunction = false;
        }
    }
    
    public void replaceLibrary(final ServerFunctionLibrary vw) {
        this.postReload(this.library = vw);
    }
    
    private void postReload(final ServerFunctionLibrary vw) {
        this.ticking.clear();
        this.ticking.addAll((Collection)vw.getTags().getTagOrEmpty(ServerFunctionManager.TICK_FUNCTION_TAG).getValues());
        this.postReload = true;
    }
    
    public CommandSourceStack getGameLoopSender() {
        return this.server.createCommandSourceStack().withPermission(2).withSuppressedOutput();
    }
    
    public Optional<CommandFunction> get(final ResourceLocation vk) {
        return this.library.getFunction(vk);
    }
    
    public Tag<CommandFunction> getTag(final ResourceLocation vk) {
        return this.library.getTag(vk);
    }
    
    public Iterable<ResourceLocation> getFunctionNames() {
        return (Iterable<ResourceLocation>)this.library.getFunctions().keySet();
    }
    
    public Iterable<ResourceLocation> getTagNames() {
        return (Iterable<ResourceLocation>)this.library.getTags().getAvailableTags();
    }
    
    static {
        TICK_FUNCTION_TAG = new ResourceLocation("tick");
        LOAD_FUNCTION_TAG = new ResourceLocation("load");
    }
    
    public static class QueuedCommand {
        private final ServerFunctionManager manager;
        private final CommandSourceStack sender;
        private final CommandFunction.Entry entry;
        
        public QueuedCommand(final ServerFunctionManager vx, final CommandSourceStack db, final CommandFunction.Entry c) {
            this.manager = vx;
            this.sender = db;
            this.entry = c;
        }
        
        public void execute(final ArrayDeque<QueuedCommand> arrayDeque, final int integer) {
            try {
                this.entry.execute(this.manager, this.sender, arrayDeque, integer);
            }
            catch (Throwable t) {}
        }
        
        public String toString() {
            return this.entry.toString();
        }
    }
}
