package net.minecraft.util.profiling;

public final class ResultField implements Comparable<ResultField> {
    public final double percentage;
    public final double globalPercentage;
    public final long count;
    public final String name;
    
    public ResultField(final String string, final double double2, final double double3, final long long4) {
        this.name = string;
        this.percentage = double2;
        this.globalPercentage = double3;
        this.count = long4;
    }
    
    public int compareTo(final ResultField anv) {
        if (anv.percentage < this.percentage) {
            return -1;
        }
        if (anv.percentage > this.percentage) {
            return 1;
        }
        return anv.name.compareTo(this.name);
    }
    
    public int getColor() {
        return (this.name.hashCode() & 0xAAAAAA) + 4473924;
    }
}
