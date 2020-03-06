package com.spincoders.aetools.jobs.regions;

import com.spincoders.aetools.jobs.IJobWork;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RegionJob {

    private Region region;
    private ICommandSender sender;

    protected int currentX=0;
    protected int currentZ=0;
    protected boolean done=false;

    private int count=0;
    private int progress;
    private int lastNotified=-1;

    protected IJobWork work;

    public RegionJob(Region region, IJobWork work, ICommandSender sender) {
        this.region=region;
        this.work=work;
        this.sender=sender;

        currentX=region.minPosX();
        currentZ=region.minPosZ();
    }

    public boolean isDone() {
        return done;
    }

    public void doWork(World world) {
        if(done)
            return;

        work.doWork(world, currentX, currentZ);

        count++;
        currentX++;
        progress=(int)Math.round((double)count/(double)(512*512)*100.0);
        if(currentX>region.maxPosX()) {
            currentX=region.minPosX();
            currentZ++;
            if(currentZ>region.maxPosZ()) {
                done=true;
            }
        }
    }

    public void notifyProgress() {
        if(lastNotified!=progress) {
            sender.addChatMessage(new TextComponentString(progress + "% done of " + region.name()));
            lastNotified=progress;
        }
    }
}
