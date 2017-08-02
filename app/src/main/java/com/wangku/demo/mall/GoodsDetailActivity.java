package com.wangku.demo.mall;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llOverlay;
    private ViewPager viewPager;
    private ArrayList<Integer> pics;
    private TextView tvCurrent;
    private RelativeLayout rlContainer;
    private ImageView ivPic_;
    private TextView tvPrice;
    private ChoiceParameterDialog choiceParameterDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        llOverlay = (LinearLayout) findViewById(R.id.ll_overlay);
        ivPic_ = (ImageView) findViewById(R.id.iv_pic_);
        viewPager = (ViewPager) findViewById(R.id.advertViewPager);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        findViewById(R.id.rl_parameter_choice).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_back_).setOnClickListener(this);
        findViewById(R.id.btn_buy).setOnClickListener(this);
        init();
        dissmissOverlay();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.getScreenWidth(this), Utils.getScreenWidth(this));
        rlContainer.setLayoutParams(layoutParams);
        ivPic_.setLayoutParams(layoutParams);
        String priceString = "￥99.00" ;
        SpannableString spannableString = new SpannableString(priceString);
        AbsoluteSizeSpan sizeSpan01 = new AbsoluteSizeSpan(12, true);
        AbsoluteSizeSpan sizeSpan02 = new AbsoluteSizeSpan(36, true);
        AbsoluteSizeSpan sizeSpan03 = new AbsoluteSizeSpan(14, true);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, priceString.length() - 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan03, priceString.length() - 3, priceString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvPrice.setText("");
        tvPrice.append(spannableString);

        pics = Generater.GeneratePicResId();
        ImageScrollAdapter adapter = new ImageScrollAdapter(this, Generater.GeneratePicResId());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                setCurrent(position);
            }
        });
        setCurrent(0);
    }

    private void setCurrent(int position) {
        String priceString = position + 1 + "/" + pics.size();
        SpannableString spannableString = new SpannableString(priceString);
        AbsoluteSizeSpan sizeSpan01 = new AbsoluteSizeSpan(18, true);
        AbsoluteSizeSpan sizeSpan02 = new AbsoluteSizeSpan(13, true);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, priceString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvCurrent.setText("");
        tvCurrent.append(spannableString);
    }

    private void dissmissOverlay() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                final ObjectAnimator animator = ObjectAnimator.ofFloat(llOverlay, "alpha", 1.0f, 0);
                animator.setDuration(800);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (llOverlay != null)
                            llOverlay.setVisibility(View.GONE);
                    }
                });
                animator.start();
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_parameter_choice:
            case R.id.btn_buy:
                if(choiceParameterDialog==null){
                    choiceParameterDialog = new ChoiceParameterDialog(this, Generater.GenerateParam());
                }
                choiceParameterDialog.show();
                break;
            case R.id.iv_back:
            case R.id.iv_back_:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else {
                    finish();
                }
                break;
        }
    }
}
