package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

public class TagPropDouble extends TagProp {

    private double value;

    public TagPropDouble() {
        type="double";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(Double) value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagDouble(value);
    }
}
