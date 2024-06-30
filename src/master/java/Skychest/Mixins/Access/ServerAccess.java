package Skychest.Mixins.Access;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// This is necessary to modify the ChunkSection tracking values to ensure proper updating
@Mixin(ServerWorld.class)
public interface ServerAccess {
    // Access Server
    @Accessor("server")
    public MinecraftServer getServer();

}