package Skychest.Mixins.Generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.BonusChestFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;


@Mixin(BonusChestFeature.class)
public abstract class BonusChestFix {

    // Ensures the Bonus Chest generates in reach of the player, or directly underneath if the player would otherwise die
    @Inject(method = "generate", at = @At(value = "INVOKE", shift = At.Shift.BY, by = 2,
        target = "net/minecraft/world/StructureWorldAccess.getTopPosition(Lnet/minecraft/world/Heightmap$Type;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;"),
        cancellable = true)
    private void fixBonusChest(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> cir, 
                    @Local LocalRef<BlockPos> blockPos, @Local StructureWorldAccess world) {
        BlockPos p = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, context.getOrigin());
        System.out.println("Vanilla Position: " + blockPos.get().toString());
        System.out.println("Origin Position: " + p.toString());
        if (p.getY() <= 1+world.getBottomY() || blockPos.get().getSquaredDistance(p) > 100) {
            blockPos.set(p);
        }
    }

}