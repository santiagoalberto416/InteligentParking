package com.example.macbook.smartparking;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
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

import com.example.macbook.smartparking.graphContainer.GraphContainerFragment;
import com.example.macbook.smartparking.mainFragment.MainFragment;

public class HomeScreenAdministrator extends AppCompatActivity implements OnGraphButtonListener {

    RelativeLayout fragmentContainer;
    FragmentManager fm;
    String tagInUse;
    MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen_administrator);
        fragmentContainer = (RelativeLayout)findViewById(R.id.fragmentContainer);
        fm = getSupportFragmentManager() ;
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new MainFragment();
        fragment.setListener(this);
        ft.add(R.id.fragmentContainer,fragment , null);
        ft.commit();

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(tagInUse.equals(MainFragment.TAG_MAIN)){
                    tagInUse = GraphContainerFragment.TAG_GRAPH_CONTAINER;
                }else{
                    tagInUse = MainFragment.TAG_MAIN;
                }
            }
        });
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickGraph() {
        GraphContainerFragment fragment = new GraphContainerFragment();
        FragmentTransaction ft = fm.beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));
        fragment.setExitTransition(new Slide(Gravity.LEFT));
        ft.add(R.id.fragmentContainer,fragment , null)
                .addToBackStack(GraphContainerFragment.TAG_GRAPH_CONTAINER);
        tagInUse = GraphContainerFragment.TAG_GRAPH_CONTAINER;
        ft.commit();
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        if(outState!=null){
            getSupportFragmentManager().putFragment(outState, "myFragmentName", fragment);
        }
    }
}
