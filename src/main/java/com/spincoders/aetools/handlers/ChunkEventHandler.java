package com.spincoders.aetools.handlers;

import com.spincoders.aetools.AeTools;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;

import java.util.HashMap;

public class ChunkEventHandler {

    private HashMap<ChunkPos,Chunk> loadedChunks=new HashMap<ChunkPos, Chunk>();

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        Chunk chunk=event.getChunk();
        ChunkPos pos=chunk.getChunkCoordIntPair();
        AeTools.logInfo("("+loadedChunks.size()+") Chunk ["+chunk.xPosition+","+chunk.zPosition+"] loaded. Chunkpos: ["+pos.getXStart()+" ~ "+pos.getXEnd()+", "+pos.getZStart()+" ~ "+pos.getZEnd()+"]");

        if(!loadedChunks.containsKey(pos))
            loadedChunks.put(pos, chunk);
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        Chunk chunk=event.getChunk();
        ChunkPos pos=chunk.getChunkCoordIntPair();

        if(loadedChunks.containsKey(pos))
            loadedChunks.remove(pos);

        AeTools.logInfo("("+loadedChunks.size()+") Chunk ["+chunk.xPosition+","+chunk.zPosition+"] unloaded. Chunkpos: ["+pos.getXStart()+" ~ "+pos.getXEnd()+", "+pos.getZStart()+" ~ "+pos.getZEnd()+"]");
    }
}
