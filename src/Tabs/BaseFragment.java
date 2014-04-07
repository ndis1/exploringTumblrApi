package Tabs;

import ndis.tumblrapi.Constants;
import row.LazyAdapterAssignsImageLayoutorTextLayoutBasedType;
import row.VideoRow;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListFragment;
 
public class BaseFragment extends SherlockListFragment{
	public interface EndlessScroller {
        public void onBottomReached(int position);
    }

	EndlessScroller callback;
    LazyAdapterAssignsImageLayoutorTextLayoutBasedType adapter = null;
    int position;
    Activity act;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (EndlessScroller) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EndlessScroller");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        position = data.getInt("current_page", 0);
        act = this.getActivity();
        setRetainInstance(true);
    }
    @Override
    public void onActivityCreated (Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
        getListView().setOnScrollListener(new EndlessScrollListener());

    }
    public void positionList(int pos){
    	getListView().setSelectionFromTop(pos, 0);
    }
   
    public class EndlessScrollListener implements OnScrollListener {

        private int visibleThreshold = 10;
        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
           int visibleItemCount, int totalItemCount) {
       //leave this empty
        }

         @Override
         public void onScrollStateChanged(AbsListView view, int scrollState) {
        	 if (scrollState == SCROLL_STATE_IDLE) {
        		 if (getListView().getLastVisiblePosition() >= getListView().getCount() - visibleThreshold) {
        			 callback.onBottomReached(0);
        		 }
        	 }
         }
    }
    @Override
    public void onStart() {
        super.onStart();
        getListView().setOnItemClickListener(new OnItemClickListener() {
       	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	if(adapter.isVideo(position)){
            		Log.w(Constants.TAG, "IS VIDEO");
            		VideoRow r = (VideoRow)adapter.getRow(position);
            		Log.w(Constants.TAG, r.getEmbed());
            		String after[] = r.getEmbed().split("src=\"");
            		String end = after[1];
            		String exact[] = end.split("\" frame");
            		String url = exact[0];
            		String videoUrl = url;
            		Intent i = new Intent(Intent.ACTION_VIEW);
            		i.setData(Uri.parse(videoUrl));
            		startActivity(i);
            	}
            }
        });
    }
    public void setAd(LazyAdapterAssignsImageLayoutorTextLayoutBasedType adapter){
    	if(this.adapter==null)
    		   this.adapter = adapter;
    	else{
    		   this.adapter.notifyDataSetChanged();
    	}
    }
}