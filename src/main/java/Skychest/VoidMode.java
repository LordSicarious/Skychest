package Skychest;

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

    public Whitelist blockWhitelist() {
        switch (this) {
            case DEFAULT :
                return Whitelist.ALL;
            case SKYCHEST :
            case SKYCHEST_ALL_ENTITIES :
                return Whitelist.CONTAINERS;
            default :
                return Whitelist.NONE;
        }
    }

    public Whitelist entityWhitelist() {
        switch (this) {
            case DEFAULT :
            case SKYCHEST_ALL_ENTITIES :
                return Whitelist.ALL;
            case SKYCHEST :
                return Whitelist.CONTAINERS;
            default :
                return Whitelist.NONE;
        }
    }
}
