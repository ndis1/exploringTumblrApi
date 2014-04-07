package Tabs;

import ndis.tumblrapi.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class NoteFragment extends BaseFragment {
    OnNoteFragmentSelectedListener mCallback;
	public interface OnNoteFragmentSelectedListener {
        public void onNoteSelected(int position);
    }
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	        try {
	            mCallback = (OnNoteFragmentSelectedListener) activity;
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
        mCallback.onNoteSelected(position);
        setListAdapter(adapter);
        setRetainInstance(true);

        return v;
    }

	
 
}