package com.spincoders.aetools;

public class McItem {
    public String name;
    public String regName;
    public int damage;
    public long count;
    public String tagCompound;

    public McItem(String name, String regName, int damage, long count, String tagCompound) {
        this.name=name;
        this.regName=regName;
        this.damage=damage;
        this.count=count;
        this.tagCompound=tagCompound;
    }

    @Override
    public String toString() {
        if(tagCompound!=null)
            return String.format("%s/%d/%s", regName, damage, tagCompound);
        else
            return String.format("%s/%d", regName, damage);
    }

    public String asCsv() {
        return String.format("%s;%s", name, toString());
    }
}
