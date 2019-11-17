package ywm.library.shared.model;

/**
 * Created by Herbert Yu on 2019-11-12 21:44
 */
public enum CommentType {

    ARTICLE("文章评论"),
    SITE("网站评论");

    private String desc;

    CommentType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
