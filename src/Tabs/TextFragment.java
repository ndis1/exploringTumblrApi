package Tabs;

import ndis.tumblrapi.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class TextFragment extends BaseFragment {
 
    OnTextFragmentSelectedListener mCallback;
	public interface OnTextFragmentSelectedListener {
        public void onTextSelected(int position);
    }
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	        try {
	            mCallback = (OnTextFragmentSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnHeadlineSelectedListener");
	        }
	    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.frag_lay, container,false);
        mCallback.onTextSelected(position);
        setListAdapter(adapter);
        setRetainInstance(true);
        return v;
    }

}