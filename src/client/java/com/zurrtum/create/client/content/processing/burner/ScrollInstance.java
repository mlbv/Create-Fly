package com.zurrtum.create.client.content.processing.burner;

import com.zurrtum.create.client.catnip.render.SpriteShiftEntry;
import com.zurrtum.create.client.flywheel.lib.instance.ColoredLitOverlayInstance;
import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.lib.util.ExtraMemoryOps;
import net.minecraft.core.Vec3i;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.lwjgl.system.MemoryUtil;

public class ScrollInstance extends ColoredLitOverlayInstance {
    public static final int OFF_POS = 12;
    public static final int OFF_ROT = 24;
    public static final int OFF_SPEED = 40;
    public static final int OFF_DIFF = 48;
    public static final int OFF_SCALE = 56;
    public static final int OFF_OFFSET = 64;

    private float x;
    private float y;
    private float z;
    private final Quaternionf rotation = new Quaternionf();
    private float speedU;
    private float speedV;
    private float offsetU;
    private float offsetV;
    private float diffU;
    private float diffV;
    private float scaleU;
    private float scaleV;

    public ScrollInstance(InstanceType<? extends ColoredLitOverlayInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public ScrollInstance position(Vec3i position) {
        return position(position.getX(), position.getY(), position.getZ());
    }

    public ScrollInstance position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        ExtraMemoryOps.putVector3f(slabPtr() + OFF_POS, x, y, z);
        return this;
    }

    public ScrollInstance shift(float x, float y, float z) {
        return position(this.x + x, this.y + y, this.z + z);
    }

    public ScrollInstance rotation(Quaternionfc rotation) {
        this.rotation.set(rotation);
        ExtraMemoryOps.putQuaternionf(slabPtr() + OFF_ROT, this.rotation);
        return this;
    }

    public ScrollInstance setSpriteShift(SpriteShiftEntry spriteShift) {
        return setSpriteShift(spriteShift, 0.5f, 0.5f);
    }

    public ScrollInstance setSpriteShift(SpriteShiftEntry spriteShift, float factorU, float factorV) {
        float spriteWidth = spriteShift.getTarget().getU1() - spriteShift.getTarget().getU0();
        float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
        return spriteShift(
            spriteShift.getTarget().getU0() - spriteShift.getOriginal().getU0(),
            spriteShift.getTarget().getV0() - spriteShift.getOriginal().getV0(),
            spriteWidth * factorU,
            spriteHeight * factorV
        );
    }

    protected ScrollInstance spriteShift(float diffU, float diffV, float scaleU, float scaleV) {
        this.diffU = diffU;
        this.diffV = diffV;
        this.scaleU = scaleU;
        this.scaleV = scaleV;
        long ptr = slabPtr();
        MemoryUtil.memPutFloat(ptr + OFF_DIFF, diffU);
        MemoryUtil.memPutFloat(ptr + OFF_DIFF + 4, diffV);
        MemoryUtil.memPutFloat(ptr + OFF_SCALE, scaleU);
        MemoryUtil.memPutFloat(ptr + OFF_SCALE + 4, scaleV);
        return this;
    }

    public ScrollInstance speed(float speedU, float speedV) {
        this.speedU = speedU;
        this.speedV = speedV;
        long ptr = slabPtr() + OFF_SPEED;
        MemoryUtil.memPutFloat(ptr, speedU);
        MemoryUtil.memPutFloat(ptr + 4, speedV);
        return this;
    }

    public ScrollInstance offsetV(float offsetV) {
        return offset(offsetU, offsetV);
    }

    public ScrollInstance offset(float offsetU, float offsetV) {
        this.offsetU = offsetU;
        this.offsetV = offsetV;
        long ptr = slabPtr() + OFF_OFFSET;
        MemoryUtil.memPutFloat(ptr, offsetU);
        MemoryUtil.memPutFloat(ptr + 4, offsetV);
        return this;
    }

    @Override
    public ScrollInstance flush() {
        super.flush();
        position(x, y, z);
        rotation(rotation);
        speed(speedU, speedV);
        offset(offsetU, offsetV);
        return spriteShift(diffU, diffV, scaleU, scaleV);
    }
}
