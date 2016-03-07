package net.xwdoor.smartbeijing.domain;

import java.util.List;

/**
 * Created by XiaoWei on 2016/2/1 001.
 */
public class PhotosBean {



    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PhotosBean{" +
                "data=" + data +
                '}';
    }

    public static class DataEntity {
        /**
         * comment : true
         * commentlist : http://zhbj.qianlong.com/static/api/news/10003/72/82772/comment_1.json
         * commenturl : http://zhbj.qianlong.com/client/user/newComment/82772
         * id : 82772
         * largeimage : http://zhbj.qianlong.com/static/images/2014/11/07/70/476518773M7R.jpg
         * listimage : http://10.0.2.2:8080/zhbj/photos/images/46728356JDGO.jpg
         * pubdate : 2014-11-07 11:40
         * smallimage : http://zhbj.qianlong.com/static/images/2014/11/07/79/485753989TVL.jpg
         * title : 北京·APEC绚丽之夜
         * type : news
         * url : http://zhbj.qianlong.com/static/html/2014/11/07/7743665E4E6B10766F26.html
         */

        private List<NewsEntity> news;

        public void setNews(List<NewsEntity> news) {
            this.news = news;
        }

        public List<NewsEntity> getNews() {
            return news;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "news=" + news +
                    '}';
        }

        public static class NewsEntity {
            private int id;
            private String listimage;
            private String title;

            public void setId(int id) {
                this.id = id;
            }

            public void setListimage(String listimage) {
                this.listimage = listimage;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getId() {
                return id;
            }

            public String getListimage() {
                return listimage;
            }

            public String getTitle() {
                return title;
            }

            @Override
            public String toString() {
                return "NewsEntity{" +
                        "title='" + title + '\'' +
                        '}';
            }
        }
    }
}
