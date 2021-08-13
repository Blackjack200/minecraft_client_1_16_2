package net.minecraft.network.protocol.game;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.PacketListener;
import java.util.Iterator;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import javax.annotation.Nullable;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.synchronization.ArgumentTypes;
import com.mojang.brigadier.builder.ArgumentBuilder;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import java.util.Queue;
import com.google.common.collect.Queues;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Map;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.network.protocol.Packet;

public class ClientboundCommandsPacket implements Packet<ClientGamePacketListener> {
    private RootCommandNode<SharedSuggestionProvider> root;
    
    public ClientboundCommandsPacket() {
    }
    
    public ClientboundCommandsPacket(final RootCommandNode<SharedSuggestionProvider> rootCommandNode) {
        this.root = rootCommandNode;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final Entry[] arr3 = new Entry[nf.readVarInt()];
        for (int integer4 = 0; integer4 < arr3.length; ++integer4) {
            arr3[integer4] = readNode(nf);
        }
        resolveEntries(arr3);
        this.root = (RootCommandNode<SharedSuggestionProvider>)arr3[nf.readVarInt()].node;
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        final Object2IntMap<CommandNode<SharedSuggestionProvider>> object2IntMap3 = enumerateNodes(this.root);
        final CommandNode<SharedSuggestionProvider>[] arr4 = getNodesInIdOrder(object2IntMap3);
        nf.writeVarInt(arr4.length);
        for (final CommandNode<SharedSuggestionProvider> commandNode8 : arr4) {
            writeNode(nf, commandNode8, (Map<CommandNode<SharedSuggestionProvider>, Integer>)object2IntMap3);
        }
        nf.writeVarInt(object2IntMap3.get(this.root));
    }
    
    private static void resolveEntries(final Entry[] arr) {
        final List<Entry> list2 = (List<Entry>)Lists.newArrayList((Object[])arr);
        while (!list2.isEmpty()) {
            final boolean boolean3 = list2.removeIf(a -> a.build(arr));
            if (!boolean3) {
                throw new IllegalStateException("Server sent an impossible command tree");
            }
        }
    }
    
    private static Object2IntMap<CommandNode<SharedSuggestionProvider>> enumerateNodes(final RootCommandNode<SharedSuggestionProvider> rootCommandNode) {
        final Object2IntMap<CommandNode<SharedSuggestionProvider>> object2IntMap2 = (Object2IntMap<CommandNode<SharedSuggestionProvider>>)new Object2IntOpenHashMap();
        final Queue<CommandNode<SharedSuggestionProvider>> queue3 = (Queue<CommandNode<SharedSuggestionProvider>>)Queues.newArrayDeque();
        queue3.add(rootCommandNode);
        CommandNode<SharedSuggestionProvider> commandNode4;
        while ((commandNode4 = (CommandNode<SharedSuggestionProvider>)queue3.poll()) != null) {
            if (object2IntMap2.containsKey(commandNode4)) {
                continue;
            }
            final int integer5 = object2IntMap2.size();
            object2IntMap2.put(commandNode4, integer5);
            queue3.addAll(commandNode4.getChildren());
            if (commandNode4.getRedirect() == null) {
                continue;
            }
            queue3.add(commandNode4.getRedirect());
        }
        return object2IntMap2;
    }
    
    private static CommandNode<SharedSuggestionProvider>[] getNodesInIdOrder(final Object2IntMap<CommandNode<SharedSuggestionProvider>> object2IntMap) {
        final CommandNode<SharedSuggestionProvider>[] arr2 = new CommandNode[object2IntMap.size()];
        for (final Object2IntMap.Entry<CommandNode<SharedSuggestionProvider>> entry4 : Object2IntMaps.fastIterable((Object2IntMap)object2IntMap)) {
            arr2[entry4.getIntValue()] = (CommandNode<SharedSuggestionProvider>)entry4.getKey();
        }
        return arr2;
    }
    
    private static Entry readNode(final FriendlyByteBuf nf) {
        final byte byte2 = nf.readByte();
        final int[] arr3 = nf.readVarIntArray();
        final int integer4 = ((byte2 & 0x8) != 0x0) ? nf.readVarInt() : 0;
        final ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder5 = createBuilder(nf, byte2);
        return new Entry((ArgumentBuilder)argumentBuilder5, byte2, integer4, arr3);
    }
    
