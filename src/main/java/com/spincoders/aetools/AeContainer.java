package com.spincoders.aetools;

import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class AeContainer {
    public int x;
    public int y;
    public int z;
    public ArrayList<McItem> items;

    public AeContainer(Vec3i pos) {
        x=pos.getX();
        y=pos.getY();
        z=pos.getZ();
        items=new ArrayList<McItem>();
    }
}
