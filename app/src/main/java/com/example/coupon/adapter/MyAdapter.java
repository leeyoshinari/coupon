package com.example.coupon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Environment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.coupon.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private final List<HashMap<String, Object>> data;
    private final ImageLoader imageLoader;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.loading)
            .showImageForEmptyUri(R.mipmap.loading)
            .showImageOnFail(R.mipmap.loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public MyAdapter(Context context, List<HashMap<String, Object>> data) {
        super();
        this.data = data;
        this.context = context;
        File picPath = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "coupon" + File.separator + "images");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(300, 300)
                .diskCacheExtraOptions(300, 300, null)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(10485760))
                .memoryCacheSize(10485760)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiskCache(picPath))
                .diskCacheSize(209715200)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_list_layout, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.coupon_price = (TextView) convertView.findViewById(R.id.coupon_price);
            holder.coupon_text = (TextView) convertView.findViewById(R.id.coupon_text);
            holder.sale_price = (TextView) convertView.findViewById(R.id.sale_price);
            holder.final_price_text = (TextView) convertView.findViewById(R.id.final_price_text);
            holder.final_price = (TextView) convertView.findViewById(R.id.final_price);
            holder.shop = (TextView) convertView.findViewById(R.id.shop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> item = data.get(position);
        holder.title.setText(Objects.requireNonNull(item.get("title")).toString());
        holder.coupon_price.setText(Objects.requireNonNull(item.get("coupon_price")).toString());
        holder.coupon_text.setText(Objects.requireNonNull(item.get("coupon_text")).toString());
        holder.sale_price.setText(Objects.requireNonNull(item.get("sale_price")).toString());
        holder.sale_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.sale_price.invalidate();
        holder.final_price_text.setText(Objects.requireNonNull(item.get("final_price_text")).toString());
        holder.final_price.setText(Objects.requireNonNull(item.get("final_price")).toString());
        holder.shop.setText(Objects.requireNonNull(item.get("shop")).toString());

        imageLoader.displayImage(Objects.requireNonNull(item.get("img")).toString(), holder.img);
        return convertView;
    }

    public static final class ViewHolder{
        public TextView title, coupon_price, coupon_text, sale_price, final_price_text, final_price, shop;
        public ImageView img;
    }
}
