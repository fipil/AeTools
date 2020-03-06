package com.spincoders.aetools.nbt;

import com.google.gson.*;

import javax.swing.text.html.HTML;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TagPropDeserializer implements JsonDeserializer<TagProp> {
    private String tagPropTypeElementName;
    private Gson gson=null;
    private Map<String, Class<? extends TagProp>> tagPropTypeRegistry;

    public TagPropDeserializer(String tagPropTypeElementName) {
        this.tagPropTypeElementName = tagPropTypeElementName;
        this.tagPropTypeRegistry = new HashMap<String, Class<? extends TagProp>>();
    }

    public void registerBarnType(String animalTypeName, Class<? extends TagProp> animalType) {
        tagPropTypeRegistry.put(animalTypeName, animalType);
    }

    public TagProp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject animalObject = json.getAsJsonObject();
        JsonElement animalTypeElement = animalObject.get(tagPropTypeElementName);

        Class<? extends TagProp> animalType = tagPropTypeRegistry.get(animalTypeElement.getAsString());

        return getGson().fromJson(animalObject, animalType);
    }

    private Gson getGson() {
        if(gson==null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(TagProp.class, TagPropDeserializer.create())
                    .create();
        }
        return gson;
    }

    public static TagPropDeserializer create() {
        TagPropDeserializer deserializer=new TagPropDeserializer("type");
        deserializer.registerBarnType("nbt", NbtTag.class);
        deserializer.registerBarnType("byte", TagPropByte.class);
        deserializer.registerBarnType("byteArray", TagPropByteArray.class);
        deserializer.registerBarnType("double", TagPropDouble.class);
        deserializer.registerBarnType("float", TagPropFloat.class);
        deserializer.registerBarnType("int", TagPropInt.class);
        deserializer.registerBarnType("intArray", TagPropIntArray.class);
        deserializer.registerBarnType("list", TagPropList.class);
        deserializer.registerBarnType("long", TagPropLong.class);
        deserializer.registerBarnType("short", TagPropShort.class);
        deserializer.registerBarnType("string", TagPropString.class);
        return deserializer;
    }
}
