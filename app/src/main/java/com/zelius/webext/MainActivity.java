package com.zelius.webext;

/**
 * Created by Luiz F. Lazzarin on 23/03/2015.
 * Email: lf.lazzarin@gmail.com
 * Github: /luizfelippe
 *
 * Feel free to use it.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zelius.webext.adapter.SectionsMenuAdapter;
import com.zelius.webext.data.DataWebsite;
import com.zelius.webext.model.SectionModel;

public class MainActivity extends Activity {

    DrawerLayout mDrawerLayoutView;
    LinearLayout mDrawerLinearLayoutView;
    ListView mDrawerListView;

    ActionBarDrawerToggle mActionBarDrawerToggle;
    ActionBar mActionBar;

    CharSequence mDrawerTitle;
    CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MyTheme);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLinearLayoutView = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerListView = (ListView) findViewById(R.id.list_view);
        mActionBar = getActionBar();

        setupActionBar();
        setupDrawer(savedInstanceState);
    }

    // region Action Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupActionBar() {
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_main, null, false);

        mActionBar.setCustomView(viewActionBar);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayoutView.isDrawerOpen(mDrawerListView);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    // endregion

    // region Drawer

    private void setupDrawer(Bundle savedInstance) {
        mDrawerLayoutView.setDrawerShadow(R.drawable.patch_shadow_drawer, GravityCompat.START);
        mDrawerListView.setAdapter(new SectionsMenuAdapter(this, R.layout.item_main_menu, DataWebsite.dataSectionLinks));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayoutView,         /* DrawerLayout object */
                R.drawable.icon_drawer_list,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                mActionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayoutView.setDrawerListener(mActionBarDrawerToggle);

        if (savedInstance == null) {
            selectItem(0);
        }
    }

    private void selectItem(int position) {
        SectionModel section = (SectionModel) mDrawerListView.getItemAtPosition(position);

        Bundle args = new Bundle();
        args.putString(MainFragment.EXTRA_SECTION_URL, section.getUrl());
        args.putString(MainFragment.EXTRA_SECTION_TITLE, section.getLabel());

        Fragment fragment = new MainFragment();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerListView.setItemChecked(position, true);
        mDrawerLayoutView.closeDrawer(mDrawerLinearLayoutView);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    // endregion

    // region Overrides

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mActionBar.setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    // endregion
}

