package net.minecraft.network.chat;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Unit;
import java.util.Optional;

public interface FormattedText {
    public static final Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);
    public static final FormattedText EMPTY = new FormattedText() {
        public <T> Optional<T> visit(final ContentConsumer<T> a) {
            return (Optional<T>)Optional.empty();
        }
        
        public <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob) {
            return (Optional<T>)Optional.empty();
        }
    };
    
     <T> Optional<T> visit(final ContentConsumer<T> a);
    
     <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob);
    
    default FormattedText of(final String string) {
        return new FormattedText() {
            public <T> Optional<T> visit(final ContentConsumer<T> a) {
                return a.accept(string);
            }
            
            public <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob) {
                return b.accept(ob, string);
            }
        };
    }
    
    default FormattedText of(final String string, final Style ob) {
        return new FormattedText() {
            public <T> Optional<T> visit(final ContentConsumer<T> a) {
                return a.accept(string);
            }
            
            public <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob) {
                return b.accept(ob.applyTo(ob), string);
            }
        };
    }
    
    default FormattedText composite(final FormattedText... arr) {
        return composite((List<FormattedText>)ImmutableList.copyOf((Object[])arr));
    }
    
    default FormattedText composite(final List<FormattedText> list) {
        return new FormattedText() {
            public <T> Optional<T> visit(final ContentConsumer<T> a) {
                for (final FormattedText nu4 : list) {
                    final Optional<T> optional5 = nu4.<T>visit(a);
                    if (optional5.isPresent()) {
                        return optional5;
                    }
                }
                return (Optional<T>)Optional.empty();
            }
            
            public <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob) {
                for (final FormattedText nu5 : list) {
                    final Optional<T> optional6 = nu5.<T>visit(b, ob);
                    if (optional6.isPresent()) {
                        return optional6;
                    }
                }
                return (Optional<T>)Optional.empty();
            }
        };
    }
    
    default String getString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        this.visit(string -> {
            stringBuilder2.append(string);
            return (java.util.Optional<Object>)Optional.empty();
        });
        return stringBuilder2.toString();
    }
    
    public interface ContentConsumer<T> {
        Optional<T> accept(final String string);
    }
    
    public interface StyledContentConsumer<T> {
        Optional<T> accept(final Style ob, final String string);
    }
}
