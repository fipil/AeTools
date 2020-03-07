package com.spincoders.aetools.commands;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.implementations.tiles.IChestOrDrive;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import com.google.gson.Gson;
import com.spincoders.aetools.AeContainer;
import com.spincoders.aetools.AeTools;
import com.spincoders.aetools.McItem;
import com.spincoders.aetools.Utils;
import com.spincoders.aetools.exportblocks.ExportModBlocksWork;
import com.spincoders.aetools.jobs.RemoveAllModBlocksWork;
import com.spincoders.aetools.jobs.IJob;
import com.spincoders.aetools.jobs.position.PositionJob;
import com.spincoders.aetools.jobs.regions.AllRegionsJob;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AeToolsCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "aetools";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("AeTools mod v%s, pouzijte tyto prikazy:\n", AeTools.VERSION));
        sb.append("file <jmeno_souboru> - zahaji plneni seznamu a nastavi cilovy soubor pro ulozeni seznamu.\n");
        sb.append("save - ulozi soubor, nastavenehy prikazem list.\n");
        sb.append("cancel - zrusi rezim plneni a vymaze seznam.\n");
        sb.append("import <jmeno_souboru> - naimportuje soubory do cilovych disku/truhel.\n");
        return sb.toString();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>(){{
            add("aet");
        }};
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world=sender.getEntityWorld();
        if (world.isRemote)
        {
            System.out.println("Not processing on Client side");
        }
        else
        {
            System.out.println("Processing on Server side");
            if(args.length==2 && args[0].equalsIgnoreCase("file")) {

                File file = new File(args[1]);
                AeTools.instance.itemFile=file;

                if(!AeTools.instance.items.isEmpty()) {
                    sender.addChatMessage(new TextComponentString(Utils.stripAccents("Jmeno ciloveho souboru aktualizovano. Seznam jiz existuje.")));
                    return;
                }
                sender.addChatMessage(new TextComponentString(Utils.stripAccents("Soubor byl nastaven a rezim plneni seznamu zahajen. Nyni klikejte drevenou tyckou pravou mysi na AE chestky nebo drive. Az budete hotovi, použijte prikaz '/aet save' pro ulození seznamu do souboru. Rezim plnení zrusite prikazem '/aet cancel'")));

            } else if(args.length==1 && args[0].equalsIgnoreCase("save")) {
                if(AeTools.instance.itemFile!=null) {
                    if(AeTools.instance.items.isEmpty())
                        sender.addChatMessage(new TextComponentString(Utils.stripAccents("Seznam je prazdny, neni co ulozit.")));
                    else {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(AeTools.instance.itemFile, false));
                            Gson gson=new Gson();
                            writer.write(gson.toJson(AeTools.instance.items.values().toArray()));
                            writer.close();
                            AeTools.instance.items.clear();
                            sender.addChatMessage(new TextComponentString(Utils.stripAccents("Seznam byl ulozen do souboru: "+AeTools.instance.itemFile.getAbsolutePath())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    sender.addChatMessage(new TextComponentString(Utils.stripAccents("Nejprve musite zahajit plneni seznamu polozek prikazem '/aet file <jmeno_souboru>'")));
                }
//                for(CreativeTabs tabs:CreativeTabs.CREATIVE_TAB_ARRAY) {
//                    System.out.println(tabs.);
//                }

            } else if(args.length==1 && args[0].equalsIgnoreCase("cancel")) {
                AeTools.instance.itemFile=null;
                AeTools.instance.items.clear();
                sender.addChatMessage(new TextComponentString(Utils.stripAccents("Rezim plneni byl zrusen a seznam vybranych polozek vymazan.")));
            } else if(args.length==1 && args[0].equalsIgnoreCase("test")) {

                sender.addChatMessage(new TextComponentString(Utils.getRegionDirectory(world).getAbsolutePath()));

            } else if(args.length>1 && args[0].equalsIgnoreCase("clear")) {
                IJob job=AeTools.instance.getJob("clear");
                if(args[1].equalsIgnoreCase("start")) {
                    if(job==null) {
                        if(Utils.tryParseInt(args[2])) {
                            int radius=Integer.parseInt(args[2]);
                            if(radius>0) {
                                AeTools.instance.addJob("clear", new PositionJob(world, new RemoveAllModBlocksWork(), sender, sender.getPosition(), radius));
                                sender.addChatMessage(new TextComponentString("Job byl zahajen na svete: " + world.getWorldInfo().getWorldName() + " do vzdalenosti " + radius + "m na vsechny strany."));
                            } else
                                sender.addChatMessage(new TextComponentString("Chybny parametr. Radius musi byt > 0."));

                        } else if("all".equalsIgnoreCase(args[2])) {
                            AeTools.instance.addJob("clear", new AllRegionsJob(world, new RemoveAllModBlocksWork(), sender, null));
                            sender.addChatMessage(new TextComponentString("Job byl zahajen na svete: " + world.getWorldInfo().getWorldName()+" ve vsech regionech."));
                        } else {
                            AeTools.instance.addJob("clear", new AllRegionsJob(world, new RemoveAllModBlocksWork(), sender, args[2]));
                            sender.addChatMessage(new TextComponentString("Job byl zahajen na svete: " +args[2]+" ve vsech regionech."));
                        }
                    } else {
                        sender.addChatMessage(new TextComponentString("Job uz bezi, na svete: "+job.getWorld().getWorldInfo().getWorldName()));
                    }
                } else if(args[1].equalsIgnoreCase("stop")) {
                    if(job==null) {
                        sender.addChatMessage(new TextComponentString("Job nebezi."));
                    } else {
                        job.stop();
                        sender.addChatMessage(new TextComponentString("Job byl zastaven."));
                    }
                }
                /*WorldBorder border=world.getWorldBorder();
                IBlockState state=world.getBlockState(new BlockPos(-10000,65,100));
                sender.addChatMessage(new TextComponentString(Utils.stripAccents("State:"+state.toString())));*/

            } else if(args.length>2 && args[0].equalsIgnoreCase("registry")) {
                IJob job=AeTools.instance.getJob("registry");
                if(args[1].equalsIgnoreCase("export") && args[2].length()>0 && Utils.tryParseInt(args[3])) {
                    if(job==null) {
                        if(Integer.parseInt(args[3])>0) {
                            AeTools.instance.addJob("registry", new PositionJob(world, new ExportModBlocksWork(sender, args[2]), sender, sender.getPosition(), Integer.parseInt(args[3])));
                            sender.addChatMessage(new TextComponentString("Export byl zahajen na svete: " + world.getWorldInfo().getWorldName()));
                        } else {
                            sender.addChatMessage(new TextComponentString("Chybny parametr radius (musi byt cele kladne cislo > 0)."));
                        }
                    } else  {
                        if(args[2].equalsIgnoreCase("stop")) {
                            job.stop();
                            sender.addChatMessage(new TextComponentString("Export byl zastaven."));
                        } else {
                            sender.addChatMessage(new TextComponentString("Job uz bezi, na svete: "+job.getWorld().getWorldInfo().getWorldName()));
                        }
                    }
                } else {
                    sender.addChatMessage(new TextComponentString(Utils.stripAccents("Chybne parametry, pouzij /aet registry export <jmenoSouboru> <radius>")));
                }
            } else if(args.length==2 && args[0].equalsIgnoreCase("import")) {
                if(!(sender.getCommandSenderEntity() instanceof EntityPlayer)) {
                    sender.addChatMessage(new TextComponentString(Utils.stripAccents("Tento prikaz musi spustit hrac.")));
                    return;
                }
                File file = new File(args[1]);
                if(file.exists() && !file.isDirectory()) {
                    try
                    {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        Gson gson=new Gson();
                        AeContainer[] arr=new AeContainer[0];
                        arr = gson.fromJson(br, arr.getClass());

                        for(AeContainer container:arr) {
                            int importedCount=0;
                            int unknownCount=0;
                            TileEntity te=world.getTileEntity(new BlockPos(container.x, container.y, container.z));
                            if(te instanceof IChestOrDrive) {
                                IChestOrDrive chest = (IChestOrDrive) te;
                                for(IMEInventoryHandler handler:chest.getCellArray(StorageChannel.ITEMS)) {
                                    while(!container.items.isEmpty()) {
                                        McItem mcItem = container.items.get(0);
                                        Item item = Item.REGISTRY.getObject(new ResourceLocation(mcItem.regName));
                                        if (item != null) {
                                            ItemStack itemStack = new ItemStack(item, 1);
                                            item.setDamage(itemStack, mcItem.damage);
                                            appendTagCompound(mcItem, itemStack);
                                            IAEItemStack aeStack = AEApi.instance().storage().createItemStack(itemStack);
                                            aeStack.setStackSize(mcItem.count);
                                            if(handler.canAccept(aeStack)) {
                                                IAEStack notImported=handler.injectItems(aeStack, Actionable.MODULATE, new BaseActionSource());
                                                if(notImported!=null && notImported.getStackSize()>0) {
                                                    mcItem.count=notImported.getStackSize();
                                                    System.out.println("Handler je plny: "+mcItem.regName+", nevzal "+mcItem.count+" ks.");
                                                    break;
                                                } else {
                                                    container.items.remove(0);
                                                }
                                                importedCount++;
                                            } else {
                                                System.out.append("Handler cannot accept: "+mcItem.regName);
                                                break;
                                            }
                                        } else {
                                            unknownCount++;
                                            container.items.remove(0);
                                        }
                                    }
                                }
                                String msg="Do kontejneru "+container.x+","+container.y+","+container.z+" bylo importovano "+importedCount+" polozek, "+unknownCount+" polozek nebylo rozpoznano";
                                if(container.items.size()>0)
                                    msg+=", "+container.items.size()+" polozek se neveslo.";
                                sender.addChatMessage(new TextComponentString(msg));
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    sender.addChatMessage(new TextComponentString(Utils.stripAccents("Zadany soubor neexistuje.")));
                }
            } else {
                sender.addChatMessage(new TextComponentString(Utils.stripAccents("Neznamy prikaz nebo chybne parametry.")));
                sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
            }

        }
    }

    private void appendTagCompound(McItem mcItem, ItemStack newStack) {
        NBTTagCompound tag=null;
        if(mcItem.tagCompound!=null) {
            try {
                tag = JsonToNBT.getTagFromJson(mcItem.tagCompound);
                if(tag!=null) {
                    newStack.setTagCompound(tag);
                }
            } catch (NBTException nex) {
                tag=null;
            }
        }
    }


    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
