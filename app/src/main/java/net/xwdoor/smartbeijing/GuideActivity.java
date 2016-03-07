package net.xwdoor.smartbeijing;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.xwdoor.smartbeijing.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private List<ImageView> imageViewList = new ArrayList<>();
    private int[] imageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private LinearLayout llContainer;
    private ImageView redPoint;
    private int pointDis;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        redPoint = (ImageView) findViewById(R.id.iv_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);

        initData();
        viewPager.setAdapter(new GuideAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.d("123123", "当前位置:" + position + ";移动偏移百分比:"
                        + positionOffset);

                int leftMargin = (int) (pointDis * positionOffset + position * pointDis);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                redPoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageViewList.size() - 1) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft()
        // - llContainer.getChildAt(0).getLeft();
        // System.out.println("圆点距离:" + mPointDis);

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                redPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                redPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                pointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
                Log.d("123123", "圆点距离:" + pointDis + ",1--->" + llContainer.getChildAt(0).getLeft()
                        + ",2--->" + llContainer.getChildAt(1).getLeft()
                        + ",红点--->" + redPoint.getLeft());
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setBoolean(getApplicationContext(), "is_first_enter", false);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //初始化数据
    private void initData() {
        //布局参数不能写在这里，一个控件分别new一个布局参数，不然就是一处修改，处处修改
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < imageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(imageIds[i]);
            imageViewList.add(view);

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shap_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = imageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
