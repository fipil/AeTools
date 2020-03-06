package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

public class TagPropList extends TagProp {

    private List<TagProp> value;

    public TagPropList() {
        type="list";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(List<TagProp>)value;
        return this;
    }

    List<TagProp> getList() {
        return value;
    }

    @Override
    public NBTBase toNBTBase() {
        NBTTagList tagList=new NBTTagList();
        if(value!=null) {
            for(TagProp item:value) {
                tagList.appendTag(item.toNBTBase());
            }
        }
        return tagList;
    }

}
