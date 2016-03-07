package net.xwdoor.smartbeijing.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import net.xwdoor.smartbeijing.MainActivity;
import net.xwdoor.smartbeijing.R;

/**
 * Created by XiaoWei on 2016/1/20 020.
 */
public class BasePager  {

    public Activity mActivity;
    public View rootView;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent;
    public ImageButton btnPhoto;

    public BasePager(Activity activity) {
        this.mActivity = activity;
        rootView = initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager,null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
        return view;
    }

    public void initData(){
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mActivity).getSlidingMenu().toggle();
            }
        });
    }

}
