package net.xwdoor.smartbeijing.pager.detailPager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import net.xwdoor.smartbeijing.MainActivity;
import net.xwdoor.smartbeijing.R;
import net.xwdoor.smartbeijing.domain.NewsMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单详情页-新闻
 * Created by XiaoWei on 2016/1/25 025.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager vpNewsDetail;
    private List<NewsMenu.NewsTabDataEntity> tabDatas;
    private List<TabDetailPager> tabDetailPagers;
    private TabPageIndicator mIndicator;
    private ImageButton btnNext;

    public NewsMenuDetailPager(Activity activity, List<NewsMenu.NewsTabDataEntity> children) {
        super(activity);
        this.tabDatas = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.detail_pager_news_menu,null);
        vpNewsDetail = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
        mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        btnNext = (ImageButton) view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = vpNewsDetail.getCurrentItem();
                item++;
                vpNewsDetail.setCurrentItem(item);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        tabDetailPagers = new ArrayList<TabDetailPager>();
        for(int i=0;i<tabDatas.size();i++){
            TabDetailPager tabDetailPager = new TabDetailPager(mActivity,tabDatas.get(i));
            tabDetailPagers.add(tabDetailPager);
        }

        vpNewsDetail.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(vpNewsDetail);//将viewpager与指示器绑定，必须在viewPager设置完数据后再绑定

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("123123", "当前位置：" + position);
                if (position == 0) {
                    setSlidingMenuEnable(true);
                } else {
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSlidingMenuEnable(boolean isEnable) {
        SlidingMenu slidingMenu = ((MainActivity) mActivity).getSlidingMenu();
        if(isEnable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class NewsMenuDetailAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager detailPager = tabDetailPagers.get(position);

            View view = detailPager.mRootView;
            detailPager.initData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabDataEntity newsTabDataEntity = tabDatas.get(position);
            return newsTabDataEntity.title;
        }
    }
}
