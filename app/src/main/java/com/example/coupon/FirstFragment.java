package com.example.coupon;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.util.DisplayMetrics;
import android.view.*;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.coupon.adapter.ActivityAdapter;
import com.example.coupon.animation.CustomLoadingDialog;
import com.example.coupon.databinding.FragmentFirstBinding;

import com.example.coupon.controller.HttpRequestController;
import com.example.coupon.adapter.MyAdapter;
import com.example.coupon.dataObject.fragmentParamsDo;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;


public class FirstFragment extends Fragment {
    private static final String APP_URL = "https://gitee.com/leeyoshinari/coupon/blob/main/app/version/%E4%BC%98%E6%83%A0%E5%88%B8.apk";
    private FragmentFirstBinding binding;
    private CustomLoadingDialog customLoadingDialog;
    private ImageLoader imageLoader;
    private ListView listView;
    private MyAdapter myAdapter;
    private ActivityAdapter activityAdapter;
    private List<HashMap<String, Object>> arrayList = new ArrayList<>();

    private Handler handler = new Handler(Looper.getMainLooper()) {
        LinearLayoutCompat linearLayout;
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(requireContext().getApplicationContext(), "~ 我是有底线的 ~", Toast.LENGTH_LONG).show();
                    break;
                case 10:
                    Toast.makeText(requireContext().getApplicationContext(), "权限不足", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    linearLayout = requireView().findViewById(R.id.linear_layout_sort); // 排序布局隐藏
                    linearLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    linearLayout = requireView().findViewById(R.id.linear_layout_sort); // 排序布局显示
                    linearLayout.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    linearLayout = requireView().findViewById(R.id.linear_layout_search); // 搜索布局隐藏
                    linearLayout.setVisibility(View.GONE);
                    break;
                case 4:
                    linearLayout = requireView().findViewById(R.id.linear_layout_search);  //搜索布局显示
                    linearLayout.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    linearLayout = requireView().findViewById(R.id.linear_layout_activity); // 活动布局隐藏
                    linearLayout.setVisibility(View.GONE);
                    break;
                case 6:
                    linearLayout = requireView().findViewById(R.id.linear_layout_activity);  //活动布局显示
                    linearLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private final DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.loading)
            .showImageForEmptyUri(R.mipmap.loading)
            .showImageOnFail(R.mipmap.loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    HttpRequestController httpRequestController = new HttpRequestController();
    fragmentParamsDo fp = new fragmentParamsDo();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 创建本地文件夹
        File picPath = new File(requireContext().getExternalCacheDir().getPath());
        if (!picPath.exists()) {
            picPath.mkdirs();
        }
        // 初始化 imageloader
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

        this.listView = requireView().findViewById(R.id.good_list_view);

        // 检查读写权限
        if (checkPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE})) {
            return;
        }
        // 检查版本，如果需要升级，则打开浏览器下载最新版本
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getAppVersion() < httpRequestController.updateVersion(picPath)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(APP_URL));
                    startActivity(intent);
                }
            }
        }).start();
        // 获取屏幕尺寸
        getScreenSizeDp();
        // 根据屏幕尺寸调整页面布局
        EditText editText = view.findViewById(R.id.key_word);
        ViewGroup.LayoutParams param = editText.getLayoutParams();
        param.width = (int) (fp.getWidthPixel() * 0.7);
        editText.setLayoutParams(param);
        Button button = view.findViewById(R.id.search_button);
        param = button.getLayoutParams();
        param.width = (int) (fp.getWidthPixel() * 0.25);
        button.setLayoutParams(param);
        LinearLayoutCompat linearLayout = view.findViewById(R.id.tb);
        int marginStartSize = (int) ((fp.getWidthPixel()/fp.getDensity() - 36 * 4) / 5 * fp.getDensity());
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

        // 绑定点击事件
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(true);
                hideKeyboard(view);
                runShowGoodList();
