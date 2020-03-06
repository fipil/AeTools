package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public class TagPropString extends TagProp {

    String value;

    public TagPropString() {
        type="string";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(String)value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagString(value);
    }

}
