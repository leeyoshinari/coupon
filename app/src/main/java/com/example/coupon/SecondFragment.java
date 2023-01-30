package com.example.coupon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.coupon.databinding.FragmentSecondBinding;
import org.json.JSONObject;

import java.util.HashMap;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private int widthDp = 0;
    private int heightDp = 0;
    public void setWidthDp(int widthDp) {
        this.widthDp = widthDp;
    }
    public int getWidthDp() {
        return this.widthDp;
    }

    public void setHeightDp(int heightDp) {
        this.heightDp = heightDp;
    }
    public int getHeightDp() {
        return heightDp;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getScreenSizeDp();
        showGoodDetail();

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    public void getScreenSizeDp() {
        WindowManager wm = (WindowManager) this.requireContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        setWidthDp(dm.widthPixels);
        setHeightDp(dm.heightPixels);
    }

    @SuppressLint("SetTextI18n")
    public void showGoodDetail() {
        TextView textView = requireView().findViewById(R.id.detail_good_title);
        if (getArguments() != null) {
            textView.setText(getArguments().getString("title"));

            textView = requireView().findViewById(R.id.detail_good_volume);
            textView.setText("销量: " + getArguments().getString("coupon_text").substring(getArguments().getString("coupon_text").lastIndexOf(" ") + 1));

            textView = requireView().findViewById(R.id.detail_good_province);
            textView.setText(getArguments().getString("provcity"));

            textView = requireView().findViewById(R.id.detail_good_sale_price);
            textView.setText(getArguments().getString("sale_price"));
            textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            textView.invalidate();

            textView = requireView().findViewById(R.id.detail_good_final_price_text);
            textView.setText(getArguments().getString("final_price_text"));

            textView = requireView().findViewById(R.id.detail_good_final_price);
            textView.setText(getArguments().getString("final_price"));

            textView = requireView().findViewById(R.id.detail_good_coupon);
            ViewGroup.LayoutParams param = textView.getLayoutParams();
            param.width = (int) (getWidthDp() * 0.5);
            textView.setLayoutParams(param);
            if (getArguments().getString("coupon_price").equals("")) {
                textView.setText("无优惠券");
                Button button = requireView().findViewById(R.id.button_second);
                button.setText("立即购买");
            } else {
                textView.setText(getArguments().getString("coupon_price") + "元优惠券");
                Button button = requireView().findViewById(R.id.button_second);
                button.setText("领券并购买");
            }

            textView = requireView().findViewById(R.id.detail_good_detail_title);
            textView.setText(getArguments().getString("title"));
        } else {
            Toast.makeText(requireContext().getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}