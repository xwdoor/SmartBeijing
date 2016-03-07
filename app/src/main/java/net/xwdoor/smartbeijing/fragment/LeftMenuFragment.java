package net.xwdoor.smartbeijing.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.xwdoor.smartbeijing.MainActivity;
import net.xwdoor.smartbeijing.R;
import net.xwdoor.smartbeijing.domain.NewsMenu;

import java.util.List;

/**
 * Created by XiaoWei on 2016/1/20 020.
 */
public class LeftMenuFragment extends BaseFragment {

    private ListView lv_list;
    private List<NewsMenu.NewsMenuData> mNewsMenuData;
    private int mCurrentPos;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        lv_list = (ListView) view.findViewById(R.id.lv_list);
        return view;
    }

    @Override
    public void initData() {

    }

    public void setMenuData(List<NewsMenu.NewsMenuData> data) {
        this.mNewsMenuData = data;
        final MenuDataAdapter adapter = new MenuDataAdapter();
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                adapter.notifyDataSetChanged();

                setCurrentDetailPager(position);
                toggle();
            }
        });
        mCurrentPos = 0;
    }

    private void setCurrentDetailPager(int position) {
        ((MainActivity)mActivity).getContentFragment().getNewsCenterPager().setCurrentDetailPager(position);

    }

    private void toggle() {
        ((MainActivity)mActivity).getSlidingMenu().toggle();
    }

    class MenuDataAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView tvMenuTitle = (TextView) view.findViewById(R.id.tv_menu_title);

            NewsMenu.NewsMenuData item = getItem(position);
            tvMenuTitle.setText(item.title);

            //设置选中效果
            if(mCurrentPos == position){
                tvMenuTitle.setEnabled(true);
            }else {
                tvMenuTitle.setEnabled(false);
            }

            return view;
        }
    }
}
