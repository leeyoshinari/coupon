package com.example.coupon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.coupon.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
//    private int widthDp = 0;
//    private int heightDp = 0;
//    public void setWidthDp(int widthDp) {
//        this.widthDp = widthDp;
//    }
//    public int getWidthDp() {
//        return this.widthDp;
//    }
//
//    public void setHeightDp(int heightDp) {
//        this.heightDp = heightDp;
//    }
//    public int getHeightDp() {
//        return heightDp;
//    }
//
//    HttpRequestController httpRequestController = new HttpRequestController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        assert getArguments() != null;
//        httpRequestController.setPlatform(getArguments().getString("platform"));
//        getScreenSizeDp();
//        showGoodDetail();

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                jumpToPurchasePage();
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

//    public void getScreenSizeDp() {
//        WindowManager wm = (WindowManager) this.requireContext().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        setWidthDp(dm.widthPixels);
//        setHeightDp(dm.heightPixels);
//    }
//
//    @SuppressLint("SetTextI18n")
//    public void showGoodDetail() {
//        TextView textView = requireView().findViewById(R.id.detail_good_title);
//        if (getArguments() != null) {
//            textView.setText(getArguments().getString("title"));
//
//            textView = requireView().findViewById(R.id.detail_good_volume);
//            textView.setText("销量: " + getArguments().getString("coupon_text").substring(getArguments().getString("coupon_text").lastIndexOf(" ") + 1));
//
//            textView = requireView().findViewById(R.id.detail_good_province);
//            textView.setText(getArguments().getString("provcity"));
//
//            textView = requireView().findViewById(R.id.detail_good_sale_price);
//            textView.setText(getArguments().getString("sale_price"));
//            textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            textView.invalidate();
//
//            textView = requireView().findViewById(R.id.detail_good_final_price_text);
//            textView.setText(getArguments().getString("final_price_text"));
//
//            textView = requireView().findViewById(R.id.detail_good_final_price);
//            textView.setText(getArguments().getString("final_price"));
//
//            textView = requireView().findViewById(R.id.detail_good_coupon);
//            ViewGroup.LayoutParams param = textView.getLayoutParams();
//            param.width = (int) (getWidthDp() * 0.5);
//            textView.setLayoutParams(param);
//            if (getArguments().getString("coupon_price").equals("")) {
//                textView.setText("无优惠券");
//                Button button = requireView().findViewById(R.id.button_second);
//                button.setText("立即购买");
//            } else {
//                textView.setText(getArguments().getString("coupon_price") + "元优惠券");
//                Button button = requireView().findViewById(R.id.button_second);
//                button.setText("领券并购买");
//            }
//
//            textView = requireView().findViewById(R.id.detail_good_detail_title);
//            textView.setText(getArguments().getString("title"));
//        } else {
//            Toast.makeText(requireContext().getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
//        }
//    }

//    public void jumpToPurchasePage() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (isPkgInstalled()) {
//                        JSONObject result = generatePromotionUrl(true);
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        intent.setData(Uri.parse(result.getString("urlPath")));
//                        startActivity(intent);
//                    } else {
//                        JSONObject result = generatePromotionUrl(false);
//                        if (httpRequestController.getPlatform().equals("tb")) {
//                            Intent intent = new Intent();
//                            intent.setAction("android.intent.action.VIEW");
//                            intent.setData(Uri.parse(result.getString("urlPath")));
//                            startActivity(intent);
//                        }
//                        if (httpRequestController.getPlatform().equals("pdd")) {
//                            String appId = result.getString("appId");
//                            String pagePath = result.getString("path");
//                        }
//                        // Toast.makeText(requireContext().getApplicationContext(), "复制淘口令成功，请在手机淘宝打开", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    public Boolean isPkgInstalled() {
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = requireContext().getPackageManager().getPackageInfo(getPkgName(), 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return packageInfo != null;
//    }
//
//    public String getPkgName(){
//        String pkgName = "com.taobao.taobao";
//        switch (httpRequestController.getPlatform()) {
//            case "jd":
//                pkgName = "com.jingdong.app.mall";
//                break;
//            case "pdd":
//                pkgName = "com.xunmeng.pinduoduo";
//                break;
//            case "wm":
//                pkgName = "com.xunmng.pindduo";
//                break;
//        }
//        return pkgName;
//    }
//
//    public JSONObject generatePromotionUrl(boolean isApp) {
//        JSONObject result = new JSONObject();
//        JSONObject queryParam = new JSONObject();
//        try {
//            assert getArguments() != null;
//            if (httpRequestController.getPlatform().equals("tb")) {
//                String coupon_url = getArguments().getString("coupon_url");
//                String urlPath = "";
//                if (coupon_url == null || "".equals(coupon_url)) {
//                    urlPath = getArguments().getString("item_url").replace("https:", "").replace("http:", "");
//                } else {
//                    urlPath = coupon_url.replace("https:", "").replace("http:", "");
//                }
//                if (isApp) {
//                    result.put("urlPath", "taobao:" + urlPath);
//                } else {
//                    result.put("urlPath", "https:" + urlPath);
//                }
//            }
//            if (httpRequestController.getPlatform().equals("pdd")) {
//                queryParam.put("search_id", getArguments().getString("search_id"));
//                queryParam.put("goods_sign", getArguments().getString("goods_sign"));
//                JSONObject generateResult = httpRequestController.generatePromotion(queryParam, isApp);
//                generateResult.getJSONObject("goods_promotion_url_generate_response");
//                if (isApp) {
//                    result.put("urlPath", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getString("schema_url"));
//                } else {
//                    result.put("appId", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getJSONObject("we_app_info").getString("app_id"));
//                    result.put("path", generateResult.getJSONObject("goods_promotion_url_generate_response").getJSONArray("goods_promotion_url_list").getJSONObject(0).getJSONObject("we_app_info").getString("page_path"));
//                }
//            }
//            if (httpRequestController.getPlatform().equals("jd")) {
//                queryParam.put("goods_id", getArguments().getString("skuId"));
//                JSONObject generateResult = httpRequestController.generatePromotion(queryParam, isApp);
//                if (isApp) {
//                    String path = "{\"category\":\"jump\",\"des\":\"m\",\"url\":\"" + generateResult.getString("data") + "\"}";
//                    result.put("urlPath", "openapp.jdmobile://virtual?params=" + URLEncoder.encode(path, "UTF-8"));
//                } else {
//                    result.put("appId", "wx91d27dbf599dff74");
//                    result.put("path", "pages/union/proxy/proxy?spreadUrl=" + generateResult.getString("data"));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}