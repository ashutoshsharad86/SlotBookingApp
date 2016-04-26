package com.ashutosh.slotbookingapp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SlotTimingsDesrializer implements JsonDeserializer<SlotTimingItems> {

    @Override
    public SlotTimingItems deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonObject slotObject = jsonObject.getAsJsonObject("slots");
        Map<String, Timings_Days> slots_per_day = new HashMap<String, Timings_Days>();
        for(Map.Entry<String, JsonElement> entry : slotObject.entrySet()) {
            Timings_Days timings_days = context.deserialize(entry.getValue(),Timings_Days.class);
            slots_per_day.put(entry.getKey(), timings_days);
        }

        SlotTimingItems items = new SlotTimingItems();
        items.setSlots_per_day(slots_per_day);
        return items;

    }
}