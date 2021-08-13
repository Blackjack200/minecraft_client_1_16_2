package net.minecraft.client.gui.components;

import net.minecraft.util.FormattedCharSequence;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import net.minecraft.network.chat.Component;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.Font;

public interface MultiLineLabel {
    public static final MultiLineLabel EMPTY = new MultiLineLabel() {
        public int renderCentered(final PoseStack dfj, final int integer2, final int integer3) {
            return integer3;
        }
        
        public int renderCentered(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
            return integer3;
        }
        
        public int renderLeftAligned(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
            return integer3;
        }
        
        public int renderLeftAlignedNoShadow(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
            return integer3;
        }
        
        public int getLineCount() {
            return 0;
        }
    };
    
    default MultiLineLabel create(final Font dkr, final FormattedText nu, final int integer) {
        return createFixed(dkr, (List<TextWithWidth>)dkr.split(nu, integer).stream().map(aex -> new TextWithWidth(aex, dkr.width(aex))).collect(ImmutableList.toImmutableList()));
    }
    
    default MultiLineLabel create(final Font dkr, final FormattedText nu, final int integer3, final int integer4) {
        return createFixed(dkr, (List<TextWithWidth>)dkr.split(nu, integer3).stream().limit((long)integer4).map(aex -> new TextWithWidth(aex, dkr.width(aex))).collect(ImmutableList.toImmutableList()));
    }
    
    default MultiLineLabel create(final Font dkr, final Component... arr) {
        return createFixed(dkr, (List<TextWithWidth>)Arrays.stream((Object[])arr).map(Component::getVisualOrderText).map(aex -> new TextWithWidth(aex, dkr.width(aex))).collect(ImmutableList.toImmutableList()));
    }
    
    default MultiLineLabel createFixed(final Font dkr, final List<TextWithWidth> list) {
        if (list.isEmpty()) {
            return MultiLineLabel.EMPTY;
        }
        return new MultiLineLabel() {
            public int renderCentered(final PoseStack dfj, final int integer2, final int integer3) {
                dkr.getClass();
                return this.renderCentered(dfj, integer2, integer3, 9, 16777215);
            }
            
            public int renderCentered(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
                int integer6 = integer3;
                for (final TextWithWidth a9 : list) {
                    dkr.drawShadow(dfj, a9.text, (float)(integer2 - a9.width / 2), (float)integer6, integer5);
                    integer6 += integer4;
                }
                return integer6;
            }
            
            public int renderLeftAligned(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
                int integer6 = integer3;
                for (final TextWithWidth a9 : list) {
                    dkr.drawShadow(dfj, a9.text, (float)integer2, (float)integer6, integer5);
                    integer6 += integer4;
                }
                return integer6;
            }
            
            public int renderLeftAlignedNoShadow(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
                int integer6 = integer3;
                for (final TextWithWidth a9 : list) {
                    dkr.draw(dfj, a9.text, (float)integer2, (float)integer6, integer5);
                    integer6 += integer4;
                }
                return integer6;
            }
            
            public int getLineCount() {
                return list.size();
            }
        };
    }
    
    int renderCentered(final PoseStack dfj, final int integer2, final int integer3);
    
    int renderCentered(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5);
    
    int renderLeftAligned(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5);
    
    int renderLeftAlignedNoShadow(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5);
    
    int getLineCount();
    
    public static class TextWithWidth {
        private final FormattedCharSequence text;
        private final int width;
        
        private TextWithWidth(final FormattedCharSequence aex, final int integer) {
            this.text = aex;
            this.width = integer;
        }
    }
}
