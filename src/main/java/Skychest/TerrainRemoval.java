package Skychest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.tick.SimpleTickScheduler;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.LevelProperties;

import Skychest.Mixins.Access.SectionData;
import Skychest.Mixins.Access.ServerAccess;
import Skychest.Mixins.Access.BlockData;
import Skychest.Mixins.Access.TickSchedule;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;


public final class TerrainRemoval {
    
    private static final HashSet<Block> blockWhitelist = new HashSet<Block>(Arrays.asList(Whitelists.BLOCK_WHITELIST));
    private static final HashSet<EntityType<?>> entityWhitelist = new HashSet<EntityType<?>>(Arrays.asList(Whitelists.ENTITY_WHITELIST));

    // Removes all blocks not specified in the whitelist
    public static void removeBlocks(Chunk chunk, ServerWorld world) {
        SaveProperties props = ((ServerAccess)world).getServer().getSaveProperties();
        VoidMode mode = ((LevelProperties)props).getVoidMode();
        ChunkSection[] sections = chunk.getSectionArray();
        for (short i = 0; i < sections.length; i++) {
            ChunkSection section = sections[i];
             // Don't bother with empty sections
            if (section.isEmpty()) { continue; }
            else if (mode.isSkychest()) { whitelistSection(section); }
            else { emptySection(section);}
            // Flag for lighting recalculations
            world.getLightingProvider().setSectionStatus(ChunkSectionPos.from(chunk.getPos(),i),true);
        }
        // Remove any persistent block entities
        for (BlockPos p : chunk.getBlockEntityPositions()) {
            if (chunk.getBlockState(p).isAir()) {
                chunk.removeBlockEntity(p);
            }
        }

        // Generates fresh Heightmaps for the chunk
        long[] blankHeightmap = new PackedIntegerArray(MathHelper.ceilLog2(chunk.getHeight() + 1), 256).getData();
        // Heightmap::setTo should really be a static method, it doesn't use instance at all
        chunk.getHeightmap(Type.MOTION_BLOCKING).setTo(chunk, Type.MOTION_BLOCKING, blankHeightmap);
        chunk.getHeightmap(Type.MOTION_BLOCKING_NO_LEAVES).setTo(chunk, Type.MOTION_BLOCKING_NO_LEAVES, blankHeightmap);
        chunk.getHeightmap(Type.OCEAN_FLOOR).setTo(chunk, Type.OCEAN_FLOOR, blankHeightmap);
        chunk.getHeightmap(Type.WORLD_SURFACE).setTo(chunk, Type.WORLD_SURFACE, blankHeightmap);
        Heightmap.populateHeightmaps(chunk, EnumSet.of(Type.MOTION_BLOCKING, Type.MOTION_BLOCKING_NO_LEAVES, Type.OCEAN_FLOOR, Type.WORLD_SURFACE));
    }

    // Remove all Non-Whitelisted Blocks from a ChunkSection
    public static void whitelistSection(ChunkSection section) {
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
                    else if (blockWhitelist.contains(state.getBlock())) {
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
    private static void emptySection(ChunkSection section) {
        // Simply replaces the existing container with an empty one
        ((SectionData)section).setBlockStateContainer(
            new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE)
        );
        // Set all the counters to 0
        ((SectionData)section).setNonEmptyFluidCount((short)0);
        ((SectionData)section).setNonEmptyBlockCount((short)0);
        ((SectionData)section).setRandomTickableBlockCount((short)0);
    }

    // Removes all entities with IDs not specified in the whitelist
    public static void removeEntities(ProtoChunk chunk) {
        chunk.getEntities().removeIf(e -> !entityWhitelist.contains(EntityType.fromNbt(e).orElse(null)));
    }

    // Clears any ticks scheduled for the chunks during generation
    public static void clearScheduledTicks(Chunk chunk) {
        if (chunk.getFluidTickScheduler() instanceof SimpleTickScheduler) {
            ((TickSchedule)chunk.getFluidTickScheduler()).getScheduledTicks().clear();
            ((TickSchedule)chunk.getFluidTickScheduler()).getScheduledTicksSet().clear();
        }
        if ((chunk.getFluidTickScheduler() instanceof SimpleTickScheduler)) {
            ((TickSchedule)chunk.getBlockTickScheduler()).getScheduledTicks().clear();
            ((TickSchedule)chunk.getBlockTickScheduler()).getScheduledTicksSet().clear();
        }
    }
}