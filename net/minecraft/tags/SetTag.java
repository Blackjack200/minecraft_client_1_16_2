package net.minecraft.tags;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import com.google.common.collect.ImmutableList;

public class SetTag<T> implements Tag<T> {
    private final ImmutableList<T> valuesList;
    private final Set<T> values;
    @VisibleForTesting
    protected final Class<?> closestCommonSuperType;
    
    protected SetTag(final Set<T> set, final Class<?> class2) {
        this.closestCommonSuperType = class2;
        this.values = set;
        this.valuesList = (ImmutableList<T>)ImmutableList.copyOf((Collection)set);
    }
    
    public static <T> SetTag<T> empty() {
        return new SetTag<T>((java.util.Set<T>)ImmutableSet.of(), Void.class);
    }
    
    public static <T> SetTag<T> create(final Set<T> set) {
        return new SetTag<T>(set, SetTag.<T>findCommonSuperClass(set));
    }
    
    public boolean contains(final T object) {
        return this.closestCommonSuperType.isInstance(object) && this.values.contains(object);
    }
    
    public List<T> getValues() {
        return (List<T>)this.valuesList;
    }
    
    private static <T> Class<?> findCommonSuperClass(final Set<T> set) {
        if (set.isEmpty()) {
            return Void.class;
        }
        Class<?> class2 = null;
        for (final T object4 : set) {
            if (class2 == null) {
                class2 = object4.getClass();
            }
            else {
                class2 = findClosestAncestor(class2, object4.getClass());
            }
        }
        return class2;
    }
    
    private static Class<?> findClosestAncestor(Class<?> class1, final Class<?> class2) {
        while (!class1.isAssignableFrom((Class)class2)) {
            class1 = class1.getSuperclass();
        }
        return class1;
    }
}
