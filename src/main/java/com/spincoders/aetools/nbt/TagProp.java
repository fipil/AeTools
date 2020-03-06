package com.spincoders.aetools.nbt;

import net.minecraft.nbt.NBTBase;

public abstract class TagProp {
    public String key;
    public String type;

    public abstract Object getValue();
    public abstract TagProp setValue(Object value);

    public abstract NBTBase toNBTBase();
}
