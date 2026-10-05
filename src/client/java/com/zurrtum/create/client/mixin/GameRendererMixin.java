package com.zurrtum.create.client.mixin;

import com.zurrtum.create.client.catnip.render.EntityBlockLayer;
import com.zurrtum.create.client.catnip.render.EntityBlockLightLayer;
import com.zurrtum.create.client.catnip.render.EntityBlockMultipleLayer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderLevel(Lnet/minecraft/client/DeltaTracker;)V", at = @At("TAIL"))
    private void recycleAll(DeltaTracker deltaTracker, CallbackInfo ci) {
        EntityBlockLightLayer.recycleAll();
        EntityBlockLayer.recycleAll();
        EntityBlockMultipleLayer.recycleAll();
    }
}
