package row;

import ndis.tumblrapi.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class TextRow implements Row {
    private LayoutInflater inflater;
    private String data;
    public TextRow(LayoutInflater inflater, String data) {
        this.inflater = inflater;
        this.data = data;
    }
    
    private static class ViewHolder {
        final TextView txt;

        private ViewHolder(TextView txt) {
            this.txt = txt;
        }
    }
    
    public View getView(View convertView) {
        ViewHolder holder;
        View view;
        if (convertView == null) {
            View vi = (View)inflater.inflate(R.layout.text_item, null);
            holder = new ViewHolder((TextView)vi.findViewById(R.id.name));
            vi.setTag(holder);
            view = vi;
        } else {
            view = convertView;
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txt.setText(data);
        
        return view;
    }

    public int getViewType() {
        return RowType.TEXT_ROW.ordinal();
    }

    
}
