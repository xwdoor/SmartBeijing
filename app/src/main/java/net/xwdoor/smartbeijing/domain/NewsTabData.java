package net.xwdoor.smartbeijing.domain;

import java.util.List;

/**
 * Created by XiaoWei on 2016/1/26 026.
 */
public class NewsTabData {

    public DataEntity data;

    public class DataEntity {
        public String more;
        public String title;
        public List<TabNewsEntity> news;

        public List<TopNewsEntity> topnews;


    }

    public class TabNewsEntity {
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

    }

    public class TopNewsEntity {
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public int id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;


    }
}
