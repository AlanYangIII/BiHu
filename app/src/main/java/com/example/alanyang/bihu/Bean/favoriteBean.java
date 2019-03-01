package com.example.alanyang.bihu.Bean;

import java.util.List;

public class favoriteBean {

    /**
     * status : 200
     * info : success
     * data : {"totalCount":2,"totalPage":1,"questions":[{"id":1,"title":"欢迎来到逼乎","content":"让我们在撕逼中寻找答案","images":"http://ok4qp4ux0.bkt.clouddn.com/logo.png,http://ok4qp4ux0.bkt.clouddn.com/1485064307258","date":"2017-12-26 16:41:52","exciting":0,"naive":0,"recent":"2017-12-26 16:57:10","answerCount":2,"authorId":2,"authorName":"Jay","authorAvatar":"http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg","is_exciting":false,"is_naive":false}],"curPage":0}
     */

    private int status;
    private String info;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * totalCount : 2
         * totalPage : 1
         * questions : [{"id":1,"title":"欢迎来到逼乎","content":"让我们在撕逼中寻找答案","images":"http://ok4qp4ux0.bkt.clouddn.com/logo.png,http://ok4qp4ux0.bkt.clouddn.com/1485064307258","date":"2017-12-26 16:41:52","exciting":0,"naive":0,"recent":"2017-12-26 16:57:10","answerCount":2,"authorId":2,"authorName":"Jay","authorAvatar":"http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg","is_exciting":false,"is_naive":false}]
         * curPage : 0
         */

        private int totalCount;
        private int totalPage;
        private int curPage;
        private List<QuestionsBean> questions;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public List<QuestionsBean> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionsBean> questions) {
            this.questions = questions;
        }

        public static class QuestionsBean {
            /**
             * id : 1
             * title : 欢迎来到逼乎
             * content : 让我们在撕逼中寻找答案
             * images : http://ok4qp4ux0.bkt.clouddn.com/logo.png,http://ok4qp4ux0.bkt.clouddn.com/1485064307258
             * date : 2017-12-26 16:41:52
             * exciting : 0
             * naive : 0
             * recent : 2017-12-26 16:57:10
             * answerCount : 2
             * authorId : 2
             * authorName : Jay
             * authorAvatar : http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg
             * is_exciting : false
             * is_naive : false
             */

            private int id;
            private String title;
            private String content;
            private String images;
            private String date;
            private int exciting;
            private int naive;
            private String recent;
            private int answerCount;
            private int authorId;
            private String authorName;
            private String authorAvatar;
            private boolean is_exciting;
            private boolean is_naive;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getExciting() {
                return exciting;
            }

            public void setExciting(int exciting) {
                this.exciting = exciting;
            }

            public int getNaive() {
                return naive;
            }

            public void setNaive(int naive) {
                this.naive = naive;
            }

            public String getRecent() {
                return recent;
            }

            public void setRecent(String recent) {
                this.recent = recent;
            }

            public int getAnswerCount() {
                return answerCount;
            }

            public void setAnswerCount(int answerCount) {
                this.answerCount = answerCount;
            }

            public int getAuthorId() {
                return authorId;
            }

            public void setAuthorId(int authorId) {
                this.authorId = authorId;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getAuthorAvatar() {
                return authorAvatar;
            }

            public void setAuthorAvatar(String authorAvatar) {
                this.authorAvatar = authorAvatar;
            }

            public boolean isIs_exciting() {
                return is_exciting;
            }

            public void setIs_exciting(boolean is_exciting) {
                this.is_exciting = is_exciting;
            }

            public boolean isIs_naive() {
                return is_naive;
            }

            public void setIs_naive(boolean is_naive) {
                this.is_naive = is_naive;
            }
        }
    }
}
