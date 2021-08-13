package net.minecraft.network.chat;

import java.util.Optional;
import net.minecraft.util.StringDecomposer;
import java.util.function.UnaryOperator;
import com.google.common.collect.Lists;
import net.minecraft.util.FormattedCharSequence;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.List;

public class SubStringSource {
    private final String plainText;
    private final List<Style> charStyles;
    private final Int2IntFunction reverseCharModifier;
    
    private SubStringSource(final String string, final List<Style> list, final Int2IntFunction int2IntFunction) {
        this.plainText = string;
        this.charStyles = (List<Style>)ImmutableList.copyOf((Collection)list);
        this.reverseCharModifier = int2IntFunction;
    }
    
    public String getPlainText() {
        return this.plainText;
    }
    
    public List<FormattedCharSequence> substring(final int integer1, final int integer2, final boolean boolean3) {
        if (integer2 == 0) {
            return (List<FormattedCharSequence>)ImmutableList.of();
        }
        final List<FormattedCharSequence> list5 = (List<FormattedCharSequence>)Lists.newArrayList();
        Style ob6 = (Style)this.charStyles.get(integer1);
        int integer3 = integer1;
        for (int integer4 = 1; integer4 < integer2; ++integer4) {
            final int integer5 = integer1 + integer4;
            final Style ob7 = (Style)this.charStyles.get(integer5);
            if (!ob7.equals(ob6)) {
                final String string11 = this.plainText.substring(integer3, integer5);
                list5.add((boolean3 ? FormattedCharSequence.backward(string11, ob6, this.reverseCharModifier) : FormattedCharSequence.forward(string11, ob6)));
                ob6 = ob7;
                integer3 = integer5;
            }
        }
        if (integer3 < integer1 + integer2) {
            final String string12 = this.plainText.substring(integer3, integer1 + integer2);
            list5.add((boolean3 ? FormattedCharSequence.backward(string12, ob6, this.reverseCharModifier) : FormattedCharSequence.forward(string12, ob6)));
        }
        return (List<FormattedCharSequence>)(boolean3 ? Lists.reverse((List)list5) : list5);
    }
    
    public static SubStringSource create(final FormattedText nu, final Int2IntFunction int2IntFunction, final UnaryOperator<String> unaryOperator) {
        final StringBuilder stringBuilder4 = new StringBuilder();
        final List<Style> list5 = (List<Style>)Lists.newArrayList();
        final StringBuilder sb;
        int integer6;
        int integer7;
        final List list6;
        nu.visit((ob, string) -> {
            StringDecomposer.iterateFormatted(string, ob, (integer3, ob, integer5) -> {
                sb.appendCodePoint(integer5);
                for (integer6 = Character.charCount(integer5), integer7 = 0; integer7 < integer6; ++integer7) {
                    list6.add(ob);
                }
                return 1 != 0;
            });
            return (java.util.Optional<Object>)Optional.empty();
        }, Style.EMPTY);
        return new SubStringSource((String)unaryOperator.apply(stringBuilder4.toString()), list5, int2IntFunction);
    }
}
