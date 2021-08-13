package com.mojang.realmsclient;

import java.util.Locale;

public enum Unit {
    B, 
    KB, 
    MB, 
    GB;
    
    public static Unit getLargest(final long long1) {
        if (long1 < 1024L) {
            return Unit.B;
        }
        try {
            final int integer3 = (int)(Math.log((double)long1) / Math.log(1024.0));
            final String string4 = String.valueOf("KMGTPE".charAt(integer3 - 1));
            return valueOf(string4 + "B");
        }
        catch (Exception exception3) {
            return Unit.GB;
        }
    }
    
    public static double convertTo(final long long1, final Unit dfu) {
        if (dfu == Unit.B) {
            return (double)long1;
        }
        return long1 / Math.pow(1024.0, (double)dfu.ordinal());
    }
    
    public static String humanReadable(final long long1) {
        final int integer3 = 1024;
        if (long1 < 1024L) {
            return new StringBuilder().append(long1).append(" B").toString();
        }
        final int integer4 = (int)(Math.log((double)long1) / Math.log(1024.0));
        final String string5 = new StringBuilder().append("KMGTPE".charAt(integer4 - 1)).append("").toString();
        return String.format(Locale.ROOT, "%.1f %sB", new Object[] { long1 / Math.pow(1024.0, (double)integer4), string5 });
    }
    
    public static String humanReadable(final long long1, final Unit dfu) {
        return String.format(new StringBuilder().append("%.").append((dfu == Unit.GB) ? "1" : "0").append("f %s").toString(), new Object[] { convertTo(long1, dfu), dfu.name() });
    }
}
