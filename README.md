# Skychest Challenge
The Skychest Challenge Mod for Minecraft. The generation should be completely seed-compatible with Vanilla, so structures 

# Dependencies
Skychest relies on the Fabric modloader. For instructions on installing Fabric, visit their homepage at https://fabricmc.net/

# Installation
Copy the .JAR file into your Mods folder.

# World Generation
When creating a new world, an additional option is added in the World tab, called Void Mode. There are four different Void Modes:
- DEFAULT: Vanilla world generation, unaltered by the mod.
- NOTHING: Absolutely nothing spawns. This is so you can play starting from just the Bonus Chest, which is spawned _after_ world generation.
- CONTAINERS ONLY: The star of the show, only storage blocks and entities will successfully generate. It's recommended to use the Bonus Chest, but you don't have to, and it's surprisingly not always necessary.
- CONTAINERS + ALL ENTITIES: Like regular Containers Only, but this time around all structure entities are preserved, rather than just storage. This makes a lot more passive mobs potentially available from Village animal pens, but also mobs like Shulkers and Piglin Brutes.

# General Changes
There are only two general changes implemented by this mod, and neither should affect any Vanilla worlds:
- If the Bonus Chest spawns at the World Bottom or more than 10 Blocks away from the player, it is instead placed directly under the player's feet.
- The Exit Portal in the End always generates at least 1 block above the lower build limit, to prevent broken generation.
