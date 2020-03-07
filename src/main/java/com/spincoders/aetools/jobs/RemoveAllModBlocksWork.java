package com.spincoders.aetools.jobs;

import com.spincoders.aetools.exportblocks.BlockEntry;
import com.spincoders.aetools.nbt.NbtTag;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RemoveAllModBlocksWork implements IJobWork {

    public void doWork(World world, int x, int z) {
        for (int y = 0; y < world.getHeight(); y++) {
            clearBlock(world, new BlockPos(x, y, z));
        }
    }

    private void clearBlock(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block=state.getBlock();

        ResourceLocation resLoc=Block.REGISTRY.getNameForObject(block);

        if(!resLoc.getResourceDomain().equals("minecraft")) {
            //world.setT(pos);
            Block newBlock = Block.REGISTRY.getObject(new ResourceLocation("minecraft:air"));
            world.setBlockState(pos, newBlock.getBlockState().getBaseState());
         }
    }

    @Override
    public void finish(World world) {

    }

    @Override
    public String workName() {
        return "Clear";
    }
}
