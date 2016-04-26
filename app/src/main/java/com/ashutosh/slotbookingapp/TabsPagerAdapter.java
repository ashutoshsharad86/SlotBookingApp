package com.ashutosh.slotbookingapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.ashutosh.slotbookingapp.advrecyclerview.common.data.ExpandableDataProvider;
import com.ashutosh.slotbookingapp.advrecyclerview.expansionhelpers.ExpandableAdapter;
import com.ashutosh.slotbookingapp.advrecyclerview.expansionhelpers.ExpandableFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    private ArrayList<String> myData ;
    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;

    }

    public View getTabView(int position) {
        //System.out.println("GetTabView called");
        View v = LayoutInflater.from(mContext).inflate(R.layout.tab, null);
        return v;
    }

    private String getProperDateString(String dateInString) {
        StringBuilder result = new StringBuilder();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
            result.append(formatter.format(date));

        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return result.toString();
    }

    private void resetToolBarText(String month) {
        System.out.println("Month obtained = " + month);
        MainActivity activity = (MainActivity) mContext;
        activity.getMonthTextView().setText(month);
    }

    @Override
    public Fragment getItem(int position) {
        //return PageFragment.newInstance(position + 1);
        //System.out.println("GetItem called");
        if(myData != null && myData.size()>0) {

            Map<String, Timings_Days> lastTimingsItems = ((MainActivity)mContext).getLastTimingsItems();
            String monthString = myData.get(position);
            Timings_Days timings = lastTimingsItems.get(monthString);
            resetToolBarText(getProperDateString(monthString));

            // This can be shifted to fragments to retain instances
            ExpandableDataProvider provider = new ExpandableDataProvider(timings);
            ExpandableAdapter expandableAdapter = new ExpandableAdapter(provider);
            ExpandableFragment fragment= new ExpandableFragment();
            fragment.setmExpandableAdapter(expandableAdapter);
            return fragment;

        } else
        return PageFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        if(myData != null)
            return myData.size();
        else
            return 0;
    }

    public ArrayList<String> getMyData() {
        return myData;
    }

    public void setMyData(ArrayList<String> myData) {
        this.myData = myData;
    }
}