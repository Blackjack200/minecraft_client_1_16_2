package net.minecraft.util;

import java.util.Locale;
import javax.annotation.Nullable;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;

public class LowerCaseEnumTypeAdapterFactory implements TypeAdapterFactory {
    @Nullable
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Class<T> class4 = (Class<T>)typeToken.getRawType();
        if (!class4.isEnum()) {
            return null;
        }
        final Map<String, T> map5 = (Map<String, T>)Maps.newHashMap();
        for (final T object9 : class4.getEnumConstants()) {
            map5.put(this.toLowercase(object9), object9);
        }
        return new TypeAdapter<T>() {
            public void write(final JsonWriter jsonWriter, final T object) throws IOException {
                if (object == null) {
                    jsonWriter.nullValue();
                }
                else {
                    jsonWriter.value(LowerCaseEnumTypeAdapterFactory.this.toLowercase(object));
                }
            }
            
            @Nullable
            public T read(final JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return (T)map5.get(jsonReader.nextString());
            }
        };
    }
    
    private String toLowercase(final Object object) {
        if (object instanceof Enum) {
            return ((Enum)object).name().toLowerCase(Locale.ROOT);
        }
        return object.toString().toLowerCase(Locale.ROOT);
    }
}
