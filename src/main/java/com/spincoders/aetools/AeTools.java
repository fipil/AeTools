package com.spincoders.aetools;

import com.spincoders.aetools.commands.AeToolsCommand;
import com.spincoders.aetools.handlers.PlayerEventHandler;
import com.spincoders.aetools.jobs.IJob;
import com.spincoders.aetools.proxy.CommonProxy;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod(modid = AeTools.MODID, version = AeTools.VERSION, name=AeTools.NAME, acceptableRemoteVersions = "*", useMetadata = true)
public class AeTools {
    public static final String MODID = "aetools";
    public static final String VERSION = "1.0";
    public static final String NAME = "AETools";

    @SidedProxy(clientSide = "com.spincoders.aetools.proxy.ClientProxy", serverSide = "com.spincoders.aetools.proxy.CommonProxy")
    public static CommonProxy proxy;

    private PlayerEventHandler plHandler;

    public static Logger logger;

    private HashMap<String,IJob> jobs=new HashMap<String,IJob>();

    public static void logInfo(String text) {
        if(logger!=null)
            logger.info(Utils.colored(text));
        else
            System.out.append(text);
    }

    public static void logError(String text) {
        if(logger!=null)
            logger.info(Utils.colored(text, Utils.COLOR_RED, true));
        else
            System.out.append(text);
    }

    @Mod.Instance
    public static AeTools instance;

    public Map<Vec3i, AeContainer> items = new HashMap<Vec3i, AeContainer>();
    public File itemFile=null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        AeTools.logger=event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

        logInfo(" _   _    _ _  _   _      _ ");
        logInfo("|_| |_  _  |  | | | | |  |_ ");
        logInfo("| | |_     |  |_| |_| |_  _|");
        logInfo("____________________________");
        logInfo("");

        plHandler =new PlayerEventHandler();
        MinecraftForge.EVENT_BUS.register(plHandler);

        event.registerServerCommand(new AeToolsCommand());
    }

    public boolean hasJob(String name) {
        return jobs.containsKey(name);
    }

    public boolean addJob(String name, IJob job) {
        if(!hasJob(name)) {
            jobs.put(name, job);
            return true;
        }
        return false;
    }

    public IJob getJob(String name) {
        return hasJob(name)?jobs.get(name):null;
    }

    public void removeJob(IJob job) {
        Iterator<Map.Entry<String,IJob>> iterator = jobs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if (job.equals(entry.getValue())) {
                iterator.remove();
            }
        }
    }

    public void doJobs() {
        Iterator<Map.Entry<String,IJob>> iterator = jobs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            IJob job=(IJob)entry.getValue();
            boolean done=job.doWork();
            if(done)
                iterator.remove();
        }
    }
}
