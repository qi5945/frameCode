package com.micropole.chebianjie.test;

import java.io.Serializable;

/**
 * @ClassName PostEntity
 * @Model todo
 * @Description 帖子
 * @Author chen qi hao
 * @Sign 沉迷学习不能自拔
 * @Date 2019/3/22 10:22
 * @Email 371232886@qq.com
 * @Copyright Guangzhou micro pole mobile Internet Technology Co., Ltd.
 */
public class PostEntity implements Serializable {

    /**
     * article_id : 1
     * user_id : 2
     * title : 本期大概率开奖号码
     * desc : 不容错过
     * content : 根据长期的规律以及跟踪，这是跑不了的了！！！
     * image : http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&hs=0&pn=20&spn=0&di=33321295040&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=2492013562%2C1239416617&os=1439034206%2C1408955886&simid=429087
     * is_top : 0
     * is_vip : 1
     * system_id : 3
     * browse_num : 206
     * praise_num : 102
     * comment_num : 50
     * create_time : 2019-03-22 16:09:20
     * update_time : 2019-03-22 16:09:20
     * reviewer_time :
     * article_to_user : {"id":2,"name":"陈先生","head":"http://image.baidu.com/search/detail?ct=503316480&amp;z=0&amp;ipn=d&amp;word=%E5%9B%BE%E7%89%87&amp;hs=0&amp;pn=20&amp;spn=0&amp;di=33321295040&amp;pi=0&amp;rn=1&amp;tn=baiduimagedetail&amp;is=0%2C0&a","gold":"9987"}
     * article_to_gold : {"system_id":3,"gold":"5"}
     */

    private String article_id;
    private String user_id;
    private String title;
    private String desc;
    private String content;
    private String image;
    private String is_top;
    private String is_vip;
    private String system_id;
    private String browse_num;
    private String praise_num;
    private String comment_num;
    private String create_time;
    private String update_time;
    private String reviewer_time;
    private ArticleToUserBean article_to_user;
//    private ArticleToGoldBean article_to_gold;
    private String article_gold;

    public String getArticle_gold() {
        return article_gold;
    }

    public void setArticle_gold(String article_gold) {
        this.article_gold = article_gold;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getBrowse_num() {
        return browse_num;
    }

    public void setBrowse_num(String browse_num) {
        this.browse_num = browse_num;
    }

    public String getPraise_num() {
        return praise_num;
    }

    public void setPraise_num(String praise_num) {
        this.praise_num = praise_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getReviewer_time() {
        return reviewer_time;
    }

    public void setReviewer_time(String reviewer_time) {
        this.reviewer_time = reviewer_time;
    }

    public ArticleToUserBean getArticle_to_user() {
        return article_to_user;
    }

    public void setArticle_to_user(ArticleToUserBean article_to_user) {
        this.article_to_user = article_to_user;
    }

//    public ArticleToGoldBean getArticle_to_gold() {
//        return article_to_gold;
//    }
//
//    public void setArticle_to_gold(ArticleToGoldBean article_to_gold) {
//        this.article_to_gold = article_to_gold;
//    }

    public static class ArticleToUserBean {
        /**
         * id : 2
         * name : 陈先生
         * head : http://image.baidu.com/search/detail?ct=503316480&amp;z=0&amp;ipn=d&amp;word=%E5%9B%BE%E7%89%87&amp;hs=0&amp;pn=20&amp;spn=0&amp;di=33321295040&amp;pi=0&amp;rn=1&amp;tn=baiduimagedetail&amp;is=0%2C0&a
         * gold : 9987
         */

        private int id;
        private String name;
        private String head;
        private String gold;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }
    }

    public static class ArticleToGoldBean {
        /**
         * system_id : 3
         * gold : 5
         */

        private int system_id;
        private String gold;

        public int getSystem_id() {
            return system_id;
        }

        public void setSystem_id(int system_id) {
            this.system_id = system_id;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }
    }
}
