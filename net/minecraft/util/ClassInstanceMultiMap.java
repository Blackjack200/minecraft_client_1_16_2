package net.minecraft.util;

import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.AbstractCollection;

public class ClassInstanceMultiMap<T> extends AbstractCollection<T> {
    private final Map<Class<?>, List<T>> byClass;
    private final Class<T> baseClass;
    private final List<T> allInstances;
    
    public ClassInstanceMultiMap(final Class<T> class1) {
        this.byClass = (Map<Class<?>, List<T>>)Maps.newHashMap();
        this.allInstances = (List<T>)Lists.newArrayList();
        this.baseClass = class1;
        this.byClass.put(class1, this.allInstances);
    }
    
    public boolean add(final T object) {
        boolean boolean3 = false;
        for (final Map.Entry<Class<?>, List<T>> entry5 : this.byClass.entrySet()) {
            if (((Class)entry5.getKey()).isInstance(object)) {
                boolean3 |= ((List)entry5.getValue()).add(object);
            }
        }
        return boolean3;
    }
    
    public boolean remove(final Object object) {
        boolean boolean3 = false;
        for (final Map.Entry<Class<?>, List<T>> entry5 : this.byClass.entrySet()) {
            if (((Class)entry5.getKey()).isInstance(object)) {
                final List<T> list6 = (List<T>)entry5.getValue();
                boolean3 |= list6.remove(object);
            }
        }
        return boolean3;
    }
    
    public boolean contains(final Object object) {
        return this.find((java.lang.Class<Object>)object.getClass()).contains(object);
    }
    
    public <S> Collection<S> find(final Class<S> class1) {
        if (!this.baseClass.isAssignableFrom((Class)class1)) {
            throw new IllegalArgumentException(new StringBuilder().append("Don't know how to search for ").append(class1).toString());
        }
        final List<T> list3 = (List<T>)this.byClass.computeIfAbsent(class1, class1 -> (List)this.allInstances.stream().filter(class1::isInstance).collect(Collectors.toList()));
        return (Collection<S>)Collections.unmodifiableCollection((Collection)list3);
    }
    
    public Iterator<T> iterator() {
        if (this.allInstances.isEmpty()) {
            return (Iterator<T>)Collections.emptyIterator();
        }
        return (Iterator<T>)Iterators.unmodifiableIterator(this.allInstances.iterator());
    }
    
    public List<T> getAllInstances() {
        return (List<T>)ImmutableList.copyOf((Collection)this.allInstances);
    }
    
    public int size() {
        return this.allInstances.size();
    }
}
