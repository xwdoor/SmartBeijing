package net.xwdoor.smartbeijing.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import net.xwdoor.smartbeijing.MainActivity;
import net.xwdoor.smartbeijing.R;
import net.xwdoor.smartbeijing.pager.BasePager;
import net.xwdoor.smartbeijing.pager.GovAffairsPager;
import net.xwdoor.smartbeijing.pager.HomePager;
import net.xwdoor.smartbeijing.pager.NewsCenterPager;
import net.xwdoor.smartbeijing.pager.SettingPager;
import net.xwdoor.smartbeijing.pager.SmartServicePager;
import net.xwdoor.smartbeijing.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoWei on 2016/1/20 020.
 */
public class ContentFragment extends BaseFragment {

    private NoScrollViewPager viewPager;
    private List<BasePager> pagers;
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        viewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        pagers = new ArrayList<>();

        pagers.add(new HomePager(mActivity));
        pagers.add(new NewsCenterPager(mActivity));
        pagers.add(new SmartServicePager(mActivity));
        pagers.add(new GovAffairsPager(mActivity));
        pagers.add(new SettingPager(mActivity));

        viewPager.setAdapter(new ContentAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position).initData();
                if (position == 0 || position == pagers.size() - 1) {
                    setSlidingMenuEnable(false);
                } else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        viewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        viewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });

        rgGroup.check(R.id.rb_home);
        pagers.get(0).initData();
        setSlidingMenuEnable(false);
    }

    private void setSlidingMenuEnable(boolean isEnable) {
        SlidingMenu slidingMenu = ((MainActivity) mActivity).getSlidingMenu();
        if(isEnable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = pagers.get(position);
//            pager.initData();//节约流量与资源，不在这里初始化数据，因为viewpager会预加载下一项
            container.addView(pager.rootView);
            return pager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) pagers.get(1);
    }
}
