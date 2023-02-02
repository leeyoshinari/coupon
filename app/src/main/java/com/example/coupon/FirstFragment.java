package com.example.coupon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import com.example.coupon.animation.CustomLoadingDialog;
import com.example.coupon.databinding.FragmentFirstBinding;

import com.example.coupon.controller.HttpRequestController;
import com.example.coupon.adapter.MyAdapter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private CustomLoadingDialog customLoadingDialog;
    private int widthPixel = 0;
    private int heightPixel = 0;
    private float density = 0;
    private ImageLoader imageLoader;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.loading)
            .showImageForEmptyUri(R.mipmap.loading)
            .showImageOnFail(R.mipmap.loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public void setWidthPixel(int widthPixel) {
        this.widthPixel = widthPixel;
    }
    public int getWidthPixel() {
        return this.widthPixel;
    }

    public void setHeightPixel(int heightPixel) {
        this.heightPixel = heightPixel;
    }
    public int getHeightPixel() {
        return heightPixel;
     }

     public void setDensity(float density) {
        this.density = density;
     }

     public float getDensity() {
        return this.density;
     }

    HttpRequestController httpRequestController = new HttpRequestController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File picPath = new File(requireContext().getExternalCacheDir().getPath());
        if (!picPath.exists()) {
            picPath.mkdirs();
        }
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(requireContext())
                .memoryCacheExtraOptions(300, 300)
                .diskCacheExtraOptions(300, 300, null)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(10485760))
                .memoryCacheSize(10485760)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiskCache(picPath))
                .diskCacheSize(209715200)
                .defaultDisplayImageOptions(options)
                .imageDownloader(new BaseImageDownloader(requireContext(), 10000, 30000))
                .build();
        ImageLoader.getInstance().init(configuration);
        this.imageLoader = ImageLoader.getInstance();

        getScreenSizeDp();
        EditText editText = view.findViewById(R.id.key_word);
        ViewGroup.LayoutParams param = editText.getLayoutParams();
        param.width = (int) (getWidthPixel() * 0.7);
        editText.setLayoutParams(param);
        Button button = view.findViewById(R.id.search_button);
        param = button.getLayoutParams();
        param.width = (int) (getWidthPixel() * 0.25);
        button.setLayoutParams(param);
        LinearLayoutCompat linearLayout = view.findViewById(R.id.tb);
        int marginStartSize = (int) ((getWidthPixel()/getDensity() - 36 * 4) / 5 * getDensity());
        ViewGroup.MarginLayoutParams paramMargin = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
        paramMargin.setMarginStart(marginStartSize);
        linearLayout.setLayoutParams(paramMargin);

        linearLayout = view.findViewById(R.id.jd);
        paramMargin = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
        paramMargin.setMarginStart(marginStartSize);
        linearLayout.setLayoutParams(paramMargin);

        linearLayout = view.findViewById(R.id.pdd);
        paramMargin = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
        paramMargin.setMarginStart(marginStartSize);
        linearLayout.setLayoutParams(paramMargin);

        linearLayout = view.findViewById(R.id.wm);
        paramMargin = (ViewGroup.MarginLayoutParams) linearLayout.getLayoutParams();
        paramMargin.setMarginStart(marginStartSize);
        linearLayout.setLayoutParams(paramMargin);

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runShowGoodList();
//                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.keyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //hideKeyboard(view);
                    runShowGoodList();
                    return true;
                }
                return false;
            }
        });

        binding.tb.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                String n = requireContext().getResources().getResourceEntryName(view.getId());
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.tb_text, "tb");
                runShowGoodList();
            }
        });

        binding.jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = requireContext().getResources().getResourceEntryName(view.getId());
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.jd_text, "jd");
                runShowGoodList();
            }
        });

        binding.pdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = requireContext().getResources().getResourceEntryName(view.getId());
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.pdd_text, "pdd");
                runShowGoodList();
            }
        });

        binding.wm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.wm_text, "wm");
            }
        });
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
//        View view;
//    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void runShowGoodList() {
        customLoadingDialog = new CustomLoadingDialog(requireContext());
        customLoadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                onShowGoodList();
                customLoadingDialog.dismiss();
            }
        }).start();
    }

    public void onShowGoodList() {
        String keyWord = binding.keyWord.getText().toString().trim();
        String url = "";
        if (keyWord.equals("")) {
            url = httpRequestController.generateUrlPathForList(keyWord, 1);
        } else {
            url = httpRequestController.generateUrlPathForList(keyWord, 1);
        }
        List<HashMap<String, Object>> arrayList = httpRequestController.parseGoodList(url);
        MyAdapter myAdapter = new MyAdapter(arrayList, this.imageLoader);
        ListView listView = requireView().findViewById(R.id.good_list_view);
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(myAdapter);
                AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        HashMap<String, Object> itemHashMap = arrayList.get(i);
                        jumpToPurchasePage(itemHashMap);
                    }
                };
                listView.setOnItemClickListener(onItemClickListener);
            }
        });
    }

    public void getScreenSizeDp() {
        WindowManager wm = (WindowManager) this.requireContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        setWidthPixel(dm.widthPixels);
        setHeightPixel(dm.heightPixels);
        setDensity(dm.density);
    }

    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public void jumpToGoodDetailPage(HashMap<String, Object> hashMap) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bundle bundle = new Bundle();
