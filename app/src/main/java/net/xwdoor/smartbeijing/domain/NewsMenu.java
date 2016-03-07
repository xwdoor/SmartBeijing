package net.xwdoor.smartbeijing.domain;

import java.util.List;

/**
 * Created by XiaoWei on 2016/1/25 025.
 */
public class NewsMenu {
    public int retcode;


    public List<NewsMenuData> data;
    public List<Integer> extend;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }

    //侧边栏菜单
    public class NewsMenuData {
        public int id;
        public String title;
        public int type;

        public List<NewsTabDataEntity> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    //新闻标签页
    public class NewsTabDataEntity {
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabDataEntity{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
