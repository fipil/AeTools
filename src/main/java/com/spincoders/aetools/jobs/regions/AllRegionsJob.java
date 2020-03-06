package com.spincoders.aetools.jobs.regions;

import com.spincoders.aetools.Utils;
import com.spincoders.aetools.jobs.IJob;
import com.spincoders.aetools.jobs.IJobWork;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Stack;

public class AllRegionsJob implements IJob {

    private World world;
    private IJobWork work;
    private ICommandSender sender;
    private Stack<Region> regions=new Stack<Region>();

    private RegionJob current=null;
    private boolean stopped;

    private boolean done;

    public AllRegionsJob(World world, IJobWork work, ICommandSender sender) {
        this.world=world;
        this.work=work;
        this.sender=sender;

        File regionsPath=Utils.getRegionDirectory(world);
        File[] regionFiles = regionsPath.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("r.") && name.endsWith(".mca");
            }
        });

        for(File file:regionFiles) {
            Region reg=new Region(file);
            if(reg.isValid())
                regions.push(reg);
        }
    }

    @Override
    public boolean doWork() {
        if(stopped)
            return true;

        if(current==null && !regions.empty()) {
            Region region=regions.pop();
            current = new RegionJob(region, work, sender);
            sender.addChatMessage(new TextComponentString("Zpracovavam region: "+region));
            sender.addChatMessage(new TextComponentString("Zbyva zpracovat "+regions.size()+" regionu."));
        }

        if(current!=null) {
            long start = System.currentTimeMillis();
            while (!done && !stopped) {
                current.doWork(world);
                if(current.isDone()) {
                    current = null;
                    break;
                }

                if (System.currentTimeMillis() > start + 100)
                    break;
            }
        } else {
            work.finish(world);
            sender.addChatMessage(new TextComponentString(work.workName() + " job byl dokoncen."));

            return true;
        }

        if(current!=null)
            current.notifyProgress();

        return false;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void stop() {
        stopped=true;
    }
}
