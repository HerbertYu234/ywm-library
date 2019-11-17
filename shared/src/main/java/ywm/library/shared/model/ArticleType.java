package ywm.library.shared.model;

/**
 * Created by Herbert Yu on 2019-11-12 21:44
 */
public enum ArticleType {

    EMOTION("情感类"),
    SCIENCES("社科类");

    private String desc;

    ArticleType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
