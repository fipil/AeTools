package com.spincoders.aetools.handlers;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.implementations.tiles.IChestOrDrive;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import com.spincoders.aetools.AeContainer;
import com.spincoders.aetools.AeTools;
import com.spincoders.aetools.CraftingPattern;
import com.spincoders.aetools.McItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class PlayerEventHandler {
    @SubscribeEvent
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        onPlayerInteract(event, false);
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        onPlayerInteract(event, true);
    }

    public void onPlayerInteract(PlayerInteractEvent event, boolean rightClick) {
        World world=event.getWorld();
        if(!world.isRemote) {
            TileEntity te=world.getTileEntity(event.getPos());

            Item item=event.getItemStack()!=null?event.getItemStack().getItem():null;
            EntityPlayer player=event.getEntityPlayer();
            if(item!=null && "minecraft:stick".equalsIgnoreCase(item.getRegistryName().toString()) && rightClick ) {
                String name="(unknown)";
                try {
                    name=te.getDisplayName().getUnformattedText();
                }   catch(NullPointerException ex) {
                }
                player.addChatMessage(new TextComponentString("Kliknuta kostka: "+name));

                Class[] interfaces = te.getClass().getInterfaces();
                for (Class iface : interfaces) {
                    player.addChatMessage(new TextComponentString("Interface Name = " + iface.getName()));
                }

                if(te instanceof IChestOrDrive) {
                    IChestOrDrive chest=(IChestOrDrive)te;

                    if(AeTools.instance.itemFile==null) {
                        player.addChatMessage(new TextComponentString("Nejprve nastavte jmeno ciloveho souboru prikazem /aet file <jmeno_souboru>."));
                        event.setCanceled(true);
                        return;
                    }

                    AeContainer container= AeTools.instance.items.get(te.getPos());
                    if(container!=null) {
                        player.addChatMessage(new TextComponentString("Tento drive/chest uz byl vybran."));
                        event.setCanceled(true);
                        return;
                    }
                    container=new AeContainer(te.getPos());
                    AeTools.instance.items.put(te.getPos(), container);

                    for(IMEInventoryHandler handler:chest.getCellArray(StorageChannel.ITEMS)) {
                        IItemList<IAEItemStack> list=handler.getChannel().createList();
                        list=handler.getAvailableItems(list);

                        for(IAEItemStack stack:list) {
                            Item sitem=stack.getItem();

                            String nbt=null;
                            if(sitem instanceof ICraftingPatternItem) {
                                ICraftingPatternItem patt=(ICraftingPatternItem)sitem;
                                ICraftingPatternDetails details=patt.getPatternForItem(stack.getItemStack(), world);

                                nbt=preparePatternNBT(patt, details);
                            } else {

                                if(stack.hasTagCompound()) {
                                    NBTTagCompound tag = stack.getItemStack().getTagCompound();
                                    if(tag.hasKey("ench")) {
                                        NBTTagList enchants = tag.getTagList("ench", Constants.NBT.TAG_COMPOUND);
                                        if(enchants!=null && enchants.tagCount()>0) {
                                            String ench="";
                                            int repairCost=tag.getInteger("RepairCost");
                                            for(int i=0;i<enchants.tagCount();i++) {
                                                int lvl=((NBTTagCompound)enchants.get(i)).getInteger("lvl");
                                                int id=((NBTTagCompound)enchants.get(i)).getInteger("id");
                                                ench+=(ench.length()>0?",":"")+"{lvl:"+lvl+"s,id:"+id+"s}";
                                            }
                                            nbt="{ench:["+ench+"],RepairCost:"+repairCost+"}";
                                        }
                                    } else if(tag.hasKey("StoredEnchantments")) {
                                        //"{StoredEnchantments:[0:{lvl:5s,id:17s}]}"
                                        NBTTagList enchants = tag.getTagList("StoredEnchantments", Constants.NBT.TAG_COMPOUND);
                                        if(enchants!=null && enchants.tagCount()>0) {
                                            String ench="";
                                            for(int i=0;i<enchants.tagCount();i++) {
                                                int lvl=((NBTTagCompound)enchants.get(i)).getInteger("lvl");
                                                int id=((NBTTagCompound)enchants.get(i)).getInteger("id");
                                                ench+=(ench.length()>0?",":"")+"{lvl:"+lvl+"s,id:"+id+"s}";
                                            }
                                            nbt="{StoredEnchantments:["+ench+"]}";
                                        }
                                    }
                                }
                                if(nbt==null)
                                    nbt = stack.hasTagCompound()?stack.getTagCompound().toString():null;
                            }
                            container.items.add(new McItem(
                                    stack.getItem().getUnlocalizedName(),
                                    Item.REGISTRY.getNameForObject(sitem).toString(),
                                    stack.getItemDamage(),
                                    (int)stack.getStackSize(),
                                    nbt));
                            /*interfaces = sitem.getClass().getInterfaces();
                            for (Class iface : interfaces) {
                                player.addChatMessage(new TextComponentString("   Interface Name = " + iface.getName()));
                            }*/

                        }
                    }
                    player.addChatMessage(new TextComponentString("Obsah tohoto kontejneru byl pridan do exportu. Celkem nalezeno " + container.items.size()+" polozek."));
                    event.setCanceled(true);
                }


            }
            //logger.info(String.format("Player intract: %s, %s",event.toString(), item!=null?item.getUnlocalizedName():"<no item>" ));
        }
    }

    private String preparePatternNBT(ICraftingPatternItem patt, ICraftingPatternDetails details) {

        CraftingPattern pattern=new CraftingPattern();

        pattern.isCraftable=details.isCraftable();
        pattern.canSubstitute=details.canSubstitute();

        for(IAEItemStack inp : details.getInputs()) {
            if(inp!=null) {
                pattern.inputs.add(new McItem(
                        inp.getItem().getUnlocalizedName(),
                        Item.REGISTRY.getNameForObject(inp.getItem()).toString(),
                        inp.getItemDamage(),
                        (int)inp.getStackSize(),
                        null));
            } else {
                pattern.inputs.add(null);
            }
        }
        for(IAEItemStack outp : details.getOutputs()) {
            if(outp!=null) {
                pattern.outputs.add(new McItem(
                        outp.getItem().getUnlocalizedName(),
                        Item.REGISTRY.getNameForObject(outp.getItem()).toString(),
                        outp.getItemDamage(),
                        (int)outp.getStackSize(),
                        null));
            } else {
                pattern.outputs.add(null);
            }
        }

        return pattern.toNBT();
    }

    private int pass=0;
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if(event.side== Side.SERVER && event.phase== TickEvent.Phase.START) {
            if(++pass%20==0) {
                doJobs(event);
            }
        }
    }

    private void doJobs(TickEvent.ServerTickEvent event) {
        if(AeTools.instance.clearJob!=null) {
            boolean done=AeTools.instance.clearJob.doWork();
            if(done)
                AeTools.instance.clearJob=null;
        }
        if(AeTools.instance.registryExportJob!=null) {
            boolean done=AeTools.instance.registryExportJob.doWork();
            if(done)
                AeTools.instance.registryExportJob=null;
        }
    }

}
