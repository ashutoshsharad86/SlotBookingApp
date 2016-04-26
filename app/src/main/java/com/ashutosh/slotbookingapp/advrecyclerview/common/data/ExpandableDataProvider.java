/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ashutosh.slotbookingapp.advrecyclerview.common.data;


import android.support.v4.util.Pair;

import com.ashutosh.slotbookingapp.Afternoon;
import com.ashutosh.slotbookingapp.Evening;
import com.ashutosh.slotbookingapp.Morning;
import com.ashutosh.slotbookingapp.Timings_Days;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExpandableDataProvider extends AbstractExpandableDataProvider {
    private List<Pair<GroupData, List<ChildData>>> mData;

    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    private String getFormattedDate(String dateInString) {
        String str = "null";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat formatter = new SimpleDateFormat("KK:mm a");
            str = formatter.format(date);

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return str;
    }

    public ExpandableDataProvider(Timings_Days timings_days) {
        final String[] groupItems = {"Morning", "Afternoon","Evening"};
        String[] childItems = null;

        mData = new LinkedList<>();

        for (int i = 0; i < groupItems.length; i++) {

            final long groupId = i;
            final String groupText = groupItems[i];
            int totalAvailableSlots = 0;
            int j = 0 ;

            if(i == 0) {
                List<Morning> morningList =  timings_days.getMorning();
                childItems = new String[morningList.size()];

                for(Morning morn : morningList) {
                    StringBuilder str = new StringBuilder(getFormattedDate(morn.getStartTime()));
                    str.append(" - "+getFormattedDate(morn.getEndTime()));
                    if(morn.isIsBooked() == false) {
                        totalAvailableSlots++;
                    } else {
                        str.append(" ");
                    }

                    childItems[j++] = str.toString() ;
                }

            }
            else if(i == 1) {
                List<Afternoon> afternoonList =  timings_days.getAfternoon();
                childItems = new String[afternoonList.size()];

                for(Afternoon an : afternoonList) {
                    StringBuilder str = new StringBuilder(getFormattedDate(an.getStartTime()));
                    str.append(" - "+ getFormattedDate(an.getEndTime()));
                    if(an.isIsBooked() == false) {
                        totalAvailableSlots++;
                    } else {
                        str.append(" ");
                    }

                    childItems[j++] = str.toString() ;
                }
            } else {
                List<Evening> eveningList =  timings_days.getEvening();
                childItems = new String[eveningList.size()];

                for(Evening el : eveningList) {
                    StringBuilder str = new StringBuilder(getFormattedDate(el.getStartTime()));
                    str.append(" - "+ getFormattedDate(el.getEndTime()));
                    if(el.isIsBooked() == false) {
                        totalAvailableSlots++;
                    } else {
                        str.append(" ");
                    }

                    childItems[j++] = str.toString() ;
                }
            }

            final ConcreteGroupData group = new ConcreteGroupData(groupId, groupText, new String(totalAvailableSlots+" slots available"));
            final List<ChildData> children = new ArrayList<>();

            for (j = 0; j < childItems.length; j++) {
                final long childId = group.generateNewChildId();
                final String childText = childItems[j];
                children.add(new ConcreteChildData(childId, childText));
            }

            mData.add(new Pair<GroupData, List<ChildData>>(group, children));
        }
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<GroupData, List<ChildData>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final Pair<GroupData, List<ChildData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(toGroupPosition);

        final ConcreteChildData item = (ConcreteChildData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
            item.setChildId(newId);
        }

        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).second.remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).first.getGroupId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }


    @Override
    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        Pair<GroupData, List<ChildData>> group = null;
        int groupPosition = -1;

        // find the group
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).first.getGroupId() == mLastRemovedChildParentGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.second.size()) {
            insertedPosition = mLastRemovedChildPosition;
        } else {
            insertedPosition = group.second.size();
        }

        group.second.add(insertedPosition, mLastRemovedChild);

        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
        mLastRemovedChild = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }

    public static final class ConcreteGroupData extends GroupData {

        private final long mId;
        private final String mText;
        private final String mSlotsAvail;
        private boolean mPinned;
        private long mNextChildId;

        ConcreteGroupData(long id, String text, String slotsAvail) {
            mId = id;
            mText = text;
            mSlotsAvail = slotsAvail;
            mNextChildId = 0;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public String getmSlotsAvail() {
            return mSlotsAvail;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteChildData extends ChildData {

        private long mId;
        private final String mText;
        private boolean mPinned;
        private boolean isAvailable;

        ConcreteChildData(long id, String text) {
            mId = id;
            mText = text;
            isAvailable = true;
            if(text.endsWith(" ")) {
                isAvailable = false;
            }
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public void setChildId(long id) {
            this.mId = id;
        }

        @Override
        public boolean isAvailable(){
            return isAvailable;
        }
    }
}
