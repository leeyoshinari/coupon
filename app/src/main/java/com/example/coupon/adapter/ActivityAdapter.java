package com.example.coupon.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.coupon.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ActivityAdapter extends BaseAdapter {
    private final List<HashMap<String, Object>> data;
    private final ImageLoader imageLoader;

    public ActivityAdapter(List<HashMap<String, Object>> data, ImageLoader imageLoader) {
        super();
        this.data = data;
        this.imageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new ActivityAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_list_layout, null);
            holder.activity_img = (ImageView) convertView.findViewById(R.id.activity_img);
            holder.activity_title = (TextView) convertView.findViewById(R.id.activity_title);
            convertView.setTag(holder);
        } else {
            holder = (ActivityAdapter.ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> item = data.get(position);
        holder.activity_title.setText(Objects.requireNonNull(item.get("title")).toString());
        imageLoader.displayImage(Objects.requireNonNull(item.get("img")).toString(), holder.activity_img);
        return convertView;
    }

    public static final class ViewHolder {
        public TextView activity_title;
        public ImageView activity_img;
    }
}
