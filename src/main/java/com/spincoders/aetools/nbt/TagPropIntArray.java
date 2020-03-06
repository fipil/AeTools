package com.spincoders.aetools.nbt;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

public class TagPropIntArray extends TagProp {

    private int[] value;

    public TagPropIntArray() {
        type="intArray";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(int[])value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagIntArray(value);
    }

}
