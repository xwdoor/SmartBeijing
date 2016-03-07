package net.xwdoor.smartbeijing.pager.detailPager;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.xwdoor.smartbeijing.R;
import net.xwdoor.smartbeijing.config.GlobalConstants;
import net.xwdoor.smartbeijing.domain.PhotosBean;
import net.xwdoor.smartbeijing.utils.PrefUtils;

import java.util.List;

/**
 * 菜单详情页-组图
 * Created by XiaoWei on 2016/1/25 025.
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {

    private final ImageButton btnPhoto;
    @ViewInject(R.id.lv_photo)
    private ListView lv_photo;
    @ViewInject(R.id.gv_photo)
    private GridView gv_photo;
    private List<PhotosBean.DataEntity.NewsEntity> mPhotoNews;

    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto = btnPhoto;
        btnPhoto.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.detail_pager_photo_menu, null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {

        String cache = PrefUtils.getString(mActivity,GlobalConstants.PHOTOS_URL,"");
        if(!TextUtils.isEmpty(cache)){
            proeecssData(cache);
        }

        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils xUtils = new HttpUtils();
        xUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                Log.d("123123",result);
                proeecssData(result);

                PrefUtils.setString(mActivity,GlobalConstants.PHOTOS_URL,result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void proeecssData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
        Log.d("123123","解析结果--->"+photosBean);

        mPhotoNews = photosBean.getData().getNews();
//        PhotoAdapter photoAdapter = new PhotoAdapter();
        lv_photo.setAdapter(new PhotoAdapter());
        gv_photo.setAdapter(new PhotoAdapter());
    }

    private boolean isListView = true;
    @Override
    public void onClick(View v) {
        if(isListView){
            isListView = false;
            lv_photo.setVisibility(View.GONE);
            gv_photo.setVisibility(View.VISIBLE);

            btnPhoto.setImageResource(R.mipmap.icon_pic_list_type);
        }else {
            isListView = true;
            lv_photo.setVisibility(View.VISIBLE);
            gv_photo.setVisibility(View.GONE);

            btnPhoto.setImageResource(R.mipmap.icon_pic_grid_type);
        }
    }

    class PhotoAdapter extends BaseAdapter{

        private final BitmapUtils bitmapUtils;

        public PhotoAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mPhotoNews.size();
        }

        @Override
        public PhotosBean.DataEntity.NewsEntity getItem(int position) {
            return mPhotoNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = View.inflate(mActivity,R.layout.list_item_photo,null);

                viewHolder = new ViewHolder();
                viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PhotosBean.DataEntity.NewsEntity item = getItem(position);
            viewHolder.tvTitle.setText(item.getTitle());

            bitmapUtils.display(viewHolder.ivPic,item.getListimage());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView ivPic;
        TextView tvTitle;
    }
}
