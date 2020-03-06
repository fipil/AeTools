package com.spincoders.aetools;

import java.util.ArrayList;

public class CraftingPattern {
    public boolean isCraftable;
    public boolean canSubstitute;
    public ArrayList<McItem> inputs;
    public ArrayList<McItem> outputs;

    public CraftingPattern() {
        inputs=new ArrayList<McItem>();
        outputs=new ArrayList<McItem>();
    }

    /*
    {Craft:0b,Cnt:1L,id:"appliedenergistics2:encoded_pattern",Count:1b,tag:
        {in:[{},{},{},{},{},{},{},{id:"minecraft:planks",Count:1b,Damage:0s},{id:"minecraft:planks",Count:1b,Damage:0s}],crafting:1b,substitute:0b,
                out:[{id:"minecraft:wooden_pressure_plate",Count:1b,Damage:0s}]},Damage:0s,Req:0L}
                */
    public String toNBT() {
        StringBuilder sb=new StringBuilder();
        sb.append("{");

        sb.append("in:[");
        String inp="";
        for(McItem item:inputs) {
            inp+=inp.length()>0?",":"";
            if(item!=null) {
                inp+="{id:\""+item.regName+"\",Count:"+item.count+"b,Damage:"+item.damage+"s}";
            } else {
                inp+="{}";
            }
        }
        sb.append(inp);
        sb.append("],crafting:"+(isCraftable?"1b":"0b"));
        sb.append(",substitute:"+(canSubstitute?"1b":"0b"));
        sb.append(",out:[");
        String outp="";
        for(McItem item:outputs) {
            outp+=outp.length()>0?",":"";
            if(item!=null) {
                outp+="{id:\""+item.regName+"\",Count:"+item.count+"b,Damage:"+item.damage+"s}";
            } else {
                outp+="{}";
            }
        }
        sb.append(outp);
        sb.append("]}");

        return sb.toString();
    }
}
