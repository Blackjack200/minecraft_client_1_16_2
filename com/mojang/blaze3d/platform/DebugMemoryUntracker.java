package com.mojang.blaze3d.platform;

import java.util.function.Supplier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandles;
import org.lwjgl.system.Pointer;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;

public class DebugMemoryUntracker {
    @Nullable
    private static final MethodHandle UNTRACK;
    
    public static void untrack(final long long1) {
        if (DebugMemoryUntracker.UNTRACK == null) {
            return;
        }
        try {
            DebugMemoryUntracker.UNTRACK.invoke(long1);
        }
        catch (Throwable throwable3) {
            throw new RuntimeException(throwable3);
        }
    }
    
    public static void untrack(final Pointer pointer) {
        untrack(pointer.address());
    }
    
    static {
        UNTRACK = GLX.<MethodHandle>make((java.util.function.Supplier<MethodHandle>)(() -> {
            try {
                final MethodHandles.Lookup lookup1 = MethodHandles.lookup();
                final Class<?> class2 = Class.forName("org.lwjgl.system.MemoryManage$DebugAllocator");
                final Method method3 = class2.getDeclaredMethod("untrack", new Class[] { Long.TYPE });
                method3.setAccessible(true);
                final Field field4 = Class.forName("org.lwjgl.system.MemoryUtil$LazyInit").getDeclaredField("ALLOCATOR");
                field4.setAccessible(true);
                final Object object5 = field4.get(null);
                if (class2.isInstance(object5)) {
                    return lookup1.unreflect(method3);
                }
                try {
                    return null;
                }
                catch (NoSuchMethodException | NoSuchFieldException ex2) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException reflectiveOperationException1 = ex;
                    throw new RuntimeException((Throwable)reflectiveOperationException1);
                }
            }
            catch (ClassNotFoundException ex3) {}
            catch (NoSuchMethodException ex4) {}
            catch (NoSuchFieldException ex5) {}
            catch (IllegalAccessException ex6) {}
        }));
    }
}
