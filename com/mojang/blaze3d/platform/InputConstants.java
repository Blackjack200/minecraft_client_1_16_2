package com.mojang.blaze3d.platform;

import com.google.common.collect.Maps;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.Map;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.locale.Language;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import org.lwjgl.glfw.GLFWDropCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFW;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;

public class InputConstants {
    @Nullable
    private static final MethodHandle glfwRawMouseMotionSupported;
    private static final int GLFW_RAW_MOUSE_MOTION;
    public static final Key UNKNOWN;
    
    public static Key getKey(final int integer1, final int integer2) {
        if (integer1 == -1) {
            return Type.SCANCODE.getOrCreate(integer2);
        }
        return Type.KEYSYM.getOrCreate(integer1);
    }
    
    public static Key getKey(final String string) {
        if (Key.NAME_MAP.containsKey(string)) {
            return (Key)Key.NAME_MAP.get(string);
        }
        for (final Type b5 : Type.values()) {
            if (string.startsWith(b5.defaultPrefix)) {
                final String string2 = string.substring(b5.defaultPrefix.length() + 1);
                return b5.getOrCreate(Integer.parseInt(string2));
            }
        }
        throw new IllegalArgumentException("Unknown key name: " + string);
    }
    
    public static boolean isKeyDown(final long long1, final int integer) {
        return GLFW.glfwGetKey(long1, integer) == 1;
    }
    
    public static void setupKeyboardCallbacks(final long long1, final GLFWKeyCallbackI gLFWKeyCallbackI, final GLFWCharModsCallbackI gLFWCharModsCallbackI) {
        GLFW.glfwSetKeyCallback(long1, gLFWKeyCallbackI);
        GLFW.glfwSetCharModsCallback(long1, gLFWCharModsCallbackI);
    }
    
    public static void setupMouseCallbacks(final long long1, final GLFWCursorPosCallbackI gLFWCursorPosCallbackI, final GLFWMouseButtonCallbackI gLFWMouseButtonCallbackI, final GLFWScrollCallbackI gLFWScrollCallbackI, final GLFWDropCallbackI gLFWDropCallbackI) {
        GLFW.glfwSetCursorPosCallback(long1, gLFWCursorPosCallbackI);
        GLFW.glfwSetMouseButtonCallback(long1, gLFWMouseButtonCallbackI);
        GLFW.glfwSetScrollCallback(long1, gLFWScrollCallbackI);
        GLFW.glfwSetDropCallback(long1, gLFWDropCallbackI);
    }
    
    public static void grabOrReleaseMouse(final long long1, final int integer, final double double3, final double double4) {
        GLFW.glfwSetCursorPos(long1, double3, double4);
        GLFW.glfwSetInputMode(long1, 208897, integer);
    }
    
    public static boolean isRawMouseInputSupported() {
        try {
            return InputConstants.glfwRawMouseMotionSupported != null && InputConstants.glfwRawMouseMotionSupported.invokeExact();
        }
        catch (Throwable throwable1) {
            throw new RuntimeException(throwable1);
        }
    }
    
    public static void updateRawMouseInput(final long long1, final boolean boolean2) {
        if (isRawMouseInputSupported()) {
            GLFW.glfwSetInputMode(long1, InputConstants.GLFW_RAW_MOUSE_MOTION, (int)(boolean2 ? 1 : 0));
        }
    }
    
    static {
        final MethodHandles.Lookup lookup1 = MethodHandles.lookup();
        final MethodType methodType2 = MethodType.methodType(Boolean.TYPE);
        MethodHandle methodHandle3 = null;
        int integer4 = 0;
        try {
            methodHandle3 = lookup1.findStatic((Class)GLFW.class, "glfwRawMouseMotionSupported", methodType2);
            final MethodHandle methodHandle4 = lookup1.findStaticGetter((Class)GLFW.class, "GLFW_RAW_MOUSE_MOTION", Integer.TYPE);
            integer4 = methodHandle4.invokeExact();
        }
        catch (NoSuchMethodException | NoSuchFieldException ex) {}
        catch (Throwable throwable5) {
            throw new RuntimeException(throwable5);
        }
        glfwRawMouseMotionSupported = methodHandle3;
        GLFW_RAW_MOUSE_MOTION = integer4;
        UNKNOWN = Type.KEYSYM.getOrCreate(-1);
    }
    
