package Skychest.Mixins.Generation;

import net.minecraft.world.chunk.ChunkStatus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import Skychest.ModChunkStatus;

// Tells the INITIALIZE_LIGHT stage of ChunkGeneration that it comes after POSTPROCESSING
@Mixin(ChunkStatus.class)
public abstract class ChunkStatusReorder {
    private static final int GETSTATIC = 178;

    @ModifyExpressionValue(method = "<clinit>", at=@At(value = "FIELD", opcode = GETSTATIC,
                    target = "net/minecraft/world/chunk/ChunkStatus.FEATURES:Lnet/minecraft/world/chunk/ChunkStatus;"))
    private static ChunkStatus reorder(ChunkStatus receiver) {
        return ModChunkStatus.POSTPROCESSING;
    }
}