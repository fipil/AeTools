package com.spincoders.aetools.exportblocks;

import appeng.api.AEApi;
import appeng.api.storage.data.IAETagCompound;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spincoders.aetools.AeTools;
import com.spincoders.aetools.Utils;
import com.spincoders.aetools.jobs.IJobWork;
import com.spincoders.aetools.nbt.NbtTag;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportModBlocksWork implements IJobWork {

    private ModBlocks modBlocks=new ModBlocks();
    private ICommandSender sender;
    private String fileName;

    public ExportModBlocksWork(ICommandSender sender, String fileName) {
        this.sender=sender;
        this.fileName=fileName;
    }

    @Override
    public void doWork(World world, int x, int z) {
        for(int y=0;y<world.getHeight();y++) {
            findBlock(world, new BlockPos(x, y, z));
        }
    }

    private void findBlock(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block=state.getBlock();
        TileEntity te=world.getTileEntity(pos);

        ResourceLocation resLoc=Block.REGISTRY.getNameForObject(block);

        if(!resLoc.getResourceDomain().equals("minecraft")) {
            int id=modBlocks.register(resLoc.toString(), block.getLocalizedName());
            BlockEntry entry=new BlockEntry();
            entry.id=id;
            entry.x=pos.getX();
            entry.y=pos.getY();
            entry.z=pos.getZ();
            if(te!=null) {
                NBTTagCompound tag=te.serializeNBT();
                entry.data=te.getBlockMetadata();
                if(tag!=null) {
                    entry.nbt=new NbtTag(tag);
                }
            }
            modBlocks.blocks.add(entry);
        }
    }

    @Override
    public void finish(World world) {
        String filePath=!fileName.toLowerCase().endsWith(".json")?fileName+".json":fileName;

        File file=new File(filePath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            Gson gson=new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(modBlocks));
            writer.close();
            sender.addChatMessage(new TextComponentString(Utils.stripAccents("Modovane bloky byly ulozeny do souboru: "+file.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String workName() {
        return "Registry export";
    }
}
