package com.zurrtum.create.client.content.contraptions.wrench;

import com.zurrtum.create.catnip.levelWrappers.WrappedLevel;
import dev.engine_room.flywheel.api.visualization.VisualizationLevel;
import net.minecraft.world.level.Level;

public class NonVisualizationLevel extends WrappedLevel implements VisualizationLevel {
    public NonVisualizationLevel(Level level) {
        super(level);
    }

    @Override
    public boolean supportsVisualization() {
        return false;
    }
}
