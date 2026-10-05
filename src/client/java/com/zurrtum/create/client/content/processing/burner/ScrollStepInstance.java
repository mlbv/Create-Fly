package com.zurrtum.create.client.content.processing.burner;

import com.zurrtum.create.client.catnip.render.SpriteShiftEntry;
import com.zurrtum.create.client.flywheel.lib.instance.ColoredLitOverlayInstance;
import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import org.lwjgl.system.MemoryUtil;

public class ScrollStepInstance extends ScrollInstance {
    public static final int OFF_STEP = 72;

    private float stepU = 1;
    private float stepV = 1;

    public ScrollStepInstance(InstanceType<? extends ColoredLitOverlayInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public ScrollStepInstance setSpriteShift(
        SpriteShiftEntry spriteShift,
        float factorU,
        float factorV,
        float factorStepU,
        float factorStepV
    ) {
        float spriteWidth = spriteShift.getTarget().getU1() - spriteShift.getTarget().getU0();
        float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
        spriteShift(
            spriteShift.getTarget().getU0() - spriteShift.getOriginal().getU0(),
            spriteShift.getTarget().getV0() - spriteShift.getOriginal().getV0(),
            spriteWidth * factorU * factorStepU,
            spriteHeight * factorV * factorStepV
        );
        stepU = factorStepU;
        stepV = factorStepV;
        return writeStep();
    }

    private ScrollStepInstance writeStep() {
        long ptr = slabPtr() + OFF_STEP;
        MemoryUtil.memPutFloat(ptr, stepU);
        MemoryUtil.memPutFloat(ptr + 4, stepV);
        return this;
    }

    @Override
    public ScrollStepInstance flush() {
        super.flush();
        return writeStep();
    }
}
