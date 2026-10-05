package com.zurrtum.create.client.content.kinetics.base;

import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;

public class RotatingPivotInstance extends RotatingInstance {
    public static final int OFF_PIVOT = 52;

    private float pivotX;
    private float pivotY;
    private float pivotZ;

    public RotatingPivotInstance(InstanceType<? extends RotatingInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public RotatingPivotInstance pivot(float x, float y, float z) {
        pivotX = x;
        pivotY = y;
        pivotZ = z;
        ExtraMemoryOps.putVector3f(slabPtr() + OFF_PIVOT, x, y, z);
        return this;
    }

    public RotatingPivotInstance pivot(Vector3fc pos) {
        return pivot(pos.x(), pos.y(), pos.z());
    }

    public RotatingPivotInstance pivot(Vec3i pos) {
        return pivot(pos.getX(), pos.getY(), pos.getZ());
    }

    public RotatingPivotInstance pivot(Vec3 pos) {
        return pivot((float) pos.x(), (float) pos.y(), (float) pos.z());
    }

    public RotatingPivotInstance translatePivot(float x, float y, float z) {
        return pivot(pivotX + x, pivotY + y, pivotZ + z);
    }

    @Override
    public RotatingPivotInstance flush() {
        super.flush();
        return pivot(pivotX, pivotY, pivotZ);
    }
}
