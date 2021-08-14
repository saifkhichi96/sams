package co.aspirasoft.sams;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ActivityDashboard extends AppCompatActivity {

    //private String[] mMenuItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawerMain;
    private ListView mDrawerList;
    private ActionBar actionBar;

    private String mTitle;
    private String mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setSupportActionBar((Toolbar) findViewById(R.id.actionBar));
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        setTitle("ActivityDashboard");

        //mMenuItems = getResources().getStringArray(R.array.menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerMain = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.listView);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                                   /* host Activity */
                mDrawerLayout,                          /* DrawerLayout object */
                (Toolbar) findViewById(R.id.actionBar), /* nav bar */
                R.string.drawer_open,                   /* "open drawer" description */
                R.string.drawer_close                   /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Set the adapter for the list view
        //final TypedArray typedArray = getResources().obtainTypedArray(R.array.menu_icons);
        //mDrawerList.setAdapter(new ArrayAdapter<String>(
        //        this,
        //        R.layout.drawer_items,
        //        mMenuItems
        //) {
            //@Override
            //public View getView(int position, View convertView, ViewGroup parent) {
                //View v = super.getView(position, convertView, parent);
                //int resourceId = typedArray.getResourceId(position, 0);
                //Drawable drawable = getResources().getDrawable(resourceId);
                //((TextView) v).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                //return v;
            //}
        //});

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * Swaps views in the main content view
     */
    private void selectItem(int position) {
        // Hide all views
        findViewById(R.id.dashboardHome).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboardClasses).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboardAssignments).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboardPayments).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboardHostel).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboardSettings).setVisibility(View.INVISIBLE);
        findViewById(R.id.dashboardHelp).setVisibility(View.INVISIBLE);

        // Show appropriate view and update page title
        switch (position) {
            case 0:
                findViewById(R.id.dashboardHome).setVisibility(View.VISIBLE);
                setTitle("ActivityDashboard");
                break;
            case 1:
                findViewById(R.id.dashboardClasses).setVisibility(View.VISIBLE);
                //setTitle(mMenuItems[1]);
                break;
            case 2:
                findViewById(R.id.dashboardAssignments).setVisibility(View.VISIBLE);
                //setTitle(mMenuItems[2]);
                break;
            case 3:
                findViewById(R.id.dashboardPayments).setVisibility(View.VISIBLE);
                //setTitle(mMenuItems[3]);
                break;
            case 4:
                findViewById(R.id.dashboardHostel).setVisibility(View.VISIBLE);
                //setTitle(mMenuItems[4]);
                break;
            case 5:
                findViewById(R.id.dashboardSettings).setVisibility(View.VISIBLE);
                //setTitle(mMenuItems[5]);
                break;
            case 6:
                findViewById(R.id.dashboardHelp).setVisibility(View.VISIBLE);
                //setTitle(mMenuItems[6]);
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerMain);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = mDrawerTitle = title.toString();
        actionBar.setTitle(title);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}