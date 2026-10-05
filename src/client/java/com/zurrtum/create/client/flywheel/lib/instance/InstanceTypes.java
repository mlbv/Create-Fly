package com.zurrtum.create.client.flywheel.lib.instance;

import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.api.layout.FloatRepr;
import dev.engine_room.flywheel.api.layout.IntegerRepr;
import dev.engine_room.flywheel.api.layout.LayoutBuilder;
import dev.engine_room.flywheel.lib.instance.SimpleInstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import dev.engine_room.flywheel.lib.util.ResourceUtil;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.system.MemoryUtil;

public final class InstanceTypes {
    private static final Matrix4f IDENTITY_M4 = new Matrix4f();
    private static final Quaternionf IDENTITY_QUAT = new Quaternionf();

    public static final InstanceType<TransformedInstance> TRANSFORMED = SimpleInstanceType.builder(TransformedInstance::new)
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .matrix("pose", FloatRepr.FLOAT, 4).build()).seed(ptr -> {
            seedLit(ptr);
            ExtraMemoryOps.putMatrix4f(ptr + TransformedInstance.OFF_POSE, IDENTITY_M4);
        }).vertexShader(ResourceUtil.rl("instance/transformed.vert"))
        .cullShader(ResourceUtil.rl("instance/cull/transformed.glsl")).build();

    public static final InstanceType<OrientedInstance> ORIENTED = SimpleInstanceType.builder(OrientedInstance::new)
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .vector("position", FloatRepr.FLOAT, 3).vector("pivot", FloatRepr.FLOAT, 3)
            .vector("rotation", FloatRepr.FLOAT, 4).build()).seed(ptr -> {
            seedLit(ptr);
            ExtraMemoryOps.putVector3f(ptr + OrientedInstance.OFF_POS, 0, 0, 0);
            ExtraMemoryOps.putVector3f(ptr + OrientedInstance.OFF_PIVOT, 0.5f, 0.5f, 0.5f);
            ExtraMemoryOps.putQuaternionf(ptr + OrientedInstance.OFF_ROT, IDENTITY_QUAT);
        }).vertexShader(ResourceUtil.rl("instance/oriented.vert"))
        .cullShader(ResourceUtil.rl("instance/cull/oriented.glsl")).build();

    public static void seedLit(long ptr) {
        MemoryUtil.memPutInt(ptr, 0xFFFFFFFF);
        ExtraMemoryOps.put2x16(ptr + 4, OverlayTexture.NO_OVERLAY);
        ExtraMemoryOps.put2x16(ptr + 8, 0);
    }

    private InstanceTypes() {
    }
}
