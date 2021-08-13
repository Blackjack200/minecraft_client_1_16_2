package net.minecraft.network.chat;

import java.util.Arrays;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;
import java.util.Iterator;
import java.util.Optional;
import java.util.regex.Matcher;
import com.google.common.collect.Lists;
import java.util.regex.Pattern;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.locale.Language;

public class TranslatableComponent extends BaseComponent implements ContextAwareComponent {
    private static final Object[] NO_ARGS;
    private static final FormattedText TEXT_PERCENT;
    private static final FormattedText TEXT_NULL;
    private final String key;
    private final Object[] args;
    @Nullable
    private Language decomposedWith;
    private final List<FormattedText> decomposedParts;
    private static final Pattern FORMAT_PATTERN;
    
    public TranslatableComponent(final String string) {
        this.decomposedParts = (List<FormattedText>)Lists.newArrayList();
        this.key = string;
        this.args = TranslatableComponent.NO_ARGS;
    }
    
    public TranslatableComponent(final String string, final Object... arr) {
        this.decomposedParts = (List<FormattedText>)Lists.newArrayList();
        this.key = string;
        this.args = arr;
    }
    
    private void decompose() {
        final Language ly2 = Language.getInstance();
        if (ly2 == this.decomposedWith) {
            return;
        }
        this.decomposedWith = ly2;
        this.decomposedParts.clear();
        final String string3 = ly2.getOrDefault(this.key);
        try {
            this.decomposeTemplate(string3);
        }
        catch (TranslatableFormatException og4) {
            this.decomposedParts.clear();
            this.decomposedParts.add(FormattedText.of(string3));
        }
    }
    
    private void decomposeTemplate(final String string) {
        final Matcher matcher3 = TranslatableComponent.FORMAT_PATTERN.matcher((CharSequence)string);
        try {
            int integer4 = 0;
            int integer5;
            int integer7;
            for (integer5 = 0; matcher3.find(integer5); integer5 = integer7) {
                final int integer6 = matcher3.start();
                integer7 = matcher3.end();
                if (integer6 > integer5) {
                    final String string2 = string.substring(integer5, integer6);
                    if (string2.indexOf(37) != -1) {
                        throw new IllegalArgumentException();
                    }
                    this.decomposedParts.add(FormattedText.of(string2));
                }
                final String string2 = matcher3.group(2);
                final String string3 = string.substring(integer6, integer7);
                if ("%".equals(string2) && "%%".equals(string3)) {
                    this.decomposedParts.add(TranslatableComponent.TEXT_PERCENT);
                }
                else {
                    if (!"s".equals(string2)) {
                        throw new TranslatableFormatException(this, "Unsupported format: '" + string3 + "'");
                    }
                    final String string4 = matcher3.group(1);
                    final int integer8 = (string4 != null) ? (Integer.parseInt(string4) - 1) : integer4++;
                    if (integer8 < this.args.length) {
                        this.decomposedParts.add(this.getArgument(integer8));
                    }
                }
            }
            if (integer5 < string.length()) {
                final String string5 = string.substring(integer5);
                if (string5.indexOf(37) != -1) {
                    throw new IllegalArgumentException();
                }
                this.decomposedParts.add(FormattedText.of(string5));
            }
        }
        catch (IllegalArgumentException illegalArgumentException4) {
            throw new TranslatableFormatException(this, (Throwable)illegalArgumentException4);
        }
    }
    
    private FormattedText getArgument(final int integer) {
        if (integer >= this.args.length) {
            throw new TranslatableFormatException(this, integer);
        }
        final Object object3 = this.args[integer];
        if (object3 instanceof Component) {
            return (Component)object3;
        }
        return (object3 == null) ? TranslatableComponent.TEXT_NULL : FormattedText.of(object3.toString());
    }
    
    @Override
    public TranslatableComponent plainCopy() {
        return new TranslatableComponent(this.key, this.args);
    }
    
    public <T> Optional<T> visitSelf(final FormattedText.StyledContentConsumer<T> b, final Style ob) {
        this.decompose();
        for (final FormattedText nu5 : this.decomposedParts) {
            final Optional<T> optional6 = nu5.<T>visit(b, ob);
            if (optional6.isPresent()) {
                return optional6;
            }
        }
        return (Optional<T>)Optional.empty();
    }
    
    public <T> Optional<T> visitSelf(final FormattedText.ContentConsumer<T> a) {
        this.decompose();
        for (final FormattedText nu4 : this.decomposedParts) {
            final Optional<T> optional5 = nu4.<T>visit(a);
            if (optional5.isPresent()) {
                return optional5;
            }
        }
        return (Optional<T>)Optional.empty();
    }
    
    @Override
    public MutableComponent resolve(@Nullable final CommandSourceStack db, @Nullable final Entity apx, final int integer) throws CommandSyntaxException {
        final Object[] arr5 = new Object[this.args.length];
        for (int integer2 = 0; integer2 < arr5.length; ++integer2) {
            final Object object7 = this.args[integer2];
            if (object7 instanceof Component) {
                arr5[integer2] = ComponentUtils.updateForEntity(db, (Component)object7, apx, integer);
            }
            else {
                arr5[integer2] = object7;
            }
        }
        return new TranslatableComponent(this.key, arr5);
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof TranslatableComponent) {
            final TranslatableComponent of3 = (TranslatableComponent)object;
            return Arrays.equals(this.args, of3.args) && this.key.equals(of3.key) && super.equals(object);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int integer2 = super.hashCode();
        integer2 = 31 * integer2 + this.key.hashCode();
        integer2 = 31 * integer2 + Arrays.hashCode(this.args);
        return integer2;
    }
    
    @Override
    public String toString() {
        return "TranslatableComponent{key='" + this.key + '\'' + ", args=" + Arrays.toString(this.args) + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Object[] getArgs() {
        return this.args;
    }
    
    static {
        NO_ARGS = new Object[0];
        TEXT_PERCENT = FormattedText.of("%");
        TEXT_NULL = FormattedText.of("null");
        FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
    }
}
