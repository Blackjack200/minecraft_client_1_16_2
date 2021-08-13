package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import net.minecraft.network.chat.ComponentUtils;
import java.io.IOException;
import net.minecraft.network.chat.Component;
import java.util.List;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestion;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.StringRange;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.network.protocol.Packet;

public class ClientboundCommandSuggestionsPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private Suggestions suggestions;
    
    public ClientboundCommandSuggestionsPacket() {
    }
    
    public ClientboundCommandSuggestionsPacket(final int integer, final Suggestions suggestions) {
        this.id = integer;
        this.suggestions = suggestions;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        final int integer3 = nf.readVarInt();
        final int integer4 = nf.readVarInt();
        final StringRange stringRange5 = StringRange.between(integer3, integer3 + integer4);
        final int integer5 = nf.readVarInt();
        final List<Suggestion> list7 = (List<Suggestion>)Lists.newArrayListWithCapacity(integer5);
        for (int integer6 = 0; integer6 < integer5; ++integer6) {
            final String string9 = nf.readUtf(32767);
            final Component nr10 = nf.readBoolean() ? nf.readComponent() : null;
            list7.add(new Suggestion(stringRange5, string9, (Message)nr10));
        }
        this.suggestions = new Suggestions(stringRange5, (List)list7);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeVarInt(this.suggestions.getRange().getStart());
        nf.writeVarInt(this.suggestions.getRange().getLength());
        nf.writeVarInt(this.suggestions.getList().size());
        for (final Suggestion suggestion4 : this.suggestions.getList()) {
            nf.writeUtf(suggestion4.getText());
            nf.writeBoolean(suggestion4.getTooltip() != null);
            if (suggestion4.getTooltip() != null) {
                nf.writeComponent(ComponentUtils.fromMessage(suggestion4.getTooltip()));
            }
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleCommandSuggestions(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public Suggestions getSuggestions() {
        return this.suggestions;
    }
}
