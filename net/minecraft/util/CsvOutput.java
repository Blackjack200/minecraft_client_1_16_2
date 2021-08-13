package net.minecraft.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import javax.annotation.Nullable;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.List;
import java.io.Writer;

public class CsvOutput {
    private final Writer output;
    private final int columnCount;
    
    private CsvOutput(final Writer writer, final List<String> list) throws IOException {
        this.output = writer;
        this.columnCount = list.size();
        this.writeLine(list.stream());
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void writeRow(final Object... arr) throws IOException {
        if (arr.length != this.columnCount) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid number of columns, expected ").append(this.columnCount).append(", but got ").append(arr.length).toString());
        }
        this.writeLine(Stream.of(arr));
    }
    
    private void writeLine(final Stream<?> stream) throws IOException {
        this.output.write((String)stream.map(CsvOutput::getStringValue).collect(Collectors.joining(",")) + "\r\n");
    }
    
    private static String getStringValue(@Nullable final Object object) {
        return StringEscapeUtils.escapeCsv((object != null) ? object.toString() : "[null]");
    }
    
    public static class Builder {
        private final List<String> headers;
        
        public Builder() {
            this.headers = (List<String>)Lists.newArrayList();
        }
        
        public Builder addColumn(final String string) {
            this.headers.add(string);
            return this;
        }
        
        public CsvOutput build(final Writer writer) throws IOException {
            return new CsvOutput(writer, this.headers, null);
        }
    }
}
