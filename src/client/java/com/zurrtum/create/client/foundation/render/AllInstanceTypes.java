package com.zurrtum.create.client.foundation.render;

import com.zurrtum.create.client.content.fluids.FluidInstance;
import com.zurrtum.create.client.content.kinetics.base.RotatingInstance;
import com.zurrtum.create.client.content.kinetics.base.RotatingPivotInstance;
import com.zurrtum.create.client.content.processing.burner.ScrollInstance;
import com.zurrtum.create.client.content.processing.burner.ScrollStepInstance;
import com.zurrtum.create.client.content.processing.burner.ScrollTransformedInstance;
import com.zurrtum.create.client.flywheel.lib.instance.InstanceTypes;
import com.zurrtum.create.client.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.api.layout.FloatRepr;
import dev.engine_room.flywheel.api.layout.IntegerRepr;
import dev.engine_room.flywheel.api.layout.LayoutBuilder;
import dev.engine_room.flywheel.lib.instance.SimpleInstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.system.MemoryUtil;

import static com.zurrtum.create.client.Create.asResource;

public class AllInstanceTypes {
    private static final Matrix4f IDENTITY_M4 = new Matrix4f();
    private static final Quaternionf IDENTITY_QUAT = new Quaternionf();

    public static final InstanceType<RotatingInstance> ROTATING = SimpleInstanceType.builder(RotatingInstance::new)
        .cullShader(asResource("instance/cull/rotating.glsl")).vertexShader(asResource("instance/rotating.vert"))
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .vector("rotation", FloatRepr.FLOAT, 4).vector("pos", FloatRepr.FLOAT, 3).scalar("speed", FloatRepr.FLOAT)
            .scalar("offset", FloatRepr.FLOAT).vector("axis", FloatRepr.NORMALIZED_BYTE, 3).build())
        .seed(AllInstanceTypes::seedRotating).build();

    public static final InstanceType<RotatingPivotInstance> ROTATING_PIVOT = SimpleInstanceType.builder(
            RotatingPivotInstance::new).cullShader(asResource("instance/cull/rotating.glsl"))
        .vertexShader(asResource("instance/rotating_pivot.vert"))
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .vector("rotation", FloatRepr.FLOAT, 4).vector("pos", FloatRepr.FLOAT, 3).scalar("speed", FloatRepr.FLOAT)
            .scalar("offset", FloatRepr.FLOAT).vector("axis", FloatRepr.NORMALIZED_BYTE, 3)
            .vector("pivot", FloatRepr.FLOAT, 3).build()).seed(ptr -> {
            seedRotating(ptr);
            ExtraMemoryOps.putVector3f(ptr + RotatingPivotInstance.OFF_PIVOT, 0, 0, 0);
        }).build();

    public static final InstanceType<ScrollInstance> SCROLLING = SimpleInstanceType.builder(ScrollInstance::new)
        .cullShader(asResource("instance/cull/scrolling.glsl")).vertexShader(asResource("instance/scrolling.vert"))
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .vector("pos", FloatRepr.FLOAT, 3).vector("rotation", FloatRepr.FLOAT, 4)
            .vector("speed", FloatRepr.FLOAT, 2).vector("diff", FloatRepr.FLOAT, 2).vector("scale", FloatRepr.FLOAT, 2)
            .vector("offset", FloatRepr.FLOAT, 2).build()).seed(AllInstanceTypes::seedScrolling).build();

    public static final InstanceType<ScrollStepInstance> SCROLLING_STEP = SimpleInstanceType.builder(ScrollStepInstance::new)
        .cullShader(asResource("instance/cull/scrolling.glsl")).vertexShader(asResource("instance/scrolling_step.vert"))
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .vector("pos", FloatRepr.FLOAT, 3).vector("rotation", FloatRepr.FLOAT, 4)
            .vector("speed", FloatRepr.FLOAT, 2).vector("diff", FloatRepr.FLOAT, 2).vector("scale", FloatRepr.FLOAT, 2)
            .vector("offset", FloatRepr.FLOAT, 2).vector("step", FloatRepr.FLOAT, 2).build()).seed(ptr -> {
            seedScrolling(ptr);
            MemoryUtil.memPutFloat(ptr + ScrollStepInstance.OFF_STEP, 1);
            MemoryUtil.memPutFloat(ptr + ScrollStepInstance.OFF_STEP + 4, 1);
        }).build();

    // TODO: Switch everything using SCROLLING to this? Right now this is only used for bogey belts.
    //  This takes a decent few more bytes to represent but perhaps it can be packed
    //  down into 96 by sacrificing precision
    public static final InstanceType<ScrollTransformedInstance> SCROLLING_TRANSFORMED = SimpleInstanceType.builder(
            ScrollTransformedInstance::new).cullShader(asResource("instance/cull/scrolling_transformed.glsl"))
        .vertexShader(asResource("instance/scrolling_transformed.vert"))
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .matrix("pose", FloatRepr.FLOAT, 4).vector("speed", FloatRepr.FLOAT, 2)
            .vector("diff", FloatRepr.FLOAT, 2).vector("scale", FloatRepr.FLOAT, 2).vector("offset", FloatRepr.FLOAT, 2)
            .build()).seed(ptr -> {
            seedTransformed(ptr);
            MemoryUtil.memSet(ptr + ScrollTransformedInstance.OFF_SPEED, 0, ScrollTransformedInstance.OFF_OFFSET + 8 - ScrollTransformedInstance.OFF_SPEED);
        }).build();

    public static final InstanceType<FluidInstance> FLUID = SimpleInstanceType.builder(FluidInstance::new)
        .cullShader(asResource("instance/cull/fluid.glsl")).vertexShader(asResource("instance/fluid.vert"))
        .layout(LayoutBuilder.create().vector("color", FloatRepr.NORMALIZED_UNSIGNED_BYTE, 4)
            .vector("overlay", IntegerRepr.SHORT, 2).vector("light", FloatRepr.UNSIGNED_SHORT, 2)
            .matrix("pose", FloatRepr.FLOAT, 4).scalar("progress", FloatRepr.FLOAT)
            .scalar("vScale", FloatRepr.FLOAT).scalar("v0", FloatRepr.FLOAT).build()).seed(ptr -> {
            seedTransformed(ptr);
            MemoryUtil.memSet(ptr + FluidInstance.OFF_PROGRESS, 0, FluidInstance.OFF_V0 + 4 - FluidInstance.OFF_PROGRESS);
        }).build();

    private static void seedRotating(long ptr) {
        InstanceTypes.seedLit(ptr);
        ExtraMemoryOps.putQuaternionf(ptr + RotatingInstance.OFF_ROT, IDENTITY_QUAT);
        MemoryUtil.memSet(ptr + RotatingInstance.OFF_POS, 0, RotatingInstance.OFF_AXIS + 3 - RotatingInstance.OFF_POS);
    }

    private static void seedScrolling(long ptr) {
        InstanceTypes.seedLit(ptr);
        ExtraMemoryOps.putVector3f(ptr + ScrollInstance.OFF_POS, 0, 0, 0);
        ExtraMemoryOps.putQuaternionf(ptr + ScrollInstance.OFF_ROT, IDENTITY_QUAT);
        MemoryUtil.memSet(ptr + ScrollInstance.OFF_SPEED, 0, ScrollInstance.OFF_OFFSET + 8 - ScrollInstance.OFF_SPEED);
    }

    private static void seedTransformed(long ptr) {
        InstanceTypes.seedLit(ptr);
        ExtraMemoryOps.putMatrix4f(ptr + TransformedInstance.OFF_POSE, IDENTITY_M4);
    }

    public static void init() {
        // noop
    }
}
