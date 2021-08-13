package net.minecraft.client;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.HashMap;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import java.util.function.Supplier;
import net.minecraft.client.resources.language.I18n;
import java.util.Iterator;
import java.util.Set;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Map;

public class KeyMapping implements Comparable<KeyMapping> {
    private static final Map<String, KeyMapping> ALL;
    private static final Map<InputConstants.Key, KeyMapping> MAP;
    private static final Set<String> CATEGORIES;
    private static final Map<String, Integer> CATEGORY_SORT_ORDER;
    private final String name;
    private final InputConstants.Key defaultKey;
    private final String category;
    private InputConstants.Key key;
    private boolean isDown;
    private int clickCount;
    
    public static void click(final InputConstants.Key a) {
        final KeyMapping djt2 = (KeyMapping)KeyMapping.MAP.get(a);
        if (djt2 != null) {
            final KeyMapping keyMapping = djt2;
            ++keyMapping.clickCount;
        }
    }
    
    public static void set(final InputConstants.Key a, final boolean boolean2) {
        final KeyMapping djt3 = (KeyMapping)KeyMapping.MAP.get(a);
        if (djt3 != null) {
            djt3.setDown(boolean2);
        }
    }
    
    public static void setAll() {
        for (final KeyMapping djt2 : KeyMapping.ALL.values()) {
            if (djt2.key.getType() == InputConstants.Type.KEYSYM && djt2.key.getValue() != InputConstants.UNKNOWN.getValue()) {
                djt2.setDown(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), djt2.key.getValue()));
            }
        }
    }
    
    public static void releaseAll() {
        for (final KeyMapping djt2 : KeyMapping.ALL.values()) {
            djt2.release();
        }
    }
    
    public static void resetMapping() {
        KeyMapping.MAP.clear();
        for (final KeyMapping djt2 : KeyMapping.ALL.values()) {
            KeyMapping.MAP.put(djt2.key, djt2);
        }
    }
    
    public KeyMapping(final String string1, final int integer, final String string3) {
        this(string1, InputConstants.Type.KEYSYM, integer, string3);
    }
    
    public KeyMapping(final String string1, final InputConstants.Type b, final int integer, final String string4) {
        this.name = string1;
        this.key = b.getOrCreate(integer);
        this.defaultKey = this.key;
        this.category = string4;
        KeyMapping.ALL.put(string1, this);
        KeyMapping.MAP.put(this.key, this);
        KeyMapping.CATEGORIES.add(string4);
    }
    
    public boolean isDown() {
        return this.isDown;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public boolean consumeClick() {
        if (this.clickCount == 0) {
            return false;
        }
        --this.clickCount;
        return true;
    }
    
    private void release() {
        this.clickCount = 0;
        this.setDown(false);
    }
    
    public String getName() {
        return this.name;
    }
    
    public InputConstants.Key getDefaultKey() {
        return this.defaultKey;
    }
    
    public void setKey(final InputConstants.Key a) {
        this.key = a;
    }
    
    public int compareTo(final KeyMapping djt) {
        if (this.category.equals(djt.category)) {
            return I18n.get(this.name).compareTo(I18n.get(djt.name));
        }
        return ((Integer)KeyMapping.CATEGORY_SORT_ORDER.get(this.category)).compareTo((Integer)KeyMapping.CATEGORY_SORT_ORDER.get(djt.category));
    }
    
    public static Supplier<Component> createNameSupplier(final String string) {
        final KeyMapping djt2 = (KeyMapping)KeyMapping.ALL.get(string);
        if (djt2 == null) {
            return (Supplier<Component>)(() -> new TranslatableComponent(string));
        }
        return (Supplier<Component>)djt2::getTranslatedKeyMessage;
    }
    
    public boolean same(final KeyMapping djt) {
        return this.key.equals(djt.key);
    }
    
    public boolean isUnbound() {
        return this.key.equals(InputConstants.UNKNOWN);
    }
    
    public boolean matches(final int integer1, final int integer2) {
        if (integer1 == InputConstants.UNKNOWN.getValue()) {
            return this.key.getType() == InputConstants.Type.SCANCODE && this.key.getValue() == integer2;
        }
        return this.key.getType() == InputConstants.Type.KEYSYM && this.key.getValue() == integer1;
    }
    
    public boolean matchesMouse(final int integer) {
        return this.key.getType() == InputConstants.Type.MOUSE && this.key.getValue() == integer;
    }
    
    public Component getTranslatedKeyMessage() {
        return this.key.getDisplayName();
    }
    
    public boolean isDefault() {
        return this.key.equals(this.defaultKey);
    }
    
    public String saveString() {
        return this.key.getName();
    }
    
    public void setDown(final boolean boolean1) {
        this.isDown = boolean1;
    }
    
    static {
        ALL = (Map)Maps.newHashMap();
        MAP = (Map)Maps.newHashMap();
        CATEGORIES = (Set)Sets.newHashSet();
        CATEGORY_SORT_ORDER = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("key.categories.movement", 1);
            hashMap.put("key.categories.gameplay", 2);
            hashMap.put("key.categories.inventory", 3);
            hashMap.put("key.categories.creative", 4);
            hashMap.put("key.categories.multiplayer", 5);
            hashMap.put("key.categories.ui", 6);
            hashMap.put("key.categories.misc", 7);
        }));
    }
}
