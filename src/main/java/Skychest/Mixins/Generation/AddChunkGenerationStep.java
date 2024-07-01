package Skychest.Mixins.Generation;

import net.minecraft.world.chunk.ChunkGenerationSteps;
import net.minecraft.world.chunk.ChunkGenerationSteps.Builder;
import net.minecraft.world.chunk.ChunkStatus;

import java.util.function.UnaryOperator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReceiver;

import Skychest.ModChunkStatus;

// This class is responsible for initialising the post-processing on all chunks
@Mixin(ChunkGenerationSteps.class)
public abstract class AddChunkGenerationStep {

    @ModifyReceiver(method = "<clinit>", at=@At(value = "INVOKE",
                    target = "net/minecraft/world/chunk/ChunkGenerationSteps$Builder.then(Lnet/minecraft/world/chunk/ChunkStatus;Ljava/util/function/UnaryOperator;)Lnet/minecraft/world/chunk/ChunkGenerationSteps$Builder;"))
    private static Builder addStep(Builder receiver, ChunkStatus status, UnaryOperator<Builder> stepFactory) {
        // Step should be inserted just before INITIALIZE_LIGHT 
        if (status == ChunkStatus.INITIALIZE_LIGHT) {
            if (receiver.build().get(ChunkStatus.FEATURES).blockStateWriteRadius() < 1) {
                return receiver.then(ModChunkStatus.POSTPROCESSING, builder -> { return builder; } );
            } else {
                return receiver.then(ModChunkStatus.POSTPROCESSING, builder ->  
                    { return builder.dependsOn(ChunkStatus.FEATURES, 1).task(Skychest.TerrainProcessing::processTerrain); });
            }
        }
        return receiver;
    }
}