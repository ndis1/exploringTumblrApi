package Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

   final int PAGE_COUNT = 5;

   /** Constructor of the class */
   public MyFragmentPagerAdapter(FragmentManager fm) {
       super(fm);
   }

   /** This method will be invoked when a page is requested to create */
   @Override
   public Fragment getItem(int arg0) {
	   Fragment myFragment = null;
	   Bundle data = new Bundle();
       data.putInt("current_page", arg0);
	   switch(arg0){
		   case 0 : {
			   myFragment = new AllFragment();
			   break;
		   }
		   case 1 :{
		       myFragment = new TextFragment();
		       break;
		   }
		   case 2:{
		       myFragment = new ImageFragment();
		       break;
		   }case 3 :{
			   myFragment = new LikeFragment();
			   break;
		   }default :{
			   myFragment = new NoteFragment();
		   }
	   }
       myFragment.setArguments(data);

       return myFragment;
   }

   
   
   /** Returns the number of pages */
   @Override
   public int getCount() {
       return PAGE_COUNT;
   }
   @Override
   public Object instantiateItem(ViewGroup container, int position) {
       Fragment fragment = (Fragment) super.instantiateItem(container, position);
       registeredFragments.put(position, fragment);
       return fragment;
   }

   @Override
   public void destroyItem(ViewGroup container, int position, Object object) {
       registeredFragments.remove(position);
       super.destroyItem(container, position, object);
   }

   public Fragment getRegisteredFragment(int position) {
       return registeredFragments.get(position);
   }
}