//                    if (httpRequestController.getPlatform().equals("pdd")) {
//                        String urlPath = httpRequestController.generateUrlPathForDetail(hashMap);
//                        JSONObject goodDetail = httpRequestController.parseGoodDetail(urlPath);
//                        hashMap.put("title_img", goodDetail.getJSONArray("title_img"));
//                    }
//                    if (httpRequestController.getPlatform().equals("jd")) {
//                        String urlPath = httpRequestController.generateUrlPathForDetail(hashMap);
//                        JSONObject goodDetail = httpRequestController.parseGoodDetail(urlPath);
//                    }
//                    for (Map.Entry<String, Object> entity : hashMap.entrySet()) {
//                        if (entity.getKey().equals("title_img") || entity.getKey().equals("desc_img")) {
//                            ArrayList<String> arrayList = new ArrayList<String>();
//                            JSONArray jsonArray = (JSONArray) entity.getValue();
//                            for (int i = 0; i < jsonArray.length(); i++){
//                                arrayList.add(jsonArray.getString(i));
//                            }
//                            bundle.putStringArrayList(entity.getKey(), arrayList);
//                        }
//                        else {
//                            bundle.putString(entity.getKey(), entity.getValue().toString());
//                        }
//                    }
//                    bundle.putString("platform", httpRequestController.getPlatform());
//                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    public void jumpToPurchasePage(HashMap<String, Object> hashMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isPkgInstalled()) {
                        JSONObject result = generatePromotionUrl(hashMap,true);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(Uri.parse(result.getString("urlPath")));
                        startActivity(intent);
                    } else {
                        JSONObject result = generatePromotionUrl(hashMap, false);
                        if (httpRequestController.getPlatform().equals("tb")) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(Uri.parse(result.getString("urlPath")));
                            startActivity(intent);
                        }
                        if (httpRequestController.getPlatform().equals("pdd")) {
                            String appId = result.getString("appId");
                            String pagePath = result.getString("path");
                        }
                        // Toast.makeText(requireContext().getApplicationContext(), "复制淘口令成功，请在手机淘宝打开", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Boolean isPkgInstalled() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = requireContext().getPackageManager().getPackageInfo(getPkgName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public String getPkgName(){
        String pkgName = "com.taobao.taobao";
        switch (httpRequestController.getPlatform()) {
            case "jd":
                pkgName = "com.jingdong.app.mall";
                break;
            case "pdd":
                pkgName = "com.xunmeng.pinduoduo";
                break;
            case "wm":
                pkgName = "com.xunmng.pindduo";
                break;
        }
        return pkgName;
    }

    public JSONObject generatePromotionUrl(HashMap<String, Object> hashMap, boolean isApp) {
        JSONObject result = new JSONObject();
        JSONObject queryParam = new JSONObject();
        try {
            if (httpRequestController.getPlatform().equals("tb")) {
                String coupon_url = (String) hashMap.get("coupon_url");
                String urlPath = "";
                if (coupon_url == null || "".equals(coupon_url)) {
                    urlPath = Objects.requireNonNull(hashMap.get("item_url")).toString().replace("https:", "").replace("http:", "");
                } else {
                    urlPath = coupon_url.replace("https:", "").replace("http:", "");
                }
                if (isApp) {
                    result.put("urlPath", "taobao:" + urlPath);
                } else {
                    result.put("urlPath", "https:" + urlPath);
                }
            }
            if (httpRequestController.getPlatform().equals("pdd")) {
                queryParam.put("search_id", hashMap.get("search_id"));
                queryParam.put("goods_sign", hashMap.get("goods_sign"));
                JSONObject generateResult = httpRequestController.generatePromotion(queryParam, isApp);
                generateResult.getJSONObject("goods_promotion_url_generate_response");
                if (isApp) {
                    result.put("urlPath", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getString("schema_url"));
                } else {
                    result.put("appId", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getJSONObject("we_app_info").getString("app_id"));
                    result.put("path", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getJSONObject("we_app_info").getString("page_path"));
                }
            }
            if (httpRequestController.getPlatform().equals("jd")) {
                queryParam.put("goods_id", hashMap.get("skuId"));
                JSONObject generateResult = httpRequestController.generatePromotion(queryParam, isApp);
                if (isApp) {
                    String path = "{\"category\":\"jump\",\"des\":\"m\",\"url\":\"" + generateResult.getString("data") + "\"}";
                    result.put("urlPath", "openapp.jdmobile://virtual?params=" + URLEncoder.encode(path, "UTF-8"));
                } else {
                    result.put("appId", "wx91d27dbf599dff74");
                    result.put("path", "pages/union/proxy/proxy?spreadUrl=" + generateResult.getString("data"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void clickButtonColorImg(int textId, String name) {
        int[] textIds = {R.id.tb_text, R.id.jd_text, R.id.pdd_text, R.id.wm_text};
        ImageView imageView;
        TextView textView;
        for (int i:textIds) {
            textView = requireView().findViewById(i);
            if (i == textId) {
                textView.setTextColor(0xFFFF5000);
            } else {
                textView.setTextColor(0xFF888888);
            }
        }
        imageView = requireView().findViewById(R.id.tb_img);
        if ("tb".equals(name)) {
            imageView.setImageResource(R.mipmap.tb_g);
        } else {
            imageView.setImageResource(R.mipmap.tb);
        }

        imageView = requireView().findViewById(R.id.jd_img);
        if ("jd".equals(name)) {
            imageView.setImageResource(R.mipmap.jd_g);
        } else {
            imageView.setImageResource(R.mipmap.jd);
        }

        imageView = requireView().findViewById(R.id.pdd_img);
        if ("pdd".equals(name)) {
            imageView.setImageResource(R.mipmap.pdd_g);
        } else {
            imageView.setImageResource(R.mipmap.pdd);
        }

        imageView = requireView().findViewById(R.id.wm_img);
        if ("wm".equals(name)) {
            imageView.setImageResource(R.mipmap.wm_g);
        } else {
            imageView.setImageResource(R.mipmap.wm);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}