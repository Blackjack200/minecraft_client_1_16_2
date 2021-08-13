package net.minecraft.world.level.lighting;

import net.minecraft.world.level.chunk.DataLayer;

public class FlatDataLayer extends DataLayer {
    public FlatDataLayer() {
        super(128);
    }
    
    public FlatDataLayer(final DataLayer cfy, final int integer) {
        super(128);
        System.arraycopy(cfy.getData(), integer * 128, this.data, 0, 128);
    }
    
    @Override
    protected int getIndex(final int integer1, final int integer2, final int integer3) {
        return integer3 << 4 | integer1;
    }
    
    @Override
    public byte[] getData() {
        final byte[] arr2 = new byte[2048];
        for (int integer3 = 0; integer3 < 16; ++integer3) {
            System.arraycopy(this.data, 0, arr2, integer3 * 128, 128);
        }
        return arr2;
    }
}
