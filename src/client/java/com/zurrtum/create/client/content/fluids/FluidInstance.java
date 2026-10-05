package com.zurrtum.create.client.content.fluids;

import com.zurrtum.create.client.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.api.instance.InstanceHandle;
import dev.engine_room.flywheel.api.instance.InstanceType;
import org.lwjgl.system.MemoryUtil;

public class FluidInstance extends TransformedInstance {
    public static final int OFF_PROGRESS = 76;
    public static final int OFF_V_SCALE = 80;
    public static final int OFF_V0 = 84;

    private float progress;
    private float vScale;
    private float v0;

    public FluidInstance(InstanceType<? extends FluidInstance> type, InstanceHandle handle) {
        super(type, handle);
    }

    public FluidInstance progress(float progress) {
        this.progress = progress;
        MemoryUtil.memPutFloat(slabPtr() + OFF_PROGRESS, progress);
        return this;
    }

    public FluidInstance vScale(float vScale) {
        this.vScale = vScale;
        MemoryUtil.memPutFloat(slabPtr() + OFF_V_SCALE, vScale);
        return this;
    }

    public FluidInstance v0(float v0) {
        this.v0 = v0;
        MemoryUtil.memPutFloat(slabPtr() + OFF_V0, v0);
        return this;
    }

    @Override
    public FluidInstance flush() {
        super.flush();
        progress(progress);
        vScale(vScale);
        return v0(v0);
    }
}
