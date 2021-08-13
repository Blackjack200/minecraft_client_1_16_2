package com.mojang.blaze3d.vertex;

import com.google.common.collect.ImmutableList;

public class DefaultVertexFormat {
    public static final VertexFormatElement ELEMENT_POSITION;
    public static final VertexFormatElement ELEMENT_COLOR;
    public static final VertexFormatElement ELEMENT_UV0;
    public static final VertexFormatElement ELEMENT_UV1;
    public static final VertexFormatElement ELEMENT_UV2;
    public static final VertexFormatElement ELEMENT_NORMAL;
    public static final VertexFormatElement ELEMENT_PADDING;
    public static final VertexFormat BLOCK;
    public static final VertexFormat NEW_ENTITY;
    @Deprecated
    public static final VertexFormat PARTICLE;
    public static final VertexFormat POSITION;
    public static final VertexFormat POSITION_COLOR;
    public static final VertexFormat POSITION_COLOR_LIGHTMAP;
    public static final VertexFormat POSITION_TEX;
    public static final VertexFormat POSITION_COLOR_TEX;
    @Deprecated
    public static final VertexFormat POSITION_TEX_COLOR;
    public static final VertexFormat POSITION_COLOR_TEX_LIGHTMAP;
    @Deprecated
    public static final VertexFormat POSITION_TEX_LIGHTMAP_COLOR;
    @Deprecated
    public static final VertexFormat POSITION_TEX_COLOR_NORMAL;
    
    static {
        ELEMENT_POSITION = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 3);
        ELEMENT_COLOR = new VertexFormatElement(0, VertexFormatElement.Type.UBYTE, VertexFormatElement.Usage.COLOR, 4);
        ELEMENT_UV0 = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);
        ELEMENT_UV1 = new VertexFormatElement(1, VertexFormatElement.Type.SHORT, VertexFormatElement.Usage.UV, 2);
        ELEMENT_UV2 = new VertexFormatElement(2, VertexFormatElement.Type.SHORT, VertexFormatElement.Usage.UV, 2);
        ELEMENT_NORMAL = new VertexFormatElement(0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.NORMAL, 3);
        ELEMENT_PADDING = new VertexFormatElement(0, VertexFormatElement.Type.BYTE, VertexFormatElement.Usage.PADDING, 1);
        BLOCK = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_UV2).add(DefaultVertexFormat.ELEMENT_NORMAL).add(DefaultVertexFormat.ELEMENT_PADDING).build());
        NEW_ENTITY = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_UV1).add(DefaultVertexFormat.ELEMENT_UV2).add(DefaultVertexFormat.ELEMENT_NORMAL).add(DefaultVertexFormat.ELEMENT_PADDING).build());
        PARTICLE = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_UV2).build());
        POSITION = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).build());
        POSITION_COLOR = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_COLOR).build());
        POSITION_COLOR_LIGHTMAP = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_UV2).build());
        POSITION_TEX = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_UV0).build());
        POSITION_COLOR_TEX = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_UV0).build());
        POSITION_TEX_COLOR = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_COLOR).build());
        POSITION_COLOR_TEX_LIGHTMAP = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_UV2).build());
        POSITION_TEX_LIGHTMAP_COLOR = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_UV2).add(DefaultVertexFormat.ELEMENT_COLOR).build());
        POSITION_TEX_COLOR_NORMAL = new VertexFormat((ImmutableList<VertexFormatElement>)ImmutableList.builder().add(DefaultVertexFormat.ELEMENT_POSITION).add(DefaultVertexFormat.ELEMENT_UV0).add(DefaultVertexFormat.ELEMENT_COLOR).add(DefaultVertexFormat.ELEMENT_NORMAL).add(DefaultVertexFormat.ELEMENT_PADDING).build());
    }
}
