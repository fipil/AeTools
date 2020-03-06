package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;

public class TagPropByteArray extends TagProp {

    private byte[] value;

    public TagPropByteArray() {
        type="byteArray";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(byte[])value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagByteArray(value);
    }

}
