
package com.ashutosh.slotbookingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Timings_Days {

    @SerializedName("afternoon")
    @Expose
    private List<Afternoon> afternoon = new ArrayList<Afternoon>();
    @SerializedName("evening")
    @Expose
    private List<Evening> evening = new ArrayList<Evening>();
    @SerializedName("morning")
    @Expose
    private List<Morning> morning = new ArrayList<Morning>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Timings_Days() {
    }

    /**
     * 
     * @param afternoon
     * @param morning
     * @param evening
     */
    public Timings_Days(List<Afternoon> afternoon, List<Evening> evening, List<Morning> morning) {
        this.afternoon = afternoon;
        this.evening = evening;
        this.morning = morning;
    }

    /**
     * 
     * @return
     *     The afternoon
     */
    public List<Afternoon> getAfternoon() {
        return afternoon;
    }

    /**
     * 
     * @param afternoon
     *     The afternoon
     */
    public void setAfternoon(List<Afternoon> afternoon) {
        this.afternoon = afternoon;
    }

    /**
     * 
     * @return
     *     The evening
     */
    public List<Evening> getEvening() {
        return evening;
    }

    /**
     * 
     * @param evening
     *     The evening
     */
    public void setEvening(List<Evening> evening) {
        this.evening = evening;
    }

    /**
     * 
     * @return
     *     The morning
     */
    public List<Morning> getMorning() {
        return morning;
    }

    /**
     * 
     * @param morning
     *     The morning
     */
    public void setMorning(List<Morning> morning) {
        this.morning = morning;
    }

}
