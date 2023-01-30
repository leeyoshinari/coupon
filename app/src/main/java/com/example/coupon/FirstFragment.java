package com.example.coupon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.coupon.databinding.FragmentFirstBinding;

import com.example.coupon.controller.HttpRequestController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private int widthPixel = 0;
    private int heightPixel = 0;
    private float density = 0;

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

        getScreenSizeDp();
        EditText editText = view.findViewById(R.id.key_word);
        ViewGroup.LayoutParams param = editText.getLayoutParams();
        param.width = (int) (getWidthPixel() * 0.7);
        editText.setLayoutParams(param);
        Button button = view.findViewById(R.id.search_button);
        param = button.getLayoutParams();
        param.width = (int) (getWidthPixel() * 0.25);
        button.setLayoutParams(param);
// 135  1080
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

        int maxMemorySize = (int) (Runtime.getRuntime().maxMemory());

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runShowGoodList();
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
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

    public void runShowGoodList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onShowGoodList();
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(this.getContext(), arrayList, R.layout.good_list_layout,
                new String[]{"img", "title", "coupon_price", "coupon_text", "sale_price", "final_price_text", "final_price", "shop"}, new int[]{R.id.img, R.id.title, R.id.coupon_price, R.id.coupon_text, R.id.sale_price, R.id.final_price_text, R.id.final_price, R.id.shop});
        ListView listView = requireView().findViewById(R.id.good_list_view);
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(simpleAdapter);
                AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        HashMap<String, Object> itemHashMap = arrayList.get(i);
                        jumpToGoodDetailPage(itemHashMap);
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

    public void jumpToGoodDetailPage(HashMap<String, Object> hashMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle bundle = new Bundle();
                    if (httpRequestController.getPlatform().equals("pdd")) {
                        String urlPath = httpRequestController.generateUrlPathForDetail(hashMap);
                        JSONObject goodDetail = httpRequestController.parseGoodDetail(urlPath);
                        hashMap.put("title_img", goodDetail.getJSONArray("title_img"));
                    }
                    if (httpRequestController.getPlatform().equals("jd")) {
                        String urlPath = httpRequestController.generateUrlPathForDetail(hashMap);
                        JSONObject goodDetail = httpRequestController.parseGoodDetail(urlPath);
                    }
                    for (Map.Entry<String, Object> entity : hashMap.entrySet()) {
                        if (entity.getKey().equals("title_img") || entity.getKey().equals("desc_img")) {
                            ArrayList<String> arrayList = new ArrayList<String>();
                            JSONArray jsonArray = (JSONArray) entity.getValue();
                            for (int i = 0; i < jsonArray.length(); i++){
                                arrayList.add(jsonArray.getString(i));
                            }
                            bundle.putStringArrayList(entity.getKey(), arrayList);
                        }
                        else {
                            bundle.putString(entity.getKey(), entity.getValue().toString());
                        }
                    }
                    bundle.putString("platform", httpRequestController.getPlatform());
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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