    public enum Type {
        KEYSYM("key.keyboard", (BiFunction<Integer, String, Component>)((integer, string) -> {
            final String string2 = GLFW.glfwGetKeyName((int)integer, -1);
            return (string2 != null) ? new TextComponent(string2) : new TranslatableComponent(string);
        })), 
        SCANCODE("scancode", (BiFunction<Integer, String, Component>)((integer, string) -> {
            final String string2 = GLFW.glfwGetKeyName(-1, (int)integer);
            return (string2 != null) ? new TextComponent(string2) : new TranslatableComponent(string);
        })), 
        MOUSE("key.mouse", (BiFunction<Integer, String, Component>)((integer, string) -> Language.getInstance().has(string) ? new TranslatableComponent(string) : new TranslatableComponent("key.mouse", new Object[] { integer + 1 })));
        
        private final Int2ObjectMap<Key> map;
        private final String defaultPrefix;
        private final BiFunction<Integer, String, Component> displayTextSupplier;
        
        private static void addKey(final Type b, final String string, final int integer) {
            final Key a4 = new Key(string, b, integer);
            b.map.put(integer, a4);
        }
        
        private Type(final String string3, final BiFunction<Integer, String, Component> biFunction) {
            this.map = (Int2ObjectMap<Key>)new Int2ObjectOpenHashMap();
            this.defaultPrefix = string3;
            this.displayTextSupplier = biFunction;
        }
        
        public Key getOrCreate(final int integer) {
            return (Key)this.map.computeIfAbsent(integer, integer -> {
                int integer2 = integer;
                if (this == Type.MOUSE) {
                    ++integer2;
                }
                final String string4 = this.defaultPrefix + "." + integer2;
                return new Key(string4, this, integer);
            });
        }
        
