package com.intel.openglesextviewer;

import com.intel.openglesextviewer.dummy.DummyContent;
import com.intel.openglesextviewer.dummy.DummyContent.DummyItem;
import com.intel.openglesextviewer.dummy.TestGLSurfaceView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;




/**
 * An activity representing a list of Versions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VersionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link VersionListFragment} and the item details
 * (if present) is a {@link VersionDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link VersionListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class VersionListActivity extends FragmentActivity
        implements VersionListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_list);

        if (findViewById(R.id.version_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((VersionListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.version_list))
                    .setActivateOnItemClick(true);
        }    
        //<
        // refer to: http://stackoverflow.com/questions/9198293/is-there-a-way-to-check-if-android-device-supports-opengl-es-2-0
        String MAX_KEY = "0";
        if (!DummyContent.ITEM_MAP.containsKey(MAX_KEY)) {
            final ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            final int glesFullVersion = activityManager.getDeviceConfigurationInfo().reqGlEsVersion;
            final int glesMajorVersion = (glesFullVersion & 0xFFFF0000) >> 16;
            final int glesMinorVersion = glesFullVersion & 0x0000FFFF;
            Log.i("TEST", String.format("reqGLESVersion: %d.%d", glesMajorVersion, glesMinorVersion));
            DummyContent.addItem(new DummyItem(MAX_KEY, String.format("OpenGLES Max: %x.%x", glesMajorVersion, glesMinorVersion), glesMajorVersion, glesMinorVersion));        	
        }
        //>
    }

    /**
     * Callback method from {@link VersionListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        //<
        DummyItem item = DummyContent.ITEM_MAP.get(id);
        TestGLSurfaceView.setTestParams(item.majorVersion, item.minorVersion);
        //>
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(VersionDetailFragment.ARG_ITEM_ID, id);
            VersionDetailFragment fragment = new VersionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.version_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, VersionDetailActivity.class);
            detailIntent.putExtra(VersionDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
