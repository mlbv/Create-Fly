package com.zurrtum.create.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zurrtum.create.client.Create;
import com.zurrtum.create.client.catnip.animation.AnimationTickHolder;
import com.zurrtum.create.client.catnip.ghostblock.GhostBlocks;
import com.zurrtum.create.client.catnip.outliner.Outliner;
import com.zurrtum.create.client.content.contraptions.actors.seat.ContraptionPlayerPassengerRotation;
import com.zurrtum.create.client.content.contraptions.minecart.CouplingRenderer;
import com.zurrtum.create.client.content.equipment.clipboard.ClipboardValueSettingsClientHandler;
import com.zurrtum.create.client.content.equipment.symmetryWand.SymmetryHandlerClient;
import com.zurrtum.create.client.content.kinetics.chainConveyor.ChainConveyorInteractionHandler;
import com.zurrtum.create.client.content.trains.entity.CarriageCouplingRenderer;
import com.zurrtum.create.client.content.trains.track.TrackBlockOutline;
import com.zurrtum.create.client.content.trains.track.TrackTargetingClient;
import com.zurrtum.create.client.infrastructure.render.BreakingRenderStateInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    @Final
    private GameRenderer gameRenderer;

    @Inject(method = "submitFeatures(Lnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;finalizeGizmoCollection()V"))
    private void afterSubmitParticles(
        LevelRenderState levelRenderState,
        SubmitNodeCollector submitNodeCollector,
        boolean renderOutline,
        CallbackInfo ci,
        @Local PoseStack poseStack
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        CameraRenderState cameraRenderState = levelRenderState.cameraRenderState;
        float lineWidth = gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;
        Vec3 cameraPos = cameraRenderState.pos;
        GhostBlocks.getInstance().renderAll(minecraft, poseStack, submitNodeCollector, cameraPos);
        Outliner.getInstance().submitOutlines(
            minecraft,
            poseStack,
            submitNodeCollector,
            cameraPos,
            AnimationTickHolder.getPartialTicks()
        );
        TrackBlockOutline.drawCurveSelection(minecraft, poseStack, submitNodeCollector, cameraPos, lineWidth);
        TrackTargetingClient.render(minecraft, poseStack, submitNodeCollector, cameraPos);
        CouplingRenderer.renderAll(minecraft, poseStack, submitNodeCollector, cameraPos);
        CarriageCouplingRenderer.renderAll(minecraft, poseStack, submitNodeCollector, cameraPos);
        Create.SCHEMATIC_HANDLER.render(minecraft, poseStack, submitNodeCollector, cameraRenderState);
        ChainConveyorInteractionHandler.drawCustomBlockSelection(poseStack, submitNodeCollector, cameraPos, lineWidth);
        SymmetryHandlerClient.onRenderWorld(minecraft, poseStack, submitNodeCollector, cameraPos);
        ContraptionPlayerPassengerRotation.frame(minecraft);
    }

    @Inject(method = "submitBlockOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/LevelRenderState;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/level/LevelRenderState;cameraRenderState:Lnet/minecraft/client/renderer/state/level/CameraRenderState;", opcode = Opcodes.GETFIELD), cancellable = true)
    private void hideBlockOutline(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        LevelRenderState levelRenderState,
        CallbackInfo ci
    ) {
        if (ChainConveyorInteractionHandler.hideVanillaBlockSelection()) {
            ci.cancel();
        }
    }

    @Inject(method = "submitBlockOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/LevelRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/level/BlockOutlineRenderState;highContrast()Z", ordinal = 0), cancellable = true)
    private void onSubmitBlockOutline(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        LevelRenderState levelRenderState,
        CallbackInfo ci,
        @Local BlockOutlineRenderState state
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        float width = gameRenderer.gameRenderState().windowRenderState.appropriateLineWidth;
        if (ClipboardValueSettingsClientHandler.drawCustomBlockSelection(
            minecraft,
            state.pos(),
            width,
            submitNodeCollector,
            poseStack
        ) || TrackBlockOutline.drawCustomBlockSelection(
            minecraft,
            state.pos(),
            width,
            submitNodeCollector,
            poseStack
        )) {
            poseStack.popPose();
            ci.cancel();
        }
    }

    @WrapOperation(method = "submitBlockDestroyAnimation(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/LevelRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockStateModelSet;get(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;"))
    private BlockStateModel getRenderModel(
        BlockStateModelSet instance,
        BlockState state,
        Operation<BlockStateModel> original,
        @Local BlockBreakingRenderState renderState
    ) {
        return ((BreakingRenderStateInfo) (Object) renderState).create$getRenderModel();
    }
}
