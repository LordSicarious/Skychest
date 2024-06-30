package Skychest.Mixins.Access;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.fluid.FluidState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(AbstractBlockState.class)
public interface BlockData {
    @Accessor("FluidState")
    public void setFluidState(FluidState fluidState);
}