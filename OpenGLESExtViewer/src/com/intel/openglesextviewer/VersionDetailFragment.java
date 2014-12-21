package com.intel.openglesextviewer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.intel.openglesextviewer.dummy.DummyContent;
import com.intel.openglesextviewer.dummy.TestGLSurfaceView;
import com.intel.openglesextviewer.dummy.DummyContent.DummyItem;
import com.intel.openglesextviewer.dummy.TestGLSurfaceView.ResultHandler;

/**
 * A fragment representing a single Version detail screen.
 * This fragment is either contained in a {@link VersionListActivity}
 * in two-pane mode (on tablets) or a {@link VersionDetailActivity}
 * on handsets.
 */
public class VersionDetailFragment extends Fragment implements ResultHandler {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private TextView mTextView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VersionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_version_detail, container, false);
        mTextView = ((TextView) rootView.findViewById(R.id.version_detail));
        mTextView.setText("Testing ...");
        //<
        TestGLSurfaceView.setResultHandler(this);
        //>        
        return rootView;
    }

    public void updateTextView()
    {
		String result = "OpenGLES " + mItem.majorVersion + "." + mItem.minorVersion + "\n";
		result += "Supported: " + mItem.supported + "\n";
		ArrayList<String> intelExts = new ArrayList<String>();
		String intelExtText = "";
		
		String allExtText = "";
		
		if (0 < mItem.extensions.length) {
			allExtText += String.format("\nAll extensions(%s):\n", mItem.extensions.length);
		}
		for (int i=0; i<mItem.extensions.length; ++i) {
			String ext = mItem.extensions[i].toString();
			allExtText += "" + ext + "\n";
			if (-1 < ext.toLowerCase().indexOf("intel")) {
				intelExts.add(ext);
			}
		}
		if (0 < intelExts.size()) {
			intelExtText += String.format("\nIntel extensions(%s):\n", intelExts.size());
			for (int i=0; i<intelExts.size(); ++i) {
				intelExtText += intelExts.get(i) + "\n";
			}
		}
		else {
			intelExtText += "\nNo Intel extension!\n";
		}
		if (mItem.supported) {
			result += intelExtText;
			result += allExtText;			
		}
		
		mTextView.setText(result);
    }
	@Override
	public void handleResult(TestGLSurfaceView testView) {
		// TODO Auto-generated method stub
		mItem.supported = testView.supported;
		mItem.extensions = testView.extensions.clone();
		mItem.intelExtensions = testView.intelExtensions.clone();
		
        getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
	        	updateTextView();				
			}				
        });		
	}
}
