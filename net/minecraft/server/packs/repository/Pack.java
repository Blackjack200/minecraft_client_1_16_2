package net.minecraft.server.packs.repository;

import java.util.function.Function;
import java.util.List;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.HoverEvent;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TextComponent;
import javax.annotation.Nullable;
import java.io.IOException;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import java.util.function.Supplier;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import org.apache.logging.log4j.Logger;

public class Pack implements AutoCloseable {
    private static final Logger LOGGER;
    private static final PackMetadataSection BROKEN_ASSETS_FALLBACK;
    private final String id;
    private final Supplier<PackResources> supplier;
    private final Component title;
    private final Component description;
    private final PackCompatibility compatibility;
    private final Position defaultPosition;
    private final boolean required;
    private final boolean fixedPosition;
    private final PackSource packSource;
    
    @Nullable
    public static Pack create(final String string, final boolean boolean2, final Supplier<PackResources> supplier, final PackConstructor a, final Position b, final PackSource abv) {
        try (final PackResources abh7 = (PackResources)supplier.get()) {
            PackMetadataSection abm9 = abh7.<PackMetadataSection>getMetadataSection((MetadataSectionSerializer<PackMetadataSection>)PackMetadataSection.SERIALIZER);
            if (boolean2 && abm9 == null) {
                Pack.LOGGER.error("Broken/missing pack.mcmeta detected, fudging it into existance. Please check that your launcher has downloaded all assets for the game correctly!");
                abm9 = Pack.BROKEN_ASSETS_FALLBACK;
            }
            if (abm9 != null) {
                return a.create(string, boolean2, supplier, abh7, abm9, b, abv);
            }
            Pack.LOGGER.warn("Couldn't find pack meta for pack {}", string);
        }
        catch (IOException iOException7) {
            Pack.LOGGER.warn("Couldn't get pack info for: {}", iOException7.toString());
        }
        return null;
    }
    
    public Pack(final String string, final boolean boolean2, final Supplier<PackResources> supplier, final Component nr4, final Component nr5, final PackCompatibility abt, final Position b, final boolean boolean8, final PackSource abv) {
        this.id = string;
        this.supplier = supplier;
        this.title = nr4;
        this.description = nr5;
        this.compatibility = abt;
        this.required = boolean2;
        this.defaultPosition = b;
        this.fixedPosition = boolean8;
        this.packSource = abv;
    }
    
    public Pack(final String string, final boolean boolean2, final Supplier<PackResources> supplier, final PackResources abh, final PackMetadataSection abm, final Position b, final PackSource abv) {
        this(string, boolean2, supplier, new TextComponent(abh.getName()), abm.getDescription(), PackCompatibility.forFormat(abm.getPackFormat()), b, false, abv);
    }
    
    public Component getTitle() {
        return this.title;
    }
    
    public Component getDescription() {
        return this.description;
    }
    
    public Component getChatLink(final boolean boolean1) {
        return ComponentUtils.wrapInSquareBrackets(this.packSource.decorate(new TextComponent(this.id))).withStyle((UnaryOperator<Style>)(ob -> ob.withColor(boolean1 ? ChatFormatting.GREEN : ChatFormatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.id)).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TextComponent("").append(this.title).append("\n").append(this.description)))));
    }
    
    public PackCompatibility getCompatibility() {
        return this.compatibility;
    }
    
    public PackResources open() {
        return (PackResources)this.supplier.get();
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean isRequired() {
        return this.required;
    }
    
    public boolean isFixedPosition() {
        return this.fixedPosition;
    }
    
    public Position getDefaultPosition() {
        return this.defaultPosition;
    }
    
    public PackSource getPackSource() {
        return this.packSource;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Pack)) {
            return false;
        }
        final Pack abs3 = (Pack)object;
        return this.id.equals(abs3.id);
    }
    
    public int hashCode() {
        return this.id.hashCode();
    }
    
    public void close() {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BROKEN_ASSETS_FALLBACK = new PackMetadataSection(new TranslatableComponent("resourcePack.broken_assets").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC), SharedConstants.getCurrentVersion().getPackVersion());
    }
    
    public enum Position {
        TOP, 
        BOTTOM;
        
        public <T> int insert(final List<T> list, final T object, final Function<T, Pack> function, final boolean boolean4) {
            final Position b6 = boolean4 ? this.opposite() : this;
            if (b6 == Position.BOTTOM) {
                int integer7;
                for (integer7 = 0; integer7 < list.size(); ++integer7) {
                    final Pack abs8 = (Pack)function.apply(list.get(integer7));
                    if (!abs8.isFixedPosition() || abs8.getDefaultPosition() != this) {
                        break;
                    }
                }
                list.add(integer7, object);
                return integer7;
            }
            int integer7;
            for (integer7 = list.size() - 1; integer7 >= 0; --integer7) {
                final Pack abs8 = (Pack)function.apply(list.get(integer7));
                if (!abs8.isFixedPosition() || abs8.getDefaultPosition() != this) {
                    break;
                }
            }
            list.add(integer7 + 1, object);
            return integer7 + 1;
        }
        
        public Position opposite() {
            return (this == Position.TOP) ? Position.BOTTOM : Position.TOP;
        }
    }
    
    @FunctionalInterface
    public interface PackConstructor {
        @Nullable
        Pack create(final String string, final boolean boolean2, final Supplier<PackResources> supplier, final PackResources abh, final PackMetadataSection abm, final Position b, final PackSource abv);
    }
}
