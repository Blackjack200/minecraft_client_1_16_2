package net.minecraft.client.resources.model;

import java.util.Locale;
import net.minecraft.resources.ResourceLocation;

public class ModelResourceLocation extends ResourceLocation {
    private final String variant;
    
    protected ModelResourceLocation(final String[] arr) {
        super(arr);
        this.variant = arr[2].toLowerCase(Locale.ROOT);
    }
    
    public ModelResourceLocation(final String string) {
        this(decompose(string));
    }
    
    public ModelResourceLocation(final ResourceLocation vk, final String string) {
        this(vk.toString(), string);
    }
    
    public ModelResourceLocation(final String string1, final String string2) {
        this(decompose(string1 + '#' + string2));
    }
    
    protected static String[] decompose(final String string) {
        final String[] arr2 = { null, string, "" };
        final int integer3 = string.indexOf(35);
        String string2 = string;
        if (integer3 >= 0) {
            arr2[2] = string.substring(integer3 + 1, string.length());
            if (integer3 > 1) {
                string2 = string.substring(0, integer3);
            }
        }
        System.arraycopy(ResourceLocation.decompose(string2, ':'), 0, arr2, 0, 2);
        return arr2;
    }
    
    public String getVariant() {
        return this.variant;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ModelResourceLocation && super.equals(object)) {
            final ModelResourceLocation elm3 = (ModelResourceLocation)object;
            return this.variant.equals(elm3.variant);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + this.variant.hashCode();
    }
    
    @Override
    public String toString() {
        return super.toString() + '#' + this.variant;
    }
}
