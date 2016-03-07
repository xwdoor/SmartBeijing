package net.xwdoor.smartbeijing.pager.detailPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import net.xwdoor.smartbeijing.NewsDetailActivity;
import net.xwdoor.smartbeijing.R;
import net.xwdoor.smartbeijing.config.GlobalConstants;
import net.xwdoor.smartbeijing.domain.NewsMenu;
import net.xwdoor.smartbeijing.domain.NewsTabData;
import net.xwdoor.smartbeijing.utils.CacheUtils;
import net.xwdoor.smartbeijing.utils.PrefUtils;
import net.xwdoor.smartbeijing.view.PullToRefreshListView;

import java.util.List;

/**
 * Created by XiaoWei on 2016/1/26 026.
 */
public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenu.NewsTabDataEntity tabData;
    private TextView textView;
    private ViewPager vpTopNews;
    private String mUrl;
    private List<NewsTabData.TopNewsEntity> mTopNews;
    private List<NewsTabData.TabNewsEntity> mListNews;

    private TextView tvTitle;
    private CirclePageIndicator mIndicator;
    private PullToRefreshListView lvList;
    private ListNewsAdapter mListNewsAdapter;
    private String mMoreUrl;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabDataEntity newsTabDataEntity) {
        super(activity);
        tabData = newsTabDataEntity;

        mUrl = GlobalConstants.SERVER_URL + tabData.url;
//        Log.d("123123", mUrl);
    }

    @Override
    public View initView() {
//        textView = new TextView(mActivity);
//        textView.setText(tabData.title);//报空指针
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(22);
//        textView.setGravity(Gravity.CENTER);

        View view = View.inflate(mActivity, R.layout.detail_pager_tab_news, null);
        lvList = (PullToRefreshListView) view.findViewById(R.id.lv_list);

        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        vpTopNews = (ViewPager) mHeaderView.findViewById(R.id.vp_top_news);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        mIndicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.indicator);

        lvList.addHeaderView(mHeaderView);
        lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(true);
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewCount = lvList.getHeaderViewsCount();
                position = position - headerViewCount;
                Log.d("123123", "第" + position + "个Item被点击了");

                NewsTabData.TabNewsEntity newsEntity = mListNews.get(position);

                String readIds = PrefUtils.getString(mActivity, "read_ids", "");

                if (!readIds.contains(newsEntity.id + "")) {
                    readIds += newsEntity.id + ",";
                    PrefUtils.setString(mActivity, "read_ids", readIds);
                }

                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", newsEntity.url);
                mActivity.startActivity(intent);
            }
        });

        //返回错误，写成返回 vpTopNews 了
        //报错： java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.

        return view;
    }

    private void getMoreDataFromServer() {
        com.lidroid.xutils.HttpUtils utils = new com.lidroid.xutils.HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                CacheUtils.setCache(mActivity, mUrl, result);
                processData(result, true);

                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d("123123", error.getMessage());
                Log.d("123123", msg);
                lvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
//        textView.setText(tabData.title);//报空指针
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            Log.d("123123", "发现缓存图片...");
            processData(cache, false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        com.lidroid.xutils.HttpUtils utils = new com.lidroid.xutils.HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.setCache(mActivity, mUrl, result);
                processData(result, false);

                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d("123123", error.getMessage());
                Log.d("123123", msg);
                lvList.onRefreshComplete(false);
            }
        });
    }

    private Handler mHandler;

    private void processData(String result, boolean isMore) {
        Log.d("123123", "处理数据");

        Gson gson = new Gson();
        NewsTabData newsTabData = gson.fromJson(result, NewsTabData.class);

        String moreUrl = newsTabData.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
        } else {
            mMoreUrl = null;
        }


        if (isMore) {
            //加载更多数据
            List<NewsTabData.TabNewsEntity> moreNews = newsTabData.data.news;
            mListNews.addAll(moreNews);
            mListNewsAdapter.notifyDataSetChanged();
        } else {
            mTopNews = newsTabData.data.topnews;
            if (mTopNews != null) {
                vpTopNews.setAdapter(new TopNewsAdatapter());
                mIndicator.setViewPager(vpTopNews);
                mIndicator.setSnap(true);

                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        tvTitle.setText(mTopNews.get(position).title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                tvTitle.setText(mTopNews.get(0).title);

                vpTopNews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int item = vpTopNews.getCurrentItem();
                        Log.d("123123", "item--->"+item);
                    }
                });

            }

            mListNews = newsTabData.data.news;
            if (mListNews != null) {
                mListNewsAdapter = new ListNewsAdapter();
                lvList.setAdapter(mListNewsAdapter);
            }

            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = vpTopNews.getCurrentItem();
                        currentItem++;
                        if (currentItem > mTopNews.size() - 1) {
                            currentItem = 0;
                        }
                        vpTopNews.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0, 3000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 3000);

                vpTopNews.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                        }
                        return false;
                    }
                });
            }
        }


    }


    class TopNewsAdatapter extends PagerAdapter {

        private BitmapUtils mBitmapUtils;

        public TopNewsAdatapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView view = new ImageView(mActivity);
//            view.setImageResource(R.mipmap.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            String imageUrl = mTopNews.get(position).topimage;
            mBitmapUtils.display(view, imageUrl);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("123123", "头条新闻被点击了");
                    String url = mTopNews.get(position).url;
                    NewsDetailActivity.startAty(mActivity,url);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ListNewsAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        public ListNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mListNews.size();
        }

        @Override
        public NewsTabData.TabNewsEntity getItem(int position) {
            return mListNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);

                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            NewsTabData.TabNewsEntity newsEntity = getItem(position);
            holder.tvTitle.setText(newsEntity.title);
            holder.tvDate.setText(newsEntity.pubdate);

            String readIds = PrefUtils.getString(mActivity, "read_ids", "");

            if (readIds.contains(newsEntity.id + "")) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            mBitmapUtils.display(holder.ivIcon, newsEntity.listimage);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDate;
    }
}
