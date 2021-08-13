package net.minecraft.client.resources.language;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.BidiRun;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.ibm.icu.text.Bidi;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.SubStringSource;
import com.ibm.icu.lang.UCharacter;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.FormattedText;

public class FormattedBidiReorder {
    public static FormattedCharSequence reorder(final FormattedText nu, final boolean boolean2) {
        final SubStringSource oc3 = SubStringSource.create(nu, UCharacter::getMirror, (UnaryOperator<String>)FormattedBidiReorder::shape);
        final Bidi bidi4 = new Bidi(oc3.getPlainText(), boolean2 ? 127 : 126);
        bidi4.setReorderingMode(0);
        final List<FormattedCharSequence> list5 = (List<FormattedCharSequence>)Lists.newArrayList();
        for (int integer6 = bidi4.countRuns(), integer7 = 0; integer7 < integer6; ++integer7) {
            final BidiRun bidiRun8 = bidi4.getVisualRun(integer7);
            list5.addAll((Collection)oc3.substring(bidiRun8.getStart(), bidiRun8.getLength(), bidiRun8.isOddRun()));
        }
        return FormattedCharSequence.composite(list5);
    }
    
    private static String shape(final String string) {
        try {
            return new ArabicShaping(8).shape(string);
        }
        catch (Exception exception2) {
            return string;
        }
    }
}
