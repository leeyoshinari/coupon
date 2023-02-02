package com.example.coupon.animation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.coupon.R;

public class CustomLoadingDialog extends Dialog {

//    TextView tvLoadingTx;
    ImageView ivLoading;

    public CustomLoadingDialog(Context context) {
        this(context, R.style.loading_dialog, "玩命加载中...");
    }

    public CustomLoadingDialog(Context context, String string) {
        this(context, R.style.loading_dialog, string);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected CustomLoadingDialog(Context context, int theme, String string) {
        super(context, theme);
        setCanceledOnTouchOutside(false);//点击其他区域时   true  关闭弹窗  false  不关闭弹窗
        setContentView(R.layout.loading_dialog);//加载布局
        // tvLoadingTx = findViewById(R.id.tv_loading_tx);
        // tvLoadingTx.setText(string);
        ivLoading = findViewById(R.id.iv_loading);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        // 使用ImageView显示动画
        ivLoading.startAnimation(hyperspaceJumpAnimation);

        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().getAttributes().dimAmount = 0.2f;//背景透明度  取值范围 0 ~ 1
    }

    @Override
    public void show() {
        super.show();
    }

    //关闭弹窗
    @Override
    public void dismiss() {
        super.dismiss();
    }
}