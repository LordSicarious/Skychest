package Skychest;

import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;

public enum VoidMode {
    DEFAULT,
    NOTHING,
    SKYCHEST,
    SKYCHEST_ALL_ENTITIES;

    public Text toText() {
        return Text.literal(this.toString());
    }

    @Override
    public String toString() {
        switch (this) {
            case DEFAULT : return "Default";
            case NOTHING : return "Nothing";
            case SKYCHEST : return "Containers Only";
            case SKYCHEST_ALL_ENTITIES : return "Containers + All Entities";
            default : return "?";
        }
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }

    public HashSet<Block> blockWhitelist() {
        switch (this) {
            case SKYCHEST :
            case SKYCHEST_ALL_ENTITIES :
                return Whitelist.SKYCHEST.getBlockWhitelist();
            default :
                return null;
        }
    }

    public HashSet<EntityType<?>> entityWhitelist() {
        switch (this) {
            case SKYCHEST :
                return Whitelist.SKYCHEST.getEntityWhitelist();
            default :
                return null;
        }
    }
}
