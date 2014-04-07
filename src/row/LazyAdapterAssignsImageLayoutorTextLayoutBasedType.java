package row;


import java.util.ArrayList;

import ImgLoader.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LazyAdapterAssignsImageLayoutorTextLayoutBasedType extends BaseAdapter {
    
    private Activity activity;
    private static LayoutInflater inflater=null;
    private ArrayList<Row> rows;
    public ImageLoader imageLoader; 
   
    public LazyAdapterAssignsImageLayoutorTextLayoutBasedType(Activity a, ArrayList<Row> rowData, ImageLoader iml) {
        rows = rowData;
        activity = a;
        imageLoader=iml;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).getViewType();
    }

    public int getCount() {
        return rows.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public Row getRow(int position){
    	return rows.get(position);
    }
    public boolean isVideo(int position){
    	Row v =rows.get(position);
    	if(v.getViewType() == RowType.VIDEO_ROW.ordinal()){
    		return true;
    	}
    	return false;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        return rows.get(position).getView(convertView);
    }
   
}