package net.minecraft.client.renderer.texture;

import net.minecraft.resources.ResourceLocation;
import java.util.function.Consumer;
import java.util.Iterator;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

public class Stitcher {
    private static final Comparator<Holder> HOLDER_COMPARATOR;
    private final int mipLevel;
    private final Set<Holder> texturesToBeStitched;
    private final List<Region> storage;
    private int storageX;
    private int storageY;
    private final int maxWidth;
    private final int maxHeight;
    
    public Stitcher(final int integer1, final int integer2, final int integer3) {
        this.texturesToBeStitched = (Set<Holder>)Sets.newHashSetWithExpectedSize(256);
        this.storage = (List<Region>)Lists.newArrayListWithCapacity(256);
        this.mipLevel = integer3;
        this.maxWidth = integer1;
        this.maxHeight = integer2;
    }
    
    public int getWidth() {
        return this.storageX;
    }
    
    public int getHeight() {
        return this.storageY;
    }
    
    public void registerSprite(final TextureAtlasSprite.Info a) {
        final Holder a2 = new Holder(a, this.mipLevel);
        this.texturesToBeStitched.add(a2);
    }
    
    public void stitch() {
        final List<Holder> list2 = (List<Holder>)Lists.newArrayList((Iterable)this.texturesToBeStitched);
        list2.sort((Comparator)Stitcher.HOLDER_COMPARATOR);
        for (final Holder a4 : list2) {
            if (!this.addToStorage(a4)) {
                throw new StitcherException(a4.spriteInfo, (Collection<TextureAtlasSprite.Info>)list2.stream().map(a -> a.spriteInfo).collect(ImmutableList.toImmutableList()));
            }
        }
        this.storageX = Mth.smallestEncompassingPowerOfTwo(this.storageX);
        this.storageY = Mth.smallestEncompassingPowerOfTwo(this.storageY);
    }
    
    public void gatherSprites(final SpriteLoader c) {
        for (final Region b4 : this.storage) {
            b4.walk((Consumer<Region>)(b -> {
                final Holder a4 = b.getHolder();
                final TextureAtlasSprite.Info a5 = a4.spriteInfo;
                c.load(a5, this.storageX, this.storageY, b.getX(), b.getY());
            }));
        }
    }
    
    private static int smallestFittingMinTexel(final int integer1, final int integer2) {
        return (integer1 >> integer2) + (((integer1 & (1 << integer2) - 1) != 0x0) ? 1 : 0) << integer2;
    }
    
    private boolean addToStorage(final Holder a) {
        for (final Region b4 : this.storage) {
            if (b4.add(a)) {
                return true;
            }
        }
        return this.expand(a);
    }
    
    private boolean expand(final Holder a) {
        final int integer4 = Mth.smallestEncompassingPowerOfTwo(this.storageX);
        final int integer5 = Mth.smallestEncompassingPowerOfTwo(this.storageY);
        final int integer6 = Mth.smallestEncompassingPowerOfTwo(this.storageX + a.width);
        final int integer7 = Mth.smallestEncompassingPowerOfTwo(this.storageY + a.height);
        final boolean boolean8 = integer6 <= this.maxWidth;
        final boolean boolean9 = integer7 <= this.maxHeight;
        if (!boolean8 && !boolean9) {
            return false;
        }
        final boolean boolean10 = boolean8 && integer4 != integer6;
        final boolean boolean11 = boolean9 && integer5 != integer7;
        boolean boolean12;
        if (boolean10 ^ boolean11) {
            boolean12 = boolean10;
        }
        else {
            boolean12 = (boolean8 && integer4 <= integer5);
        }
        Region b12;
        if (boolean12) {
            if (this.storageY == 0) {
                this.storageY = a.height;
            }
            b12 = new Region(this.storageX, 0, a.width, this.storageY);
            this.storageX += a.width;
        }
        else {
            b12 = new Region(0, this.storageY, this.storageX, a.height);
            this.storageY += a.height;
        }
        b12.add(a);
        this.storage.add(b12);
        return true;
    }
    
    static {
        HOLDER_COMPARATOR = Comparator.comparing(a -> -a.height).thenComparing(a -> -a.width).thenComparing(a -> a.spriteInfo.name());
    }
    
    static class Holder {
        public final TextureAtlasSprite.Info spriteInfo;
        public final int width;
        public final int height;
        
        public Holder(final TextureAtlasSprite.Info a, final int integer) {
            this.spriteInfo = a;
            this.width = smallestFittingMinTexel(a.width(), integer);
            this.height = smallestFittingMinTexel(a.height(), integer);
        }
        
        public String toString() {
            return new StringBuilder().append("Holder{width=").append(this.width).append(", height=").append(this.height).append('}').toString();
        }
    }
    
    public static class Region {
        private final int originX;
        private final int originY;
        private final int width;
        private final int height;
        private List<Region> subSlots;
        private Holder holder;
        
        public Region(final int integer1, final int integer2, final int integer3, final int integer4) {
            this.originX = integer1;
            this.originY = integer2;
            this.width = integer3;
            this.height = integer4;
        }
        
        public Holder getHolder() {
            return this.holder;
        }
        
        public int getX() {
            return this.originX;
        }
        
        public int getY() {
            return this.originY;
        }
        
        public boolean add(final Holder a) {
            if (this.holder != null) {
                return false;
            }
            final int integer3 = a.width;
            final int integer4 = a.height;
            if (integer3 > this.width || integer4 > this.height) {
                return false;
            }
            if (integer3 == this.width && integer4 == this.height) {
                this.holder = a;
                return true;
            }
            if (this.subSlots == null) {
                (this.subSlots = (List<Region>)Lists.newArrayListWithCapacity(1)).add(new Region(this.originX, this.originY, integer3, integer4));
                final int integer5 = this.width - integer3;
                final int integer6 = this.height - integer4;
                if (integer6 > 0 && integer5 > 0) {
                    final int integer7 = Math.max(this.height, integer5);
                    final int integer8 = Math.max(this.width, integer6);
                    if (integer7 >= integer8) {
                        this.subSlots.add(new Region(this.originX, this.originY + integer4, integer3, integer6));
                        this.subSlots.add(new Region(this.originX + integer3, this.originY, integer5, this.height));
                    }
                    else {
                        this.subSlots.add(new Region(this.originX + integer3, this.originY, integer5, integer4));
                        this.subSlots.add(new Region(this.originX, this.originY + integer4, this.width, integer6));
                    }
                }
                else if (integer5 == 0) {
                    this.subSlots.add(new Region(this.originX, this.originY + integer4, integer3, integer6));
                }
                else if (integer6 == 0) {
                    this.subSlots.add(new Region(this.originX + integer3, this.originY, integer5, integer4));
                }
            }
            for (final Region b6 : this.subSlots) {
                if (b6.add(a)) {
                    return true;
                }
            }
            return false;
        }
        
        public void walk(final Consumer<Region> consumer) {
            if (this.holder != null) {
                consumer.accept(this);
            }
            else if (this.subSlots != null) {
                for (final Region b4 : this.subSlots) {
                    b4.walk(consumer);
                }
            }
        }
        
        public String toString() {
            return new StringBuilder().append("Slot{originX=").append(this.originX).append(", originY=").append(this.originY).append(", width=").append(this.width).append(", height=").append(this.height).append(", texture=").append(this.holder).append(", subSlots=").append(this.subSlots).append('}').toString();
        }
    }
    
    public interface SpriteLoader {
        void load(final TextureAtlasSprite.Info a, final int integer2, final int integer3, final int integer4, final int integer5);
    }
}
