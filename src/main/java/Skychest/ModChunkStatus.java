package Skychest;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ChunkType;
import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;


public final class ModChunkStatus extends ChunkStatus {
    public static final ChunkStatus POSTPROCESSING = register ("postprocessing", ChunkStatus.FEATURES, ChunkStatus.NORMAL_HEIGHTMAP_TYPES, ChunkType.PROTOCHUNK);

    protected ModChunkStatus(@Nullable ChunkStatus previous, EnumSet<Heightmap.Type> heightMapTypes, ChunkType chunkType) {
        super(previous, heightMapTypes, chunkType);
    }
    private static ChunkStatus register(String id, @Nullable ChunkStatus previous, EnumSet<Heightmap.Type> heightMapTypes, ChunkType chunkType) {
        return (ModChunkStatus)Registry.register(Registries.CHUNK_STATUS, id, new ModChunkStatus(previous, heightMapTypes, chunkType));
    }
}