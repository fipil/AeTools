package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;

public class TagPropLong extends TagProp {

    private long value;

    public TagPropLong() {
        type="long";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(Long)value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagLong(value);
    }

}
