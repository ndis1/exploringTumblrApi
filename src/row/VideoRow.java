package row;

import ndis.tumblrapi.R;
import ImgLoader.ImageLoader;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
public class VideoRow implements Row {
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private String embed;
    private String data;

    public VideoRow(LayoutInflater inflater, String data, String embed,ImageLoader imageLoader) {
        this.inflater = inflater;
        this.imageLoader = imageLoader;
        this.embed = embed;
        this.data = data;
    }

    public View getView(View convertView) {
        ViewHolder holder;
        View view;
        if (convertView == null) {
            View vi = (View)inflater.inflate(R.layout.video_item, null);
            holder = new ViewHolder((ImageView)vi.findViewById(R.id.image));
	        imageLoader.DisplayImage(data, holder.image);
            vi.setTag(holder);
            view = vi;
        } else {
            view = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        imageLoader.DisplayImage(data, holder.image);
        return view;
    }

    public int getViewType() {
        return RowType.VIDEO_ROW.ordinal();
    }
    public String getEmbed(){
    	return embed;
    }

    private static class ViewHolder {
        final ImageView image;

        private ViewHolder(ImageView image) {
            this.image = image;
        }
    }
}