        static {
            addKey(Type.KEYSYM, "key.keyboard.unknown", -1);
            addKey(Type.MOUSE, "key.mouse.left", 0);
            addKey(Type.MOUSE, "key.mouse.right", 1);
            addKey(Type.MOUSE, "key.mouse.middle", 2);
            addKey(Type.MOUSE, "key.mouse.4", 3);
            addKey(Type.MOUSE, "key.mouse.5", 4);
            addKey(Type.MOUSE, "key.mouse.6", 5);
            addKey(Type.MOUSE, "key.mouse.7", 6);
            addKey(Type.MOUSE, "key.mouse.8", 7);
            addKey(Type.KEYSYM, "key.keyboard.0", 48);
            addKey(Type.KEYSYM, "key.keyboard.1", 49);
            addKey(Type.KEYSYM, "key.keyboard.2", 50);
            addKey(Type.KEYSYM, "key.keyboard.3", 51);
            addKey(Type.KEYSYM, "key.keyboard.4", 52);
            addKey(Type.KEYSYM, "key.keyboard.5", 53);
            addKey(Type.KEYSYM, "key.keyboard.6", 54);
            addKey(Type.KEYSYM, "key.keyboard.7", 55);
            addKey(Type.KEYSYM, "key.keyboard.8", 56);
            addKey(Type.KEYSYM, "key.keyboard.9", 57);
            addKey(Type.KEYSYM, "key.keyboard.a", 65);
            addKey(Type.KEYSYM, "key.keyboard.b", 66);
            addKey(Type.KEYSYM, "key.keyboard.c", 67);
            addKey(Type.KEYSYM, "key.keyboard.d", 68);
            addKey(Type.KEYSYM, "key.keyboard.e", 69);
            addKey(Type.KEYSYM, "key.keyboard.f", 70);
            addKey(Type.KEYSYM, "key.keyboard.g", 71);
            addKey(Type.KEYSYM, "key.keyboard.h", 72);
            addKey(Type.KEYSYM, "key.keyboard.i", 73);
            addKey(Type.KEYSYM, "key.keyboard.j", 74);
            addKey(Type.KEYSYM, "key.keyboard.k", 75);
            addKey(Type.KEYSYM, "key.keyboard.l", 76);
            addKey(Type.KEYSYM, "key.keyboard.m", 77);
            addKey(Type.KEYSYM, "key.keyboard.n", 78);
            addKey(Type.KEYSYM, "key.keyboard.o", 79);
            addKey(Type.KEYSYM, "key.keyboard.p", 80);
            addKey(Type.KEYSYM, "key.keyboard.q", 81);
            addKey(Type.KEYSYM, "key.keyboard.r", 82);
            addKey(Type.KEYSYM, "key.keyboard.s", 83);
            addKey(Type.KEYSYM, "key.keyboard.t", 84);
            addKey(Type.KEYSYM, "key.keyboard.u", 85);
            addKey(Type.KEYSYM, "key.keyboard.v", 86);
            addKey(Type.KEYSYM, "key.keyboard.w", 87);
            addKey(Type.KEYSYM, "key.keyboard.x", 88);
            addKey(Type.KEYSYM, "key.keyboard.y", 89);
            addKey(Type.KEYSYM, "key.keyboard.z", 90);
            addKey(Type.KEYSYM, "key.keyboard.f1", 290);
            addKey(Type.KEYSYM, "key.keyboard.f2", 291);
            addKey(Type.KEYSYM, "key.keyboard.f3", 292);
            addKey(Type.KEYSYM, "key.keyboard.f4", 293);
            addKey(Type.KEYSYM, "key.keyboard.f5", 294);
            addKey(Type.KEYSYM, "key.keyboard.f6", 295);
            addKey(Type.KEYSYM, "key.keyboard.f7", 296);
            addKey(Type.KEYSYM, "key.keyboard.f8", 297);
            addKey(Type.KEYSYM, "key.keyboard.f9", 298);
            addKey(Type.KEYSYM, "key.keyboard.f10", 299);
            addKey(Type.KEYSYM, "key.keyboard.f11", 300);
            addKey(Type.KEYSYM, "key.keyboard.f12", 301);
            addKey(Type.KEYSYM, "key.keyboard.f13", 302);
            addKey(Type.KEYSYM, "key.keyboard.f14", 303);
            addKey(Type.KEYSYM, "key.keyboard.f15", 304);
            addKey(Type.KEYSYM, "key.keyboard.f16", 305);
            addKey(Type.KEYSYM, "key.keyboard.f17", 306);
            addKey(Type.KEYSYM, "key.keyboard.f18", 307);
            addKey(Type.KEYSYM, "key.keyboard.f19", 308);
            addKey(Type.KEYSYM, "key.keyboard.f20", 309);
            addKey(Type.KEYSYM, "key.keyboard.f21", 310);
            addKey(Type.KEYSYM, "key.keyboard.f22", 311);
            addKey(Type.KEYSYM, "key.keyboard.f23", 312);
            addKey(Type.KEYSYM, "key.keyboard.f24", 313);
            addKey(Type.KEYSYM, "key.keyboard.f25", 314);
            addKey(Type.KEYSYM, "key.keyboard.num.lock", 282);
            addKey(Type.KEYSYM, "key.keyboard.keypad.0", 320);
            addKey(Type.KEYSYM, "key.keyboard.keypad.1", 321);
            addKey(Type.KEYSYM, "key.keyboard.keypad.2", 322);
            addKey(Type.KEYSYM, "key.keyboard.keypad.3", 323);
            addKey(Type.KEYSYM, "key.keyboard.keypad.4", 324);
            addKey(Type.KEYSYM, "key.keyboard.keypad.5", 325);
            addKey(Type.KEYSYM, "key.keyboard.keypad.6", 326);
            addKey(Type.KEYSYM, "key.keyboard.keypad.7", 327);
            addKey(Type.KEYSYM, "key.keyboard.keypad.8", 328);
            addKey(Type.KEYSYM, "key.keyboard.keypad.9", 329);
            addKey(Type.KEYSYM, "key.keyboard.keypad.add", 334);
            addKey(Type.KEYSYM, "key.keyboard.keypad.decimal", 330);
            addKey(Type.KEYSYM, "key.keyboard.keypad.enter", 335);
            addKey(Type.KEYSYM, "key.keyboard.keypad.equal", 336);
            addKey(Type.KEYSYM, "key.keyboard.keypad.multiply", 332);
            addKey(Type.KEYSYM, "key.keyboard.keypad.divide", 331);
            addKey(Type.KEYSYM, "key.keyboard.keypad.subtract", 333);
            addKey(Type.KEYSYM, "key.keyboard.down", 264);
            addKey(Type.KEYSYM, "key.keyboard.left", 263);
            addKey(Type.KEYSYM, "key.keyboard.right", 262);
            addKey(Type.KEYSYM, "key.keyboard.up", 265);
            addKey(Type.KEYSYM, "key.keyboard.apostrophe", 39);
            addKey(Type.KEYSYM, "key.keyboard.backslash", 92);
            addKey(Type.KEYSYM, "key.keyboard.comma", 44);
            addKey(Type.KEYSYM, "key.keyboard.equal", 61);
            addKey(Type.KEYSYM, "key.keyboard.grave.accent", 96);
            addKey(Type.KEYSYM, "key.keyboard.left.bracket", 91);
            addKey(Type.KEYSYM, "key.keyboard.minus", 45);
            addKey(Type.KEYSYM, "key.keyboard.period", 46);
            addKey(Type.KEYSYM, "key.keyboard.right.bracket", 93);
            addKey(Type.KEYSYM, "key.keyboard.semicolon", 59);
            addKey(Type.KEYSYM, "key.keyboard.slash", 47);
            addKey(Type.KEYSYM, "key.keyboard.space", 32);
            addKey(Type.KEYSYM, "key.keyboard.tab", 258);
            addKey(Type.KEYSYM, "key.keyboard.left.alt", 342);
            addKey(Type.KEYSYM, "key.keyboard.left.control", 341);
            addKey(Type.KEYSYM, "key.keyboard.left.shift", 340);
            addKey(Type.KEYSYM, "key.keyboard.left.win", 343);
            addKey(Type.KEYSYM, "key.keyboard.right.alt", 346);
            addKey(Type.KEYSYM, "key.keyboard.right.control", 345);
            addKey(Type.KEYSYM, "key.keyboard.right.shift", 344);
            addKey(Type.KEYSYM, "key.keyboard.right.win", 347);
            addKey(Type.KEYSYM, "key.keyboard.enter", 257);
            addKey(Type.KEYSYM, "key.keyboard.escape", 256);
            addKey(Type.KEYSYM, "key.keyboard.backspace", 259);
            addKey(Type.KEYSYM, "key.keyboard.delete", 261);
            addKey(Type.KEYSYM, "key.keyboard.end", 269);
            addKey(Type.KEYSYM, "key.keyboard.home", 268);
            addKey(Type.KEYSYM, "key.keyboard.insert", 260);
            addKey(Type.KEYSYM, "key.keyboard.page.down", 267);
            addKey(Type.KEYSYM, "key.keyboard.page.up", 266);
            addKey(Type.KEYSYM, "key.keyboard.caps.lock", 280);
            addKey(Type.KEYSYM, "key.keyboard.pause", 284);
            addKey(Type.KEYSYM, "key.keyboard.scroll.lock", 281);
            addKey(Type.KEYSYM, "key.keyboard.menu", 348);
            addKey(Type.KEYSYM, "key.keyboard.print.screen", 283);
            addKey(Type.KEYSYM, "key.keyboard.world.1", 161);
            addKey(Type.KEYSYM, "key.keyboard.world.2", 162);
        }
    }
    
    public static final class Key {
        private final String name;
        private final Type type;
        private final int value;
        private final LazyLoadedValue<Component> displayName;
        private static final Map<String, Key> NAME_MAP;
        
        private Key(final String string, final Type b, final int integer) {
            this.name = string;
            this.type = b;
            this.value = integer;
            this.displayName = new LazyLoadedValue<Component>((java.util.function.Supplier<Component>)(() -> (Component)b.displayTextSupplier.apply(integer, string)));
            Key.NAME_MAP.put(string, this);
        }
        
        public Type getType() {
            return this.type;
        }
        
        public int getValue() {
            return this.value;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Component getDisplayName() {
            return this.displayName.get();
        }
        
        public OptionalInt getNumericKeyValue() {
            if (this.value >= 48 && this.value <= 57) {
                return OptionalInt.of(this.value - 48);
            }
            if (this.value >= 320 && this.value <= 329) {
                return OptionalInt.of(this.value - 320);
            }
            return OptionalInt.empty();
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final Key a3 = (Key)object;
            return this.value == a3.value && this.type == a3.type;
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.type, this.value });
        }
        
        public String toString() {
            return this.name;
        }
        
        static {
            NAME_MAP = (Map)Maps.newHashMap();
        }
    }
}
