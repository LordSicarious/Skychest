package Skychest.Mixins.Generation;

import net.minecraft.entity.EntityType;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerating;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.chunk.ChunkGenerationStep;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.SaveProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import Skychest.VoidMode;
import Skychest.Mixins.Access.ServerAccess;

// This class is responsible for initialising the post-processing on all chunks
@Deprecated
@Mixin(ChunkGenerating.class)
public abstract class GenerationStepModifications {

    // 
    @Inject(at = @At("RETURN"), method = "generateEntities")
    private static void generateEntities(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> cir) {
        // Get the Void Mode being used
        SaveProperties props = ((ServerAccess)(context.world())).getServer().getSaveProperties();
        VoidMode mode = ((LevelProperties)props).getVoidMode();
        if (mode.isDefault()) { return; }
        if (mode == VoidMode.NOTHING) {
            ((ProtoChunk)chunk).getEntities().clear();
        }
        HashSet<EntityType<?>> whitelist = mode.entityWhitelist();
        if (whitelist != null) {
            ((ProtoChunk)chunk).getEntities().removeIf(
                e -> !whitelist.contains(EntityType.fromNbt(e).orElse(null))
            );
        }
    }

    // redirects to skip generating leaf/fluid ticks
    @Redirect(method = "generateFeatures", at = @At(value = "INVOKE", target = "net/minecraft/world/gen/chunk/Blender.tickLeavesAndFluids(Lnet/minecraft/world/ChunkRegion;Lnet/minecraft/world/chunk/Chunk;)V"))
    private static void skipTicks(ChunkRegion region, Chunk chunk) {}

}