package com.spincoders.aetools.exportblocks;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class ModBlocks {
    public HashMap<String, RegEntry> registry=new HashMap<String, RegEntry>();
    public ArrayList<BlockEntry> blocks=new ArrayList<BlockEntry>();

    public int register(String regName, String displayName) {
        if(!registry.containsKey(regName)) {
            RegEntry entry=new RegEntry();
            entry.id=registry.size()+1;
            entry.regName=regName;
            entry.displayName=displayName;
            registry.put(regName, entry);
        }
        return registry.get(regName).id;
    }
}
