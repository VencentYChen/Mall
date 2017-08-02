package com.wangku.demo.mall;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llGoods;
    private ImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llGoods = (LinearLayout) findViewById(R.id.ll_goods);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
        llGoods.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_goods:
                ActivityOptions options = null;
                Intent intent = new Intent(this, GoodsDetailActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(this,
                            ivPic, "imageView");
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
        }
    }
}
