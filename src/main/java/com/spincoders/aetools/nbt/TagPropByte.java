package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

public class TagPropByte extends TagProp {

    private byte value;

    public TagPropByte() {
        type="byte";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(Byte)value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagByte(value);
    }
}
