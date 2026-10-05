package com.zurrtum.create.client.flywheel.lib.model.baked;

import dev.engine_room.flywheel.api.material.Material;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.jspecify.annotations.Nullable;

public interface BlockMaterialFunction {
    @Nullable Material apply(ChunkSectionLayer chunkRenderType, boolean shaded, boolean ambientOcclusion);
}
