package Skychest;

// Interface injection to make it possible to retrieve Void Mode info from LevelProperties
public interface VoidModeInterface {
    // Basic Getters and Setters
    default VoidMode getVoidMode() {
        return null;
    }
    default void setVoidMode(VoidMode mode) {}
}
