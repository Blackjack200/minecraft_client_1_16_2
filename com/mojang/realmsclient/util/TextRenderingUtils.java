package com.mojang.realmsclient.util;

import java.util.Objects;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.List;

public class TextRenderingUtils {
    @VisibleForTesting
    protected static List<String> lineBreak(final String string) {
        return (List<String>)Arrays.asList((Object[])string.split("\\n"));
    }
    
    public static List<Line> decompose(final String string, final LineSegment... arr) {
        return decompose(string, (List<LineSegment>)Arrays.asList((Object[])arr));
    }
    
    private static List<Line> decompose(final String string, final List<LineSegment> list) {
        final List<String> list2 = lineBreak(string);
        return insertLinks(list2, list);
    }
    
    private static List<Line> insertLinks(final List<String> list1, final List<LineSegment> list2) {
        int integer3 = 0;
        final List<Line> list3 = (List<Line>)Lists.newArrayList();
        for (final String string6 : list1) {
            final List<LineSegment> list4 = (List<LineSegment>)Lists.newArrayList();
            final List<String> list5 = split(string6, "%link");
            for (final String string7 : list5) {
                if ("%link".equals(string7)) {
                    list4.add(list2.get(integer3++));
                }
                else {
                    list4.add(LineSegment.text(string7));
                }
            }
            list3.add(new Line(list4));
        }
        return list3;
    }
    
    public static List<String> split(final String string1, final String string2) {
        if (string2.isEmpty()) {
            throw new IllegalArgumentException("Delimiter cannot be the empty string");
        }
        final List<String> list3 = (List<String>)Lists.newArrayList();
        int integer4;
        int integer5;
        for (integer4 = 0; (integer5 = string1.indexOf(string2, integer4)) != -1; integer4 = integer5 + string2.length()) {
            if (integer5 > integer4) {
                list3.add(string1.substring(integer4, integer5));
            }
            list3.add(string2);
        }
        if (integer4 < string1.length()) {
            list3.add(string1.substring(integer4));
        }
        return list3;
    }
    
    public static class Line {
        public final List<LineSegment> segments;
        
        Line(final List<LineSegment> list) {
            this.segments = list;
        }
        
        public String toString() {
            return new StringBuilder().append("Line{segments=").append(this.segments).append('}').toString();
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final Line a3 = (Line)object;
            return Objects.equals(this.segments, a3.segments);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.segments });
        }
    }
    
    public static class LineSegment {
        private final String fullText;
        private final String linkTitle;
        private final String linkUrl;
        
        private LineSegment(final String string) {
            this.fullText = string;
            this.linkTitle = null;
            this.linkUrl = null;
        }
        
        private LineSegment(final String string1, final String string2, final String string3) {
            this.fullText = string1;
            this.linkTitle = string2;
            this.linkUrl = string3;
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final LineSegment b3 = (LineSegment)object;
            return Objects.equals(this.fullText, b3.fullText) && Objects.equals(this.linkTitle, b3.linkTitle) && Objects.equals(this.linkUrl, b3.linkUrl);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.fullText, this.linkTitle, this.linkUrl });
        }
        
        public String toString() {
            return "Segment{fullText='" + this.fullText + '\'' + ", linkTitle='" + this.linkTitle + '\'' + ", linkUrl='" + this.linkUrl + '\'' + '}';
        }
        
        public String renderedText() {
            return this.isLink() ? this.linkTitle : this.fullText;
        }
        
        public boolean isLink() {
            return this.linkTitle != null;
        }
        
        public String getLinkUrl() {
            if (!this.isLink()) {
                throw new IllegalStateException(new StringBuilder().append("Not a link: ").append(this).toString());
            }
            return this.linkUrl;
        }
        
        public static LineSegment link(final String string1, final String string2) {
            return new LineSegment(null, string1, string2);
        }
        
        @VisibleForTesting
        protected static LineSegment text(final String string) {
            return new LineSegment(string);
        }
    }
}
