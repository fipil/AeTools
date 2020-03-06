package com.spincoders.aetools.jobs;

import net.minecraft.world.World;

public interface IJobWork {
    void doWork(World world, int x, int z);
    void finish(World world);
    String workName();
}
