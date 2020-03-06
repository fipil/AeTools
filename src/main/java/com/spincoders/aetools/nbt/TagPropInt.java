package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

public class TagPropInt extends TagProp {

    private int value;

    public TagPropInt() {
        type="int";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(Integer)value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagInt(value);
    }

}
