package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;

public class TagPropFloat extends TagProp {

    private float value;

    public TagPropFloat() {
        type="float";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(Float)value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagFloat(value);
    }

}
