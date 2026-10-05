package com.zurrtum.create.client.content.contraptions.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import com.zurrtum.create.content.contraptions.ControlledContraptionEntity;
import net.minecraft.core.Direction.Axis;

public class ControlledContraptionVisual extends ContraptionVisual<ControlledContraptionEntity> {
    public ControlledContraptionVisual(
        VisualizationContext ctx,
        ControlledContraptionEntity entity,
        float partialTick
    ) {
        super(ctx, entity, partialTick);
    }

    @Override
    public void transform(PoseStack matrixStack, float partialTicks) {
        Axis axis = entity.getRotationAxis();
        if (axis != null) {
            float angle = entity.getAngle(partialTicks);
            TransformStack.of(matrixStack).nudge(entity.getId()).center().rotateDegrees(angle, axis).uncenter();
        }
    }
}
