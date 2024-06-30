package Skychest;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;


public final class Whitelists {
    // Blocks are most readily identifiable by their Block object.
    public static final Block[] BLOCK_WHITELIST = {
        // Actual Storage
        Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.BARREL, Blocks.SHULKER_BOX,
        Blocks.WHITE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX,
        Blocks.BROWN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX,
        Blocks.LIME_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX,
        Blocks.BLUE_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.PINK_SHULKER_BOX,
        // Redstone-y Storage
        Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER, Blocks.CRAFTER,  Blocks.LECTERN, Blocks.CHISELED_BOOKSHELF,
        // Blocks that Store things while Processing
        Blocks.FURNACE, Blocks.BLAST_FURNACE, Blocks.SMOKER, Blocks.BREWING_STAND, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE,
        // Pots
        Blocks.DECORATED_POT, Blocks.FLOWER_POT,
        // Potted Saplings
        Blocks.POTTED_ACACIA_SAPLING, Blocks.POTTED_BIRCH_SAPLING, Blocks.POTTED_CHERRY_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, Blocks.POTTED_OAK_SAPLING, Blocks.POTTED_SPRUCE_SAPLING,
        Blocks.POTTED_MANGROVE_PROPAGULE, Blocks.POTTED_AZALEA_BUSH, Blocks.POTTED_FLOWERING_AZALEA_BUSH, Blocks.POTTED_BAMBOO,
        // Potted Fungus
        Blocks.POTTED_CRIMSON_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, Blocks.POTTED_CRIMSON_ROOTS, Blocks.POTTED_WARPED_ROOTS, Blocks.POTTED_RED_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM,
        // Potted Flowers
        Blocks.POTTED_ALLIUM, Blocks.POTTED_AZURE_BLUET, Blocks.POTTED_BLUE_ORCHID, Blocks.POTTED_CORNFLOWER, Blocks.POTTED_DANDELION,
        Blocks.POTTED_LILY_OF_THE_VALLEY, Blocks.POTTED_ORANGE_TULIP, Blocks.POTTED_OXEYE_DAISY, Blocks.POTTED_PINK_TULIP, Blocks.POTTED_POPPY,
        Blocks.POTTED_RED_TULIP, Blocks.POTTED_TORCHFLOWER, Blocks.POTTED_WHITE_TULIP, Blocks.POTTED_WITHER_ROSE,
        // Potted Other Stuff
        Blocks.POTTED_CACTUS, Blocks.POTTED_DEAD_BUSH, Blocks.POTTED_FERN,
        // Blocks that are only sorta storage
        Blocks.SUSPICIOUS_GRAVEL, Blocks.SUSPICIOUS_SAND, Blocks.VAULT, Blocks.END_PORTAL_FRAME
    };

    // Entities are most readily identifiable by their Entity Type.
    public static final EntityType<?>[] ENTITY_WHITELIST = {
        EntityType.ITEM_FRAME,
        EntityType.GLOW_ITEM_FRAME,
        EntityType.CHEST_MINECART,
        EntityType.FURNACE_MINECART,
        EntityType.HOPPER_MINECART,
        EntityType.CHEST_BOAT,
        EntityType.ARMOR_STAND
    };
}