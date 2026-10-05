package com.zurrtum.create.client.content.processing.burner;

import com.zurrtum.create.client.catnip.render.SpriteShiftEntry;
import com.zurrtum.create.client.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import org.lwjgl.system.MemoryUtil;

public class ScrollTransformedInstance extends TransformedInstance {
    public static final int OFF_SPEED = 76;
    public static final int OFF_DIFF = 84;
    public static final int OFF_SCALE = 92;
    public static final int OFF_OFFSET = 100;

    private float speedU;
    private float speedV;
    private float offsetU;
    private float offsetV;
    private float diffU;
    private float diffV;
    private float scaleU;
    private float scaleV;

    public ScrollTransformedInstance(InstanceType<? extends TransformedInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public ScrollTransformedInstance setSpriteShift(SpriteShiftEntry spriteShift) {
        return setSpriteShift(spriteShift, 0.5f, 0.5f);
    }

    public ScrollTransformedInstance setSpriteShift(SpriteShiftEntry spriteShift, float factorU, float factorV) {
        float spriteWidth = spriteShift.getTarget().getU1() - spriteShift.getTarget().getU0();
        float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
        scaleU = spriteWidth * factorU;
        scaleV = spriteHeight * factorV;
        diffU = spriteShift.getTarget().getU0() - spriteShift.getOriginal().getU0();
        diffV = spriteShift.getTarget().getV0() - spriteShift.getOriginal().getV0();
        return writeSpriteShift();
    }

    private ScrollTransformedInstance writeSpriteShift() {
        long ptr = slabPtr();
        MemoryUtil.memPutFloat(ptr + OFF_DIFF, diffU);
        MemoryUtil.memPutFloat(ptr + OFF_DIFF + 4, diffV);
        MemoryUtil.memPutFloat(ptr + OFF_SCALE, scaleU);
        MemoryUtil.memPutFloat(ptr + OFF_SCALE + 4, scaleV);
        return this;
    }

    public ScrollTransformedInstance speed(float speedU, float speedV) {
        this.speedU = speedU;
        this.speedV = speedV;
        long ptr = slabPtr() + OFF_SPEED;
        MemoryUtil.memPutFloat(ptr, speedU);
        MemoryUtil.memPutFloat(ptr + 4, speedV);
        return this;
    }

    public ScrollTransformedInstance offset(float offsetU, float offsetV) {
        this.offsetU = offsetU;
        this.offsetV = offsetV;
        long ptr = slabPtr() + OFF_OFFSET;
        MemoryUtil.memPutFloat(ptr, offsetU);
        MemoryUtil.memPutFloat(ptr + 4, offsetV);
        return this;
    }

    @Override
    public ScrollTransformedInstance flush() {
        super.flush();
        speed(speedU, speedV);
        offset(offsetU, offsetV);
        return writeSpriteShift();
    }
}
