package com.zurrtum.create.client.flywheel.lib.visual;

import dev.engine_room.flywheel.api.visual.SectionTrackedVisual.SectionCollector;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractBlockEntityVisual<T extends BlockEntity> extends dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual<T> {
    public AbstractBlockEntityVisual(VisualizationContext ctx, T blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
    }

    protected void setSectionCollector(
        SectionCollector sectionCollector,
        int minX,
        int minY,
        int minZ,
        int maxX,
        int maxY,
        int maxZ
    ) {
        lightSections = sectionCollector;
        LongSet longSet = new LongArraySet();
        int offsetX = pos.getX();
        int offsetY = pos.getY();
        int offsetZ = pos.getZ();
        int maxSectionX = SectionPos.posToSectionCoord(offsetX + maxX);
        int maxSectionY = SectionPos.posToSectionCoord(offsetY + maxY);
        int maxSectionZ = SectionPos.posToSectionCoord(offsetZ + maxZ);
        for (int x = SectionPos.posToSectionCoord(offsetX + minX); x <= maxSectionX; x++) {
            for (int y = SectionPos.posToSectionCoord(offsetY + minY); y <= maxSectionY; y++) {
                for (int z = SectionPos.posToSectionCoord(offsetZ + minZ); z <= maxSectionZ; z++) {
                    longSet.add(SectionPos.asLong(x, y, z));
                }
            }
        }
        sectionCollector.sections(longSet);
    }
}
