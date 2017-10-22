package com.example.macbook.smartparking;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.macbook.smartparking.container.GraphContainerFragment;
import com.example.macbook.smartparking.main.MainFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeScreenAdministrator extends AppCompatActivity implements OnGraphButtonListener {

    RelativeLayout fragmentContainer;
    FragmentManager fm;
    String tagInUse;
    MainFragment fragment;


    String [] fragmentsDate = {"", "", ""};
    Calendar [] mDateSelected = {null, null, null};;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_administrator);
        if(savedInstanceState==null) {
            fragmentContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new MainFragment();
            fragment.setmListener(this);
            ft.add(R.id.fragmentContainer, fragment, null);
            ft.commit();
        }else{
            fragmentsDate = savedInstanceState.getStringArray("dates");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putStringArray("dates", fragmentsDate);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    public void setDateFragment(String dateFragment, int position){
        fragmentsDate[position] = dateFragment;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateFragment);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mDateSelected[position] = calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDateFragment(int position){
        return fragmentsDate[position];
    }

    public Calendar getCalendarFragment(int position){
        return mDateSelected[position];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.getItem(0);
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mybutton:
                SharedUtils.getInstance().setUserId(this, 0);
                SharedUtils.getInstance().setUserType(this, 0);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickGraph() {
        GraphContainerFragment fragment = new GraphContainerFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));
        fragment.setExitTransition(new Slide(Gravity.LEFT));
        ft.add(R.id.fragmentContainer,fragment , null)
                .addToBackStack(GraphContainerFragment.TAG_GRAPH_CONTAINER);
        tagInUse = GraphContainerFragment.TAG_GRAPH_CONTAINER;
        ft.commit();
    }

}
