package com.mojang.realmsclient.dto;

import java.lang.reflect.Modifier;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;

public abstract class ValueObject {
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder("{");
        for (final Field field6 : this.getClass().getFields()) {
            if (!isStatic(field6)) {
                try {
                    stringBuilder2.append(getName(field6)).append("=").append(field6.get(this)).append(" ");
                }
                catch (IllegalAccessException ex) {}
            }
        }
        stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
        stringBuilder2.append('}');
        return stringBuilder2.toString();
    }
    
    private static String getName(final Field field) {
        final SerializedName serializedName2 = (SerializedName)field.getAnnotation((Class)SerializedName.class);
        return (serializedName2 != null) ? serializedName2.value() : field.getName();
    }
    
    private static boolean isStatic(final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
}
