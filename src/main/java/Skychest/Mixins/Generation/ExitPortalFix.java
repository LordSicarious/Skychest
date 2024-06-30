package Skychest.Mixins.Generation;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.dimension.YLevels;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EnderDragonFight.class)
public abstract class ExitPortalFix {

    // Accesses the exitPortalLocation field from the actual EnderDragonFight class
    @Shadow
    private BlockPos exitPortalLocation;

    // The Exit Portal generates partially below world limit with no terrain around, shifting it up a block if so resolves this.
    @Inject(method = "generateEndPortal", at = @At(value = "INVOKE", shift = Shift.BEFORE,
        target = "net/minecraft/world/gen/feature/EndPortalFeature.generateIfValid(Lnet/minecraft/world/gen/feature/FeatureConfig;Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
    private void generateEndPortal(boolean killedTheDragon, CallbackInfo ci) {
        Mutable pos = exitPortalLocation.mutableCopy();
        pos.setY(Math.max(pos.getY(),YLevels.END_MIN_Y+1));
        exitPortalLocation=pos;
    }

}