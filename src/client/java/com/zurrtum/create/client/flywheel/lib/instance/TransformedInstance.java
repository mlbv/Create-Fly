package com.zurrtum.create.client.flywheel.lib.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.transform.Affine;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

public class TransformedInstance extends ColoredLitOverlayInstance implements Affine<TransformedInstance> {
    public static final int OFF_POSE = 12;

    private final Matrix4f pose = new Matrix4f();

    public TransformedInstance(InstanceType<? extends TransformedInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public Matrix4fc pose() {
        return pose;
    }

    private TransformedInstance writePose() {
        ExtraMemoryOps.putMatrix4f(slabPtr() + OFF_POSE, pose);
        return this;
    }

    @Override
    public TransformedInstance translate(float x, float y, float z) {
        pose.translate(x, y, z);
        return writePose();
    }

    @Override
    public TransformedInstance rotate(Quaternionfc quaternion) {
        pose.rotate(quaternion);
        return writePose();
    }

    @Override
    public TransformedInstance scale(float x, float y, float z) {
        pose.scale(x, y, z);
        return writePose();
    }

    public TransformedInstance mul(Matrix4fc other) {
        pose.mul(other);
        return writePose();
    }

    public TransformedInstance mul(PoseStack.Pose other) {
        return mul(other.pose());
    }

    public TransformedInstance mul(PoseStack stack) {
        return mul(stack.last());
    }

    public TransformedInstance setTransform(Matrix4fc pose) {
        this.pose.set(pose);
        return writePose();
    }

    public TransformedInstance setTransform(PoseStack.Pose pose) {
        return setTransform(pose.pose());
    }

    public TransformedInstance setTransform(PoseStack stack) {
        return setTransform(stack.last());
    }

    public TransformedInstance setIdentityTransform() {
        pose.identity();
        return writePose();
    }

    public TransformedInstance setZeroTransform() {
        pose.zero();
        return writePose();
    }

    @Override
    public TransformedInstance rotateAround(Quaternionfc quaternion, float x, float y, float z) {
        pose.rotateAround(quaternion, x, y, z);
        return writePose();
    }

    @Override
    public TransformedInstance rotateCentered(float radians, float axisX, float axisY, float axisZ) {
        pose.translate(CENTER, CENTER, CENTER).rotate(radians, axisX, axisY, axisZ)
            .translate(-CENTER, -CENTER, -CENTER);
        return writePose();
    }

    @Override
    public TransformedInstance rotateXCentered(float radians) {
        pose.translate(CENTER, CENTER, CENTER).rotateX(radians).translate(-CENTER, -CENTER, -CENTER);
        return writePose();
    }

    @Override
    public TransformedInstance rotateYCentered(float radians) {
        pose.translate(CENTER, CENTER, CENTER).rotateY(radians).translate(-CENTER, -CENTER, -CENTER);
        return writePose();
    }

    @Override
    public TransformedInstance rotateZCentered(float radians) {
        pose.translate(CENTER, CENTER, CENTER).rotateZ(radians).translate(-CENTER, -CENTER, -CENTER);
        return writePose();
    }

    @Override
    public TransformedInstance rotate(float radians, float axisX, float axisY, float axisZ) {
        pose.rotate(radians, axisX, axisY, axisZ);
        return writePose();
    }

    @Override
    public TransformedInstance rotate(AxisAngle4f axisAngle) {
        pose.rotate(axisAngle);
        return writePose();
    }

    @Override
    public TransformedInstance rotateX(float radians) {
        pose.rotateX(radians);
        return writePose();
    }

    @Override
    public TransformedInstance rotateY(float radians) {
        pose.rotateY(radians);
        return writePose();
    }

    @Override
    public TransformedInstance rotateZ(float radians) {
        pose.rotateZ(radians);
        return writePose();
    }

    @Override
    public TransformedInstance flush() {
        super.flush();
        return writePose();
    }
}
