package com.spincoders.aetools.jobs;

import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class JobDirection {
    private int x;
    private int z;
    private int next;

    private Vec3i spawn;
    private int max =5;

    private IJobWork work;

    protected int currentX=0;
    protected int currentZ=0;
    protected boolean done=false;

    protected boolean first;
    private boolean wholeMap;

    private boolean allWasAir;

    public JobDirection(boolean first, IJobWork work, int x, int z, int next, Vec3i spawn, int radius) {
        this.first=first;
        this.x=x;
        this.z=z;
        this.spawn=spawn;
        currentX=spawn.getX();
        currentZ=spawn.getZ();
        this.next=next;
        this.max=radius;
        wholeMap=radius<0;
        this.work=work;
    }

    public boolean isDone() {
        return done;
    }

    public void doWork(World world) {
        long start=System.currentTimeMillis();
        while(!done) {
            if(wholeMap)
                doPositionWorkWholeMap(world);
            else
                doPositionWork(world);

            if(System.currentTimeMillis()>start+100)
                break;
        }
    }

    private void doPositionWorkWholeMap(World world) {
        if(done)
            return;

        boolean allIsAir=false;
        if((currentX!=spawn.getX() || currentZ!=spawn.getZ()) || first )
            allIsAir=work.doWork(world, currentX, currentZ);

        currentX+=x;
        currentZ+=z;

        if(x==0 && allIsAir ) {
            currentX+=next;
            currentZ=spawn.getZ();
            if(allWasAir)
                done=true;
        } else if(z==0 && allIsAir) {
            currentZ+=next;
            currentX=spawn.getX();
            if(allWasAir)
                done=true;
        }

        allWasAir=allIsAir;
    }

    private void doPositionWork(World world) {
        if(done)
            return;

        if((currentX!=spawn.getX() || currentZ!=spawn.getZ()) || first )
        work.doWork(world, currentX, currentZ);

        currentX+=x;
        currentZ+=z;

        if(x==0 && (z>0?currentZ>spawn.getZ()+(max *z):currentZ<spawn.getZ()+(max *z)) ) {
            currentX+=next;
            currentZ=spawn.getZ();
            if( (next>0?currentX>spawn.getX()+(max *next):currentX<spawn.getX()+(max *next)))
                done=true;
        } else if(z==0 &&  (x>0?currentX>spawn.getX()+(max *x):currentX<spawn.getX()+(max *x))) {
            currentZ+=next;
            currentX=spawn.getX();
            if((next>0?currentZ>spawn.getZ()+(max *next):currentZ<spawn.getZ()+(max *next)))
                done=true;
        }
    }

    @Override
    public String toString() {
        return "x:"+currentX+", z:"+currentZ+(done?", done.":"");
    }
}
