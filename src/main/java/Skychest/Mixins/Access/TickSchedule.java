package Skychest.Mixins.Access;

import net.minecraft.world.tick.Tick;
import net.minecraft.world.tick.SimpleTickScheduler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Set;

// This is necessary to get direct access to the scheduled ticks in order to clear them after removing the terrain

@Mixin(SimpleTickScheduler.class)
public interface TickSchedule {
    @Accessor("scheduledTicks")
    public List<Tick<?>> getScheduledTicks();

    @Accessor("scheduledTicksSet")
    public Set<Tick<?>> getScheduledTicksSet();
}