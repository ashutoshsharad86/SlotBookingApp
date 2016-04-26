
package com.ashutosh.slotbookingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Morning {

    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("is_booked")
    @Expose
    private boolean isBooked;
    @SerializedName("is_expired")
    @Expose
    private boolean isExpired;
    @SerializedName("slot_id")
    @Expose
    private long slotId;
    @SerializedName("start_time")
    @Expose
    private String startTime;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Morning() {
    }

    /**
     * 
     * @param startTime
     * @param isExpired
     * @param slotId
     * @param isBooked
     * @param endTime
     */
    public Morning(String endTime, boolean isBooked, boolean isExpired, long slotId, String startTime) {
        this.endTime = endTime;
        this.isBooked = isBooked;
        this.isExpired = isExpired;
        this.slotId = slotId;
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 
     * @param endTime
     *     The end_time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     * @return
     *     The isBooked
     */
    public boolean isIsBooked() {
        return isBooked;
    }

    /**
     * 
     * @param isBooked
     *     The is_booked
     */
    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    /**
     * 
     * @return
     *     The isExpired
     */
    public boolean isIsExpired() {
        return isExpired;
    }

    /**
     * 
     * @param isExpired
     *     The is_expired
     */
    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    /**
     * 
     * @return
     *     The slotId
     */
    public long getSlotId() {
        return slotId;
    }

    /**
     * 
     * @param slotId
     *     The slot_id
     */
    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
