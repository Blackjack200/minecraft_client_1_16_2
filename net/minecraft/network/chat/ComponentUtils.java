package net.minecraft.network.chat;

import com.mojang.brigadier.Message;
import net.minecraft.ChatFormatting;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.function.Function;
import java.util.Collection;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;

public class ComponentUtils {
    public static MutableComponent mergeStyles(final MutableComponent nx, final Style ob) {
        if (ob.isEmpty()) {
            return nx;
        }
        final Style ob2 = nx.getStyle();
        if (ob2.isEmpty()) {
            return nx.setStyle(ob);
        }
        if (ob2.equals(ob)) {
            return nx;
        }
        return nx.setStyle(ob2.applyTo(ob));
    }
    
    public static MutableComponent updateForEntity(@Nullable final CommandSourceStack db, final Component nr, @Nullable final Entity apx, final int integer) throws CommandSyntaxException {
        if (integer > 100) {
            return nr.copy();
        }
        final MutableComponent nx5 = (nr instanceof ContextAwareComponent) ? ((ContextAwareComponent)nr).resolve(db, apx, integer + 1) : nr.plainCopy();
        for (final Component nr2 : nr.getSiblings()) {
            nx5.append(updateForEntity(db, nr2, apx, integer + 1));
        }
        return nx5.withStyle(resolveStyle(db, nr.getStyle(), apx, integer));
    }
    
    private static Style resolveStyle(@Nullable final CommandSourceStack db, final Style ob, @Nullable final Entity apx, final int integer) throws CommandSyntaxException {
        final HoverEvent nv5 = ob.getHoverEvent();
        if (nv5 != null) {
            final Component nr6 = nv5.<Component>getValue(HoverEvent.Action.SHOW_TEXT);
            if (nr6 != null) {
                final HoverEvent nv6 = new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)updateForEntity(db, nr6, apx, integer + 1));
                return ob.withHoverEvent(nv6);
            }
        }
        return ob;
    }
    
    public static Component getDisplayName(final GameProfile gameProfile) {
        if (gameProfile.getName() != null) {
            return new TextComponent(gameProfile.getName());
        }
        if (gameProfile.getId() != null) {
            return new TextComponent(gameProfile.getId().toString());
        }
        return new TextComponent("(unknown)");
    }
    
    public static Component formatList(final Collection<String> collection) {
        return ComponentUtils.<String>formatAndSortList(collection, (java.util.function.Function<String, Component>)(string -> new TextComponent(string).withStyle(ChatFormatting.GREEN)));
    }
    
    public static <T extends Comparable<T>> Component formatAndSortList(final Collection<T> collection, final Function<T, Component> function) {
        if (collection.isEmpty()) {
            return TextComponent.EMPTY;
        }
        if (collection.size() == 1) {
            return (Component)function.apply(collection.iterator().next());
        }
        final List<T> list3 = (List<T>)Lists.newArrayList((Iterable)collection);
        list3.sort(Comparable::compareTo);
        return ComponentUtils.<T>formatList((java.util.Collection<T>)list3, function);
    }
    
    public static <T> MutableComponent formatList(final Collection<T> collection, final Function<T, Component> function) {
        if (collection.isEmpty()) {
            return new TextComponent("");
        }
        if (collection.size() == 1) {
            return ((Component)function.apply(collection.iterator().next())).copy();
        }
        final MutableComponent nx3 = new TextComponent("");
        boolean boolean4 = true;
        for (final T object6 : collection) {
            if (!boolean4) {
                nx3.append(new TextComponent(", ").withStyle(ChatFormatting.GRAY));
            }
            nx3.append((Component)function.apply(object6));
            boolean4 = false;
        }
        return nx3;
    }
    
    public static MutableComponent wrapInSquareBrackets(final Component nr) {
        return new TranslatableComponent("chat.square_brackets", new Object[] { nr });
    }
    
    public static Component fromMessage(final Message message) {
        if (message instanceof Component) {
            return (Component)message;
        }
        return new TextComponent(message.getString());
    }
}
