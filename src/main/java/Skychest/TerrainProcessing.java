package Skychest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.chunk.ChunkGenerationStep;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.tick.SimpleTickScheduler;

import Skychest.Mixins.Access.SectionData;
import Skychest.Mixins.Access.ServerAccess;
import Skychest.Mixins.Access.BlockData;
import Skychest.Mixins.Access.TickSchedule;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;


public final class TerrainProcessing {

    public static CompletableFuture<Chunk> processTerrain(ChunkGenerationContext context, ChunkGenerationStep step, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        SaveProperties props = ((ServerAccess)(context.world())).getServer().getSaveProperties();
        VoidMode mode = ((LevelProperties)props).getVoidMode();
        // Skip step if the terrain is set to default
        if (mode.isDefault()) {
            Heightmap.populateHeightmaps(chunk, ChunkStatus.NORMAL_HEIGHTMAP_TYPES);
            return CompletableFuture.completedFuture(chunk);
        } else if (mode.blockWhitelist() != null) {
            whitelistChunk(chunk, mode.blockWhitelist());
        } else if (mode == VoidMode.NOTHING) {
            emptyChunk(chunk);
        }
        // Generate new heightmaps
        long[] blankHeightmap = new PackedIntegerArray(MathHelper.ceilLog2(chunk.getHeight() + 1), 256).getData();
        ChunkStatus.NORMAL_HEIGHTMAP_TYPES.forEach(
            type -> chunk.getHeightmap(type).setTo(chunk, type, blankHeightmap)
        );
        Heightmap.populateHeightmaps(chunk, ChunkStatus.NORMAL_HEIGHTMAP_TYPES);
        clearScheduledTicks(chunk);
        return CompletableFuture.completedFuture(chunk);
    }

    // Removes all blocks not specified in the whitelist
    public static void whitelistChunk(Chunk chunk, HashSet<Block> whitelist) {
        ChunkSection[] sections = chunk.getSectionArray();
        for (short i = 0; i < sections.length; i++) {
            ChunkSection section = sections[i];
             // Don't bother with empty sections
            if (section.isEmpty()) { continue; }
            else { whitelistSection(section, whitelist); }
        }
        // Remove any persistent block entities
        for (BlockPos p : chunk.getBlockEntityPositions()) {
            if (chunk.getBlockState(p).isAir()) {
                chunk.removeBlockEntity(p);
            }
        }
    }

    // Removes all blocks from a chunk in its entirety
    public static void emptyChunk(Chunk chunk) {
        ChunkSection[] sections = chunk.getSectionArray();
        for (short i = 0; i < sections.length; i++) {
            ChunkSection section = sections[i];
             // Don't bother with empty sections
            if (section.isEmpty()) { continue; }
            else { emptySection(section); }
        }
        // Remove any persistent block entities
        for (BlockPos p : chunk.getBlockEntityPositions()) {
            chunk.removeBlockEntity(p);
        }
    }

    // Remove all Non-Whitelisted Blocks from a ChunkSection
    public static void whitelistSection(ChunkSection section, HashSet<Block> whitelist) {
        BlockState state;
        short blockCount = 0, randomTickCount = 0;
        // Iterate through each block in section
        for (short dy = 0; dy < 16; dy++) {
            for (short dx = 0; dx < 16; dx++) {
                for (short dz = 0; dz < 16; dz++) {
                    state = section.getBlockState(dx, dy, dz);
                    // Skip if it's already air
                    if (state.isAir()) { continue; }
                    // Check if it's a block we want to keep
                    else if (whitelist.contains(state.getBlock())) {
                        // Update block count trackers
                        blockCount++;
                        if (state.hasRandomTicks()) { randomTickCount++; }
                        // Handle Waterlogged Stuff
                        if(!state.getFluidState().isEmpty()) {
                            ((BlockData)state).setFluidState(Fluids.EMPTY.getDefaultState());
                        }
                        continue;
                    // Otherwise, remove the block
                    } else {
                        section.setBlockState(dx, dy, dz, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        // Update Section Counters
        ((SectionData)section).setNonEmptyFluidCount((short)0);
        ((SectionData)section).setNonEmptyBlockCount(blockCount);
        ((SectionData)section).setRandomTickableBlockCount(randomTickCount);
    }

    // Remove all Blocks from a ChunkSection
    private static ChunkSection emptySection(ChunkSection section) {
        // Generates a new ChunkSection with old biome data
        ChunkSection newSection = new ChunkSection(
            new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE),
            section.getBiomeContainer()
        );
        return newSection;
    }

    // Clears any ticks scheduled for the chunks during generation
    public static void clearScheduledTicks(Chunk chunk) {
        if (chunk.getFluidTickScheduler() instanceof SimpleTickScheduler) {
            ((TickSchedule)chunk.getFluidTickScheduler()).getScheduledTicks().clear();
            ((TickSchedule)chunk.getFluidTickScheduler()).getScheduledTicksSet().clear();
        }
        if ((chunk.getBlockTickScheduler() instanceof SimpleTickScheduler)) {
            ((TickSchedule)chunk.getBlockTickScheduler()).getScheduledTicks().clear();
            ((TickSchedule)chunk.getBlockTickScheduler()).getScheduledTicksSet().clear();
        }
    }
}