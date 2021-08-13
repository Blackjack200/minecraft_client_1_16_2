package net.minecraft.world.level.pathfinder;

public class BinaryHeap {
    private Node[] heap;
    private int size;
    
    public BinaryHeap() {
        this.heap = new Node[128];
    }
    
    public Node insert(final Node cwy) {
        if (cwy.heapIdx >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.size == this.heap.length) {
            final Node[] arr3 = new Node[this.size << 1];
            System.arraycopy(this.heap, 0, arr3, 0, this.size);
            this.heap = arr3;
        }
        this.heap[this.size] = cwy;
        cwy.heapIdx = this.size;
        this.upHeap(this.size++);
        return cwy;
    }
    
    public void clear() {
        this.size = 0;
    }
    
    public Node pop() {
        final Node cwy2 = this.heap[0];
        final Node[] heap = this.heap;
        final int n = 0;
        final Node[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        this.heap[this.size] = null;
        if (this.size > 0) {
            this.downHeap(0);
        }
        cwy2.heapIdx = -1;
        return cwy2;
    }
    
    public void changeCost(final Node cwy, final float float2) {
        final float float3 = cwy.f;
        cwy.f = float2;
        if (float2 < float3) {
            this.upHeap(cwy.heapIdx);
        }
        else {
            this.downHeap(cwy.heapIdx);
        }
    }
    
    private void upHeap(int integer) {
        final Node cwy3 = this.heap[integer];
        final float float4 = cwy3.f;
        while (integer > 0) {
            final int integer2 = integer - 1 >> 1;
            final Node cwy4 = this.heap[integer2];
            if (float4 >= cwy4.f) {
                break;
            }
            this.heap[integer] = cwy4;
            cwy4.heapIdx = integer;
            integer = integer2;
        }
        this.heap[integer] = cwy3;
        cwy3.heapIdx = integer;
    }
    
    private void downHeap(int integer) {
        final Node cwy3 = this.heap[integer];
        final float float4 = cwy3.f;
        while (true) {
            final int integer2 = 1 + (integer << 1);
            final int integer3 = integer2 + 1;
            if (integer2 >= this.size) {
                break;
            }
            final Node cwy4 = this.heap[integer2];
            final float float5 = cwy4.f;
            Node cwy5;
            float float6;
            if (integer3 >= this.size) {
                cwy5 = null;
                float6 = Float.POSITIVE_INFINITY;
            }
            else {
                cwy5 = this.heap[integer3];
                float6 = cwy5.f;
            }
            if (float5 < float6) {
                if (float5 >= float4) {
                    break;
                }
                this.heap[integer] = cwy4;
                cwy4.heapIdx = integer;
                integer = integer2;
            }
            else {
                if (float6 >= float4) {
                    break;
                }
                this.heap[integer] = cwy5;
                cwy5.heapIdx = integer;
                integer = integer3;
            }
        }
        this.heap[integer] = cwy3;
        cwy3.heapIdx = integer;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
}