    @Nullable
    private static ArgumentBuilder<SharedSuggestionProvider, ?> createBuilder(final FriendlyByteBuf nf, final byte byte2) {
        final int integer3 = byte2 & 0x3;
        if (integer3 == 2) {
            final String string4 = nf.readUtf(32767);
            final ArgumentType<?> argumentType5 = ArgumentTypes.deserialize(nf);
            if (argumentType5 == null) {
                return null;
            }
            final RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder6 = RequiredArgumentBuilder.argument(string4, (ArgumentType)argumentType5);
            if ((byte2 & 0x10) != 0x0) {
                requiredArgumentBuilder6.suggests((SuggestionProvider)SuggestionProviders.getProvider(nf.readResourceLocation()));
            }
            return requiredArgumentBuilder6;
        }
        else {
            if (integer3 == 1) {
                return LiteralArgumentBuilder.literal(nf.readUtf(32767));
            }
            return null;
        }
    }
    
    private static void writeNode(final FriendlyByteBuf nf, final CommandNode<SharedSuggestionProvider> commandNode, final Map<CommandNode<SharedSuggestionProvider>, Integer> map) {
        byte byte4 = 0;
        if (commandNode.getRedirect() != null) {
            byte4 |= 0x8;
        }
        if (commandNode.getCommand() != null) {
            byte4 |= 0x4;
        }
        if (commandNode instanceof RootCommandNode) {
            byte4 |= 0x0;
        }
        else if (commandNode instanceof ArgumentCommandNode) {
            byte4 |= 0x2;
            if (((ArgumentCommandNode)commandNode).getCustomSuggestions() != null) {
                byte4 |= 0x10;
            }
        }
        else {
            if (!(commandNode instanceof LiteralCommandNode)) {
                throw new UnsupportedOperationException(new StringBuilder().append("Unknown node type ").append(commandNode).toString());
            }
            byte4 |= 0x1;
        }
        nf.writeByte(byte4);
        nf.writeVarInt(commandNode.getChildren().size());
        for (final CommandNode<SharedSuggestionProvider> commandNode2 : commandNode.getChildren()) {
            nf.writeVarInt((int)map.get(commandNode2));
        }
        if (commandNode.getRedirect() != null) {
            nf.writeVarInt((int)map.get(commandNode.getRedirect()));
        }
        if (commandNode instanceof ArgumentCommandNode) {
            final ArgumentCommandNode<SharedSuggestionProvider, ?> argumentCommandNode5 = commandNode;
            nf.writeUtf(argumentCommandNode5.getName());
            ArgumentTypes.<ArgumentType>serialize(nf, argumentCommandNode5.getType());
            if (argumentCommandNode5.getCustomSuggestions() != null) {
                nf.writeResourceLocation(SuggestionProviders.getName((SuggestionProvider<SharedSuggestionProvider>)argumentCommandNode5.getCustomSuggestions()));
            }
        }
        else if (commandNode instanceof LiteralCommandNode) {
            nf.writeUtf(((LiteralCommandNode)commandNode).getLiteral());
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleCommands(this);
    }
    
    public RootCommandNode<SharedSuggestionProvider> getRoot() {
        return this.root;
    }
    
    static class Entry {
        @Nullable
        private final ArgumentBuilder<SharedSuggestionProvider, ?> builder;
        private final byte flags;
        private final int redirect;
        private final int[] children;
        @Nullable
        private CommandNode<SharedSuggestionProvider> node;
        
        private Entry(@Nullable final ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder, final byte byte2, final int integer, final int[] arr) {
            this.builder = argumentBuilder;
            this.flags = byte2;
            this.redirect = integer;
            this.children = arr;
        }
        
        public boolean build(final Entry[] arr) {
            if (this.node == null) {
                if (this.builder == null) {
                    this.node = (CommandNode<SharedSuggestionProvider>)new RootCommandNode();
                }
                else {
                    if ((this.flags & 0x8) != 0x0) {
                        if (arr[this.redirect].node == null) {
                            return false;
                        }
                        this.builder.redirect((CommandNode)arr[this.redirect].node);
                    }
                    if ((this.flags & 0x4) != 0x0) {
                        this.builder.executes(commandContext -> 0);
                    }
                    this.node = (CommandNode<SharedSuggestionProvider>)this.builder.build();
                }
            }
            for (final int integer6 : this.children) {
                if (arr[integer6].node == null) {
                    return false;
                }
            }
            for (final int integer6 : this.children) {
                final CommandNode<SharedSuggestionProvider> commandNode7 = arr[integer6].node;
                if (!(commandNode7 instanceof RootCommandNode)) {
                    this.node.addChild((CommandNode)commandNode7);
                }
            }
            return true;
        }
    }
}
