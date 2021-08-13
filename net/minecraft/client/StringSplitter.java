package net.minecraft.client;

import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.function.BiConsumer;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableObject;
import java.util.Optional;
import org.apache.commons.lang3.mutable.MutableInt;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.StringDecomposer;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.mutable.MutableFloat;
import javax.annotation.Nullable;

public class StringSplitter {
    private final WidthProvider widthProvider;
    
    public StringSplitter(final WidthProvider f) {
        this.widthProvider = f;
    }
    
    public float stringWidth(@Nullable final String string) {
        if (string == null) {
            return 0.0f;
        }
        final MutableFloat mutableFloat3 = new MutableFloat();
        StringDecomposer.iterateFormatted(string, Style.EMPTY, (integer2, ob, integer4) -> {
            mutableFloat3.add(this.widthProvider.getWidth(integer4, ob));
            return true;
        });
        return mutableFloat3.floatValue();
    }
    
    public float stringWidth(final FormattedText nu) {
        final MutableFloat mutableFloat3 = new MutableFloat();
        StringDecomposer.iterateFormatted(nu, Style.EMPTY, (integer2, ob, integer4) -> {
            mutableFloat3.add(this.widthProvider.getWidth(integer4, ob));
            return true;
        });
        return mutableFloat3.floatValue();
    }
    
    public float stringWidth(final FormattedCharSequence aex) {
        final MutableFloat mutableFloat3 = new MutableFloat();
        aex.accept((integer2, ob, integer4) -> {
            mutableFloat3.add(this.widthProvider.getWidth(integer4, ob));
            return true;
        });
        return mutableFloat3.floatValue();
    }
    
    public int plainIndexAtWidth(final String string, final int integer, final Style ob) {
        final WidthLimitedCharSink e5 = new WidthLimitedCharSink((float)integer);
        StringDecomposer.iterate(string, ob, e5);
        return e5.getPosition();
    }
    
    public String plainHeadByWidth(final String string, final int integer, final Style ob) {
        return string.substring(0, this.plainIndexAtWidth(string, integer, ob));
    }
    
    public String plainTailByWidth(final String string, final int integer, final Style ob) {
        final MutableFloat mutableFloat5 = new MutableFloat();
        final MutableInt mutableInt6 = new MutableInt(string.length());
        final MutableFloat mutableFloat6;
        final float float8;
        final MutableInt mutableInt7;
        StringDecomposer.iterateBackwards(string, ob, (integer4, ob, integer6) -> {
            float8 = mutableFloat6.addAndGet(this.widthProvider.getWidth(integer6, ob));
            if (float8 > integer) {
                return false;
            }
            else {
                mutableInt7.setValue(integer4);
                return true;
            }
        });
        return string.substring(mutableInt6.intValue());
    }
    
    @Nullable
    public Style componentStyleAtWidth(final FormattedText nu, final int integer) {
        final WidthLimitedCharSink e4 = new WidthLimitedCharSink((float)integer);
        return (Style)nu.visit((ob, string) -> StringDecomposer.iterateFormatted(string, ob, e4) ? Optional.empty() : Optional.of(ob), Style.EMPTY).orElse(null);
    }
    
    @Nullable
    public Style componentStyleAtWidth(final FormattedCharSequence aex, final int integer) {
        final WidthLimitedCharSink e4 = new WidthLimitedCharSink((float)integer);
        final MutableObject<Style> mutableObject5 = (MutableObject<Style>)new MutableObject();
        final WidthLimitedCharSink widthLimitedCharSink;
        final MutableObject mutableObject6;
        aex.accept((integer3, ob, integer5) -> {
            if (!widthLimitedCharSink.accept(integer3, ob, integer5)) {
                mutableObject6.setValue(ob);
                return false;
            }
            else {
                return true;
            }
        });
        return (Style)mutableObject5.getValue();
    }
    
    public FormattedText headByWidth(final FormattedText nu, final int integer, final Style ob) {
        final WidthLimitedCharSink e5 = new WidthLimitedCharSink((float)integer);
        return (FormattedText)nu.visit((FormattedText.StyledContentConsumer<Object>)new FormattedText.StyledContentConsumer<FormattedText>() {
            private final ComponentCollector collector = new ComponentCollector();
            
            public Optional<FormattedText> accept(final Style ob, final String string) {
                e5.resetPosition();
                if (!StringDecomposer.iterateFormatted(string, ob, e5)) {
                    final String string2 = string.substring(0, e5.getPosition());
                    if (!string2.isEmpty()) {
                        this.collector.append(FormattedText.of(string2, ob));
                    }
                    return (Optional<FormattedText>)Optional.of(this.collector.getResultOrEmpty());
                }
                if (!string.isEmpty()) {
                    this.collector.append(FormattedText.of(string, ob));
                }
                return (Optional<FormattedText>)Optional.empty();
            }
        }, ob).orElse(nu);
    }
    
