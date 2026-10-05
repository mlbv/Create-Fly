package com.zurrtum.create.client.content.kinetics.base;

import com.zurrtum.create.catnip.theme.Color;
import com.zurrtum.create.client.flywheel.lib.instance.ColoredLitOverlayInstance;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class RotatingInstance extends ColoredLitOverlayInstance {
    public static final float SPEED_MULTIPLIER = 6;

    public static final int OFF_ROT = 12;
    public static final int OFF_POS = 28;
    public static final int OFF_SPEED = 40;
    public static final int OFF_OFFSET = 44;
    public static final int OFF_AXIS = 48;

    private byte rotationAxisX;
    private byte rotationAxisY;
    private byte rotationAxisZ;
    private float x;
    private float y;
    private float z;
    /**
     * Speed in degrees per second
     */
    private float rotationalSpeed;
    /**
     * Offset in degrees
     */
    private float rotationOffset;
    /**
     * Base rotation of the instance, applied before kinetic rotation
     */
    private final Quaternionf rotation = new Quaternionf();

    public RotatingInstance(InstanceType<? extends RotatingInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public static int colorFromBE(KineticBlockEntity be) {
        if (be.network != null) {
            return Color.generateFromLong(be.network).getRGB();
        }
        return 0xFFFFFF;
    }

    public RotatingInstance setup(KineticBlockEntity blockEntity) {
        var blockState = blockEntity.getBlockState();
        var axis = KineticBlockEntityVisual.rotationAxis(blockState);
        return setup(blockEntity, axis, blockEntity.getSpeed());
    }

    public RotatingInstance setup(KineticBlockEntity blockEntity, Axis axis) {
        return setup(blockEntity, axis, blockEntity.getSpeed());
    }

    public RotatingInstance setup(KineticBlockEntity blockEntity, float speed) {
        var blockState = blockEntity.getBlockState();
        var axis = KineticBlockEntityVisual.rotationAxis(blockState);
        return setup(blockEntity, axis, speed);
    }

    public RotatingInstance setup(KineticBlockEntity blockEntity, Axis axis, float speed) {
        var blockState = blockEntity.getBlockState();
        var pos = blockEntity.getBlockPos();
        return setRotationAxis(axis).setRotationalSpeed(speed * SPEED_MULTIPLIER)
            .setRotationOffset(KineticBlockEntityVisual.rotationOffset(
                blockState,
                axis,
                pos
            ) + blockEntity.getRotationAngleOffset(axis));
    }

    public RotatingInstance rotateToFace(Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return rotateToFace(orientation);
    }

    public RotatingInstance rotateToFace(Direction from, Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return rotateToFace(from, orientation);
    }

    public RotatingInstance rotateToFace(Direction orientation) {
        return rotateToFace(orientation.getStepX(), orientation.getStepY(), orientation.getStepZ());
    }

    public RotatingInstance rotateToFace(Direction from, Direction orientation) {
        return rotateTo(
            from.getStepX(),
            from.getStepY(),
            from.getStepZ(),
            orientation.getStepX(),
            orientation.getStepY(),
            orientation.getStepZ()
        );
    }

    public RotatingInstance rotateToFace(float stepX, float stepY, float stepZ) {
        return rotateTo(0, 1, 0, stepX, stepY, stepZ);
    }

    public RotatingInstance rotateTo(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
        rotation.rotateTo(fromX, fromY, fromZ, toX, toY, toZ);
        return writeRotation();
    }

    public RotatingInstance setRotationAxis(Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return setRotationAxis(orientation.step());
    }

    public RotatingInstance setRotationAxis(Vector3f axis) {
        return setRotationAxis(axis.x(), axis.y(), axis.z());
    }

    public RotatingInstance setRotationAxis(float rotationAxisX, float rotationAxisY, float rotationAxisZ) {
        this.rotationAxisX = (byte) (rotationAxisX * 127);
        this.rotationAxisY = (byte) (rotationAxisY * 127);
        this.rotationAxisZ = (byte) (rotationAxisZ * 127);
        return writeAxis();
    }

    public RotatingInstance setPosition(Vec3i pos) {
        return setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public RotatingInstance setPosition(Vector3f pos) {
        return setPosition(pos.x(), pos.y(), pos.z());
    }

    public RotatingInstance setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return writePosition();
    }

    public RotatingInstance nudge(float x, float y, float z) {
        return setPosition(this.x + x, this.y + y, this.z + z);
    }

    public RotatingInstance setColor(KineticBlockEntity blockEntity) {
        colorRgb(colorFromBE(blockEntity));
        return this;
    }

    public RotatingInstance setColor(Color c) {
        color(c.getRed(), c.getGreen(), c.getBlue());
        return this;
    }

    public RotatingInstance setRotationalSpeed(float rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
        MemoryUtil.memPutFloat(slabPtr() + OFF_SPEED, rotationalSpeed);
        return this;
    }

    public RotatingInstance setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
        MemoryUtil.memPutFloat(slabPtr() + OFF_OFFSET, rotationOffset);
        return this;
    }

    private RotatingInstance writeRotation() {
        ExtraMemoryOps.putQuaternionf(slabPtr() + OFF_ROT, rotation);
        return this;
    }

    private RotatingInstance writePosition() {
        ExtraMemoryOps.putVector3f(slabPtr() + OFF_POS, x, y, z);
        return this;
    }

    private RotatingInstance writeAxis() {
        long ptr = slabPtr() + OFF_AXIS;
        MemoryUtil.memPutByte(ptr, rotationAxisX);
        MemoryUtil.memPutByte(ptr + 1, rotationAxisY);
        MemoryUtil.memPutByte(ptr + 2, rotationAxisZ);
        return this;
    }

    @Override
    public RotatingInstance flush() {
        super.flush();
        writeRotation();
        writePosition();
        setRotationalSpeed(rotationalSpeed);
        setRotationOffset(rotationOffset);
        return writeAxis();
    }
}
