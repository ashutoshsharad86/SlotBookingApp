package com.ashutosh.slotbookingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<SlotTimingItems> {
    public ProgressDialog progress_horizontal;

    TabsPagerAdapter adapter;
    ViewPager pager;
    private Map<String, Timings_Days> lastTimingsItems;

    private TextView monthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        monthTextView = (TextView) findViewById(R.id.month);

        adapter = new TabsPagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        setupTabLayout(pager, adapter);

        callForSlotsNow();
        progress_horizontal = new ProgressDialog(MainActivity.this);
        progress_horizontal.setMessage("Getting all slots, please wait ...");
        progress_horizontal.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress_horizontal.setIndeterminate(true);
        progress_horizontal.setCancelable(false);
        progress_horizontal.show();

        lastTimingsItems = null;

    }


    public TextView getMonthTextView() {
        return monthTextView;
    }


    private void setupTabLayout(ViewPager viewPager, TabsPagerAdapter viewPagerAdapter) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        int length = tabLayout.getTabCount();
        for (int i = 0; i < length; i++) {
            tabLayout.getTabAt(i).setCustomView(viewPagerAdapter.getTabView(i));

        }
    }

    private String getProperDateString(String dateInString) {
        StringBuilder result = new StringBuilder();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date date = sdf.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat formatter = new SimpleDateFormat("EE");
            result.append(day+"\n"+formatter.format(date));

        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return result.toString();
    }

    private void refreshTabLayoutWithData(ViewPager viewPager, TabsPagerAdapter viewPagerAdapter, ArrayList<String> list) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        int length = tabLayout.getTabCount();
        for (int i = 0; i < length; i++) {
            View newView = viewPagerAdapter.getTabView(i);
            TextView tv = (TextView) newView.findViewById(R.id.datetextView);
            String formattedDate = getProperDateString(list.get(i));
            tv.setText(formattedDate);
            tabLayout.getTabAt(i).setCustomView(newView);

        }
    }


    public void callForSlotsNow() {

        //System.out.println("Request fired");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://108.healthifyme.com")
                .addConverterFactory(buildGsonConverter())
                .build();


        // prepare call in Retrofit 2.0
        HealthifyMeInterface slotTimingsItemsGet = retrofit.create(HealthifyMeInterface.class);
        Call<SlotTimingItems> call = slotTimingsItemsGet.loadQuestions();
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<SlotTimingItems> call, Response<SlotTimingItems> response) {
        //System.out.println(response.body().slots_per_day.keySet());
        lastTimingsItems = response.body().slots_per_day;
        TreeSet<String> newSet = new TreeSet<String>();
        newSet.addAll(response.body().slots_per_day.keySet());
        System.out.println(newSet.toString());
        if (progress_horizontal.isShowing()) {
            progress_horizontal.dismiss();
            progress_horizontal.cancel();
        }

        ArrayList<String> myData = new ArrayList<>();
        myData.addAll(newSet);
        adapter.setMyData(myData);
        adapter.notifyDataSetChanged();
        refreshTabLayoutWithData(pager, adapter, myData);

    }

    @Override
    public void onFailure(Call<SlotTimingItems> call, Throwable t) {
        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SlotTimingItems.class, new SlotTimingsDesrializer());
        Gson myGson = gsonBuilder.create();
        return GsonConverterFactory.create(myGson);
    }


    public Map<String, Timings_Days> getLastTimingsItems() {
        return lastTimingsItems;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
