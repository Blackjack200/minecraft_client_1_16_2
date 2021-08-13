package net.minecraft.client.searchtree;

import org.apache.logging.log4j.LogManager;
import java.util.Set;
import it.unimi.dsi.fastutil.ints.IntSet;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Collections;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class SuffixArray<T> {
    private static final boolean DEBUG_COMPARISONS;
    private static final boolean DEBUG_ARRAY;
    private static final Logger LOGGER;
    protected final List<T> list;
    private final IntList chars;
    private final IntList wordStarts;
    private IntList suffixToT;
    private IntList offsets;
    private int maxStringLength;
    
    public SuffixArray() {
        this.list = (List<T>)Lists.newArrayList();
        this.chars = (IntList)new IntArrayList();
        this.wordStarts = (IntList)new IntArrayList();
        this.suffixToT = (IntList)new IntArrayList();
        this.offsets = (IntList)new IntArrayList();
    }
    
    public void add(final T object, final String string) {
        this.maxStringLength = Math.max(this.maxStringLength, string.length());
        final int integer4 = this.list.size();
        this.list.add(object);
        this.wordStarts.add(this.chars.size());
        for (int integer5 = 0; integer5 < string.length(); ++integer5) {
            this.suffixToT.add(integer4);
            this.offsets.add(integer5);
            this.chars.add((int)string.charAt(integer5));
        }
        this.suffixToT.add(integer4);
        this.offsets.add(string.length());
        this.chars.add(-1);
    }
    
    public void generate() {
        final int integer2 = this.chars.size();
        final int[] arr3 = new int[integer2];
        final int[] arr4 = new int[integer2];
        final int[] arr5 = new int[integer2];
        final int[] arr6 = new int[integer2];
        final IntComparator intComparator7 = (IntComparator)new IntComparator() {
            public int compare(final int integer1, final int integer2) {
                if (arr4[integer1] == arr4[integer2]) {
                    return Integer.compare(arr5[integer1], arr5[integer2]);
                }
                return Integer.compare(arr4[integer1], arr4[integer2]);
            }
            
            public int compare(final Integer integer1, final Integer integer2) {
                return this.compare((int)integer1, (int)integer2);
            }
        };
        final Swapper swapper8 = (integer4, integer5) -> {
            if (integer4 != integer5) {
                int integer6 = arr4[integer4];
                arr4[integer4] = arr4[integer5];
                arr4[integer5] = integer6;
                integer6 = arr5[integer4];
                arr5[integer4] = arr5[integer5];
                arr5[integer5] = integer6;
                integer6 = arr6[integer4];
                arr6[integer4] = arr6[integer5];
                arr6[integer5] = integer6;
            }
        };
        for (int integer3 = 0; integer3 < integer2; ++integer3) {
            arr3[integer3] = this.chars.getInt(integer3);
        }
        for (int integer3 = 1, integer4 = Math.min(integer2, this.maxStringLength); integer3 * 2 < integer4; integer3 *= 2) {
            for (int integer5 = 0; integer5 < integer2; ++integer5) {
                arr4[integer5] = arr3[integer5];
                arr5[integer5] = ((integer5 + integer3 < integer2) ? arr3[integer5 + integer3] : -2);
                arr6[integer5] = integer5;
            }
            Arrays.quickSort(0, integer2, intComparator7, swapper8);
            for (int integer5 = 0; integer5 < integer2; ++integer5) {
                if (integer5 > 0 && arr4[integer5] == arr4[integer5 - 1] && arr5[integer5] == arr5[integer5 - 1]) {
                    arr3[arr6[integer5]] = arr3[arr6[integer5 - 1]];
                }
                else {
                    arr3[arr6[integer5]] = integer5;
                }
            }
        }
        final IntList intList11 = this.suffixToT;
        final IntList intList12 = this.offsets;
        this.suffixToT = (IntList)new IntArrayList(intList11.size());
        this.offsets = (IntList)new IntArrayList(intList12.size());
        for (final int integer7 : arr6) {
            this.suffixToT.add(intList11.getInt(integer7));
            this.offsets.add(intList12.getInt(integer7));
        }
        if (SuffixArray.DEBUG_ARRAY) {
            this.print();
        }
    }
    
    private void print() {
        for (int integer2 = 0; integer2 < this.suffixToT.size(); ++integer2) {
            SuffixArray.LOGGER.debug("{} {}", integer2, this.getString(integer2));
        }
        SuffixArray.LOGGER.debug("");
    }
    
    private String getString(final int integer) {
        final int integer2 = this.offsets.getInt(integer);
        final int integer3 = this.wordStarts.getInt(this.suffixToT.getInt(integer));
        final StringBuilder stringBuilder5 = new StringBuilder();
        for (int integer4 = 0; integer3 + integer4 < this.chars.size(); ++integer4) {
            if (integer4 == integer2) {
                stringBuilder5.append('^');
            }
            final int integer5 = this.chars.get(integer3 + integer4);
            if (integer5 == -1) {
                break;
            }
            stringBuilder5.append((char)integer5);
        }
        return stringBuilder5.toString();
    }
    
    private int compare(final String string, final int integer) {
        final int integer2 = this.wordStarts.getInt(this.suffixToT.getInt(integer));
        final int integer3 = this.offsets.getInt(integer);
        for (int integer4 = 0; integer4 < string.length(); ++integer4) {
            final int integer5 = this.chars.getInt(integer2 + integer3 + integer4);
            if (integer5 == -1) {
                return 1;
            }
            final char character8 = string.charAt(integer4);
            final char character9 = (char)integer5;
            if (character8 < character9) {
                return -1;
            }
            if (character8 > character9) {
                return 1;
            }
        }
        return 0;
    }
    
    public List<T> search(final String string) {
        final int integer3 = this.suffixToT.size();
        int integer4 = 0;
        int integer5 = integer3;
        while (integer4 < integer5) {
            final int integer6 = integer4 + (integer5 - integer4) / 2;
            final int integer7 = this.compare(string, integer6);
            if (SuffixArray.DEBUG_COMPARISONS) {
                SuffixArray.LOGGER.debug("comparing lower \"{}\" with {} \"{}\": {}", string, integer6, this.getString(integer6), integer7);
            }
            if (integer7 > 0) {
                integer4 = integer6 + 1;
            }
            else {
                integer5 = integer6;
            }
        }
        if (integer4 < 0 || integer4 >= integer3) {
            return (List<T>)Collections.emptyList();
        }
        final int integer6 = integer4;
        integer5 = integer3;
        while (integer4 < integer5) {
            final int integer7 = integer4 + (integer5 - integer4) / 2;
            final int integer8 = this.compare(string, integer7);
            if (SuffixArray.DEBUG_COMPARISONS) {
                SuffixArray.LOGGER.debug("comparing upper \"{}\" with {} \"{}\": {}", string, integer7, this.getString(integer7), integer8);
            }
            if (integer8 >= 0) {
                integer4 = integer7 + 1;
            }
            else {
                integer5 = integer7;
            }
        }
        final int integer7 = integer4;
        final IntSet intSet8 = (IntSet)new IntOpenHashSet();
        for (int integer9 = integer6; integer9 < integer7; ++integer9) {
            intSet8.add(this.suffixToT.getInt(integer9));
        }
        final int[] arr9 = intSet8.toIntArray();
        java.util.Arrays.sort(arr9);
        final Set<T> set10 = (Set<T>)Sets.newLinkedHashSet();
        for (final int integer10 : arr9) {
            set10.add(this.list.get(integer10));
        }
        return (List<T>)Lists.newArrayList((Iterable)set10);
    }
    
    static {
        DEBUG_COMPARISONS = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));
        DEBUG_ARRAY = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));
        LOGGER = LogManager.getLogger();
    }
}