    public static int getWordPosition(final String string, final int integer2, final int integer3, final boolean boolean4) {
        int integer4 = integer3;
        final boolean boolean5 = integer2 < 0;
        for (int integer5 = Math.abs(integer2), integer6 = 0; integer6 < integer5; ++integer6) {
            if (boolean5) {
                while (boolean4 && integer4 > 0 && (string.charAt(integer4 - 1) == ' ' || string.charAt(integer4 - 1) == '\n')) {
                    --integer4;
                }
                while (integer4 > 0 && string.charAt(integer4 - 1) != ' ' && string.charAt(integer4 - 1) != '\n') {
                    --integer4;
                }
            }
            else {
                final int integer7 = string.length();
                final int integer8 = string.indexOf(32, integer4);
                final int integer9 = string.indexOf(10, integer4);
                if (integer8 == -1 && integer9 == -1) {
                    integer4 = -1;
                }
                else if (integer8 != -1 && integer9 != -1) {
                    integer4 = Math.min(integer8, integer9);
                }
                else if (integer8 != -1) {
                    integer4 = integer8;
                }
                else {
                    integer4 = integer9;
                }
                if (integer4 == -1) {
                    integer4 = integer7;
                }
                else {
                    while (boolean4 && integer4 < integer7 && (string.charAt(integer4) == ' ' || string.charAt(integer4) == '\n')) {
                        ++integer4;
                    }
                }
            }
        }
        return integer4;
    }
    
    public void splitLines(final String string, final int integer, final Style ob, final boolean boolean4, final LinePosConsumer d) {
        int integer2 = 0;
        final int integer3 = string.length();
        Style ob2 = ob;
        while (integer2 < integer3) {
            final LineBreakFinder b10 = new LineBreakFinder((float)integer);
            final boolean boolean5 = StringDecomposer.iterateFormatted(string, integer2, ob2, ob, b10);
            if (boolean5) {
                d.accept(ob2, integer2, integer3);
                break;
            }
            final int integer4 = b10.getSplitPosition();
            final char character13 = string.charAt(integer4);
            final int integer5 = (character13 == '\n' || character13 == ' ') ? (integer4 + 1) : integer4;
            d.accept(ob2, integer2, boolean4 ? integer5 : integer4);
            integer2 = integer5;
            ob2 = b10.getSplitStyle();
        }
    }
    
    public List<FormattedText> splitLines(final String string, final int integer, final Style ob) {
        final List<FormattedText> list5 = (List<FormattedText>)Lists.newArrayList();
        this.splitLines(string, integer, ob, false, (ob, integer4, integer5) -> list5.add(FormattedText.of(string.substring(integer4, integer5), ob)));
        return list5;
    }
    
    public List<FormattedText> splitLines(final FormattedText nu, final int integer, final Style ob) {
        final List<FormattedText> list5 = (List<FormattedText>)Lists.newArrayList();
        this.splitLines(nu, integer, ob, (BiConsumer<FormattedText, Boolean>)((nu, boolean3) -> list5.add(nu)));
        return list5;
    }
    
    public void splitLines(final FormattedText nu, final int integer, final Style ob, final BiConsumer<FormattedText, Boolean> biConsumer) {
        final List<LineComponent> list6 = (List<LineComponent>)Lists.newArrayList();
        final List list7;
        nu.visit((ob, string) -> {
            if (!string.isEmpty()) {
                list7.add(new LineComponent(string, ob));
            }
            return (java.util.Optional<Object>)Optional.empty();
        }, ob);
        final FlatComponents a7 = new FlatComponents(list6);
        boolean boolean8 = true;
        boolean boolean9 = false;
        boolean boolean10 = false;
        while (boolean8) {
            boolean8 = false;
            final LineBreakFinder b11 = new LineBreakFinder((float)integer);
            for (final LineComponent c13 : a7.parts) {
                final boolean boolean11 = StringDecomposer.iterateFormatted(c13.contents, 0, c13.style, ob, b11);
                if (!boolean11) {
                    final int integer2 = b11.getSplitPosition();
                    final Style ob2 = b11.getSplitStyle();
                    final char character17 = a7.charAt(integer2);
                    final boolean boolean12 = character17 == '\n';
                    final boolean boolean13 = boolean12 || character17 == ' ';
                    boolean9 = boolean12;
                    final FormattedText nu2 = a7.splitAt(integer2, boolean13 ? 1 : 0, ob2);
                    biConsumer.accept(nu2, boolean10);
                    boolean10 = !boolean12;
                    boolean8 = true;
                    break;
                }
                b11.addToOffset(c13.contents.length());
            }
        }
        final FormattedText nu3 = a7.getRemainder();
        if (nu3 != null) {
            biConsumer.accept(nu3, boolean10);
        }
        else if (boolean9) {
            biConsumer.accept(FormattedText.EMPTY, false);
        }
    }
    
    class WidthLimitedCharSink implements FormattedCharSink {
        private float maxWidth;
        private int position;
        
        public WidthLimitedCharSink(final float float2) {
            this.maxWidth = float2;
        }
        
