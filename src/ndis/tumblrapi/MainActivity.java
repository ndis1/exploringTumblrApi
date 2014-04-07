package ndis.tumblrapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import row.ImageRow;
import row.LazyAdapterAssignsImageLayoutorTextLayoutBasedType;
import row.Row;
import row.TextRow;
import row.VideoRow;
import ImgLoader.ImageLoader;
import Tabs.AllFragment;
import Tabs.AllFragment.OnAllSelectedListener;
import Tabs.BaseFragment.EndlessScroller;
import Tabs.ImageFragment;
import Tabs.ImageFragment.OnImageFragmentSelectedListener;
import Tabs.MyFragmentPagerAdapter;
import Tabs.TextFragment;
import Tabs.TextFragment.OnTextFragmentSelectedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.PhotoSize;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.Video;
import com.tumblr.jumblr.types.VideoPost;

public class MainActivity extends SherlockFragmentActivity implements OnImageFragmentSelectedListener,OnTextFragmentSelectedListener,OnAllSelectedListener,EndlessScroller {
	private boolean adapterTaskSet;
    private static final String TAG = "TumblrDemo";
    private  String[] labels = {"All", "Video", "Images"};
    private ViewPager pager = null;
    private static LazyAdapterAssignsImageLayoutorTextLayoutBasedType allAdapter;
    private int offset =0;
    private static LazyAdapterAssignsImageLayoutorTextLayoutBasedType textAdapter;
    private static LazyAdapterAssignsImageLayoutorTextLayoutBasedType imageAdapter;
    private MyFragmentPagerAdapter pagerAdapter;
    private String query;
    private Activity con;
    private JumblrClient client;
	private SharedPreferences prefs;
    private ImageLoader imgLoader;
    private ArrayList<Row> rowsImg; 
    private ArrayList<Row> rowsAll;
    private ArrayList<Row> rowsText;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgLoader = new ImageLoader(this);
        adapterTaskSet = false;
        con = this;
		rowsAll = new ArrayList<Row>();
		rowsText = new ArrayList<Row>();
		rowsImg = new ArrayList<Row>();
		prefs = PreferenceManager.getDefaultSharedPreferences(con);
		String[] tokens = new SharedPreferencesCredentialStore(prefs).read();
		if(tokens != null && tokens[0] != ""){
				
		}else{
			Intent i = new Intent(this,OAuthAccessTokenActivity.class);
			startActivity(i);
		}

    }
    private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	setupTabs();
        }
    }   
    @Override
    protected void onResume(){
    	super.onResume();
		String[] tokens = new SharedPreferencesCredentialStore(prefs).read();
		if(tokens!= null && !tokens[0].equals("") &&allAdapter == null){
			Log.w(TAG,"TOKENS NOT NULL");
			new ApiCallExecutor().execute();
		}
    }
  
	private class ApiCallExecutor extends AsyncTask<Uri, Void, Void> {

		@Override
		protected Void doInBackground(Uri...params) {
			try {
				prefs = PreferenceManager.getDefaultSharedPreferences(con);
				String[] tokens = new SharedPreferencesCredentialStore(prefs).read();
				client = new JumblrClient(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
				client.setToken(tokens[0], tokens[1]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
            return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
		}
	}
	
	private class GetFromString extends AsyncTask<Uri, Void, Void> {
		private String query;
		public GetFromString(String query){
			if(query.contains(".com")||query.contains(".org")||query.contains(".net")){
				this.query = query;
			}else{
				this.query = query + ".tumblr.com";
			}
		}
		@Override
		protected Void doInBackground(Uri...params) {
			try{
				if(client == null){
					prefs = PreferenceManager.getDefaultSharedPreferences(con);
					String[] tokens = new SharedPreferencesCredentialStore(prefs).read();
					client = new JumblrClient(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
					client.setToken(tokens[0], tokens[1]);
				}
				Blog blog = client.blogInfo(query);
				Map<String, String> options = new HashMap<String, String>();
				options.put("offset", offset+"");
				options.put("limit", 10+"");
				LayoutInflater li = LayoutInflater.from(con);
				List<Post> posts = blog.posts(options);
				for (Post p : posts){
					if(p != null){
						if(p.getType().equals("photo")){
							Log.w(TAG, "some photo");
							Log.w(TAG, p.getPostUrl());
							PhotoPost po = (PhotoPost)p;
							List<Photo> photos = po.getPhotos();
							for(Photo photo : photos){
								List<PhotoSize> photosizes = photo.getSizes();
								if(photosizes.size()>0){
									PhotoSize smallestPhoto = null;
									int smallestHeight = 1000;
									for (PhotoSize pz : photosizes){
										if(pz.getHeight()<smallestHeight){
											smallestPhoto = pz;
											smallestHeight = pz.getHeight();
										}
									}
									ImageRow newListElement = new ImageRow(li,smallestPhoto.getUrl(),imgLoader);
		                            rowsAll.add(newListElement);
		                            rowsImg.add(newListElement);
								}
							}
						}
						else if(p.getType().equals("video")){
							VideoPost vp = (VideoPost)p;
							List<Video> videos = vp.getVideos();
							if(videos.size() > 0){
								Video smallestVideo = null;
								int smallestVid = 100000000;
								for(Video v : videos){
									if(v.getWidth() < smallestVid){
										smallestVid = v.getWidth();
										smallestVideo = v;
									}
								}
								VideoRow newListElement = new VideoRow(li,vp.getThumbnailUrl(),smallestVideo.getEmbedCode(),imgLoader);
		                        rowsAll.add(newListElement);		
		                        rowsText.add(newListElement);
							}
	                    }else{
							TextRow newListElement = new TextRow(li,p.getType());
	                        rowsAll.add(newListElement);		
	                    }
					}
				}
	            if(!adapterTaskSet){
    	            allAdapter = new LazyAdapterAssignsImageLayoutorTextLayoutBasedType(MainActivity.this, rowsAll,imgLoader);
    	            textAdapter = new LazyAdapterAssignsImageLayoutorTextLayoutBasedType(MainActivity.this, rowsText,imgLoader);
    	            imageAdapter = new LazyAdapterAssignsImageLayoutorTextLayoutBasedType(MainActivity.this, rowsImg,imgLoader);
	    			new SetAdapterTask().execute();
	    			adapterTaskSet = true;
	    		}else{
	    			runOnUiThread(new Runnable() {
	    			     @Override
	    			     public void run() {
	    		    			allAdapter.notifyDataSetChanged();
	    		    			textAdapter.notifyDataSetChanged();
	    		    			imageAdapter.notifyDataSetChanged();
	    			    }
	    			});
	    		}
	            return null;
			}catch(Exception e){
				runOnUiThread(new Runnable() {
   			     @Override
   			     public void run() {
   		    			malformedQueryDialog();
   			     }
				});
			}
			return null;
		}
	}
	
	private void setupTabs(){
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
        FragmentManager fm = getSupportFragmentManager();
        pagerAdapter = new MyFragmentPagerAdapter(fm);
        pager.setAdapter(pagerAdapter);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        TabListener tabListener = new TabListener() {

			@Override
			public void onTabReselected(Tab arg0,
					FragmentTransaction arg1) {    				
			}

			@Override
			public void onTabSelected(Tab tab,
					FragmentTransaction arg1) {
	            pager.setCurrentItem(tab.getPosition());

			}

			@Override
			public void onTabUnselected(Tab arg0,
					FragmentTransaction arg1) {    				
			}
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 3; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(labels[i])
                            .setTabListener(tabListener));
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            queryEntryDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	public void queryEntryDialog(){
		AlertDialog.Builder editalert = new AlertDialog.Builder(this);
		editalert.setTitle("Load tumblr");
		editalert.setMessage("Enter a user or tumblr url");
		final EditText input = new EditText(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		        LinearLayout.LayoutParams.MATCH_PARENT,
		        LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		editalert.setView(input);

		editalert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	query = input.getText().toString();
		    	imgLoader.clearCache();
		    	rowsAll.clear();
		    	rowsImg.clear();
		    	rowsText.clear();
				offset = 0;
				new GetFromString(query).execute();
		    }
		});
		editalert.show();
    }
	public void malformedQueryDialog(){
		AlertDialog.Builder editalert = new AlertDialog.Builder(this);
		editalert.setTitle("Malformed query");
		editalert.setMessage("please enter a valid tumblr address or username");
		final EditText input = new EditText(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		        LinearLayout.LayoutParams.MATCH_PARENT,
		        LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		editalert.setView(input);

		editalert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	query = input.getText().toString();
		    	imgLoader.clearCache();
		    	rowsAll.clear();
		    	rowsImg.clear();
		    	rowsText.clear();
				offset = 0;
				new GetFromString(query).execute();
		    }
		});
		editalert.show();
    }

	@Override
	public void onArticleSelected(int position) {
		if(pagerAdapter != null){
		 ImageFragment articleFrag = (ImageFragment) pagerAdapter.getRegisteredFragment(position);
			if(articleFrag != null){
				articleFrag.setAd(imageAdapter);
			}
		}
	}
	
	@Override
	public void onTextSelected(int position) {
		if(pagerAdapter != null){
		 TextFragment articleFrag = (TextFragment) pagerAdapter.getRegisteredFragment(position);
			if(articleFrag != null){
				articleFrag.setAd(textAdapter);
			}
		}
	}

	@Override
	public void onAllSelected(int position) {
		if(pagerAdapter != null){
			AllFragment articleFrag = (AllFragment) pagerAdapter.getRegisteredFragment(position);
			if(articleFrag != null){
				articleFrag.setAd(allAdapter);
			}
		}
	}
	
	@Override
	public void onBottomReached(int position) {
		Log.w(TAG, "onbottomreached called");
		offset += 10;
		new GetFromString(query).execute();
	}
}
