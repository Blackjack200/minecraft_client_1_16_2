package net.minecraft.advancements;

import java.util.Iterator;
import java.util.Collection;

public interface RequirementsStrategy {
    public static final RequirementsStrategy AND = collection -> {
        arr2 = new String[collection.size()][];
        integer3 = 0;
        collection.iterator();
        while (iterator.hasNext()) {
            string5 = (String)iterator.next();
            integer3++;
            o[n] = new String[] { string5 };
        }
        return arr2;
    };
    public static final RequirementsStrategy OR = collection -> new String[][] { (String[])collection.toArray((Object[])new String[0]) };
    
    String[][] createRequirements(final Collection<String> collection);
    
    default static {
        final String[][] arr2;
        int integer3;
        final Iterator iterator;
        String string5;
        final Object o;
        final int n;
    }
}
