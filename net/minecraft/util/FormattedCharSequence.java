package net.minecraft.util;

import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.List;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.network.chat.Style;

@FunctionalInterface
public interface FormattedCharSequence {
    public static final FormattedCharSequence EMPTY = aey -> true;
    
    boolean accept(final FormattedCharSink aey);
    
    default FormattedCharSequence codepoint(final int integer, final Style ob) {
        return aey -> aey.accept(0, ob, integer);
    }
    
    default FormattedCharSequence forward(final String string, final Style ob) {
        if (string.isEmpty()) {
            return FormattedCharSequence.EMPTY;
        }
        return aey -> StringDecomposer.iterate(string, ob, aey);
    }
    
    default FormattedCharSequence backward(final String string, final Style ob, final Int2IntFunction int2IntFunction) {
        if (string.isEmpty()) {
            return FormattedCharSequence.EMPTY;
        }
        return aey -> StringDecomposer.iterateBackwards(string, ob, decorateOutput(aey, int2IntFunction));
    }
    
    default FormattedCharSink decorateOutput(final FormattedCharSink aey, final Int2IntFunction int2IntFunction) {
        return (integer3, ob, integer5) -> aey.accept(integer3, ob, (int)int2IntFunction.apply(integer5));
    }
    
    default FormattedCharSequence composite(final FormattedCharSequence aex1, final FormattedCharSequence aex2) {
        return fromPair(aex1, aex2);
    }
    
    default FormattedCharSequence composite(final List<FormattedCharSequence> list) {
        final int integer2 = list.size();
        switch (integer2) {
            case 0: {
                return FormattedCharSequence.EMPTY;
            }
            case 1: {
                return (FormattedCharSequence)list.get(0);
            }
            case 2: {
                return fromPair((FormattedCharSequence)list.get(0), (FormattedCharSequence)list.get(1));
            }
            default: {
                return fromList((List<FormattedCharSequence>)ImmutableList.copyOf((Collection)list));
            }
        }
    }
    
    default FormattedCharSequence fromPair(final FormattedCharSequence aex1, final FormattedCharSequence aex2) {
        return aey -> aex1.accept(aey) && aex2.accept(aey);
    }
    
    default FormattedCharSequence fromList(final List<FormattedCharSequence> list) {
        final Iterator iterator;
        FormattedCharSequence aex4;
        return aey -> {
            list.iterator();
            while (iterator.hasNext()) {
                aex4 = (FormattedCharSequence)iterator.next();
                if (!aex4.accept(aey)) {
                    return false;
                }
            }
            return true;
        };
    }
}
