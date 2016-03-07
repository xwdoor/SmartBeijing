package net.xwdoor.smartbeijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.xwdoor.smartbeijing.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XiaoWei on 2016/1/29 029.
 */
public class PullToRefreshListView extends ListView {

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private static int mCurrentState = STATE_PULL_TO_REFRESH;

    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private int mHeaderViewHeight;
    private int startY = -1;
    private View mHeaderView;
    private RotateAnimation rotateAnimUp;
    private RotateAnimation rotateAnimDown;
    private View mFootView;
    private int mFootViewHeight;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFootView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFootView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFootView();
    }

    public void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        initAnimation();
        setCurrentTime();
    }

    private boolean isLoadingMore = false;
    private void initFootView(){
        mFootView = View.inflate(getContext(), R.layout.pull_to_refresh_foot, null);
        this.addFooterView(mFootView);

        mFootView.measure(0, 0);
        mFootViewHeight = mFootView.getMeasuredHeight();
        mFootView.setPadding(0, -mFootViewHeight, 0, 0);

        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {//空闲状态

                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition == getCount() - 1 && !isLoadingMore) {
                        Log.d("123123","加载更多...");
                        mFootView.setPadding(0,0,0,0);

                        setSelection(getCount()-1);
                        isLoadingMore=true;

                        if(mListener!=null){
                            mListener.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 刷新结束，收起面板
     */
    public void onRefreshComplete(boolean success) {

        if(isLoadingMore){
            mFootView.setPadding(0,-mFootViewHeight,0,0);
            isLoadingMore = false;
        }else {
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;

            tvTitle.setText("下拉刷新");

            ivArrow.clearAnimation();
            ivArrow.setVisibility(VISIBLE);
            pbProgress.setVisibility(INVISIBLE);
            if (success) {
                setCurrentTime();
            }
        }


    }

    public void setCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(new Date());

        tvTime.setText(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,
                    // 导致startY没有赋值,此处需要重新获取一下
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                int endY = (int) ev.getY();
                int dy = endY - startY;
                int firstVisiblePosition = getFirstVisiblePosition();

                if (dy > 0 && firstVisiblePosition == 0) {
                    int paddingTop = dy - mHeaderViewHeight;

                    mHeaderView.setPadding(0, paddingTop, 0, 0);

                    if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    } else if (paddingTop > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initAnimation() {
        rotateAnimUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimUp.setDuration(200);
        rotateAnimUp.setFillAfter(true);

        rotateAnimDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimDown.setDuration(200);
        rotateAnimDown.setFillAfter(true);
    }

    private void refreshState() {

        ivArrow.setVisibility(VISIBLE);
        pbProgress.setVisibility(INVISIBLE);

        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.startAnimation(rotateAnimDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.startAnimation(rotateAnimUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");

                ivArrow.clearAnimation();
                ivArrow.setVisibility(INVISIBLE);
                pbProgress.setVisibility(VISIBLE);
                break;
        }
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}
