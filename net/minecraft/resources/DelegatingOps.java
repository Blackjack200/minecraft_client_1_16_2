package net.minecraft.resources;

import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.ListBuilder;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import com.mojang.serialization.MapLike;
import java.util.List;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public abstract class DelegatingOps<T> implements DynamicOps<T> {
    protected final DynamicOps<T> delegate;
    
    protected DelegatingOps(final DynamicOps<T> dynamicOps) {
        this.delegate = dynamicOps;
    }
    
    public T empty() {
        return (T)this.delegate.empty();
    }
    
    public <U> U convertTo(final DynamicOps<U> dynamicOps, final T object) {
        return (U)this.delegate.convertTo((DynamicOps)dynamicOps, object);
    }
    
    public DataResult<Number> getNumberValue(final T object) {
        return (DataResult<Number>)this.delegate.getNumberValue(object);
    }
    
    public T createNumeric(final Number number) {
        return (T)this.delegate.createNumeric(number);
    }
    
    public T createByte(final byte byte1) {
        return (T)this.delegate.createByte(byte1);
    }
    
    public T createShort(final short short1) {
        return (T)this.delegate.createShort(short1);
    }
    
    public T createInt(final int integer) {
        return (T)this.delegate.createInt(integer);
    }
    
    public T createLong(final long long1) {
        return (T)this.delegate.createLong(long1);
    }
    
    public T createFloat(final float float1) {
        return (T)this.delegate.createFloat(float1);
    }
    
    public T createDouble(final double double1) {
        return (T)this.delegate.createDouble(double1);
    }
    
    public DataResult<Boolean> getBooleanValue(final T object) {
        return (DataResult<Boolean>)this.delegate.getBooleanValue(object);
    }
    
    public T createBoolean(final boolean boolean1) {
        return (T)this.delegate.createBoolean(boolean1);
    }
    
    public DataResult<String> getStringValue(final T object) {
        return (DataResult<String>)this.delegate.getStringValue(object);
    }
    
    public T createString(final String string) {
        return (T)this.delegate.createString(string);
    }
    
    public DataResult<T> mergeToList(final T object1, final T object2) {
        return (DataResult<T>)this.delegate.mergeToList(object1, object2);
    }
    
    public DataResult<T> mergeToList(final T object, final List<T> list) {
        return (DataResult<T>)this.delegate.mergeToList(object, (List)list);
    }
    
    public DataResult<T> mergeToMap(final T object1, final T object2, final T object3) {
        return (DataResult<T>)this.delegate.mergeToMap(object1, object2, object3);
    }
    
    public DataResult<T> mergeToMap(final T object, final MapLike<T> mapLike) {
        return (DataResult<T>)this.delegate.mergeToMap(object, (MapLike)mapLike);
    }
    
    public DataResult<Stream<Pair<T, T>>> getMapValues(final T object) {
        return (DataResult<Stream<Pair<T, T>>>)this.delegate.getMapValues(object);
    }
    
    public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(final T object) {
        return (DataResult<Consumer<BiConsumer<T, T>>>)this.delegate.getMapEntries(object);
    }
    
    public T createMap(final Stream<Pair<T, T>> stream) {
        return (T)this.delegate.createMap((Stream)stream);
    }
    
    public DataResult<MapLike<T>> getMap(final T object) {
        return (DataResult<MapLike<T>>)this.delegate.getMap(object);
    }
    
    public DataResult<Stream<T>> getStream(final T object) {
        return (DataResult<Stream<T>>)this.delegate.getStream(object);
    }
    
    public DataResult<Consumer<Consumer<T>>> getList(final T object) {
        return (DataResult<Consumer<Consumer<T>>>)this.delegate.getList(object);
    }
    
    public T createList(final Stream<T> stream) {
        return (T)this.delegate.createList((Stream)stream);
    }
    
    public DataResult<ByteBuffer> getByteBuffer(final T object) {
        return (DataResult<ByteBuffer>)this.delegate.getByteBuffer(object);
    }
    
    public T createByteList(final ByteBuffer byteBuffer) {
        return (T)this.delegate.createByteList(byteBuffer);
    }
    
    public DataResult<IntStream> getIntStream(final T object) {
        return (DataResult<IntStream>)this.delegate.getIntStream(object);
    }
    
    public T createIntList(final IntStream intStream) {
        return (T)this.delegate.createIntList(intStream);
    }
    
    public DataResult<LongStream> getLongStream(final T object) {
        return (DataResult<LongStream>)this.delegate.getLongStream(object);
    }
    
    public T createLongList(final LongStream longStream) {
        return (T)this.delegate.createLongList(longStream);
    }
    
    public T remove(final T object, final String string) {
        return (T)this.delegate.remove(object, string);
    }
    
    public boolean compressMaps() {
        return this.delegate.compressMaps();
    }
    
    public ListBuilder<T> listBuilder() {
        return (ListBuilder<T>)this.delegate.listBuilder();
    }
    
    public RecordBuilder<T> mapBuilder() {
        return (RecordBuilder<T>)this.delegate.mapBuilder();
    }
}
