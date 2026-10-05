package com.zurrtum.create.client.flywheel.lib.instance;

import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.transform.Rotate;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public class OrientedInstance extends ColoredLitOverlayInstance implements Rotate<OrientedInstance> {
    public static final int OFF_POS = 12;
    public static final int OFF_PIVOT = 24;
    public static final int OFF_ROT = 36;

    private float posX;
    private float posY;
    private float posZ;
    private float pivotX = 0.5f;
    private float pivotY = 0.5f;
    private float pivotZ = 0.5f;
    private final Quaternionf rotation = new Quaternionf();

    public OrientedInstance(InstanceType<? extends OrientedInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public Quaternionfc rotation() {
        return rotation;
    }

    private OrientedInstance writePosition() {
        ExtraMemoryOps.putVector3f(slabPtr() + OFF_POS, posX, posY, posZ);
        return this;
    }

    private OrientedInstance writePivot() {
        ExtraMemoryOps.putVector3f(slabPtr() + OFF_PIVOT, pivotX, pivotY, pivotZ);
        return this;
    }

    private OrientedInstance writeRotation() {
        ExtraMemoryOps.putQuaternionf(slabPtr() + OFF_ROT, rotation);
        return this;
    }

    public OrientedInstance position(float x, float y, float z) {
        posX = x;
        posY = y;
        posZ = z;
        return writePosition();
    }

    public OrientedInstance position(Vector3fc pos) {
        return position(pos.x(), pos.y(), pos.z());
    }

    public OrientedInstance position(Vec3i pos) {
        return position(pos.getX(), pos.getY(), pos.getZ());
    }

    public OrientedInstance position(Vec3 pos) {
        return position((float) pos.x(), (float) pos.y(), (float) pos.z());
    }

    public OrientedInstance zeroPosition() {
        return position(0, 0, 0);
    }

    public OrientedInstance translatePosition(float x, float y, float z) {
        return position(posX + x, posY + y, posZ + z);
    }

    public OrientedInstance pivot(float x, float y, float z) {
        pivotX = x;
        pivotY = y;
        pivotZ = z;
        return writePivot();
    }

    public OrientedInstance pivot(Vector3fc pos) {
        return pivot(pos.x(), pos.y(), pos.z());
    }

    public OrientedInstance pivot(Vec3i pos) {
        return pivot(pos.getX(), pos.getY(), pos.getZ());
    }

    public OrientedInstance pivot(Vec3 pos) {
        return pivot((float) pos.x(), (float) pos.y(), (float) pos.z());
    }

    public OrientedInstance centerPivot() {
        return pivot(0.5f, 0.5f, 0.5f);
    }

    public OrientedInstance translatePivot(float x, float y, float z) {
        return pivot(pivotX + x, pivotY + y, pivotZ + z);
    }

    public OrientedInstance rotation(Quaternionfc q) {
        rotation.set(q);
        return writeRotation();
    }

    public OrientedInstance rotation(float x, float y, float z, float w) {
        rotation.set(x, y, z, w);
        return writeRotation();
    }

    public OrientedInstance identityRotation() {
        rotation.identity();
        return writeRotation();
    }

    @Override
    public OrientedInstance rotate(Quaternionfc quaternion) {
        rotation.mul(quaternion);
        return writeRotation();
    }

    @Override
    public OrientedInstance flush() {
        super.flush();
        writePosition();
        writePivot();
        return writeRotation();
    }
}
