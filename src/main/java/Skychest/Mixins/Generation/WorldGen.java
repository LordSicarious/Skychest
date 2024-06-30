package Skychest.Mixins.Generation;

import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerating;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.chunk.ChunkGenerationStep;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.SaveProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

import Skychest.TerrainRemoval;
import Skychest.VoidMode;
import Skychest.Mixins.Access.ServerAccess;

// This class is responsible for initialising the post-processing on all chunks
@Mixin(ChunkGenerating.class)
public abstract class WorldGen {

    // Blocks need to be removed at least 2 steps after feature generation, as otherwise neighbouring chunks on different processing threads will
    // bleed over into the current chunk after removal, resulting in lots of partial features surviving the cull.
    @Inject(at = @At("HEAD"), method = "light")
    private static void light(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> cir) {
        // Skip if generating default world
        SaveProperties props = ((ServerAccess)(context.world())).getServer().getSaveProperties();
        if (((LevelProperties)props).getVoidMode().isDefault()) { return; }
        TerrainRemoval.removeBlocks(chunk, context.world());
        // Need to redo most of light initialization due to changes in the terrain
        chunk.getChunkSkyLight().refreshSurfaceY(chunk);
        chunk.setLightOn(false);
        context.lightingProvider().initializeLight(chunk, chunk.isLightOn());
    }

    // When chunks are being finalised, structure entities are removed
    // Also, any scheduled ticks are cleared, to prevent suspicious sand/gravel falling due to previously scheduled updates
    @Inject(at = @At("HEAD"), method = "convertToFullChunk")
    private static void convertToFullChunk(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> cir) {
        // Skip if generating default world
        SaveProperties props = ((ServerAccess)(context.world())).getServer().getSaveProperties();
        if (((LevelProperties)props).getVoidMode().isDefault()) { return; }
        if (((LevelProperties)props).getVoidMode() != VoidMode.SKYCHEST_ALL_ENTITIES) {
            TerrainRemoval.removeEntities((ProtoChunk)chunk);
        }
        TerrainRemoval.clearScheduledTicks(chunk);
    }
}