//                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.keyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    initData(true);
                    hideKeyboard(view);
                    runShowGoodList();
                    return true;
                }
                return false;
            }
        });

        binding.sortAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (httpRequestController.getPlatform().equals("pdd")) {
                    fp.setSort("0");
                } else {
                    fp.setSort("");
                }
                if (httpRequestController.getPlatform().equals("jd")) {
                    fp.setSortType("desc");
                }
                clickSortChangeColor(R.id.sort_all);
                initData(false);
                runShowGoodList();
            }
        });

        binding.sortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (httpRequestController.getPlatform().equals("tb")) {
                    fp.setSort("price_asc");
                }
                if (httpRequestController.getPlatform().equals("jd")) {
                    fp.setSort("1");
                    fp.setSortType("asc");
                }
                if (httpRequestController.getPlatform().equals("pdd")) {
                    fp.setSort("3");
                }
                clickSortChangeColor(R.id.sort_price);
                initData(false);
                runShowGoodList();
            }
        });

        binding.sortVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (httpRequestController.getPlatform().equals("tb")) {
                    fp.setSort("total_sales_des");
                }
                if (httpRequestController.getPlatform().equals("jd")) {
                    fp.setSort("4");
                    fp.setSortType("desc");
                }
                if (httpRequestController.getPlatform().equals("pdd")) {
                    fp.setSort("6");
                }
                clickSortChangeColor(R.id.sort_volume);
                initData(false);
                runShowGoodList();
            }
        });
        binding.sortCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = requireView().findViewById(R.id.sort_coupon);
                if (fp.getHasCoupon()) {
                    fp.setHasCoupon(false);
                    textView.setTextColor(0xFF888888);
                } else {
                    fp.setHasCoupon(true);
                    textView.setTextColor(0xFFFF5000);
                }
                initData(false);
                runShowGoodList();
            }
        });

        binding.tb.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.tb_text, "tb");
                handler.sendEmptyMessage(4);
                handler.sendEmptyMessage(5);
                initData(true);
                runShowGoodList();
            }
        });

        binding.jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.jd_text, "jd");
                handler.sendEmptyMessage(4);
                handler.sendEmptyMessage(5);
                initData(true);
                runShowGoodList();
            }
        });

        binding.pdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpRequestController.setPlatform(requireContext().getResources().getResourceEntryName(view.getId()));
                clickButtonColorImg(R.id.pdd_text, "pdd");
                handler.sendEmptyMessage(4);
                handler.sendEmptyMessage(5);
                initData(true);
                runShowGoodList();
            }
        });

        binding.wm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButtonColorImg(R.id.wm_text, "wm");
                handler.sendEmptyMessage(1);
                handler.sendEmptyMessage(3);
                handler.sendEmptyMessage(6);
                fp.setActivityType("ele");
                clickActivityChangeColor(R.id.activity_elem);
                initData(true);
                runShowActivity();
            }
        });
        binding.activityElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fp.setActivityType("ele");
                clickActivityChangeColor(R.id.activity_elem);
                runShowActivity();
            }
        });
        binding.activityTb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fp.setActivityType("tb");
                clickActivityChangeColor(R.id.activity_tb);
                runShowActivity();
            }
        });
        binding.activityJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fp.setActivityType("jd");
                clickActivityChangeColor(R.id.activity_jd);
                runShowActivity();
            }
        });
        binding.activityPdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fp.setActivityType("pdd");
                clickActivityChangeColor(R.id.activity_pdd);
                runShowActivity();
            }
        });
        handler.sendEmptyMessage(1);
        handler.sendEmptyMessage(5);
        runShowGoodList();
    }

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
            handler.sendEmptyMessage(1);
            url = httpRequestController.generateUrlPathForRecommend(fp.getPageNo());
        } else {
            handler.sendEmptyMessage(2);
            url = httpRequestController.generateUrlPathForList(keyWord, fp.getSort(), fp.getSortType(), fp.getHasCoupon(), fp.getPageNo());
        }
        List<HashMap<String, Object>> result = httpRequestController.parseGoodList(url, keyWord);
        if (result.size() > 0) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    arrayList.addAll(result);
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    } else {
                        myAdapter = new MyAdapter(arrayList, imageLoader);
                        listView.setAdapter(myAdapter);
                        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                HashMap<String, Object> itemHashMap = arrayList.get(i);
                                jumpToPurchasePage(itemHashMap);
                            }
                        };
                        AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                TextView textView = view.findViewById(R.id.title);
                                ClipboardManager clipboardManager = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText(null, textView.getText());
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(requireContext().getApplicationContext(), "复制商品标题成功", Toast.LENGTH_LONG).show();
                                return true;
                            }
                        };
                        listView.setOnItemClickListener(onItemClickListener);
                        listView.setOnItemLongClickListener(onItemLongClickListener);
                    }
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    if (i == 0 && absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        fp.setPageNo(fp.getPageNo() + 1);
                        runShowGoodList();
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }
            });
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    public void getScreenSizeDp() {
        WindowManager wm = (WindowManager) this.requireContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        fp.setWidthPixel(dm.widthPixels);
        fp.setHeightPixel(dm.heightPixels);
        fp.setDensity(dm.density);
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
        customLoadingDialog = new CustomLoadingDialog(requireContext());
        customLoadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isPkgInstalled(httpRequestController.getPlatform())) {
                        JSONObject result = generatePromotionUrl(hashMap,true);
                        customLoadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(Uri.parse(result.getString("urlPath")));
                        startActivity(intent);
                    } else {
                        JSONObject result = generatePromotionUrl(hashMap, false);
                        customLoadingDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(Uri.parse(result.getString("urlPath")));
                        startActivity(intent);
                        //if (httpRequestController.getPlatform().equals("pdd")) {
                        //    String appId = result.getString("appId");
                        //    String pagePath = result.getString("path");
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Boolean isPkgInstalled(String platform) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = requireContext().getPackageManager().getPackageInfo(getPkgName(platform), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public String getPkgName(String platform){
        String pkgName = "com.taobao.taobao";
        switch (platform) {
            case "jd":
                pkgName = "com.jingdong.app.mall";
                break;
            case "pdd":
                pkgName = "com.xunmeng.pinduoduo";
                break;
            case "wm":
                pkgName = "com.xunmng.pindduo";
                break;
            case "ele":
                pkgName = "me.ele";
                break;
            case "alipay":
                pkgName = "com.eg.android.AlipayGphone";
                break;
            case "wechat":
                pkgName = "com.tencent.mm";
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
                    //result.put("appId", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getJSONObject("we_app_info").getString("app_id"));
                    //result.put("path", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getJSONObject("we_app_info").getString("page_path"));
                    result.put("urlPath", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getString("url"));
                }
            }
            if (httpRequestController.getPlatform().equals("jd")) {
                queryParam.put("goods_id", hashMap.get("skuId"));
                JSONObject generateResult = httpRequestController.generatePromotion(queryParam, isApp);
                if (isApp) {
                    String path = "{\"category\":\"jump\",\"des\":\"m\",\"url\":\"" + generateResult.getString("data") + "\"}";
                    result.put("urlPath", "openapp.jdmobile://virtual?params=" + URLEncoder.encode(path, "UTF-8"));
                } else {
                    //result.put("appId", "wx91d27dbf599dff74");
                    //result.put("path", "pages/union/proxy/proxy?spreadUrl=" + generateResult.getString("data"));
                    result.put("urlPath", generateResult.getString("data"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void initData(Boolean isAll) {
        arrayList = new ArrayList<>();
        myAdapter = null;
        activityAdapter = null;
        listView.setAdapter(null);
        binding.keyWord.clearFocus();
        fp.setPageNo(1);
        if (isAll) {
            fp.setSort("");
            fp.setSortType("");
            fp.setHasCoupon(false);
            TextView textView = requireView().findViewById(R.id.sort_coupon);
            textView.setTextColor(0xFF888888);
            clickSortChangeColor(-1);
        }
    }

    public void clickSortChangeColor(int textId) {
        int[] textIds = {R.id.sort_all, R.id.sort_price, R.id.sort_volume};
        TextView textView;
        for (int i:textIds) {
            textView = requireView().findViewById(i);
            if (i == textId) {
                textView.setTextColor(0xFFFF5000);
            } else {
                textView.setTextColor(0xFF888888);
            }
        }
    }

    public void clickActivityChangeColor(int textId) {
        int[] textIds = {R.id.activity_elem, R.id.activity_tb, R.id.activity_pdd, R.id.activity_jd};
        TextView textView;
        for (int i:textIds) {
            textView = requireView().findViewById(i);
            if (i == textId) {
                textView.setTextColor(0xFFFF5000);
            } else {
                textView.setTextColor(0xFF888888);
            }
        }
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

    public void runShowActivity() {
        customLoadingDialog = new CustomLoadingDialog(requireContext());
        customLoadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                showActivity();
                customLoadingDialog.dismiss();
            }
        }).start();
    }

    public void showActivity() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("activityType", fp.getActivityType());
            String urlPath = httpRequestController.generateUrlPathForActivity(jsonObject);
            arrayList = httpRequestController.parseActivityList(urlPath, fp.getActivityType());
            listView.post(new Runnable() {
                @Override
                public void run() {
                    activityAdapter = new ActivityAdapter(arrayList, imageLoader);
                    listView.setAdapter(activityAdapter);
                    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            HashMap<String, Object> itemHashMap = arrayList.get(i);
                            jumpToActivityPage(itemHashMap);
                        }
                    };
                    listView.setOnItemClickListener(onItemClickListener);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jumpToActivityPage(HashMap<String, Object> hashMap) {
        try {
            String urlPath = Objects.requireNonNull(hashMap.get("urlPath")).toString();
            if (isPkgInstalled(fp.getActivityType())) {
                if (fp.getActivityType().equals("ele")) {
                    urlPath = "eleme:" + urlPath.replace("https:", "");
                }
                if (fp.getActivityType().equals("tb")) {
                    urlPath = "taobao:" + urlPath.replace("https:", "");
                }
                if (fp.getActivityType().equals("jd")) {
                    String path = "{\"category\":\"jump\",\"des\":\"m\",\"url\":\"" + urlPath + "\"}";
                    urlPath = "openapp.jdmobile://virtual?params=" + URLEncoder.encode(path, "UTF-8");
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(urlPath));
                startActivity(intent);
            } else {
                if (isPkgInstalled("alipay") && fp.getActivityType().equals("ele")) {
                    urlPath = "alipays://platformapi/startapp?appId=2018090761255717&page=pages/webview-redirect/webview-redirect?url=" + urlPath;
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(urlPath));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean checkPermissions(String[] permissions) {
        List<String> mPermissionList = new ArrayList<>();
        for (String s : permissions) {
            if (requireContext().checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(s);
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager() || mPermissionList.size() == 0) {
            return false;
        } else {
            handler.sendEmptyMessage(10);
            ActivityCompat.requestPermissions(requireActivity(), mPermissionList.toArray(new String[0]), 1024);
            return true;
        }
    }

    public int getAppVersion() {
        try {
            return requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}