package com.spincoders.aetools.jobs;

import net.minecraft.world.World;

public interface IJob {
    boolean doWork();
    World getWorld();
    void stop();
}
