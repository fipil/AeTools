package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;

public class TagPropShort extends TagProp {

    private short value;

    public TagPropShort() {
        type="short";
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public TagProp setValue(Object value) {
        this.value=(Short) value;
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        return new NBTTagShort(value);
    }

}
