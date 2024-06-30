package Skychest.Mixins.Access;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// This is necessary to modify the ChunkSection tracking values to ensure proper updating
@Mixin(ChunkSection.class)
public interface SectionData {
    // Block Count
    @Accessor("nonEmptyBlockCount")
    public void setNonEmptyBlockCount(short count);

    // Fluid Count
    @Accessor("nonEmptyFluidCount")
    public void setNonEmptyFluidCount(short count);

    // Random Tick Count
    @Accessor("randomTickableBlockCount")
    public void setRandomTickableBlockCount(short count);

    // Block Palette, for Void worlds
    @Accessor("blockStateContainer")
    public void setBlockStateContainer(PalettedContainer<BlockState> blockStateContainer);
}