        public boolean accept(final int integer1, final Style ob, final int integer3) {
            this.maxWidth -= StringSplitter.this.widthProvider.getWidth(integer3, ob);
            if (this.maxWidth >= 0.0f) {
                this.position = integer1 + Character.charCount(integer3);
                return true;
            }
            return false;
        }
        
        public int getPosition() {
            return this.position;
        }
        
        public void resetPosition() {
            this.position = 0;
        }
    }
    
    class LineBreakFinder implements FormattedCharSink {
        private final float maxWidth;
        private int lineBreak;
        private Style lineBreakStyle;
        private boolean hadNonZeroWidthChar;
        private float width;
        private int lastSpace;
        private Style lastSpaceStyle;
        private int nextChar;
        private int offset;
        
        public LineBreakFinder(final float float2) {
            this.lineBreak = -1;
            this.lineBreakStyle = Style.EMPTY;
            this.lastSpace = -1;
            this.lastSpaceStyle = Style.EMPTY;
            this.maxWidth = Math.max(float2, 1.0f);
        }
        
        public boolean accept(final int integer1, final Style ob, final int integer3) {
            final int integer4 = integer1 + this.offset;
            switch (integer3) {
                case 10: {
                    return this.finishIteration(integer4, ob);
                }
                case 32: {
                    this.lastSpace = integer4;
                    this.lastSpaceStyle = ob;
                    break;
                }
            }
            final float float6 = StringSplitter.this.widthProvider.getWidth(integer3, ob);
            this.width += float6;
            if (!this.hadNonZeroWidthChar || this.width <= this.maxWidth) {
                this.hadNonZeroWidthChar |= (float6 != 0.0f);
                this.nextChar = integer4 + Character.charCount(integer3);
                return true;
            }
            if (this.lastSpace != -1) {
                return this.finishIteration(this.lastSpace, this.lastSpaceStyle);
            }
            return this.finishIteration(integer4, ob);
        }
        
        private boolean finishIteration(final int integer, final Style ob) {
            this.lineBreak = integer;
            this.lineBreakStyle = ob;
            return false;
        }
        
        private boolean lineBreakFound() {
            return this.lineBreak != -1;
        }
        
        public int getSplitPosition() {
            return this.lineBreakFound() ? this.lineBreak : this.nextChar;
        }
        
        public Style getSplitStyle() {
            return this.lineBreakStyle;
        }
        
        public void addToOffset(final int integer) {
            this.offset += integer;
        }
    }
    
    static class LineComponent implements FormattedText {
        private final String contents;
        private final Style style;
        
        public LineComponent(final String string, final Style ob) {
            this.contents = string;
            this.style = ob;
        }
        
        public <T> Optional<T> visit(final ContentConsumer<T> a) {
            return a.accept(this.contents);
        }
        
        public <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob) {
            return b.accept(this.style.applyTo(ob), this.contents);
        }
    }
    
    static class FlatComponents {
        private final List<LineComponent> parts;
        private String flatParts;
        
        public FlatComponents(final List<LineComponent> list) {
            this.parts = list;
            this.flatParts = (String)list.stream().map(c -> c.contents).collect(Collectors.joining());
        }
        
        public char charAt(final int integer) {
            return this.flatParts.charAt(integer);
        }
        
        public FormattedText splitAt(final int integer1, final int integer2, final Style ob) {
            final ComponentCollector djl5 = new ComponentCollector();
            final ListIterator<LineComponent> listIterator6 = (ListIterator<LineComponent>)this.parts.listIterator();
            int integer3 = integer1;
            boolean boolean8 = false;
            while (listIterator6.hasNext()) {
                final LineComponent c9 = (LineComponent)listIterator6.next();
                final String string10 = c9.contents;
                final int integer4 = string10.length();
                if (!boolean8) {
                    if (integer3 > integer4) {
                        djl5.append(c9);
                        listIterator6.remove();
                        integer3 -= integer4;
                    }
                    else {
                        final String string11 = string10.substring(0, integer3);
                        if (!string11.isEmpty()) {
                            djl5.append(FormattedText.of(string11, c9.style));
                        }
                        integer3 += integer2;
                        boolean8 = true;
                    }
                }
                if (boolean8) {
                    if (integer3 > integer4) {
                        listIterator6.remove();
                        integer3 -= integer4;
                    }
                    else {
                        final String string11 = string10.substring(integer3);
                        if (string11.isEmpty()) {
                            listIterator6.remove();
                            break;
                        }
                        listIterator6.set(new LineComponent(string11, ob));
                        break;
                    }
                }
            }
            this.flatParts = this.flatParts.substring(integer1 + integer2);
            return djl5.getResultOrEmpty();
        }
        
        @Nullable
        public FormattedText getRemainder() {
            final ComponentCollector djl2 = new ComponentCollector();
            this.parts.forEach(djl2::append);
            this.parts.clear();
            return djl2.getResult();
        }
    }
    
    @FunctionalInterface
    public interface LinePosConsumer {
        void accept(final Style ob, final int integer2, final int integer3);
    }
    
    @FunctionalInterface
    public interface WidthProvider {
        float getWidth(final int integer, final Style ob);
    }
}
