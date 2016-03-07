package net.xwdoor.smartbeijing.pager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import net.xwdoor.smartbeijing.MainActivity;
import net.xwdoor.smartbeijing.config.GlobalConstants;
import net.xwdoor.smartbeijing.domain.NewsMenu;
import net.xwdoor.smartbeijing.fragment.LeftMenuFragment;
import net.xwdoor.smartbeijing.pager.detailPager.BaseMenuDetailPager;
import net.xwdoor.smartbeijing.pager.detailPager.InteractMenuDetailPager;
import net.xwdoor.smartbeijing.pager.detailPager.NewsMenuDetailPager;
import net.xwdoor.smartbeijing.pager.detailPager.PhotosMenuDetailPager;
import net.xwdoor.smartbeijing.pager.detailPager.TopicMenuDetailPager;
import net.xwdoor.smartbeijing.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoWei on 2016/1/20 020.
 */
public class NewsCenterPager extends BasePager {

    private static final int REQUEST_COMPLETE = 0;
    private static final int REQUEST_FAILURE = 1;

    private List<BaseMenuDetailPager> detailPagers;
    private NewsMenu newsMenu;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("123123", "新闻中心初始化");
//        TextView textView = new TextView(mActivity);
//        textView.setText("新闻中心");
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(22);
//        textView.setGravity(Gravity.CENTER);
//
//        flContent.addView(textView);

        tvTitle.setText("新闻中心");
        btnMenu.setVisibility(View.VISIBLE);

        String json = CacheUtils.getCache(mActivity,GlobalConstants.CATEGORY_URL);
        if(!TextUtils.isEmpty(json)){
            Log.d("123123","发现缓存数据...");
            processData(json);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {

        com.lidroid.xutils.HttpUtils utils = new com.lidroid.xutils.HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.e("123123", result);
                CacheUtils.setCache(mActivity, GlobalConstants.CATEGORY_URL,result);
                processData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                Log.e("123123", "onFailure--->"+e.getMessage());
                Log.e("123123","onFailure--->"+s);
            }
        });

//        HttpUtils.doGetAsyn(GlobalConstants.CATEGORY_URL, new HttpUtils.CallBack() {
//            @Override
//            public void onRequestComplete(String result) {
//                Log.e("123123", result);
//                CacheUtils.setCache(mActivity,GlobalConstants.CATEGORY_URL,result);
//                Message msg = new Message();
//                msg.what = REQUEST_COMPLETE;
//                msg.obj = result;
//                handler.sendMessage(msg);//线程中无法更新UI
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e("123123", e.getMessage());
//                Message msg = new Message();
//                msg.what = REQUEST_FAILURE;
//                msg.obj = e;
//                handler.sendMessage(msg);
//            }
//        });

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUEST_COMPLETE:
                    String json = (String) msg.obj;
                    processData(json);
                    break;
                case REQUEST_FAILURE:
                    Exception e = (Exception) msg.obj;
                    Log.e("123123",e.getMessage());
                    break;
            }
        }
    };

    private void processData(String json) {
        Gson gson = new Gson();
        newsMenu = gson.fromJson(json, NewsMenu.class);
//        Log.e("123123", newsMenu.toString());

        LeftMenuFragment leftMenuFragment = ((MainActivity)mActivity).getLeftMenuFragment();
        leftMenuFragment.setMenuData(newsMenu.data);

        detailPagers = new ArrayList<>();
        detailPagers.add(new NewsMenuDetailPager(mActivity,newsMenu.data.get(0).children));
        detailPagers.add(new TopicMenuDetailPager(mActivity));
        detailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
        detailPagers.add(new InteractMenuDetailPager(mActivity));

        setCurrentDetailPager(0);
    }

    public void setCurrentDetailPager(int position) {
        BaseMenuDetailPager menuDetailPager = detailPagers.get(position);

        flContent.removeAllViews();
        flContent.addView(menuDetailPager.mRootView);
// 初始化页面数据
        menuDetailPager.initData();
        tvTitle.setText(newsMenu.data.get(position).title);

        if(menuDetailPager instanceof PhotosMenuDetailPager){
            btnPhoto.setVisibility(View.VISIBLE);
        }else {
            btnPhoto.setVisibility(View.INVISIBLE);
        }
    }
}
