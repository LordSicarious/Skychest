package Skychest.Mixins.Generation;

import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerating;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.chunk.ChunkGenerationStep;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.SaveProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

import Skychest.TerrainRemoval;
import Skychest.VoidMode;
import Skychest.Mixins.Access.ServerAccess;

// This class is responsible for initialising the post-processing on all chunks
@Mixin(ChunkGenerating.class)
public abstract class WorldGen {

    // Injecting into the generateEntities step because it's the last step that's only called for newly generated chunks.
    // The other post-feature generation steps are called for all chunks, including those being loaded from save.
    // And feature generation is unsuitable due to the multithreading, as blocks generated from neighbouring chunks would avoid the cull.
    // So this is the best injection point available.
    @Inject(at = @At("HEAD"), method = "generateEntities")
    private static void generateEntities(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture<Chunk>> cir) {
        // Skip if generating default world
        SaveProperties props = ((ServerAccess)(context.world())).getServer().getSaveProperties();
        if (((LevelProperties)props).getVoidMode().isDefault()) { return; }
        // This is where blocks are removed
        TerrainRemoval.removeBlocks(chunk, context.world());
        // Generates fresh Heightmaps for the chunk
        long[] blankHeightmap = new PackedIntegerArray(MathHelper.ceilLog2(chunk.getHeight() + 1), 256).getData();
        // Heightmap::setTo should really be a static method, it doesn't use instance at all
        // Regardless, refresh all the heightmaps
        chunk.getHeightmap(Type.MOTION_BLOCKING).setTo(chunk, Type.MOTION_BLOCKING, blankHeightmap);
        chunk.getHeightmap(Type.MOTION_BLOCKING_NO_LEAVES).setTo(chunk, Type.MOTION_BLOCKING_NO_LEAVES, blankHeightmap);
        chunk.getHeightmap(Type.OCEAN_FLOOR).setTo(chunk, Type.OCEAN_FLOOR, blankHeightmap);
        chunk.getHeightmap(Type.WORLD_SURFACE).setTo(chunk, Type.WORLD_SURFACE, blankHeightmap);
        Heightmap.populateHeightmaps(chunk, EnumSet.of(Type.MOTION_BLOCKING, Type.MOTION_BLOCKING_NO_LEAVES, Type.OCEAN_FLOOR, Type.WORLD_SURFACE));
        // Mark the lighting for recalculation
        chunk.getChunkSkyLight().refreshSurfaceY(chunk);
        chunk.setLightOn(false);
        context.lightingProvider().initializeLight(chunk, chunk.isLightOn());
        context.lightingProvider().light(chunk, false);
        // Clear any scheduled ticks, this will save at least some of the floating suspicious sand/gravel
        TerrainRemoval.clearScheduledTicks(chunk);
        // Finally, remove all the entities
        if (((LevelProperties)props).getVoidMode() != VoidMode.SKYCHEST_ALL_ENTITIES) {
            TerrainRemoval.removeEntities((ProtoChunk)chunk);
        }
    }
}