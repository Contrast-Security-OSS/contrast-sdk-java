package com.contrastsecurity.utils;


import com.contrastsecurity.models.Application;
import com.contrastsecurity.models.BaseResponse;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiDeserializer implements JsonDeserializer<List<BaseResponse>> {

    private static Map<String, Class> classMap = new HashMap<String, Class>();

    static {
        classMap.put("BaseResponse", BaseResponse.class);
        classMap.put("Application", Application.class);
    }

    public List<BaseResponse> deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context) throws JsonParseException {

        List list = new ArrayList<BaseResponse>();
        JsonArray ja = json.getAsJsonArray();

        for (JsonElement je : ja) {

            String type = je.getAsJsonObject().get("clazz").getAsString();
            Class c = classMap.get(type);
            if (c == null)
                throw new RuntimeException("Unknown class: " + type);
            list.add(context.deserialize(je, c));
        }

        return list;

    }

}