package com.ashutosh.slotbookingapp;

import java.util.HashMap;
import java.util.Map;

public class SlotTimingItems {

    Map<String, Timings_Days> slots_per_day;

    public Map<String, Timings_Days> getSlots_per_day() {
        return slots_per_day;
    }

    public void setSlots_per_day(Map<String, Timings_Days> slots_per_day) {
        this.slots_per_day = slots_per_day;
    }

    public SlotTimingItems() {
        this.slots_per_day = new HashMap<String, Timings_Days>();
    }
}