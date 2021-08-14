package co.aspirasoft.sams.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import co.aspirasoft.sams.R

class ActivityDashboard : AppCompatActivity() {

    //private String[] mMenuItems;
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mDrawerMain: LinearLayout
    private lateinit var mDrawerList: ListView
    private lateinit var mTitle: String
    private lateinit var mDrawerTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setSupportActionBar(findViewById<View>(R.id.actionBar) as Toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer)
        title = "ActivityDashboard"

        //mMenuItems = getResources().getStringArray(R.array.menu_items);
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerMain = findViewById(R.id.left_drawer)
        mDrawerList = findViewById(R.id.listView)
        mDrawerToggle = object : ActionBarDrawerToggle(
            this,  /* host Activity */
            mDrawerLayout,  /* DrawerLayout object */
            findViewById<View>(R.id.actionBar) as Toolbar,  /* nav bar */
            R.string.drawer_open,  /* "open drawer" description */
            R.string.drawer_close /* "close drawer" description */
        ) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                title = mTitle
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                title = mDrawerTitle
            }
        }

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

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
        mDrawerList.onItemClickListener = DrawerItemClickListener()
    }

    /**
     * Swaps views in the main content view
     */
    private fun selectItem(position: Int) {
        // Hide all views
        findViewById<View>(R.id.dashboardHome).visibility = View.INVISIBLE
        findViewById<View>(R.id.dashboardClasses).visibility = View.INVISIBLE
        findViewById<View>(R.id.dashboardAssignments).visibility = View.INVISIBLE
        findViewById<View>(R.id.dashboardPayments).visibility = View.INVISIBLE
        findViewById<View>(R.id.dashboardHostel).visibility = View.INVISIBLE
        findViewById<View>(R.id.dashboardSettings).visibility = View.INVISIBLE
        findViewById<View>(R.id.dashboardHelp).visibility = View.INVISIBLE
        when (position) {
            0 -> {
                findViewById<View>(R.id.dashboardHome).visibility = View.VISIBLE
                title = "ActivityDashboard"
            }
            1 -> findViewById<View>(R.id.dashboardClasses).visibility = View.VISIBLE
            2 -> findViewById<View>(R.id.dashboardAssignments).visibility = View.VISIBLE
            3 -> findViewById<View>(R.id.dashboardPayments).visibility = View.VISIBLE
            4 -> findViewById<View>(R.id.dashboardHostel).visibility = View.VISIBLE
            5 -> findViewById<View>(R.id.dashboardSettings).visibility = View.VISIBLE
            6 -> findViewById<View>(R.id.dashboardHelp).visibility = View.VISIBLE
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true)
        mDrawerLayout.closeDrawer(mDrawerMain)
    }

    override fun setTitle(title: CharSequence) {
        mDrawerTitle = title.toString()
        mTitle = mDrawerTitle
        supportActionBar?.title = title
    }

    private inner class DrawerItemClickListener : OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            selectItem(position)
        }
    }
}