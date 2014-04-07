package row;

import ndis.tumblrapi.R;
import ImgLoader.ImageLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class ImageRow implements Row {
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private String data;
    public ImageRow(LayoutInflater inflater, String data,ImageLoader imageLoader) {
        this.inflater = inflater;
        this.imageLoader = imageLoader;
        this.data = data;
    }

    public View getView(View convertView) {
        ViewHolder holder;
        View view;
        if (convertView == null) {
            View vi = (View)inflater.inflate(R.layout.image_item, null);
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
        return RowType.IMAGE_ROW.ordinal();
    }

    private static class ViewHolder {
        final ImageView image;

        private ViewHolder(ImageView image) {
            this.image = image;
        }
    }
}
