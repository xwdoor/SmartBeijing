package net.xwdoor.smartbeijing.pager.detailPager;

import android.app.Activity;
import android.view.View;

/**
 * Created by XiaoWei on 2016/1/25 025.
 */
public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        this.mActivity = activity;
        this.mRootView = initView();
    }

    public abstract View initView();

    public abstract void initData();
}
