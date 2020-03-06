package com.spincoders.aetools.jobs.position;

import com.spincoders.aetools.jobs.IJob;
import com.spincoders.aetools.jobs.IJobWork;
import com.spincoders.aetools.jobs.position.JobDirection;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;

public class PositionJob implements IJob {
    private ArrayList<JobDirection> directions;
    private World world;
    private ICommandSender sender;
    private IJobWork work;
    boolean stopped;

    public PositionJob(World world, IJobWork work, ICommandSender sender, Vec3i pos, int radius) {
        this.world=world;
        this.sender=sender;
        this.work=work;

        sender.addChatMessage(new TextComponentString("Job radius: "+(radius<0?"cela mapa":radius)));

        directions=new ArrayList<JobDirection>();

        try {
            directions.add(new JobDirection(true, work, 0, 1, 1, pos, radius));
            directions.add(new JobDirection(false ,work,1, 0, -1, pos, radius));
            directions.add(new JobDirection(false, work,0, -1, -1, pos, radius));
            directions.add(new JobDirection(false, work,-1, 0, 1, pos, radius));
        } catch(Exception ex) {
            System.out.println("Error creating jobs:"+ex.getMessage());
        }
    }

    public World getWorld() {
        return world;
    }

    public boolean doWork() {

        if(stopped)
            return true;

        boolean allDone=true;

        String report="";
        for(JobDirection dir:directions) {
            dir.doWork(world);
            allDone=allDone && dir.isDone();
            report+=(report.length()>0?"; ":"")+ dir.toString();

            if(stopped)
                break;
        }

        sender.addChatMessage(new TextComponentString(work.workName()+" job: "+report));
        if(allDone) {
            work.finish(world);
            sender.addChatMessage(new TextComponentString(work.workName() + " job byl dokoncen."));
        }
        return allDone || stopped;
    }

    public void stop() {
        stopped=true;
    }
}
