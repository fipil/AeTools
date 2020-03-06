package com.spincoders.aetools.nbt;

import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;

public class NbtTag extends TagProp {

    public ArrayList<TagProp> props=new ArrayList<TagProp>();;

    public NbtTag() {
        type="nbt";
    };

    public NbtTag(NBTTagCompound tag) {
        super();
        load(tag);
    }

    public void load(NBTTagCompound tag) {
        for(String key:tag.getKeySet()) {
            Object base=tag.getTag(key);
            TagProp prop=getPropFromTag((NBTBase)base);
            if(prop!=null) {
                prop.key=key;
                props.add(prop);
            }
        }
    }

    private TagProp getPropFromTag(NBTBase tag) {
        if(tag instanceof NBTTagInt)
            return new TagPropInt().setValue(((NBTTagInt)tag).getInt());
        else if(tag instanceof NBTTagLong)
            return new TagPropLong().setValue(((NBTTagLong)tag).getLong());
        else if(tag instanceof NBTTagShort)
            return new TagPropShort().setValue(((NBTTagShort)tag).getShort());
        else if(tag instanceof NBTTagByte)
            return new TagPropByte().setValue(((NBTTagByte)tag).getByte());
        else if(tag instanceof NBTTagFloat)
            return new TagPropFloat().setValue(((NBTTagFloat)tag).getFloat());
        else if(tag instanceof NBTTagDouble)
            return new TagPropDouble().setValue(((NBTTagDouble)tag).getDouble());
        else if(tag instanceof NBTTagString)
            return new TagPropString().setValue(((NBTTagString)tag).getString());
        else if(tag instanceof NBTTagList) {
            NBTTagList list=(NBTTagList)tag;
            List<TagProp> propList=new ArrayList<TagProp>();
            for(int i=0;i<list.tagCount();i++) {
                propList.add(getPropFromTag(list.get(i)));
            }
            return new TagPropList().setValue(propList);
        }
        else if(tag instanceof NBTTagIntArray)
            return new TagPropIntArray().setValue(((NBTTagIntArray)tag).getIntArray());
        else if(tag instanceof NBTTagByteArray)
            return new TagPropByteArray().setValue(((NBTTagByteArray)tag).getByteArray());
        else if(tag instanceof NBTTagCompound)
            return new NbtTag().setValue(tag);

        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public TagProp setValue(Object value) {
        load((NBTTagCompound)value);
        return this;
    }

    @Override
    public NBTBase toNBTBase() {
        NBTTagCompound tag=new NBTTagCompound();
        for(TagProp prop:props) {
            tag.setTag(prop.key, prop.toNBTBase());
        }
        return tag;
    }
}
