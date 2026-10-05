package com.zurrtum.create.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.zurrtum.create.content.contraptions.minecart.capability.CapabilityMinecartController;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin {
    @Inject(method = "drop(Lnet/minecraft/world/level/ChunkPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;drop(ILnet/minecraft/world/level/chunk/LevelChunk;)V"))
    private void unload(ChunkPos pos, CallbackInfo ci, @Local LevelChunk currentChunk) {
        CapabilityMinecartController.onChunkUnloaded(currentChunk);
    }
}
